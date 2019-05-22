package org.apache.maven.scm.provider.vss.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.vss.repository.VssScmProviderRepository;
import org.apache.maven.scm.providers.vss.settings.Settings;
import org.apache.maven.scm.providers.vss.settings.io.xpp3.VssXpp3Reader;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public final class VssCommandLineUtils {
   private static File scmConfDir = new File(System.getProperty("user.home"), ".scm");
   private static Settings settings;

   private VssCommandLineUtils() {
   }

   public static void addFiles(Commandline cl, ScmFileSet fileSet) {
      Iterator it = fileSet.getFileList().iterator();

      while(it.hasNext()) {
         File file = (File)it.next();
         cl.createArg().setValue(file.getPath().replace('\\', '/'));
      }

   }

   public static Commandline getBaseVssCommandLine(File workingDirectory, String cmd, VssScmProviderRepository repository) {
      Commandline cl = new Commandline();
      cl.setExecutable("ss");
      cl.setWorkingDirectory(workingDirectory.getAbsolutePath());
      if (!StringUtils.isEmpty(repository.getUser())) {
         cl.createArg().setValue("-Y");
         StringBuilder sb = new StringBuilder(repository.getUser());
         if (!StringUtils.isEmpty(repository.getPassword())) {
            sb.append(",").append(repository.getPassword());
         }

         cl.createArg().setValue(sb.toString());
      }

      return cl;
   }

   public static int executeCommandline(Commandline cl, StreamConsumer consumer, CommandLineUtils.StringStreamConsumer stderr, ScmLogger logger) throws ScmException {
      try {
         if (logger.isInfoEnabled()) {
            logger.info("Executing: " + cl);
            logger.info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
         }

         int exitcode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
         if (logger.isDebugEnabled()) {
            logger.debug("VSS Command Exit_Code: " + exitcode);
         }

         return exitcode;
      } catch (CommandLineException var5) {
         throw new ScmException("Error while executing command.", var5);
      }
   }

   public static Settings getSettings() {
      if (settings == null) {
         settings = readSettings();
      }

      return settings;
   }

   public static Settings readSettings() {
      Settings settings = null;
      File settingsFile = getScmConfFile();
      if (settingsFile.exists()) {
         VssXpp3Reader reader = new VssXpp3Reader();

         try {
            settings = reader.read((Reader)ReaderFactory.newXmlReader(settingsFile));
         } catch (FileNotFoundException var5) {
         } catch (IOException var6) {
         } catch (XmlPullParserException var7) {
            String message = settingsFile.getAbsolutePath() + " isn't well formed. SKIPPED." + var7.getMessage();
            System.err.println(message);
         }
      }

      String vssDirectory = System.getProperty("vssDirectory");
      if (StringUtils.isNotEmpty(vssDirectory)) {
         if (settings == null) {
            settings = new Settings();
         }

         settings.setVssDirectory(vssDirectory);
      }

      return settings;
   }

   protected static File getScmConfDir() {
      return scmConfDir;
   }

   protected static void setScmConfDir(File directory) {
      scmConfDir = directory;
      settings = readSettings();
   }

   public static String getSsDir() {
      String ssDir = "";
      if (getSettings() != null) {
         String ssDir2 = getSettings().getVssDirectory();
         if (ssDir2 != null) {
            ssDir = StringUtils.replace(ssDir2, "\\", "/");
            if (!ssDir.endsWith("/")) {
               ssDir = ssDir + "/";
            }
         }
      }

      return ssDir;
   }

   public static File getScmConfFile() {
      return new File(scmConfDir, "vss-settings.xml");
   }
}
