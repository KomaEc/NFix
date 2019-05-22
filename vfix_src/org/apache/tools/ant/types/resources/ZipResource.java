package org.apache.tools.ant.types.resources;

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class ZipResource extends ArchiveResource {
   private String encoding;

   public ZipResource() {
   }

   public ZipResource(File z, String enc, ZipEntry e) {
      super(z, true);
      this.setEncoding(enc);
      this.setEntry(e);
   }

   public void setZipfile(File z) {
      this.setArchive(z);
   }

   public File getZipfile() {
      FileResource r = (FileResource)this.getArchive();
      return r.getFile();
   }

   public void addConfigured(ResourceCollection a) {
      super.addConfigured(a);
      if (!a.isFilesystemOnly()) {
         throw new BuildException("only filesystem resources are supported");
      }
   }

   public void setEncoding(String enc) {
      this.checkAttributesAllowed();
      this.encoding = enc;
   }

   public String getEncoding() {
      return this.isReference() ? ((ZipResource)this.getCheckedRef()).getEncoding() : this.encoding;
   }

   public void setRefid(Reference r) {
      if (this.encoding != null) {
         throw this.tooManyAttributes();
      } else {
         super.setRefid(r);
      }
   }

   public InputStream getInputStream() throws IOException {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getInputStream();
      } else {
         final ZipFile z = new ZipFile(this.getZipfile(), this.getEncoding());
         ZipEntry ze = z.getEntry(this.getName());
         if (ze == null) {
            z.close();
            throw new BuildException("no entry " + this.getName() + " in " + this.getArchive());
         } else {
            return new FilterInputStream(z.getInputStream(ze)) {
               public void close() throws IOException {
                  FileUtils.close(this.in);
                  z.close();
               }

               protected void finalize() throws Throwable {
                  try {
                     this.close();
                  } finally {
                     super.finalize();
                  }

               }
            };
         }
      }
   }

   public OutputStream getOutputStream() throws IOException {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getOutputStream();
      } else {
         throw new UnsupportedOperationException("Use the zip task for zip output.");
      }
   }

   protected void fetchEntry() {
      ZipFile z = null;

      try {
         z = new ZipFile(this.getZipfile(), this.getEncoding());
         this.setEntry(z.getEntry(this.getName()));
      } catch (IOException var10) {
         this.log(var10.getMessage(), 4);
         throw new BuildException(var10);
      } finally {
         if (z != null) {
            try {
               z.close();
            } catch (IOException var9) {
            }
         }

      }

   }

   private void setEntry(ZipEntry e) {
      if (e == null) {
         this.setExists(false);
      } else {
         this.setName(e.getName());
         this.setExists(true);
         this.setLastModified(e.getTime());
         this.setDirectory(e.isDirectory());
         this.setSize(e.getSize());
         this.setMode(e.getUnixMode());
      }
   }
}
