package org.jschema.generators;

import org.jschema.generators.JSchemaToJavaRunner;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.jschema.parser.Token.TokenType.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

public class JSchemaToJavaTest{


    @Test
    public void TestGenerateClass() throws Exception
    {
        assertEquals("Class Temp_Name{\n", JSchemaToJavaRunner.generateClass("Temp_Name"));
    }

    @Test
    public void TestGenerateFields() throws Exception
    {
        assertEquals("private String _Name;" , JSchemaToJavaRunner.generateFields("{\"Name\" : \"@String\"}"));

        assertEquals("private String _Name;\nprivate int _age;",
                JSchemaToJavaRunner.generateFields("{\"Name\" : \"@String\", \"age\": \"@int\"}"));
    }

    @Test
    public void TestGenerateObject() throws Exception
    {
        assertEquals("public Temp_Name(String Name){_Name = Name;}",JSchemaToJavaRunner.generateObject("{\"Name\" : \"@String\"}", "Temp_Name"));

        assertEquals("public Temp_Name(String Name, int age){_Name = Name;_age = age;}",
                JSchemaToJavaRunner.generateObject("{\"Name\" : \"@String\", \"age\": \"@int\"}", "Temp_Name"));

    }
    @Test
    public void TestGenerateGET() throws Exception
    {
        assertEquals("public String getName(){return _Name;}",JSchemaToJavaRunner.generateGET("{\"Name\" : \"@String\"}"));

        assertEquals("public String getName(){return _Name;}\npublic int getage(){return _age;} ",
                JSchemaToJavaRunner.generateGET("{\"name\" : \"@String\", \"age\": \"@int\"}"));
    }
    @Test
    public void TestGenerateSET() throws Exception
    {
        assertEquals("public void setName(String Name){_Name = Name;}",JSchemaToJavaRunner.generateGET("{\"Name\" : \"@String\"}"));

        assertEquals("public String setName(String Name){_Name = Name;}\npublic int setage(int age){_age = age;} ",
                JSchemaToJavaRunner.generateSET("{\"name\" : \"@String\", \"age\": \"@int\"}"));
    }


    //Helpers
    private String Open(String PathToFile) throws IOException{
        StringBuilder builder = new StringBuilder();
        String Line;
        BufferedReader schema = new BufferedReader( new FileReader(PathToFile));
        while((Line = schema.readLine()) != null){
            builder.append(Line);
        }
        String str = builder.toString();
        return str;
    }

}
