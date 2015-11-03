package org.jschema.tools.converter;

import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.ir.PropertyNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract public class ConverterNode<T>
{
  public final T toJava( Node n ) {
    try
    {
      return _toJava( n );
    }
    catch( Exception e )
    {
      if( e instanceof RuntimeException )
      {
        throw (RuntimeException) e;
      }
      else
      {
        throw new RuntimeException( e );
      }
    }
  }

  protected abstract T _toJava( Node n ) throws Exception;

  protected void check( Node n, Class clazz, String name )
  {
    if( !clazz.isInstance( n ) ) {
      throw new IllegalStateException( "Expected " + name + " at offset " + n.getStart() + ", found " + n.toString() );
    }
  }

  protected Object convertToRawValue( Node value )
  {
    if( value instanceof ObjectNode )
    {
      HashMap<String, Object> result = new HashMap<>();
      List<PropertyNode> elements = ((ObjectNode)value).getElements();
      for( PropertyNode element : elements )
      {
        result.put( element.getKeyName(), convertToRawValue( element.getValue() ) );
      }
      return result;
    }
    else if( value instanceof LiteralNode.ArrayLiteralNode )
    {
      ArrayList<Object> result = new ArrayList<>();
      for( Node node : ((LiteralNode.ArrayLiteralNode)value).getArray() )
      {
        result.add( convertToRawValue( node ) );
      }
      return result;
    }
    else if( value instanceof LiteralNode )
    {
      return ((LiteralNode) value).getValue();
    }
    else
    {
      return value.toString();
    }
  }

  protected StringBuilder convertFromRawValue(  int offset, Integer indent, StringBuilder stringBuilder, Object value  )
  {
    if( value instanceof Map )
    {
      stringBuilder.append( "{" );
      int newOffset = indent == null ? 0 : offset + indent;
      Set<Map.Entry> set = ((Map)value).entrySet();
      for( Iterator<Map.Entry> iterator = set.iterator(); iterator.hasNext(); )
      {
        Map.Entry entry = iterator.next();
        maybeNewLine( indent, stringBuilder );
        maybeIndent( newOffset, stringBuilder );
        stringBuilder.append( quote( entry.getKey().toString() ) ).append( " : " );
        convertFromRawValue( newOffset, indent, stringBuilder, entry.getValue() );
        if( iterator.hasNext() )
        {
          stringBuilder.append( ", " );
        }
      }
      if( set.size() > 0 )
      {
        maybeNewLine( indent, stringBuilder );
        maybeIndent( offset, stringBuilder );
      }
      stringBuilder.append( "}" );
      return stringBuilder;
    }
    else if( value instanceof List )
    {
      stringBuilder.append( "[" );
      int newOffset = indent == null ? 0 : offset + indent;
      List lst = (List) value;
      for( Iterator<T> iterator = lst.iterator(); iterator.hasNext(); )
      {
        T t = iterator.next();
        maybeNewLine( indent, stringBuilder );
        maybeIndent( newOffset, stringBuilder );
        convertFromRawValue( newOffset, indent, stringBuilder, t );
        if(iterator.hasNext())
        {
          stringBuilder.append( ", " );
        }
      }
      if( lst.size() > 0 )
      {
        maybeNewLine( indent, stringBuilder );
        maybeIndent( offset, stringBuilder );
      }
      stringBuilder.append( "]" );
      return stringBuilder;
    }
    else if( value instanceof Boolean || value instanceof Number )
    {
      return stringBuilder.append( value.toString() );
    }
    else
    {
      return stringBuilder.append( quote( value.toString() ) );
    }
  }

  public abstract StringBuilder prettyPrint( int offset, Integer indent, StringBuilder stringBuilder, T value );

  protected void maybeIndent( int offset, StringBuilder stringBuilder )
  {
    while(offset > 0)
    {
      stringBuilder.append( ' ' );
      offset = offset - 1;
    }
  }

  protected void maybeNewLine( Integer indent, StringBuilder stringBuilder )
  {
    if(indent != null)
    {
      stringBuilder.append( "\n" );
    }
  }

  protected String quote( String s )
  {
    return "\"" + s + "\"";
  }

}