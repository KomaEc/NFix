package com.gzoltar.shaded.org.jacoco.core.runtime;

import com.gzoltar.shaded.org.jacoco.core.data.ExecutionDataReader;
import java.io.IOException;
import java.io.InputStream;

public class RemoteControlReader extends ExecutionDataReader {
   private IRemoteCommandVisitor remoteCommandVisitor;

   public RemoteControlReader(InputStream input) throws IOException {
      super(input);
   }

   protected boolean readBlock(byte blockid) throws IOException {
      switch(blockid) {
      case 32:
         return false;
      case 64:
         this.readDumpCommand();
         return true;
      default:
         return super.readBlock(blockid);
      }
   }

   public void setRemoteCommandVisitor(IRemoteCommandVisitor visitor) {
      this.remoteCommandVisitor = visitor;
   }

   private void readDumpCommand() throws IOException {
      if (this.remoteCommandVisitor == null) {
         throw new IOException("No remote command visitor.");
      } else {
         boolean dump = this.in.readBoolean();
         boolean reset = this.in.readBoolean();
         this.remoteCommandVisitor.visitDumpCommand(dump, reset);
      }
   }
}
