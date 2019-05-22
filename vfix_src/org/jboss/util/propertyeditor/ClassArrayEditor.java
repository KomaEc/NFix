package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ClassArrayEditor extends PropertyEditorSupport {
   public void setAsText(String text) throws IllegalArgumentException {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      StringTokenizer tokenizer = new StringTokenizer(text, ", \t\r\n");
      ArrayList classes = new ArrayList();

      while(tokenizer.hasMoreTokens()) {
         String name = tokenizer.nextToken();

         try {
            Class<?> c = loader.loadClass(name);
            classes.add(c);
         } catch (ClassNotFoundException var7) {
            throw new IllegalArgumentException("Failed to find class: " + name);
         }
      }

      Class<?>[] theValue = new Class[classes.size()];
      classes.toArray(theValue);
      this.setValue(theValue);
   }

   public String getAsText() {
      Class<?>[] theValue = (Class[])((Class[])this.getValue());
      StringBuffer text = new StringBuffer();
      int length = theValue == null ? 0 : theValue.length;

      for(int n = 0; n < length; ++n) {
         text.append(theValue[n].getName());
         text.append(',');
      }

      text.setLength(text.length() - 1);
      return text.toString();
   }
}
