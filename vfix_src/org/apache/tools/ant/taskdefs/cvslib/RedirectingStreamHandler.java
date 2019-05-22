package org.apache.tools.ant.taskdefs.cvslib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;

class RedirectingStreamHandler extends PumpStreamHandler {
   RedirectingStreamHandler(ChangeLogParser parser) {
      super(new RedirectingOutputStream(parser), new ByteArrayOutputStream());
   }

   String getErrors() {
      try {
         ByteArrayOutputStream error = (ByteArrayOutputStream)this.getErr();
         return error.toString("ASCII");
      } catch (Exception var2) {
         return null;
      }
   }

   public void stop() {
      super.stop();

      try {
         this.getErr().close();
         this.getOut().close();
      } catch (IOException var2) {
         throw new BuildException(var2);
      }
   }
}
