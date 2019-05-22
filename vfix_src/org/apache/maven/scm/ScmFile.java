package org.apache.maven.scm;

import java.io.Serializable;

public class ScmFile implements Comparable<ScmFile>, Serializable {
   private static final long serialVersionUID = -9133015730693522690L;
   private String path;
   private ScmFileStatus status;

   public ScmFile(String path, ScmFileStatus status) {
      this.path = path;
      this.status = status;
   }

   public String getPath() {
      return this.path;
   }

   public ScmFileStatus getStatus() {
      return this.status;
   }

   public int compareTo(ScmFile other) {
      return other.getPath().compareTo(this.path);
   }

   public boolean equals(Object other) {
      return !(other instanceof ScmFile) ? false : ((ScmFile)other).getPath().equals(this.path);
   }

   public int hashCode() {
      return this.path.hashCode();
   }

   public String toString() {
      return "[" + this.path + ":" + this.status + "]";
   }
}
