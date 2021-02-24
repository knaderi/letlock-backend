package com.landedexperts.letlock.filetransfer.backend.session;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.landedexperts.letlock.filetransfer.backend.BackendConstants.USER_ID;
import static com.landedexperts.letlock.filetransfer.backend.BackendConstants.TEMP_TOKEN_PREFIX;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.utils.ResponseCode;

@Component
public class AuthenticationFilter extends HttpFilter {

    private static final long serialVersionUID = 1L;
    private static final List<String> openEndpoints = AuthenticationManager.getInstance().getOpenEndpoints();
    private static final List<String> adminEndpoints = AuthenticationManager.getInstance().getAdminEndpoints();

    @Override
    protected void doFilter(final HttpServletRequest req, final HttpServletResponse res, final FilterChain chain) throws IOException, ServletException {

        String path = req.getServletPath();
        if (path == null || path.isEmpty()) { // for tests
            path = req.getRequestURI();
        }
        
        if (authenticationRequired(path)) {
            // Authenticate
            long userId = getUserId(req);
            if (userId == -1) {
                setResponse(res, ResponseCode.TOKEN_INVALID);
                return;
            }
            
            // Authorize
            if (!userHasPermissions(path, userId)) {
                setResponse(res, ResponseCode.ADMIN_USER_EXPECTED);
                return;
            }

            req.setAttribute(USER_ID, userId);
        }
        
        chain.doFilter(req, res);
    }

    private Boolean authenticationRequired (final String path) {
        return !openEndpoints.contains(path);
    }
    
    private long getUserId (final HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        token = StringUtils.isBlank(token) ? "" : token.replace("Bearer ", "");
        long userId = -1;
        if (token.startsWith(TEMP_TOKEN_PREFIX)) {
            userId = TwoFAManager.getInstance().getUserId(token);
        } else {
            userId = SessionManager.getInstance().getUserId(token);
        }
        return userId;
    }
    
    private Boolean userHasPermissions (final String path, final long userId) {
        //  Check if admin privileges are required for the endpoint and if the user is admin (userId = 1)
        // TODO: Move settings to DB and check user's roles later
        return !(adminEndpoints.contains(path) && userId != 1);
    }
    
    private void setResponse (final HttpServletResponse res, final ResponseCode code) throws IOException, ServletException {
        ReturnCodeMessageResponse response = new ReturnCodeMessageResponse(code);
        res.setContentType("application/JSON;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().print(new ObjectMapper().writeValueAsString(response));
    }
    
}
