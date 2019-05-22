package org.codehaus.groovy.tools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class Utilities {
   private static final Set<String> INVALID_JAVA_IDENTIFIERS = new HashSet(Arrays.asList("abstract assert boolean break byte case catch char class const continue default do double else enum extends final finally float for goto if implements import instanceof int interface long native new package private protected public short static strictfp super switch synchronized this throw throws transient try void volatile while true false null".split(" ")));
   private static String eol = System.getProperty("line.separator", "\n");

   public static String repeatString(String pattern, int repeats) {
      StringBuffer buffer = new StringBuffer(pattern.length() * repeats);

      for(int i = 0; i < repeats; ++i) {
         buffer.append(pattern);
      }

      return new String(buffer);
   }

   public static String eol() {
      return eol;
   }

   public static boolean isJavaIdentifier(String name) {
      if (name.length() != 0 && !INVALID_JAVA_IDENTIFIERS.contains(name)) {
         char[] chars = name.toCharArray();
         if (!Character.isJavaIdentifierStart(chars[0])) {
            return false;
         } else {
            for(int i = 1; i < chars.length; ++i) {
               if (!Character.isJavaIdentifierPart(chars[i])) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }
}
