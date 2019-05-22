package org.codehaus.groovy.ant;

import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;

public class FileIterator implements Iterator {
   private Iterator fileSetIterator;
   private Project project;
   private DirectoryScanner ds;
   private String[] files;
   private int fileIndex;
   private File nextFile;
   private boolean nextObjectSet;
   private boolean iterateDirectories;

   public FileIterator(Project project, Iterator fileSetIterator) {
      this(project, fileSetIterator, false);
   }

   public FileIterator(Project project, Iterator fileSetIterator, boolean iterateDirectories) {
      this.fileIndex = -1;
      this.nextObjectSet = false;
      this.iterateDirectories = false;
      this.project = project;
      this.fileSetIterator = fileSetIterator;
      this.iterateDirectories = iterateDirectories;
   }

   public boolean hasNext() {
      return this.nextObjectSet ? true : this.setNextObject();
   }

   public Object next() {
      if (!this.nextObjectSet && !this.setNextObject()) {
         throw new NoSuchElementException();
      } else {
         this.nextObjectSet = false;
         return this.nextFile;
      }
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }

   private boolean setNextObject() {
      while(true) {
         if (this.ds == null) {
            if (!this.fileSetIterator.hasNext()) {
               return false;
            }

            FileSet fs = (FileSet)this.fileSetIterator.next();
            this.ds = fs.getDirectoryScanner(this.project);
            this.ds.scan();
            if (this.iterateDirectories) {
               this.files = this.ds.getIncludedDirectories();
            } else {
               this.files = this.ds.getIncludedFiles();
            }

            if (this.files.length <= 0) {
               this.ds = null;
               continue;
            }

            this.fileIndex = -1;
         }

         if (this.ds != null && this.files != null) {
            if (++this.fileIndex < this.files.length) {
               this.nextFile = new File(this.ds.getBasedir(), this.files[this.fileIndex]);
               this.nextObjectSet = true;
               return true;
            }

            this.ds = null;
         }
      }
   }
}
