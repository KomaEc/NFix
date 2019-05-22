package scm;

class Cell implements Obj {
   Obj car;
   Cell cdr;

   public Obj eval(Env e) throws Exception {
      if (this.car == null) {
         throw new SchemeError("null car cell trying to eval " + this);
      } else {
         Procedure p;
         if (this.car instanceof Procedure) {
            p = (Procedure)this.car;
         } else {
            p = (Procedure)this.car.eval(e);
         }

         return p.apply(this.cdr, e);
      }
   }

   Cell(Obj a, Cell b) {
      this.car = a;
      this.cdr = b;
   }

   public String toString() {
      return this.toString("");
   }

   public String toString(String s) {
      if (this.car == null) {
         s = s + "()";
      } else {
         s = s + this.car.toString();
      }

      return this.cdr == null ? "(" + s + ")" : this.cdr.toString(s + " ");
   }
}
