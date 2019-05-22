package com.gzoltar.instrumentation.testing;

import java.util.HashSet;
import java.util.Iterator;

public class TestFilter {
   private HashSet<String> toExecute = new HashSet();
   private HashSet<String> notToExecute = new HashSet();

   public boolean lookup(String var1, String var2) {
      var2 = var1 + "#" + var2;
      if (this.notToExecute.contains(var2)) {
         return false;
      } else if (this.toExecute.contains(var2)) {
         return true;
      } else {
         var1 = var1 + "#";
         Iterator var4 = this.toExecute.iterator();

         do {
            if (!var4.hasNext()) {
               return true;
            }
         } while(!((String)var4.next()).startsWith(var1));

         return false;
      }
   }
}
