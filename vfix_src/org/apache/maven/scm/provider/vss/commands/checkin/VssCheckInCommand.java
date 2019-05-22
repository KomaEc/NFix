package org.apache.maven.scm.provider.vss.commands.checkin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkin.AbstractCheckInCommand;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.vss.commands.VssCommandLineUtils;
import org.apache.maven.scm.provider.vss.repository.VssScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class VssCheckInCommand extends AbstractCheckInCommand {
   protected CheckInScmResult executeCheckInCommand(ScmProviderRepository repository, ScmFileSet fileSet, String message, ScmVersion scmVersion) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing checkin command...");
      }

      VssScmProviderRepository repo = (VssScmProviderRepository)repository;
      List<Commandline> commandLines = this.buildCmdLine(repo, fileSet, scmVersion);
      VssCheckInConsumer consumer = new VssCheckInConsumer(repo, this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      StringBuilder sb = new StringBuilder();
      Iterator i$ = commandLines.iterator();

      while(i$.hasNext()) {
         Commandline cl = (Commandline)i$.next();
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Executing: " + cl.getWorkingDirectory().getAbsolutePath() + ">>" + cl.toString());
         }

         int exitCode = VssCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
         if (exitCode != 0) {
            String error = stderr.getOutput();
            if (this.getLogger().isDebugEnabled()) {
               this.getLogger().debug("VSS returns error: [" + error + "] return code: [" + exitCode + "]");
            }

            if (error.indexOf("A writable copy of") < 0) {
               return new CheckInScmResult(cl.toString(), "The vss command failed.", error, false);
            }

            if (this.getLogger().isWarnEnabled()) {
               this.getLogger().warn(error);
            }
         }
      }

      return new CheckInScmResult(sb.toString(), new ArrayList());
   }

   public List<Commandline> buildCmdLine(VssScmProviderRepository repo, ScmFileSet fileSet, ScmVersion version) throws ScmException {
      List<File> files = fileSet.getFileList();
      List<Commandline> commands = new ArrayList();
      if (files.size() > 0) {
         String base;
         try {
            base = fileSet.getBasedir().getCanonicalPath();
         } catch (IOException var17) {
            throw new ScmException("Invalid canonical path", var17);
         }

         Iterator i$ = files.iterator();

         while(i$.hasNext()) {
            File file = (File)i$.next();
            Commandline command = new Commandline();

            try {
               command.addSystemEnvironment();
            } catch (Exception var16) {
               throw new ScmException("Can't add system environment.", var16);
            }

            command.addEnvironment("SSDIR", repo.getVssdir());
            String ssDir = VssCommandLineUtils.getSsDir();
            command.setExecutable(ssDir + "ss");
            command.createArg().setValue("Checkin");

            try {
               String absolute = file.getCanonicalPath();
               int index = absolute.indexOf(base);
               String relative;
               if (index >= 0) {
                  relative = absolute.substring(index + base.length());
               } else {
                  relative = file.getPath();
               }

               relative = relative.replace('\\', '/');
               if (!relative.startsWith("/")) {
                  relative = '/' + relative;
               }

               String relativeFolder = relative.substring(0, relative.lastIndexOf(47));
               command.setWorkingDirectory((new File(fileSet.getBasedir().getAbsolutePath() + File.separatorChar + relativeFolder)).getCanonicalPath());
               command.createArg().setValue("$" + repo.getProject() + relative);
            } catch (IOException var18) {
               throw new ScmException("Invalid canonical path", var18);
            }

            if (repo.getUserPassword() != null) {
               command.createArg().setValue("-Y" + repo.getUserPassword());
            }

            command.createArg().setValue("-I-");
            command.createArg().setValue("-GWR");
            commands.add(command);
         }
      } else {
         Commandline command = new Commandline();
         command.setWorkingDirectory(fileSet.getBasedir().getAbsolutePath());

         try {
            command.addSystemEnvironment();
         } catch (Exception var15) {
            throw new ScmException("Can't add system environment.", var15);
         }

         command.addEnvironment("SSDIR", repo.getVssdir());
         String ssDir = VssCommandLineUtils.getSsDir();
         command.setExecutable(ssDir + "ss");
         command.createArg().setValue("Checkin");
         command.createArg().setValue("$" + repo.getProject());
         command.createArg().setValue("-R");
         if (repo.getUserPassword() != null) {
            command.createArg().setValue("-Y" + repo.getUserPassword());
         }

         command.createArg().setValue("-I-");
         command.createArg().setValue("-GWR");
         commands.add(command);
      }

      return commands;
   }
}
