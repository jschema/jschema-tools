package org.jschema.tools.converter;

import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;

public class BooleanNode extends ConverterNode<Boolean>
{
  @Override
  protected Boolean _toJava( Node n ) throws Exception
  {
    check( n, LiteralNode.class, "boolean" );
    return ((LiteralNode)n).getBoolean();
  }

  @Override
  public StringBuilder prettyPrint( int offset, Integer indent, StringBuilder stringBuilder, Boolean value )
  {
    return stringBuilder.append(value);
  }
}
