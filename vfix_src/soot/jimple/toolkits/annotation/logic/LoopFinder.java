package soot.jimple.toolkits.annotation.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.UnitGraph;

public class LoopFinder extends BodyTransformer {
   private UnitGraph g;
   private HashMap<Stmt, List<Stmt>> loops;

   public Collection<Loop> loops() {
      Collection<Loop> result = new HashSet();
      Iterator var2 = this.loops.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<Stmt, List<Stmt>> entry = (Entry)var2.next();
         result.add(new Loop((Stmt)entry.getKey(), (List)entry.getValue(), this.g));
      }

      return result;
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      this.g = new ExceptionalUnitGraph(b);
      MHGDominatorsFinder<Unit> a = new MHGDominatorsFinder(this.g);
      this.loops = new HashMap();
      Iterator stmtsIt = b.getUnits().iterator();

      while(stmtsIt.hasNext()) {
         Stmt s = (Stmt)stmtsIt.next();
         List<Unit> succs = this.g.getSuccsOf((Unit)s);
         Collection<Unit> dominaters = a.getDominators(s);
         ArrayList<Stmt> headers = new ArrayList();
         Iterator succsIt = succs.iterator();

         while(succsIt.hasNext()) {
            Stmt succ = (Stmt)succsIt.next();
            if (dominaters.contains(succ)) {
               headers.add(succ);
            }
         }

         Iterator headersIt = headers.iterator();

         while(headersIt.hasNext()) {
            Stmt header = (Stmt)headersIt.next();
            List<Stmt> loopBody = this.getLoopBodyFor(header, s);
            if (this.loops.containsKey(header)) {
               List<Stmt> lb1 = (List)this.loops.get(header);
               this.loops.put(header, this.union(lb1, loopBody));
            } else {
               this.loops.put(header, loopBody);
            }
         }
      }

   }

   private List<Stmt> getLoopBodyFor(Stmt header, Stmt node) {
      ArrayList<Stmt> loopBody = new ArrayList();
      Stack<Unit> stack = new Stack();
      loopBody.add(header);
      stack.push(node);

      while(true) {
         Stmt next;
         do {
            if (stack.isEmpty()) {
               assert node == header && loopBody.size() == 1 || loopBody.get(loopBody.size() - 2) == node;

               assert loopBody.get(loopBody.size() - 1) == header;

               return loopBody;
            }

            next = (Stmt)stack.pop();
         } while(loopBody.contains(next));

         loopBody.add(0, next);
         Iterator it = this.g.getPredsOf((Unit)next).iterator();

         while(it.hasNext()) {
            stack.push(it.next());
         }
      }
   }

   private List<Stmt> union(List<Stmt> l1, List<Stmt> l2) {
      Iterator it = l2.iterator();

      while(it.hasNext()) {
         Stmt next = (Stmt)it.next();
         if (!l1.contains(next)) {
            l1.add(next);
         }
      }

      return l1;
   }
}
