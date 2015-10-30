package org.jschema.tools;

import handbuilt.Contact;
import jdk.nashorn.internal.ir.Node;
import junit.framework.Assert;
import org.jschema.tools.converter.ArrayNode;
import org.jschema.tools.converter.StringNode;
import org.junit.Test;

import java.util.List;

public class BootstrapTest
{

  @Test
  public void testEval()
  {
    Assert.assertEquals(2, JSchemaUtils.evalJS( "1 + 1" ));
  }

  @Test
  public void testParsing()
  {
    Node node = JSchemaUtils.parseJSON( "[[\"asdf\"]]" );

    ArrayNode<List<String>> converter = new ArrayNode<List<String>>(
      new ArrayNode<String>(
        new StringNode()
      )
    );

    List<List<String>> values = converter.toJava( node );

    Assert.assertEquals( 1, values.size() );
    Assert.assertEquals( 1, values.get(0).size() );
    Assert.assertEquals( "asdf", values.get(0).get( 0 ) );
  }

  @Test
  public void testHandBuildConversionToJava() {

    String contactStr = "{\"first_name\": \"Carson\",\"last_name\": \"Gross\",\"age\": 39,\"type\": \"friend\", \"emails\": [\"carson@example.com\", \"carson2@example.com\"]}";

    Contact contact = Contact.parse( contactStr );

    Assert.assertEquals( "Carson", contact.getFirstName() );
    Assert.assertEquals( "Gross", contact.getLastName() );
    Assert.assertEquals( 39, contact.getAge().intValue() );
    Assert.assertEquals( Contact.Type.friend, contact.getType() );
    Assert.assertEquals( 2, contact.getEmails().size() );
    Assert.assertEquals( "carson@example.com", contact.getEmails().get(0) );
    Assert.assertEquals( "carson2@example.com", contact.getEmails().get( 1 ) );

    System.out.println(contact.toString());
    System.out.println(contact.toJSON());
  }

  @Test
  public void testConvertJSONToJSchemaPassThrough() {
    String x = JSchemaUtils.convertJSONToJSchema( "{}" );
    Assert.assertEquals( "foo", x );
  }

}
