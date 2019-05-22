package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.FilterSet;
import org.apache.tools.ant.types.FilterSetCollection;

public class Move extends Copy {
   public Move() {
      this.setOverwrite(true);
   }

   protected void validateAttributes() throws BuildException {
      if (this.file != null && this.file.isDirectory()) {
         if (this.destFile != null && this.destDir != null || this.destFile == null && this.destDir == null) {
            throw new BuildException("One and only one of tofile and todir must be set.");
         }

         this.destFile = this.destFile == null ? new File(this.destDir, this.file.getName()) : this.destFile;
         this.destDir = this.destDir == null ? this.destFile.getParentFile() : this.destDir;
         this.completeDirMap.put(this.file, this.destFile);
         this.file = null;
      } else {
         super.validateAttributes();
      }

   }

   protected void doFileOperations() {
      if (this.completeDirMap.size() > 0) {
         Enumeration e = this.completeDirMap.keys();

         while(e.hasMoreElements()) {
            File fromDir = (File)e.nextElement();
            File toDir = (File)this.completeDirMap.get(fromDir);
            boolean renamed = false;

            try {
               this.log("Attempting to rename dir: " + fromDir + " to " + toDir, this.verbosity);
               renamed = this.renameFile(fromDir, toDir, this.filtering, this.forceOverwrite);
            } catch (IOException var10) {
               String msg = "Failed to rename dir " + fromDir + " to " + toDir + " due to " + var10.getMessage();
               throw new BuildException(msg, var10, this.getLocation());
            }

            if (!renamed) {
               FileSet fs = new FileSet();
               fs.setProject(this.getProject());
               fs.setDir(fromDir);
               this.addFileset(fs);
               DirectoryScanner ds = fs.getDirectoryScanner(this.getProject());
               String[] files = ds.getIncludedFiles();
               String[] dirs = ds.getIncludedDirectories();
               this.scan(fromDir, toDir, files, dirs);
            }
         }
      }

      int moveCount = this.fileCopyMap.size();
      Enumeration e;
      int i;
      if (moveCount > 0) {
         this.log("Moving " + moveCount + " file" + (moveCount == 1 ? "" : "s") + " to " + this.destDir.getAbsolutePath());
         e = this.fileCopyMap.keys();

         label108:
         while(true) {
            String fromFile;
            File f;
            boolean selfMove;
            do {
               if (!e.hasMoreElements()) {
                  break label108;
               }

               fromFile = (String)e.nextElement();
               f = new File(fromFile);
               selfMove = false;
            } while(!f.exists());

            String[] toFiles = (String[])((String[])this.fileCopyMap.get(fromFile));

            for(i = 0; i < toFiles.length; ++i) {
               String toFile = toFiles[i];
               if (fromFile.equals(toFile)) {
                  this.log("Skipping self-move of " + fromFile, this.verbosity);
                  selfMove = true;
               } else {
                  File d = new File(toFile);
                  if (i + 1 == toFiles.length && !selfMove) {
                     this.moveFile(f, d, this.filtering, this.forceOverwrite);
                  } else {
                     this.copyFile(f, d, this.filtering, this.forceOverwrite);
                  }
               }
            }
         }
      }

      if (this.includeEmpty) {
         e = this.dirCopyMap.keys();
         int createCount = 0;

         while(e.hasMoreElements()) {
            String fromDirName = (String)e.nextElement();
            String[] toDirNames = (String[])((String[])this.dirCopyMap.get(fromDirName));
            boolean selfMove = false;

            for(i = 0; i < toDirNames.length; ++i) {
               if (fromDirName.equals(toDirNames[i])) {
                  this.log("Skipping self-move of " + fromDirName, this.verbosity);
                  selfMove = true;
               } else {
                  File d = new File(toDirNames[i]);
                  if (!d.exists()) {
                     if (!d.mkdirs()) {
                        this.log("Unable to create directory " + d.getAbsolutePath(), 0);
                     } else {
                        ++createCount;
                     }
                  }
               }
            }

            File fromDir = new File(fromDirName);
            if (!selfMove && this.okToDelete(fromDir)) {
               this.deleteDir(fromDir);
            }
         }

         if (createCount > 0) {
            this.log("Moved " + this.dirCopyMap.size() + " empty director" + (this.dirCopyMap.size() == 1 ? "y" : "ies") + " to " + createCount + " empty director" + (createCount == 1 ? "y" : "ies") + " under " + this.destDir.getAbsolutePath());
         }
      }

   }

