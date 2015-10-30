package org.jschema.tools;

import org.jschema.tools.converter.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class JSchemaObject
{
  Map<String, Object> _values = new HashMap<>();

  public void set( String property, Object val )
  {
    _values.put(property, val);
  }

  public Object get( String property )
  {
    return _values.get(property);
  }

  public Map<String, Object> getValues() {
    return _values;
  }

  //============================================================
  //  Grammar expression
  //============================================================

  protected static <T extends JSchemaObject> StructNode<T> struct( Callable<T> ctor, JSONProperty... properties)
  {
    return new StructNode<T>( ctor, properties );
  }

  protected static JSONProperty prop( String name, ConverterNode valueParser )
  {
    return new JSONProperty( name, valueParser );
  }

  protected static StringNode string()
  {
    return new StringNode();
  }

  protected static IntNode _int()
  {
    return new IntNode();
  }

  protected static BooleanNode _boolean()
  {
    return new BooleanNode();
  }

  protected static DateNode date()
  {
    return new DateNode();
  }

  protected static URINode uri()
  {
    return new URINode();
  }

  protected static NumberNode number()
  {
    return new NumberNode();
  }

  protected static WildcardNode wildcard()
  {
    return new WildcardNode();
  }

  protected static <T> ArrayNode<T> array( ConverterNode<T> component )
  {
    return new ArrayNode<T>( component );
  }

  protected static <T extends Enum<T>> EnumNode<T> _enum( Class<T> clazz )
  {
    return new EnumNode<T>( clazz );
  }

  //============================================================
  //  Utilities
  //============================================================

  protected static <T> T convert( ConverterNode<T> converter, String str )
  {
    return converter.toJava( JSchemaUtils.parseJSON( str ) );
  }

  public String toString()
  {
    return prettyPrint( 2 );
  }

  public String toJSON() {
    return prettyPrint( null );
  }

  public <T extends JSchemaObject> T to(Class<T> clazz) {
    try
    {
      T that = clazz.newInstance();
      that._values = new HashMap<>( this._values );
      return that;
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  protected abstract String prettyPrint( Integer indent );
}