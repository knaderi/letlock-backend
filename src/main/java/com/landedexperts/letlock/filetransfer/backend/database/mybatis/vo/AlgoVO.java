package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ErrorCodeMessageResponse;

public class AlgoVO extends ErrorCodeMessageResponse {
	public AlgoVO(String hashingAlgo, String encodingAlgo, String errorCode, String errorMessage ) {
	    
        super(errorCode, errorMessage );
        setHashingAlgo(hashingAlgo);
        setHashingAlgo(encodingAlgo);
    }

    public AlgoVO(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
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
