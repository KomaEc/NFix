package org.apache.maven.plugin.lifecycle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Execution implements Serializable {
   private Object configuration;
   private List goals;
   private String modelEncoding = "UTF-8";

   public void addGoal(String string) {
      this.getGoals().add(string);
   }

   public Object getConfiguration() {
      return this.configuration;
   }

   public List getGoals() {
      if (this.goals == null) {
         this.goals = new ArrayList();
      }

      return this.goals;
   }

   public void removeGoal(String string) {
      this.getGoals().remove(string);
   }

   public void setConfiguration(Object configuration) {
      this.configuration = configuration;
   }

   public void setGoals(List goals) {
      this.goals = goals;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }
}
