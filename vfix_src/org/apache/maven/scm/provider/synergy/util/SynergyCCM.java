package org.apache.maven.scm.provider.synergy.util;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmVersion;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class SynergyCCM {
   private static final String CCM = "ccm";
   private static final String BASELINE = "baseline";
   private static final String CI = "ci";
   private static final String CO = "co";
   private static final String CREATE = "create";
   private static final String DELETE = "delete";
   private static final String DELIMITER = "delimiter";
   private static final String DIR = "dir";
   private static final String QUERY = "query";
   private static final String RECONCILE = "rwa";
   private static final String RECONFIGURE = "reconfigure";
   private static final String RECONFIGURE_PROPERTIES = "reconfigure_properties";
   private static final String START = "start";
   private static final String STOP = "stop";
   private static final String SYNC = "sync";
   private static final String TASK = "task";
   private static final String WA = "wa";

   public static Commandline showTaskObjects(int taskNumber, String format, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("task");
      cl.createArg().setValue("-show");
      cl.createArg().setValue("objects");
      if (format != null && !format.equals("")) {
         cl.createArg().setValue("-f");
         cl.createArg().setValue(format);
      }

      cl.createArg().setValue(Integer.toString(taskNumber));
      return cl;
   }

   public static Commandline query(String query, String format, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("query");
      cl.createArg().setValue("-u");
      if (format != null && !format.equals("")) {
         cl.createArg().setValue("-f");
         cl.createArg().setValue(format);
      }

      cl.createArg().setValue(query);
      return cl;
   }

   public static Commandline createBaseline(String projectSpec, String name, String release, String purpose, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("baseline");
      cl.createArg().setValue("-create");
      cl.createArg().setValue(name);
      cl.createArg().setValue("-p");
      cl.createArg().setValue(projectSpec);
      cl.createArg().setValue("-release");
      cl.createArg().setValue(release);
      cl.createArg().setValue("-purpose");
      cl.createArg().setValue(purpose);
      return cl;
   }

   public static Commandline create(List<File> files, String message, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("create");
      if (message != null && !message.equals("")) {
         cl.createArg().setValue("-c");
         cl.createArg().setValue(message);
      }

      Iterator i$ = files.iterator();

      while(i$.hasNext()) {
         File f = (File)i$.next();

         try {
            cl.createArg().setValue(f.getCanonicalPath());
         } catch (IOException var7) {
            throw new ScmException("Invalid file path " + f.toString(), var7);
         }
      }

      return cl;
   }

   public static Commandline createTask(String synopsis, String release, boolean defaultTask, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("task");
      cl.createArg().setValue("-create");
      cl.createArg().setValue("-synopsis");
      cl.createArg().setValue(synopsis);
      if (release != null && !release.equals("")) {
         cl.createArg().setValue("-release");
         cl.createArg().setValue(release);
      }

      if (defaultTask) {
         cl.createArg().setValue("-default");
      }

      cl.createArg().setValue("-description");
      cl.createArg().setValue("This task was created by Maven SCM Synergy provider on " + Calendar.getInstance().getTime());
      return cl;
   }

   public static Commandline checkinTask(String taskSpecs, String comment, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("task");
      cl.createArg().setValue("-checkin");
      cl.createArg().setValue(taskSpecs);
      cl.createArg().setValue("-comment");
      cl.createArg().setValue(comment);
      return cl;
   }

   public static Commandline delete(List<File> files, String ccmAddr, boolean replace) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("delete");
      if (replace) {
         cl.createArg().setValue("-replace");
      }

      Iterator i$ = files.iterator();

      while(i$.hasNext()) {
         File f = (File)i$.next();

         try {
            cl.createArg().setValue(f.getCanonicalPath());
         } catch (IOException var7) {
            throw new ScmException("Invalid file path " + f.toString(), var7);
         }
      }

      return cl;
   }

   public static Commandline reconfigure(String projectSpec, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("reconfigure");
      cl.createArg().setValue("-recurse");
      if (projectSpec != null) {
         cl.createArg().setValue("-p");
         cl.createArg().setValue(projectSpec);
      }

      return cl;
   }

   public static Commandline reconfigureProperties(String projectSpec, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("reconfigure_properties");
      cl.createArg().setValue("-refresh");
      cl.createArg().setValue(projectSpec);
      return cl;
   }

   public static Commandline reconcileUwa(String projectSpec, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("rwa");
      cl.createArg().setValue("-r");
      cl.createArg().setValue("-uwa");
      if (projectSpec != null) {
         cl.createArg().setValue("-p");
         cl.createArg().setValue(projectSpec);
      }

      return cl;
   }

   public static Commandline reconcileUdb(String projectSpec, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("rwa");
      cl.createArg().setValue("-r");
      cl.createArg().setValue("-udb");
      if (projectSpec != null) {
         cl.createArg().setValue("-p");
         cl.createArg().setValue(projectSpec);
      }

      return cl;
   }

   public static Commandline dir(File directory, String format, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);

      try {
         cl.setWorkingDirectory(directory.getCanonicalPath());
      } catch (IOException var5) {
         throw new ScmException("Invalid directory", var5);
      }

      cl.setExecutable("ccm");
      cl.createArg().setValue("dir");
      cl.createArg().setValue("-m");
      if (format != null && !format.equals("")) {
         cl.createArg().setValue("-f");
         cl.createArg().setValue(format);
      }

      return cl;
   }

   public static Commandline checkoutFiles(List<File> files, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("co");
      Iterator i$ = files.iterator();

      while(i$.hasNext()) {
         File f = (File)i$.next();

         try {
            cl.createArg().setValue(f.getCanonicalPath());
         } catch (IOException var6) {
            throw new ScmException("Invalid file path " + f.toString(), var6);
         }
      }

      return cl;
   }

   public static Commandline checkoutProject(File directory, String projectSpec, ScmVersion version, String purpose, String release, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("co");
      cl.createArg().setValue("-subprojects");
      cl.createArg().setValue("-rel");
      if (version != null && StringUtils.isNotEmpty(version.getName())) {
         cl.createArg().setValue("-t");
         cl.createArg().setValue(version.getName());
      }

      if (purpose != null && !purpose.equals("")) {
         cl.createArg().setValue("-purpose");
         cl.createArg().setValue(purpose);
      }

      if (release != null && !release.equals("")) {
         cl.createArg().setValue("-release");
         cl.createArg().setValue(release);
      }

      if (directory != null) {
         cl.createArg().setValue("-path");

         try {
            cl.createArg().setValue(directory.getCanonicalPath());
         } catch (IOException var8) {
            throw new ScmException("Invalid directory", var8);
         }
      }

      cl.createArg().setValue("-p");
      cl.createArg().setValue(projectSpec);
      return cl;
   }

   public static Commandline checkinProject(String projectSpec, String comment, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("ci");
      if (comment != null && !comment.equals("")) {
         cl.createArg().setValue("-c");
         cl.createArg().setValue(comment);
      }

      cl.createArg().setValue("-p");
      cl.createArg().setValue(projectSpec);
      return cl;
   }

   public static Commandline checkinFiles(List<File> files, String comment, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("ci");
      if (comment != null && !comment.equals("")) {
         cl.createArg().setValue("-c");
         cl.createArg().setValue(comment);
      }

      if (files.size() > 0) {
         Iterator i$ = files.iterator();

         while(i$.hasNext()) {
            File f = (File)i$.next();

            try {
               cl.createArg().setValue(f.getCanonicalPath());
            } catch (IOException var7) {
               throw new ScmException("Invalid file path " + f.toString(), var7);
            }
         }
      }

      return cl;
   }

   public static Commandline synchronize(String projectSpec, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("sync");
      cl.createArg().setValue("-r");
      cl.createArg().setValue("-p");
      cl.createArg().setValue(projectSpec);
      return cl;
   }

   public static Commandline showWorkArea(String projectSpec, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("wa");
      cl.createArg().setValue("-show");
      cl.createArg().setValue(projectSpec);
      return cl;
   }

   public static Commandline stop(String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("stop");
      return cl;
   }

   private static void configureEnvironment(Commandline cl, String ccmAddr) throws ScmException {
      try {
         Properties envVars = CommandLineUtils.getSystemEnvVars();
         Iterator i = envVars.keySet().iterator();

         while(i.hasNext()) {
            String key = (String)i.next();
            if (!key.equalsIgnoreCase("CCM_ADDR")) {
               cl.addEnvironment(key, envVars.getProperty(key));
            }
         }
      } catch (Exception var5) {
         throw new ScmException("Fail to add PATH environment variable.", var5);
      }

      cl.addEnvironment("CCM_ADDR", ccmAddr);
   }

   public static Commandline start(String username, String password, SynergyRole role) throws ScmException {
      Commandline cl = new Commandline();
      cl.setExecutable("ccm");
      cl.createArg().setValue("start");
      cl.createArg().setValue("-nogui");
      cl.createArg().setValue("-m");
      cl.createArg().setValue("-q");
      cl.createArg().setValue("-n");
      cl.createArg().setValue(username);
      cl.createArg().setValue("-pw");
      cl.createArg().setValue(password);
      if (role != null) {
         cl.createArg().setValue("-r");
         cl.createArg().setValue(role.toString());
      }

      return cl;
   }

   public static Commandline startRemote(String username, String password, SynergyRole role) throws ScmException {
      Commandline cl = new Commandline();
      cl.setExecutable("ccm");
      cl.createArg().setValue("start");
      cl.createArg().setValue("-nogui");
      cl.createArg().setValue("-m");
      cl.createArg().setValue("-q");
      cl.createArg().setValue("-rc");
      cl.createArg().setValue("-n");
      cl.createArg().setValue(username);
      cl.createArg().setValue("-pw");
      cl.createArg().setValue(password);
      if (role != null) {
         cl.createArg().setValue("-r");
         cl.createArg().setValue(role.toString());
      }

      return cl;
   }

   public static Commandline delimiter(String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("delimiter");
      return cl;
   }

   public static Commandline showDefaultTask(String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("task");
      cl.createArg().setValue("-default");
      return cl;
   }

   public static Commandline setDefaultTask(int task, String ccmAddr) throws ScmException {
      Commandline cl = new Commandline();
      configureEnvironment(cl, ccmAddr);
      cl.setExecutable("ccm");
      cl.createArg().setValue("task");
      cl.createArg().setValue("-default");
      cl.createArg().setValue(String.valueOf(task));
      return cl;
   }
}
