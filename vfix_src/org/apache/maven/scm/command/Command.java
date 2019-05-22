package org.apache.maven.scm.command;

import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;

public interface Command {
   String ROLE = Command.class.getName();

   ScmResult execute(ScmProviderRepository var1, ScmFileSet var2, CommandParameters var3) throws ScmException;

   void setLogger(ScmLogger var1);

   ScmLogger getLogger();
}
