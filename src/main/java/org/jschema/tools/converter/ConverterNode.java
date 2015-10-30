package org.jschema.tools.converter;

import jdk.nashorn.internal.ir.Node;

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
    throw new UnsupportedOperationException( "Need to implement" );
  }

  protected StringBuilder convertFromRawValue(  int offset, Integer indent, StringBuilder stringBuilder, Object value  )
  {
    throw new UnsupportedOperationException( "Need to implement" );
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