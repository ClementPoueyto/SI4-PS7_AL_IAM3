package com.aliam3.polyvilleactive.exception;

public class SemanticException extends IllegalStateException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2681521065373894195L;

	public SemanticException(String msg) {
        super("Erreur sémantique:\n"+msg);
    }
}
