package com.gzoltar.shaded.org.jacoco.core.data;

import com.gzoltar.shaded.org.jacoco.core.internal.data.CompactDataOutput;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ExecutionDataWriter implements ISessionInfoVisitor, IExecutionDataVisitor {
   public static final char FORMAT_VERSION = 4103;
   public static final char MAGIC_NUMBER = '샀';
   public static final byte BLOCK_HEADER = 1;
   public static final byte BLOCK_SESSIONINFO = 16;
   public static final byte BLOCK_EXECUTIONDATA = 17;
   protected final CompactDataOutput out;

   public ExecutionDataWriter(OutputStream output) throws IOException {
      this.out = new CompactDataOutput(output);
      this.writeHeader();
   }

   private void writeHeader() throws IOException {
      this.out.writeByte(1);
      this.out.writeChar(49344);
      this.out.writeChar(FORMAT_VERSION);
   }

   public void flush() throws IOException {
      this.out.flush();
   }

   public void visitSessionInfo(SessionInfo info) {
      try {
         this.out.writeByte(16);
         this.out.writeUTF(info.getId());
         this.out.writeLong(info.getStartTimeStamp());
         this.out.writeLong(info.getDumpTimeStamp());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void visitClassExecution(ExecutionData data) {
      if (data.hasHits()) {
         try {
            this.out.writeByte(17);
            this.out.writeLong(data.getId());
            this.out.writeUTF(data.getName());
            this.out.writeBooleanArray(data.getProbes());
         } catch (IOException var3) {
            throw new RuntimeException(var3);
         }
      }

   }

   public static final byte[] getFileHeader() {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();

      try {
         new ExecutionDataWriter(buffer);
      } catch (IOException var2) {
         throw new AssertionError(var2);
      }

      return buffer.toByteArray();
   }
}
