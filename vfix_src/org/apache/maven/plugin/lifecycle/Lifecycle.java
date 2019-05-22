package org.apache.maven.plugin.lifecycle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Lifecycle implements Serializable {
   private String id;
   private List phases;
   private String modelEncoding = "UTF-8";

   public void addPhase(Phase phase) {
      this.getPhases().add(phase);
   }

   public String getId() {
      return this.id;
   }

   public List getPhases() {
      if (this.phases == null) {
         this.phases = new ArrayList();
      }

      return this.phases;
   }

   public void removePhase(Phase phase) {
      this.getPhases().remove(phase);
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setPhases(List phases) {
      this.phases = phases;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }
}
