package org.apache.tools.tar;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarOutputStream extends FilterOutputStream {
   public static final int LONGFILE_ERROR = 0;
   public static final int LONGFILE_TRUNCATE = 1;
   public static final int LONGFILE_GNU = 2;
   protected boolean debug;
   protected long currSize;
   protected String currName;
   protected long currBytes;
   protected byte[] oneBuf;
   protected byte[] recordBuf;
   protected int assemLen;
   protected byte[] assemBuf;
   protected TarBuffer buffer;
   protected int longFileMode;
   private boolean closed;

   public TarOutputStream(OutputStream os) {
      this(os, 10240, 512);
   }

   public TarOutputStream(OutputStream os, int blockSize) {
      this(os, blockSize, 512);
   }

   public TarOutputStream(OutputStream os, int blockSize, int recordSize) {
      super(os);
      this.longFileMode = 0;
      this.closed = false;
      this.buffer = new TarBuffer(os, blockSize, recordSize);
      this.debug = false;
      this.assemLen = 0;
      this.assemBuf = new byte[recordSize];
      this.recordBuf = new byte[recordSize];
      this.oneBuf = new byte[1];
   }

   public void setLongFileMode(int longFileMode) {
      this.longFileMode = longFileMode;
   }

   public void setDebug(boolean debugF) {
      this.debug = debugF;
   }

   public void setBufferDebug(boolean debug) {
      this.buffer.setDebug(debug);
   }

   public void finish() throws IOException {
      this.writeEOFRecord();
      this.writeEOFRecord();
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.finish();
         this.buffer.close();
         this.out.close();
         this.closed = true;
      }

   }

   public int getRecordSize() {
      return this.buffer.getRecordSize();
   }

   public void putNextEntry(TarEntry entry) throws IOException {
      if (entry.getName().length() >= 100) {
         if (this.longFileMode == 2) {
            TarEntry longLinkEntry = new TarEntry("././@LongLink", (byte)76);
            longLinkEntry.setSize((long)(entry.getName().length() + 1));
            this.putNextEntry(longLinkEntry);
            this.write(entry.getName().getBytes());
            this.write(0);
            this.closeEntry();
         } else if (this.longFileMode != 1) {
            throw new RuntimeException("file name '" + entry.getName() + "' is too long ( > " + 100 + " bytes)");
         }
      }

      entry.writeEntryHeader(this.recordBuf);
      this.buffer.writeRecord(this.recordBuf);
      this.currBytes = 0L;
      if (entry.isDirectory()) {
         this.currSize = 0L;
      } else {
         this.currSize = entry.getSize();
      }

      this.currName = entry.getName();
   }

   public void closeEntry() throws IOException {
      if (this.assemLen > 0) {
         for(int i = this.assemLen; i < this.assemBuf.length; ++i) {
            this.assemBuf[i] = 0;
         }

         this.buffer.writeRecord(this.assemBuf);
         this.currBytes += (long)this.assemLen;
         this.assemLen = 0;
      }

      if (this.currBytes < this.currSize) {
         throw new IOException("entry '" + this.currName + "' closed at '" + this.currBytes + "' before the '" + this.currSize + "' bytes specified in the header were written");
      }
   }

   public void write(int b) throws IOException {
      this.oneBuf[0] = (byte)b;
      this.write(this.oneBuf, 0, 1);
   }

   public void write(byte[] wBuf) throws IOException {
      this.write(wBuf, 0, wBuf.length);
   }

   public void write(byte[] wBuf, int wOffset, int numToWrite) throws IOException {
      if (this.currBytes + (long)numToWrite > this.currSize) {
         throw new IOException("request to write '" + numToWrite + "' bytes exceeds size in header of '" + this.currSize + "' bytes for entry '" + this.currName + "'");
      } else {
         int num;
         if (this.assemLen > 0) {
            if (this.assemLen + numToWrite >= this.recordBuf.length) {
               num = this.recordBuf.length - this.assemLen;
               System.arraycopy(this.assemBuf, 0, this.recordBuf, 0, this.assemLen);
               System.arraycopy(wBuf, wOffset, this.recordBuf, this.assemLen, num);
               this.buffer.writeRecord(this.recordBuf);
               this.currBytes += (long)this.recordBuf.length;
               wOffset += num;
               numToWrite -= num;
               this.assemLen = 0;
            } else {
               System.arraycopy(wBuf, wOffset, this.assemBuf, this.assemLen, numToWrite);
               wOffset += numToWrite;
               this.assemLen += numToWrite;
               numToWrite -= numToWrite;
            }
         }

         while(numToWrite > 0) {
            if (numToWrite < this.recordBuf.length) {
               System.arraycopy(wBuf, wOffset, this.assemBuf, this.assemLen, numToWrite);
               this.assemLen += numToWrite;
               break;
            }

            this.buffer.writeRecord(wBuf, wOffset);
            num = this.recordBuf.length;
            this.currBytes += (long)num;
            numToWrite -= num;
            wOffset += num;
         }

      }
   }

   private void writeEOFRecord() throws IOException {
      for(int i = 0; i < this.recordBuf.length; ++i) {
         this.recordBuf[i] = 0;
      }

      this.buffer.writeRecord(this.recordBuf);
   }
}
