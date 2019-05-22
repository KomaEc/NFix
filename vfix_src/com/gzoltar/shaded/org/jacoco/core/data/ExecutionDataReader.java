package com.gzoltar.shaded.org.jacoco.core.data;

import com.gzoltar.shaded.org.jacoco.core.internal.data.CompactDataInput;
import java.io.IOException;
import java.io.InputStream;

public class ExecutionDataReader {
   protected final CompactDataInput in;
   private ISessionInfoVisitor sessionInfoVisitor = null;
   private IExecutionDataVisitor executionDataVisitor = null;
   private boolean firstBlock = true;

   public ExecutionDataReader(InputStream input) {
      this.in = new CompactDataInput(input);
   }

   public void setSessionInfoVisitor(ISessionInfoVisitor visitor) {
      this.sessionInfoVisitor = visitor;
   }

   public void setExecutionDataVisitor(IExecutionDataVisitor visitor) {
      this.executionDataVisitor = visitor;
   }

   public boolean read() throws IOException, IncompatibleExecDataVersionException {
      byte type;
      do {
         int i = this.in.read();
         if (i == -1) {
            return false;
         }

         type = (byte)i;
         if (this.firstBlock && type != 1) {
            throw new IOException("Invalid execution data file.");
         }

         this.firstBlock = false;
      } while(this.readBlock(type));

      return true;
   }

   protected boolean readBlock(byte blocktype) throws IOException {
      switch(blocktype) {
      case 1:
         this.readHeader();
         return true;
      case 16:
         this.readSessionInfo();
         return true;
      case 17:
         this.readExecutionData();
         return true;
      default:
         throw new IOException(String.format("Unknown block type %x.", blocktype));
      }
   }

   private void readHeader() throws IOException {
      if (this.in.readChar() != '샀') {
         throw new IOException("Invalid execution data file.");
      } else {
         char version = this.in.readChar();
         if (version != ExecutionDataWriter.FORMAT_VERSION) {
            throw new IncompatibleExecDataVersionException(version);
         }
      }
   }

   private void readSessionInfo() throws IOException {
      if (this.sessionInfoVisitor == null) {
         throw new IOException("No session info visitor.");
      } else {
         String id = this.in.readUTF();
         long start = this.in.readLong();
         long dump = this.in.readLong();
         this.sessionInfoVisitor.visitSessionInfo(new SessionInfo(id, start, dump));
      }
   }

   private void readExecutionData() throws IOException {
      if (this.executionDataVisitor == null) {
         throw new IOException("No execution data visitor.");
      } else {
         long id = this.in.readLong();
         String name = this.in.readUTF();
         boolean[] probes = this.in.readBooleanArray();
         this.executionDataVisitor.visitClassExecution(new ExecutionData(id, name, probes));
      }
   }
}
