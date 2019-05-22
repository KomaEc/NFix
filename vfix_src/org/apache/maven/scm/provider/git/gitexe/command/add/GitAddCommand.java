package org.apache.maven.scm.provider.git.gitexe.command.add;

import com.gzoltar.shaded.org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.add.AbstractAddCommand;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.gitexe.command.GitCommandLineUtils;
import org.apache.maven.scm.provider.git.gitexe.command.status.GitStatusCommand;
import org.apache.maven.scm.provider.git.gitexe.command.status.GitStatusConsumer;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitAddCommand extends AbstractAddCommand implements GitCommand {
   protected ScmResult executeAddCommand(ScmProviderRepository repo, ScmFileSet fileSet, String message, boolean binary) throws ScmException {
      GitScmProviderRepository repository = (GitScmProviderRepository)repo;
      if (fileSet.getFileList().isEmpty()) {
         throw new ScmException("You must provide at least one file/directory to add");
      } else {
         AddScmResult result = this.executeAddFileSet(fileSet);
         if (result != null) {
            return result;
         } else {
            Commandline clRevparse = GitStatusCommand.createRevparseShowToplevelCommand(fileSet);
            CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
            CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
            URI relativeRepositoryPath = null;
            int exitCode = GitCommandLineUtils.execute(clRevparse, stdout, stderr, this.getLogger());
            if (exitCode != 0) {
               if (this.getLogger().isInfoEnabled()) {
                  this.getLogger().info("Could not resolve toplevel");
               }
            } else {
               relativeRepositoryPath = GitStatusConsumer.resolveURI(stdout.getOutput().trim(), fileSet.getBasedir().toURI());
            }

            Commandline clStatus = GitStatusCommand.createCommandLine(repository, fileSet);
            GitStatusConsumer statusConsumer = new GitStatusConsumer(this.getLogger(), fileSet.getBasedir(), relativeRepositoryPath);
            stderr = new CommandLineUtils.StringStreamConsumer();
            exitCode = GitCommandLineUtils.execute(clStatus, (StreamConsumer)statusConsumer, stderr, this.getLogger());
            if (exitCode != 0 && this.getLogger().isInfoEnabled()) {
               this.getLogger().info("nothing added to commit but untracked files present (use \"git add\" to track)");
            }

            List<ScmFile> changedFiles = new ArrayList();
            Iterator i$ = statusConsumer.getChangedFiles().iterator();

            while(i$.hasNext()) {
               ScmFile scmfile = (ScmFile)i$.next();
               Iterator i$ = fileSet.getFileList().iterator();

               while(i$.hasNext()) {
                  File f = (File)i$.next();
                  if (FilenameUtils.separatorsToUnix(f.getPath()).equals(scmfile.getPath())) {
                     changedFiles.add(scmfile);
                  }
               }
            }

            Commandline cl = createCommandLine(fileSet.getBasedir(), fileSet.getFileList());
            return new AddScmResult(cl.toString(), changedFiles);
         }
      }
   }

   public static Commandline createCommandLine(File workingDirectory, List<File> files) throws ScmException {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(workingDirectory, "add");
      cl.createArg().setValue("--");
      GitCommandLineUtils.addTarget(cl, files);
      return cl;
   }

   private AddScmResult executeAddFileSet(ScmFileSet fileSet) throws ScmException {
      File workingDirectory = fileSet.getBasedir();
      List<File> files = fileSet.getFileList();
      if (Os.isFamily("windows")) {
         Iterator i$ = files.iterator();

         while(i$.hasNext()) {
            File file = (File)i$.next();
            AddScmResult result = this.executeAddFiles(workingDirectory, Collections.singletonList(file));
            if (result != null) {
               return result;
            }
         }
      } else {
         AddScmResult result = this.executeAddFiles(workingDirectory, files);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   private AddScmResult executeAddFiles(File workingDirectory, List<File> files) throws ScmException {
      Commandline cl = createCommandLine(workingDirectory, files);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      int exitCode = GitCommandLineUtils.execute(cl, stdout, stderr, this.getLogger());
      return exitCode != 0 ? new AddScmResult(cl.toString(), "The git-add command failed.", stderr.getOutput(), false) : null;
   }
}
