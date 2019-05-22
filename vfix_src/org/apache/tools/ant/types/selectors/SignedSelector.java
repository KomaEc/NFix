package org.apache.tools.ant.types.selectors;

import java.io.File;
import org.apache.tools.ant.taskdefs.condition.IsSigned;
import org.apache.tools.ant.types.DataType;

public class SignedSelector extends DataType implements FileSelector {
   private IsSigned isSigned = new IsSigned();

   public void setName(String name) {
      this.isSigned.setName(name);
   }

   public boolean isSelected(File basedir, String filename, File file) {
      if (file.isDirectory()) {
         return false;
      } else {
         this.isSigned.setProject(this.getProject());
         this.isSigned.setFile(file);
         return this.isSigned.eval();
      }
   }
}
