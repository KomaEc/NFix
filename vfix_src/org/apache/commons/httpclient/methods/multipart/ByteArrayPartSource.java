package org.apache.commons.httpclient.methods.multipart;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayPartSource implements PartSource {
   private String fileName;
   private byte[] bytes;

   public ByteArrayPartSource(String fileName, byte[] bytes) {
      this.fileName = fileName;
      this.bytes = bytes;
   }

   public long getLength() {
      return (long)this.bytes.length;
   }

   public String getFileName() {
      return this.fileName;
   }

   public InputStream createInputStream() throws IOException {
      return new ByteArrayInputStream(this.bytes);
   }
}
