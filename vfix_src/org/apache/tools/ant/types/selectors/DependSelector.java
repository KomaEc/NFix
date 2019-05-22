package org.apache.tools.ant.types.selectors;

import java.io.File;

public class DependSelector extends MappingSelector {
   public String toString() {
      StringBuffer buf = new StringBuffer("{dependselector targetdir: ");
      if (this.targetdir == null) {
         buf.append("NOT YET SET");
      } else {
         buf.append(this.targetdir.getName());
      }

      buf.append(" granularity: ");
      buf.append(this.granularity);
      if (this.map != null) {
         buf.append(" mapper: ");
         buf.append(this.map.toString());
      } else if (this.mapperElement != null) {
         buf.append(" mapper: ");
         buf.append(this.mapperElement.toString());
      }

      buf.append("}");
      return buf.toString();
   }

   public boolean selectionTest(File srcfile, File destfile) {
      boolean selected = SelectorUtils.isOutOfDate(srcfile, destfile, this.granularity);
      return selected;
   }
}
