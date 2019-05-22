package org.apache.maven.wagon;

import java.io.InputStream;
import org.apache.maven.wagon.resource.Resource;

public class InputData {
   private InputStream inputStream;
   private Resource resource;

   public InputStream getInputStream() {
      return this.inputStream;
   }

   public void setInputStream(InputStream inputStream) {
      this.inputStream = inputStream;
   }

   public Resource getResource() {
      return this.resource;
   }

   public void setResource(Resource resource) {
      this.resource = resource;
   }
}
