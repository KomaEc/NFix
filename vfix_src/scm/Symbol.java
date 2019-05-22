package scm;

import java.util.Hashtable;

class Symbol implements Obj {
   static Hashtable internset = new Hashtable();
   String name;

   Symbol(String s) {
      this.name = s;
   }

   static Symbol intern(String s) {
      Symbol ret;
      if ((ret = (Symbol)internset.get(s)) == null) {
         ret = new Symbol(s);
         internset.put(s, ret);
      }

      return ret;
   }

   public Obj eval(Env e) {
      return e.lookup(this);
   }

   public String toString() {
      return this.name;
   }
}
