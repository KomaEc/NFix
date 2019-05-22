package org.apache.maven.scm.provider.cvslib.cvsjava.command.changelog;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.apache.maven.scm.provider.cvslib.command.changelog.AbstractCvsChangeLogCommand;
import org.apache.maven.scm.provider.cvslib.command.changelog.CvsChangeLogConsumer;
import org.apache.maven.scm.provider.cvslib.cvsjava.util.CvsConnection;
import org.apache.maven.scm.provider.cvslib.cvsjava.util.CvsLogListener;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsJavaChangeLogCommand extends AbstractCvsChangeLogCommand {
   protected ChangeLogScmResult executeCvsCommand(Commandline cl, Date startDate, Date endDate, ScmVersion startVersion, ScmVersion endVersion, String datePattern) throws ScmException {
      CvsLogListener logListener = new CvsLogListener();
      CvsChangeLogConsumer consumer = new CvsChangeLogConsumer(this.getLogger(), datePattern);

      try {
         boolean isSuccess = CvsConnection.processCommand(cl.getArguments(), cl.getWorkingDirectory().getAbsolutePath(), logListener, this.getLogger());
         if (!isSuccess) {
            return new ChangeLogScmResult(cl.toString(), "The cvs command failed.", logListener.getStderr().toString(), false);
         }

         BufferedReader stream = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(logListener.getStdout().toString().getBytes())));

         String line;
         while((line = stream.readLine()) != null) {
            consumer.consumeLine(line);
         }
      } catch (Exception var12) {
         var12.printStackTrace();
         return new ChangeLogScmResult(cl.toString(), "The cvs command failed.", logListener.getStdout().toString(), false);
      }

      ChangeLogSet changeLogSet = new ChangeLogSet(consumer.getModifications(), startDate, endDate);
      changeLogSet.setStartVersion(startVersion);
      changeLogSet.setEndVersion(endVersion);
      return new ChangeLogScmResult(cl.toString(), changeLogSet);
   }

   protected void addDateRangeParameter(Commandline cl, String dateRange) {
      cl.createArg().setValue(dateRange);
   }
}
