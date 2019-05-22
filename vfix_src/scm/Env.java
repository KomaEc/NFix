package scm;

import java.util.Hashtable;

class Env {
   Hashtable bindings = null;
   static Object MAGIC_KLUDGE = "**jas-nil-internal";
   Env parent;

   Obj lookup(Symbol cvar) {
      Env f = this;
      Object ret = null;

      for(Hashtable b = this.bindings; ret == null && f != null; f = f.parent) {
         b = f.bindings;
         if (b != null) {
            ret = b.get(cvar);
         }
      }

      if (ret == null) {
         throw new SchemeError("Unbound variable " + cvar);
      } else {
         return ret == MAGIC_KLUDGE ? null : (Obj)ret;
      }
   }

   void setvar(Symbol cvar, Obj val) {
      Env f = this;
      Object ret = null;

      Hashtable b;
      for(b = this.bindings; ret == null && f != null; f = f.parent) {
         b = f.bindings;
         if (b != null) {
            ret = b.get(cvar);
         }
      }

      if (ret == null) {
         throw new SchemeError("Attempted to set unbound variable " + cvar);
      } else {
         if (val == null) {
            b.put(cvar, MAGIC_KLUDGE);
         } else {
            b.put(cvar, val);
         }

      }
   }

   void definevar(Symbol v, Obj val) {
      if (this.bindings == null) {
         this.bindings = new Hashtable();
      }

      if (val == null) {
         this.bindings.put(v, MAGIC_KLUDGE);
      } else {
         this.bindings.put(v, val);
      }

   }

   Env extendenv(Cell formals, Cell params) {
      Env ret = new Env();
      ret.parent = this;
      if (formals == null) {
         if (params != null) {
            throw new SchemeError("mismatched arglist to entend env");
         }
      } else {
         for(ret.bindings = new Hashtable(); formals != null; params = params.cdr) {
            Symbol thissym = (Symbol)formals.car;
            Obj thisval = params.car;
            if (thisval == null) {
               ret.bindings.put(thissym, MAGIC_KLUDGE);
            } else {
               ret.bindings.put(thissym, thisval);
            }

            formals = formals.cdr;
         }

         if (params != null) {
            throw new SchemeError("mismatched arglist to extend env");
         }
      }

      return ret;
   }

   public String toString() {
      return "Binding is " + this.bindings + "\nparent is " + this.parent;
   }
}
