package com.beust.jcommander;

import java.lang.reflect.Type;

class JCommander$2 implements IStringConverter {
   // $FF: synthetic field
   final Parameterized val$parameterized;
   // $FF: synthetic field
   final JCommander this$0;

   JCommander$2(JCommander var1, Parameterized var2) {
      this.this$0 = var1;
      this.val$parameterized = var2;
   }

   public Object convert(String var1) {
      Type var2 = this.val$parameterized.findFieldGenericType();
      return this.this$0.convertValue(this.val$parameterized, var2 instanceof Class ? (Class)var2 : String.class, (String)null, var1);
   }
}
