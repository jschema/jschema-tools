package org.jschema.tools.converter;

import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.ir.PropertyNode;
import org.jschema.tools.JSchemaObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

public class StructNode<T extends JSchemaObject> extends ConverterNode<T>
{
  private final Callable<T> _ctor;
  private Map<String, ConverterNode<T>> _components = new HashMap<String, ConverterNode<T>>(  );

  public StructNode( Callable<T> ctor, JSONProperty... properties)
  {
    _ctor = ctor;
    for( JSONProperty property : properties )
    {
      _components.put( property.getName(), property.getValueParser() );
    }
  }

  @Override
  protected T _toJava( Node n ) throws Exception
  {
    check( n, ObjectNode.class, "object" );
    T instance = _ctor.call();
    for( PropertyNode element : ((ObjectNode)n).getElements() )
    {
      String name = element.getKeyName();
      Expression value = element.getValue();

      ConverterNode<T> valParser = _components.get( name );
      if(valParser != null)
      {
        instance.set( name, valParser.toJava( value ) );
      }
      else
      {
        instance.set( name, convertToRawValue( value ) );
      }
    }
    return instance;
  }

  @Override
  public StringBuilder prettyPrint( int offset, Integer indent, StringBuilder stringBuilder, T value )
  {
    stringBuilder.append( "{" );
    int newOffset = indent == null ? 0 : offset + indent;
    for( Iterator<Map.Entry<String, Object>> iterator = value.getValues().entrySet().iterator(); iterator.hasNext(); )
    {
      Map.Entry<String, Object> entry = iterator.next();
      maybeNewLine( indent, stringBuilder );
      maybeIndent( newOffset, stringBuilder );
      ConverterNode slotConverter = _components.get( entry.getKey() );
      if(slotConverter != null)
      {
        stringBuilder.append( quote( entry.getKey() ) ).append( " : " );
        slotConverter.prettyPrint( newOffset, indent, stringBuilder, entry.getValue() );
      }
      else
      {
        convertFromRawValue( newOffset, indent, stringBuilder, entry.getValue() );
      }
      if(iterator.hasNext())
      {
        stringBuilder.append( ", " );
      }
    }
    if( value.getValues().size() > 0 )
    {
      maybeNewLine( indent, stringBuilder );
      maybeIndent( offset, stringBuilder );
    }
    stringBuilder.append( "}" );
    return stringBuilder;
  }

}
