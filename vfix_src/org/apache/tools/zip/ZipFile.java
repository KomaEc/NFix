package org.apache.tools.zip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

public class ZipFile {
   private Hashtable entries;
   private Hashtable nameMap;
   private String encoding;
   private RandomAccessFile archive;
   private static final int CFH_LEN = 42;
   private static final int MIN_EOCD_SIZE = 22;
   private static final int CFD_LOCATOR_OFFSET = 16;
   private static final long LFH_OFFSET_FOR_FILENAME_LENGTH = 26L;

   public ZipFile(File f) throws IOException {
      this((File)f, (String)null);
   }

   public ZipFile(String name) throws IOException {
      this((File)(new File(name)), (String)null);
   }

   public ZipFile(String name, String encoding) throws IOException {
      this(new File(name), encoding);
   }

   public ZipFile(File f, String encoding) throws IOException {
      this.entries = new Hashtable(509);
      this.nameMap = new Hashtable(509);
      this.encoding = null;
      this.encoding = encoding;
      this.archive = new RandomAccessFile(f, "r");

      try {
         this.populateFromCentralDirectory();
         this.resolveLocalFileHeaderData();
      } catch (IOException var6) {
         try {
            this.archive.close();
         } catch (IOException var5) {
         }

         throw var6;
      }
   }

   public String getEncoding() {
      return this.encoding;
   }

   public void close() throws IOException {
      this.archive.close();
   }

   public static void closeQuietly(ZipFile zipfile) {
      if (zipfile != null) {
         try {
            zipfile.close();
         } catch (IOException var2) {
         }
      }

   }

   public Enumeration getEntries() {
      return this.entries.keys();
   }

   public ZipEntry getEntry(String name) {
      return (ZipEntry)this.nameMap.get(name);
   }

   public InputStream getInputStream(ZipEntry ze) throws IOException, ZipException {
      ZipFile.OffsetEntry offsetEntry = (ZipFile.OffsetEntry)this.entries.get(ze);
      if (offsetEntry == null) {
         return null;
      } else {
         long start = offsetEntry.dataOffset;
         ZipFile.BoundedInputStream bis = new ZipFile.BoundedInputStream(start, ze.getCompressedSize());
         switch(ze.getMethod()) {
         case 0:
            return bis;
         case 8:
            bis.addDummy();
            return new InflaterInputStream(bis, new Inflater(true));
         default:
            throw new ZipException("Found unsupported compression method " + ze.getMethod());
         }
      }
   }

   private void populateFromCentralDirectory() throws IOException {
      this.positionAtCentralDirectory();
      byte[] cfh = new byte[42];
      byte[] signatureBytes = new byte[4];
      this.archive.readFully(signatureBytes);
      long sig = ZipLong.getValue(signatureBytes);

      for(long cfhSig = ZipLong.getValue(ZipOutputStream.CFH_SIG); sig == cfhSig; sig = ZipLong.getValue(signatureBytes)) {
         this.archive.readFully(cfh);
         int off = 0;
         ZipEntry ze = new ZipEntry();
         int versionMadeBy = ZipShort.getValue(cfh, off);
         int off = off + 2;
         ze.setPlatform(versionMadeBy >> 8 & 15);
         off += 4;
         ze.setMethod(ZipShort.getValue(cfh, off));
         off += 2;
         long time = dosToJavaTime(ZipLong.getValue(cfh, off));
         ze.setTime(time);
         off += 4;
         ze.setCrc(ZipLong.getValue(cfh, off));
         off += 4;
         ze.setCompressedSize(ZipLong.getValue(cfh, off));
         off += 4;
         ze.setSize(ZipLong.getValue(cfh, off));
         off += 4;
         int fileNameLen = ZipShort.getValue(cfh, off);
         off += 2;
         int extraLen = ZipShort.getValue(cfh, off);
         off += 2;
         int commentLen = ZipShort.getValue(cfh, off);
         off += 2;
         off += 2;
         ze.setInternalAttributes(ZipShort.getValue(cfh, off));
         off += 2;
         ze.setExternalAttributes(ZipLong.getValue(cfh, off));
         off += 4;
         byte[] fileName = new byte[fileNameLen];
         this.archive.readFully(fileName);
         ze.setName(this.getString(fileName));
         ZipFile.OffsetEntry offset = new ZipFile.OffsetEntry();
         offset.headerOffset = ZipLong.getValue(cfh, off);
         this.entries.put(ze, offset);
         this.nameMap.put(ze.getName(), ze);
         this.archive.skipBytes(extraLen);
         byte[] comment = new byte[commentLen];
         this.archive.readFully(comment);
         ze.setComment(this.getString(comment));
         this.archive.readFully(signatureBytes);
      }

   }

