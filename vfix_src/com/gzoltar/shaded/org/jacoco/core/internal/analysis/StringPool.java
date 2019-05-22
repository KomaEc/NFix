package com.gzoltar.shaded.org.jacoco.core.internal.analysis;

import java.util.HashMap;
import java.util.Map;

public final class StringPool {
   private static final String[] EMPTY_ARRAY = new String[0];
   private final Map<String, String> pool = new HashMap(1024);

   public String get(String s) {
      if (s == null) {
         return null;
      } else {
         String norm = (String)this.pool.get(s);
         if (norm == null) {
            this.pool.put(s, s);
            return s;
         } else {
            return norm;
         }
      }
   }

   public String[] get(String[] arr) {
      if (arr == null) {
         return null;
      } else if (arr.length == 0) {
         return EMPTY_ARRAY;
      } else {
         for(int i = 0; i < arr.length; ++i) {
            arr[i] = this.get(arr[i]);
         }

         return arr;
      }
   }
}
