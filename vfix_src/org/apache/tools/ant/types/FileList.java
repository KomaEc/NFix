package org.apache.tools.ant.types;

import java.io.File;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.resources.FileResourceIterator;

public class FileList extends DataType implements ResourceCollection {
   private Vector filenames = new Vector();
   private File dir;

   public FileList() {
   }

   protected FileList(FileList filelist) {
      this.dir = filelist.dir;
      this.filenames = filelist.filenames;
      this.setProject(filelist.getProject());
   }

   public void setRefid(Reference r) throws BuildException {
      if (this.dir == null && this.filenames.size() == 0) {
         super.setRefid(r);
      } else {
         throw this.tooManyAttributes();
      }
   }

   public void setDir(File dir) throws BuildException {
      this.checkAttributesAllowed();
      this.dir = dir;
   }

   public File getDir(Project p) {
      return this.isReference() ? this.getRef(p).getDir(p) : this.dir;
   }

   public void setFiles(String filenames) {
      this.checkAttributesAllowed();
      if (filenames != null && filenames.length() > 0) {
         StringTokenizer tok = new StringTokenizer(filenames, ", \t\n\r\f", false);

         while(tok.hasMoreTokens()) {
            this.filenames.addElement(tok.nextToken());
         }
      }

   }

   public String[] getFiles(Project p) {
      if (this.isReference()) {
         return this.getRef(p).getFiles(p);
      } else if (this.dir == null) {
         throw new BuildException("No directory specified for filelist.");
      } else if (this.filenames.size() == 0) {
         throw new BuildException("No files specified for filelist.");
      } else {
         String[] result = new String[this.filenames.size()];
         this.filenames.copyInto(result);
         return result;
      }
   }

   protected FileList getRef(Project p) {
      return (FileList)this.getCheckedRef(p);
   }

   public void addConfiguredFile(FileList.FileName name) {
      if (name.getName() == null) {
         throw new BuildException("No name specified in nested file element");
      } else {
         this.filenames.addElement(name.getName());
      }
   }

   public Iterator iterator() {
      return (Iterator)(this.isReference() ? this.getRef(this.getProject()).iterator() : new FileResourceIterator(this.dir, (String[])((String[])this.filenames.toArray(new String[this.filenames.size()]))));
   }

   public int size() {
      return this.isReference() ? this.getRef(this.getProject()).size() : this.filenames.size();
   }

   public boolean isFilesystemOnly() {
      return true;
   }

   public static class FileName {
      private String name;

      public void setName(String name) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }
   }
}
