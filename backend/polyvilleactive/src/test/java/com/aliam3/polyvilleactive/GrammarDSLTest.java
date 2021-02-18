package com.aliam3.polyvilleactive;

import com.aliam3.polyvilleactive.dsl.Interpreter;
import com.aliam3.polyvilleactive.exception.LexicalException;
import com.aliam3.polyvilleactive.exception.SemanticException;
import com.aliam3.polyvilleactive.exception.SyntaxException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class GrammarDSLTest {

    File testFile;

    void loadResource(String name) {
        ClassLoader classLoader = getClass().getClassLoader();
        testFile = new File(classLoader.getResource("dsl/"+name).getFile());
    }

    @Test
    void temoinSectionGlobal() throws IOException {
        loadResource("ok_global.txt");
        assertDoesNotThrow(() -> new Interpreter().read( testFile, true));
    }

    @Test
    void erreurLexicale1SectionGlobal() throws IOException {
        loadResource("lex_error_global_1.txt");
        assertThrows(LexicalException.class,() -> new Interpreter().read( testFile, true));
    }

    @Test
    void erreurSyntaxique1SectionGlobal() throws IOException {
        loadResource("syntax_error_global_1.txt");
        assertThrows(SyntaxException.class,() -> new Interpreter().read( testFile, true));
    }

    @Test
    void erreurSyntaxiqueSectionGlobalNotClosed() throws IOException {
        loadResource("syntax_error_global_2.txt");
        assertThrows(SyntaxException.class,() -> new Interpreter().read( testFile, true));
    }

    @Test
    void erreurSemantiqueAvoidedAndPrioritizedSectionGlobal() throws IOException {
        loadResource("sem_error_global_1.txt");
        assertThrows(SemanticException.class,() -> new Interpreter().read( testFile, true));
    }

    @Test
    void erreurSemantiqueTwoTransportsOneRankSectionGlobal() throws IOException {
        loadResource("sem_error_global_2.txt");
        assertThrows(SemanticException.class,() -> new Interpreter().read( testFile, true));
    }

    @Test
    void erreurSemantiqueOneTransportTwoRanksSectionGlobal() throws IOException {
        loadResource("sem_error_global_3.txt");
        assertThrows(SemanticException.class,() -> new Interpreter().read( testFile, true));
    }

    @Test
    void erreurSemantiqueNegativePrioritySectionGlobal() throws IOException {
        loadResource("sem_error_global_4.txt");
        assertThrows(SemanticException.class,() -> new Interpreter().read( testFile, true));
    }


    @Test
    void erreurSyntaxiqueSectionLocalNotclosed() throws IOException {
        loadResource("syntax_error_local_1.txt");
        assertThrows(SyntaxException.class,() -> new Interpreter().read( testFile, true));
    }

    @Test
    void okSectionLocal() throws IOException {
        loadResource("ok_local.txt");
        assertDoesNotThrow(() -> new Interpreter().read( testFile, true));
    }
    
    @Test
    void erreurInterdictionGlobal() throws IOException {
        loadResource("lex_error_global_interdiction.txt");
        assertThrows(LexicalException.class,() -> new Interpreter().read( testFile, true));
    }
    
    @Test
    void okProgrammeComplexe() throws IOException {
        loadResource("ok_programmecomplexe.txt");
        assertDoesNotThrow(() -> new Interpreter().read( testFile, true));
    }
    
    @Test
    void erreurProgrammeComplexe() throws IOException {
        loadResource("sem_error_programmecomplexe.txt");
        assertThrows(SemanticException.class,() -> new Interpreter().read( testFile, true));
    }
    
}