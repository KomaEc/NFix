package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.StringUtils;

public class DefaultExcludes extends Task {
   private String add = "";
   private String remove = "";
   private boolean defaultrequested = false;
   private boolean echo = false;
   private int logLevel = 1;

   public void execute() throws BuildException {
      if (!this.defaultrequested && this.add.equals("") && this.remove.equals("") && !this.echo) {
         throw new BuildException("<defaultexcludes> task must set at least one attribute (echo=\"false\" doesn't count since that is the default");
      } else {
         if (this.defaultrequested) {
            DirectoryScanner.resetDefaultExcludes();
         }

         if (!this.add.equals("")) {
            DirectoryScanner.addDefaultExclude(this.add);
         }

         if (!this.remove.equals("")) {
            DirectoryScanner.removeDefaultExclude(this.remove);
         }

         if (this.echo) {
            StringBuffer message = new StringBuffer("Current Default Excludes:");
            message.append(StringUtils.LINE_SEP);
            String[] excludes = DirectoryScanner.getDefaultExcludes();

            for(int i = 0; i < excludes.length; ++i) {
               message.append("  ");
               message.append(excludes[i]);
               message.append(StringUtils.LINE_SEP);
            }

            this.log(message.toString(), this.logLevel);
         }

      }
   }

   public void setDefault(boolean def) {
      this.defaultrequested = def;
   }

   public void setAdd(String add) {
      this.add = add;
   }

   public void setRemove(String remove) {
      this.remove = remove;
   }

   public void setEcho(boolean echo) {
      this.echo = echo;
   }
}
