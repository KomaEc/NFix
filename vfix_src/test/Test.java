package test;

import java.util.ArrayList;

public class Test {
   public static void main(String[] args) {
      int x = 0;
      int x = x + 1;
      A obj = new A();
      obj.a = x;
      if (obj.a > 2) {
         ++x;
      } else {
         --x;
      }

      obj.a = x;
      System.out.println(obj.a);
   }

   public static ArrayList<Integer> getNewNums() {
      return new ArrayList();
   }
}
