package org.apache.maven.scm.provider.bazaar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.log.DefaultLog;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.bazaar.command.BazaarConsumer;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public final class BazaarUtils {
   private static final Map<String, List<Integer>> EXITCODEMAP = new HashMap();
   private static final List<Integer> DEFAULTEEXITCODES = new ArrayList();

   private BazaarUtils() {
   }

   public static ScmResult execute(BazaarConsumer consumer, ScmLogger logger, File workingDir, String[] cmdAndArgs) throws ScmException {
      try {
         Commandline cmd = buildCmd(workingDir, cmdAndArgs);
         if (logger.isInfoEnabled()) {
            logger.info("EXECUTING: " + cmd);
         }

         int exitCode = executeCmd(consumer, cmd);
         List<Integer> exitCodes = DEFAULTEEXITCODES;
         if (EXITCODEMAP.containsKey(cmdAndArgs[0])) {
            exitCodes = (List)EXITCODEMAP.get(cmdAndArgs[0]);
         }

         boolean success = exitCodes.contains(exitCode);
         String providerMsg = "Execution of bazaar command succeded";
         if (!success) {
            BazaarConfig config = new BazaarConfig(workingDir);
            providerMsg = "\nEXECUTION FAILED\n  Execution of cmd : " + cmdAndArgs[0] + " failed with exit code: " + exitCode + "." + "\n  Working directory was: " + "\n    " + workingDir.getAbsolutePath() + config.toString(workingDir) + "\n";
            if (logger.isErrorEnabled()) {
               logger.error(providerMsg);
            }
         }

         return new ScmResult(cmd.toString(), providerMsg, consumer.getStdErr(), success);
      } catch (ScmException var10) {
         String msg = "EXECUTION FAILED\n  Execution failed before invoking the Bazaar command. Last exception:\n    " + var10.getMessage();
         if (var10.getCause() != null) {
            msg = msg + "\n  Nested exception:\n    " + var10.getCause().getMessage();
         }

         if (logger.isErrorEnabled()) {
            logger.error(msg);
         }

         throw var10;
      }
   }

   static Commandline buildCmd(File workingDir, String[] cmdAndArgs) throws ScmException {
      Commandline cmd = new Commandline();
      cmd.setExecutable("bzr");
      cmd.setWorkingDirectory(workingDir.getAbsolutePath());
      cmd.addArguments(cmdAndArgs);
      if (!workingDir.exists()) {
         boolean success = workingDir.mkdirs();
         if (!success) {
            String msg = "Working directory did not exist and it couldn't be created: " + workingDir;
            throw new ScmException(msg);
         }
      }

      return cmd;
   }

   static int executeCmd(BazaarConsumer consumer, Commandline cmd) throws ScmException {
      try {
         int exitCode = CommandLineUtils.executeCommandLine(cmd, consumer, consumer);
         return exitCode;
      } catch (CommandLineException var4) {
         throw new ScmException("Command could not be executed: " + cmd, var4);
      }
   }

   public static ScmResult execute(File workingDir, String[] cmdAndArgs) throws ScmException {
      ScmLogger logger = new DefaultLog();
      return execute(new BazaarConsumer(logger), logger, workingDir, cmdAndArgs);
   }

   public static String[] expandCommandLine(String[] cmdAndArgs, ScmFileSet additionalFiles) {
      List<File> files = additionalFiles.getFileList();
      String[] cmd = new String[files.size() + cmdAndArgs.length];
      System.arraycopy(cmdAndArgs, 0, cmd, 0, cmdAndArgs.length);

      for(int i = 0; i < files.size(); ++i) {
         String file = ((File)files.get(i)).getPath().replace('\\', File.separatorChar);
         cmd[i + cmdAndArgs.length] = file;
      }

      return cmd;
   }

   public static int getCurrentRevisionNumber(ScmLogger logger, File workingDir) throws ScmException {
      String[] revCmd = new String[]{"revno"};
      BazaarUtils.BazaarRevNoConsumer consumer = new BazaarUtils.BazaarRevNoConsumer(logger);
      execute(consumer, logger, workingDir, revCmd);
      return consumer.getCurrentRevisionNumber();
   }

   static {
      DEFAULTEEXITCODES.add(0);
      List<Integer> diffExitCodes = new ArrayList();
      diffExitCodes.add(0);
      diffExitCodes.add(1);
      diffExitCodes.add(2);
      EXITCODEMAP.put("diff", diffExitCodes);
   }

   private static class BazaarRevNoConsumer extends BazaarConsumer {
      private int revNo;

      BazaarRevNoConsumer(ScmLogger logger) {
         super(logger);
      }

      public void doConsume(ScmFileStatus status, String line) {
         try {
            this.revNo = Integer.valueOf(line);
         } catch (NumberFormatException var4) {
         }

      }

      int getCurrentRevisionNumber() {
         return this.revNo;
      }
   }
}
