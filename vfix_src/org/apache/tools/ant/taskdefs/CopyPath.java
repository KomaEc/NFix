package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;

public class CopyPath extends Task {
   public static final String ERROR_NO_DESTDIR = "No destDir specified";
   public static final String ERROR_NO_PATH = "No path specified";
   public static final String ERROR_NO_MAPPER = "No mapper specified";
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private FileNameMapper mapper;
   private Path path;
   private File destDir;
   private long granularity;
   private boolean preserveLastModified;

   public CopyPath() {
      this.granularity = FILE_UTILS.getFileTimestampGranularity();
      this.preserveLastModified = false;
   }

   public void setDestDir(File destDir) {
      this.destDir = destDir;
   }

   public void add(FileNameMapper newmapper) {
      if (this.mapper != null) {
         throw new BuildException("Only one mapper allowed");
      } else {
         this.mapper = newmapper;
      }
   }

   public void setPath(Path s) {
      this.createPath().append(s);
   }

   public void setPathRef(Reference r) {
      this.createPath().setRefid(r);
   }

   public Path createPath() {
      if (this.path == null) {
         this.path = new Path(this.getProject());
      }

      return this.path;
   }

   public void setGranularity(long granularity) {
      this.granularity = granularity;
   }

   public void setPreserveLastModified(boolean preserveLastModified) {
      this.preserveLastModified = preserveLastModified;
   }

   protected void validateAttributes() throws BuildException {
      if (this.destDir == null) {
         throw new BuildException("No destDir specified");
      } else if (this.mapper == null) {
         throw new BuildException("No mapper specified");
      } else if (this.path == null) {
         throw new BuildException("No path specified");
      }
   }

   public void execute() throws BuildException {
      this.validateAttributes();
      String[] sourceFiles = this.path.list();
      if (sourceFiles.length == 0) {
         this.log("Path is empty", 3);
      } else {
         for(int sources = 0; sources < sourceFiles.length; ++sources) {
            String sourceFileName = sourceFiles[sources];
            File sourceFile = new File(sourceFileName);
            String[] toFiles = (String[])this.mapper.mapFileName(sourceFileName);

            for(int i = 0; i < toFiles.length; ++i) {
               String destFileName = toFiles[i];
               File destFile = new File(this.destDir, destFileName);
               if (sourceFile.equals(destFile)) {
                  this.log("Skipping self-copy of " + sourceFileName, 3);
               } else if (sourceFile.isDirectory()) {
                  this.log("Skipping directory " + sourceFileName);
               } else {
                  try {
                     this.log("Copying " + sourceFile + " to " + destFile, 3);
                     FILE_UTILS.copyFile((File)sourceFile, (File)destFile, (FilterSetCollection)null, (Vector)null, false, this.preserveLastModified, (String)null, (String)null, this.getProject());
                  } catch (IOException var11) {
                     String msg = "Failed to copy " + sourceFile + " to " + destFile + " due to " + var11.getMessage();
                     if (destFile.exists() && !destFile.delete()) {
                        msg = msg + " and I couldn't delete the corrupt " + destFile;
                     }

                     throw new BuildException(msg, var11, this.getLocation());
                  }
               }
            }
         }

      }
   }
}
