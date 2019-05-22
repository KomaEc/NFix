package org.apache.maven.scm.provider.bazaar.command.tag;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.bazaar.command.BazaarConsumer;

class BazaarLsConsumer extends BazaarConsumer {
   private File repositoryRoot;
   private List<ScmFile> files = new LinkedList();

   public BazaarLsConsumer(ScmLogger logger, File repositoryRoot, ScmFileStatus wantedStatus) {
      super(logger);
   }

   public void doConsume(ScmFileStatus status, String trimmedLine) {
      if (!trimmedLine.endsWith(File.separator)) {
         String path = (new File(this.repositoryRoot, trimmedLine)).toString();
         this.files.add(new ScmFile(path, ScmFileStatus.TAGGED));
      }
   }

   public List<ScmFile> getListedFiles() {
      return this.files;
   }
}
