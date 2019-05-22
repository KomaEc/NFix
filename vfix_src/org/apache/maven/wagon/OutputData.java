package org.apache.maven.wagon;

import java.io.OutputStream;
import org.apache.maven.wagon.resource.Resource;

public class OutputData {
   private OutputStream outputStream;
   private Resource resource;

   public OutputStream getOutputStream() {
      return this.outputStream;
   }

   public void setOutputStream(OutputStream outputStream) {
      this.outputStream = outputStream;
   }

   public Resource getResource() {
      return this.resource;
   }

   public void setResource(Resource resource) {
      this.resource = resource;
   }
}
