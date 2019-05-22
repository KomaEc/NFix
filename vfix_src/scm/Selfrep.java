package scm;

class Selfrep implements Obj {
   String val;
   double num;

   public Obj eval(Env e) {
      return this;
   }

   Selfrep(double num) {
      this.num = num;
      this.val = null;
   }

   Selfrep(String s) {
      this.val = s;
   }

   public String toString() {
      return this.val == null ? "Number: " + this.num : "String: " + this.val;
   }
}
