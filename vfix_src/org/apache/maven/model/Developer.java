package org.apache.maven.model;

import java.io.Serializable;

public class Developer extends Contributor implements Serializable {
   private String id;

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }
}
