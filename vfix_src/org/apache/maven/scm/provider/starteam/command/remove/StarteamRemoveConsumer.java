package org.apache.maven.scm.provider.starteam.command.remove;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.starteam.command.StarteamCommandLineUtils;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class StarteamRemoveConsumer implements StreamConsumer {
   private ScmLogger logger;
   private String workingDirectory;
   private String currentDir;
   private List<ScmFile> files = new ArrayList();
   private static final String DIR_MARKER = "(working dir: ";
   private static final String ADDED_MARKER = ": removed";
   private static final String LINKTO_MARKER = ": linked to";

   public StarteamRemoveConsumer(ScmLogger logger, File basedir) {
      this.logger = logger;
      this.workingDirectory = basedir.getPath().replace('\\', '/');
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      int pos = false;
      int pos;
      if ((pos = line.indexOf("(working dir: ")) != -1) {
         this.processDirectory(line, pos);
      } else if ((pos = line.indexOf(": removed")) != -1) {
         this.processRemovedFile(line, pos);
      } else if (line.indexOf(": linked to") == -1 && this.logger.isWarnEnabled()) {
         this.logger.warn("Unknown remove ouput: " + line);
      }

   }

   public List<ScmFile> getRemovedFiles() {
      return this.files;
   }

   private void processDirectory(String line, int pos) {
      String dirPath = line.substring(pos + "(working dir: ".length(), line.length() - 1).replace('\\', '/');

      try {
         this.currentDir = StarteamCommandLineUtils.getRelativeChildDirectory(this.workingDirectory, dirPath);
      } catch (IllegalStateException var6) {
         String error = "Working and checkout directories are not on the same tree";
         if (this.logger.isErrorEnabled()) {
            this.logger.error(error);
            this.logger.error("Working directory: " + this.workingDirectory);
            this.logger.error("Checked out directory: " + dirPath);
         }

         throw new IllegalStateException(error);
      }
   }

   private void processRemovedFile(String line, int pos) {
      String addedFilePath = this.currentDir + "/" + line.substring(0, pos);
      this.files.add(new ScmFile(addedFilePath, ScmFileStatus.DELETED));
      if (this.logger.isInfoEnabled()) {
         this.logger.info("Removed: " + addedFilePath);
      }

   }
}
