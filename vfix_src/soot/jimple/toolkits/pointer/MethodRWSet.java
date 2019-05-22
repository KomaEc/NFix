package soot.jimple.toolkits.pointer;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.PointsToSet;
import soot.SootField;

public class MethodRWSet extends RWSet {
   private static final Logger logger = LoggerFactory.getLogger(MethodRWSet.class);
   public Set globals;
   public Map<Object, PointsToSet> fields;
   protected boolean callsNative = false;
   protected boolean isFull = false;
   public static final int MAX_SIZE = Integer.MAX_VALUE;

   public String toString() {
      boolean empty = true;
      StringBuffer ret = new StringBuffer();
      Iterator globalIt;
      Object global;
      if (this.fields != null) {
         for(globalIt = this.fields.keySet().iterator(); globalIt.hasNext(); empty = false) {
            global = globalIt.next();
            ret.append("[Field: " + global + " " + this.fields.get(global) + "]\n");
         }
      }

      if (this.globals != null) {
         for(globalIt = this.globals.iterator(); globalIt.hasNext(); empty = false) {
            global = globalIt.next();
            ret.append("[Global: " + global + "]\n");
         }
      }

      if (empty) {
         ret.append("empty");
      }

      return ret.toString();
   }

   public int size() {
      if (this.globals == null) {
         return this.fields == null ? 0 : this.fields.size();
      } else {
         return this.fields == null ? this.globals.size() : this.globals.size() + this.fields.size();
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
      if (this.isFull) {
         return G.v().MethodRWSet_allGlobals;
      } else {
         return this.globals == null ? Collections.EMPTY_SET : this.globals;
      }
   }

   public Set getFields() {
      if (this.isFull) {
         return G.v().MethodRWSet_allFields;
      } else {
         return this.fields == null ? Collections.EMPTY_SET : this.fields.keySet();
      }
   }

   public PointsToSet getBaseForField(Object f) {
      if (this.isFull) {
         return FullObjectSet.v();
      } else {
         return this.fields == null ? null : (PointsToSet)this.fields.get(f);
      }
   }

   public boolean hasNonEmptyIntersection(RWSet oth) {
      if (this.isFull) {
         return oth != null;
      } else if (!(oth instanceof MethodRWSet)) {
         return oth.hasNonEmptyIntersection(this);
      } else {
         MethodRWSet other = (MethodRWSet)oth;
         Iterator it;
         if (this.globals != null && other.globals != null && !this.globals.isEmpty() && !other.globals.isEmpty()) {
            it = other.globals.iterator();

            while(it.hasNext()) {
               if (this.globals.contains(it.next())) {
                  return true;
               }
            }
         }

         if (this.fields != null && other.fields != null && !this.fields.isEmpty() && !other.fields.isEmpty()) {
            it = other.fields.keySet().iterator();

            while(it.hasNext()) {
               Object element = it.next();
               if (this.fields.containsKey(element) && Union.hasNonEmptyIntersection(this.getBaseForField(element), other.getBaseForField(element))) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean union(RWSet other) {
      if (other == null) {
         return false;
      } else if (this.isFull) {
         return false;
      } else {
         boolean ret = false;
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
               for(Iterator var4 = o.fields.keySet().iterator(); var4.hasNext(); ret |= this.addFieldRef(os, element)) {
                  element = var4.next();
                  os = o.getBaseForField(element);
               }
            }
         } else {
            StmtRWSet oth = (StmtRWSet)other;
            if (oth.base != null) {
               ret |= this.addFieldRef(oth.base, oth.field);
            } else if (oth.field != null) {
               ret |= this.addGlobal((SootField)oth.field);
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

   public boolean addGlobal(SootField global) {
      if (this.globals == null) {
         this.globals = new HashSet();
      }

      boolean ret = this.globals.add(global);
      if (this.globals.size() > Integer.MAX_VALUE) {
         this.globals = null;
         this.isFull = true;
         throw new RuntimeException("attempt to add more than 2147483647 globals into " + this);
      } else {
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
         Union u;
         if (base != null && base instanceof Union) {
            u = (Union)base;
         } else {
            u = G.v().Union_factory.newUnion();
            if (base != null) {
               u.addAll(base);
            }

            this.fields.put(field, u);
            if (base == null) {
               addedField(this.fields.size());
            }

            ret = true;
            if (this.fields.keySet().size() > Integer.MAX_VALUE) {
               this.fields = null;
               this.isFull = true;
               throw new RuntimeException("attempt to add more than 2147483647 fields into " + this);
            }
         }

         ret |= u.addAll(otherBase);
         return ret;
      }
   }

   static void addedField(int size) {
   }

   public boolean isEquivTo(RWSet other) {
      return other == this;
   }
}
