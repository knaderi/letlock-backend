package com.landedexperts.letlock.filetransfer.backend.database.result;

public class AlgoVO extends ErrorCodeMessageVO {
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
