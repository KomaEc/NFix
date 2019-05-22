package com.gzoltar.instrumentation.transformer;

public final class AccessFlags {
   private static boolean is(int var0, int var1) {
      return (var0 & var1) != 0;
   }

   public static boolean isAbstract(int var0) {
      return is(var0, 1024);
   }

   public static boolean isAnnotation(int var0) {
      return is(var0, 8192);
   }

   public static boolean isBridge(int var0) {
      return is(var0, 64);
   }

   public static boolean isEnum(int var0) {
      return is(var0, 16384);
   }

   public static boolean isFinal(int var0) {
      return is(var0, 16);
   }

   public static boolean isInterface(int var0) {
      return is(var0, 512);
   }

   public static boolean isMandated(int var0) {
      return is(var0, 32768);
   }

   public static boolean isNative(int var0) {
      return is(var0, 256);
   }

   public static boolean isPrivate(int var0) {
      return is(var0, 2);
   }

   public static boolean isProtected(int var0) {
      return is(var0, 4);
   }

   public static boolean isPublic(int var0) {
      return is(var0, 1);
   }

   public static boolean isStatic(int var0) {
      return is(var0, 8);
   }

   public static boolean isStrict(int var0) {
      return is(var0, 2048);
   }

   public static boolean isSuper(int var0) {
      return is(var0, 32);
   }

   public static boolean isSynchronized(int var0) {
      return is(var0, 32);
   }

   public static boolean isSynthetic(int var0) {
      return is(var0, 4096);
   }

   public static boolean isTransient(int var0) {
      return is(var0, 128);
   }

   public static boolean isVarArgs(int var0) {
      return is(var0, 128);
   }

   public static boolean isVolatile(int var0) {
      return is(var0, 64);
   }
}
