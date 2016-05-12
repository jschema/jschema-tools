package org.jschema.generated.java;
import java.util.*;

import org.jschema.parser.*;
public class Invoice2{
  private Map<String, Object> _fields = new HashMap<String, Object>();

  public static Invoice2 parse(String jsonString){
    Invoice2 newInvoice2 = new Invoice2();
    Map<String, Object> jsonObject = (Map) new Parser(jsonString).parse();
    Iterator it = jsonObject.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry)it.next();
      if(pair.getValue() instanceof Map){
        Object obj = makeObject(newInvoice2, (String)pair.getKey(), (Map)pair.getValue());
        newInvoice2._fields.put((String) pair.getKey(), obj);
      }
      else if(pair.getValue() instanceof List){
        List list = makeList(newInvoice2, (String)pair.getKey(), (List)pair.getValue());
        newInvoice2._fields.put((String) pair.getKey(), list);
      }
      else{
        newInvoice2._fields.put((String) pair.getKey(), pair.getValue());
      }
    }
    return newInvoice2;
  }
  public static Object makeObject(Invoice2 newInvoice2, String key, Map value){
    if(key.equals("customer")){
      Invoice2.Customer c = newInvoice2.new Customer();
      c = (Customer) makeCustomer(c, key, value);
      return c;
    }
    if(key.equals("to_address")){
      Invoice2.To_address t = newInvoice2.new To_address();
      t = (To_address) makeTo_address(t, key, value);
      return t;
    }
    if(key.equals("line_items")){
      Invoice2.Line_items l = newInvoice2.new Line_items();
      l = (Line_items) makeLine_items(l, key, value);
      return l;
    }
    if(key.equals("Nest")){
      Invoice2.Nest N = newInvoice2.new Nest();
      N = (Nest) makeNest(N, key, value);
      return N;
    }
    return null;
  }
  public static Object makeCustomer(Customer newCustomer, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newCustomer._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newCustomer;
  }
  public static Object makeTo_address(To_address newTo_address, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newTo_address._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newTo_address;
  }
  public static Object makeLine_items(Line_items newLine_items, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newLine_items._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newLine_items;
  }
  public static Object makeNest(Nest newNest, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      if(pair.getKey().toString().equals("Nested")){
        Nest.Nested N = newNest.new Nested();
        N = (Nest.Nested) makeNested(N, (String) pair.getKey(), (Map) pair.getValue());
        newNest._fields.put((String) pair.getKey(), N);
      }
      else newNest._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newNest;
  }
  public static Object makeNested(Nest.Nested newNested, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newNested._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newNested;
  }
  public static List makeList(Invoice2 newInvoice2, String key, List value){
    List<Object> list = new ArrayList<>();
    for(int i = 0; i < value.size(); i++) {
      if(value.get(i) instanceof Map){
        Object result = makeObject(newInvoice2, key, (Map) value.get(i));
        list.add(result);
      }
      else{
        list.add(value.get(i));
      }
    }
    return list;
  }
  public String toJSON(){return _fields.toString();}

  public String getId(){return (String) _fields.get("id");}
  public void setId(String id){_fields.put("id", id);}

  public Date getCreated_at(){return (Date) _fields.get("created_at");}
  public void setCreated_at(Date created_at){_fields.put("created_at", created_at);}

  public Date getUpdated_at(){return (Date) _fields.get("updated_at");}
  public void setUpdated_at(Date updated_at){_fields.put("updated_at", updated_at);}

  public String getEmail(){return (String) _fields.get("email");}
  public void setEmail(String email){_fields.put("email", email);}

  public double getTotal(){return (double) _fields.get("total");}
  public void setTotal(double total){_fields.put("total", total);}

  public double getSubtotal(){return (double) _fields.get("subtotal");}
  public void setSubtotal(double subtotal){_fields.put("subtotal", subtotal);}

  public double getTax(){return (double) _fields.get("tax");}
  public void setTax(double tax){_fields.put("tax", tax);}

  public String getNotes(){return (String) _fields.get("notes");}
  public void setNotes(String notes){_fields.put("notes", notes);}

  public Customer getCustomer(){return (Customer) _fields.get("customer");}
  public void setCustomer(Customer customer){_fields.put("customer", customer);}

  public class Customer{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public String toJSON(){return _fields.toString();}

    public String getEmail(){return (String) _fields.get("email");}
    public void setEmail(String email){_fields.put("email", email);}

    public String getFirst_name(){return (String) _fields.get("first_name");}
    public void setFirst_name(String first_name){_fields.put("first_name", first_name);}

    public String getLast_name(){return (String) _fields.get("last_name");}
    public void setLast_name(String last_name){_fields.put("last_name", last_name);}


  }
  public To_address getTo_address(){return (To_address) _fields.get("to_address");}
  public void setTo_address(To_address to_address){_fields.put("to_address", to_address);}

  public class To_address{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public String toJSON(){return _fields.toString();}

    public String getAddress1(){return (String) _fields.get("address1");}
    public void setAddress1(String address1){_fields.put("address1", address1);}

    public String getAddress2(){return (String) _fields.get("address2");}
    public void setAddress2(String address2){_fields.put("address2", address2);}

    public String getZip(){return (String) _fields.get("zip");}
    public void setZip(String zip){_fields.put("zip", zip);}

    public String getState(){return (String) _fields.get("state");}
    public void setState(String state){_fields.put("state", state);}

    public String getCountry(){return (String) _fields.get("country");}
    public void setCountry(String country){_fields.put("country", country);}


  }
  public List<Line_items> getLine_items(){return (List<Line_items>) _fields.get("line_items");}
  public void setLine_items(List<Line_items> line_items){_fields.put("line_items", line_items);}

  public class Line_items{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public String toJSON(){return _fields.toString();}

    public String getSku(){return (String) _fields.get("sku");}
    public void setSku(String sku){_fields.put("sku", sku);}

    public String getDescription(){return (String) _fields.get("description");}
    public void setDescription(String description){_fields.put("description", description);}

    public String getNotes(){return (String) _fields.get("notes");}
    public void setNotes(String notes){_fields.put("notes", notes);}

    public int getCount(){return (int) _fields.get("count");}
    public void setCount(int count){_fields.put("count", count);}

    public double getPrice(){return (double) _fields.get("price");}
    public void setPrice(double price){_fields.put("price", price);}

    public double getSubtotal(){return (double) _fields.get("subtotal");}
    public void setSubtotal(double subtotal){_fields.put("subtotal", subtotal);}


  }
  public List<String> getList(){return (List<String>) _fields.get("list");}
  public void setList(List<String> list){_fields.put("list", list);}

  public Nest getNest(){return (Nest) _fields.get("Nest");}
  public void setNest(Nest Nest){_fields.put("Nest", Nest);}

  public class Nest{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public String toJSON(){return _fields.toString();}

    public Nested getNested(){return (Nested) _fields.get("Nested");}
    public void setNested(Nested Nested){_fields.put("Nested", Nested);}

    public class Nested{
      private Map<String, Object> _fields = new HashMap<String, Object>();

      public String toJSON(){return _fields.toString();}

      public int getInnerVal(){return (int) _fields.get("InnerVal");}
      public void setInnerVal(int InnerVal){_fields.put("InnerVal", InnerVal);}


    }
    public String getNonNested(){return (String) _fields.get("NonNested");}
    public void setNonNested(String NonNested){_fields.put("NonNested", NonNested);}


  }

}
