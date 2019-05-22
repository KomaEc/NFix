package soot.jimple.toolkits.pointer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.G;
import soot.PointsToSet;
import soot.SootField;

public class SiteRWSet extends RWSet {
   public HashSet<RWSet> sets = new HashSet();
   protected boolean callsNative = false;

   public int size() {
      Set globals = this.getGlobals();
      Set fields = this.getFields();
      if (globals == null) {
         return fields == null ? 0 : fields.size();
      } else {
         return fields == null ? globals.size() : globals.size() + fields.size();
      }
   }

   public String toString() {
      boolean empty = true;
      StringBuffer ret = new StringBuffer();
      ret.append("SiteRWSet: ");

      for(Iterator keyIt = this.sets.iterator(); keyIt.hasNext(); empty = false) {
         Object key = keyIt.next();
         ret.append(key.toString());
      }

      if (empty) {
         ret.append("empty");
      }

      return ret.toString();
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
      HashSet ret = new HashSet();
      Iterator var2 = this.sets.iterator();

      while(var2.hasNext()) {
         RWSet s = (RWSet)var2.next();
         ret.addAll(s.getGlobals());
      }

      return ret;
   }

   public Set getFields() {
      HashSet ret = new HashSet();
      Iterator var2 = this.sets.iterator();

      while(var2.hasNext()) {
         RWSet s = (RWSet)var2.next();
         ret.addAll(s.getFields());
      }

      return ret;
   }

   public PointsToSet getBaseForField(Object f) {
      Union ret = null;
      Iterator var3 = this.sets.iterator();

      while(var3.hasNext()) {
         RWSet s = (RWSet)var3.next();
         PointsToSet os = s.getBaseForField(f);
         if (os != null && !os.isEmpty()) {
            if (ret == null) {
               ret = G.v().Union_factory.newUnion();
            }

            ret.addAll(os);
         }
      }

      return ret;
   }

   public boolean hasNonEmptyIntersection(RWSet oth) {
      if (this.sets.contains(oth)) {
         return true;
      } else {
         Iterator var2 = this.sets.iterator();

         RWSet s;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            s = (RWSet)var2.next();
         } while(!oth.hasNonEmptyIntersection(s));

         return true;
      }
   }

   public boolean union(RWSet other) {
      if (other == null) {
         return false;
      } else {
         boolean ret = false;
         if (other.getCallsNative()) {
            ret = this.setCallsNative();
         }

         return other.getFields().isEmpty() && other.getGlobals().isEmpty() ? ret : this.sets.add(other) | ret;
      }
   }

   public boolean addGlobal(SootField global) {
      throw new RuntimeException("Not implemented; try MethodRWSet");
   }

   public boolean addFieldRef(PointsToSet otherBase, Object field) {
      throw new RuntimeException("Not implemented; try MethodRWSet");
   }

   public boolean isEquivTo(RWSet other) {
      if (!(other instanceof SiteRWSet)) {
         return false;
      } else {
         SiteRWSet o = (SiteRWSet)other;
         return o.callsNative != this.callsNative ? false : o.sets.equals(this.sets);
      }
   }
}
