package org.apache.maven.plugin.lifecycle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Phase implements Serializable {
   private String id;
   private List executions;
   private Object configuration;
   private String modelEncoding = "UTF-8";

   public void addExecution(Execution execution) {
      this.getExecutions().add(execution);
   }

   public Object getConfiguration() {
      return this.configuration;
   }

   public List getExecutions() {
      if (this.executions == null) {
         this.executions = new ArrayList();
      }

      return this.executions;
   }

   public String getId() {
      return this.id;
   }

   public void removeExecution(Execution execution) {
      this.getExecutions().remove(execution);
   }

   public void setConfiguration(Object configuration) {
      this.configuration = configuration;
   }

   public void setExecutions(List executions) {
      this.executions = executions;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }
}
