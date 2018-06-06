package com.checkout.exceptions;

public class UnknownProductException extends Exception {

	private static final long serialVersionUID = 1L;
	private String missingProductCode;

	public UnknownProductException(String productCode) {
        this.setMissingProductCode(productCode);
    }

	public String getMissingProductCode() {
		return missingProductCode;
	}

	public void setMissingProductCode(String missingProductCode) {
		this.missingProductCode = missingProductCode;
	}
}
