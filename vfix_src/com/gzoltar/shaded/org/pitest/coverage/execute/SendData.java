package com.gzoltar.shaded.org.pitest.coverage.execute;

import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.util.Log;
import com.gzoltar.shaded.org.pitest.util.SafeDataOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

final class SendData implements SideEffect1<SafeDataOutputStream> {
   private static final Logger LOG = Log.getLogger();
   private final CoverageOptions arguments;
   private final List<String> testClasses;

   SendData(CoverageOptions arguments, List<String> testClasses) {
      this.arguments = arguments;
      this.testClasses = testClasses;
   }

   public void apply(SafeDataOutputStream dos) {
      this.sendArguments(dos);
      this.sendTests(dos);
   }

   private void sendArguments(SafeDataOutputStream dos) {
      dos.write(this.arguments);
      dos.flush();
   }

   private void sendTests(SafeDataOutputStream dos) {
      LOG.info("Sending " + this.testClasses.size() + " test classes to slave");
      dos.writeInt(this.testClasses.size());
      Iterator i$ = this.testClasses.iterator();

      while(i$.hasNext()) {
         String tc = (String)i$.next();
         dos.writeString(tc);
      }

      dos.flush();
      LOG.info("Sent tests to slave");
   }
}
