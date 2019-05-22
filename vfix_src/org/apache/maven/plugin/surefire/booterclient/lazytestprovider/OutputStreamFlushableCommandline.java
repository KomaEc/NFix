package org.apache.maven.plugin.surefire.booterclient.lazytestprovider;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.CommandLineException;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.Commandline;

public class OutputStreamFlushableCommandline extends Commandline implements FlushReceiverProvider {
   private FlushReceiver flushReceiver;

   public Process execute() throws CommandLineException {
      Process process = super.execute();
      if (process.getOutputStream() != null) {
         this.flushReceiver = new OutputStreamFlushableCommandline.OutputStreamFlushReceiver(process.getOutputStream());
      }

      return process;
   }

   public FlushReceiver getFlushReceiver() {
      return this.flushReceiver;
   }

   private final class OutputStreamFlushReceiver implements FlushReceiver {
      private final OutputStream outputStream;

      private OutputStreamFlushReceiver(OutputStream outputStream) {
         this.outputStream = outputStream;
      }

      public void flush() throws IOException {
         this.outputStream.flush();
      }

      // $FF: synthetic method
      OutputStreamFlushReceiver(OutputStream x1, Object x2) {
         this(x1);
      }
   }
}
