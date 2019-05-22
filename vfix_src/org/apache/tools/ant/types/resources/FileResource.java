package org.apache.tools.ant.types.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.FileUtils;

public class FileResource extends Resource implements Touchable {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private static final int NULL_FILE = Resource.getMagicNumber("null file".getBytes());
   private File file;
   private File baseDir;

   public FileResource() {
   }

   public FileResource(File b, String name) {
      this.setFile(FILE_UTILS.resolveFile(b, name));
      this.setBaseDir(b);
   }

   public FileResource(File f) {
      this.setFile(f);
   }

   public FileResource(Project p, String s) {
      this(p.resolveFile(s));
      this.setProject(p);
   }

   public void setFile(File f) {
      this.checkAttributesAllowed();
      this.file = f;
   }

   public File getFile() {
      return this.isReference() ? ((FileResource)this.getCheckedRef()).getFile() : this.file;
   }

   public void setBaseDir(File b) {
      this.checkAttributesAllowed();
      this.baseDir = b;
   }

   public File getBaseDir() {
      return this.isReference() ? ((FileResource)this.getCheckedRef()).getBaseDir() : this.baseDir;
   }

   public void setRefid(Reference r) {
      if (this.file == null && this.baseDir == null) {
         super.setRefid(r);
      } else {
         throw this.tooManyAttributes();
      }
   }

   public String getName() {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getName();
      } else {
         File b = this.getBaseDir();
         return b == null ? this.getNotNullFile().getName() : FILE_UTILS.removeLeadingPath(b, this.getNotNullFile());
      }
   }

   public boolean isExists() {
      return this.isReference() ? ((Resource)this.getCheckedRef()).isExists() : this.getNotNullFile().exists();
   }

   public long getLastModified() {
      return this.isReference() ? ((Resource)this.getCheckedRef()).getLastModified() : this.getNotNullFile().lastModified();
   }

   public boolean isDirectory() {
      return this.isReference() ? ((Resource)this.getCheckedRef()).isDirectory() : this.getNotNullFile().isDirectory();
   }

   public long getSize() {
      return this.isReference() ? ((Resource)this.getCheckedRef()).getSize() : this.getNotNullFile().length();
   }

   public InputStream getInputStream() throws IOException {
      return (InputStream)(this.isReference() ? ((Resource)this.getCheckedRef()).getInputStream() : new FileInputStream(this.getNotNullFile()));
   }

   public OutputStream getOutputStream() throws IOException {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getOutputStream();
      } else {
         File f = this.getNotNullFile();
         if (f.exists()) {
            if (f.isFile()) {
               f.delete();
            }
         } else {
            File p = f.getParentFile();
            if (p != null && !p.exists()) {
               p.mkdirs();
            }
         }

         return new FileOutputStream(f);
      }
   }

   public int compareTo(Object another) {
      if (this.isReference()) {
         return ((Comparable)this.getCheckedRef()).compareTo(another);
      } else if (this.equals(another)) {
         return 0;
      } else if (another.getClass().equals(this.getClass())) {
         FileResource otherfr = (FileResource)another;
         File f = this.getFile();
         if (f == null) {
            return -1;
         } else {
            File of = otherfr.getFile();
            return of == null ? 1 : f.compareTo(of);
         }
      } else {
         return super.compareTo(another);
      }
   }

   public boolean equals(Object another) {
      if (this == another) {
         return true;
      } else if (this.isReference()) {
         return this.getCheckedRef().equals(another);
      } else if (!another.getClass().equals(this.getClass())) {
         return false;
      } else {
         FileResource otherfr = (FileResource)another;
         return this.getFile() == null ? otherfr.getFile() == null : this.getFile().equals(otherfr.getFile());
      }
   }

   public int hashCode() {
      return this.isReference() ? this.getCheckedRef().hashCode() : MAGIC * (this.getFile() == null ? NULL_FILE : this.getFile().hashCode());
   }

   public String toString() {
      if (this.isReference()) {
         return this.getCheckedRef().toString();
      } else if (this.file == null) {
         return "(unbound file resource)";
      } else {
         String absolutePath = this.file.getAbsolutePath();
         return FILE_UTILS.normalize(absolutePath).getAbsolutePath();
      }
   }

   public boolean isFilesystemOnly() {
      return !this.isReference() || ((FileResource)this.getCheckedRef()).isFilesystemOnly();
   }

   public void touch(long modTime) {
      if (this.isReference()) {
         ((FileResource)this.getCheckedRef()).touch(modTime);
      } else {
         this.getNotNullFile().setLastModified(modTime);
      }
   }

   protected File getNotNullFile() {
      if (this.getFile() == null) {
         throw new BuildException("file attribute is null!");
      } else {
         return this.getFile();
      }
   }
}
