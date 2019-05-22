package soot.jimple.toolkits.base;

public class Zone {
   private String name;

   public Zone(String name) {
      this.name = name;
   }

   public String toString() {
      return "<zone: " + this.name + ">";
   }
}
