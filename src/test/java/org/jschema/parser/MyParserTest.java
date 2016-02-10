//QUESTION!
//
//how can we check the error type?
//



package org.jschema.parser;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class MyParserTest
{

    @Test
    public void testParseObject(){
        assertEquals( new HashMap(), parse( "{}" ) );
        HashMap map1 = new HashMap();
        map1.put( "foo", "bar" );
        assertEquals( map1, parse( "{\"foo\":\"bar\"}" ) );

        //NEW

        //{pair, pair}
        map1.put( "foo2", "bar2" );
        assertEquals( map1, parse( "{\"foo\":\"bar\",\"foo2\":\"bar2\"}" ) );
        //{pair, pair, string:emptyArray}
        map1.put( "foo3", parse("[]") );
        assertEquals( map1, parse( "{\"foo\":\"bar\",\"foo2\":\"bar2\", \"foo3\":[]}" ) );
        HashMap map2 = new HashMap();
        map2.put( "foo4", parse("[]") );
        assertEquals( map2, parse( "{\"foo4\":[]}" ));



    }



    @Test
    public void testParseArray(){
        assertEquals( new ArrayList(), parse( "[]" ) );
        assertEquals( Arrays.asList( "foo", "bar" ), parse( "[\"foo\", \"bar\"]" ) );

        //NEW
        //array within array
        assertEquals( Arrays.asList( "foo", Arrays.asList( "foo", "bar" ) ), parse( "[\"foo\", [\"foo\", \"bar\"]]" ) );
        //something pointless maybe?
//QUESTION!        // [[[]]] == []   OR [[[]]] == [[[]]]
        //assertEquals( new ArrayList, parse( "[[[1]]]" ) );
        //["hey",{}]
        assertEquals(Arrays.asList("hey", new ArrayList()), parse(" [\"hey\", []] ") );
    }


//TODO mix objects and arrays and things



    @Test
    public void testParseLiterals(){//AKA values?
        //TODO: objects


        //string
        assertEquals( "foo", parse( "\"foo\"" ) );
        //null
        assertEquals( null, parse( "null" ) );
        //true
        assertEquals( true, parse( "true" ) );
        //false
        assertEquals( false, parse( "false" ) );

        //numbers
        assertEquals( 1, parse( "1" ) );
        assertEquals( 1.1, parse( "1.1" ) );

        //NEW ONES
        //string
        assertEquals( "Hey, this is a: test. Are you true to yourself, brother? It is already 5.30pm.", parse( "\"Hey, this is a: test. Are you true to yourself, brother? It is already 5.30pm.\"" ) );
    }


    @Test
    public void testErrors() {
        assertTrue( parse( "}{" ) instanceof Error);

        //NEW

        //array not closed
        assertTrue( parse( "[\"something\" " ) instanceof Error);
        //object closed within array
        assertTrue( parse( "[\"something\" } \"else\"" ) instanceof Error);
        //colon after [
        assertTrue( parse( "[:]" ) instanceof Error);
        //EOF affter [
        assertTrue( parse( "[" ) instanceof Error);
        //COMMA after [
        assertTrue( parse( "[,]" ) instanceof Error);
        //ERROR whithin []
        assertTrue( parse( "[g]" ) instanceof Error);
        //ERROR too many  ]
        assertTrue( parse( "[]]" ) instanceof Error);
        //ERROR } with no {
        assertTrue( parse( "[]}" ) instanceof Error);
        //ERROR  : not in pair
        assertTrue( parse( ":" ) instanceof Error);
        //ERROR , not in elements
        assertTrue( parse( "," ) instanceof Error);

        assertTrue(parse("[ \"hey\" , ,]") instanceof Error);

    }


    private Object parse( String src )
    {
        return new Parser( src ).parse();
    }

}
