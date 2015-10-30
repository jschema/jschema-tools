package org.jschema.tools.converter;

import jdk.nashorn.internal.ir.Node;

public class WildcardNode extends ConverterNode<Object>
{
  @Override
  protected Object _toJava( Node n ) throws Exception
  {
    return convertToRawValue( n );
  }

  @Override
  public StringBuilder prettyPrint( int offset, Integer indent, StringBuilder stringBuilder, Object value )
  {
    return convertFromRawValue( offset, indent, stringBuilder, value );
  }
}
