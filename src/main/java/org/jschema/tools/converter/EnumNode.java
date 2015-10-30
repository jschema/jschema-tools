package org.jschema.tools.converter;

import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;

public class EnumNode<T extends Enum<T>> extends ConverterNode<T>
{
  Class<T> _enumClass;

  public EnumNode( Class enumClass )
  {
    _enumClass = enumClass;
  }

  @Override
  protected T _toJava( Node n ) throws Exception
  {
    check( n, LiteralNode.class, "string" );
    return Enum.valueOf( _enumClass, ((LiteralNode)n).getString() );
  }

  @Override
  public StringBuilder prettyPrint( int offset, Integer indent, StringBuilder stringBuilder, T value )
  {
    return stringBuilder.append( quote( value.toString() ) );
  }

}
