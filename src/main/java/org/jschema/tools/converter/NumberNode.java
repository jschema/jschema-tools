package org.jschema.tools.converter;

import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;

public class NumberNode extends ConverterNode<Double>
{
  @Override
  protected Double _toJava( Node n ) throws Exception
  {
    check( n, LiteralNode.class, "int" );
    return ((LiteralNode)n).getNumber();
  }

  @Override
  public StringBuilder prettyPrint( int offset, Integer indent, StringBuilder stringBuilder, Double value )
  {
    return stringBuilder.append(value);
  }
}