   private void moveFile(File fromFile, File toFile, boolean filtering, boolean overwrite) {
      boolean moved = false;

      try {
         this.log("Attempting to rename: " + fromFile + " to " + toFile, this.verbosity);
         moved = this.renameFile(fromFile, toFile, filtering, this.forceOverwrite);
      } catch (IOException var8) {
         String msg = "Failed to rename " + fromFile + " to " + toFile + " due to " + var8.getMessage();
         throw new BuildException(msg, var8, this.getLocation());
      }

      if (!moved) {
         this.copyFile(fromFile, toFile, filtering, overwrite);
         if (!fromFile.delete()) {
            throw new BuildException("Unable to delete file " + fromFile.getAbsolutePath());
         }
      }

   }

   private void copyFile(File fromFile, File toFile, boolean filtering, boolean overwrite) {
      try {
         this.log("Copying " + fromFile + " to " + toFile, this.verbosity);
         FilterSetCollection executionFilters = new FilterSetCollection();
         if (filtering) {
            executionFilters.addFilterSet(this.getProject().getGlobalFilterSet());
         }

         Enumeration filterEnum = this.getFilterSets().elements();

         while(filterEnum.hasMoreElements()) {
            executionFilters.addFilterSet((FilterSet)filterEnum.nextElement());
         }

         this.getFileUtils().copyFile(fromFile, toFile, executionFilters, this.getFilterChains(), this.forceOverwrite, this.getPreserveLastModified(), this.getEncoding(), this.getOutputEncoding(), this.getProject());
      } catch (IOException var7) {
         String msg = "Failed to copy " + fromFile + " to " + toFile + " due to " + var7.getMessage();
         throw new BuildException(msg, var7, this.getLocation());
      }
   }

   protected boolean okToDelete(File d) {
      String[] list = d.list();
      if (list == null) {
         return false;
      } else {
         for(int i = 0; i < list.length; ++i) {
            String s = list[i];
            File f = new File(d, s);
            if (!f.isDirectory()) {
               return false;
            }

            if (!this.okToDelete(f)) {
               return false;
            }
         }

         return true;
      }
   }

   protected void deleteDir(File d) {
      this.deleteDir(d, false);
   }

   protected void deleteDir(File d, boolean deleteFiles) {
      String[] list = d.list();
      if (list != null) {
         for(int i = 0; i < list.length; ++i) {
            String s = list[i];
            File f = new File(d, s);
            if (!f.isDirectory()) {
               if (deleteFiles && !f.delete()) {
                  throw new BuildException("Unable to delete file " + f.getAbsolutePath());
               }

               throw new BuildException("UNEXPECTED ERROR - The file " + f.getAbsolutePath() + " should not exist!");
            }

            this.deleteDir(f);
         }

         this.log("Deleting directory " + d.getAbsolutePath(), this.verbosity);
         if (!d.delete()) {
            throw new BuildException("Unable to delete directory " + d.getAbsolutePath());
         }
      }
   }

   protected boolean renameFile(File sourceFile, File destFile, boolean filtering, boolean overwrite) throws IOException, BuildException {
      boolean renamed = false;
      if (this.getFilterSets().size() + this.getFilterChains().size() == 0 && !filtering && !destFile.isDirectory()) {
         File parent = destFile.getParentFile();
         if (parent != null && !parent.exists()) {
            parent.mkdirs();
         }

         if (destFile.isFile() && !destFile.equals(sourceFile) && !destFile.delete()) {
            throw new BuildException("Unable to remove existing file " + destFile);
         }

         renamed = sourceFile.renameTo(destFile);
      }

      return renamed;
   }
}
