package com.aliam3.polyvilleactive.exception;

import org.antlr.v4.runtime.misc.ParseCancellationException;

public class LexicalException extends ParseCancellationException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5069798607861298375L;
	
	public LexicalException(String msg) {
		super("Erreur lexicale:\n"+msg);
	}

}
