package com.gzoltar.shaded.org.pitest.mutationtest.execute;

import com.gzoltar.shaded.org.pitest.mutationtest.MutationStatusTestPair;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.util.ExitCode;
import com.gzoltar.shaded.org.pitest.util.SafeDataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DefaultReporter implements Reporter {
   private final SafeDataOutputStream w;

   DefaultReporter(OutputStream w) {
      this.w = new SafeDataOutputStream(w);
   }

   public synchronized void describe(MutationIdentifier i) throws IOException {
      this.w.writeByte((byte)1);
      this.w.write(i);
      this.w.flush();
   }

   public synchronized void report(MutationIdentifier i, MutationStatusTestPair mutationDetected) throws IOException {
      this.w.writeByte((byte)2);
      this.w.write(i);
      this.w.write(mutationDetected);
      this.w.flush();
   }

   public synchronized void done(ExitCode exitCode) {
      this.w.writeByte((byte)64);
      this.w.writeInt(exitCode.getCode());
      this.w.flush();
   }
}
