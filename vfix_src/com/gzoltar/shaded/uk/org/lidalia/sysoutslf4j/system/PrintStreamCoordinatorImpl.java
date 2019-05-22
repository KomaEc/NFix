package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.system;

import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.PrintStreamCoordinator;
import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.SLF4JPrintStream;
import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.SystemOutput;
import java.io.PrintStream;

public final class PrintStreamCoordinatorImpl implements PrintStreamCoordinator {
   public void replaceSystemOutputsWithSLF4JPrintStreams() {
      SystemOutput[] var1;
      int var2 = (var1 = SystemOutput.values()).length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SystemOutput systemOutput = var1[var3];
         replaceSystemOutputWithSLF4JPrintStream(systemOutput);
      }

   }

   private static void replaceSystemOutputWithSLF4JPrintStream(SystemOutput systemOutput) {
      SLF4JPrintStreamImpl slf4jPrintStream = buildSLF4JPrintStream(systemOutput.get());
      systemOutput.set(slf4jPrintStream);
   }

   private static SLF4JPrintStreamImpl buildSLF4JPrintStream(PrintStream originalPrintStream) {
      LoggerAppenderStore loggerAppenderStore = new LoggerAppenderStore();
      SLF4JPrintStreamDelegate delegate = new SLF4JPrintStreamDelegate(originalPrintStream, loggerAppenderStore);
      return new SLF4JPrintStreamImpl(originalPrintStream, delegate);
   }

   public void restoreOriginalSystemOutputs() {
      SystemOutput[] var1;
      int var2 = (var1 = SystemOutput.values()).length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SystemOutput systemOutput = var1[var3];
         restoreSystemOutput(systemOutput);
      }

   }

   private static void restoreSystemOutput(SystemOutput systemOutput) {
      SLF4JPrintStream slf4jPrintStream = (SLF4JPrintStream)systemOutput.get();
      systemOutput.set(slf4jPrintStream.getOriginalPrintStream());
   }
}
