package org.codehaus.groovy.transform.powerassert;

public class PowerAssertionError extends AssertionError {
   public PowerAssertionError(String msg) {
      super(msg);
   }

   public String toString() {
      return String.format("Assertion failed: \n\n%s\n", this.getMessage());
   }
}
