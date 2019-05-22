package org.apache.maven.settings;

import java.io.Serializable;

public class ActivationProperty implements Serializable {
   private String name;
   private String value;

   public String getName() {
      return this.name;
   }

   public String getValue() {
      return this.value;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setValue(String value) {
      this.value = value;
   }
}
