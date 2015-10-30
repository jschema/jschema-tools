package org.jschema.tools.converter;

import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;

import java.net.URI;

public class URINode extends ConverterNode<URI>
{
  @Override
  protected URI _toJava( Node n ) throws Exception
  {
    check( n, LiteralNode.class, "URI" );
    return new URI( ((LiteralNode)n).getString() );
  }

  @Override
  public StringBuilder prettyPrint( int offset, Integer indent, StringBuilder stringBuilder, URI value )
  {
    return stringBuilder.append(quote(value.toString()));
  }

}
