package com.google.common.base;

import java.io.Serializable;
import javax.annotation.Nullable;

final class Converter$FunctionBasedConverter<A, B> extends Converter<A, B> implements Serializable {
   private final Function<? super A, ? extends B> forwardFunction;
   private final Function<? super B, ? extends A> backwardFunction;

   private Converter$FunctionBasedConverter(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) {
      this.forwardFunction = (Function)Preconditions.checkNotNull(forwardFunction);
      this.backwardFunction = (Function)Preconditions.checkNotNull(backwardFunction);
   }

   protected B doForward(A a) {
      return this.forwardFunction.apply(a);
   }

   protected A doBackward(B b) {
      return this.backwardFunction.apply(b);
   }

   public boolean equals(@Nullable Object object) {
      if (!(object instanceof Converter$FunctionBasedConverter)) {
         return false;
      } else {
         Converter$FunctionBasedConverter<?, ?> that = (Converter$FunctionBasedConverter)object;
         return this.forwardFunction.equals(that.forwardFunction) && this.backwardFunction.equals(that.backwardFunction);
      }
   }

   public int hashCode() {
      return this.forwardFunction.hashCode() * 31 + this.backwardFunction.hashCode();
   }

   public String toString() {
      String var1 = String.valueOf(String.valueOf(this.forwardFunction));
      String var2 = String.valueOf(String.valueOf(this.backwardFunction));
      return (new StringBuilder(18 + var1.length() + var2.length())).append("Converter.from(").append(var1).append(", ").append(var2).append(")").toString();
   }

   // $FF: synthetic method
   Converter$FunctionBasedConverter(Function x0, Function x1, Object x2) {
      this(x0, x1);
   }
}
