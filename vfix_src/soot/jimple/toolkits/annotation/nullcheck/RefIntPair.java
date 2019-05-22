package soot.jimple.toolkits.annotation.nullcheck;

import soot.EquivalentValue;

/** @deprecated */
@Deprecated
public class RefIntPair {
   private EquivalentValue _ref;
   private int _val;

   RefIntPair(EquivalentValue r, int v, BranchedRefVarsAnalysis brva) {
      this._ref = r;
      this._val = v;
   }

   public EquivalentValue ref() {
      return this._ref;
   }

   public int val() {
      return this._val;
   }

   public String toString() {
      String prefix = "(" + this._ref + ", ";
      if (this._val == 1) {
         return prefix + "null)";
      } else if (this._val == 2) {
         return prefix + "non-null)";
      } else if (this._val == 99) {
         return prefix + "top)";
      } else {
         return this._val == 0 ? prefix + "bottom)" : prefix + this._val + ")";
      }
   }
}
