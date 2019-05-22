package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.util.StringTokenizer;

public class IntArrayEditor extends PropertyEditorSupport {
   public void setAsText(String text) {
      StringTokenizer stok = new StringTokenizer(text, ",\r\n");
      int[] theValue = new int[stok.countTokens()];

      for(int var4 = 0; stok.hasMoreTokens(); theValue[var4++] = Integer.decode(stok.nextToken())) {
      }

      this.setValue(theValue);
   }

   public String getAsText() {
      int[] theValue = (int[])((int[])this.getValue());
      StringBuffer text = new StringBuffer();
      int length = theValue == null ? 0 : theValue.length;

      for(int n = 0; n < length; ++n) {
         if (n > 0) {
            text.append(',');
         }

         text.append(theValue[n]);
      }

      return text.toString();
   }
}
