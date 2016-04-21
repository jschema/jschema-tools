package org.jschema.generated.java;
import java.util.*;

public class Contact{
  private Map<String, Object> _fields = new HashMap<String, Object>();

  public String toJSON(){return _fields.toString();}

  public String getFirst_name(){return (String) _fields.get("first_name");}
  public void setFirst_name(String first_name){_fields.put("first_name", first_name);}

  public String getLast_name(){return (String) _fields.get("last_name");}
  public void setLast_name(String last_name){_fields.put("last_name", last_name);}

  public int getAge(){return (int) _fields.get("age");}
  public void setAge(int age){_fields.put("age", age);}

  public List<Type> getType(){return (List<Type>) _fields.get("type");}
  public void setType(List<Type> type){_fields.put("type", type);}

  public enum Type{
    friend,
    customer,
    supplier
  }

  public List<String> getEmails(){return (List<String>) _fields.get("emails");}
  public void setEmails(List<String> emails){_fields.put("emails", emails);}

  public Customer getCustomer(){return (Customer) _fields.get("customer");}
  public void setCustomer(Customer customer){_fields.put("customer", customer);}

  public class Customer{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public String toJSON(){return _fields.toString();}

    public String getName(){return (String) _fields.get("name");}
    public void setName(String name){_fields.put("name", name);}


  }

}
