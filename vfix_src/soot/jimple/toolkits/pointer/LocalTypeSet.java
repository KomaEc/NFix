package soot.jimple.toolkits.pointer;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.FastHierarchy;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.Type;

class LocalTypeSet extends BitSet {
   private static final Logger logger = LoggerFactory.getLogger(LocalTypeSet.class);
   protected List<Local> locals;
   protected List<Type> types;

   public LocalTypeSet(List<Local> locals, List<Type> types) {
      super(locals.size() * types.size());
      this.locals = locals;
      this.types = types;
      Scene.v().getOrMakeFastHierarchy();
   }

   protected int indexOf(Local l, RefType t) {
      if (this.locals.indexOf(l) != -1 && this.types.indexOf(t) != -1) {
         return this.locals.indexOf(l) * this.types.size() + this.types.indexOf(t);
      } else {
         throw new RuntimeException("Invalid local or type in LocalTypeSet");
      }
   }

   public void killLocal(Local l) {
      int base = this.types.size() * this.locals.indexOf(l);

      for(int i = 0; i < this.types.size(); ++i) {
         this.clear(i + base);
      }

   }

   public void localCopy(Local to, Local from) {
      int baseTo = this.types.size() * this.locals.indexOf(to);
      int baseFrom = this.types.size() * this.locals.indexOf(from);

      for(int i = 0; i < this.types.size(); ++i) {
         if (this.get(i + baseFrom)) {
            this.set(i + baseTo);
         } else {
            this.clear(i + baseTo);
         }
      }

   }

   public void clearAllBits() {
      for(int i = 0; i < this.types.size() * this.locals.size(); ++i) {
         this.clear(i);
      }

   }

   public void setAllBits() {
      for(int i = 0; i < this.types.size() * this.locals.size(); ++i) {
         this.set(i);
      }

   }

   public void localMustBeSubtypeOf(Local l, RefType t) {
      FastHierarchy fh = Scene.v().getFastHierarchy();
      Iterator var4 = this.types.iterator();

      while(var4.hasNext()) {
         Type type = (Type)var4.next();
         RefType supertype = (RefType)type;
         if (fh.canStoreType(t, supertype)) {
            this.set(this.indexOf(l, supertype));
         }
      }

   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      Iterator localsIt = this.locals.iterator();

      while(localsIt.hasNext()) {
         Local l = (Local)localsIt.next();
         Iterator typesIt = this.types.iterator();

         while(typesIt.hasNext()) {
            RefType t = (RefType)typesIt.next();
            int index = this.indexOf(l, t);
            if (this.get(index)) {
               sb.append("((" + l + "," + t + ") -> elim cast check) ");
            }
         }
      }

      return sb.toString();
   }
}
