package soot.jimple.toolkits.thread.mhp;

public class Counter {
   private static int tagNo = 0;
   private static int objNo = 0;
   private static int threadNo = 0;

   Counter() {
   }

   protected static int getTagNo() {
      return tagNo++;
   }

   protected static int getObjNo() {
      return objNo++;
   }

   protected static int getThreadNo() {
      return threadNo++;
   }
}
