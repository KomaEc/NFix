package org.apache.maven.scm.provider.starteam.command.checkin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class StarteamCheckInConsumer implements StreamConsumer {
   private String workingDirectory;
   private ScmLogger logger;
   private List<ScmFile> files = new ArrayList();
   private String currentDir = "";
   private static final String DIR_MARKER = "(working dir: ";
   private static final String CHECKIN_MARKER = ": checked in";
   private static final String SKIPPED_MARKER = ": skipped";
   private static final String LINKTO_MARKER = ": linked to";

   public StarteamCheckInConsumer(ScmLogger logger, File basedir) {
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
      } else if ((pos = line.indexOf(": checked in")) != -1) {
         this.processCheckedInFile(line, pos);
      } else if ((pos = line.indexOf(": skipped")) != -1) {
         this.processSkippedFile(line, pos);
      } else if (line.indexOf(": linked to") == -1 && this.logger.isWarnEnabled()) {
         this.logger.warn("Unknown checkin ouput: " + line);
      }

   }

   public List<ScmFile> getCheckedInFiles() {
      return this.files;
   }

   private void processDirectory(String line, int pos) {
      String dirPath = line.substring(pos + "(working dir: ".length(), line.length() - 1).replace('\\', '/');
      if (!dirPath.startsWith(this.workingDirectory)) {
         if (this.logger.isInfoEnabled()) {
            this.logger.info("Working directory: " + this.workingDirectory);
            this.logger.info("Checkin directory: " + dirPath);
         }

         throw new IllegalStateException("Working and checkin directories are not on the same tree");
      } else {
         this.currentDir = "." + dirPath.substring(this.workingDirectory.length());
      }
   }

   private void processCheckedInFile(String line, int pos) {
      String checkedInFilePath = this.currentDir + "/" + line.substring(0, pos);
      this.files.add(new ScmFile(checkedInFilePath, ScmFileStatus.CHECKED_OUT));
      if (this.logger.isInfoEnabled()) {
         this.logger.info("Checked in: " + checkedInFilePath);
      }

   }

   private void processSkippedFile(String line, int pos) {
      String skippedFilePath = this.currentDir + "/" + line.substring(0, pos);
      if (this.logger.isInfoEnabled()) {
         this.logger.info("Skipped: " + skippedFilePath);
      }

   }
}
