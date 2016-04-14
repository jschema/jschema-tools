package org.jschema.endtoend;
import org.jschema.generated.java.*;

import org.junit.Test;

public class EndToEndJavaTests
{
  @Test
  public void bootstrap(){
    Basic b = new Basic("Fred", 24);
    System.out.print(b.getname());

  }
}
