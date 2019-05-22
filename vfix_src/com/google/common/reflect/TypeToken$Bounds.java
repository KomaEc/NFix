package com.google.common.reflect;

import java.lang.reflect.Type;

class TypeToken$Bounds {
   private final Type[] bounds;
   private final boolean target;

   TypeToken$Bounds(Type[] bounds, boolean target) {
      this.bounds = bounds;
      this.target = target;
   }

   boolean isSubtypeOf(Type supertype) {
      Type[] var2 = this.bounds;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Type bound = var2[var4];
         if (TypeToken.of(bound).isSubtypeOf(supertype) == this.target) {
            return this.target;
         }
      }

      return !this.target;
   }

   boolean isSupertypeOf(Type subtype) {
      TypeToken<?> type = TypeToken.of(subtype);
      Type[] var3 = this.bounds;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Type bound = var3[var5];
         if (type.isSubtypeOf(bound) == this.target) {
            return this.target;
         }
      }

      return !this.target;
   }
}
