package soot.toolkits.graph.interaction;

public class InteractionEvent {
   private int type;
   private Object info;

   public InteractionEvent(int type) {
      this.type(type);
   }

   public InteractionEvent(int type, Object info) {
      this.type(type);
      this.info(info);
   }

   private void type(int t) {
      this.type = t;
   }

   private void info(Object i) {
      this.info = i;
   }

   public int type() {
      return this.type;
   }

   public Object info() {
      return this.info;
   }
}
