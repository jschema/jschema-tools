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
    public void TestGenerateAll() throws Exception{
        assertEquals("public class classname{\nprivate String _Name;\nprivate int _Age;\npublic classname(String Name, int Age){\n_Name = Name;\n_Age = Age;\n}\npublic String getName(){return _Name;}\npublic int getAge(){return _Age;}\npublic void setName(String Name){_Name = Name;}\npublic void setAge(int Age){_Age = Age;}\n}", JSchemaToJavaRunner.generateAll("classname", "{\"Name\" : \"@String\", \"Age\": \"@int\"}"));
    }

    @Test
    public void TestGenerateClass() throws Exception
    {
        assertEquals("public class Temp_Name{\n", JSchemaToJavaRunner.generateClass("Temp_Name"));
    }

    @Test
    public void TestGenerateEnums() throws Exception
    {
        assertEquals("", JSchemaToJavaRunner.generateEnums("{\"name\" : \"@String\"}"));
        assertEquals("public enum color{\n  blue,\n  brown,\n  red\n}\nprivate color _color;\n", JSchemaToJavaRunner.generateEnums("{\"color\" : [\"blue\", \"brown\", \"red\"]}"));
        assertEquals("public enum color{\n  blue,\n  brown,\n  red\n}\nprivate color _color;\n",
                JSchemaToJavaRunner.generateEnums("{\"name\" : \"@String\",\"color\" : [\"blue\", \"brown\", \"red\"]}"));
        assertEquals("public enum color{\n  blue,\n  brown,\n  red\n}\nprivate color _color;\n",
                JSchemaToJavaRunner.generateEnums("{\"name\" : [\"@String\"],\"color\" : [\"blue\", \"brown\", \"red\"]}"));
    }

    @Test
    public void TestGenerateFields() throws Exception
    {
        assertEquals("private String _Name;\n" , JSchemaToJavaRunner.generateFields("{\"Name\" : \"@String\"}"));

        assertEquals("private String _Name;\n" , JSchemaToJavaRunner.generateFields("[{\"Name\" : \"@String\"}]"));

        assertEquals("private String _Name;\nprivate int _age;\n",
                JSchemaToJavaRunner.generateFields("{\"Name\" : \"@String\", \"age\": \"@int\"}"));

        //simple array case
        assertEquals("private String[] _emails;\n" ,
                JSchemaToJavaRunner.generateFields("{\"emails\" : [\"@String\"]}"));

        //assertEquals("private Object _person{\n private String _name;\n private int _age;\n}\n" ,
        //      JSchemaToJavaRunner.generateFields("{\"person\" : {\"name\" : \"@String\", \"age\" : \"@int\"}}"));

        //simple enum case
        assertEquals("public enum type{ red, green, blue};\nprivate type _type",
                JSchemaToJavaRunner.generateFields("{\"type\" : [\"red\", \"green\", \"blue\"]}"));


    }

    @Test
    public void TestGenerateObject() throws Exception
    {
        assertEquals("public Temp_Name(String Name){\n_Name = Name;\n}\n",JSchemaToJavaRunner.generateObject("{\"Name\" : \"@String\"}", "Temp_Name"));

        assertEquals("public Temp_Name(String Name, int age){\n_Name = Name;\n_age = age;\n}\n",
                JSchemaToJavaRunner.generateObject("{\"Name\" : \"@String\", \"age\": \"@int\"}", "Temp_Name"));

        //array case --this requires more thought, not sure if you can simply equate arrays
        assertEquals("public Temp_Name(String[] emails){\n_emails = emails;\n}\n",
                JSchemaToJavaRunner.generateObject("{\"emails\" : [\"@String\"]}", "Temp_Name"));

        //enum case --we will need to watch our variable capitalization
        assertEquals("public enum Temp_Name(type Type){\n_type = Type;\n}\n",
                JSchemaToJavaRunner.generateObject("{\"type\" : [\"red\", \"green\", \"blue\"]}" ,"Temp_Name"));

    }

//returns a variable (string, date, int etc)
    @Test
    public void TestGenerateGET() throws Exception
    {
        assertEquals("public String getName(){return _Name;}\n",
                JSchemaToJavaRunner.generateGET("{\"Name\" : \"@String\"}"));

        assertEquals("public String getName(){return _Name;}\npublic int getage(){return _age;}\n",
                JSchemaToJavaRunner.generateGET("{\"Name\" : \"@String\", \"age\": \"@int\"}"));

        //array case
        assertEquals("public String[] getemails(){return _emails;}\n",
                JSchemaToJavaRunner.generateGET("{\"emails\" : [\"@String\"]}"));

        //enum case --will have to watch our variable capitalization
        assertEquals("public type getType(){return _type;}\n",
                JSchemaToJavaRunner.generateGET("{\"type\" : [\"red\", \"green\", \"blue\"]}"));
    }

//does not return anything. sets value.
    @Test
    public void TestGenerateSET() throws Exception
    {
        assertEquals("public void setName(String Name){_Name = Name;}\n",JSchemaToJavaRunner.generateSET("{\"Name\" : \"@String\"}"));

        assertEquals("public void setName(String Name){_Name = Name;}\npublic void setage(int age){_age = age;}\n",
                JSchemaToJavaRunner.generateSET("{\"Name\" : \"@String\", \"age\": \"@int\"}"));

        //array case --this requires more thought, not sure if you can simply equate arrays
        assertEquals("public void setemails(String[] emails){_emails = emails;}\n",
                JSchemaToJavaRunner.generateSET("{\"emails\" : [\"@String\"]}"));

        //enum case --we will need error checking to make sure the input is a valid enum type
        assertEquals("public void setType(type Type){_type = Type}\n",
                JSchemaToJavaRunner.generateSET("{\"type\" : [\"red\", \"green\", \"blue\"]}"));
    }



    @Test
    public void TestGenerateError() throws Exception{
        assertEquals("public void setError(String Name){_Name = Name;}",JSchemaToJavaRunner.generateError("{\"Name\" : \"@String\"}"));

        assertEquals("public void setError(String Name)[{_Name = Name;}]",JSchemaToJavaRunner.generateError("[{\"Name\" : \"@String\"}]"));


        assertEquals("public void setError(String date){_date = date;}",JSchemaToJavaRunner.generateError("{\"date\" : \"@date\"}"));

        assertEquals("public void setError(String uri){_uri = uri;}",JSchemaToJavaRunner.generateError("{\"uri\" : \"@uri\"}"));

    }

    //Helpers
    /*
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
*/
}
