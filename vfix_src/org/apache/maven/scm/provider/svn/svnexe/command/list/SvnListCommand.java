package org.apache.maven.scm.provider.svn.svnexe.command.list;

import java.io.File;
import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmRevision;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.list.AbstractListCommand;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.svnexe.command.SvnCommandLineUtils;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnListCommand extends AbstractListCommand implements SvnCommand {
   private static final File TMP_DIR = new File(System.getProperty("java.io.tmpdir"));

   protected ListScmResult executeListCommand(ScmProviderRepository repository, ScmFileSet fileSet, boolean recursive, ScmVersion version) throws ScmException {
      Commandline cl = createCommandLine((SvnScmProviderRepository)repository, fileSet, recursive, version);
      SvnListConsumer consumer = new SvnListConsumer();
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + SvnCommandLineUtils.cryptPassword(cl));
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      int exitCode;
      try {
         exitCode = SvnCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
      } catch (CommandLineException var10) {
         throw new ScmException("Error while executing command.", var10);
      }

      return exitCode != 0 ? new ListScmResult(cl.toString(), "The svn command failed.", stderr.getOutput(), false) : new ListScmResult(cl.toString(), consumer.getFiles());
   }

   static Commandline createCommandLine(SvnScmProviderRepository repository, ScmFileSet fileSet, boolean recursive, ScmVersion version) {
      Commandline cl = SvnCommandLineUtils.getBaseSvnCommandLine(TMP_DIR, repository);
      cl.createArg().setValue("list");
      if (recursive) {
         cl.createArg().setValue("--recursive");
      }

      if (version != null && StringUtils.isNotEmpty(version.getName()) && version instanceof ScmRevision) {
         cl.createArg().setValue("-r");
         cl.createArg().setValue(version.getName());
      }

      Iterator it = fileSet.getFileList().iterator();

      while(it.hasNext()) {
         File file = (File)it.next();
         cl.createArg().setValue(repository.getUrl() + "/" + file.getPath().replace('\\', '/'));
      }

      return cl;
   }
}
