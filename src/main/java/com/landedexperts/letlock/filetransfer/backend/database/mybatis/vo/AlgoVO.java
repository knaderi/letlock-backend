package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;

public class AlgoVO extends ReturnCodeMessageResponse {
	public AlgoVO(String hashingAlgo, String encodingAlgo, String returnCode, String returnMessage ) {
	    
        super(returnCode, returnMessage );
        setHashingAlgo(hashingAlgo);
        setHashingAlgo(encodingAlgo);
    }

    public AlgoVO(String returnCode, String returnMessage) {
        super(returnCode, returnMessage);
        // TODO Auto-generated constructor stub
    }

    private String hashingAlgo;

	public String getHashingAlgo() {
		return hashingAlgo;
	}
	
	public void setHashingAlgo(final String hashingAlgo) {
		this.hashingAlgo = hashingAlgo;
	}

	private String encodingAlgo;

	public String getEncodingAlgo() {
		return encodingAlgo;
	}
	
	public void setEncodingAlgo(final String encodingAlgo) {
		this.encodingAlgo = encodingAlgo;
	}
}
