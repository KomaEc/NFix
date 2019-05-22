package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;

/** @deprecated */
public class Copydir extends MatchingTask {
   private File srcDir;
   private File destDir;
   private boolean filtering = false;
   private boolean flatten = false;
   private boolean forceOverwrite = false;
   private Hashtable filecopyList = new Hashtable();

   public void setSrc(File src) {
      this.srcDir = src;
   }

   public void setDest(File dest) {
      this.destDir = dest;
   }

   public void setFiltering(boolean filter) {
      this.filtering = filter;
   }

   public void setFlatten(boolean flatten) {
      this.flatten = flatten;
   }

   public void setForceoverwrite(boolean force) {
      this.forceOverwrite = force;
   }

   public void execute() throws BuildException {
      this.log("DEPRECATED - The copydir task is deprecated.  Use copy instead.");
      if (this.srcDir == null) {
         throw new BuildException("src attribute must be set!", this.getLocation());
      } else if (!this.srcDir.exists()) {
         throw new BuildException("srcdir " + this.srcDir.toString() + " does not exist!", this.getLocation());
      } else if (this.destDir == null) {
         throw new BuildException("The dest attribute must be set.", this.getLocation());
      } else {
         if (this.srcDir.equals(this.destDir)) {
            this.log("Warning: src == dest", 1);
         }

         DirectoryScanner ds = super.getDirectoryScanner(this.srcDir);

         try {
            String[] files = ds.getIncludedFiles();
            this.scanDir(this.srcDir, this.destDir, files);
            if (this.filecopyList.size() > 0) {
               this.log("Copying " + this.filecopyList.size() + " file" + (this.filecopyList.size() == 1 ? "" : "s") + " to " + this.destDir.getAbsolutePath());
               Enumeration e = this.filecopyList.keys();

               while(e.hasMoreElements()) {
                  String fromFile = (String)e.nextElement();
                  String toFile = (String)this.filecopyList.get(fromFile);

                  try {
                     this.getProject().copyFile(fromFile, toFile, this.filtering, this.forceOverwrite);
                  } catch (IOException var11) {
                     String msg = "Failed to copy " + fromFile + " to " + toFile + " due to " + var11.getMessage();
                     throw new BuildException(msg, var11, this.getLocation());
                  }
               }
            }
         } finally {
            this.filecopyList.clear();
         }

      }
   }

   private void scanDir(File from, File to, String[] files) {
      for(int i = 0; i < files.length; ++i) {
         String filename = files[i];
         File srcFile = new File(from, filename);
         File destFile;
         if (this.flatten) {
            destFile = new File(to, (new File(filename)).getName());
         } else {
            destFile = new File(to, filename);
         }

         if (this.forceOverwrite || srcFile.lastModified() > destFile.lastModified()) {
            this.filecopyList.put(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
         }
      }

   }
}
