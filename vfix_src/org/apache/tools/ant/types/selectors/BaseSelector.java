package org.apache.tools.ant.types.selectors;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;

public abstract class BaseSelector extends DataType implements FileSelector {
   private String errmsg = null;

   public void setError(String msg) {
      if (this.errmsg == null) {
         this.errmsg = msg;
      }

   }

   public String getError() {
      return this.errmsg;
   }

   public void verifySettings() {
      if (this.isReference()) {
         ((BaseSelector)this.getCheckedRef()).verifySettings();
      }

   }

   public void validate() {
      if (this.getError() == null) {
         this.verifySettings();
      }

      if (this.getError() != null) {
         throw new BuildException(this.errmsg);
      }
   }

   public abstract boolean isSelected(File var1, String var2, File var3);
}
