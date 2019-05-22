package org.apache.tools.ant.types.resources.comparators;

import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Resource;

public class DelegatedResourceComparator extends ResourceComparator {
   private Vector v = null;

   public synchronized void add(ResourceComparator c) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else if (c != null) {
         this.v = this.v == null ? new Vector() : this.v;
         this.v.add(c);
      }
   }

   public synchronized boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (this.isReference()) {
         return this.getCheckedRef().equals(o);
      } else if (!(o instanceof DelegatedResourceComparator)) {
         return false;
      } else {
         Vector ov = ((DelegatedResourceComparator)o).v;
         return this.v == null ? ov == null : this.v.equals(ov);
      }
   }

   public synchronized int hashCode() {
      if (this.isReference()) {
         return this.getCheckedRef().hashCode();
      } else {
         return this.v == null ? 0 : this.v.hashCode();
      }
   }

   protected synchronized int resourceCompare(Resource foo, Resource bar) {
      if (this.v != null && !this.v.isEmpty()) {
         int result = 0;

         for(Iterator i = this.v.iterator(); result == 0 && i.hasNext(); result = ((ResourceComparator)i.next()).resourceCompare(foo, bar)) {
         }

         return result;
      } else {
         return foo.compareTo(bar);
      }
   }

   protected void dieOnCircularReference(Stack stk, Project p) throws BuildException {
      if (!this.isChecked()) {
         if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
         } else {
            if (this.v != null && !this.v.isEmpty()) {
               Iterator i = this.v.iterator();

               while(i.hasNext()) {
                  Object o = i.next();
                  if (o instanceof DataType) {
                     stk.push(o);
                     invokeCircularReferenceCheck((DataType)o, stk, p);
                  }
               }
            }

            this.setChecked(true);
         }

      }
   }
}
