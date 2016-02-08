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
      HashMap map5=new HashMap();
    map5.put("bar","baz");
      ArrayList temp=new ArrayList();
      temp.add(map1);
      temp.add(map5);
    System.out.println(map1.toString());
    assertEquals( temp, parse( "[{\"foo\":\"bar\"},{\"bar\":\"baz\"}]" ) );
    HashMap map2 = new HashMap();
      HashMap map3=new HashMap();
      ArrayList testArray=new ArrayList();
      testArray.add(1);
      testArray.add(2);
      testArray.add(3);
    map2.put( "foo2", testArray );
    assertEquals( map2, parse( "{\"foo2\":[1,2,3]}" ) );
      HashMap map4=new HashMap();
      ArrayList testArray2=new ArrayList();
      testArray2.add(temp);
      testArray2.add(map2);
      map4.put("array",testArray2);
      System.out.println("crazy map is "+map4.toString());
      assertEquals( map4, parse( "{\"array\":[[{\"foo\":\"bar\"},{\"bar\":\"baz\"}],{\"foo2\":[1,2,3]}]}"));//,{\"bar\":\"baz\"},{\"foo\":[1,2,3]},{\"test\":{\"foo\":\"bar\"},{\"bar\":\"baz\"}}]" ) );
      //TODO add more
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
    //TODO add more
  }


  private Object parse( String src )
  {
    return new Parser( src ).parse();
  }

}