   private void positionAtCentralDirectory() throws IOException {
      boolean found = false;
      long off = this.archive.length() - 22L;
      byte[] sig;
      if (off >= 0L) {
         this.archive.seek(off);
         sig = ZipOutputStream.EOCD_SIG;

         for(int curr = this.archive.read(); curr != -1; curr = this.archive.read()) {
            if (curr == sig[0]) {
               curr = this.archive.read();
               if (curr == sig[1]) {
                  curr = this.archive.read();
                  if (curr == sig[2]) {
                     curr = this.archive.read();
                     if (curr == sig[3]) {
                        found = true;
                        break;
                     }
                  }
               }
            }

            this.archive.seek(--off);
         }
      }

      if (!found) {
         throw new ZipException("archive is not a ZIP archive");
      } else {
         this.archive.seek(off + 16L);
         sig = new byte[4];
         this.archive.readFully(sig);
         this.archive.seek(ZipLong.getValue(sig));
      }
   }

   private void resolveLocalFileHeaderData() throws IOException {
      Enumeration e = this.getEntries();

      while(e.hasMoreElements()) {
         ZipEntry ze = (ZipEntry)e.nextElement();
         ZipFile.OffsetEntry offsetEntry = (ZipFile.OffsetEntry)this.entries.get(ze);
         long offset = offsetEntry.headerOffset;
         this.archive.seek(offset + 26L);
         byte[] b = new byte[2];
         this.archive.readFully(b);
         int fileNameLen = ZipShort.getValue(b);
         this.archive.readFully(b);
         int extraFieldLen = ZipShort.getValue(b);
         this.archive.skipBytes(fileNameLen);
         byte[] localExtraData = new byte[extraFieldLen];
         this.archive.readFully(localExtraData);
         ze.setExtra(localExtraData);
         offsetEntry.dataOffset = offset + 26L + 2L + 2L + (long)fileNameLen + (long)extraFieldLen;
      }

   }

   protected static Date fromDosTime(ZipLong zipDosTime) {
      long dosTime = zipDosTime.getValue();
      return new Date(dosToJavaTime(dosTime));
   }

   private static long dosToJavaTime(long dosTime) {
      Calendar cal = Calendar.getInstance();
      cal.set(1, (int)(dosTime >> 25 & 127L) + 1980);
      cal.set(2, (int)(dosTime >> 21 & 15L) - 1);
      cal.set(5, (int)(dosTime >> 16) & 31);
      cal.set(11, (int)(dosTime >> 11) & 31);
      cal.set(12, (int)(dosTime >> 5) & 63);
      cal.set(13, (int)(dosTime << 1) & 62);
      return cal.getTime().getTime();
   }

   protected String getString(byte[] bytes) throws ZipException {
      if (this.encoding == null) {
         return new String(bytes);
      } else {
         try {
            return new String(bytes, this.encoding);
         } catch (UnsupportedEncodingException var3) {
            throw new ZipException(var3.getMessage());
         }
      }
   }

   private class BoundedInputStream extends InputStream {
      private long remaining;
      private long loc;
      private boolean addDummyByte = false;

      BoundedInputStream(long start, long remaining) {
         this.remaining = remaining;
         this.loc = start;
      }

      public int read() throws IOException {
         if (this.remaining-- <= 0L) {
            if (this.addDummyByte) {
               this.addDummyByte = false;
               return 0;
            } else {
               return -1;
            }
         } else {
            synchronized(ZipFile.this.archive) {
               ZipFile.this.archive.seek((long)(this.loc++));
               return ZipFile.this.archive.read();
            }
         }
      }

      public int read(byte[] b, int off, int len) throws IOException {
         if (this.remaining <= 0L) {
            if (this.addDummyByte) {
               this.addDummyByte = false;
               b[off] = 0;
               return 1;
            } else {
               return -1;
            }
         } else if (len <= 0) {
            return 0;
         } else {
            if ((long)len > this.remaining) {
               len = (int)this.remaining;
            }

            int retx = true;
            int ret;
            synchronized(ZipFile.this.archive) {
               ZipFile.this.archive.seek(this.loc);
               ret = ZipFile.this.archive.read(b, off, len);
            }

            if (ret > 0) {
               this.loc += (long)ret;
               this.remaining -= (long)ret;
            }

            return ret;
         }
      }

      void addDummy() {
         this.addDummyByte = true;
      }
   }

   private static final class OffsetEntry {
      private long headerOffset;
      private long dataOffset;

      private OffsetEntry() {
         this.headerOffset = -1L;
         this.dataOffset = -1L;
      }

      // $FF: synthetic method
      OffsetEntry(Object x0) {
         this();
      }
   }
}
