package org.apache.tools.ant.types;

import java.util.Iterator;
import org.apache.tools.ant.types.resources.FileResourceIterator;

public class FileSet extends AbstractFileSet implements ResourceCollection {
   public FileSet() {
   }

   protected FileSet(FileSet fileset) {
      super(fileset);
   }

   public Object clone() {
      return this.isReference() ? ((FileSet)this.getRef(this.getProject())).clone() : super.clone();
   }

   public Iterator iterator() {
      return (Iterator)(this.isReference() ? ((FileSet)this.getRef(this.getProject())).iterator() : new FileResourceIterator(this.getDir(this.getProject()), this.getDirectoryScanner(this.getProject()).getIncludedFiles()));
   }

   public int size() {
      return this.isReference() ? ((FileSet)this.getRef(this.getProject())).size() : this.getDirectoryScanner(this.getProject()).getIncludedFilesCount();
   }

   public boolean isFilesystemOnly() {
      return true;
   }
}
