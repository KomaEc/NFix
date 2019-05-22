package org.apache.maven.scm.provider.cvslib.command.login;

import java.io.IOException;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.login.AbstractLoginCommand;
import org.apache.maven.scm.command.login.LoginScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.command.CvsCommandUtils;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;

public class CvsLoginCommand extends AbstractLoginCommand {
   public LoginScmResult executeLoginCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      CvsScmProviderRepository repo = (CvsScmProviderRepository)repository;
      if (!"pserver".equals(repo.getTransport())) {
         return new LoginScmResult((String)null, "The cvs login ignored for " + repo.getTransport() + ".", "", true);
      } else if (this.isCvsNT()) {
         return new LoginScmResult((String)null, "The cvs login ignored for CVSNT.", "", true);
      } else {
         CvsPass passGenerator = new CvsPass(this.getLogger());
         passGenerator.setCvsroot(repo.getCvsRootForCvsPass());
         passGenerator.setPassword(repo.getPassword());

         try {
            passGenerator.execute();
         } catch (IOException var7) {
            throw new ScmException("Error while executing cvs login command.", var7);
         }

         return new LoginScmResult((String)null, "The cvs command succeed.", "", true);
      }
   }

   public boolean isCvsNT() throws ScmException {
      return CvsCommandUtils.isCvsNT();
   }
}
