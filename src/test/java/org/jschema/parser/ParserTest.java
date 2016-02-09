package org.jschema.parser;

import junit.framework.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
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
    assertEquals( new HashMap(), parse( "{}" ) );
    HashMap map1 = new HashMap();
    map1.put( "foo", "bar" );
      assertEquals( map1, parse( "{\"foo\":\"bar\"}" ) );
      HashMap map5=new HashMap();
    map5.put("bar","baz");
      ArrayList temp=new ArrayList();
      temp.add(map1);
      temp.add(map5);
      //array of objects
      assertEquals( temp, parse( "[{\"foo\":\"bar\"},{\"bar\":\"baz\"}]" ) );
      HashMap map7=new HashMap();
      //object with object value
      map7.put("nested",map1);
      assertEquals(map7, parse( "{\"nested\":{\"foo\":\"bar\"}}" ));
    HashMap map2 = new HashMap();
      HashMap map3=new HashMap();
      ArrayList testArray=new ArrayList();
      testArray.add(1);
      testArray.add(2);
      testArray.add(3);
    map2.put( "foo2", testArray );
    assertEquals( map2, parse( "{\"foo2\":[1,2,3]}" ) );
      //Array of array of objects and object
      HashMap map4=new HashMap();
      ArrayList testArray2=new ArrayList();
      testArray2.add(temp);
      testArray2.add(map2);
      map4.put("array",testArray2);
      assertEquals( map4, parse( "{\"array\":[[{\"foo\":\"bar\"},{\"bar\":\"baz\"}],{\"foo2\":[1,2,3]}]}"));
      //TODO add more
      //object with object as value
      HashMap map6=new HashMap();
      map6.put("object",map2);
      assertEquals(map6,parse("{\"object\":{\"foo2\":[1,2,3]}}"));

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
    assertEquals( map( "foo", "bat", "doh", "rey" ), parse( "{\"foo\":\"bat\", \"doh\":\"rey\"}" ) );
   assertEquals( map( "foo", "rey" ), parse( "{\"foo\":\"bar\", \"foo\":\"rey\"}" ) );
  }


  @Test
  public void testParsePunc() {
    assertEquals( ",", parse( "," ) );
  }
  @Test
  public void testParseArray()
  {
    assertEquals( new ArrayList(), parse( "[]" ) );
    assertEquals( Arrays.asList( "foo", "bar" ), parse( "[\"foo\", \"bar\"]" ) );
    int[][] nestedArray = {{1,2,3},{4,5,6}};
    ArrayList nestArray=new ArrayList();
    ArrayList nest1= new ArrayList();
    nest1.add(1);
    nest1.add(2);
    nest1.add(3);
    ArrayList nest2= new ArrayList();
    nest2.add(4);
    nest2.add(5);
    nest2.add(6);
    nestArray.add(nest1);
    nestArray.add(nest2);
    assertEquals(Arrays.asList(1,2,3), parse( "[1,2,3]" ) );
    assertEquals(nestArray, parse( "[[1,2,3],[4,5,6]]" ));
      //assorted array
      HashMap map1 = new HashMap();
      map1.put( "foo", "bar" );
      nestArray.add(map1);
      assertEquals( nestArray, parse( "[[1,2,3],[4,5,6],{\"foo\":\"bar\"}]" ) );
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

    // bad objects
    assertTrue( parse( "{\"foo\"}" ) instanceof Error);
    assertTrue( parse( "{\"foo\":}" ) instanceof Error);
    assertTrue( parse( "{\"foo\": badToken}" ) instanceof Error);

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
