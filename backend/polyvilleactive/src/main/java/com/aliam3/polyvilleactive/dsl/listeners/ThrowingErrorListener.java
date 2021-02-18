package com.aliam3.polyvilleactive.dsl.listeners;


import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import com.aliam3.polyvilleactive.exception.LexicalException;
import com.aliam3.polyvilleactive.exception.SyntaxException;

public class ThrowingErrorListener extends BaseErrorListener {
	public static final ThrowingErrorListener INSTANCE = new ThrowingErrorListener();

	@Override
	   public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
	      /* throws LexicalException,SyntaxException */{
		   if(e!= null) {
			   throw new LexicalException("line " + line + ":" + charPositionInLine + " " + msg);
		   }
		   throw new SyntaxException("line " + line + ":" + charPositionInLine + " " + msg);
	      }

}
