package org.apache.tools.zip;

import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.zip.ZipException;

public class ZipEntry extends java.util.zip.ZipEntry implements Cloneable {
   private static final int PLATFORM_UNIX = 3;
   private static final int PLATFORM_FAT = 0;
   private int internalAttributes;
   private int platform;
   private long externalAttributes;
   private Vector extraFields;
   private String name;

   public ZipEntry(String name) {
      super(name);
      this.internalAttributes = 0;
      this.platform = 0;
      this.externalAttributes = 0L;
      this.extraFields = null;
      this.name = null;
   }

   public ZipEntry(java.util.zip.ZipEntry entry) throws ZipException {
      super(entry);
      this.internalAttributes = 0;
      this.platform = 0;
      this.externalAttributes = 0L;
      this.extraFields = null;
      this.name = null;
      byte[] extra = entry.getExtra();
      if (extra != null) {
         this.setExtraFields(ExtraFieldUtils.parse(extra));
      } else {
         this.setExtra();
      }

   }

   public ZipEntry(ZipEntry entry) throws ZipException {
      this((java.util.zip.ZipEntry)entry);
      this.setInternalAttributes(entry.getInternalAttributes());
      this.setExternalAttributes(entry.getExternalAttributes());
      this.setExtraFields(entry.getExtraFields());
   }

   protected ZipEntry() {
      super("");
      this.internalAttributes = 0;
      this.platform = 0;
      this.externalAttributes = 0L;
      this.extraFields = null;
      this.name = null;
   }

   public Object clone() {
      ZipEntry e = (ZipEntry)super.clone();
      e.extraFields = this.extraFields != null ? (Vector)this.extraFields.clone() : null;
      e.setInternalAttributes(this.getInternalAttributes());
      e.setExternalAttributes(this.getExternalAttributes());
      e.setExtraFields(this.getExtraFields());
      return e;
   }

   public int getInternalAttributes() {
      return this.internalAttributes;
   }

   public void setInternalAttributes(int value) {
      this.internalAttributes = value;
   }

   public long getExternalAttributes() {
      return this.externalAttributes;
   }

   public void setExternalAttributes(long value) {
      this.externalAttributes = value;
   }

   public void setUnixMode(int mode) {
      this.setExternalAttributes((long)(mode << 16 | ((mode & 128) == 0 ? 1 : 0) | (this.isDirectory() ? 16 : 0)));
      this.platform = 3;
   }

   public int getUnixMode() {
      return (int)(this.getExternalAttributes() >> 16 & 65535L);
   }

   public int getPlatform() {
      return this.platform;
   }

   protected void setPlatform(int platform) {
      this.platform = platform;
   }

   public void setExtraFields(ZipExtraField[] fields) {
      this.extraFields = new Vector();

      for(int i = 0; i < fields.length; ++i) {
         this.extraFields.addElement(fields[i]);
      }

      this.setExtra();
   }

   public ZipExtraField[] getExtraFields() {
      if (this.extraFields == null) {
         return new ZipExtraField[0];
      } else {
         ZipExtraField[] result = new ZipExtraField[this.extraFields.size()];
         this.extraFields.copyInto(result);
         return result;
      }
   }

   public void addExtraField(ZipExtraField ze) {
      if (this.extraFields == null) {
         this.extraFields = new Vector();
      }

      ZipShort type = ze.getHeaderId();
      boolean done = false;
      int i = 0;

      for(int fieldsSize = this.extraFields.size(); !done && i < fieldsSize; ++i) {
         if (((ZipExtraField)this.extraFields.elementAt(i)).getHeaderId().equals(type)) {
            this.extraFields.setElementAt(ze, i);
            done = true;
         }
      }

      if (!done) {
         this.extraFields.addElement(ze);
      }

      this.setExtra();
   }

   public void removeExtraField(ZipShort type) {
      if (this.extraFields == null) {
         this.extraFields = new Vector();
      }

      boolean done = false;
      int i = 0;

      for(int fieldsSize = this.extraFields.size(); !done && i < fieldsSize; ++i) {
         if (((ZipExtraField)this.extraFields.elementAt(i)).getHeaderId().equals(type)) {
            this.extraFields.removeElementAt(i);
            done = true;
         }
      }

      if (!done) {
         throw new NoSuchElementException();
      } else {
         this.setExtra();
      }
   }

   public void setExtra(byte[] extra) throws RuntimeException {
      try {
         this.setExtraFields(ExtraFieldUtils.parse(extra));
      } catch (Exception var3) {
         throw new RuntimeException(var3.getMessage());
      }
   }

   protected void setExtra() {
      super.setExtra(ExtraFieldUtils.mergeLocalFileDataData(this.getExtraFields()));
   }

   public byte[] getLocalFileDataExtra() {
      byte[] extra = this.getExtra();
      return extra != null ? extra : new byte[0];
   }

   public byte[] getCentralDirectoryExtra() {
      return ExtraFieldUtils.mergeCentralDirectoryData(this.getExtraFields());
   }

   /** @deprecated */
   public void setComprSize(long size) {
      this.setCompressedSize(size);
   }

   public String getName() {
      return this.name == null ? super.getName() : this.name;
   }

   public boolean isDirectory() {
      return this.getName().endsWith("/");
   }

   protected void setName(String name) {
      this.name = name;
   }

   public int hashCode() {
      return this.getName().hashCode();
   }

   public boolean equals(Object o) {
      return this == o;
   }
}
