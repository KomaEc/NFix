package org.apache.tools.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipException;

public class ZipOutputStream extends FilterOutputStream {
   public static final int DEFLATED = 8;
   public static final int DEFAULT_COMPRESSION = -1;
   public static final int STORED = 0;
   private ZipEntry entry;
   private String comment = "";
   private int level = -1;
   private boolean hasCompressionLevelChanged = false;
   private int method = 8;
   private Vector entries = new Vector();
   private CRC32 crc = new CRC32();
   private long written = 0L;
   private long dataStart = 0L;
   private long localDataStart = 0L;
   private long cdOffset = 0L;
   private long cdLength = 0L;
   private static final byte[] ZERO = new byte[]{0, 0};
   private static final byte[] LZERO = new byte[]{0, 0, 0, 0};
   private Hashtable offsets = new Hashtable();
   private String encoding = null;
   protected Deflater def;
   protected byte[] buf;
   private RandomAccessFile raf;
   protected static final byte[] LFH_SIG = ZipLong.getBytes(67324752L);
   protected static final byte[] DD_SIG = ZipLong.getBytes(134695760L);
   protected static final byte[] CFH_SIG = ZipLong.getBytes(33639248L);
   protected static final byte[] EOCD_SIG = ZipLong.getBytes(101010256L);
   private static final byte[] DOS_TIME_MIN = ZipLong.getBytes(8448L);

   public ZipOutputStream(OutputStream out) {
      super(out);
      this.def = new Deflater(this.level, true);
      this.buf = new byte[512];
      this.raf = null;
   }

   public ZipOutputStream(File file) throws IOException {
      super((OutputStream)null);
      this.def = new Deflater(this.level, true);
      this.buf = new byte[512];
      this.raf = null;

      try {
         this.raf = new RandomAccessFile(file, "rw");
         this.raf.setLength(0L);
      } catch (IOException var5) {
         if (this.raf != null) {
            try {
               this.raf.close();
            } catch (IOException var4) {
            }

            this.raf = null;
         }

         this.out = new FileOutputStream(file);
      }

   }

   public boolean isSeekable() {
      return this.raf != null;
   }

   public void setEncoding(String encoding) {
      this.encoding = encoding;
   }

   public String getEncoding() {
      return this.encoding;
   }

   public void finish() throws IOException {
      this.closeEntry();
      this.cdOffset = this.written;
      int i = 0;

      for(int entriesSize = this.entries.size(); i < entriesSize; ++i) {
         this.writeCentralFileHeader((ZipEntry)this.entries.elementAt(i));
      }

      this.cdLength = this.written - this.cdOffset;
      this.writeCentralDirectoryEnd();
      this.offsets.clear();
      this.entries.removeAllElements();
   }

   public void closeEntry() throws IOException {
      if (this.entry != null) {
         long realCrc = this.crc.getValue();
         this.crc.reset();
         long save;
         if (this.entry.getMethod() == 8) {
            this.def.finish();

            while(!this.def.finished()) {
               this.deflate();
            }

            this.entry.setSize(adjustToLong(this.def.getTotalIn()));
            this.entry.setCompressedSize(adjustToLong(this.def.getTotalOut()));
            this.entry.setCrc(realCrc);
            this.def.reset();
            this.written += this.entry.getCompressedSize();
         } else if (this.raf == null) {
            if (this.entry.getCrc() != realCrc) {
               throw new ZipException("bad CRC checksum for entry " + this.entry.getName() + ": " + Long.toHexString(this.entry.getCrc()) + " instead of " + Long.toHexString(realCrc));
            }

            if (this.entry.getSize() != this.written - this.dataStart) {
               throw new ZipException("bad size for entry " + this.entry.getName() + ": " + this.entry.getSize() + " instead of " + (this.written - this.dataStart));
            }
         } else {
            save = this.written - this.dataStart;
            this.entry.setSize(save);
            this.entry.setCompressedSize(save);
            this.entry.setCrc(realCrc);
         }

         if (this.raf != null) {
            save = this.raf.getFilePointer();
            this.raf.seek(this.localDataStart);
            this.writeOut(ZipLong.getBytes(this.entry.getCrc()));
            this.writeOut(ZipLong.getBytes(this.entry.getCompressedSize()));
            this.writeOut(ZipLong.getBytes(this.entry.getSize()));
            this.raf.seek(save);
         }

         this.writeDataDescriptor(this.entry);
         this.entry = null;
      }
   }

