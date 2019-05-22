package soot.jimple.toolkits.pointer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import soot.PointsToSet;
import soot.Scene;
import soot.SootField;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.sets.HashPointsToSet;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;

public class CodeBlockRWSet extends MethodRWSet {
   public int size() {
      if (this.globals == null) {
         return this.fields == null ? 0 : this.fields.size();
      } else {
         return this.fields == null ? this.globals.size() : this.globals.size() + this.fields.size();
      }
   }

   public String toString() {
      boolean empty = true;
      StringBuffer ret = new StringBuffer();
      Iterator globalIt;
      Object global;
      if (this.fields != null) {
         for(globalIt = this.fields.keySet().iterator(); globalIt.hasNext(); empty = false) {
            global = globalIt.next();
            ret.append("[Field: " + global + " ");
            Object baseObj = this.fields.get(global);
            if (baseObj instanceof PointsToSetInternal) {
               int baseSize = ((PointsToSetInternal)baseObj).size();
               ret.append(baseSize + (baseSize == 1 ? " Node]\n" : " Nodes]\n"));
            } else {
               ret.append(baseObj + "]\n");
            }
         }
      }

      if (this.globals != null) {
         for(globalIt = this.globals.iterator(); globalIt.hasNext(); empty = false) {
            global = globalIt.next();
            ret.append("[Global: " + global + "]\n");
         }
      }

      if (empty) {
         ret.append("[emptyset]\n");
      }

      return ret.toString();
   }

   public boolean union(RWSet other) {
      if (other == null) {
         return false;
      } else if (this.isFull) {
         return false;
      } else {
         boolean ret = false;
         Iterator var4;
         if (other instanceof MethodRWSet) {
            MethodRWSet o = (MethodRWSet)other;
            if (o.getCallsNative()) {
               ret |= !this.getCallsNative();
               this.setCallsNative();
            }

            if (o.isFull) {
               ret |= !this.isFull;
               this.isFull = true;
               throw new RuntimeException("attempt to add full set " + o + " into " + this);
            }

            if (o.globals != null) {
               if (this.globals == null) {
                  this.globals = new HashSet();
               }

               ret |= this.globals.addAll(o.globals);
               if (this.globals.size() > Integer.MAX_VALUE) {
                  this.globals = null;
                  this.isFull = true;
                  throw new RuntimeException("attempt to add full set " + o + " into " + this);
               }
            }

            Object element;
            PointsToSet os;
            if (o.fields != null) {
               for(var4 = o.fields.keySet().iterator(); var4.hasNext(); ret |= this.addFieldRef(os, element)) {
                  element = var4.next();
                  os = o.getBaseForField(element);
               }
            }
         } else if (other instanceof StmtRWSet) {
            StmtRWSet oth = (StmtRWSet)other;
            if (oth.base != null) {
               ret |= this.addFieldRef(oth.base, oth.field);
            } else if (oth.field != null) {
               ret |= this.addGlobal((SootField)oth.field);
            }
         } else if (other instanceof SiteRWSet) {
            SiteRWSet oth = (SiteRWSet)other;
            var4 = oth.sets.iterator();

            while(var4.hasNext()) {
               RWSet set = (RWSet)var4.next();
               this.union(set);
            }
         }

         if (!this.getCallsNative() && other.getCallsNative()) {
            this.setCallsNative();
            return true;
         } else {
            return ret;
         }
      }
   }

   public boolean containsField(Object field) {
      return this.fields == null ? false : this.fields.containsKey(field);
   }

   public CodeBlockRWSet intersection(MethodRWSet other) {
      CodeBlockRWSet ret = new CodeBlockRWSet();
      if (this.isFull) {
         return ret;
      } else {
         Iterator it;
         if (this.globals != null && other.globals != null && !this.globals.isEmpty() && !other.globals.isEmpty()) {
            it = other.globals.iterator();

            while(it.hasNext()) {
               SootField sg = (SootField)it.next();
               if (this.globals.contains(sg)) {
                  ret.addGlobal(sg);
               }
            }
         }

         if (this.fields != null && other.fields != null && !this.fields.isEmpty() && !other.fields.isEmpty()) {
            it = other.fields.keySet().iterator();

            while(it.hasNext()) {
               Object element = it.next();
               if (this.fields.containsKey(element)) {
                  PointsToSet pts1 = this.getBaseForField(element);
                  PointsToSet pts2 = other.getBaseForField(element);
                  if (pts1 instanceof FullObjectSet) {
                     ret.addFieldRef(pts2, element);
                  } else if (pts2 instanceof FullObjectSet) {
                     ret.addFieldRef(pts1, element);
                  } else if (pts1.hasNonEmptyIntersection(pts2) && pts1 instanceof PointsToSetInternal && pts2 instanceof PointsToSetInternal) {
                     PointsToSetInternal pti1 = (PointsToSetInternal)pts1;
                     final PointsToSetInternal pti2 = (PointsToSetInternal)pts2;
                     final PointsToSetInternal newpti = new HashPointsToSet(pti1.getType(), (PAG)Scene.v().getPointsToAnalysis());
                     pti1.forall(new P2SetVisitor() {
                        public void visit(Node n) {
                           if (pti2.contains(n)) {
                              newpti.add(n);
                           }

                        }
                     });
                     ret.addFieldRef(newpti, element);
                  }
               }
            }
         }

         return ret;
      }
   }

   public boolean addFieldRef(PointsToSet otherBase, Object field) {
      boolean ret = false;
      if (this.fields == null) {
         this.fields = new HashMap();
      }

      PointsToSet base = this.getBaseForField(field);
      if (base instanceof FullObjectSet) {
         return false;
      } else if (otherBase instanceof FullObjectSet) {
         this.fields.put(field, otherBase);
         return true;
      } else if (otherBase.equals(base)) {
         return false;
      } else {
         if (base == null) {
            PointsToSetInternal newpti = new HashPointsToSet(((PointsToSetInternal)otherBase).getType(), (PAG)Scene.v().getPointsToAnalysis());
            base = newpti;
            this.fields.put(field, newpti);
         }

         ret |= ((PointsToSetInternal)base).addAll((PointsToSetInternal)otherBase, (PointsToSetInternal)null);
         return ret;
      }
   }
}
