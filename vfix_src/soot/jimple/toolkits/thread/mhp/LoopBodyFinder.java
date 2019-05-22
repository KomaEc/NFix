package soot.jimple.toolkits.thread.mhp;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;
import soot.toolkits.graph.DirectedGraph;

public class LoopBodyFinder {
   private final Stack<Object> stack = new Stack();
   private final Set<Set<Object>> loops = new HashSet();

   LoopBodyFinder(Map<Object, Object> backEdges, DirectedGraph g) {
      this.findLoopBody(backEdges, g);
   }

   private void findLoopBody(Map<Object, Object> backEdges, DirectedGraph g) {
      Set maps = backEdges.entrySet();
      Iterator iter = maps.iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         Object tail = entry.getKey();
         Object head = entry.getValue();
         Set<Object> loopBody = this.finder(tail, head, g);
         this.loops.add(loopBody);
      }

   }

   private Set<Object> finder(Object tail, Object head, DirectedGraph g) {
      Set<Object> loop = new HashSet();
      this.stack.empty();
      loop.add(head);
      this.insert(tail, loop);

      while(!this.stack.empty()) {
         Object p = this.stack.pop();
         Iterator predsListIt = g.getPredsOf(p).iterator();

         while(predsListIt.hasNext()) {
            Object pred = predsListIt.next();
            this.insert(pred, loop);
         }
      }

      return loop;
   }

   private void insert(Object m, Set<Object> loop) {
      if (!loop.contains(m)) {
         loop.add(m);
         this.stack.push(m);
      }

   }

   public Set<Set<Object>> getLoopBody() {
      return this.loops;
   }
}
