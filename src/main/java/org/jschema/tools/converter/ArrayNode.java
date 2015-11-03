package org.jschema.tools.converter;

import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayNode<T> extends ConverterNode<List<T>>
{
  private ConverterNode<T> _component;

  public ArrayNode( ConverterNode<T> component )
  {
    _component = component;
  }

  @Override
  protected List<T> _toJava( Node n ) throws Exception
  {
    check( n, LiteralNode.ArrayLiteralNode.class, "array" );
    List returnList = new ArrayList();
    Node[] array = ((LiteralNode.ArrayLiteralNode)n).getArray();
    for( Node node : array )
    {
      returnList.add( _component.toJava( node ) );
    }
    return returnList;
  }

  @Override
  public StringBuilder prettyPrint( int offset, Integer indent, StringBuilder stringBuilder, List<T> value )
  {
    stringBuilder.append( "[" );
    int newOffset = indent == null ? 0 : offset + indent;
    for( Iterator<T> iterator = value.iterator(); iterator.hasNext(); )
    {
      T t = iterator.next();
      maybeNewLine( indent, stringBuilder );
      maybeIndent( newOffset, stringBuilder );
      _component.prettyPrint( newOffset, indent, stringBuilder, t );
      if(iterator.hasNext())
      {
        stringBuilder.append( ", " );
      }
    }
    if( value.size() > 0 )
    {
      maybeNewLine( indent, stringBuilder );
      maybeIndent( offset, stringBuilder );
    }
    stringBuilder.append( "]" );
    return stringBuilder;
  }

}
