package soot.toolkits.graph;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import soot.Body;
import soot.jimple.Stmt;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.jimple.toolkits.annotation.logic.LoopFinder;

public class LoopNestTree extends TreeSet<Loop> {
   public LoopNestTree(Body b) {
      this(computeLoops(b));
   }

   public LoopNestTree(Collection<Loop> loops) {
      super(new LoopNestTree.LoopNestTreeComparator());
      this.addAll(loops);
   }

   private static Collection<Loop> computeLoops(Body b) {
      LoopFinder loopFinder = new LoopFinder();
      loopFinder.transform(b);
      Collection<Loop> loops = loopFinder.loops();
      return loops;
   }

   public boolean hasNestedLoops() {
      LoopNestTree.LoopNestTreeComparator comp = new LoopNestTree.LoopNestTreeComparator();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         Loop loop1 = (Loop)var2.next();
         Iterator var4 = this.iterator();

         while(var4.hasNext()) {
            Loop loop2 = (Loop)var4.next();
            if (comp.compare(loop1, loop2) != 0) {
               return true;
            }
         }
      }

      return false;
   }

   private static class LoopNestTreeComparator implements Comparator<Loop> {
      private LoopNestTreeComparator() {
      }

      public int compare(Loop loop1, Loop loop2) {
         Collection<Stmt> stmts1 = loop1.getLoopStatements();
         Collection<Stmt> stmts2 = loop2.getLoopStatements();
         if (stmts1.equals(stmts2)) {
            assert loop1.getHead().equals(loop2.getHead());

            return 0;
         } else if (stmts1.containsAll(stmts2)) {
            return 1;
         } else {
            return stmts2.containsAll(stmts1) ? -1 : 1;
         }
      }

      // $FF: synthetic method
      LoopNestTreeComparator(Object x0) {
         this();
      }
   }
}
