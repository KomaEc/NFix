package soot;

public class Modifier {
   public static final int ABSTRACT = 1024;
   public static final int FINAL = 16;
   public static final int INTERFACE = 512;
   public static final int NATIVE = 256;
   public static final int PRIVATE = 2;
   public static final int PROTECTED = 4;
   public static final int PUBLIC = 1;
   public static final int STATIC = 8;
   public static final int SYNCHRONIZED = 32;
   public static final int TRANSIENT = 128;
   public static final int VOLATILE = 64;
   public static final int STRICTFP = 2048;
   public static final int ANNOTATION = 8192;
   public static final int ENUM = 16384;
   public static final int SYNTHETIC = 4096;
   public static final int CONSTRUCTOR = 65536;
   public static final int DECLARED_SYNCHRONIZED = 131072;

   private Modifier() {
   }

   public static boolean isAbstract(int m) {
      return (m & 1024) != 0;
   }

   public static boolean isFinal(int m) {
      return (m & 16) != 0;
   }

   public static boolean isInterface(int m) {
      return (m & 512) != 0;
   }

   public static boolean isNative(int m) {
      return (m & 256) != 0;
   }

   public static boolean isPrivate(int m) {
      return (m & 2) != 0;
   }

   public static boolean isProtected(int m) {
      return (m & 4) != 0;
   }

   public static boolean isPublic(int m) {
      return (m & 1) != 0;
   }

   public static boolean isStatic(int m) {
      return (m & 8) != 0;
   }

   public static boolean isSynchronized(int m) {
      return (m & 32) != 0;
   }

   public static boolean isTransient(int m) {
      return (m & 128) != 0;
   }

   public static boolean isVolatile(int m) {
      return (m & 64) != 0;
   }

   public static boolean isStrictFP(int m) {
      return (m & 2048) != 0;
   }

   public static boolean isAnnotation(int m) {
      return (m & 8192) != 0;
   }

   public static boolean isEnum(int m) {
      return (m & 16384) != 0;
   }

   public static boolean isSynthetic(int m) {
      return (m & 4096) != 0;
   }

   public static boolean isConstructor(int m) {
      return (m & 65536) != 0;
   }

   public static boolean isDeclaredSynchronized(int m) {
      return (m & 131072) != 0;
   }

   public static String toString(int m) {
      StringBuffer buffer = new StringBuffer();
      if (isPublic(m)) {
         buffer.append("public ");
      } else if (isPrivate(m)) {
         buffer.append("private ");
      } else if (isProtected(m)) {
         buffer.append("protected ");
      }

      if (isAbstract(m)) {
         buffer.append("abstract ");
      }

      if (isStatic(m)) {
         buffer.append("static ");
      }

      if (isFinal(m)) {
         buffer.append("final ");
      }

      if (isSynchronized(m)) {
         buffer.append("synchronized ");
      }

      if (isNative(m)) {
         buffer.append("native ");
      }

      if (isTransient(m)) {
         buffer.append("transient ");
      }

      if (isVolatile(m)) {
         buffer.append("volatile ");
      }

      if (isStrictFP(m)) {
         buffer.append("strictfp ");
      }

      if (isAnnotation(m)) {
         buffer.append("annotation ");
      }

      if (isEnum(m)) {
         buffer.append("enum ");
      }

      if (isInterface(m)) {
         buffer.append("interface ");
      }

      return buffer.toString().trim();
   }
}
