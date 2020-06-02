#!/bin/sh

WORKSPACE_DIR=${PWD}/workspace

#------------------------------------------------------------------------------
# FUNCTIONS
#------------------------------------------------------------------------------
workspace() {
    echo "${WORKSPACE_DIR}"
    if [ ! -d "${WORKSPACE_DIR}" ]; then
        mkdir -p "${WORKSPACE_DIR}"
    fi

    cd "${WORKSPACE_DIR}"
    if [ ! -d "${1}" ]; then
        git clone "https://git-codecommit.us-west-2.amazonaws.com/v1/repos/${1}"
        if [ $? != 0 ]; then
            exit
        fi
    fi

    cd ${1}
}

#------------------------------------------------------------------------------
pushALL() {
    rebaseApp letlock-backend dev qa master
}

pushQA() {
    rebaseApp letlock-backend dev qa
}

pushPROD() {
    rebaseApp letlock-backend qa master
}

rebaseEnv() {
    local SRC_BRANCH=${1}
    local DEST_BRANCH=${2}

    echo "--------------------------------------------------------------------"
    echo "Rebase ${DEST_BRANCH}"
    echo "--------------------------------------------------------------------"
    git co ${DEST_BRANCH}
    if [ $? != 0 ]; then
        exit
    fi

    git pull
    if [ $? != 0 ]; then
        exit
    fi

    git rebase ${SRC_BRANCH}
        if [ $? != 0 ]; then
        exit
    fi

    git push
    if [ $? != 0 ]; then
        exit
    fi
}

rebaseApp() {
    local APP=${1}
    local SRC_BRANCH=${2}

    echo
    echo "--------------------------------------------------------------------"
    echo "App ${APP}"
    echo "--------------------------------------------------------------------"

    workspace ${APP}
    git co ${SRC_BRANCH};
    if [ $? != 0 ]; then
        exit
    fi

    git pull
    if [ $? != 0 ]; then
        exit
    fi

    shift
    shift
    while [ "${1}" != "" ]; do
        rebaseEnv ${SRC_BRANCH} ${1}
        shift
    done
}


#------------------------------------------------------------------------------
# MAIN
#------------------------------------------------------------------------------
echo
echo "Welcome to the environment deployment script. Options are:"
echo "  qa:   push dev to qa"
echo "  prd: push qa  to prod"
echo "  all:  push main development branch to qa, prd"
echo

read -p "What would you like to do (qa/prd/all): " input

case ${input} in
    qa)   pushQA;;
    prd) pushPROD;;
    all)  pushALL;;
    *) echo "Unknown answer."
esac