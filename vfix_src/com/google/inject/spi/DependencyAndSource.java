package com.google.inject.spi;

import com.google.inject.internal.util.StackTraceElements;
import java.lang.reflect.Member;

public final class DependencyAndSource {
   private final Dependency<?> dependency;
   private final Object source;

   public DependencyAndSource(Dependency<?> dependency, Object source) {
      this.dependency = dependency;
      this.source = source;
   }

   public Dependency<?> getDependency() {
      return this.dependency;
   }

   public String getBindingSource() {
      if (this.source instanceof Class) {
         return StackTraceElements.forType((Class)this.source).toString();
      } else {
         return this.source instanceof Member ? StackTraceElements.forMember((Member)this.source).toString() : this.source.toString();
      }
   }

   public String toString() {
      Dependency<?> dep = this.getDependency();
      Object source = this.getBindingSource();
      String var3;
      if (dep != null) {
         var3 = String.valueOf(String.valueOf(dep));
         String var4 = String.valueOf(String.valueOf(source));
         return (new StringBuilder(22 + var3.length() + var4.length())).append("Dependency: ").append(var3).append(", source: ").append(var4).toString();
      } else {
         var3 = String.valueOf(String.valueOf(source));
         return (new StringBuilder(8 + var3.length())).append("Source: ").append(var3).toString();
      }
   }
}
