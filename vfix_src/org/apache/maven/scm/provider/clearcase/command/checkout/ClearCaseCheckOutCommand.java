package org.apache.maven.scm.provider.clearcase.command.checkout;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkout.AbstractCheckOutCommand;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.clearcase.command.ClearCaseCommand;
import org.apache.maven.scm.provider.clearcase.repository.ClearCaseScmProviderRepository;
import org.apache.maven.scm.providers.clearcase.settings.Settings;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class ClearCaseCheckOutCommand extends AbstractCheckOutCommand implements ClearCaseCommand {
   private Settings settings = null;

   protected CheckOutScmResult executeCheckOutCommand(ScmProviderRepository repository, ScmFileSet fileSet, ScmVersion version, boolean recursive) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing checkout command...");
      }

      ClearCaseScmProviderRepository repo = (ClearCaseScmProviderRepository)repository;
      File workingDirectory = fileSet.getBasedir();
      if (version != null && this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(version.getType() + ": " + version.getName());
      }

      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("Running with CLEARCASE " + this.settings.getClearcaseType());
      }

      ClearCaseCheckOutConsumer consumer = new ClearCaseCheckOutConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      String projectDirectory = "";

      Commandline cl;
      int exitCode;
      try {
         FileUtils.deleteDirectory(workingDirectory);
         String viewName = this.getUniqueViewName(repo, workingDirectory.getAbsolutePath());
         String streamIdentifier = this.getStreamIdentifier(repo.getStreamName(), repo.getVobName());
         cl = this.createCreateViewCommandLine(workingDirectory, viewName, streamIdentifier);
         if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("Executing: " + cl.getWorkingDirectory().getAbsolutePath() + ">>" + cl.toString());
         }

         exitCode = CommandLineUtils.executeCommandLine(cl, new CommandLineUtils.StringStreamConsumer(), stderr);
         if (exitCode == 0) {
            File configSpecLocation;
            if (!repo.isAutoConfigSpec()) {
               configSpecLocation = repo.getConfigSpec();
               if (version != null && StringUtils.isNotEmpty(version.getName())) {
                  throw new UnsupportedOperationException("Building on a label not supported with user-specified config specs");
               }
            } else {
               String configSpec;
               if (!repo.hasElements()) {
                  configSpec = this.createConfigSpec(repo.getLoadDirectory(), version);
               } else {
                  configSpec = this.createConfigSpec(repo.getLoadDirectory(), repo.getElementName(), version);
               }

               if (this.getLogger().isInfoEnabled()) {
                  this.getLogger().info("Created config spec for view '" + viewName + "':\n" + configSpec);
               }

               configSpecLocation = this.writeTemporaryConfigSpecFile(configSpec, viewName);
               projectDirectory = repo.getLoadDirectory();
               if (projectDirectory.startsWith("/")) {
                  projectDirectory = projectDirectory.substring(1);
               }
            }

            cl = this.createUpdateConfigSpecCommandLine(workingDirectory, configSpecLocation, viewName);
            if (this.getLogger().isInfoEnabled()) {
               this.getLogger().info("Executing: " + cl.getWorkingDirectory().getAbsolutePath() + ">>" + cl.toString());
            }

            exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
         }
      } catch (CommandLineException var16) {
         throw new ScmException("Error while executing clearcase command.", var16);
      } catch (IOException var17) {
         throw new ScmException("Error while deleting working directory.", var17);
      }

      return exitCode != 0 ? new CheckOutScmResult(cl.toString(), "The cleartool command failed.", stderr.getOutput(), false) : new CheckOutScmResult(cl.toString(), consumer.getCheckedOutFiles(), projectDirectory);
   }

   protected File writeTemporaryConfigSpecFile(String configSpecContents, String viewName) throws IOException {
      File configSpecLocation = File.createTempFile("configspec-" + viewName, ".txt");
      FileWriter fw = new FileWriter(configSpecLocation);

      try {
         fw.write(configSpecContents);
      } finally {
         try {
            fw.close();
         } catch (IOException var11) {
         }

      }

      configSpecLocation.deleteOnExit();
      return configSpecLocation;
   }

   protected String createConfigSpec(String loadDirectory, ScmVersion version) {
      StringBuilder configSpec = new StringBuilder();
      configSpec.append("element * CHECKEDOUT\n");
      if (version != null && StringUtils.isNotEmpty(version.getName())) {
         configSpec.append("element * " + version.getName() + "\n");
         configSpec.append("element -directory * /main/LATEST\n");
      } else {
         configSpec.append("element * /main/LATEST\n");
      }

      configSpec.append("load " + loadDirectory + "\n");
      return configSpec.toString();
   }

   protected String createConfigSpec(String loadDirectory, String elementName, ScmVersion version) {
      StringBuilder configSpec = new StringBuilder();
      configSpec.append("element * CHECKEDOUT\n");
      if (version != null && StringUtils.isNotEmpty(version.getName())) {
         configSpec.append("element * " + version.getName() + "\n");
         configSpec.append("element * " + elementName + "\n");
      } else {
         configSpec.append("element * /main/LATEST\n");
      }

      configSpec.append("load " + loadDirectory + "\n");
      return configSpec.toString();
   }

   protected Commandline createCreateViewCommandLine(File workingDirectory, String viewName, String streamIdentifier) throws IOException {
      Commandline command = new Commandline();
      command.setWorkingDirectory(workingDirectory.getParentFile().getAbsolutePath());
      command.setExecutable("cleartool");
      command.createArg().setValue("mkview");
      command.createArg().setValue("-snapshot");
      command.createArg().setValue("-tag");
      command.createArg().setValue(viewName);
      if (this.isClearCaseUCM()) {
         command.createArg().setValue("-stream");
         command.createArg().setValue(streamIdentifier);
      }

      if (!this.isClearCaseLT() && this.useVWS()) {
         command.createArg().setValue("-vws");
         command.createArg().setValue(this.getViewStore() + viewName + ".vws");
      }

      command.createArg().setValue(workingDirectory.getCanonicalPath());
      return command;
   }

   protected String getStreamIdentifier(String streamName, String vobName) {
      return streamName != null && vobName != null ? "stream:" + streamName + "@" + vobName : null;
   }

   protected Commandline createUpdateConfigSpecCommandLine(File workingDirectory, File configSpecLocation, String viewName) {
      Commandline command = new Commandline();
      command.setWorkingDirectory(workingDirectory.getAbsolutePath());
      command.setExecutable("cleartool");
      command.createArg().setValue("setcs");
      command.createArg().setValue("-tag");
      command.createArg().setValue(viewName);
      command.createArg().setValue(configSpecLocation.getAbsolutePath());
      return command;
   }

   private String getUniqueViewName(ClearCaseScmProviderRepository repository, String absolutePath) {
      int lastIndexBack = absolutePath.lastIndexOf(92);
      int lastIndexForward = absolutePath.lastIndexOf(47);
      String uniqueId;
      if (lastIndexBack != -1) {
         uniqueId = absolutePath.substring(lastIndexBack + 1);
      } else {
         uniqueId = absolutePath.substring(lastIndexForward + 1);
      }

      return repository.getViewName(uniqueId);
   }

   protected String getViewStore() {
      String result = null;
      if (this.settings.getViewstore() != null) {
         result = this.settings.getViewstore();
      }

      if (result == null) {
         result = "\\\\" + this.getHostName() + "\\viewstore\\";
      } else if (this.isClearCaseLT()) {
         result = result + this.getUserName() + "\\";
      }

      return result;
   }

   protected boolean isClearCaseLT() {
      return "LT".equals(this.settings.getClearcaseType());
   }

   protected boolean isClearCaseUCM() {
      return "UCM".equals(this.settings.getClearcaseType());
   }

   protected boolean useVWS() {
      return this.settings.isUseVWSParameter();
   }

   private String getHostName() {
      try {
         String hostname = InetAddress.getLocalHost().getHostName();
         return hostname;
      } catch (UnknownHostException var3) {
         throw new RuntimeException(var3);
      }
   }

   private String getUserName() {
      String username = System.getProperty("user.name");
      return username;
   }

   public void setSettings(Settings settings) {
      this.settings = settings;
   }
}
