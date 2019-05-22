package org.apache.maven.scm.provider.svn.svnexe.command.remoteinfo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.remoteinfo.AbstractRemoteInfoCommand;
import org.apache.maven.scm.command.remoteinfo.RemoteInfoScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.svnexe.command.SvnCommandLineUtils;
import org.apache.maven.scm.util.AbstractConsumer;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnRemoteInfoCommand extends AbstractRemoteInfoCommand implements SvnCommand {
   public RemoteInfoScmResult executeRemoteInfoCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      String url = ((SvnScmProviderRepository)repository).getUrl();
      String baseUrl = StringUtils.endsWith(url, "/") ? StringUtils.substringAfter(StringUtils.removeEnd(url, "/"), "/") : StringUtils.substringBeforeLast(url, "/");
      Commandline cl = SvnCommandLineUtils.getBaseSvnCommandLine(fileSet == null ? null : fileSet.getBasedir(), (SvnScmProviderRepository)repository);
      cl.createArg().setValue("ls");
      cl.createArg().setValue(baseUrl + "/tags");
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      SvnRemoteInfoCommand.LsConsumer consumer = new SvnRemoteInfoCommand.LsConsumer(this.getLogger(), baseUrl);
      int exitCode = false;
      Map tagsInfos = null;

      int exitCode;
      try {
         exitCode = SvnCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
         tagsInfos = consumer.infos;
      } catch (CommandLineException var14) {
         throw new ScmException("Error while executing svn command.", var14);
      }

      if (exitCode != 0) {
         return new RemoteInfoScmResult(cl.toString(), "The svn command failed.", stderr.getOutput(), false);
      } else {
         cl = SvnCommandLineUtils.getBaseSvnCommandLine(fileSet == null ? null : fileSet.getBasedir(), (SvnScmProviderRepository)repository);
         cl.createArg().setValue("ls");
         cl.createArg().setValue(baseUrl + "/tags");
         stderr = new CommandLineUtils.StringStreamConsumer();
         consumer = new SvnRemoteInfoCommand.LsConsumer(this.getLogger(), baseUrl);
         Map branchesInfos = null;

         try {
            exitCode = SvnCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
            branchesInfos = consumer.infos;
         } catch (CommandLineException var13) {
            throw new ScmException("Error while executing svn command.", var13);
         }

         return exitCode != 0 ? new RemoteInfoScmResult(cl.toString(), "The svn command failed.", stderr.getOutput(), false) : new RemoteInfoScmResult(cl.toString(), branchesInfos, tagsInfos);
      }
   }

   public boolean remoteUrlExist(ScmProviderRepository repository, CommandParameters parameters) throws ScmException {
      String url = ((SvnScmProviderRepository)repository).getUrl();
      Commandline cl = SvnCommandLineUtils.getBaseSvnCommandLine((File)null, (SvnScmProviderRepository)repository);
      cl.createArg().setValue("ls");
      cl.createArg().setValue(url);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      SvnRemoteInfoCommand.LsConsumer consumer = new SvnRemoteInfoCommand.LsConsumer(this.getLogger(), url);
      boolean var7 = false;

      int exitCode;
      try {
         exitCode = SvnCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
      } catch (CommandLineException var9) {
         throw new ScmException("Error while executing svn command.", var9);
      }

      if (exitCode != 0) {
         String output = stderr.getOutput();
         if (output.indexOf("W160013") < 0 && output.indexOf("svn: URL") < 0) {
            throw new ScmException(cl.toString() + ".The svn command failed:" + stderr.getOutput());
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   private static class LsConsumer extends AbstractConsumer {
      Map<String, String> infos = new HashMap();
      String url;

      LsConsumer(ScmLogger logger, String url) {
         super(logger);
         this.url = url;
      }

      public void consumeLine(String s) {
         this.infos.put(StringUtils.removeEnd(s, "/"), this.url + "/" + s);
      }

      Map<String, String> getInfos() {
         return this.infos;
      }
   }
}
