package org.apache.maven.scm.provider.perforce.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.perforce.PerforceScmProvider;
import org.apache.maven.scm.provider.perforce.repository.PerforceScmProviderRepository;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.Commandline;

public class PerforceInfoCommand extends AbstractCommand implements PerforceCommand {
   private static PerforceInfoCommand singleton = null;
   private Map<String, String> entries = null;

   public static PerforceInfoCommand getInfo(ScmLogger logger, PerforceScmProviderRepository repo) {
      return getSingleton(logger, repo);
   }

   public String getEntry(String key) {
      return (String)this.entries.get(key);
   }

   private static synchronized PerforceInfoCommand getSingleton(ScmLogger logger, PerforceScmProviderRepository repo) {
      if (singleton == null) {
         PerforceInfoCommand pic = new PerforceInfoCommand();
         if (logger != null) {
            pic.setLogger(logger);
         }

         try {
            pic.executeCommand(repo, (ScmFileSet)null, (CommandParameters)null);
            singleton = pic;
         } catch (ScmException var4) {
            if (pic.getLogger().isErrorEnabled()) {
               pic.getLogger().error("ScmException " + var4.getMessage(), var4);
            }
         }
      }

      return singleton;
   }

   protected ScmResult executeCommand(ScmProviderRepository repo, ScmFileSet scmFileSet, CommandParameters commandParameters) throws ScmException {
      if (!PerforceScmProvider.isLive()) {
         return null;
      } else {
         InputStreamReader isReader = null;

         try {
            Commandline command = PerforceScmProvider.createP4Command((PerforceScmProviderRepository)repo, (File)null);
            command.createArg().setValue("info");
            if (this.getLogger().isDebugEnabled()) {
               this.getLogger().debug(PerforceScmProvider.clean("Executing: " + command.toString()));
            }

            Process proc = command.execute();
            isReader = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(isReader);
            this.entries = new HashMap();

            String line;
            while((line = br.readLine()) != null) {
               int idx = line.indexOf(58);
               if (idx == -1) {
                  if (line.indexOf("Client unknown.") == -1) {
                     throw new IllegalStateException("Unexpected results from 'p4 info' command: " + line);
                  }

                  if (this.getLogger().isDebugEnabled()) {
                     this.getLogger().debug("Cannot find client.");
                  }

                  this.entries.put("Client root", "");
               } else {
                  String key = line.substring(0, idx);
                  String value = line.substring(idx + 1).trim();
                  this.entries.put(key, value);
               }
            }
         } catch (CommandLineException var16) {
            throw new ScmException(var16.getLocalizedMessage());
         } catch (IOException var17) {
            throw new ScmException(var17.getLocalizedMessage());
         } finally {
            IOUtil.close((Reader)isReader);
         }

         return null;
      }
   }
}
