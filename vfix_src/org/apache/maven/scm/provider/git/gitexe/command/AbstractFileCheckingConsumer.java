package org.apache.maven.scm.provider.git.gitexe.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmFile;
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

         this.parseLine(line);
      }
   }

   protected abstract void parseLine(String var1);

   protected List<ScmFile> getFiles() {
      if (!this.filtered) {
         Iterator it = this.files.iterator();

         while(it.hasNext()) {
            if (!(new File(this.workingDirectory, ((ScmFile)it.next()).getPath())).isFile()) {
               it.remove();
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
}
