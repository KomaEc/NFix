package org.jboss.util;

public final class Primitives {
   public static Boolean valueOf(boolean value) {
      return value ? Boolean.TRUE : Boolean.FALSE;
   }

   public static boolean equals(double a, double b) {
      return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
   }

   public static boolean equals(float a, float b) {
      return Float.floatToIntBits(a) == Float.floatToIntBits(b);
   }

   public static boolean equals(byte[] a, int abegin, byte[] b, int bbegin, int length) {
      try {
         int i = length;

         do {
            --i;
            if (i < 0) {
               return true;
            }
         } while(a[abegin + i] == b[bbegin + i]);

         return false;
      } catch (ArrayIndexOutOfBoundsException var6) {
         return false;
      }
   }

   public static boolean equals(byte[] a, byte[] b) {
      if (a == b) {
         return true;
      } else if (a != null && b != null) {
         if (a.length != b.length) {
            return false;
         } else {
            try {
               for(int i = 0; i < a.length; ++i) {
                  if (a[i] != b[i]) {
                     return false;
                  }
               }

               return true;
            } catch (ArrayIndexOutOfBoundsException var3) {
               return false;
            }
         }
      } else {
         return false;
      }
   }
}
