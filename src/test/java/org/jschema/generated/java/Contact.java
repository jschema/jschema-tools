package org.jschema.generated.java;
import java.util.Map;
public class Contact{
  private Map<String, Object> _fields;
  public String toJSON(){return _fields.toString();}
  public Object getfirst_name(){return _fields.get("first_name");}
  public Object getlast_name(){return _fields.get("last_name");}
  public Object getage(){return _fields.get("age");}
  public Object gettype(){return _fields.get("type");}
  public Object getemails(){return _fields.get("emails");}
  public void setfirst_name(Object first_name){_fields.put("first_name", first_name);}
  public void setlast_name(Object last_name){_fields.put("last_name", last_name);}
  public void setage(Object age){_fields.put("age", age);}
  public void settype(Object type){_fields.put("type", type);}
  public void setemails(Object emails){_fields.put("emails", emails);}

}
