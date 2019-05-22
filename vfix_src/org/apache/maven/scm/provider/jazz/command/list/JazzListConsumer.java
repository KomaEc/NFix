package org.apache.maven.scm.provider.jazz.command.list;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.consumer.AbstractRepositoryConsumer;

public class JazzListConsumer extends AbstractRepositoryConsumer {
   private List<ScmFile> files = new ArrayList();

   public JazzListConsumer(ScmProviderRepository repository, ScmLogger logger) {
      super(repository, logger);
   }

   public void consumeLine(String line) {
      super.consumeLine(line);
      this.files.add(new ScmFile(line, ScmFileStatus.CHECKED_IN));
   }

   public List<ScmFile> getFiles() {
      return this.files;
   }
}
