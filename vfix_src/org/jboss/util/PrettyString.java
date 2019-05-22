package org.jboss.util;

public interface PrettyString {
   String toPrettyString(String var1);

   public interface Appendable {
      StringBuffer appendPrettyString(StringBuffer var1, String var2);
   }
}
