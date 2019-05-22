package org.apache.maven.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PluginExecution extends ConfigurationContainer implements Serializable {
   private String id = "default";
   private String phase;
   private List<String> goals;
   public static final String DEFAULT_EXECUTION_ID = "default";

   public void addGoal(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("PluginExecution.addGoals(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getGoals().add(string);
      }
   }

   public List<String> getGoals() {
      if (this.goals == null) {
         this.goals = new ArrayList();
      }

      return this.goals;
   }

   public String getId() {
      return this.id;
   }

   public String getPhase() {
      return this.phase;
   }

   public void removeGoal(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("PluginExecution.removeGoals(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getGoals().remove(string);
      }
   }

   public void setGoals(List<String> goals) {
      this.goals = goals;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setPhase(String phase) {
      this.phase = phase;
   }
}
