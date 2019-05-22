package org.apache.maven.scm.provider.starteam.command.unedit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class StarteamUnEditConsumer implements StreamConsumer {
   private String workingDirectory;
   private ScmLogger logger;
   private List<ScmFile> files = new ArrayList();
   private String currentDir = "";
   private static final String DIR_MARKER = "(working dir: ";
   private static final String UNLOCKED_MARKER = ": unlocked";

   public StarteamUnEditConsumer(ScmLogger logger, File basedir) {
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
      } else if ((pos = line.indexOf(": unlocked")) != -1) {
         this.processUnLockedFile(line, pos);
      } else if (this.logger.isWarnEnabled()) {
         this.logger.warn("Unknown unedit ouput: " + line);
      }

   }

   public List<ScmFile> getUnEditFiles() {
      return this.files;
   }

   private void processDirectory(String line, int pos) {
      String dirPath = line.substring(pos + "(working dir: ".length(), line.length() - 1).replace('\\', '/');
      if (!dirPath.startsWith(this.workingDirectory)) {
         if (this.logger.isInfoEnabled()) {
            this.logger.info("Working directory: " + this.workingDirectory);
            this.logger.info("unedit directory: " + dirPath);
         }

         throw new IllegalStateException("Working and unedit directories are not on the same tree");
      } else {
         this.currentDir = "." + dirPath.substring(this.workingDirectory.length());
      }
   }

   private void processUnLockedFile(String line, int pos) {
      String lockedFilePath = this.currentDir + "/" + line.substring(0, pos);
      this.files.add(new ScmFile(lockedFilePath, ScmFileStatus.UNKNOWN));
      if (this.logger.isInfoEnabled()) {
         this.logger.info("Unlocked: " + lockedFilePath);
      }

   }
}
