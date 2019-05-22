package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class StringArrayEditor extends PropertyEditorSupport {
   Pattern commaDelim = Pattern.compile("','|[^,\r\n]+");

   static String[] parseList(String text) {
      ArrayList<String> list = new ArrayList();
      StringBuffer tmp = new StringBuffer();

      for(int n = 0; n < text.length(); ++n) {
         char c = text.charAt(n);
         switch(c) {
         case '\n':
         case '\r':
         case ',':
            if (tmp.length() > 0) {
               list.add(tmp.toString());
            }

            tmp.setLength(0);
            break;
         case '\\':
            tmp.append(c);
            if (n < text.length() && text.charAt(n + 1) == ',') {
               tmp.setCharAt(tmp.length() - 1, ',');
               ++n;
            }
            break;
         default:
            tmp.append(c);
         }
      }

      if (tmp.length() > 0) {
         list.add(tmp.toString());
      }

      String[] x = new String[list.size()];
      list.toArray(x);
      return x;
   }

   public void setAsText(String text) {
      String[] theValue = parseList(text);
      this.setValue(theValue);
   }

   public String getAsText() {
      String[] theValue = (String[])((String[])this.getValue());
      StringBuffer text = new StringBuffer();
      int length = theValue == null ? 0 : theValue.length;

      for(int n = 0; n < length; ++n) {
         String s = theValue[n];
         if (s.equals(",")) {
            text.append('\\');
         }

         text.append(s);
         text.append(',');
      }

      if (text.length() > 0) {
         text.setLength(text.length() - 1);
      }

      return text.toString();
   }
}
