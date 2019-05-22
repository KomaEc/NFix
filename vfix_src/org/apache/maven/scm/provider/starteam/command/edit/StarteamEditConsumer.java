package org.apache.maven.scm.provider.starteam.command.edit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class StarteamEditConsumer implements StreamConsumer {
   private String workingDirectory;
   private ScmLogger logger;
   private List<ScmFile> files = new ArrayList();
   private String currentDir = "";
   private static final String DIR_MARKER = "(working dir: ";
   private static final String LOCKED_MARKER = ": locked";

   public StarteamEditConsumer(ScmLogger logger, File basedir) {
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
      } else if ((pos = line.indexOf(": locked")) != -1) {
         this.processLockedFile(line, pos);
      } else if (this.logger.isWarnEnabled()) {
         this.logger.warn("Unknown edit ouput: " + line);
      }

   }

   public List<ScmFile> getEditedFiles() {
      return this.files;
   }

   private void processDirectory(String line, int pos) {
      String dirPath = line.substring(pos + "(working dir: ".length(), line.length() - 1).replace('\\', '/');
      if (!dirPath.startsWith(this.workingDirectory)) {
         if (this.logger.isInfoEnabled()) {
            this.logger.info("Working directory: " + this.workingDirectory);
            this.logger.info("Edit directory: " + dirPath);
         }

         throw new IllegalStateException("Working and edit directories are not on the same tree");
      } else {
         this.currentDir = "." + dirPath.substring(this.workingDirectory.length());
      }
   }

   private void processLockedFile(String line, int pos) {
      String lockedFilePath = this.currentDir + "/" + line.substring(0, pos);
      this.files.add(new ScmFile(lockedFilePath, ScmFileStatus.EDITED));
      if (this.logger.isInfoEnabled()) {
         this.logger.info("Locked: " + lockedFilePath);
      }

   }
}
