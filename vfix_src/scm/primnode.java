package scm;

class primnode implements Obj {
   Object val;

   public Obj eval(Env e) {
      return this;
   }

   primnode(Object thing) {
      this.val = thing;
   }

   public String toString() {
      return this.val.toString();
   }
}
