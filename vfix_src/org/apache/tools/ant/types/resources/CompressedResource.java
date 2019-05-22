package org.apache.tools.ant.types.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.util.FileUtils;

public abstract class CompressedResource extends Resource {
   private Resource resource;

   public CompressedResource() {
   }

   public CompressedResource(ResourceCollection other) {
      this.addConfigured(other);
   }

   public void addConfigured(ResourceCollection a) {
      this.checkChildrenAllowed();
      if (this.resource != null) {
         throw new BuildException("you must not specify more than one resource");
      } else if (a.size() != 1) {
         throw new BuildException("only single argument resource collections are supported");
      } else {
         this.resource = (Resource)a.iterator().next();
      }
   }

   public String getName() {
      return this.getResource().getName();
   }

   public void setName(String name) throws BuildException {
      throw new BuildException("you can't change the name of a compressed resource");
   }

   public boolean isExists() {
      return this.getResource().isExists();
   }

   public void setExists(boolean exists) {
      throw new BuildException("you can't change the exists state of a  compressed resource");
   }

   public long getLastModified() {
      return this.getResource().getLastModified();
   }

   public void setLastModified(long lastmodified) throws BuildException {
      throw new BuildException("you can't change the timestamp of a  compressed resource");
   }

   public boolean isDirectory() {
      return this.getResource().isDirectory();
   }

   public void setDirectory(boolean directory) throws BuildException {
      throw new BuildException("you can't change the directory state of a  compressed resource");
   }

   public long getSize() {
      if (this.isExists()) {
         InputStream in = null;

         try {
            in = this.getInputStream();
            byte[] buf = new byte[8192];

            int size;
            int readNow;
            for(size = 0; (readNow = in.read(buf, 0, buf.length)) > 0; size += readNow) {
            }

            long var5 = (long)size;
            return var5;
         } catch (IOException var10) {
            throw new BuildException("caught exception while reading " + this.getName(), var10);
         } finally {
            FileUtils.close(in);
         }
      } else {
         return 0L;
      }
   }

   public void setSize(long size) throws BuildException {
      throw new BuildException("you can't change the size of a  compressed resource");
   }

   public int compareTo(Object other) {
      if (other == this) {
         return 0;
      } else {
         return other instanceof CompressedResource ? this.getResource().compareTo(((CompressedResource)other).getResource()) : this.getResource().compareTo(other);
      }
   }

   public int hashCode() {
      return this.getResource().hashCode();
   }

   public InputStream getInputStream() throws IOException {
      InputStream in = this.getResource().getInputStream();
      if (in != null) {
         in = this.wrapStream(in);
      }

      return in;
   }

   public OutputStream getOutputStream() throws IOException {
      OutputStream out = this.getResource().getOutputStream();
      if (out != null) {
         out = this.wrapStream(out);
      }

      return out;
   }

   public boolean isFilesystemOnly() {
      return false;
   }

   public String toString() {
      return this.getCompressionName() + " compressed " + this.getResource().toString();
   }

   public void setRefid(Reference r) {
      if (this.resource != null) {
         throw this.noChildrenAllowed();
      } else {
         super.setRefid(r);
      }
   }

   protected abstract InputStream wrapStream(InputStream var1) throws IOException;

   protected abstract OutputStream wrapStream(OutputStream var1) throws IOException;

   protected abstract String getCompressionName();

   private Resource getResource() {
      if (this.isReference()) {
         return (Resource)this.getCheckedRef();
      } else if (this.resource == null) {
         throw new BuildException("no resource specified");
      } else {
         return this.resource;
      }
   }
}
