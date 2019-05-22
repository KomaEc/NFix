package org.apache.maven.scm.provider.starteam.command;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.starteam.repository.StarteamScmProviderRepository;
import org.apache.maven.scm.provider.starteam.util.StarteamUtil;
import org.apache.maven.scm.providers.starteam.settings.Settings;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public final class StarteamCommandLineUtils {
   private static Settings settings = StarteamUtil.getSettings();

   private StarteamCommandLineUtils() {
   }

   public static Commandline createStarteamBaseCommandLine(String action, StarteamScmProviderRepository repo) {
      Commandline cl = new Commandline();
      cl.createArg().setValue("stcmd");
      cl.createArg().setValue(action);
      cl.createArg().setValue("-x");
      cl.createArg().setValue("-nologo");
      cl.createArg().setValue("-stop");
      return cl;
   }

   private static Commandline addCommandlineArguments(Commandline cl, List<String> args) {
      if (args == null) {
         return cl;
      } else {
         Iterator i$ = args.iterator();

         while(i$.hasNext()) {
            String arg = (String)i$.next();
            cl.createArg().setValue(arg);
         }

         return cl;
      }
   }

   public static Commandline createStarteamCommandLine(String action, List<String> args, ScmFileSet scmFileSet, StarteamScmProviderRepository repo) {
      Commandline cl = createStarteamBaseCommandLine(action, repo);
      if (scmFileSet.getFileList().size() == 0) {
         cl.createArg().setValue("-p");
         cl.createArg().setValue(repo.getFullUrl());
         cl.createArg().setValue("-fp");
         cl.createArg().setValue(scmFileSet.getBasedir().getAbsolutePath().replace('\\', '/'));
         cl.createArg().setValue("-is");
         addCompressionOption(cl);
         addCommandlineArguments(cl, args);
         return cl;
      } else {
         File fileInFileSet = (File)scmFileSet.getFileList().get(0);
         File subFile = new File(scmFileSet.getBasedir(), fileInFileSet.getPath());
         File workingDirectory = subFile;
         String scmUrl = repo.getFullUrl() + "/" + fileInFileSet.getPath().replace('\\', '/');
         if (!subFile.isDirectory()) {
            workingDirectory = subFile.getParentFile();
            if (fileInFileSet.getParent() != null) {
               scmUrl = repo.getFullUrl() + "/" + fileInFileSet.getParent().replace('\\', '/');
            } else {
               scmUrl = repo.getFullUrl();
            }
         }

         cl.createArg().setValue("-p");
         cl.createArg().setValue(scmUrl);
         cl.createArg().setValue("-fp");
         cl.createArg().setValue(workingDirectory.getPath().replace('\\', '/'));
         cl.setWorkingDirectory(workingDirectory.getPath());
         if (subFile.isDirectory()) {
            cl.createArg().setValue("-is");
         }

         addCompressionOption(cl);
         addCommandlineArguments(cl, args);
         if (!subFile.isDirectory()) {
            cl.createArg().setValue(subFile.getName());
         }

         return cl;
      }
   }

   public static void addCompressionOption(Commandline cl) {
      if (settings.isCompressionEnable()) {
         cl.createArg().setValue("-cmp");
      }

   }

   public static void addEOLOption(List<String> args) {
      if (settings.getEol() != null) {
         args.add("-eol");
         args.add(settings.getEol());
      }

   }

   public static String toJavaPath(String path) {
      return path.replace('\\', '/');
   }

   public static String displayCommandlineWithoutPassword(Commandline cl) throws ScmException {
      String retStr = "";
      String fullStr = cl.toString();
      int usernamePos = fullStr.indexOf("-p ") + 3;
      if (usernamePos == 2) {
         throw new ScmException("Invalid command line");
      } else {
         retStr = fullStr.substring(0, usernamePos);
         int passwordStartPos = fullStr.indexOf(58);
         if (passwordStartPos == -1) {
            throw new ScmException("Invalid command line");
         } else {
            int passwordEndPos = fullStr.indexOf(64);
            if (passwordEndPos == -1) {
               throw new ScmException("Invalid command line");
            } else {
               retStr = retStr + fullStr.substring(usernamePos, passwordStartPos);
               retStr = retStr + fullStr.substring(passwordEndPos);
               return retStr;
            }
         }
      }
   }

   public static int executeCommandline(Commandline cl, StreamConsumer consumer, CommandLineUtils.StringStreamConsumer stderr, ScmLogger logger) throws ScmException {
      if (logger.isInfoEnabled()) {
         logger.info("Command line: " + displayCommandlineWithoutPassword(cl));
      }

      try {
         return CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var5) {
         throw new ScmException("Error while executing command.", var5);
      }
   }

   public static String getRelativeChildDirectory(String parent, String child) {
      try {
         String childPath = (new File(child)).getCanonicalFile().getPath().replace('\\', '/');
         String parentPath = (new File(parent)).getCanonicalFile().getPath().replace('\\', '/');
         if (!childPath.startsWith(parentPath)) {
            throw new IllegalStateException();
         } else {
            String retDir = "." + childPath.substring(parentPath.length());
            return retDir;
         }
      } catch (IOException var5) {
         throw new IllegalStateException("Unable to convert to canonical path of either " + parent + " or " + child);
      }
   }
}
