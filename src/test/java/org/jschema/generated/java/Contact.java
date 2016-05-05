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
      if(pair.getValue() instanceof HashMap){
        newContact._fields.put((String) pair.getKey(), parseInnerMap(newContact, pair.getKey(), (Map) pair.getValue()));
      }
      else if(pair.getValue() instanceof ArrayList && ((ArrayList) pair.getValue()).get(0) instanceof HashMap){
        newContact._fields.put((String) pair.getKey(), parseInnerList(newContact, pair.getKey(), (List) pair.getValue()));
      }
      else newContact._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newContact;
  }
  public static Object parseInnerMap(Contact newContact, Object key, Map value){
    if(key.toString().equals("customer")){
      Contact.Customer c = newContact.new Customer();
      c._fields = value;
      return c;
    }
    return null;
}
  public static List parseInnerList(Contact newContact, Object key, List value){
    List<Object> list = new ArrayList<>();
    for(int i = 0; i < value.size(); i++) {
      Object result = parseInnerMap(newContact, key, (Map) value.get(i));
      list.add(result);
    }
    return list;
  }
  public String toJSON(){return _fields.toString();}

  public String getFirst_name(){return (String) _fields.get("first_name");}
  public void setFirst_name(String first_name){_fields.put("first_name", first_name);}

  public String getLast_name(){return (String) _fields.get("last_name");}
  public void setLast_name(String last_name){_fields.put("last_name", last_name);}

  public int getAge(){return (int) _fields.get("age");}
  public void setAge(int age){_fields.put("age", age);}

//-------------------------------------------------------------------------------------------------------------
  //TODO change enums generation, so type if the enum type and not a list
  // it returns a string? should probably change that
  //public List<Type> getType(){return (List<Type>) _fields.get("type");}
  //public void setType(List<Type> type){_fields.put("type", type);}

  public Type getType(){return Type.valueOf((String) _fields.get("type"));}
  public void setType(Type type){_fields.put("type", type);}

//-------------------------------------------------------------------------------------------------------------
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


  }

}
