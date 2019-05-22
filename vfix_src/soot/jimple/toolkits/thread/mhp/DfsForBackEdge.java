package soot.jimple.toolkits.thread.mhp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.tagkit.Tag;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;

public class DfsForBackEdge {
   private final Map<Object, Object> backEdges = new HashMap();
   private final Set<Object> gray = new HashSet();
   private final Set<Object> black = new HashSet();
   private final DominatorsFinder domFinder;

   DfsForBackEdge(Chain chain, DirectedGraph peg) {
      this.domFinder = new DominatorsFinder(chain, peg);
      Iterator it = chain.iterator();
      this.dfs(it, peg);
      this.testBackEdge();
   }

   private void dfs(Iterator it, DirectedGraph g) {
      while(it.hasNext()) {
         Object s = it.next();
         if (!this.gray.contains(s)) {
            this.visitNode(g, s);
         }
      }

   }

   private void visitNode(DirectedGraph g, Object s) {
      this.gray.add(s);
      Iterator it = g.getSuccsOf(s).iterator();
      if (g.getSuccsOf(s).size() > 0) {
         while(it.hasNext()) {
            Object succ = it.next();
            if (!this.gray.contains(succ)) {
               this.visitNode(g, succ);
            } else if (this.gray.contains(succ) && !this.black.contains(succ)) {
               FlowSet dominators = this.domFinder.getDominatorsOf(s);
               if (dominators.contains(succ)) {
                  System.out.println("s is " + s);
                  System.out.println("succ is " + succ);
                  this.backEdges.put(s, succ);
               }
            }
         }
      }

      this.black.add(s);
   }

   protected Map<Object, Object> getBackEdges() {
      return this.backEdges;
   }

   private void testBackEdge() {
      System.out.println("===test backEdges==");
      Set maps = this.backEdges.entrySet();
      Iterator iter = maps.iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         JPegStmt key = (JPegStmt)entry.getKey();
         Tag tag = (Tag)key.getTags().get(0);
         System.out.println("---key=  " + tag + " " + key);
         JPegStmt value = (JPegStmt)entry.getValue();
         Tag tag1 = (Tag)value.getTags().get(0);
         System.out.println("---value=  " + tag1 + " " + value);
      }

      System.out.println("===test backEdges==end==");
   }
}
