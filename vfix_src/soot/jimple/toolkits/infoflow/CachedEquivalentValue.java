package soot.jimple.toolkits.infoflow;

import java.util.WeakHashMap;
import soot.EquivalentValue;
import soot.Value;

public class CachedEquivalentValue extends EquivalentValue {
   protected int code = Integer.MAX_VALUE;
   protected WeakHashMap<Value, Boolean> isEquivalent = new WeakHashMap();

   public CachedEquivalentValue(Value e) {
      super(e);
   }

   public int hashCode() {
      if (this.code == Integer.MAX_VALUE) {
         this.code = super.hashCode();
      }

      return this.code;
   }

   public boolean equals(Object o) {
      if (this.getClass() != o.getClass()) {
         return false;
      } else {
         EquivalentValue ev = (EquivalentValue)o;
         Value v = ev.getValue();
         Boolean b = (Boolean)this.isEquivalent.get(v);
         if (b == null) {
            b = super.equals(o);
            this.isEquivalent.put(v, b);
         }

         return b;
      }
   }
}
