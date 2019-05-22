package org.apache.tools.tar;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TarInputStream extends FilterInputStream {
   protected boolean debug;
   protected boolean hasHitEOF;
   protected long entrySize;
   protected long entryOffset;
   protected byte[] readBuf;
   protected TarBuffer buffer;
   protected TarEntry currEntry;
   protected byte[] oneBuf;

   public TarInputStream(InputStream is) {
      this(is, 10240, 512);
   }

   public TarInputStream(InputStream is, int blockSize) {
      this(is, blockSize, 512);
   }

   public TarInputStream(InputStream is, int blockSize, int recordSize) {
      super(is);
      this.buffer = new TarBuffer(is, blockSize, recordSize);
      this.readBuf = null;
      this.oneBuf = new byte[1];
      this.debug = false;
      this.hasHitEOF = false;
   }

   public void setDebug(boolean debug) {
      this.debug = debug;
      this.buffer.setDebug(debug);
   }

   public void close() throws IOException {
      this.buffer.close();
   }

   public int getRecordSize() {
      return this.buffer.getRecordSize();
   }

   public int available() throws IOException {
      return this.entrySize - this.entryOffset > 2147483647L ? Integer.MAX_VALUE : (int)(this.entrySize - this.entryOffset);
   }

   public long skip(long numToSkip) throws IOException {
      byte[] skipBuf = new byte[8192];

      long skip;
      int numRead;
      for(skip = numToSkip; skip > 0L; skip -= (long)numRead) {
         int realSkip = (int)(skip > (long)skipBuf.length ? (long)skipBuf.length : skip);
         numRead = this.read(skipBuf, 0, realSkip);
         if (numRead == -1) {
            break;
         }
      }

      return numToSkip - skip;
   }

   public boolean markSupported() {
      return false;
   }

   public void mark(int markLimit) {
   }

   public void reset() {
   }

   public TarEntry getNextEntry() throws IOException {
      if (this.hasHitEOF) {
         return null;
      } else {
         if (this.currEntry != null) {
            long numToSkip = this.entrySize - this.entryOffset;
            if (this.debug) {
               System.err.println("TarInputStream: SKIP currENTRY '" + this.currEntry.getName() + "' SZ " + this.entrySize + " OFF " + this.entryOffset + "  skipping " + numToSkip + " bytes");
            }

            if (numToSkip > 0L) {
               this.skip(numToSkip);
            }

            this.readBuf = null;
         }

         byte[] headerBuf = this.buffer.readRecord();
         if (headerBuf == null) {
            if (this.debug) {
               System.err.println("READ NULL RECORD");
            }

            this.hasHitEOF = true;
         } else if (this.buffer.isEOFRecord(headerBuf)) {
            if (this.debug) {
               System.err.println("READ EOF RECORD");
            }

            this.hasHitEOF = true;
         }

         if (this.hasHitEOF) {
            this.currEntry = null;
         } else {
            this.currEntry = new TarEntry(headerBuf);
            if (this.debug) {
               System.err.println("TarInputStream: SET CURRENTRY '" + this.currEntry.getName() + "' size = " + this.currEntry.getSize());
            }

            this.entryOffset = 0L;
            this.entrySize = this.currEntry.getSize();
         }

         if (this.currEntry != null && this.currEntry.isGNULongNameEntry()) {
            StringBuffer longName = new StringBuffer();
            byte[] buf = new byte[256];
            boolean var6 = false;

            int length;
            while((length = this.read(buf)) >= 0) {
               longName.append(new String(buf, 0, length));
            }

            this.getNextEntry();
            if (this.currEntry == null) {
               return null;
            }

            if (longName.length() > 0 && longName.charAt(longName.length() - 1) == 0) {
               longName.deleteCharAt(longName.length() - 1);
            }

            this.currEntry.setName(longName.toString());
         }

         return this.currEntry;
      }
   }

   public int read() throws IOException {
      int num = this.read(this.oneBuf, 0, 1);
      return num == -1 ? -1 : this.oneBuf[0] & 255;
   }

   public int read(byte[] buf, int offset, int numToRead) throws IOException {
      int totalRead = 0;
      if (this.entryOffset >= this.entrySize) {
         return -1;
      } else {
         if ((long)numToRead + this.entryOffset > this.entrySize) {
            numToRead = (int)(this.entrySize - this.entryOffset);
         }

         int sz;
         if (this.readBuf != null) {
            int sz = numToRead > this.readBuf.length ? this.readBuf.length : numToRead;
            System.arraycopy(this.readBuf, 0, buf, offset, sz);
            if (sz >= this.readBuf.length) {
               this.readBuf = null;
            } else {
               sz = this.readBuf.length - sz;
               byte[] newBuf = new byte[sz];
               System.arraycopy(this.readBuf, sz, newBuf, 0, sz);
               this.readBuf = newBuf;
            }

            totalRead += sz;
            numToRead -= sz;
            offset += sz;
         }

         while(numToRead > 0) {
            byte[] rec = this.buffer.readRecord();
            if (rec == null) {
               throw new IOException("unexpected EOF with " + numToRead + " bytes unread");
            }

            sz = numToRead;
            int recLen = rec.length;
            if (recLen > numToRead) {
               System.arraycopy(rec, 0, buf, offset, numToRead);
               this.readBuf = new byte[recLen - numToRead];
               System.arraycopy(rec, numToRead, this.readBuf, 0, recLen - numToRead);
            } else {
               sz = recLen;
               System.arraycopy(rec, 0, buf, offset, recLen);
            }

            totalRead += sz;
            numToRead -= sz;
            offset += sz;
         }

         this.entryOffset += (long)totalRead;
         return totalRead;
      }
   }

   public void copyEntryContents(OutputStream out) throws IOException {
      byte[] buf = new byte['耀'];

      while(true) {
         int numRead = this.read(buf, 0, buf.length);
         if (numRead == -1) {
            return;
         }

         out.write(buf, 0, numRead);
      }
   }
}
