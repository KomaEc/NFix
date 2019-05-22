package org.apache.maven.scm.provider.perforce.command.checkout;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkout.AbstractCheckOutCommand;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.perforce.PerforceScmProvider;
import org.apache.maven.scm.provider.perforce.command.PerforceCommand;
import org.apache.maven.scm.provider.perforce.repository.PerforceScmProviderRepository;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class PerforceCheckOutCommand extends AbstractCheckOutCommand implements PerforceCommand {
   private String actualLocation;

   protected CheckOutScmResult executeCheckOutCommand(ScmProviderRepository repo, ScmFileSet files, ScmVersion version, boolean recursive) throws ScmException {
      PerforceScmProviderRepository prepo = (PerforceScmProviderRepository)repo;
      File workingDirectory = new File(files.getBasedir().getAbsolutePath());
      this.actualLocation = PerforceScmProvider.getRepoPath(this.getLogger(), prepo, files.getBasedir());
      String specname = PerforceScmProvider.getClientspecName(this.getLogger(), prepo, workingDirectory);
      PerforceCheckOutConsumer consumer = new PerforceCheckOutConsumer(specname, this.actualLocation);
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Checkout working directory: " + workingDirectory);
      }

      Commandline cl = null;

      String line;
      try {
         cl = PerforceScmProvider.createP4Command(prepo, workingDirectory);
         cl.createArg().setValue("client");
         cl.createArg().setValue("-i");
         if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("Executing: " + PerforceScmProvider.clean(cl.toString()));
         }

         String client = PerforceScmProvider.createClientspec(this.getLogger(), prepo, workingDirectory, this.actualLocation);
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Updating clientspec:\n" + client);
         }

         CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();
         int exitCode = CommandLineUtils.executeCommandLine(cl, new ByteArrayInputStream(client.getBytes()), consumer, err);
         if (exitCode != 0) {
            line = CommandLineUtils.toString(cl.getCommandline());
            StringBuilder msg = new StringBuilder("Exit code: " + exitCode + " - " + err.getOutput());
            msg.append('\n');
            msg.append("Command line was:" + line);
            throw new CommandLineException(msg.toString());
         }
      } catch (CommandLineException var86) {
         if (this.getLogger().isErrorEnabled()) {
            this.getLogger().error("CommandLineException " + var86.getMessage(), var86);
         }
      }

      boolean clientspecExists = consumer.isSuccess();

      CheckOutScmResult var89;
      try {
         if (clientspecExists) {
            try {
               this.getLastChangelist(prepo, workingDirectory, specname);
               cl = createCommandLine(prepo, workingDirectory, version, specname);
               if (this.getLogger().isDebugEnabled()) {
                  this.getLogger().debug("Executing: " + PerforceScmProvider.clean(cl.toString()));
               }

               Process proc = cl.execute();

               for(BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream())); (line = br.readLine()) != null; consumer.consumeLine(line)) {
                  if (this.getLogger().isDebugEnabled()) {
                     this.getLogger().debug("Consuming: " + line);
                  }
               }

               CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();
               int exitCode = CommandLineUtils.executeCommandLine(cl, consumer, err);
               if (exitCode != 0) {
                  String cmdLine = CommandLineUtils.toString(cl.getCommandline());
                  StringBuilder msg = new StringBuilder("Exit code: " + exitCode + " - " + err.getOutput());
                  msg.append('\n');
                  msg.append("Command line was:" + cmdLine);
                  throw new CommandLineException(msg.toString());
               }

               if (this.getLogger().isDebugEnabled()) {
                  this.getLogger().debug("Perforce sync complete.");
               }
            } catch (CommandLineException var83) {
               if (this.getLogger().isErrorEnabled()) {
                  this.getLogger().error("CommandLineException " + var83.getMessage(), var83);
               }
            } catch (IOException var84) {
               if (this.getLogger().isErrorEnabled()) {
                  this.getLogger().error("IOException " + var84.getMessage(), var84);
               }
            }
         }

         if (!consumer.isSuccess()) {
            var89 = new CheckOutScmResult(cl.toString(), "Unable to sync.  Are you logged in?", consumer.getOutput(), consumer.isSuccess());
            return var89;
         }

         var89 = new CheckOutScmResult(cl.toString(), consumer.getCheckedout());
      } finally {
         if (clientspecExists && !prepo.isPersistCheckout()) {
            InputStreamReader isReader = null;
            InputStreamReader isReaderErr = null;

            try {
               cl = PerforceScmProvider.createP4Command(prepo, workingDirectory);
               cl.createArg().setValue("client");
               cl.createArg().setValue("-d");
               cl.createArg().setValue(specname);
               if (this.getLogger().isInfoEnabled()) {
                  this.getLogger().info("Executing: " + PerforceScmProvider.clean(cl.toString()));
               }

               Process proc = cl.execute();
               isReader = new InputStreamReader(proc.getInputStream());

               BufferedReader br;
               String line;
               for(br = new BufferedReader(isReader); (line = br.readLine()) != null; consumer.consumeLine(line)) {
                  if (this.getLogger().isDebugEnabled()) {
                     this.getLogger().debug("Consuming: " + line);
                  }
               }

               br.close();
               isReaderErr = new InputStreamReader(proc.getErrorStream());

               BufferedReader brErr;
               for(brErr = new BufferedReader(isReaderErr); (line = brErr.readLine()) != null; consumer.consumeLine(line)) {
                  if (this.getLogger().isDebugEnabled()) {
                     this.getLogger().debug("Consuming stderr: " + line);
                  }
               }

               brErr.close();
            } catch (CommandLineException var80) {
               if (this.getLogger().isErrorEnabled()) {
                  this.getLogger().error("CommandLineException " + var80.getMessage(), var80);
               }
            } catch (IOException var81) {
               if (this.getLogger().isErrorEnabled()) {
                  this.getLogger().error("IOException " + var81.getMessage(), var81);
               }
            } finally {
               IOUtil.close((Reader)isReader);
               IOUtil.close((Reader)isReaderErr);
            }
         } else if (clientspecExists) {
            System.setProperty("maven.scm.perforce.clientspec.name", specname);
         }

      }

      return var89;
   }

   public static Commandline createCommandLine(PerforceScmProviderRepository repo, File workingDirectory, ScmVersion version, String specname) {
      Commandline command = PerforceScmProvider.createP4Command(repo, workingDirectory);
      command.createArg().setValue("-c" + specname);
      command.createArg().setValue("sync");
      String[] files = workingDirectory.list();
      if (files == null || files.length == 0) {
         command.createArg().setValue("-f");
      }

      if (version != null && StringUtils.isNotEmpty(version.getName())) {
         command.createArg().setValue("@" + version.getName());
      }

      return command;
   }

   private int getLastChangelist(PerforceScmProviderRepository repo, File workingDirectory, String specname) {
      int lastChangelist = 0;

      try {
         Commandline command = PerforceScmProvider.createP4Command(repo, workingDirectory);
         command.createArg().setValue("-c" + specname);
         command.createArg().setValue("changes");
         command.createArg().setValue("-m1");
         command.createArg().setValue("-ssubmitted");
         command.createArg().setValue("//" + specname + "/...");
         this.getLogger().debug("Executing: " + PerforceScmProvider.clean(command.toString()));
         Process proc = command.execute();
         BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
         String lastChangelistStr = "";

         String line;
         while((line = br.readLine()) != null) {
            this.getLogger().debug("Consuming: " + line);
            Pattern changeRegexp = Pattern.compile("Change (\\d+)");
            Matcher matcher = changeRegexp.matcher(line);
            if (matcher.find()) {
               lastChangelistStr = matcher.group(1);
            }
         }

         br.close();

         try {
            lastChangelist = Integer.parseInt(lastChangelistStr);
         } catch (NumberFormatException var12) {
            this.getLogger().debug("Could not parse changelist from line " + line);
         }
      } catch (IOException var13) {
         this.getLogger().error((Throwable)var13);
      } catch (CommandLineException var14) {
         this.getLogger().error((Throwable)var14);
      }

      return lastChangelist;
   }
}
