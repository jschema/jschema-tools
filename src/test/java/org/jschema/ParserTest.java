package org.jschema;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ParserTest {
  @Test
  public void readBigFile() {
    String bigJson = null;
    try {
      String path = getClass().getClassLoader().getResource("citylots.json").getPath();
      bigJson = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    } catch(Exception e) {
      fail("Please copy the file 'citylots.json' in the src/main/resources folder");
    }
    long times = 10;
    long before = System.currentTimeMillis();
    int errors = 0;
    for(int i = 0; i < times; i++) {
      Tokenizer tokenizer = new Tokenizer(bigJson);
      Parser p = new Parser(tokenizer);
      p.parse();
      errors = p.getErrors().size();
    }
    long after = System.currentTimeMillis();
    System.out.println("Time: " + ((after-before) / (double) times) + " ms");
    assertEquals(0, errors);
  }
}
