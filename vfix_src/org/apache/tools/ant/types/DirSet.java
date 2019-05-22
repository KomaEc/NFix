package org.apache.tools.ant.types;

import java.util.Iterator;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.resources.FileResourceIterator;

public class DirSet extends AbstractFileSet implements ResourceCollection {
   public DirSet() {
   }

   protected DirSet(DirSet dirset) {
      super(dirset);
   }

   public Object clone() {
      return this.isReference() ? ((DirSet)this.getRef(this.getProject())).clone() : super.clone();
   }

   public Iterator iterator() {
      return (Iterator)(this.isReference() ? ((DirSet)this.getRef(this.getProject())).iterator() : new FileResourceIterator(this.getDir(this.getProject()), this.getDirectoryScanner(this.getProject()).getIncludedDirectories()));
   }

   public int size() {
      return this.isReference() ? ((DirSet)this.getRef(this.getProject())).size() : this.getDirectoryScanner(this.getProject()).getIncludedDirsCount();
   }

   public boolean isFilesystemOnly() {
      return true;
   }

   public String toString() {
      DirectoryScanner ds = this.getDirectoryScanner(this.getProject());
      String[] dirs = ds.getIncludedDirectories();
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < dirs.length; ++i) {
         if (i > 0) {
            sb.append(';');
         }

         sb.append(dirs[i]);
      }

      return sb.toString();
   }
}
