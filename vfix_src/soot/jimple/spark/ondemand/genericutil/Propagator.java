package soot.jimple.spark.ondemand.genericutil;

import java.util.Set;

public class Propagator<T> {
   private final Set<T> marked;
   private final Stack<T> worklist;

   public Propagator(Set<T> marked, Stack<T> worklist) {
      this.marked = marked;
      this.worklist = worklist;
   }

   public void prop(T val) {
      if (this.marked.add(val)) {
         this.worklist.push(val);
      }

   }
}
