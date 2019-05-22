package org.apache.maven.scm.provider.cvslib.cvsjava.command.branch;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.command.branch.BranchScmResult;
import org.apache.maven.scm.provider.cvslib.command.branch.AbstractCvsBranchCommand;
import org.apache.maven.scm.provider.cvslib.command.branch.CvsBranchConsumer;
import org.apache.maven.scm.provider.cvslib.cvsjava.util.CvsConnection;
import org.apache.maven.scm.provider.cvslib.cvsjava.util.CvsLogListener;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsJavaBranchCommand extends AbstractCvsBranchCommand {
   protected BranchScmResult executeCvsCommand(Commandline cl) throws ScmException {
      CvsLogListener logListener = new CvsLogListener();
      CvsBranchConsumer consumer = new CvsBranchConsumer(this.getLogger());

      try {
         boolean isSuccess = CvsConnection.processCommand(cl.getArguments(), cl.getWorkingDirectory().getAbsolutePath(), logListener, this.getLogger());
         if (!isSuccess) {
            return new BranchScmResult(cl.toString(), "The cvs branch command failed.", logListener.getStderr().toString(), false);
         }

         BufferedReader stream = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(logListener.getStdout().toString().getBytes())));

         String line;
         while((line = stream.readLine()) != null) {
            consumer.consumeLine(line);
         }
      } catch (Exception var7) {
         this.getLogger().error(var7.getMessage(), var7);
         return new BranchScmResult(cl.toString(), "The cvs branch command failed.", logListener.getStderr().toString(), false);
      }

      return new BranchScmResult(cl.toString(), consumer.getTaggedFiles());
   }
}
