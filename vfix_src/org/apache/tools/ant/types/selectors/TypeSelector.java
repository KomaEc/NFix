package org.apache.tools.ant.types.selectors;

import java.io.File;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Parameter;

public class TypeSelector extends BaseExtendSelector {
   private String type = null;
   public static final String TYPE_KEY = "type";

   public String toString() {
      StringBuffer buf = new StringBuffer("{typeselector type: ");
      buf.append(this.type);
      buf.append("}");
      return buf.toString();
   }

   public void setType(TypeSelector.FileType fileTypes) {
      this.type = fileTypes.getValue();
   }

   public void setParameters(Parameter[] parameters) {
      super.setParameters(parameters);
      if (parameters != null) {
         for(int i = 0; i < parameters.length; ++i) {
            String paramname = parameters[i].getName();
            if ("type".equalsIgnoreCase(paramname)) {
               TypeSelector.FileType t = new TypeSelector.FileType();
               t.setValue(parameters[i].getValue());
               this.setType(t);
            } else {
               this.setError("Invalid parameter " + paramname);
            }
         }
      }

   }

   public void verifySettings() {
      if (this.type == null) {
         this.setError("The type attribute is required");
      }

   }

   public boolean isSelected(File basedir, String filename, File file) {
      this.validate();
      return file.isDirectory() ? this.type.equals("dir") : this.type.equals("file");
   }

   public static class FileType extends EnumeratedAttribute {
      public static final String FILE = "file";
      public static final String DIR = "dir";

      public String[] getValues() {
         return new String[]{"file", "dir"};
      }
   }
}
