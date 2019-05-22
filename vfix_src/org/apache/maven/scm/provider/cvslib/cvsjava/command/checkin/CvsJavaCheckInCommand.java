package org.apache.maven.scm.provider.cvslib.cvsjava.command.checkin;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.provider.cvslib.command.checkin.AbstractCvsCheckInCommand;
import org.apache.maven.scm.provider.cvslib.command.checkin.CvsCheckInConsumer;
import org.apache.maven.scm.provider.cvslib.cvsjava.util.CvsConnection;
import org.apache.maven.scm.provider.cvslib.cvsjava.util.CvsLogListener;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsJavaCheckInCommand extends AbstractCvsCheckInCommand {
   protected CheckInScmResult executeCvsCommand(Commandline cl, CvsScmProviderRepository repository, File messageFile) throws ScmException {
      CvsLogListener logListener = new CvsLogListener();
      CvsCheckInConsumer consumer = new CvsCheckInConsumer(repository.getPath(), this.getLogger());

      try {
         CheckInScmResult var7;
         try {
            boolean isSuccess = CvsConnection.processCommand(cl.getArguments(), cl.getWorkingDirectory().getAbsolutePath(), logListener, this.getLogger());
            if (!isSuccess) {
               var7 = new CheckInScmResult(cl.toString(), "The cvs command failed.", logListener.getStderr().toString(), false);
               return var7;
            }

            BufferedReader stream = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(logListener.getStdout().toString().getBytes())));

            String line;
            while((line = stream.readLine()) != null) {
               consumer.consumeLine(line);
            }
         } catch (Exception var18) {
            var18.printStackTrace();
            var7 = new CheckInScmResult(cl.toString(), "The cvs command failed.", logListener.getStdout().toString(), false);
            return var7;
         }
      } finally {
         try {
            FileUtils.forceDelete(messageFile);
         } catch (IOException var17) {
         }

      }

      return new CheckInScmResult(cl.toString(), consumer.getCheckedInFiles());
   }
}
