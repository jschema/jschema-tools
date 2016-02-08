package org.jschema;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
    System.out.println("Time: " + ((after - before) / (double) times) + " ms");
    assertEquals(0, errors);
  }

  @Test
  public void testSample() {
    String sample = "{\n" +
      "  \"firstName\": \"John\",\n" +
      "  \"lastName\": \"Smith\",\n" +
      "  \"age\": 25,\n" +
      "  \"address\": {\n" +
      "    \"streetAddress\": \"21 2nd Street\",\n" +
      "    \"city\": \"New York\",\n" +
      "    \"state\": \"NY\",\n" +
      "    \"postalCode\": \"10021\"\n" +
      "  },\n" +
      "  \"etc\": [true, false, null, 3.14, [\"a\", 8]]\n" +
      "}";
    HashMap expected = new HashMap();
    expected.put("firstName", "John");
    expected.put("lastName", "Smith");
    expected.put("age", 25);

    HashMap address = new HashMap();
    address.put("streetAddress", "21 2nd Street");
    address.put("city", "New York");
    address.put("state", "NY");
    address.put("postalCode", "10021");

    expected.put("address", address);
    expected.put("etc", Arrays.asList(true, false, null, 3.14, Arrays.asList("a", 8)));
    Tokenizer tokenizer = new Tokenizer(sample);
    Parser p = new Parser(tokenizer);
    Object val = p.parse();
    assertEquals("[]", p.getErrors().toString());
    assertEquals(expected, val);
  }

  @Test
  public void testSampleErr() {
    String sample = "{\n" +
      "  \"firstName\": \"John\",\n" +
      "  \"lastName\": \"Smith\",\n" +
      "  \"age\": 25,\n" +
      "  \"address\": {\n" +
      "    \"streetAddress\" BUG \"21 2nd Street\",\n" +
      "    \"city\": \"New York\",\n" +
      "    \"state\": \"NY\",\n" +
      "    \"postalCode\": \"10021\"\n" +
      "  },\n" +
      "  \"etc\": [true, false, null, 3.14, [\"a\", 8]]\n" +
      "}";
    HashMap expected = new HashMap();
    expected.put("firstName", "John");
    expected.put("lastName", "Smith");
    expected.put("age", 25);
    HashMap address = new HashMap();
    address.put("streetAddress", null);
    expected.put("address", address);
    expected.put("etc", Arrays.asList(true, false, null, 3.14, Arrays.asList("a", 8)));
    Tokenizer tokenizer = new Tokenizer(sample);
    Parser p = new Parser(tokenizer);
    Object val = p.parse();
    assertEquals(expected, val);
    assertEquals(3, p.getErrors().size());
  }

  @Test
  public void testParseObject() {
    // empty
    assertEquals(map(), parse("{}"));

    // simple single
    assertEquals(map("foo", "bar"), parse("{\"foo\":\"bar\"}"));
    assertEquals(map("foo", 1), parse("{\"foo\":1}"));
    assertEquals(map("foo", 1.1), parse("{\"foo\":1.1}"));
    assertEquals(map("foo", true), parse("{\"foo\":true}"));
    assertEquals(map("foo", false), parse("{\"foo\":false}"));
    assertEquals(map("foo", null), parse("{\"foo\":null}"));
    assertEquals(map("foo", list()), parse("{\"foo\":[]}"));

    // complex single
    assertEquals(map("foo", map("foo", "bar")), parse("{\"foo\" : {\"foo\":\"bar\"}}"));
    assertEquals(map("foo", list("foo", "bar")), parse("{\"foo\" : [\"foo\", \"bar\"}]"));

    // simple multi
    assertEquals(map("foo", "bar", "doh", "rey"), parse("{\"foo\":\"bar\", \"doh\":\"rey\"}"));
    assertEquals(map("foo", "rey"), parse("{\"foo\":\"bar\", \"foo\":\"rey\"}"));
  }


  @Test
  public void testParseArray() {
    assertEquals(list(), parse("[]"));
    assertEquals(list("foo"), parse("[\"foo\"]"));
    assertEquals(list("foo", "bar"), parse("[\"foo\", \"bar\"]"));
    assertEquals(list("string", 1, 1.1, map("foo", "bar"), list("doh"), true, false, null),
      parse("[\"string\", 1, 1.1, {\"foo\" : \"bar\"}, [\"doh\"], true, false, null]"));
  }

  @Test
  public void testParseLiterals() {
    // strings
    assertEquals("", parse("\"\""));
    assertEquals("foo", parse("\"foo\""));
    assertEquals("foo\"bar", parse("\"foo\\\"bar\""));

    // numbers
    assertEquals(0, parse("0"));
    assertEquals(1, parse("1"));
    assertEquals(123456789, parse("123456789"));
    assertEquals(-1, parse("-1"));
    assertEquals(-0, parse("-0"));
    assertEquals(-123456789, parse("-123456789"));
    assertEquals(1.1, parse("1.1"));
    assertEquals(123456789.1, parse("123456789.1"));
    assertEquals(123456.123456, parse("123456.123456"));
    assertEquals(-1.1, parse("-1.1"));
    assertEquals(-123456789.1, parse("-123456789.1"));
    assertEquals(-123456.123456, parse("-123456.123456"));
    assertEquals(1e1, parse("1e1"));
    assertEquals(123456789e1, parse("123456789e1"));
    assertEquals(1e+1, parse("1e+1"));
    assertEquals(1e+1, parse("1e+1"));
    assertEquals(1e-1, parse("1e-1"));
    assertEquals(1E1, parse("1E1"));
    assertEquals(1E+1, parse("1E+1"));
    assertEquals(1E-1, parse("1E-1"));

    // literals
    assertEquals(true, parse("true"));
    assertEquals(false, parse("false"));
    assertEquals(null, parse("null"));
  }


  @Test
  public void testErrors() {

    // bad punctuation
    assertTrue(hasErrors("{"));
    assertTrue(hasErrors("}"));
    assertTrue(hasErrors("}{"));
    assertTrue(hasErrors("["));
    assertTrue(hasErrors("]"));
    assertTrue(hasErrors("]["));
    assertTrue(hasErrors(","));
    assertTrue(hasErrors(":"));

    // bad strings
    assertTrue(hasErrors("\""));
    assertTrue(hasErrors("\"foo"));

    // bad numbers
    assertTrue(hasErrors(".1"));
    assertTrue(hasErrors("-.1"));
    assertTrue(hasErrors("e1"));
    assertTrue(hasErrors("e+1"));
    assertTrue(hasErrors("e-1"));
    assertTrue(hasErrors("E1"));
    assertTrue(hasErrors("E+1"));
    assertTrue(hasErrors("E-1"));
    assertTrue(hasErrors("1E.1"));

    // bad objects
    assertTrue(hasErrors("{\"foo\"}"));
    assertTrue(hasErrors("{\"foo\":}"));
    assertTrue(hasErrors("{\"foo\": badToken}"));

    // bad arrays
    assertTrue(hasErrors("[1"));
    assertTrue(hasErrors("[1,"));
    assertTrue(hasErrors("[1,]"));
    assertTrue(hasErrors("[1, badToken]"));
    assertTrue(hasErrors("[1, [badToken]]"));

    // bad literals
    assertTrue(hasErrors("badToken"));
    assertTrue(hasErrors("True"));
    assertTrue(hasErrors("nil"));
  }

  private boolean hasErrors(String src) {
    Parser parser = new Parser(new Tokenizer(src));
    parser.parse();
    return parser.getErrors().size() != 0;
  }

  private Object parse(String src) {
    return new Parser(new Tokenizer(src)).parse();
  }

  private List list(Object... listVals) {
    return Arrays.asList(listVals);
  }

  private HashMap map(Object... mapVals) {
    HashMap m = new HashMap();
    Iterator it = Arrays.asList(mapVals).iterator();
    while(it.hasNext()) {
      m.put(it.next(), it.next());
    }
    return m;
  }
}
