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
      else if(pair.getValue() instanceof List){
        List list = makeList(newContact, (String)pair.getKey(), (List)pair.getValue());
        newContact._fields.put((String) pair.getKey(), list);
      }
      else{
        newContact._fields.put((String) pair.getKey(), pair.getValue());
      }
    }
    return newContact;
  }
  private static Object makeObject(Contact newContact, String key, Map value){
    if(key.equals("customer")){
      Contact.Customer c = newContact.new Customer();
      c = (Customer) makeCustomer(c, key, value);
      return c;
    }
    return null;
  }
  private static Object makeCustomer(Customer newCustomer, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newCustomer._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newCustomer;
  }
  private static List makeList(Contact newContact, String key, List value){
    List<Object> list = new ArrayList<>();
    for(int i = 0; i < value.size(); i++) {
      if(value.get(i) instanceof Map){
        Object result = makeObject(newContact, key, (Map) value.get(i));
        list.add(result);
      }
      else if(value.get(i) instanceof List){
        List result = makeList(newContact, key, (List) value.get(i));
        list.add(result);      }
      else{
        list.add(value.get(i));
      }
    }
    return list;
  }
  public java.lang.String toJSON(){return _fields.toString();}

  public java.lang.String getfirst_name(){return (java.lang.String) _fields.get("first_name");}
  public void setfirst_name(java.lang.String first_name){_fields.put("first_name", first_name);}

  public java.lang.String getlast_name(){return (java.lang.String) _fields.get("last_name");}
  public void setlast_name(java.lang.String last_name){_fields.put("last_name", last_name);}

  public java.lang.Integer getage(){return (java.lang.Integer) _fields.get("age");}
  public void setage(java.lang.Integer age){_fields.put("age", age);}

  public List<Type> gettype(){return (List<Type>) _fields.get("type");}
  public void settype(List<Type> type){_fields.put("type", type);}

  public enum Type{
    FRIEND,
    CUSTOMER,
    SUPPLIER
  }

  public List<java.lang.String> getemails(){return (List<java.lang.String>) _fields.get("emails");}
  public void setemails(List<java.lang.String> emails){_fields.put("emails", emails);}

  public Customer getcustomer(){return (Customer) _fields.get("customer");}
  public void setcustomer(Customer customer){_fields.put("customer", customer);}

  public class Customer{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public java.lang.String getname(){return (java.lang.String) _fields.get("name");}
    public void setname(java.lang.String name){_fields.put("name", name);}

    public java.lang.Integer getage(){return (java.lang.Integer) _fields.get("age");}
    public void setage(java.lang.Integer age){_fields.put("age", age);}


  }

}
