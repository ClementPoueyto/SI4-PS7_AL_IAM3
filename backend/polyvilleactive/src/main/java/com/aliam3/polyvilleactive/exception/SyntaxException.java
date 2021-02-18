package com.aliam3.polyvilleactive.exception;

import org.antlr.v4.runtime.misc.ParseCancellationException;

public class SyntaxException extends ParseCancellationException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7633844425999619034L;

	public SyntaxException(String msg) {
		super("Erreur syntaxique:\n"+msg);
	}
}
