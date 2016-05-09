package org.jschema.generated.java;
import java.util.*;

import org.jschema.parser.*;
public class Contact{
  private Map<String, Object> _fields = new HashMap<String, Object>();

  public static Contact parse(String jsonString){
    Contact newContact = new Contact();
    Map<String, Object> jsonObject = (Map) new Parser(jsonString).parse();
    Iterator it = jsonObject.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry)it.next();
      if(pair.getValue() instanceof Map){
        Object obj = makeObject(newContact, (String)pair.getKey(), (Map)pair.getValue());
        newContact._fields.put((String) pair.getKey(), obj);
      }
      else{
        newContact._fields.put((String) pair.getKey(), pair.getValue());
      }
    }
    return newContact;
  }
  public static Object makeObject(Contact newContact, String key, Map value){
    if(key.equals("customer")){
      Contact.Customer c = newContact.new Customer();
      c._fields = value;
      return c;
    }
    return null;
}
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
    FRIEND,
    CUSTOMER,
    SUPPLIER
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

    public int getAge(){return (int) _fields.get("age");}
    public void setAge(int age){_fields.put("age", age);}


  }

}
