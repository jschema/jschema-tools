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
    public void testGenerateClass() throws Exception
    {
        assertEquals("{\"name\" : \"@String\", \"age\": \"@int\"}",
                JSchemaToJavaRunner.generateFields(Open("src/test/java/schemas/basic.jschema")));
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
