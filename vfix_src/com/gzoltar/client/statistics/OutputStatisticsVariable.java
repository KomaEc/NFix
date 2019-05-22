package com.gzoltar.client.statistics;

import java.io.Serializable;

public class OutputStatisticsVariable implements Serializable {
   private static final long serialVersionUID = 7177539782462499756L;
   private final String name;
   private final Object value;

   public OutputStatisticsVariable(String var1, Object var2) {
      this.name = var1;
      this.value = var2;
   }

   public String getName() {
      return this.name;
   }

   public Object getValue() {
      return this.value;
   }

   public String toString() {
      return this.name + ": " + this.value;
   }
}
