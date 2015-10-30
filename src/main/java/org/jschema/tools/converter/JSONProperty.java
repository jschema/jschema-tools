package org.jschema.tools.converter;

public class JSONProperty
{
  String _name;
  ConverterNode _valueParser;

  public JSONProperty( String name, ConverterNode valueParser )
  {
    _name = name;
    _valueParser = valueParser;
  }

  public String getName()
  {
    return _name;
  }

  public ConverterNode getValueParser()
  {
    return _valueParser;
  }
}
