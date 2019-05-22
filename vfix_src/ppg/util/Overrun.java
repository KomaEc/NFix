package ppg.util;

class Overrun extends Exception {
   int amount;

   Overrun(int amount_) {
      this.amount = amount_;
   }
}
