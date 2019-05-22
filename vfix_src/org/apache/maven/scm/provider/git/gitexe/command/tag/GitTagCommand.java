package org.apache.maven.scm.provider.git.gitexe.command.tag;

import java.io.File;
import java.io.IOException;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.command.tag.AbstractTagCommand;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.gitexe.command.GitCommandLineUtils;
import org.apache.maven.scm.provider.git.gitexe.command.list.GitListCommand;
import org.apache.maven.scm.provider.git.gitexe.command.list.GitListConsumer;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitTagCommand extends AbstractTagCommand implements GitCommand {
   public ScmResult executeTagCommand(ScmProviderRepository repo, ScmFileSet fileSet, String tag, String message) throws ScmException {
      return this.executeTagCommand(repo, fileSet, tag, new ScmTagParameters(message));
   }

   public ScmResult executeTagCommand(ScmProviderRepository repo, ScmFileSet fileSet, String tag, ScmTagParameters scmTagParameters) throws ScmException {
      if (tag != null && !StringUtils.isEmpty(tag.trim())) {
         if (!fileSet.getFileList().isEmpty()) {
            throw new ScmException("This provider doesn't support tagging subsets of a directory");
         } else {
            GitScmProviderRepository repository = (GitScmProviderRepository)repo;
            File messageFile = FileUtils.createTempFile("maven-scm-", ".commit", (File)null);

            try {
               FileUtils.fileWrite(messageFile.getAbsolutePath(), scmTagParameters.getMessage());
            } catch (IOException var25) {
               return new TagScmResult((String)null, "Error while making a temporary file for the commit message: " + var25.getMessage(), (String)null, false);
            }

            TagScmResult var11;
            try {
               CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
               CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
               Commandline clTag = createCommandLine(repository, fileSet.getBasedir(), tag, messageFile);
               int exitCode = GitCommandLineUtils.execute(clTag, stdout, stderr, this.getLogger());
               if (exitCode == 0) {
                  if (repo.isPushChanges()) {
                     Commandline clPush = createPushCommandLine(repository, fileSet, tag);
                     exitCode = GitCommandLineUtils.execute(clPush, stdout, stderr, this.getLogger());
                     if (exitCode != 0) {
                        TagScmResult var29 = new TagScmResult(clPush.toString(), "The git-push command failed.", stderr.getOutput(), false);
                        return var29;
                     }
                  }

                  GitListConsumer listConsumer = new GitListConsumer(this.getLogger(), fileSet.getBasedir(), ScmFileStatus.TAGGED);
                  Commandline clList = GitListCommand.createCommandLine(repository, fileSet.getBasedir());
                  exitCode = GitCommandLineUtils.execute(clList, (StreamConsumer)listConsumer, stderr, this.getLogger());
                  if (exitCode != 0) {
                     CheckOutScmResult var30 = new CheckOutScmResult(clList.toString(), "The git-ls-files command failed.", stderr.getOutput(), false);
                     return var30;
                  }

                  TagScmResult var13 = new TagScmResult(clTag.toString(), listConsumer.getListedFiles());
                  return var13;
               }

               var11 = new TagScmResult(clTag.toString(), "The git-tag command failed.", stderr.getOutput(), false);
            } finally {
               try {
                  FileUtils.forceDelete(messageFile);
               } catch (IOException var24) {
               }

            }

            return var11;
         }
      } else {
         throw new ScmException("tag name must be specified");
      }
   }

   public static Commandline createCommandLine(GitScmProviderRepository repository, File workingDirectory, String tag, File messageFile) {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(workingDirectory, "tag");
      cl.createArg().setValue("-F");
      cl.createArg().setValue(messageFile.getAbsolutePath());
      cl.createArg().setValue(tag);
      return cl;
   }

   public static Commandline createPushCommandLine(GitScmProviderRepository repository, ScmFileSet fileSet, String tag) throws ScmException {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(fileSet.getBasedir(), "push");
      cl.createArg().setValue(repository.getPushUrl());
      cl.createArg().setValue("refs/tags/" + tag);
      return cl;
   }
}
