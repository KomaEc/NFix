package com.gzoltar.instrumentation.runtime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RuntimeData {
   private static final Map<String, int[]> classHitArrays = new HashMap();

   public static int[] getHitArray(String var0, int var1) {
      synchronized(classHitArrays) {
         if (classHitArrays.containsKey(var0)) {
            return (int[])classHitArrays.get(var0);
         } else {
            int[] var4 = new int[var1];
            classHitArrays.put(var0, var4);
            return var4;
         }
      }
   }

   public static Map<String, int[]> getMapHitArray() {
      synchronized(classHitArrays) {
         return classHitArrays;
      }
   }

   public static int[] getMapHitArray(String var0) {
      synchronized(classHitArrays) {
         return (int[])classHitArrays.get(var0);
      }
   }

   public static Map<String, int[]> getMapHitArrayAndClear() {
      synchronized(classHitArrays) {
         HashMap var1 = new HashMap(classHitArrays);
         Iterator var2 = classHitArrays.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();

            for(int var4 = 0; var4 < ((int[])classHitArrays.get(var3)).length; ++var4) {
               ((int[])classHitArrays.get(var3))[var4] = 0;
            }
         }

         return var1;
      }
   }

   public static int[] getMapHitArrayAndClear(String var0) {
      synchronized(classHitArrays) {
         int[] var2 = (int[])((int[])classHitArrays.get(var0)).clone();

         for(int var3 = 0; var3 < ((int[])classHitArrays.get(var0)).length; ++var3) {
            ((int[])classHitArrays.get(var0))[var3] = 0;
         }

         return var2;
      }
   }
}
