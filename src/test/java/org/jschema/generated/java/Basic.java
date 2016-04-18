package org.jschema.generated.java;
import java.util.Map;
public class Basic{
  private Map<String, Object> _fields;
  public String toJSON(){return _fields.toString();}
  public Object getname(){return _fields.get("name");}
  public Object getage(){return _fields.get("age");}
  public void setname(Object name){_fields.put("name", name);}
  public void setage(Object age){_fields.put("age", age);}

}
