package org.apache.tools.ant.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Resource extends DataType implements Cloneable, Comparable, ResourceCollection {
   public static final long UNKNOWN_SIZE = -1L;
   public static final long UNKNOWN_DATETIME = 0L;
   protected static final int MAGIC = getMagicNumber("Resource".getBytes());
   private static final int NULL_NAME = getMagicNumber("null name".getBytes());
   private String name;
   private Boolean exists;
   private Long lastmodified;
   private Boolean directory;
   private Long size;

   protected static int getMagicNumber(byte[] seed) {
      return (new BigInteger(seed)).intValue();
   }

   public Resource() {
      this.name = null;
      this.exists = null;
      this.lastmodified = null;
      this.directory = null;
      this.size = null;
   }

   public Resource(String name) {
      this(name, false, 0L, false);
   }

   public Resource(String name, boolean exists, long lastmodified) {
      this(name, exists, lastmodified, false);
   }

   public Resource(String name, boolean exists, long lastmodified, boolean directory) {
      this(name, exists, lastmodified, directory, -1L);
   }

   public Resource(String name, boolean exists, long lastmodified, boolean directory, long size) {
      this.name = null;
      this.exists = null;
      this.lastmodified = null;
      this.directory = null;
      this.size = null;
      this.name = name;
      this.setName(name);
      this.setExists(exists);
      this.setLastModified(lastmodified);
      this.setDirectory(directory);
      this.setSize(size);
   }

   public String getName() {
      return this.isReference() ? ((Resource)this.getCheckedRef()).getName() : this.name;
   }

   public void setName(String name) {
      this.checkAttributesAllowed();
      this.name = name;
   }

   public boolean isExists() {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).isExists();
      } else {
         return this.exists == null || this.exists;
      }
   }

   public void setExists(boolean exists) {
      this.checkAttributesAllowed();
      this.exists = exists ? Boolean.TRUE : Boolean.FALSE;
   }

   public long getLastModified() {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getLastModified();
      } else if (this.isExists() && this.lastmodified != null) {
         long result = this.lastmodified;
         return result < 0L ? 0L : result;
      } else {
         return 0L;
      }
   }

   public void setLastModified(long lastmodified) {
      this.checkAttributesAllowed();
      this.lastmodified = new Long(lastmodified);
   }

   public boolean isDirectory() {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).isDirectory();
      } else {
         return this.directory != null && this.directory;
      }
   }

   public void setDirectory(boolean directory) {
      this.checkAttributesAllowed();
      this.directory = directory ? Boolean.TRUE : Boolean.FALSE;
   }

   public void setSize(long size) {
      this.checkAttributesAllowed();
      this.size = new Long(size > -1L ? size : -1L);
   }

   public long getSize() {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getSize();
      } else {
         return this.isExists() ? (this.size != null ? this.size : -1L) : 0L;
      }
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new UnsupportedOperationException("CloneNotSupportedException for a Resource caught. Derived classes must support cloning.");
      }
   }

   public int compareTo(Object other) {
      if (this.isReference()) {
         return ((Comparable)this.getCheckedRef()).compareTo(other);
      } else if (!(other instanceof Resource)) {
         throw new IllegalArgumentException("Can only be compared with Resources");
      } else {
         return this.toString().compareTo(other.toString());
      }
   }

   public boolean equals(Object other) {
      if (this.isReference()) {
         return this.getCheckedRef().equals(other);
      } else {
         return other.getClass().equals(this.getClass()) && this.compareTo(other) == 0;
      }
   }

   public int hashCode() {
      if (this.isReference()) {
         return this.getCheckedRef().hashCode();
      } else {
         String name = this.getName();
         return MAGIC * (name == null ? NULL_NAME : name.hashCode());
      }
   }

   public InputStream getInputStream() throws IOException {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getInputStream();
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public OutputStream getOutputStream() throws IOException {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getOutputStream();
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public Iterator iterator() {
      return this.isReference() ? ((Resource)this.getCheckedRef()).iterator() : new Iterator() {
         private boolean done = false;

         public boolean hasNext() {
            return !this.done;
         }

         public Object next() {
            if (this.done) {
               throw new NoSuchElementException();
            } else {
               this.done = true;
               return Resource.this;
            }
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public int size() {
      return this.isReference() ? ((Resource)this.getCheckedRef()).size() : 1;
   }

   public boolean isFilesystemOnly() {
      return this.isReference() && ((Resource)this.getCheckedRef()).isFilesystemOnly();
   }

   public String toString() {
      if (this.isReference()) {
         return this.getCheckedRef().toString();
      } else {
         String n = this.getName();
         return n == null ? "(anonymous)" : n;
      }
   }

   public final String toLongString() {
      return this.isReference() ? ((Resource)this.getCheckedRef()).toLongString() : this.getDataTypeName() + " \"" + this.toString() + '"';
   }

   public void setRefid(Reference r) {
      if (this.name == null && this.exists == null && this.lastmodified == null && this.directory == null && this.size == null) {
         super.setRefid(r);
      } else {
         throw this.tooManyAttributes();
      }
   }
}
