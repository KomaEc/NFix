package org.apache.maven.scm.provider.svn.svnexe.command.info;

import java.io.File;
import java.util.Iterator;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.command.info.InfoScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.svnexe.command.SvnCommandLineUtils;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnInfoCommand extends AbstractCommand implements SvnCommand {
   protected ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return this.executeInfoCommand((SvnScmProviderRepository)repository, fileSet, parameters, false, (String)null);
   }

   public InfoScmResult executeInfoCommand(SvnScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters, boolean recursive, String revision) throws ScmException {
      Commandline cl = createCommandLine(repository, fileSet, recursive, revision);
      SvnInfoConsumer consumer = new SvnInfoConsumer();
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + SvnCommandLineUtils.cryptPassword(cl));
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      int exitCode;
      try {
         exitCode = SvnCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
      } catch (CommandLineException var11) {
         throw new ScmException("Error while executing command.", var11);
      }

      return exitCode != 0 ? new InfoScmResult(cl.toString(), "The svn command failed.", stderr.getOutput(), false) : new InfoScmResult(cl.toString(), consumer.getInfoItems());
   }

   protected static Commandline createCommandLine(SvnScmProviderRepository repository, ScmFileSet fileSet, boolean recursive, String revision) {
      Commandline cl = SvnCommandLineUtils.getBaseSvnCommandLine(fileSet.getBasedir(), repository);
      cl.createArg().setValue("info");
      if (recursive) {
         cl.createArg().setValue("--recursive");
      }

      if (StringUtils.isNotEmpty(revision)) {
         cl.createArg().setValue("-r");
         cl.createArg().setValue(revision);
      }

      Iterator it = fileSet.getFileList().iterator();

      while(it.hasNext()) {
         File file = (File)it.next();
         if (repository == null) {
            cl.createArg().setValue(file.getPath());
         } else {
            cl.createArg().setValue(repository.getUrl() + "/" + file.getPath().replace('\\', '/'));
         }
      }

      return cl;
   }
}
