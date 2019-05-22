package com.gzoltar.shaded.org.pitest.testapi.execute;

import com.gzoltar.shaded.org.pitest.testapi.AbstractTestUnit;
import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import java.util.Iterator;
import java.util.List;

public final class MultipleTestGroup extends AbstractTestUnit {
   private final List<TestUnit> children;

   public MultipleTestGroup(List<TestUnit> children) {
      super(new Description("MultipleTestGroup"));
      this.children = children;
   }

   public void execute(ClassLoader loader, ResultCollector rc) {
      Iterator i$ = this.children.iterator();

      while(i$.hasNext()) {
         TestUnit each = (TestUnit)i$.next();
         each.execute(loader, rc);
         if (rc.shouldExit()) {
            break;
         }
      }

   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.children == null ? 0 : this.children.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         MultipleTestGroup other = (MultipleTestGroup)obj;
         if (this.children == null) {
            if (other.children != null) {
               return false;
            }
         } else if (!this.children.equals(other.children)) {
            return false;
         }

         return true;
      }
   }
}
