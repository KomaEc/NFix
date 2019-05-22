package org.apache.maven.scm.provider.svn.svnexe.command.mkdir;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.mkdir.AbstractMkdirCommand;
import org.apache.maven.scm.command.mkdir.MkdirScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.svnexe.command.SvnCommandLineUtils;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnMkdirCommand extends AbstractMkdirCommand implements SvnCommand {
   protected MkdirScmResult executeMkdirCommand(ScmProviderRepository repository, ScmFileSet fileSet, String message, boolean createInLocal) throws ScmException {
      File messageFile = FileUtils.createTempFile("maven-scm-", ".commit", (File)null);

      try {
         FileUtils.fileWrite(messageFile.getAbsolutePath(), message);
      } catch (IOException var21) {
         return new MkdirScmResult((String)null, "Error while making a temporary file for the mkdir message: " + var21.getMessage(), (String)null, false);
      }

      Commandline cl = createCommandLine((SvnScmProviderRepository)repository, fileSet, messageFile, createInLocal);
      SvnMkdirConsumer consumer = new SvnMkdirConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + SvnCommandLineUtils.cryptPassword(cl));
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      int exitCode;
      try {
         exitCode = SvnCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
      } catch (CommandLineException var19) {
         throw new ScmException("Error while executing command.", var19);
      } finally {
         try {
            FileUtils.forceDelete(messageFile);
         } catch (IOException var18) {
         }

      }

      if (exitCode != 0) {
         return new MkdirScmResult(cl.toString(), "The svn command failed.", stderr.getOutput(), false);
      } else {
         return createInLocal ? new MkdirScmResult(cl.toString(), consumer.getCreatedDirs()) : new MkdirScmResult(cl.toString(), Integer.toString(consumer.getRevision()));
      }
   }

   protected static Commandline createCommandLine(SvnScmProviderRepository repository, ScmFileSet fileSet, File messageFile, boolean createInLocal) {
      if (!fileSet.getBasedir().exists() && !createInLocal) {
         fileSet.getBasedir().mkdirs();
      }

      Commandline cl = SvnCommandLineUtils.getBaseSvnCommandLine(fileSet.getBasedir(), repository);
      cl.createArg().setValue("mkdir");
      Iterator<File> it = fileSet.getFileList().iterator();
      String dirPath = ((File)it.next()).getPath();
      if (dirPath != null && Os.isFamily("dos")) {
         dirPath = StringUtils.replace(dirPath, "\\", "/");
      }

      if (!createInLocal) {
         cl.createArg().setValue(repository.getUrl() + "/" + dirPath);
         if (messageFile != null) {
            cl.createArg().setValue("--file");
            cl.createArg().setValue(messageFile.getAbsolutePath());
         }
      } else {
         cl.createArg().setValue(dirPath);
      }

      return cl;
   }
}
