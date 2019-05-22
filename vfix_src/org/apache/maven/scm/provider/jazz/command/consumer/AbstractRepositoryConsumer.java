package org.apache.maven.scm.provider.jazz.command.consumer;

import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.util.AbstractConsumer;

public abstract class AbstractRepositoryConsumer extends AbstractConsumer {
   private ScmProviderRepository repository = null;
   protected boolean fed = false;

   public AbstractRepositoryConsumer(ScmProviderRepository repository, ScmLogger logger) {
      super(logger);
      this.setRepository(repository);
   }

   public ScmProviderRepository getRepository() {
      return this.repository;
   }

   public void setRepository(ScmProviderRepository repository) {
      this.repository = repository;
   }

   public boolean isFed() {
      return this.fed;
   }

   public void setFed(boolean fed) {
      this.fed = fed;
   }

   public void consumeLine(String line) {
      this.getLogger().debug("Consumed line :" + line);
      this.fed = true;
   }
}
