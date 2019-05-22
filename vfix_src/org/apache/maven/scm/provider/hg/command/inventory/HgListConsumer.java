package org.apache.maven.scm.provider.hg.command.inventory;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.hg.command.HgConsumer;

public class HgListConsumer extends HgConsumer {
   private List<ScmFile> files = new ArrayList();

   public HgListConsumer(ScmLogger logger) {
      super(logger);
   }

   public void doConsume(ScmFileStatus status, String trimmedLine) {
      this.files.add(new ScmFile(trimmedLine, status));
   }

   public List<ScmFile> getFiles() {
      return this.files;
   }
}
