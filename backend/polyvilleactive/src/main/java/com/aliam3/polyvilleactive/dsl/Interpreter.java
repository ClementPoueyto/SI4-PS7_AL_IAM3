package com.aliam3.polyvilleactive.dsl;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.aliam3.polyvilleactive.dsl.listeners.CustomListener;
import com.aliam3.polyvilleactive.dsl.listeners.ThrowingErrorListener;

import java.io.*;

public class Interpreter {

    public void read(String str, boolean verifyOnly) throws IOException {
        read(new ByteArrayInputStream(str.getBytes()), verifyOnly);
    }

    public void read(File file, boolean verifyOnly) throws IOException {
        read(new FileInputStream(file),verifyOnly);
    }

    /**
     * Permet de parser un texte en DSL
     * @param in InputStream sur le texte à parser
     * @throws IOException
     */
    public void read(InputStream in, boolean verifyOnly) throws IOException {
        ANTLRInputStream inputStream = new ANTLRInputStream(in);
        DSLLexer lexer = new DSLLexer(inputStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
        DSLParser parser = new DSLParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(ThrowingErrorListener.INSTANCE);
        CustomListener lis= new CustomListener(verifyOnly);
        ParseTree tree = null;
        tree = parser.prog();
        ParseTreeWalker.DEFAULT.walk(lis, tree);
    }
}