package org.apache.maven.model;

import java.io.Serializable;

public class Profile extends ModelBase implements Serializable {
   private String id;
   private Activation activation;
   private BuildBase build;
   private String source = "pom";

   public Activation getActivation() {
      return this.activation;
   }

   public BuildBase getBuild() {
      return this.build;
   }

   public String getId() {
      return this.id;
   }

   public void setActivation(Activation activation) {
      this.activation = activation;
   }

   public void setBuild(BuildBase build) {
      this.build = build;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setSource(String source) {
      this.source = source;
   }

   public String getSource() {
      return this.source;
   }

   public String toString() {
      return "Profile {id: " + this.getId() + ", source: " + this.getSource() + "}";
   }
}
