package org.apache.tools.ant.types.resources.comparators;

import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.ResourceUtils;

public class Content extends ResourceComparator {
   private boolean binary = true;

   public void setBinary(boolean b) {
      this.binary = b;
   }

   public boolean isBinary() {
      return this.binary;
   }

   protected int resourceCompare(Resource foo, Resource bar) {
      try {
         return ResourceUtils.compareContent(foo, bar, !this.binary);
      } catch (IOException var4) {
         throw new BuildException(var4);
      }
   }
}
