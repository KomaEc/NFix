package org.apache.tools.tar;

import java.io.File;
import java.util.Date;
import java.util.Locale;

public class TarEntry implements TarConstants {
   private StringBuffer name;
   private int mode;
   private int userId;
   private int groupId;
   private long size;
   private long modTime;
   private byte linkFlag;
   private StringBuffer linkName;
   private StringBuffer magic;
   private StringBuffer userName;
   private StringBuffer groupName;
   private int devMajor;
   private int devMinor;
   private File file;
   public static final int MAX_NAMELEN = 31;
   public static final int DEFAULT_DIR_MODE = 16877;
   public static final int DEFAULT_FILE_MODE = 33188;
   public static final int MILLIS_PER_SECOND = 1000;

   private TarEntry() {
      this.magic = new StringBuffer("ustar");
      this.name = new StringBuffer();
      this.linkName = new StringBuffer();
      String user = System.getProperty("user.name", "");
      if (user.length() > 31) {
         user = user.substring(0, 31);
      }

      this.userId = 0;
      this.groupId = 0;
      this.userName = new StringBuffer(user);
      this.groupName = new StringBuffer("");
      this.file = null;
   }

   public TarEntry(String name) {
      this();
      boolean isDir = name.endsWith("/");
      this.devMajor = 0;
      this.devMinor = 0;
      this.name = new StringBuffer(name);
      this.mode = isDir ? 16877 : '膤';
      this.linkFlag = (byte)(isDir ? 53 : 48);
      this.userId = 0;
      this.groupId = 0;
      this.size = 0L;
      this.modTime = (new Date()).getTime() / 1000L;
      this.linkName = new StringBuffer("");
      this.userName = new StringBuffer("");
      this.groupName = new StringBuffer("");
      this.devMajor = 0;
      this.devMinor = 0;
   }

   public TarEntry(String name, byte linkFlag) {
      this(name);
      this.linkFlag = linkFlag;
   }

   public TarEntry(File file) {
      this();
      this.file = file;
      String fileName = file.getPath();
      String osname = System.getProperty("os.name").toLowerCase(Locale.US);
      if (osname != null) {
         if (osname.startsWith("windows")) {
            if (fileName.length() > 2) {
               char ch1 = fileName.charAt(0);
               char ch2 = fileName.charAt(1);
               if (ch2 == ':' && (ch1 >= 'a' && ch1 <= 'z' || ch1 >= 'A' && ch1 <= 'Z')) {
                  fileName = fileName.substring(2);
               }
            }
         } else if (osname.indexOf("netware") > -1) {
            int colon = fileName.indexOf(58);
            if (colon != -1) {
               fileName = fileName.substring(colon + 1);
            }
         }
      }

      for(fileName = fileName.replace(File.separatorChar, '/'); fileName.startsWith("/"); fileName = fileName.substring(1)) {
      }

      this.linkName = new StringBuffer("");
      this.name = new StringBuffer(fileName);
      if (file.isDirectory()) {
         this.mode = 16877;
         this.linkFlag = 53;
         if (this.name.charAt(this.name.length() - 1) != '/') {
            this.name.append("/");
         }
      } else {
         this.mode = 33188;
         this.linkFlag = 48;
      }

      this.size = file.length();
      this.modTime = file.lastModified() / 1000L;
      this.devMajor = 0;
      this.devMinor = 0;
   }

   public TarEntry(byte[] headerBuf) {
      this();
      this.parseTarHeader(headerBuf);
   }

   public boolean equals(TarEntry it) {
      return this.getName().equals(it.getName());
   }

   public boolean equals(Object it) {
      return it != null && this.getClass() == it.getClass() ? this.equals((TarEntry)it) : false;
   }

   public int hashCode() {
      return this.getName().hashCode();
   }

   public boolean isDescendent(TarEntry desc) {
      return desc.getName().startsWith(this.getName());
   }

   public String getName() {
      return this.name.toString();
   }

   public void setName(String name) {
      this.name = new StringBuffer(name);
   }

   public void setMode(int mode) {
      this.mode = mode;
   }

   public String getLinkName() {
      return this.linkName.toString();
   }

   public int getUserId() {
      return this.userId;
   }

   public void setUserId(int userId) {
      this.userId = userId;
   }

   public int getGroupId() {
      return this.groupId;
   }

   public void setGroupId(int groupId) {
      this.groupId = groupId;
   }

   public String getUserName() {
      return this.userName.toString();
   }

