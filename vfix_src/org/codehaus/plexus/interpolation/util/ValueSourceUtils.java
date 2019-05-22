package org.codehaus.plexus.interpolation.util;

import java.util.Collection;
import java.util.Iterator;

public final class ValueSourceUtils {
   private ValueSourceUtils() {
   }

   public static String trimPrefix(String expression, Collection possiblePrefixes, boolean allowUnprefixedExpressions) {
      if (expression == null) {
         return null;
      } else {
         String realExpr = null;
         Iterator it = possiblePrefixes.iterator();

         while(it.hasNext()) {
            String prefix = (String)it.next();
            if (expression.startsWith(prefix)) {
               realExpr = expression.substring(prefix.length());
               if (realExpr.startsWith(".")) {
                  realExpr = realExpr.substring(1);
               }
               break;
            }
         }

         if (realExpr == null && allowUnprefixedExpressions) {
            realExpr = expression;
         }

         return realExpr;
      }
   }
}
