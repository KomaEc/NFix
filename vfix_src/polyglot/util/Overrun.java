package polyglot.util;

class Overrun extends Exception {
   int amount;
   int type;
   static final int POS = 0;
   static final int WIDTH = 1;
   static final int FIN = 2;
   private static final Overrun overrun = new Overrun();

   private Overrun() {
   }

   static Overrun overrun(int amount, int type) {
      overrun.amount = amount;
      overrun.type = type;
      return overrun;
   }
}
