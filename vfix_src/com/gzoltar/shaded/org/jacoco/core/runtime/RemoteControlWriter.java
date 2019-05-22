package com.gzoltar.shaded.org.jacoco.core.runtime;

import com.gzoltar.shaded.org.jacoco.core.data.ExecutionDataWriter;
import java.io.IOException;
import java.io.OutputStream;

public class RemoteControlWriter extends ExecutionDataWriter implements IRemoteCommandVisitor {
   public static final byte BLOCK_CMDOK = 32;
   public static final byte BLOCK_CMDDUMP = 64;

   public RemoteControlWriter(OutputStream output) throws IOException {
      super(output);
   }

   public void sendCmdOk() throws IOException {
      this.out.writeByte(32);
   }

   public void visitDumpCommand(boolean dump, boolean reset) throws IOException {
      this.out.writeByte(64);
      this.out.writeBoolean(dump);
      this.out.writeBoolean(reset);
   }
}
