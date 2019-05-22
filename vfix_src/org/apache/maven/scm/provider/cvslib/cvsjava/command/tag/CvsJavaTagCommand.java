package org.apache.maven.scm.provider.cvslib.cvsjava.command.tag;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.provider.cvslib.command.tag.AbstractCvsTagCommand;
import org.apache.maven.scm.provider.cvslib.command.tag.CvsTagConsumer;
import org.apache.maven.scm.provider.cvslib.cvsjava.util.CvsConnection;
import org.apache.maven.scm.provider.cvslib.cvsjava.util.CvsLogListener;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsJavaTagCommand extends AbstractCvsTagCommand {
   protected TagScmResult executeCvsCommand(Commandline cl) throws ScmException {
      CvsLogListener logListener = new CvsLogListener();
      CvsTagConsumer consumer = new CvsTagConsumer(this.getLogger());

      try {
         boolean isSuccess = CvsConnection.processCommand(cl.getArguments(), cl.getWorkingDirectory().getAbsolutePath(), logListener, this.getLogger());
         if (!isSuccess) {
            return new TagScmResult(cl.toString(), "The cvs tag command failed.", logListener.getStderr().toString(), false);
         }

         BufferedReader stream = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(logListener.getStdout().toString().getBytes())));

         String line;
         while((line = stream.readLine()) != null) {
            consumer.consumeLine(line);
         }
      } catch (Exception var7) {
         var7.printStackTrace();
         return new TagScmResult(cl.toString(), "The cvs tag command failed.", logListener.getStderr().toString(), false);
      }

      return new TagScmResult(cl.toString(), consumer.getTaggedFiles());
   }
}
