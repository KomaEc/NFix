package org.apache.maven.scm.provider.cvslib.command;

import java.io.File;
import java.util.Enumeration;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.util.CvsUtil;
import org.apache.maven.scm.providers.cvslib.settings.Settings;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsCommandUtils {
   private CvsCommandUtils() {
   }

   public static boolean isCvsNT() throws ScmException {
      Commandline cl = new Commandline();
      cl.setExecutable("cvs");
      cl.createArg().setValue("-v");
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      try {
         CommandLineUtils.executeCommandLine(cl, stdout, stderr);
      } catch (CommandLineException var4) {
         throw new ScmException("Error while executing command.", var4);
      }

      return stdout.getOutput().indexOf("CVSNT") >= 0;
   }

   public static Commandline getBaseCommand(String commandName, CvsScmProviderRepository repo, ScmFileSet fileSet) {
      return getBaseCommand(commandName, repo, fileSet, (String)null, true);
   }

   public static Commandline getBaseCommand(String commandName, CvsScmProviderRepository repo, ScmFileSet fileSet, boolean addCvsRoot) {
      return getBaseCommand(commandName, repo, fileSet, (String)null, addCvsRoot);
   }

   public static Commandline getBaseCommand(String commandName, CvsScmProviderRepository repo, ScmFileSet fileSet, String options) {
      return getBaseCommand(commandName, repo, fileSet, options, true);
   }

   public static Commandline getBaseCommand(String commandName, CvsScmProviderRepository repo, ScmFileSet fileSet, String options, boolean addCvsRoot) {
      Settings settings = CvsUtil.getSettings();
      Commandline cl = new Commandline();
      cl.setExecutable("cvs");
      cl.setWorkingDirectory(fileSet.getBasedir().getAbsolutePath());
      if (Boolean.getBoolean("maven.scm.cvs.use_compression")) {
         cl.createArg().setValue("-z" + System.getProperty("maven.scm.cvs.compression_level", "3"));
      } else if (settings.getCompressionLevel() > 0) {
         cl.createArg().setValue("-z" + settings.getCompressionLevel());
      }

      if (!settings.isUseCvsrc()) {
         cl.createArg().setValue("-f");
      }

      if (settings.isTraceCvsCommand()) {
         cl.createArg().setValue("-t");
      }

      if (!StringUtils.isEmpty(settings.getTemporaryFilesDirectory())) {
         File tempDir = new File(settings.getTemporaryFilesDirectory());
         if (!tempDir.exists()) {
            tempDir.mkdirs();
         }

         cl.createArg().setValue("-T");
         cl.createArg().setValue(tempDir.getAbsolutePath());
      }

      if (settings.getCvsVariables().size() > 0) {
         Enumeration e = settings.getCvsVariables().propertyNames();

         while(e.hasMoreElements()) {
            String key = (String)e.nextElement();
            String value = settings.getCvsVariables().getProperty(key);
            cl.createArg().setValue("-s");
            cl.createArg().setValue(key + "=" + value);
         }
      }

      if (addCvsRoot) {
         cl.createArg().setValue("-d");
         cl.createArg().setValue(repo.getCvsRoot());
      }

      cl.createArg().setLine(options);
      cl.createArg().setValue("-q");
      cl.createArg().setValue(commandName);
      return cl;
   }
}
