package org.testng.internal;

public class Dynamic {
   public static boolean hasBsh() {
      try {
         Class.forName("bsh.Interpreter");
         return true;
      } catch (ClassNotFoundException var1) {
         return false;
      }
   }
}
