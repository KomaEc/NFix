package org.apache.maven.plugin.lifecycle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LifecycleConfiguration implements Serializable {
   private List lifecycles;
   private String modelEncoding = "UTF-8";

   public void addLifecycle(Lifecycle lifecycle) {
      this.getLifecycles().add(lifecycle);
   }

   public List getLifecycles() {
      if (this.lifecycles == null) {
         this.lifecycles = new ArrayList();
      }

      return this.lifecycles;
   }

   public void removeLifecycle(Lifecycle lifecycle) {
      this.getLifecycles().remove(lifecycle);
   }

   public void setLifecycles(List lifecycles) {
      this.lifecycles = lifecycles;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }
}
