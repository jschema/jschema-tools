package org.jschema.parser;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ParserTest
{

  @Test
  public void testParseObject()
  {
    // empty
    assertEquals( map(), parse( "{}" ) );

    // simple single
    assertEquals( map( "foo", "bar" ), parse( "{\"foo\":\"bar\"}" ) );
    assertEquals( map( "foo", 1 ), parse( "{\"foo\":1}" ) );
    assertEquals( map( "foo", 1.1 ), parse( "{\"foo\":1.1}" ) );
    assertEquals( map( "foo", true ), parse( "{\"foo\":true}" ) );
    assertEquals( map( "foo", false ), parse( "{\"foo\":false}" ) );
    assertEquals( map( "foo", null ), parse( "{\"foo\":null}" ) );
    assertEquals( map( "foo", list() ), parse( "{\"foo\":[]}" ) );

    // complex single
    assertEquals( map("foo", map( "foo", "bar" )), parse( "{\"foo\" : {\"foo\":\"bar\"}}" ) );
    assertEquals( map("foo", list( "foo", "bar" )), parse( "{\"foo\" : [\"foo\", \"bar\"]}" ) );

    // simple multi
    assertEquals( map( "foo", "bar", "doh", "rey" ), parse( "{\"foo\":\"bar\", \"doh\":\"rey\"}" ) );
    assertEquals( map( "foo", "rey" ), parse( "{\"foo\":\"bar\", \"foo\":\"rey\"}" ) );
  }


  @Test
  public void testParseArray()
  {
    assertEquals( list(), parse( "[]" ) );
    assertEquals( list( "foo"), parse( "[\"foo\"]" ) );
    assertEquals( list( "foo", "bar" ), parse( "[\"foo\", \"bar\"]" ) );
    assertEquals( list( "string", 1, 1.1, map("foo", "bar"), list("doh"), true, false, null ),
            parse( "[\"string\", 1, 1.1, {\"foo\" : \"bar\"}, [\"doh\"], true, false, null]" ) );
  }

  @Test
  public void testParseLiterals()
  {
    // strings
    assertEquals( "", parse( "\"\"" ) );
    assertEquals( "foo", parse( "\"foo\"" ) );
    assertEquals( "foo\"bar", parse( "\"foo\\\"bar\"" ) );

    // numbers
    assertEquals( 0, parse( "0" ) );
    assertEquals( 1, parse( "1" ) );
    assertEquals( 123456789, parse( "123456789" ) );
    assertEquals( -1, parse( "-1" ) );
    assertEquals( -0, parse( "-0" ) );
    assertEquals( -123456789, parse( "-123456789" ) );
    assertEquals( 1.1, parse( "1.1" ) );
    assertEquals( 123456789.1, parse( "123456789.1" ) );
    assertEquals(123456.123456, parse("123456.123456"));
    assertEquals( -1.1, parse( "-1.1" ) );
    assertEquals(-123456789.1, parse("-123456789.1"));
    assertEquals(-123456.123456, parse("-123456.123456"));
    assertEquals( 1e1, parse( "1e1" ) );
    assertEquals( 123456789e1, parse( "123456789e1" ) );
    assertEquals( 1e+1, parse( "1e+1" ) );
    assertEquals( 1e+1, parse( "1e+1" ) );
    assertEquals( 1e-1, parse( "1e-1" ) );
    assertEquals( 1E1, parse( "1E1" ) );
    assertEquals( 1E+1, parse( "1E+1" ) );
    assertEquals( 1E-1, parse( "1E-1" ) );

    // literals
    assertEquals( true, parse( "true" ) );
    assertEquals( false, parse( "false" ) );
    assertEquals( null, parse( "null" ) );
  }


  @Test
  public void testErrors() {

    // bad punctuation
    assertTrue( parse( "{" ) instanceof Error);
    assertTrue( parse( "}" ) instanceof Error);
    assertTrue( parse( "}{" ) instanceof Error);
    assertTrue( parse( "[" ) instanceof Error);
    assertTrue( parse( "]" ) instanceof Error);
    assertTrue( parse( "][" ) instanceof Error);
    assertTrue( parse( "," ) instanceof Error);
    assertTrue( parse( ":" ) instanceof Error);

    // bad strings
    assertTrue( parse( "\"" ) instanceof Error);
    assertTrue( parse( "\"foo" ) instanceof Error);

    // bad numbers
    assertTrue( parse( ".1" ) instanceof Error);
    assertTrue( parse( "-.1" ) instanceof Error);
    assertTrue( parse( "e1" ) instanceof Error);
    assertTrue( parse( "e+1" ) instanceof Error);
    assertTrue( parse( "e-1" ) instanceof Error);
    assertTrue( parse( "E1" ) instanceof Error);
    assertTrue( parse( "E+1" ) instanceof Error);
    assertTrue( parse( "E-1" ) instanceof Error);
    assertTrue( parse( "1E.1" ) instanceof Error);

    // bad arrays
    assertTrue( parse( "[1" ) instanceof Error);
    assertTrue( parse( "[1," ) instanceof Error);
    assertTrue( parse( "[1,]" ) instanceof Error);
    assertTrue( parse( "[1, badToken]" ) instanceof Error);
    assertTrue( parse( "[1, [badToken]]" ) instanceof Error);


    // bad literals
    assertTrue( parse( "badToken" ) instanceof Error);
    assertTrue( parse( "True" ) instanceof Error);
    assertTrue( parse( "nil" ) instanceof Error);


    // bad objects
    assertTrue( parse( "{\"foo\"}" ) instanceof Error);
    assertTrue( parse( "{\"foo\":}" ) instanceof Error);
    assertTrue( parse( "{\"foo\": badToken}" ) instanceof Error);

  }


  //======================================================================
  //  HELPERS
  //======================================================================
  private Object parse( String src )
  {
    return new Parser( src ).parse();
  }

  private List list(Object... listVals)
  {
    return Arrays.asList( listVals );
  }

  private HashMap map(Object... mapVals)
  {
    HashMap m = new HashMap();
    Iterator it = Arrays.asList( mapVals ).iterator();
    while(it.hasNext())
    {
      m.put( it.next(), it.next() );
    }
    return m;
  }

}