   public void putNextEntry(ZipEntry ze) throws IOException {
      this.closeEntry();
      this.entry = ze;
      this.entries.addElement(this.entry);
      if (this.entry.getMethod() == -1) {
         this.entry.setMethod(this.method);
      }

      if (this.entry.getTime() == -1L) {
         this.entry.setTime(System.currentTimeMillis());
      }

      if (this.entry.getMethod() == 0 && this.raf == null) {
         if (this.entry.getSize() == -1L) {
            throw new ZipException("uncompressed size is required for STORED method when not writing to a file");
         }

         if (this.entry.getCrc() == -1L) {
            throw new ZipException("crc checksum is required for STORED method when not writing to a file");
         }

         this.entry.setCompressedSize(this.entry.getSize());
      }

      if (this.entry.getMethod() == 8 && this.hasCompressionLevelChanged) {
         this.def.setLevel(this.level);
         this.hasCompressionLevelChanged = false;
      }

      this.writeLocalFileHeader(this.entry);
   }

   public void setComment(String comment) {
      this.comment = comment;
   }

   public void setLevel(int level) {
      if (level >= -1 && level <= 9) {
         this.hasCompressionLevelChanged = this.level != level;
         this.level = level;
      } else {
         throw new IllegalArgumentException("Invalid compression level: " + level);
      }
   }

   public void setMethod(int method) {
      this.method = method;
   }

   public void write(byte[] b, int offset, int length) throws IOException {
      if (this.entry.getMethod() == 8) {
         if (length > 0 && !this.def.finished()) {
            this.def.setInput(b, offset, length);

            while(!this.def.needsInput()) {
               this.deflate();
            }
         }
      } else {
         this.writeOut(b, offset, length);
         this.written += (long)length;
      }

      this.crc.update(b, offset, length);
   }

   public void write(int b) throws IOException {
      byte[] buff = new byte[]{(byte)(b & 255)};
      this.write(buff, 0, 1);
   }

   public void close() throws IOException {
      this.finish();
      if (this.raf != null) {
         this.raf.close();
      }

      if (this.out != null) {
         this.out.close();
      }

   }

   public void flush() throws IOException {
      if (this.out != null) {
         this.out.flush();
      }

   }

   protected final void deflate() throws IOException {
      int len = this.def.deflate(this.buf, 0, this.buf.length);
      if (len > 0) {
         this.writeOut(this.buf, 0, len);
      }

   }

   protected void writeLocalFileHeader(ZipEntry ze) throws IOException {
      this.offsets.put(ze, ZipLong.getBytes(this.written));
      this.writeOut(LFH_SIG);
      this.written += 4L;
      int zipMethod = ze.getMethod();
      if (zipMethod == 8 && this.raf == null) {
         this.writeOut(ZipShort.getBytes(20));
         this.writeOut(ZipShort.getBytes(8));
      } else {
         this.writeOut(ZipShort.getBytes(10));
         this.writeOut(ZERO);
      }

      this.written += 4L;
      this.writeOut(ZipShort.getBytes(zipMethod));
      this.written += 2L;
      this.writeOut(toDosTime(ze.getTime()));
      this.written += 4L;
      this.localDataStart = this.written;
      if (zipMethod != 8 && this.raf == null) {
         this.writeOut(ZipLong.getBytes(ze.getCrc()));
         this.writeOut(ZipLong.getBytes(ze.getSize()));
         this.writeOut(ZipLong.getBytes(ze.getSize()));
      } else {
         this.writeOut(LZERO);
         this.writeOut(LZERO);
         this.writeOut(LZERO);
      }

      this.written += 12L;
      byte[] name = this.getBytes(ze.getName());
      this.writeOut(ZipShort.getBytes(name.length));
      this.written += 2L;
      byte[] extra = ze.getLocalFileDataExtra();
      this.writeOut(ZipShort.getBytes(extra.length));
      this.written += 2L;
      this.writeOut(name);
      this.written += (long)name.length;
      this.writeOut(extra);
      this.written += (long)extra.length;
      this.dataStart = this.written;
   }

