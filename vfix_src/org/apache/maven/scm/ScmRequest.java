package org.apache.maven.scm;

import java.io.Serializable;
import org.apache.maven.scm.repository.ScmRepository;

public class ScmRequest implements Serializable {
   private static final long serialVersionUID = 20120620L;
   private ScmRepository scmRepository;
   private ScmFileSet scmFileSet;
   protected final CommandParameters parameters = new CommandParameters();

   public ScmRequest() {
   }

   public ScmRequest(ScmRepository scmRepository, ScmFileSet scmFileSet) {
      this.scmRepository = scmRepository;
      this.scmFileSet = scmFileSet;
   }

   public ScmRepository getScmRepository() {
      return this.scmRepository;
   }

   public void setScmRepository(ScmRepository scmRepository) {
      this.scmRepository = scmRepository;
   }

   public ScmFileSet getScmFileSet() {
      return this.scmFileSet;
   }

   public void setScmFileSet(ScmFileSet scmFileSet) {
      this.scmFileSet = scmFileSet;
   }

   public CommandParameters getCommandParameters() {
      return this.parameters;
   }
}
