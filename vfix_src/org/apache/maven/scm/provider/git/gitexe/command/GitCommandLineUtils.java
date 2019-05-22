package org.apache.maven.scm.provider.git.gitexe.command;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public final class GitCommandLineUtils {
   private GitCommandLineUtils() {
   }

   public static void addTarget(Commandline cl, List<File> files) {
      if (files != null && !files.isEmpty()) {
         File workingDirectory = cl.getWorkingDirectory();

         try {
            String canonicalWorkingDirectory = workingDirectory.getCanonicalPath();

            String relativeFile;
            for(Iterator i$ = files.iterator(); i$.hasNext(); cl.createArg().setValue(relativeFile)) {
               File file = (File)i$.next();
               relativeFile = file.getPath();
               String canonicalFile = file.getCanonicalPath();
               if (canonicalFile.startsWith(canonicalWorkingDirectory)) {
                  relativeFile = canonicalFile.substring(canonicalWorkingDirectory.length());
                  if (relativeFile.startsWith(File.separator)) {
                     relativeFile = relativeFile.substring(File.separator.length());
                  }
               }
            }

         } catch (IOException var8) {
            throw new IllegalArgumentException("Could not get canonical paths for workingDirectory = " + workingDirectory + " or files=" + files, var8);
         }
      }
   }

   public static Commandline getBaseGitCommandLine(File workingDirectory, String command) {
      return getAnonymousBaseGitCommandLine(workingDirectory, command);
   }

   private static Commandline getAnonymousBaseGitCommandLine(File workingDirectory, String command) {
      if (command != null && command.length() != 0) {
         Commandline cl = new AnonymousCommandLine();
         composeCommand(workingDirectory, command, cl);
         return cl;
      } else {
         return null;
      }
   }

   private static void composeCommand(File workingDirectory, String command, Commandline cl) {
      cl.setExecutable("git");
      cl.createArg().setValue(command);
      if (workingDirectory != null) {
         cl.setWorkingDirectory(workingDirectory.getAbsolutePath());
      }

   }

   public static int execute(Commandline cl, StreamConsumer consumer, CommandLineUtils.StringStreamConsumer stderr, ScmLogger logger) throws ScmException {
      if (logger.isInfoEnabled()) {
         logger.info("Executing: " + cl);
         logger.info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      try {
         int exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
         return exitCode;
      } catch (CommandLineException var6) {
         throw new ScmException("Error while executing command.", var6);
      }
   }

   public static int execute(Commandline cl, CommandLineUtils.StringStreamConsumer stdout, CommandLineUtils.StringStreamConsumer stderr, ScmLogger logger) throws ScmException {
      if (logger.isInfoEnabled()) {
         logger.info("Executing: " + cl);
         logger.info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      try {
         int exitCode = CommandLineUtils.executeCommandLine(cl, stdout, stderr);
         return exitCode;
      } catch (CommandLineException var6) {
         throw new ScmException("Error while executing command.", var6);
      }
   }
}
