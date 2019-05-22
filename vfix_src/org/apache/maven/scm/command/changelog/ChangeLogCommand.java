package org.apache.maven.scm.command.changelog;

import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;

public interface ChangeLogCommand {
   ScmResult executeCommand(ScmProviderRepository var1, ScmFileSet var2, CommandParameters var3) throws ScmException;
}
