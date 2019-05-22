package org.jboss.logging;

public class NDC {
   private static final NDCProvider ndc;

   public static void clear() {
      ndc.clear();
   }

   public static String get() {
      return ndc.get();
   }

   public static int getDepth() {
      return ndc.getDepth();
   }

   public static String pop() {
      return ndc.pop();
   }

   public static String peek() {
      return ndc.peek();
   }

   public static void push(String message) {
      ndc.push(message);
   }

   public static void setMaxDepth(int maxDepth) {
      ndc.setMaxDepth(maxDepth);
   }

   static {
      NDCProvider n = null;
      if (NDCSupport.class.isAssignableFrom(Logger.pluginClass)) {
         try {
            n = ((NDCSupport)Logger.pluginClass.newInstance()).getNDCProvider();
         } catch (Throwable var2) {
         }
      }

      if (n == null) {
         n = new NullNDCProvider();
      }

      ndc = (NDCProvider)n;
   }
}
