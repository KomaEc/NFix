package org.apache.maven.model;

import java.io.Serializable;

public class FileSet extends PatternSet implements Serializable {
   private String directory;

   public String getDirectory() {
      return this.directory;
   }

   public void setDirectory(String directory) {
      this.directory = directory;
   }

   public String toString() {
      return "FileSet {directory: " + this.getDirectory() + ", " + super.toString() + "}";
   }
}
