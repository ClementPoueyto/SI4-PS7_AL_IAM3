package com.aliam3.polyvilleactive.stepdefs;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(value = Cucumber.class)
@SpringBootTest
@CucumberOptions(plugin = {"pretty"}, features = "src/test/resources/features", glue= {"com.aliam3.polyvilleactive.stepdefs"})
public class RunCucumberTest {
    // will run all features found on the classpath
    // in the same package as this class
}