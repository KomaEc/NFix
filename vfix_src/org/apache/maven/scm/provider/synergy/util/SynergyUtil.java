package org.apache.maven.scm.provider.synergy.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.synergy.consumer.SynergyCreateTaskConsumer;
import org.apache.maven.scm.provider.synergy.consumer.SynergyGetCompletedTasksConsumer;
import org.apache.maven.scm.provider.synergy.consumer.SynergyGetTaskObjectsConsumer;
import org.apache.maven.scm.provider.synergy.consumer.SynergyGetWorkingFilesConsumer;
import org.apache.maven.scm.provider.synergy.consumer.SynergyGetWorkingProjectConsumer;
import org.apache.maven.scm.provider.synergy.consumer.SynergyShowDefaultTaskConsumer;
import org.apache.maven.scm.provider.synergy.consumer.SynergyWorkareaConsumer;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public final class SynergyUtil {
   public static final String SEPARATOR = "#####";

   private SynergyUtil() {
   }

   public static String removePrefix(File prefix, File file) throws ScmException {
      try {
         String prefixStr = prefix.getCanonicalPath();
         String fileStr = file.getCanonicalPath();
         if (!fileStr.startsWith(prefixStr)) {
            throw new ScmException(prefixStr + " is not a prefix of " + fileStr);
         } else {
            return fileStr.substring(prefixStr.length());
         }
      } catch (IOException var4) {
         throw new ScmException("IOException", var4);
      }
   }

   public static String getWorkingProject(ScmLogger logger, String projectSpec, String username, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering getWorkingProject method");
      }

      String query = "owner='" + username + "' and status='working' and type='project' and has_predecessor('" + projectSpec + "')";
      Commandline cl = SynergyCCM.query(query, "%objectname", ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      SynergyGetWorkingProjectConsumer stdout = new SynergyGetWorkingProjectConsumer(logger);
      int errorCode = executeSynergyCommand(logger, cl, stderr, stdout, false);
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : getWorkingProject returns " + stdout.getProjectSpec() + " with code " + errorCode);
      }

      return stdout.getProjectSpec();
   }

   public static List<String> getWorkingFiles(ScmLogger logger, String projectSpec, String release, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering getWorkingFiles method");
      }

      String query = "status='working' and release='" + release + "' and is_member_of('" + projectSpec + "')";
      Commandline cl = SynergyCCM.query(query, "%name", ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      SynergyGetWorkingFilesConsumer stdout = new SynergyGetWorkingFilesConsumer(logger);
      int errorCode = executeSynergyCommand(logger, cl, stderr, stdout, false);
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : getWorkingFiles returns " + stdout.getFiles().size() + " files with code " + errorCode);
      }

      return stdout.getFiles();
   }

   public static List<ChangeFile> getModifiedObjects(ScmLogger logger, int numTask, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering getModifiedObjects method");
      }

      Commandline cl = SynergyCCM.showTaskObjects(numTask, "%name#####%version#####", ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      SynergyGetTaskObjectsConsumer stdout = new SynergyGetTaskObjectsConsumer(logger);
      int errorCode = executeSynergyCommand(logger, cl, stderr, stdout, false);
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : getModifiedObjects returns " + stdout.getFiles().size() + " files with code " + errorCode);
      }

      return stdout.getFiles();
   }

   public static List<SynergyTask> getCompletedTasks(ScmLogger logger, String projectSpec, Date startDate, Date endDate, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering getCompletedTasks method");
      }

      SimpleDateFormat toCcmDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", new Locale("en", "US"));
      String query = "is_task_in_folder_of(is_folder_in_rp_of('" + projectSpec + "'))";
      if (startDate != null) {
         query = query + "and completion_date>time('" + toCcmDate.format(startDate) + "')";
      }

      if (endDate != null) {
         query = query + "and completion_date<time('" + toCcmDate.format(endDate) + "')";
      }

      Commandline cl = SynergyCCM.query(query, "%displayname#####%owner#####%completion_date#####%task_synopsis#####", ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      SynergyGetCompletedTasksConsumer stdout = new SynergyGetCompletedTasksConsumer(logger);
      executeSynergyCommand(logger, cl, stderr, stdout, false);
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : getCompletedTasks method returns " + stdout.getTasks().size() + " tasks");
      }

      return stdout.getTasks();
   }

   public static void createBaseline(ScmLogger logger, String projectSpec, String name, String release, String purpose, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering createBaseline method");
      }

      Commandline cl = SynergyCCM.createBaseline(projectSpec, name, release, purpose, ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
   }

   public static void create(ScmLogger logger, File file, String message, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering create method");
      }

      List<File> files = new ArrayList();
      files.add(file);
      Commandline cl = SynergyCCM.create(files, message, ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
   }

   public static int createTask(ScmLogger logger, String synopsis, String release, boolean defaultTask, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering createTask method of SynergyUtil");
      }

      if (synopsis != null && !synopsis.equals("")) {
         Commandline cl = SynergyCCM.createTask(synopsis, release, defaultTask, ccmAddr);
         CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
         SynergyCreateTaskConsumer stdout = new SynergyCreateTaskConsumer(logger);
         executeSynergyCommand(logger, cl, stderr, stdout, true);
         if (logger.isDebugEnabled()) {
            logger.debug("createTask returns " + stdout.getTask());
         }

         return stdout.getTask();
      } else {
         throw new ScmException("A synopsis must be specified to create a task.");
      }
   }

   public static void checkinDefaultTask(ScmLogger logger, String comment, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering checkinDefaultTask method");
      }

      Commandline cl = SynergyCCM.checkinTask("default", comment, ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
   }

   public static void checkinTask(ScmLogger logger, int taskNumber, String comment, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering checkinTask method");
      }

      Commandline cl = SynergyCCM.checkinTask("" + taskNumber, comment, ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
   }

   public static void delete(ScmLogger logger, File file, String ccmAddr, boolean replace) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering delete method");
      }

      List<File> list = new ArrayList();
      list.add(file);
      Commandline cl = SynergyCCM.delete(list, ccmAddr, replace);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
   }

   public static void reconfigure(ScmLogger logger, String projectSpec, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering reconfigure method");
      }

      Commandline cl = SynergyCCM.reconfigure(projectSpec, ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
   }

   public static void reconfigureProperties(ScmLogger logger, String projectSpec, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering reconfigureProperties method");
      }

      Commandline cl = SynergyCCM.reconfigureProperties(projectSpec, ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
   }

   public static void reconcileUwa(ScmLogger logger, String projectSpec, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering reconcileUwa method");
      }

      Commandline cl = SynergyCCM.reconcileUwa(projectSpec, ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
   }

   public static void reconcileUdb(ScmLogger logger, String projectSpec, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering reconcileUdb method");
      }

      Commandline cl = SynergyCCM.reconcileUdb(projectSpec, ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
   }

   public static void checkoutFiles(ScmLogger logger, List<File> files, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering checkoutFiles files method");
      }

      Commandline cl = SynergyCCM.checkoutFiles(files, ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
   }

   public static void checkoutProject(ScmLogger logger, File directory, String projectSpec, ScmVersion version, String purpose, String release, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering checkoutProject project method");
      }

      Commandline cl = SynergyCCM.checkoutProject(directory, projectSpec, version, purpose, release, ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
   }

   public static void checkinProject(ScmLogger logger, String projectSpec, String comment, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering checkinProject project method");
      }

      Commandline cl = SynergyCCM.checkinProject(projectSpec, comment, ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
   }

   public static void checkinFiles(ScmLogger logger, List<File> files, String comment, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering checkinFiles project method");
      }

      Commandline cl = SynergyCCM.checkinFiles(files, comment, ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
   }

   public static int getDefaultTask(ScmLogger logger, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering getDefaultTask method");
      }

      Commandline cl = SynergyCCM.showDefaultTask(ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      SynergyShowDefaultTaskConsumer stdout = new SynergyShowDefaultTaskConsumer(logger);
      int errorCode = executeSynergyCommand(logger, cl, stderr, stdout, false);
      if (logger.isDebugEnabled()) {
         logger.debug("getDefaultTask returns " + stdout.getTask() + " with error code " + errorCode);
      }

      return stdout.getTask();
   }

   public static void setDefaultTask(ScmLogger logger, int task, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering setDefaultTask method");
      }

      Commandline cl = SynergyCCM.setDefaultTask(task, ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
   }

   public static void synchronize(ScmLogger logger, String projectSpec, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering synchronize method");
      }

      Commandline cl = SynergyCCM.synchronize(projectSpec, ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
   }

   public static File getWorkArea(ScmLogger logger, String projectSpec, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering getWorkArea method");
      }

      Commandline cl = SynergyCCM.showWorkArea(projectSpec, ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      SynergyWorkareaConsumer stdout = new SynergyWorkareaConsumer(logger);
      executeSynergyCommand(logger, cl, stderr, stdout, true);
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : getWorkArea returns " + stdout.getWorkAreaPath());
      }

      return stdout.getWorkAreaPath();
   }

   public static void stop(ScmLogger logger, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering stop method");
      }

      Commandline cl = SynergyCCM.stop(ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
   }

   public static String start(ScmLogger logger, String username, String password, SynergyRole role) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering start method");
      }

      if (username == null) {
         throw new ScmException("username can't be null");
      } else if (password == null) {
         throw new ScmException("password can't be null");
      } else {
         Commandline cl = SynergyCCM.start(username, password, role);
         CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
         CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
         int exitCode = executeSynergyCommand(logger, cl, stderr, stdout, false);
         if (logger.isDebugEnabled()) {
            logger.debug("Synergy : start returns with error code " + exitCode);
         }

         if (exitCode != 0) {
            cl = SynergyCCM.startRemote(username, password, role);
            stderr = new CommandLineUtils.StringStreamConsumer();
            stdout = new CommandLineUtils.StringStreamConsumer();
            executeSynergyCommand(logger, cl, stderr, stdout, true);
         }

         return stdout.getOutput();
      }
   }

   public static String delimiter(ScmLogger logger, String ccmAddr) throws ScmException {
      if (logger.isDebugEnabled()) {
         logger.debug("Synergy : Entering delimiter method");
      }

      Commandline cl = SynergyCCM.delimiter(ccmAddr);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      executeSynergyCommand(logger, cl, stderr, stdout, true);
      return stdout.getOutput();
   }

   protected static int executeSynergyCommand(ScmLogger logger, Commandline cl, CommandLineUtils.StringStreamConsumer stderr, StreamConsumer stdout, boolean failOnError) throws ScmException {
      int exitCode;
      try {
         if (logger.isDebugEnabled()) {
            logger.debug("Executing: " + cl.toString());
         }

         exitCode = CommandLineUtils.executeCommandLine(cl, stdout, stderr);
      } catch (CommandLineException var7) {
         throw new ScmException("Error while executing synergy command [" + cl.toString() + "].", var7);
      }

      if (logger.isDebugEnabled()) {
         logger.debug("Exit code :" + exitCode);
      }

      if (stdout instanceof CommandLineUtils.StringStreamConsumer) {
         if (logger.isDebugEnabled()) {
            logger.debug("STDOUT :" + ((CommandLineUtils.StringStreamConsumer)stdout).getOutput());
         }
      } else if (logger.isDebugEnabled()) {
         logger.debug("STDOUT : unavailable");
      }

      if (logger.isDebugEnabled()) {
         logger.debug("STDERR :" + stderr.getOutput());
      }

      if (exitCode != 0 && failOnError) {
         if (stdout instanceof CommandLineUtils.StringStreamConsumer) {
            throw new ScmException("Commandeline = " + cl.toString() + "\nSTDOUT = " + ((CommandLineUtils.StringStreamConsumer)stdout).getOutput() + "\nSTDERR = " + stderr.getOutput() + "\n");
         } else {
            throw new ScmException("Commandeline = " + cl.toString() + "\nSTDOUT = unavailable" + "\nSTDERR = " + stderr.getOutput() + "\n");
         }
      } else {
         return exitCode;
      }
   }
}
