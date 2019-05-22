package org.apache.tools.ant.types.spi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

public class Service extends ProjectComponent {
   private List providerList = new ArrayList();
   private String type;

   public void setProvider(String className) {
      Provider provider = new Provider();
      provider.setClassName(className);
      this.providerList.add(provider);
   }

   public void addConfiguredProvider(Provider provider) {
      provider.check();
      this.providerList.add(provider);
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public InputStream getAsStream() throws IOException {
      ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
      Writer writer = new OutputStreamWriter(arrayOut, "UTF-8");
      Iterator providerIterator = this.providerList.iterator();

      while(providerIterator.hasNext()) {
         Provider provider = (Provider)providerIterator.next();
         writer.write(provider.getClassName());
         writer.write("\n");
      }

      writer.close();
      return new ByteArrayInputStream(arrayOut.toByteArray());
   }

   public void check() {
      if (this.type == null) {
         throw new BuildException("type attribute must be set for service element", this.getLocation());
      } else if (this.type.length() == 0) {
         throw new BuildException("Invalid empty type classname", this.getLocation());
      } else if (this.providerList.size() == 0) {
         throw new BuildException("provider attribute or nested provider element must be set!", this.getLocation());
      }
   }
}
