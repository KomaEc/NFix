package org.apache.tools.ant.types.resources.selectors;

import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;

public class ResourceSelectorContainer extends DataType {
   private Vector v = new Vector();

   public ResourceSelectorContainer() {
   }

   public ResourceSelectorContainer(ResourceSelector[] r) {
      for(int i = 0; i < r.length; ++i) {
         this.add(r[i]);
      }

   }

   public void add(ResourceSelector s) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else if (s != null) {
         this.v.add(s);
         this.setChecked(false);
      }
   }

   public boolean hasSelectors() {
      if (this.isReference()) {
         return ((ResourceSelectorContainer)this.getCheckedRef()).hasSelectors();
      } else {
         this.dieOnCircularReference();
         return !this.v.isEmpty();
      }
   }

   public int selectorCount() {
      if (this.isReference()) {
         return ((ResourceSelectorContainer)this.getCheckedRef()).selectorCount();
      } else {
         this.dieOnCircularReference();
         return this.v.size();
      }
   }

   public Iterator getSelectors() {
      if (this.isReference()) {
         return ((ResourceSelectorContainer)this.getCheckedRef()).getSelectors();
      } else {
         this.dieOnCircularReference();
         return Collections.unmodifiableList(this.v).iterator();
      }
   }

   protected void dieOnCircularReference(Stack stk, Project p) throws BuildException {
      if (!this.isChecked()) {
         if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
         } else {
            Iterator i = this.v.iterator();

            while(i.hasNext()) {
               Object o = i.next();
               if (o instanceof DataType) {
                  stk.push(o);
                  invokeCircularReferenceCheck((DataType)o, stk, p);
               }
            }

            this.setChecked(true);
         }

      }
   }
}
