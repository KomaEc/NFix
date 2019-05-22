package org.apache.tools.ant.types.resources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.PropertyOutputStream;

public class PropertyResource extends Resource {
   private static final int PROPERTY_MAGIC = Resource.getMagicNumber("PropertyResource".getBytes());
   private static final InputStream UNSET = new InputStream() {
      public int read() {
         return -1;
      }
   };

   public PropertyResource() {
   }

   public PropertyResource(Project p, String n) {
      super(n);
      this.setProject(p);
   }

   public String getValue() {
      Project p = this.getProject();
      return p == null ? null : p.getProperty(this.getName());
   }

   public boolean isExists() {
      return this.getValue() != null;
   }

   public long getSize() {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getSize();
      } else {
         return this.isExists() ? (long)this.getValue().length() : 0L;
      }
   }

   public int hashCode() {
      return this.isReference() ? this.getCheckedRef().hashCode() : super.hashCode() * PROPERTY_MAGIC;
   }

   public String toString() {
      return this.isReference() ? this.getCheckedRef().toString() : String.valueOf(this.getValue());
   }

   public InputStream getInputStream() throws IOException {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getInputStream();
      } else {
         return (InputStream)(this.isExists() ? new ByteArrayInputStream(this.getValue().getBytes()) : UNSET);
      }
   }

   public OutputStream getOutputStream() throws IOException {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getOutputStream();
      } else if (this.isExists()) {
         throw new ImmutableResourceException();
      } else {
         return new PropertyOutputStream(this.getProject(), this.getName());
      }
   }
}
