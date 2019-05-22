package org.apache.tools.ant;

public class UnsupportedElementException extends BuildException {
   private String element;

   public UnsupportedElementException(String msg, String element) {
      super(msg);
      this.element = element;
   }

   public String getElement() {
      return this.element;
   }
}
