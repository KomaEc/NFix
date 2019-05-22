package org.apache.tools.ant.types.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;

public class StringResource extends Resource {
   private static final int STRING_MAGIC = Resource.getMagicNumber("StringResource".getBytes());
   private String encoding = null;

   public StringResource() {
   }

   public StringResource(String value) {
      this.setValue(value);
   }

   public synchronized void setName(String s) {
      if (this.getName() != null) {
         throw new BuildException(new ImmutableResourceException());
      } else {
         super.setName(s);
      }
   }

   public synchronized void setValue(String s) {
      this.setName(s);
   }

   public synchronized String getName() {
      return super.getName();
   }

   public synchronized String getValue() {
      return this.getName();
   }

   public synchronized void setEncoding(String s) {
      this.encoding = s;
   }

   public synchronized String getEncoding() {
      return this.encoding;
   }

   public synchronized long getSize() {
      return this.isReference() ? ((Resource)this.getCheckedRef()).getSize() : (long)this.getContent().length();
   }

   public synchronized int hashCode() {
      return this.isReference() ? this.getCheckedRef().hashCode() : super.hashCode() * STRING_MAGIC;
   }

   public String toString() {
      return this.isReference() ? this.getCheckedRef().toString() : String.valueOf(this.getContent());
   }

   public synchronized InputStream getInputStream() throws IOException {
      return (InputStream)(this.isReference() ? ((Resource)this.getCheckedRef()).getInputStream() : new ByteArrayInputStream(this.getContent().getBytes()));
   }

   public synchronized OutputStream getOutputStream() throws IOException {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getOutputStream();
      } else if (this.getValue() != null) {
         throw new ImmutableResourceException();
      } else {
         final ByteArrayOutputStream baos = new ByteArrayOutputStream();
         return new FilterOutputStream(baos) {
            public void close() throws IOException {
               super.close();
               StringResource.this.setValue(StringResource.this.encoding == null ? baos.toString() : baos.toString(StringResource.this.encoding));
            }
         };
      }
   }

   public void setRefid(Reference r) {
      if (this.encoding != null) {
         throw this.tooManyAttributes();
      } else {
         super.setRefid(r);
      }
   }

   protected synchronized String getContent() {
      if (this.isReference()) {
         return ((StringResource)this.getCheckedRef()).getContent();
      } else {
         String value = this.getValue();
         if (value == null) {
            return value;
         } else {
            return this.getProject() == null ? value : this.getProject().replaceProperties(value);
         }
      }
   }
}
