package org.apache.tools.ant;

public class UnsupportedAttributeException extends BuildException {
   private String attribute;

   public UnsupportedAttributeException(String msg, String attribute) {
      super(msg);
      this.attribute = attribute;
   }

   public String getAttribute() {
      return this.attribute;
   }
}
