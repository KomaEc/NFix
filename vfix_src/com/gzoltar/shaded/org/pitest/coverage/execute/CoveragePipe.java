package com.gzoltar.shaded.org.pitest.coverage.execute;

import com.gzoltar.shaded.org.pitest.coverage.CoverageReceiver;
import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.util.ExitCode;
import com.gzoltar.shaded.org.pitest.util.SafeDataOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import sun.pitest.CodeCoverageStore;

public class CoveragePipe implements CoverageReceiver {
   private final SafeDataOutputStream dos;

   public CoveragePipe(OutputStream dos) {
      this.dos = new SafeDataOutputStream(dos);
   }

   public synchronized void newTest() {
      CodeCoverageStore.reset();
   }

   public synchronized void recordTestOutcome(Description description, boolean wasGreen, int executionTime) {
      Collection<Long> hits = CodeCoverageStore.getHits();
      this.dos.writeByte((byte)16);
      this.dos.write(description);
      this.dos.writeInt(hits.size());
      Iterator i$ = hits.iterator();

      while(i$.hasNext()) {
         Long each = (Long)i$.next();
         this.dos.writeLong(each);
      }

      this.dos.writeBoolean(wasGreen);
      this.dos.writeInt(executionTime);
   }

   public synchronized void end(ExitCode exitCode) {
      this.dos.writeByte((byte)64);
      this.dos.writeInt(exitCode.getCode());
      this.dos.flush();
   }

   public synchronized void registerClass(int id, String className) {
      this.dos.writeByte((byte)32);
      this.dos.writeInt(id);
      this.dos.writeString(className);
   }

   public synchronized void registerProbes(int classId, String methodName, String methodDesc, int firstProbe, int lastProbe) {
      this.dos.writeByte((byte)4);
      this.dos.writeInt(classId);
      this.dos.writeString(methodName);
      this.dos.writeString(methodDesc);
      this.dos.writeInt(firstProbe);
      this.dos.writeInt(lastProbe);
   }
}
