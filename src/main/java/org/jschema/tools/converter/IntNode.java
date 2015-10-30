package org.jschema.tools.converter;

import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;

public class IntNode extends ConverterNode<Long>
{
  @Override
  protected Long _toJava( Node n ) throws Exception
  {
    check( n, LiteralNode.class, "int" );
    return ((LiteralNode)n).getLong();
  }

  @Override
  public StringBuilder prettyPrint( int offset, Integer indent, StringBuilder stringBuilder, Long value )
  {
    return stringBuilder.append(value);
  }
}
