package org.apache.tools.ant.taskdefs.condition;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.ReflectWrapper;
import org.apache.tools.ant.util.StringUtils;

public class HasFreeSpace implements Condition {
   private String partition;
   private String needed;

   public boolean eval() throws BuildException {
      this.validate();

      try {
         if (JavaEnvUtils.isAtLeastJavaVersion("1.6")) {
            File fs = new File(this.partition);
            ReflectWrapper w = new ReflectWrapper(fs);
            long free = (Long)w.invoke("getFreeSpace");
            return free >= StringUtils.parseHumanSizes(this.needed);
         } else {
            throw new BuildException("HasFreeSpace condition not supported on Java5 or less.");
         }
      } catch (Exception var5) {
         throw new BuildException(var5);
      }
   }

   private void validate() throws BuildException {
      if (null == this.partition) {
         throw new BuildException("Please set the partition attribute.");
      } else if (null == this.needed) {
         throw new BuildException("Please set the needed attribute.");
      }
   }

   public String getPartition() {
      return this.partition;
   }

   public void setPartition(String partition) {
      this.partition = partition;
   }

   public String getNeeded() {
      return this.needed;
   }

   public void setNeeded(String needed) {
      this.needed = needed;
   }
}
