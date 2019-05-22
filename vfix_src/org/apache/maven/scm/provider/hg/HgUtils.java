package org.apache.maven.scm.provider.hg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.log.DefaultLog;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.hg.command.HgConsumer;
import org.apache.maven.scm.provider.hg.command.inventory.HgChangeSet;
import org.apache.maven.scm.provider.hg.command.inventory.HgOutgoingConsumer;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public final class HgUtils {
   private static final Map<String, List<Integer>> EXIT_CODE_MAP = new HashMap();
   private static final List<Integer> DEFAULT_EXIT_CODES = new ArrayList();

   private HgUtils() {
   }

   public static ScmResult execute(HgConsumer consumer, ScmLogger logger, File workingDir, String[] cmdAndArgs) throws ScmException {
      try {
         Commandline cmd = buildCmd(workingDir, cmdAndArgs);
         if (logger.isInfoEnabled()) {
            logger.info("EXECUTING: " + maskPassword(cmd));
         }

         int exitCode = executeCmd(consumer, cmd);
         List<Integer> exitCodes = DEFAULT_EXIT_CODES;
         if (EXIT_CODE_MAP.containsKey(cmdAndArgs[0])) {
            exitCodes = (List)EXIT_CODE_MAP.get(cmdAndArgs[0]);
         }

         boolean success = exitCodes.contains(exitCode);
         String providerMsg = "Execution of hg command succeded";
         if (!success) {
            HgConfig config = new HgConfig(workingDir);
            providerMsg = "\nEXECUTION FAILED\n  Execution of cmd : " + cmdAndArgs[0] + " failed with exit code: " + exitCode + "." + "\n  Working directory was: " + "\n    " + workingDir.getAbsolutePath() + config.toString(workingDir) + "\n";
            if (logger.isErrorEnabled()) {
               logger.error(providerMsg);
            }
         }

         return new ScmResult(cmd.toString(), providerMsg, consumer.getStdErr(), success);
      } catch (ScmException var10) {
         String msg = "EXECUTION FAILED\n  Execution failed before invoking the Hg command. Last exception:\n    " + var10.getMessage();
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
      cmd.setExecutable("hg");
      cmd.addArguments(cmdAndArgs);
      if (workingDir != null) {
         cmd.setWorkingDirectory(workingDir.getAbsolutePath());
         if (!workingDir.exists()) {
            boolean success = workingDir.mkdirs();
            if (!success) {
               String msg = "Working directory did not exist and it couldn't be created: " + workingDir;
               throw new ScmException(msg);
            }
         }
      }

      return cmd;
   }

   static int executeCmd(HgConsumer consumer, Commandline cmd) throws ScmException {
      try {
         int exitCode = CommandLineUtils.executeCommandLine(cmd, consumer, consumer);
         return exitCode;
      } catch (CommandLineException var4) {
         throw new ScmException("Command could not be executed: " + cmd, var4);
      }
   }

   public static ScmResult execute(File workingDir, String[] cmdAndArgs) throws ScmException {
      ScmLogger logger = new DefaultLog();
      return execute(new HgConsumer(logger), logger, workingDir, cmdAndArgs);
   }

   public static String[] expandCommandLine(String[] cmdAndArgs, ScmFileSet additionalFiles) {
      List<File> filesList = additionalFiles.getFileList();
      String[] cmd = new String[filesList.size() + cmdAndArgs.length];
      System.arraycopy(cmdAndArgs, 0, cmd, 0, cmdAndArgs.length);
      int i = 0;

      for(Iterator i$ = filesList.iterator(); i$.hasNext(); ++i) {
         File scmFile = (File)i$.next();
         String file = scmFile.getPath().replace('\\', File.separatorChar);
         cmd[i + cmdAndArgs.length] = file;
      }

      return cmd;
   }

   public static int getCurrentRevisionNumber(ScmLogger logger, File workingDir) throws ScmException {
      String[] revCmd = new String[]{"id"};
      HgUtils.HgRevNoConsumer consumer = new HgUtils.HgRevNoConsumer(logger);
      execute(consumer, logger, workingDir, revCmd);
      return consumer.getCurrentRevisionNumber();
   }

   public static String getCurrentBranchName(ScmLogger logger, File workingDir) throws ScmException {
      String[] branchnameCmd = new String[]{"branch"};
      HgUtils.HgBranchnameConsumer consumer = new HgUtils.HgBranchnameConsumer(logger);
      execute(consumer, logger, workingDir, branchnameCmd);
      return consumer.getBranchName();
   }

   public static boolean differentOutgoingBranchFound(ScmLogger logger, File workingDir, String workingbranchName) throws ScmException {
      String[] outCmd = new String[]{"outgoing"};
      HgOutgoingConsumer outConsumer = new HgOutgoingConsumer(logger);
      ScmResult outResult = execute(outConsumer, logger, workingDir, outCmd);
      List<HgChangeSet> changes = outConsumer.getChanges();
      if (outResult.isSuccess()) {
         Iterator i$ = changes.iterator();

         while(i$.hasNext()) {
            HgChangeSet set = (HgChangeSet)i$.next();
            if (set.getBranch() != null) {
               logger.warn("A different branch than " + workingbranchName + " was found in outgoing changes, branch name was " + set.getBranch() + ". Only local branch named " + workingbranchName + " will be pushed.");
               return true;
            }
         }
      }

      return false;
   }

   public static String maskPassword(Commandline cl) {
      String clString = cl.toString();
      int pos = clString.indexOf(64);
      if (pos > 0) {
         clString = clString.replaceAll(":\\w+@", ":*****@");
      }

      return clString;
   }

   static {
      DEFAULT_EXIT_CODES.add(0);
      List<Integer> diffExitCodes = new ArrayList(3);
      diffExitCodes.add(0);
      diffExitCodes.add(1);
      diffExitCodes.add(2);
      EXIT_CODE_MAP.put("diff", diffExitCodes);
      List<Integer> outgoingExitCodes = new ArrayList(2);
      outgoingExitCodes.add(0);
      outgoingExitCodes.add(1);
      EXIT_CODE_MAP.put("outgoing", outgoingExitCodes);
   }

   private static class HgBranchnameConsumer extends HgConsumer {
      private String branchName;

      HgBranchnameConsumer(ScmLogger logger) {
         super(logger);
      }

      public void doConsume(ScmFileStatus status, String trimmedLine) {
         this.branchName = String.valueOf(trimmedLine);
      }

      String getBranchName() {
         return this.branchName;
      }
   }

   private static class HgRevNoConsumer extends HgConsumer {
      private int revNo;

      HgRevNoConsumer(ScmLogger logger) {
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
