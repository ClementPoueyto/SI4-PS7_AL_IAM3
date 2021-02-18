package com.aliam3.polyvilleactive.db;



import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class MockAPI {


    public MockAPI() {/* Default Constructor */}

    public String loadResource(String name) {
        ClassLoader classLoader = getClass().getClassLoader();
        File testFile = new File(classLoader.getResource("static/" + name).getFile());

        try {
            return Files.readString(testFile.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}