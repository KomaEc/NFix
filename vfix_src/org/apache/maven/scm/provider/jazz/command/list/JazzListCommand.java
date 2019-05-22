package org.apache.maven.scm.provider.jazz.command.list;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.list.AbstractListCommand;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.JazzScmCommand;
import org.apache.maven.scm.provider.jazz.command.consumer.ErrorConsumer;
import org.apache.maven.scm.provider.jazz.repository.JazzScmProviderRepository;

public class JazzListCommand extends AbstractListCommand {
   protected ListScmResult executeListCommand(ScmProviderRepository repo, ScmFileSet fileSet, boolean recursive, ScmVersion version) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("Executing list command...");
      }

      JazzScmProviderRepository jazzRepo = (JazzScmProviderRepository)repo;
      JazzListConsumer listConsumer = new JazzListConsumer(repo, this.getLogger());
      ErrorConsumer errConsumer = new ErrorConsumer(this.getLogger());
      JazzScmCommand listCmd = this.createListCommand(jazzRepo, fileSet, recursive, version);
      int status = listCmd.execute(listConsumer, errConsumer);
      return status == 0 && !errConsumer.hasBeenFed() ? new ListScmResult(listCmd.getCommandString(), listConsumer.getFiles()) : new ListScmResult(listCmd.getCommandString(), "Error code for Jazz SCM list command - " + status, errConsumer.getOutput(), false);
   }

   public JazzScmCommand createListCommand(JazzScmProviderRepository repo, ScmFileSet fileSet, boolean recursive, ScmVersion version) {
      JazzScmCommand command = new JazzScmCommand("list", "remotefiles", repo, fileSet, this.getLogger());
      command.addArgument(repo.getRepositoryWorkspace());
      command.addArgument(repo.getComponent());
      return command;
   }
}
