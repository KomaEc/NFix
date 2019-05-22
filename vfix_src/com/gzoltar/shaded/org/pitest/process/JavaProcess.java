package com.gzoltar.shaded.org.pitest.process;

import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.util.Monitor;
import com.gzoltar.shaded.org.pitest.util.StreamMonitor;

public class JavaProcess {
   private final Process process;
   private final Monitor out;
   private final Monitor err;

   public JavaProcess(Process process, SideEffect1<String> sysoutHandler, SideEffect1<String> syserrHandler) {
      this.process = process;
      this.out = new StreamMonitor(process.getInputStream(), sysoutHandler);
      this.err = new StreamMonitor(process.getErrorStream(), syserrHandler);
      this.out.requestStart();
      this.err.requestStart();
   }

   public void destroy() {
      this.out.requestStop();
      this.err.requestStop();
      this.process.destroy();
   }

   public int waitToDie() throws InterruptedException {
      int exitVal = this.process.waitFor();
      this.out.requestStop();
      this.err.requestStop();
      return exitVal;
   }

   public boolean isAlive() {
      try {
         this.process.exitValue();
         return false;
      } catch (IllegalThreadStateException var2) {
         return true;
      }
   }
}
