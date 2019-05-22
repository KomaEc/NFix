package com.beust.jcommander;

import java.util.Comparator;

class JCommander$Options$1 implements Comparator<ParameterDescription> {
   // $FF: synthetic field
   final JCommander$Options this$0;

   JCommander$Options$1(JCommander$Options var1) {
      this.this$0 = var1;
   }

   public int compare(ParameterDescription var1, ParameterDescription var2) {
      Parameter var3 = var1.getParameterAnnotation();
      Parameter var4 = var2.getParameterAnnotation();
      if (var3 != null && var3.order() != -1 && var4 != null && var4.order() != -1) {
         return Integer.compare(var3.order(), var4.order());
      } else if (var3 != null && var3.order() != -1) {
         return -1;
      } else {
         return var4 != null && var4.order() != -1 ? 1 : var1.getLongestName().compareTo(var2.getLongestName());
      }
   }
}
