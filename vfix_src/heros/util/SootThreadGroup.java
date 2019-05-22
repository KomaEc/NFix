package heros.util;

public class SootThreadGroup extends ThreadGroup {
   private final Thread startThread;

   public SootThreadGroup() {
      super("Soot Threadgroup");
      if (Thread.currentThread().getThreadGroup() instanceof SootThreadGroup) {
         SootThreadGroup group = (SootThreadGroup)Thread.currentThread().getThreadGroup();
         this.startThread = group.getStarterThread();
      } else {
         this.startThread = Thread.currentThread();
      }

   }

   public Thread getStarterThread() {
      return this.startThread;
   }
}
