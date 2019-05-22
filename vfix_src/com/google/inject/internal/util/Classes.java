package com.google.inject.internal.util;

import com.google.common.base.Preconditions;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class Classes {
   public static boolean isInnerClass(Class<?> clazz) {
      return !Modifier.isStatic(clazz.getModifiers()) && clazz.getEnclosingClass() != null;
   }

   public static boolean isConcrete(Class<?> clazz) {
      int modifiers = clazz.getModifiers();
      return !clazz.isInterface() && !Modifier.isAbstract(modifiers);
   }

   public static String toString(Member member) {
      Class<? extends Member> memberType = memberType(member);
      String var2;
      String var3;
      if (memberType == Method.class) {
         var2 = String.valueOf(String.valueOf(member.getDeclaringClass().getName()));
         var3 = String.valueOf(String.valueOf(member.getName()));
         return (new StringBuilder(3 + var2.length() + var3.length())).append(var2).append(".").append(var3).append("()").toString();
      } else if (memberType == Field.class) {
         var2 = String.valueOf(String.valueOf(member.getDeclaringClass().getName()));
         var3 = String.valueOf(String.valueOf(member.getName()));
         return (new StringBuilder(1 + var2.length() + var3.length())).append(var2).append(".").append(var3).toString();
      } else if (memberType == Constructor.class) {
         return String.valueOf(member.getDeclaringClass().getName()).concat(".<init>()");
      } else {
         throw new AssertionError();
      }
   }

   public static Class<? extends Member> memberType(Member member) {
      Preconditions.checkNotNull(member, "member");
      if (member instanceof Field) {
         return Field.class;
      } else if (member instanceof Method) {
         return Method.class;
      } else if (member instanceof Constructor) {
         return Constructor.class;
      } else {
         String var1 = String.valueOf(String.valueOf(member.getClass()));
         throw new IllegalArgumentException((new StringBuilder(45 + var1.length())).append("Unsupported implementation class for Member, ").append(var1).toString());
      }
   }
}
