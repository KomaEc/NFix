package org.apache.tools.ant.types.resources.selectors;

import java.util.Iterator;
import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Comparison;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Quantifier;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.types.resources.comparators.DelegatedResourceComparator;
import org.apache.tools.ant.types.resources.comparators.ResourceComparator;

public class Compare extends DataType implements ResourceSelector {
   private static final String ONE_CONTROL_MESSAGE = " the <control> element should be specified exactly once.";
   private DelegatedResourceComparator comp = new DelegatedResourceComparator();
   private Quantifier against;
   private Comparison when;
   private Union control;

   public Compare() {
      this.against = Quantifier.ALL;
      this.when = Comparison.EQUAL;
   }

   public synchronized void add(ResourceComparator c) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         this.comp.add(c);
      }
   }

   public synchronized void setAgainst(Quantifier against) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.against = against;
      }
   }

   public synchronized void setWhen(Comparison when) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.when = when;
      }
   }

   public synchronized ResourceCollection createControl() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else if (this.control != null) {
         throw this.oneControl();
      } else {
         this.control = new Union();
         return this.control;
      }
   }

   public synchronized boolean isSelected(Resource r) {
      if (this.isReference()) {
         return ((ResourceSelector)this.getCheckedRef()).isSelected(r);
      } else if (this.control == null) {
         throw this.oneControl();
      } else {
         int t = 0;
         int f = 0;
         Iterator it = this.control.iterator();

         while(it.hasNext()) {
            if (this.when.evaluate(this.comp.compare(r, (Resource)it.next()))) {
               ++t;
            } else {
               ++f;
            }
         }

         return this.against.evaluate(t, f);
      }
   }

   protected synchronized void dieOnCircularReference(Stack stk, Project p) throws BuildException {
      if (!this.isChecked()) {
         if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
         } else {
            if (this.control != null) {
               DataType.invokeCircularReferenceCheck(this.control, stk, p);
            }

            DataType.invokeCircularReferenceCheck(this.comp, stk, p);
            this.setChecked(true);
         }

      }
   }

   private BuildException oneControl() {
      return new BuildException(super.toString() + " the <control> element should be specified exactly once.");
   }
}
