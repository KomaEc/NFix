package org.apache.maven.scm.provider.svn.svnexe.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public abstract class AbstractFileCheckingConsumer implements StreamConsumer {
   protected ScmLogger logger;
   protected File workingDirectory;
   private List<ScmFile> files = new ArrayList();
   protected int revision;
   private boolean filtered;

   public AbstractFileCheckingConsumer(ScmLogger logger, File workingDirectory) {
      this.logger = logger;
      this.workingDirectory = workingDirectory;
   }

   public final void consumeLine(String line) {
      if (line.length() > 3) {
         if (this.logger.isDebugEnabled()) {
            this.logger.debug(line);
         }

         try {
            this.parseLine(line);
         } catch (RuntimeException var3) {
            this.logger.warn("RuntimeException while parsing: " + line, var3);
            throw var3;
         }
      }
   }

   protected abstract void parseLine(String var1);

   protected List<ScmFile> getFiles() {
      if (!this.filtered) {
         Iterator ite = this.files.iterator();

         while(ite.hasNext()) {
            ScmFile file = (ScmFile)ite.next();
            if (!file.getStatus().equals(ScmFileStatus.DELETED) && !(new File(this.workingDirectory, file.getPath())).isFile()) {
               ite.remove();
            }
         }

         this.filtered = true;
      }

      return this.files;
   }

   protected final int parseInt(String revisionString) {
      try {
         return Integer.parseInt(revisionString);
      } catch (NumberFormatException var3) {
         return 0;
      }
   }

   protected void addFile(ScmFile file) {
      this.files.add(file);
   }

   public final int getRevision() {
      return this.revision;
   }

   public File getWorkingDirectory() {
      return this.workingDirectory;
   }
}
