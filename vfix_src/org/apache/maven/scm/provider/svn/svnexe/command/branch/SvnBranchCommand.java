package org.apache.maven.scm.provider.svn.svnexe.command.branch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmBranchParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.branch.AbstractBranchCommand;
import org.apache.maven.scm.command.branch.BranchScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.SvnCommandUtils;
import org.apache.maven.scm.provider.svn.SvnTagBranchUtils;
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.svnexe.command.SvnCommandLineUtils;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class SvnBranchCommand extends AbstractBranchCommand implements SvnCommand {
   public ScmResult executeBranchCommand(ScmProviderRepository repo, ScmFileSet fileSet, String branch, ScmBranchParameters scmBranchParameters) throws ScmException {
      if (branch != null && !StringUtils.isEmpty(branch.trim())) {
         if (!fileSet.getFileList().isEmpty()) {
            throw new ScmException("This provider doesn't support branching subsets of a directory");
         } else {
            SvnScmProviderRepository repository = (SvnScmProviderRepository)repo;
            File messageFile = FileUtils.createTempFile("maven-scm-", ".commit", (File)null);

            try {
               FileUtils.fileWrite(messageFile.getAbsolutePath(), scmBranchParameters.getMessage());
            } catch (IOException var25) {
               return new BranchScmResult((String)null, "Error while making a temporary file for the commit message: " + var25.getMessage(), (String)null, false);
            }

            Commandline cl = createCommandLine(repository, fileSet.getBasedir(), branch, messageFile, scmBranchParameters);
            CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
            CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
            if (this.getLogger().isInfoEnabled()) {
               this.getLogger().info("Executing: " + SvnCommandLineUtils.cryptPassword(cl));
               this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
            }

            int exitCode;
            try {
               exitCode = SvnCommandLineUtils.execute(cl, stdout, stderr, this.getLogger());
            } catch (CommandLineException var23) {
               throw new ScmException("Error while executing command.", var23);
            } finally {
               try {
                  FileUtils.forceDelete(messageFile);
               } catch (IOException var21) {
               }

            }

            if (exitCode != 0) {
               return new BranchScmResult(cl.toString(), "The svn branch command failed.", stderr.getOutput(), false);
            } else {
               List<ScmFile> fileList = new ArrayList();
               List files = null;

               try {
                  List<File> listFiles = FileUtils.getFiles(fileSet.getBasedir(), "**", "**/.svn/**", false);
                  files = listFiles;
               } catch (IOException var22) {
                  throw new ScmException("Error while executing command.", var22);
               }

               Iterator i$ = files.iterator();

               while(i$.hasNext()) {
                  File f = (File)i$.next();
                  fileList.add(new ScmFile(f.getPath(), ScmFileStatus.TAGGED));
               }

               return new BranchScmResult(cl.toString(), fileList);
            }
         }
      } else {
         throw new ScmException("branch name must be specified");
      }
   }

   public ScmResult executeBranchCommand(ScmProviderRepository repo, ScmFileSet fileSet, String branch, String message) throws ScmException {
      ScmBranchParameters scmBranchParameters = new ScmBranchParameters(message);
      return this.executeBranchCommand(repo, fileSet, branch, scmBranchParameters);
   }

   public static Commandline createCommandLine(SvnScmProviderRepository repository, File workingDirectory, String branch, File messageFile) {
      ScmBranchParameters scmBranchParameters = new ScmBranchParameters();
      scmBranchParameters.setRemoteBranching(false);
      return createCommandLine(repository, workingDirectory, branch, messageFile, scmBranchParameters);
   }

   public static Commandline createCommandLine(SvnScmProviderRepository repository, File workingDirectory, String branch, File messageFile, ScmBranchParameters scmBranchParameters) {
      Commandline cl = SvnCommandLineUtils.getBaseSvnCommandLine(workingDirectory, repository);
      cl.createArg().setValue("copy");
      cl.createArg().setValue("--file");
      cl.createArg().setValue(messageFile.getAbsolutePath());
      if (scmBranchParameters != null && scmBranchParameters.isRemoteBranching()) {
         if (StringUtils.isNotBlank(scmBranchParameters.getScmRevision())) {
            cl.createArg().setValue("--revision");
            cl.createArg().setValue(scmBranchParameters.getScmRevision());
         }

         cl.createArg().setValue(repository.getUrl());
      } else {
         cl.createArg().setValue(".");
      }

      String branchUrl = SvnTagBranchUtils.resolveBranchUrl(repository, new ScmBranch(branch));
      cl.createArg().setValue(SvnCommandUtils.fixUrl(branchUrl, repository.getUser()));
      return cl;
   }
}
