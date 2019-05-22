package org.apache.tools.ant;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.WeakHashMap;

public class DemuxOutputStream extends OutputStream {
   private static final int MAX_SIZE = 1024;
   private static final int INTIAL_SIZE = 132;
   private static final int CR = 13;
   private static final int LF = 10;
   private WeakHashMap buffers = new WeakHashMap();
   private Project project;
   private boolean isErrorStream;

   public DemuxOutputStream(Project project, boolean isErrorStream) {
      this.project = project;
      this.isErrorStream = isErrorStream;
   }

   private DemuxOutputStream.BufferInfo getBufferInfo() {
      Thread current = Thread.currentThread();
      DemuxOutputStream.BufferInfo bufferInfo = (DemuxOutputStream.BufferInfo)this.buffers.get(current);
      if (bufferInfo == null) {
         bufferInfo = new DemuxOutputStream.BufferInfo();
         bufferInfo.buffer = new ByteArrayOutputStream(132);
         bufferInfo.crSeen = false;
         this.buffers.put(current, bufferInfo);
      }

      return bufferInfo;
   }

   private void resetBufferInfo() {
      Thread current = Thread.currentThread();
      DemuxOutputStream.BufferInfo bufferInfo = (DemuxOutputStream.BufferInfo)this.buffers.get(current);

      try {
         bufferInfo.buffer.close();
      } catch (IOException var4) {
      }

      bufferInfo.buffer = new ByteArrayOutputStream();
      bufferInfo.crSeen = false;
   }

   private void removeBuffer() {
      Thread current = Thread.currentThread();
      this.buffers.remove(current);
   }

   public void write(int cc) throws IOException {
      byte c = (byte)cc;
      DemuxOutputStream.BufferInfo bufferInfo = this.getBufferInfo();
      if (c == 10) {
         bufferInfo.buffer.write(cc);
         this.processBuffer(bufferInfo.buffer);
      } else {
         if (bufferInfo.crSeen) {
            this.processBuffer(bufferInfo.buffer);
         }

         bufferInfo.buffer.write(cc);
      }

      bufferInfo.crSeen = c == 13;
      if (!bufferInfo.crSeen && bufferInfo.buffer.size() > 1024) {
         this.processBuffer(bufferInfo.buffer);
      }

   }

   protected void processBuffer(ByteArrayOutputStream buffer) {
      String output = buffer.toString();
      this.project.demuxOutput(output, this.isErrorStream);
      this.resetBufferInfo();
   }

   protected void processFlush(ByteArrayOutputStream buffer) {
      String output = buffer.toString();
      this.project.demuxFlush(output, this.isErrorStream);
      this.resetBufferInfo();
   }

   public void close() throws IOException {
      this.flush();
      this.removeBuffer();
   }

   public void flush() throws IOException {
      DemuxOutputStream.BufferInfo bufferInfo = this.getBufferInfo();
      if (bufferInfo.buffer.size() > 0) {
         this.processFlush(bufferInfo.buffer);
      }

   }

   public void write(byte[] b, int off, int len) throws IOException {
      int offset = off;
      int blockStartOffset = off;
      int remaining = len;

      for(DemuxOutputStream.BufferInfo bufferInfo = this.getBufferInfo(); remaining > 0; blockStartOffset = offset) {
         while(remaining > 0 && b[offset] != 10 && b[offset] != 13) {
            ++offset;
            --remaining;
         }

         int blockLength = offset - blockStartOffset;
         if (blockLength > 0) {
            bufferInfo.buffer.write(b, blockStartOffset, blockLength);
         }

         while(remaining > 0 && (b[offset] == 10 || b[offset] == 13)) {
            this.write(b[offset]);
            ++offset;
            --remaining;
         }
      }

   }

   private static class BufferInfo {
      private ByteArrayOutputStream buffer;
      private boolean crSeen;

      private BufferInfo() {
         this.crSeen = false;
      }

      // $FF: synthetic method
      BufferInfo(Object x0) {
         this();
      }
   }
}
