package heros.fieldsens;

import com.google.common.collect.Lists;
import java.util.LinkedList;

public class Scheduler {
   private LinkedList<Runnable> worklist = Lists.newLinkedList();

   public void schedule(Runnable job) {
      this.worklist.add(job);
   }

   public void runAndAwaitCompletion() {
      while(!this.worklist.isEmpty()) {
         ((Runnable)this.worklist.removeLast()).run();
      }

   }
}
