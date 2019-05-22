package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Resource;

public class Type implements ResourceSelector {
   private static final String FILE_ATTR = "file";
   private static final String DIR_ATTR = "dir";
   public static final Type FILE = new Type(new Type.FileDir("file"));
   public static final Type DIR = new Type(new Type.FileDir("dir"));
   private Type.FileDir type = null;

   public Type() {
   }

   public Type(Type.FileDir fd) {
      this.setType(fd);
   }

   public void setType(Type.FileDir fd) {
      this.type = fd;
   }

   public boolean isSelected(Resource r) {
      if (this.type == null) {
         throw new BuildException("The type attribute is required.");
      } else {
         int i = this.type.getIndex();
         return r.isDirectory() ? i == 1 : i == 0;
      }
   }

   public static class FileDir extends EnumeratedAttribute {
      private static final String[] VALUES = new String[]{"file", "dir"};

      public FileDir() {
      }

      public FileDir(String value) {
         this.setValue(value);
      }

      public String[] getValues() {
         return VALUES;
      }
   }
}
