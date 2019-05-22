package org.apache.maven.scm.provider.svn.svnexe;

import java.io.File;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.info.InfoItem;
import org.apache.maven.scm.command.info.InfoScmResult;
import org.apache.maven.scm.command.remoteinfo.RemoteInfoScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.AbstractSvnScmProvider;
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.svnexe.command.add.SvnAddCommand;
import org.apache.maven.scm.provider.svn.svnexe.command.blame.SvnBlameCommand;
import org.apache.maven.scm.provider.svn.svnexe.command.branch.SvnBranchCommand;
import org.apache.maven.scm.provider.svn.svnexe.command.changelog.SvnChangeLogCommand;
import org.apache.maven.scm.provider.svn.svnexe.command.checkin.SvnCheckInCommand;
import org.apache.maven.scm.provider.svn.svnexe.command.checkout.SvnCheckOutCommand;
import org.apache.maven.scm.provider.svn.svnexe.command.diff.SvnDiffCommand;
import org.apache.maven.scm.provider.svn.svnexe.command.export.SvnExeExportCommand;
import org.apache.maven.scm.provider.svn.svnexe.command.info.SvnInfoCommand;
import org.apache.maven.scm.provider.svn.svnexe.command.list.SvnListCommand;
import org.apache.maven.scm.provider.svn.svnexe.command.mkdir.SvnMkdirCommand;
import org.apache.maven.scm.provider.svn.svnexe.command.remoteinfo.SvnRemoteInfoCommand;
import org.apache.maven.scm.provider.svn.svnexe.command.remove.SvnRemoveCommand;
import org.apache.maven.scm.provider.svn.svnexe.command.status.SvnStatusCommand;
import org.apache.maven.scm.provider.svn.svnexe.command.tag.SvnTagCommand;
import org.apache.maven.scm.provider.svn.svnexe.command.update.SvnUpdateCommand;
import org.apache.maven.scm.repository.ScmRepositoryException;

public class SvnExeScmProvider extends AbstractSvnScmProvider {
   protected SvnCommand getAddCommand() {
      return new SvnAddCommand();
   }

   protected SvnCommand getBranchCommand() {
      return new SvnBranchCommand();
   }

   protected SvnCommand getChangeLogCommand() {
      return new SvnChangeLogCommand();
   }

   protected SvnCommand getCheckInCommand() {
      return new SvnCheckInCommand();
   }

   protected SvnCommand getCheckOutCommand() {
      return new SvnCheckOutCommand();
   }

   protected SvnCommand getDiffCommand() {
      return new SvnDiffCommand();
   }

   protected SvnCommand getExportCommand() {
      return new SvnExeExportCommand();
   }

   protected SvnCommand getRemoveCommand() {
      return new SvnRemoveCommand();
   }

   protected SvnCommand getStatusCommand() {
      return new SvnStatusCommand();
   }

   protected SvnCommand getTagCommand() {
      return new SvnTagCommand();
   }

   protected SvnCommand getUpdateCommand() {
      return new SvnUpdateCommand();
   }

   protected SvnCommand getListCommand() {
      return new SvnListCommand();
   }

   public SvnCommand getInfoCommand() {
      return new SvnInfoCommand();
   }

   protected SvnCommand getBlameCommand() {
      return new SvnBlameCommand();
   }

   protected SvnCommand getMkdirCommand() {
      return new SvnMkdirCommand();
   }

   protected String getRepositoryURL(File path) throws ScmException {
      SvnInfoCommand infoCmd = (SvnInfoCommand)this.getInfoCommand();
      infoCmd.setLogger(this.getLogger());
      InfoScmResult result = infoCmd.executeInfoCommand((SvnScmProviderRepository)null, new ScmFileSet(new File(""), path), (CommandParameters)null, false, (String)null);
      if (result.getInfoItems().size() != 1) {
         throw new ScmRepositoryException("Cannot find URL: " + (result.getInfoItems().size() == 0 ? "no" : "multiple") + " items returned by the info command");
      } else {
         return ((InfoItem)result.getInfoItems().get(0)).getURL();
      }
   }

   public RemoteInfoScmResult remoteInfo(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      SvnRemoteInfoCommand svnRemoteInfoCommand = new SvnRemoteInfoCommand();
      return svnRemoteInfoCommand.executeRemoteInfoCommand(repository, fileSet, parameters);
   }

   public boolean remoteUrlExist(ScmProviderRepository repository, CommandParameters parameters) throws ScmException {
      SvnRemoteInfoCommand svnRemoteInfoCommand = new SvnRemoteInfoCommand();
      return svnRemoteInfoCommand.remoteUrlExist(repository, parameters);
   }
}
