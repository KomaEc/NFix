package com.gzoltar.shaded.org.jacoco.report.internal;

import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

class NormalizedFileNames {
   private static final BitSet LEGAL_CHARS = new BitSet();
   private final Map<String, String> mapping = new HashMap();
   private final Set<String> usedNames = new HashSet();

   public String getFileName(String id) {
      String name = (String)this.mapping.get(id);
      if (name != null) {
         return name;
      } else {
         name = this.replaceIllegalChars(id);
         name = this.ensureUniqueness(name);
         this.mapping.put(id, name);
         return name;
      }
   }

   private String replaceIllegalChars(String s) {
      StringBuilder sb = new StringBuilder(s.length());
      boolean modified = false;

      for(int i = 0; i < s.length(); ++i) {
         char c = s.charAt(i);
         if (LEGAL_CHARS.get(c)) {
            sb.append(c);
         } else {
            sb.append('_');
            modified = true;
         }
      }

      return modified ? sb.toString() : s;
   }

   private String ensureUniqueness(String s) {
      String unique = s;
      String lower = s.toLowerCase(Locale.ENGLISH);

      for(int var4 = 1; this.usedNames.contains(lower); lower = unique.toLowerCase(Locale.ENGLISH)) {
         unique = s + '~' + var4++;
      }

      this.usedNames.add(lower);
      return unique;
   }

   static {
      String legal = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWYXZ0123456789$-._";
      char[] arr$ = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWYXZ0123456789$-._".toCharArray();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         char c = arr$[i$];
         LEGAL_CHARS.set(c);
      }

   }
}
