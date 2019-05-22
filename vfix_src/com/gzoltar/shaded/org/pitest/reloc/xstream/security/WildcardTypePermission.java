package com.gzoltar.shaded.org.pitest.reloc.xstream.security;

public class WildcardTypePermission extends RegExpTypePermission {
   public WildcardTypePermission(String[] patterns) {
      super(getRegExpPatterns(patterns));
   }

   private static String[] getRegExpPatterns(String[] wildcards) {
      if (wildcards == null) {
         return null;
      } else {
         String[] regexps = new String[wildcards.length];

         for(int i = 0; i < wildcards.length; ++i) {
            String wildcardExpression = wildcards[i];
            StringBuffer result = new StringBuffer(wildcardExpression.length() * 2);
            result.append("(?u)");
            int length = wildcardExpression.length();

            for(int j = 0; j < length; ++j) {
               char ch = wildcardExpression.charAt(j);
               switch(ch) {
               case '$':
               case '(':
               case ')':
               case '+':
               case '.':
               case '[':
               case '\\':
               case ']':
               case '^':
               case '|':
                  result.append('\\').append(ch);
                  break;
               case '*':
                  if (j + 1 < length && wildcardExpression.charAt(j + 1) == '*') {
                     result.append("[\\P{C}]*");
                     ++j;
                     break;
                  }

                  result.append("[\\P{C}&&[^").append('.').append("]]*");
                  break;
               case '?':
                  result.append('.');
                  break;
               default:
                  result.append(ch);
               }
            }

            regexps[i] = result.toString();
         }

         return regexps;
      }
   }
}
