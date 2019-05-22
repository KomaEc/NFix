package soot.jimple.toolkits.annotation.purity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.DirectedGraph;
import soot.util.HashMultiMap;
import soot.util.MultiMap;

public class DirectedCallGraph implements DirectedGraph<SootMethod> {
   private static final Logger logger = LoggerFactory.getLogger(DirectedCallGraph.class);
   protected Set<SootMethod> nodes;
   protected Map<SootMethod, List<SootMethod>> succ;
   protected Map<SootMethod, List<SootMethod>> pred;
   protected List<SootMethod> heads;
   protected List<SootMethod> tails;
   protected int size;

   public DirectedCallGraph(CallGraph cg, SootMethodFilter filter, Iterator<SootMethod> heads, boolean verbose) {
      LinkedList filteredHeads = new LinkedList();

      while(heads.hasNext()) {
         SootMethod m = (SootMethod)heads.next();
         if (m.isConcrete() && filter.want(m)) {
            filteredHeads.add(m);
         }
      }

      this.nodes = new HashSet(filteredHeads);
      MultiMap<SootMethod, SootMethod> s = new HashMultiMap();
      MultiMap<SootMethod, SootMethod> p = new HashMultiMap();
      Set<SootMethod> remain = new HashSet(filteredHeads);
      int nb = 0;
      if (verbose) {
         logger.debug("[AM] dumping method dependencies");
      }

      while(!remain.isEmpty()) {
         Set<SootMethod> newRemain = new HashSet();

         for(Iterator var11 = remain.iterator(); var11.hasNext(); ++nb) {
            SootMethod m = (SootMethod)var11.next();
            if (verbose) {
               logger.debug(" |- " + m.toString() + " calls");
            }

            Iterator itt = cg.edgesOutOf((MethodOrMethodContext)m);

            while(itt.hasNext()) {
               Edge edge = (Edge)itt.next();
               SootMethod mm = edge.tgt();
               boolean keep = mm.isConcrete() && filter.want(mm);
               if (verbose) {
                  logger.debug(" |  |- " + mm.toString() + (keep ? "" : " (filtered out)"));
               }

               if (keep) {
                  if (this.nodes.add(mm)) {
                     newRemain.add(mm);
                  }

                  s.put(m, mm);
                  p.put(mm, m);
               }
            }
         }

         remain = newRemain;
      }

      logger.debug("[AM] number of methods to be analysed: " + nb);
      this.succ = new HashMap();
      this.pred = new HashMap();
      this.tails = new LinkedList();
      this.heads = new LinkedList();
      Iterator var18 = this.nodes.iterator();

      while(var18.hasNext()) {
         SootMethod x = (SootMethod)var18.next();
         Set<SootMethod> ss = s.get(x);
         Set<SootMethod> pp = p.get(x);
         this.succ.put(x, new LinkedList(ss));
         this.pred.put(x, new LinkedList(pp));
         if (ss.isEmpty()) {
            this.tails.add(x);
         }

         if (pp.isEmpty()) {
            this.heads.add(x);
         }
      }

      this.size = this.nodes.size();
   }

   public List<SootMethod> getHeads() {
      return this.heads;
   }

   public List<SootMethod> getTails() {
      return this.tails;
   }

   public Iterator<SootMethod> iterator() {
      return this.nodes.iterator();
   }

   public int size() {
      return this.size;
   }

   public List<SootMethod> getSuccsOf(SootMethod s) {
      return (List)this.succ.get(s);
   }

   public List<SootMethod> getPredsOf(SootMethod s) {
      return (List)this.pred.get(s);
   }
}
