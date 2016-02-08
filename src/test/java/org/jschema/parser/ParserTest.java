package org.jschema.parser;

import junit.framework.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
      assertEquals( map4, parse( "{\"array\":[[{\"foo\":\"bar\"},{\"bar\":\"baz\"}],{\"foo2\":[1,2,3]}]}"));//,{\"bar\":\"baz\"},{\"foo\":[1,2,3]},{\"test\":{\"foo\":\"bar\"},{\"bar\":\"baz\"}}]" ) );
      //TODO add more
      //object with object as value
      HashMap map6=new HashMap();
      map6.put("object",map2);
      assertEquals(map6,parse("{\"object\":{\"foo2\":[1,2,3]}}"));

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
    //TODO add more
  }

  @Test
  public void testParseLiterals()
  {
    assertEquals( "foo", parse( "\"foo\"" ) );
    assertEquals( null, parse( "null" ) );
    assertEquals( true, parse( "true" ) );
    assertEquals( false, parse( "false" ) );
    assertEquals( 1, parse( "1" ) );
    assertEquals( 1.1, parse( "1.1" ) );
  }


  @Test
  public void testErrors() {
    assertTrue( parse( "}{" ) instanceof Error);
      assertTrue (parse("][") instanceof Error);
      //assertTrue( parse( "{" ) instanceof Error);

      //TODO add more
  }


  private Object parse( String src )
  {
    return new Parser( src ).parse();
  }

}
