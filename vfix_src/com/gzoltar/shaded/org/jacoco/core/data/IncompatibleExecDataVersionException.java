package com.gzoltar.shaded.org.jacoco.core.data;

import java.io.IOException;

public class IncompatibleExecDataVersionException extends IOException {
   private static final long serialVersionUID = 1L;
   private final int actualVersion;

   public IncompatibleExecDataVersionException(int actualVersion) {
      super(String.format("Cannot read execution data version 0x%x. This version of JaCoCo uses execution data version 0x%x.", actualVersion, Integer.valueOf(ExecutionDataWriter.FORMAT_VERSION)));
      this.actualVersion = actualVersion;
   }

   public int getExpectedVersion() {
      return ExecutionDataWriter.FORMAT_VERSION;
   }

   public int getActualVersion() {
      return this.actualVersion;
   }
}
