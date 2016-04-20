package org.jschema.generated.java;
import java.util.*;

public class Contact{
  private Map<String, Object> _fields = new HashMap<String, Object>();

  public String toJSON(){return _fields.toString();}

  public String getFirst_name(){return (String) _fields.get("first_name");}
  public void setFirst_name(Object first_name){_fields.put("first_name", first_name);}

  public String getLast_name(){return (String) _fields.get("last_name");}
  public void setLast_name(Object last_name){_fields.put("last_name", last_name);}

  public int getAge(){return (int) _fields.get("age");}
  public void setAge(Object age){_fields.put("age", age);}

  public List<type>  getType(){return (List<type> ) _fields.get("type");}
  public void setType(Object type){_fields.put("type", type);}

  public List<String>  getEmails(){return (List<String> ) _fields.get("emails");}
  public void setEmails(Object emails){_fields.put("emails", emails);}

  public Customer getCustomer(){return (Customer) _fields.get("customer");}
  public void setCustomer(Object customer){_fields.put("customer", customer);}

  public class Customer{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public String toJSON(){return _fields.toString();}

    public String getName(){return (String) _fields.get("name");}
    public void setName(Object name){_fields.put("name", name);}


  }

}
