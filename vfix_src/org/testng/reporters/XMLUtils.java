package org.testng.reporters;

import java.text.StringCharacterIterator;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;
import org.testng.internal.Nullable;

public final class XMLUtils {
   private static final String EOL = System.getProperty("line.separator");

   private XMLUtils() {
   }

   public static String xml(String indent, String name, @Nullable String content, @Nullable Properties attributes) {
      IBuffer result = Buffer.create();
      xmlOpen(result, indent, name, attributes, true);
      if (content != null) {
         result.append(content);
      }

      xmlClose(result, "", name, extractComment(name, attributes));
      return result.toString();
   }

   public static String extractComment(String tag, Properties properties) {
      if (properties != null && !"span".equals(tag)) {
         String[] attributes = new String[]{"id", "name", "class"};
         String[] arr$ = attributes;
         int len$ = attributes.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String a = arr$[i$];
            String comment = properties.getProperty(a);
            if (comment != null) {
               return " <!-- " + comment.replaceAll("[-]{2,}", "-") + " -->";
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public static void xmlOptional(IBuffer result, String sp, String elementName, Boolean value, Properties attributes) {
      if (null != value) {
         xmlRequired(result, sp, elementName, value.toString(), attributes);
      }

   }

   public static void xmlOptional(IBuffer result, String sp, String elementName, @Nullable String value, Properties attributes) {
      if (null != value) {
         xmlRequired(result, sp, elementName, value, attributes);
      }

   }

   public static void xmlRequired(IBuffer result, String sp, String elementName, @Nullable String value, @Nullable Properties attributes) {
      result.append(xml(sp, elementName, value, attributes));
   }

   public static void xmlOpen(IBuffer result, String indent, String tag, Properties attributes) {
      xmlOpen(result, indent, tag, attributes, false);
   }

   public static void appendAttributes(IBuffer result, Properties attributes) {
      if (null != attributes) {
         Iterator i$ = attributes.entrySet().iterator();

         while(i$.hasNext()) {
            Object element = i$.next();
            Entry entry = (Entry)element;
            String key = entry.getKey().toString();
            String value = escape(entry.getValue().toString());
            result.append(" ").append(key).append("=\"").append(value).append("\"");
         }
      }

   }

   public static void xmlOpen(IBuffer result, String indent, String tag, Properties attributes, boolean noNewLine) {
      result.append(indent).append("<").append(tag);
      appendAttributes(result, attributes);
      result.append(">");
      if (!noNewLine) {
         result.append(EOL);
      }

   }

   public static void xmlClose(IBuffer result, String indent, String tag, String comment) {
      result.append(indent).append("</").append(tag).append(">").append(comment != null ? comment : "").append(EOL);
   }

   public static String escape(String input) {
      if (input == null) {
         return null;
      } else {
         StringBuilder result = new StringBuilder();
         StringCharacterIterator iterator = new StringCharacterIterator(input);

         for(char character = iterator.current(); character != '\uffff'; character = iterator.next()) {
            if (character == '<') {
               result.append("&lt;");
            } else if (character == '>') {
               result.append("&gt;");
            } else if (character == '"') {
               result.append("&quot;");
            } else if (character == '\'') {
               result.append("&#039;");
            } else if (character == '&') {
               result.append("&amp;");
            } else {
               result.append(character);
            }
         }

         return result.toString();
      }
   }
}
