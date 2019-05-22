package soot.jimple.toolkits.pointer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import soot.PointsToSet;
import soot.SootField;

public class StmtRWSet extends RWSet {
   protected Object field;
   protected PointsToSet base;
   protected boolean callsNative = false;

   public String toString() {
      return "[Field: " + this.field + this.base + "]\n";
   }

   public int size() {
      Set globals = this.getGlobals();
      Set fields = this.getFields();
      if (globals == null) {
         return fields == null ? 0 : fields.size();
      } else {
         return fields == null ? globals.size() : globals.size() + fields.size();
      }
   }

   public boolean getCallsNative() {
      return this.callsNative;
   }

   public boolean setCallsNative() {
      boolean ret = !this.callsNative;
      this.callsNative = true;
      return ret;
   }

   public Set getGlobals() {
      if (this.base == null) {
         HashSet ret = new HashSet();
         ret.add(this.field);
         return ret;
      } else {
         return Collections.EMPTY_SET;
      }
   }

   public Set getFields() {
      if (this.base != null) {
         HashSet ret = new HashSet();
         ret.add(this.field);
         return ret;
      } else {
         return Collections.EMPTY_SET;
      }
   }

   public PointsToSet getBaseForField(Object f) {
      return this.field.equals(f) ? this.base : null;
   }

   public boolean hasNonEmptyIntersection(RWSet other) {
      if (this.field == null) {
         return false;
      } else if (other instanceof StmtRWSet) {
         StmtRWSet o = (StmtRWSet)other;
         if (!this.field.equals(o.field)) {
            return false;
         } else if (this.base == null) {
            return o.base == null;
         } else {
            return Union.hasNonEmptyIntersection(this.base, o.base);
         }
      } else if (other instanceof MethodRWSet) {
         return this.base == null ? other.getGlobals().contains(this.field) : Union.hasNonEmptyIntersection(this.base, other.getBaseForField(this.field));
      } else {
         return other.hasNonEmptyIntersection(this);
      }
   }

   public boolean union(RWSet other) {
      throw new RuntimeException("Can't do that");
   }

   public boolean addGlobal(SootField global) {
      if (this.field == null && this.base == null) {
         this.field = global;
         return true;
      } else {
         throw new RuntimeException("Can't do that");
      }
   }

   public boolean addFieldRef(PointsToSet otherBase, Object field) {
      if (this.field == null && this.base == null) {
         this.field = field;
         this.base = otherBase;
         return true;
      } else {
         throw new RuntimeException("Can't do that");
      }
   }

   public boolean isEquivTo(RWSet other) {
      if (!(other instanceof StmtRWSet)) {
         return false;
      } else {
         StmtRWSet o = (StmtRWSet)other;
         if (this.callsNative != o.callsNative) {
            return false;
         } else if (!this.field.equals(o.field)) {
            return false;
         } else if (this.base instanceof FullObjectSet && o.base instanceof FullObjectSet) {
            return true;
         } else {
            return this.base == o.base;
         }
      }
   }
}
