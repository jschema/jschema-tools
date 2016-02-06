package org.jschema.parser;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
    //TODO add more
  }

  @Test
  public void testParseArray()
  {
    assertEquals( new ArrayList(), parse( "[]" ) );
    assertEquals( Arrays.asList( "foo", "bar" ), parse( "[\"foo\", \"bar\"]" ) );
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
    assertTrue( parse( "}}}}" ) instanceof Error);
    assertTrue( parse( "}{}}{{" ) instanceof Error);
    assertTrue( parse( "{}}{{" ) instanceof Error);
  }


  private Object parse( String src )
  {
    return new Parser( src ).parse();
  }

}
