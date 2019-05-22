package soot.jimple.toolkits.thread.mhp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;

public class CompactStronglyConnectedComponents {
   long compactNodes = 0L;
   long add = 0L;

   public CompactStronglyConnectedComponents(PegGraph pg) {
      Chain mainPegChain = pg.getMainPegChain();
      this.compactGraph(mainPegChain, pg);
      this.compactStartChain(pg);
      System.err.println("compact SCC nodes: " + this.compactNodes);
      System.err.println(" number of compacting scc nodes: " + this.add);
   }

   private void compactGraph(Chain chain, PegGraph peg) {
      Set canNotBeCompacted = peg.getCanNotBeCompacted();
      SCC scc = new SCC(chain.iterator(), peg);
      List<List<Object>> sccList = scc.getSccList();
      Iterator sccListIt = sccList.iterator();

      while(sccListIt.hasNext()) {
         List s = (List)sccListIt.next();
         if (s.size() > 1 && !this.checkIfContainsElemsCanNotBeCompacted(s, canNotBeCompacted)) {
            ++this.add;
            this.compact(s, chain, peg);
         }
      }

   }

   private void compactStartChain(PegGraph graph) {
      Set maps = graph.getStartToThread().entrySet();
      Iterator iter = maps.iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         List runMethodChainList = (List)entry.getValue();
         Iterator it = runMethodChainList.iterator();

         while(it.hasNext()) {
            Chain chain = (Chain)it.next();
            this.compactGraph(chain, graph);
         }
      }

   }

   private boolean checkIfContainsElemsCanNotBeCompacted(List list, Set canNotBeCompacted) {
      Iterator sccIt = list.iterator();

      JPegStmt node;
      do {
         if (!sccIt.hasNext()) {
            return false;
         }

         node = (JPegStmt)sccIt.next();
      } while(!canNotBeCompacted.contains(node));

      return true;
   }

   private void compact(List list, Chain chain, PegGraph peg) {
      Iterator it = list.iterator();
      FlowSet allNodes = peg.getAllNodes();
      HashMap unitToSuccs = peg.getUnitToSuccs();
      HashMap unitToPreds = peg.getUnitToPreds();
      List<Object> newPreds = new ArrayList();
      ArrayList newSuccs = new ArrayList();

      JPegStmt s;
      while(it.hasNext()) {
         s = (JPegStmt)it.next();
         Iterator succsIt = peg.getPredsOf(s).iterator();

         Object succ;
         List predsOfSucc;
         while(succsIt.hasNext()) {
            succ = succsIt.next();
            predsOfSucc = peg.getSuccsOf(succ);
            predsOfSucc.remove(s);
            if (!list.contains(succ)) {
               newPreds.add(succ);
               predsOfSucc.add(list);
            }
         }

         succsIt = peg.getSuccsOf(s).iterator();

         while(succsIt.hasNext()) {
            succ = succsIt.next();
            predsOfSucc = peg.getPredsOf(succ);
            predsOfSucc.remove(s);
            if (!list.contains(succ)) {
               newSuccs.add(succ);
               predsOfSucc.add(list);
            }
         }
      }

      unitToSuccs.put(list, newSuccs);
      unitToPreds.put(list, newPreds);
      allNodes.add(list);
      chain.add(list);
      this.updateMonitor(peg, list);
      it = list.iterator();

      while(it.hasNext()) {
         s = (JPegStmt)it.next();
         chain.remove(s);
         allNodes.remove(s);
         unitToSuccs.remove(s);
         unitToPreds.remove(s);
      }

      this.compactNodes += (long)list.size();
   }

   private void updateMonitor(PegGraph pg, List list) {
      Set maps = pg.getMonitor().entrySet();
      Iterator iter = maps.iterator();

      while(true) {
         while(iter.hasNext()) {
            Entry entry = (Entry)iter.next();
            FlowSet fs = (FlowSet)entry.getValue();
            Iterator it = list.iterator();

            while(it.hasNext()) {
               Object obj = it.next();
               if (fs.contains(obj)) {
                  fs.add(list);
                  break;
               }
            }
         }

         return;
      }
   }
}
