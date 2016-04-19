package org.jschema.generated.java;
import java.util.*;
public class Contact{
  private Map<String, Object> _fields = new HashMap<String, Object>();
  public String toJSON(){return _fields.toString();}
  public String getfirst_name(){return (String) _fields.get("first_name");}
  public void setfirst_name(Object first_name){_fields.put("first_name", first_name);}
  public String getlast_name(){return (String) _fields.get("last_name");}
  public void setlast_name(Object last_name){_fields.put("last_name", last_name);}
  public int getage(){return (int) _fields.get("age");}
  public void setage(Object age){_fields.put("age", age);}
  public ARRAY gettype(){return (ARRAY) _fields.get("type");}
  public void settype(Object type){_fields.put("type", type);}
  public ARRAY getemails(){return (ARRAY) _fields.get("emails");}
  public void setemails(Object emails){_fields.put("emails", emails);}
  public OBJECT getobject(){return (OBJECT) _fields.get("object");}
  public void setobject(Object object){_fields.put("object", object);}

}
