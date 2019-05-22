package org.apache.tools.ant.types.resources;

import java.util.Iterator;
import org.apache.tools.ant.types.FileSet;

public class BCFileSet extends FileSet {
   public BCFileSet() {
   }

   public BCFileSet(FileSet fs) {
      super(fs);
   }

   public Iterator iterator() {
      if (this.isReference()) {
         return ((FileSet)this.getRef(this.getProject())).iterator();
      } else {
         FileResourceIterator result = new FileResourceIterator(this.getDir());
         result.addFiles(this.getDirectoryScanner().getIncludedFiles());
         result.addFiles(this.getDirectoryScanner().getIncludedDirectories());
         return result;
      }
   }

   public int size() {
      return this.isReference() ? ((FileSet)this.getRef(this.getProject())).size() : this.getDirectoryScanner().getIncludedFilesCount() + this.getDirectoryScanner().getIncludedDirsCount();
   }
}
