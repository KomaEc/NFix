package org.apache.tools.ant.taskdefs;

public class Taskdef extends Typedef {
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$TaskAdapter;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$Task;

   public Taskdef() {
      this.setAdapterClass(class$org$apache$tools$ant$TaskAdapter == null ? (class$org$apache$tools$ant$TaskAdapter = class$("org.apache.tools.ant.TaskAdapter")) : class$org$apache$tools$ant$TaskAdapter);
      this.setAdaptToClass(class$org$apache$tools$ant$Task == null ? (class$org$apache$tools$ant$Task = class$("org.apache.tools.ant.Task")) : class$org$apache$tools$ant$Task);
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