   protected void writeDataDescriptor(ZipEntry ze) throws IOException {
      if (ze.getMethod() == 8 && this.raf == null) {
         this.writeOut(DD_SIG);
         this.writeOut(ZipLong.getBytes(this.entry.getCrc()));
         this.writeOut(ZipLong.getBytes(this.entry.getCompressedSize()));
         this.writeOut(ZipLong.getBytes(this.entry.getSize()));
         this.written += 16L;
      }
   }

   protected void writeCentralFileHeader(ZipEntry ze) throws IOException {
      this.writeOut(CFH_SIG);
      this.written += 4L;
      this.writeOut(ZipShort.getBytes(ze.getPlatform() << 8 | 20));
      this.written += 2L;
      if (ze.getMethod() == 8 && this.raf == null) {
         this.writeOut(ZipShort.getBytes(20));
         this.writeOut(ZipShort.getBytes(8));
      } else {
         this.writeOut(ZipShort.getBytes(10));
         this.writeOut(ZERO);
      }

      this.written += 4L;
      this.writeOut(ZipShort.getBytes(ze.getMethod()));
      this.written += 2L;
      this.writeOut(toDosTime(ze.getTime()));
      this.written += 4L;
      this.writeOut(ZipLong.getBytes(ze.getCrc()));
      this.writeOut(ZipLong.getBytes(ze.getCompressedSize()));
      this.writeOut(ZipLong.getBytes(ze.getSize()));
      this.written += 12L;
      byte[] name = this.getBytes(ze.getName());
      this.writeOut(ZipShort.getBytes(name.length));
      this.written += 2L;
      byte[] extra = ze.getCentralDirectoryExtra();
      this.writeOut(ZipShort.getBytes(extra.length));
      this.written += 2L;
      String comm = ze.getComment();
      if (comm == null) {
         comm = "";
      }

      byte[] commentB = this.getBytes(comm);
      this.writeOut(ZipShort.getBytes(commentB.length));
      this.written += 2L;
      this.writeOut(ZERO);
      this.written += 2L;
      this.writeOut(ZipShort.getBytes(ze.getInternalAttributes()));
      this.written += 2L;
      this.writeOut(ZipLong.getBytes(ze.getExternalAttributes()));
      this.written += 4L;
      this.writeOut((byte[])((byte[])this.offsets.get(ze)));
      this.written += 4L;
      this.writeOut(name);
      this.written += (long)name.length;
      this.writeOut(extra);
      this.written += (long)extra.length;
      this.writeOut(commentB);
      this.written += (long)commentB.length;
   }

   protected void writeCentralDirectoryEnd() throws IOException {
      this.writeOut(EOCD_SIG);
      this.writeOut(ZERO);
      this.writeOut(ZERO);
      byte[] num = ZipShort.getBytes(this.entries.size());
      this.writeOut(num);
      this.writeOut(num);
      this.writeOut(ZipLong.getBytes(this.cdLength));
      this.writeOut(ZipLong.getBytes(this.cdOffset));
      byte[] data = this.getBytes(this.comment);
      this.writeOut(ZipShort.getBytes(data.length));
      this.writeOut(data);
   }

   protected static ZipLong toDosTime(Date time) {
      return new ZipLong(toDosTime(time.getTime()));
   }

   protected static byte[] toDosTime(long t) {
      Date time = new Date(t);
      int year = time.getYear() + 1900;
      if (year < 1980) {
         return DOS_TIME_MIN;
      } else {
         int month = time.getMonth() + 1;
         long value = (long)(year - 1980 << 25 | month << 21 | time.getDate() << 16 | time.getHours() << 11 | time.getMinutes() << 5 | time.getSeconds() >> 1);
         return ZipLong.getBytes(value);
      }
   }

   protected byte[] getBytes(String name) throws ZipException {
      if (this.encoding == null) {
         return name.getBytes();
      } else {
         try {
            return name.getBytes(this.encoding);
         } catch (UnsupportedEncodingException var3) {
            throw new ZipException(var3.getMessage());
         }
      }
   }

   protected final void writeOut(byte[] data) throws IOException {
      this.writeOut(data, 0, data.length);
   }

   protected final void writeOut(byte[] data, int offset, int length) throws IOException {
      if (this.raf != null) {
         this.raf.write(data, offset, length);
      } else {
         this.out.write(data, offset, length);
      }

   }

   protected static long adjustToLong(int i) {
      return i < 0 ? 4294967296L + (long)i : (long)i;
   }
}
