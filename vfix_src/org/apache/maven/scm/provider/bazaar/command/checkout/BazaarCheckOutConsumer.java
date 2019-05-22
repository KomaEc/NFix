package org.apache.maven.scm.provider.bazaar.command.checkout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.bazaar.command.BazaarConsumer;

public class BazaarCheckOutConsumer extends BazaarConsumer {
   private final File workingDirectory;
   private final List<ScmFile> checkedOut = new ArrayList();

   public BazaarCheckOutConsumer(ScmLogger logger, File workingDirectory) {
      super(logger);
      this.workingDirectory = workingDirectory;
   }

   public void doConsume(ScmFileStatus status, String line) {
      File file = new File(this.workingDirectory, line);
      if (file.isFile()) {
         this.checkedOut.add(new ScmFile(line, ScmFileStatus.CHECKED_OUT));
      }

   }

   public List<ScmFile> getCheckedOutFiles() {
      return this.checkedOut;
   }
}
