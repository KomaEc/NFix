package org.apache.maven.scm.provider.perforce.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.perforce.PerforceScmProvider;
import org.apache.maven.scm.provider.perforce.repository.PerforceScmProviderRepository;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.Commandline;

public class PerforceWhereCommand {
   private ScmLogger logger = null;
   private PerforceScmProviderRepository repo = null;

   public PerforceWhereCommand(ScmLogger log, PerforceScmProviderRepository repos) {
      this.logger = log;
      this.repo = repos;
   }

   public String getDepotLocation(File file) {
      return this.getDepotLocation(file.getAbsolutePath());
   }

   public String getDepotLocation(String filepath) {
      if (!PerforceScmProvider.isLive()) {
         return null;
      } else {
         InputStreamReader isReader = null;
         InputStreamReader isReaderErr = null;

         try {
            Commandline command = PerforceScmProvider.createP4Command(this.repo, (File)null);
            command.createArg().setValue("where");
            command.createArg().setValue(filepath);
            if (this.logger.isDebugEnabled()) {
               this.logger.debug(PerforceScmProvider.clean("Executing: " + command.toString()));
            }

            Process proc = command.execute();
            isReader = new InputStreamReader(proc.getInputStream());
            isReaderErr = new InputStreamReader(proc.getErrorStream());
            BufferedReader br = new BufferedReader(isReader);
            BufferedReader brErr = new BufferedReader(isReaderErr);

            String path;
            String line;
            String var10;
            for(path = null; (line = br.readLine()) != null; path = line.substring(0, line.lastIndexOf("//") - 1)) {
               if (line.indexOf("not in client view") != -1) {
                  if (this.logger.isErrorEnabled()) {
                     this.logger.error(line);
                  }

                  var10 = null;
                  return var10;
               }

               if (line.indexOf("is not under") != -1) {
                  if (this.logger.isErrorEnabled()) {
                     this.logger.error(line);
                  }

                  var10 = null;
                  return var10;
               }

               if (this.logger.isDebugEnabled()) {
                  this.logger.debug(line);
               }
            }

            while((line = brErr.readLine()) != null) {
               if (line.indexOf("not in client view") != -1) {
                  if (this.logger.isErrorEnabled()) {
                     this.logger.error(line);
                  }

                  var10 = null;
                  return var10;
               }

               if (line.indexOf("is not under") != -1) {
                  if (this.logger.isErrorEnabled()) {
                     this.logger.error(line);
                  }

                  var10 = null;
                  return var10;
               }

               if (this.logger.isDebugEnabled()) {
                  this.logger.debug(line);
               }
            }

            var10 = path;
            return var10;
         } catch (CommandLineException var15) {
            if (this.logger.isErrorEnabled()) {
               this.logger.error((Throwable)var15);
            }

            throw new RuntimeException(var15.getLocalizedMessage());
         } catch (IOException var16) {
            if (this.logger.isErrorEnabled()) {
               this.logger.error((Throwable)var16);
            }

            throw new RuntimeException(var16.getLocalizedMessage());
         } finally {
            IOUtil.close((Reader)isReader);
            IOUtil.close((Reader)isReaderErr);
         }
      }
   }
}
