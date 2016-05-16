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
  private static Object makeObject(Invoice2 newInvoice2, String key, Map value){
    if(key.equals("customer")){
      Invoice2.Customer c = newInvoice2.new Customer();
      c = (Customer) makeCustomer(c, key, value);
      return c;
    }
    else if(key.equals("to_address")){
      Invoice2.To_address t = newInvoice2.new To_address();
      t = (To_address) makeTo_address(t, key, value);
      return t;
    }
    else if(key.equals("line_items")){
      Invoice2.Line_items l = newInvoice2.new Line_items();
      l = (Line_items) makeLine_items(l, key, value);
      return l;
    }
    else if(key.equals("Nest")){
      Invoice2.Nest N = newInvoice2.new Nest();
      N = (Nest) makeNest(N, key, value);
      return N;
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
  private static Object makeTo_address(To_address newTo_address, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newTo_address._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newTo_address;
  }
  private static Object makeLine_items(Line_items newLine_items, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newLine_items._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newLine_items;
  }
  private static Object makeNest(Nest newNest, String key, Map value){
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
  private static Object makeNested(Nest.Nested newNested, String key, Map value){
    Iterator it = value.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry) it.next();
      newNested._fields.put((String) pair.getKey(), pair.getValue());
    }
    return newNested;
  }
  private static List makeList(Invoice2 newInvoice2, String key, List value){
    List<Object> list = new ArrayList<>();
    for(int i = 0; i < value.size(); i++) {
      if(value.get(i) instanceof Map){
        Object result = makeObject(newInvoice2, key, (Map) value.get(i));
        list.add(result);
      }
      else if(value.get(i) instanceof List){
        List result = makeList(newInvoice2, key, (List) value.get(i));
        list.add(result);      }
      else{
        list.add(value.get(i));
      }
    }
    return list;
  }
  public java.lang.String toJSON(){return _fields.toString();}

  public java.lang.String getId(){return (java.lang.String) _fields.get("id");}
  public void setId(java.lang.String id){_fields.put("id", id);}

  public java.util.Date getCreated_at(){return (java.util.Date) _fields.get("created_at");}
  public void setCreated_at(java.util.Date created_at){_fields.put("created_at", created_at);}

  public java.util.Date getUpdated_at(){return (java.util.Date) _fields.get("updated_at");}
  public void setUpdated_at(java.util.Date updated_at){_fields.put("updated_at", updated_at);}

  public java.lang.String getEmail(){return (java.lang.String) _fields.get("email");}
  public void setEmail(java.lang.String email){_fields.put("email", email);}

  public java.lang.Double getTotal(){return (java.lang.Double) _fields.get("total");}
  public void setTotal(java.lang.Double total){_fields.put("total", total);}

  public java.lang.Double getSubtotal(){return (java.lang.Double) _fields.get("subtotal");}
  public void setSubtotal(java.lang.Double subtotal){_fields.put("subtotal", subtotal);}

  public java.lang.Double getTax(){return (java.lang.Double) _fields.get("tax");}
  public void setTax(java.lang.Double tax){_fields.put("tax", tax);}

  public java.lang.String getNotes(){return (java.lang.String) _fields.get("notes");}
  public void setNotes(java.lang.String notes){_fields.put("notes", notes);}

  public Customer getCustomer(){return (Customer) _fields.get("customer");}
  public void setCustomer(Customer customer){_fields.put("customer", customer);}

  public class Customer{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public java.lang.String getEmail(){return (java.lang.String) _fields.get("email");}
    public void setEmail(java.lang.String email){_fields.put("email", email);}

    public java.lang.String getFirst_name(){return (java.lang.String) _fields.get("first_name");}
    public void setFirst_name(java.lang.String first_name){_fields.put("first_name", first_name);}

    public java.lang.String getLast_name(){return (java.lang.String) _fields.get("last_name");}
    public void setLast_name(java.lang.String last_name){_fields.put("last_name", last_name);}


  }
  public To_address getTo_address(){return (To_address) _fields.get("to_address");}
  public void setTo_address(To_address to_address){_fields.put("to_address", to_address);}

  public class To_address{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public java.lang.String getAddress1(){return (java.lang.String) _fields.get("address1");}
    public void setAddress1(java.lang.String address1){_fields.put("address1", address1);}

    public java.lang.String getAddress2(){return (java.lang.String) _fields.get("address2");}
    public void setAddress2(java.lang.String address2){_fields.put("address2", address2);}

    public java.lang.String getZip(){return (java.lang.String) _fields.get("zip");}
    public void setZip(java.lang.String zip){_fields.put("zip", zip);}

    public java.lang.String getState(){return (java.lang.String) _fields.get("state");}
    public void setState(java.lang.String state){_fields.put("state", state);}

    public java.lang.String getCountry(){return (java.lang.String) _fields.get("country");}
    public void setCountry(java.lang.String country){_fields.put("country", country);}


  }
  public List<java.lang.Object> getLine_items(){return (List<java.lang.Object>) _fields.get("line_items");}
  public void setLine_items(List<java.lang.Object> line_items){_fields.put("line_items", line_items);}

  public class Line_items{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public java.lang.String getSku(){return (java.lang.String) _fields.get("sku");}
    public void setSku(java.lang.String sku){_fields.put("sku", sku);}

    public java.lang.String getDescription(){return (java.lang.String) _fields.get("description");}
    public void setDescription(java.lang.String description){_fields.put("description", description);}

    public java.lang.String getNotes(){return (java.lang.String) _fields.get("notes");}
    public void setNotes(java.lang.String notes){_fields.put("notes", notes);}

    public java.lang.Integer getCount(){return (java.lang.Integer) _fields.get("count");}
    public void setCount(java.lang.Integer count){_fields.put("count", count);}

    public java.lang.Double getPrice(){return (java.lang.Double) _fields.get("price");}
    public void setPrice(java.lang.Double price){_fields.put("price", price);}

    public java.lang.Double getSubtotal(){return (java.lang.Double) _fields.get("subtotal");}
    public void setSubtotal(java.lang.Double subtotal){_fields.put("subtotal", subtotal);}


  }
  public List<java.lang.String> getList(){return (List<java.lang.String>) _fields.get("list");}
  public void setList(List<java.lang.String> list){_fields.put("list", list);}

  public Nest getNEST(){return (NEST) _fields.get("NEST");}
  public void setNEST(NEST NEST){_fields.put("NEST", NEST);}

  public class Nest{
    private Map<String, Object> _fields = new HashMap<String, Object>();

    public Nested getNESTED(){return (NESTED) _fields.get("NESTED");}
    public void setNESTED(NESTED NESTED){_fields.put("NESTED", NESTED);}

    public class Nested{
      private Map<String, Object> _fields = new HashMap<String, Object>();

      public java.lang.Integer getINNERVAL(){return (java.lang.Integer) _fields.get("INNERVAL");}
      public void setINNERVAL(java.lang.Integer INNERVAL){_fields.put("INNERVAL", INNERVAL);}


    }
    public java.lang.String getNONNESTED(){return (java.lang.String) _fields.get("NONNESTED");}
    public void setNONNESTED(java.lang.String NONNESTED){_fields.put("NONNESTED", NONNESTED);}


  }

}