   public void setUserName(String userName) {
      this.userName = new StringBuffer(userName);
   }

   public String getGroupName() {
      return this.groupName.toString();
   }

   public void setGroupName(String groupName) {
      this.groupName = new StringBuffer(groupName);
   }

   public void setIds(int userId, int groupId) {
      this.setUserId(userId);
      this.setGroupId(groupId);
   }

   public void setNames(String userName, String groupName) {
      this.setUserName(userName);
      this.setGroupName(groupName);
   }

   public void setModTime(long time) {
      this.modTime = time / 1000L;
   }

   public void setModTime(Date time) {
      this.modTime = time.getTime() / 1000L;
   }

   public Date getModTime() {
      return new Date(this.modTime * 1000L);
   }

   public File getFile() {
      return this.file;
   }

   public int getMode() {
      return this.mode;
   }

   public long getSize() {
      return this.size;
   }

   public void setSize(long size) {
      this.size = size;
   }

   public boolean isGNULongNameEntry() {
      return this.linkFlag == 76 && this.name.toString().equals("././@LongLink");
   }

   public boolean isDirectory() {
      if (this.file != null) {
         return this.file.isDirectory();
      } else if (this.linkFlag == 53) {
         return true;
      } else {
         return this.getName().endsWith("/");
      }
   }

   public TarEntry[] getDirectoryEntries() {
      if (this.file != null && this.file.isDirectory()) {
         String[] list = this.file.list();
         TarEntry[] result = new TarEntry[list.length];

         for(int i = 0; i < list.length; ++i) {
            result[i] = new TarEntry(new File(this.file, list[i]));
         }

         return result;
      } else {
         return new TarEntry[0];
      }
   }

   public void writeEntryHeader(byte[] outbuf) {
      int offset = 0;
      int offset = TarUtils.getNameBytes(this.name, outbuf, offset, 100);
      offset = TarUtils.getOctalBytes((long)this.mode, outbuf, offset, 8);
      offset = TarUtils.getOctalBytes((long)this.userId, outbuf, offset, 8);
      offset = TarUtils.getOctalBytes((long)this.groupId, outbuf, offset, 8);
      offset = TarUtils.getLongOctalBytes(this.size, outbuf, offset, 12);
      offset = TarUtils.getLongOctalBytes(this.modTime, outbuf, offset, 12);
      int csOffset = offset;

      for(int c = 0; c < 8; ++c) {
         outbuf[offset++] = 32;
      }

      outbuf[offset++] = this.linkFlag;
      offset = TarUtils.getNameBytes(this.linkName, outbuf, offset, 100);
      offset = TarUtils.getNameBytes(this.magic, outbuf, offset, 8);
      offset = TarUtils.getNameBytes(this.userName, outbuf, offset, 32);
      offset = TarUtils.getNameBytes(this.groupName, outbuf, offset, 32);
      offset = TarUtils.getOctalBytes((long)this.devMajor, outbuf, offset, 8);

      for(offset = TarUtils.getOctalBytes((long)this.devMinor, outbuf, offset, 8); offset < outbuf.length; outbuf[offset++] = 0) {
      }

      long chk = TarUtils.computeCheckSum(outbuf);
      TarUtils.getCheckSumOctalBytes(chk, outbuf, csOffset, 8);
   }

   public void parseTarHeader(byte[] header) {
      int offset = 0;
      this.name = TarUtils.parseName(header, offset, 100);
      int offset = offset + 100;
      this.mode = (int)TarUtils.parseOctal(header, offset, 8);
      offset += 8;
      this.userId = (int)TarUtils.parseOctal(header, offset, 8);
      offset += 8;
      this.groupId = (int)TarUtils.parseOctal(header, offset, 8);
      offset += 8;
      this.size = TarUtils.parseOctal(header, offset, 12);
      offset += 12;
      this.modTime = TarUtils.parseOctal(header, offset, 12);
      offset += 12;
      offset += 8;
      this.linkFlag = header[offset++];
      this.linkName = TarUtils.parseName(header, offset, 100);
      offset += 100;
      this.magic = TarUtils.parseName(header, offset, 8);
      offset += 8;
      this.userName = TarUtils.parseName(header, offset, 32);
      offset += 32;
      this.groupName = TarUtils.parseName(header, offset, 32);
      offset += 32;
      this.devMajor = (int)TarUtils.parseOctal(header, offset, 8);
      offset += 8;
      this.devMinor = (int)TarUtils.parseOctal(header, offset, 8);
   }
}
