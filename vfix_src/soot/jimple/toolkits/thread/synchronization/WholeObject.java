package soot.jimple.toolkits.thread.synchronization;

import soot.SootField;
import soot.SootFieldRef;
import soot.Type;
import soot.jimple.FieldRef;

class WholeObject {
   Type type;

   public WholeObject(Type type) {
      this.type = type;
   }

   public WholeObject() {
      this.type = null;
   }

   public String toString() {
      return "All Fields" + (this.type == null ? "" : " (" + this.type + ")");
   }

   public int hashCode() {
      return this.type == null ? 1 : this.type.hashCode();
   }

   public boolean equals(Object o) {
      if (this.type == null) {
         return true;
      } else if (o instanceof WholeObject) {
         WholeObject other = (WholeObject)o;
         if (other.type == null) {
            return true;
         } else {
            return this.type == other.type;
         }
      } else if (o instanceof FieldRef) {
         return this.type == ((FieldRef)o).getType();
      } else if (o instanceof SootFieldRef) {
         return this.type == ((SootFieldRef)o).type();
      } else if (o instanceof SootField) {
         return this.type == ((SootField)o).getType();
      } else {
         return true;
      }
   }
}
