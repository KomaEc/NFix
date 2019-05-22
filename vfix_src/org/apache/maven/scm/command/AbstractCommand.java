package org.apache.maven.scm.command;

import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractCommand implements Command {
   private ScmLogger logger;

   protected abstract ScmResult executeCommand(ScmProviderRepository var1, ScmFileSet var2, CommandParameters var3) throws ScmException;

   public final ScmResult execute(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      if (repository == null) {
         throw new NullPointerException("repository cannot be null");
      } else if (fileSet == null) {
         throw new NullPointerException("fileSet cannot be null");
      } else {
         try {
            return this.executeCommand(repository, fileSet, parameters);
         } catch (Exception var5) {
            throw new ScmException("Exception while executing SCM command.", var5);
         }
      }
   }

   public final ScmLogger getLogger() {
      return this.logger;
   }

   public final void setLogger(ScmLogger logger) {
      this.logger = logger;
   }
}
