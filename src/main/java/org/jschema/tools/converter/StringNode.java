package org.jschema.tools.converter;

import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;

public class StringNode extends ConverterNode<String>
{
  @Override
  protected String _toJava( Node n ) throws Exception
  {
    check( n, LiteralNode.class, "string" );
    return ((LiteralNode)n).getString();
  }

  @Override
  public StringBuilder prettyPrint( int offset, Integer indent, StringBuilder stringBuilder, String value )
  {
    return stringBuilder.append(quote( value ));
  }
}
