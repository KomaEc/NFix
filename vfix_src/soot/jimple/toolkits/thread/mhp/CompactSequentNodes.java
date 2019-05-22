package soot.jimple.toolkits.thread.mhp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Map.Entry;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;

public class CompactSequentNodes {
   long compactNodes = 0L;
   long add = 0L;

   public CompactSequentNodes(PegGraph pg) {
      Chain mainPegChain = pg.getMainPegChain();
      this.compactGraph(mainPegChain, pg);
      this.compactStartChain(pg);
      System.err.println("compact seq. node: " + this.compactNodes);
      System.err.println("number of compacting seq. nodes: " + this.add);
   }

   private void compactGraph(Chain chain, PegGraph peg) {
      Set canNotBeCompacted = peg.getCanNotBeCompacted();
      List<List<Object>> list = this.computeSequentNodes(chain, peg);
      Iterator it = list.iterator();

      while(it.hasNext()) {
         List s = (List)it.next();
         if (!this.checkIfContainsElemsCanNotBeCompacted(s, canNotBeCompacted)) {
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

   private List<List<Object>> computeSequentNodes(Chain chain, PegGraph pg) {
      Set<Object> gray = new HashSet();
      List<List<Object>> sequentNodes = new ArrayList();
      Set canNotBeCompacted = pg.getCanNotBeCompacted();
      TopologicalSorter ts = new TopologicalSorter(chain, pg);
      ListIterator it = ts.sorter().listIterator();

      while(it.hasNext()) {
         Object node = it.next();
         List<Object> list = new ArrayList();
         if (!gray.contains(node)) {
            this.visitNode(pg, node, list, canNotBeCompacted, gray);
            if (list.size() > 1) {
               gray.addAll(list);
               sequentNodes.add(list);
            }
         }
      }

      return sequentNodes;
   }

   private void visitNode(PegGraph pg, Object node, List<Object> list, Set canNotBeCompacted, Set<Object> gray) {
      if (pg.getPredsOf(node).size() == 1 && pg.getSuccsOf(node).size() == 1 && !canNotBeCompacted.contains(node) && !gray.contains(node)) {
         list.add(node);
         Iterator it = pg.getSuccsOf(node).iterator();

         while(it.hasNext()) {
            Object o = it.next();
            this.visitNode(pg, o, list, canNotBeCompacted, gray);
         }
      }

   }

   private boolean checkIfContainsElemsCanNotBeCompacted(List list, Set canNotBeCompacted) {
      Iterator sccIt = list.iterator();

      Object node;
      do {
         if (!sccIt.hasNext()) {
            return false;
         }

         node = sccIt.next();
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

      Object s;
      while(it.hasNext()) {
         s = it.next();
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
         s = it.next();
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
