package org.apache.maven.scm.provider.jazz.command.add;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.consumer.AbstractRepositoryConsumer;

public class JazzAddConsumer extends AbstractRepositoryConsumer {
   private boolean haveSeenChanges = false;
   protected String fCurrentDir = "";
   private List<ScmFile> fCheckedInFiles = new ArrayList();

   public JazzAddConsumer(ScmProviderRepository repository, ScmLogger logger) {
      super(repository, logger);
   }

   public void consumeLine(String line) {
      super.consumeLine(line);
      if (this.haveSeenChanges) {
         String trimmed = line.trim();
         int spacePos = trimmed.indexOf(" ");
         String path = trimmed.substring(spacePos + 1 + 1);
         this.fCheckedInFiles.add(new ScmFile(path, ScmFileStatus.CHECKED_OUT));
      } else if ("Changes:".equals(line.trim())) {
         this.haveSeenChanges = true;
      }

   }

   protected ScmFile getScmFile(String filename) {
      return new ScmFile((new File(this.fCurrentDir, filename)).getAbsolutePath(), ScmFileStatus.CHECKED_OUT);
   }

   public List<ScmFile> getFiles() {
      return this.fCheckedInFiles;
   }
}
