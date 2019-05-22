package soot.jimple.toolkits.thread.mhp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Timers;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.tagkit.Tag;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

public class MonitorAnalysis extends ForwardFlowAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(MonitorAnalysis.class);
   private PegGraph g;
   private final HashMap<String, FlowSet> monitor = new HashMap();
   private final Vector<Object> nodes = new Vector();
   private final Vector<Object> valueBefore = new Vector();
   private final Vector<Object> valueAfter = new Vector();

   public MonitorAnalysis(PegGraph g) {
      super(g);
      this.g = g;
      this.doAnalysis();
      g.setMonitor(this.monitor);
   }

   protected void doAnalysis() {
      LinkedList<Object> changedUnits = new LinkedList();
      HashSet<Object> changedUnitsSet = new HashSet();
      int numNodes = this.graph.size();
      int numComputations = 0;
      this.createWorkList(changedUnits, changedUnitsSet);
      Iterator it = this.graph.getHeads().iterator();

      Object beforeFlow;
      while(it.hasNext()) {
         beforeFlow = it.next();
         this.nodes.add(beforeFlow);
         this.valueBefore.add(this.entryInitialFlow());
      }

      Object previousAfterFlow = this.newInitialFlow();

      while(true) {
         Object afterFlow;
         Object s;
         do {
            if (changedUnits.isEmpty()) {
               Timers var10000 = Timers.v();
               var10000.totalFlowNodes += numNodes;
               var10000 = Timers.v();
               var10000.totalFlowComputations += numComputations;
               return;
            }

            s = changedUnits.removeFirst();
            changedUnitsSet.remove(s);
            int pos = this.nodes.indexOf(s);
            this.copy(this.valueAfter.elementAt(pos), previousAfterFlow);
            List preds = this.graph.getPredsOf(s);
            beforeFlow = this.valueBefore.elementAt(pos);
            if (preds.size() == 1) {
               this.copy(this.valueAfter.elementAt(this.nodes.indexOf(preds.get(0))), beforeFlow);
            } else if (preds.size() != 0) {
               Iterator predIt = preds.iterator();
               Object obj = predIt.next();
               this.copy(this.valueAfter.elementAt(this.nodes.indexOf(obj)), beforeFlow);

               while(predIt.hasNext()) {
                  JPegStmt stmt = (JPegStmt)predIt.next();
                  if (!stmt.equals(obj) && this.nodes.indexOf(stmt) >= 0) {
                     Object otherBranchFlow = this.valueAfter.elementAt(this.nodes.indexOf(stmt));
                     this.merge(beforeFlow, otherBranchFlow, beforeFlow);
                  }
               }
            }

            afterFlow = this.valueAfter.elementAt(this.nodes.indexOf(s));
            this.flowThrough(beforeFlow, s, afterFlow);
            this.valueAfter.set(this.nodes.indexOf(s), afterFlow);
            ++numComputations;
         } while(afterFlow.equals(previousAfterFlow));

         Iterator succIt = this.graph.getSuccsOf(s).iterator();

         while(succIt.hasNext()) {
            Object succ = succIt.next();
            if (!changedUnitsSet.contains(succ)) {
               changedUnits.addLast(succ);
               changedUnitsSet.add(succ);
            }
         }
      }
   }

   protected void merge(Object in1, Object in2, Object out) {
      MonitorSet inSet1 = (MonitorSet)in1;
      MonitorSet inSet2 = (MonitorSet)in2;
      MonitorSet outSet = (MonitorSet)out;
      inSet1.intersection(inSet2, outSet);
   }

   protected void flowThrough(Object inValue, Object unit, Object outValue) {
      MonitorSet in = (MonitorSet)inValue;
      MonitorSet out = (MonitorSet)outValue;
      JPegStmt s = (JPegStmt)unit;
      Tag tag = (Tag)s.getTags().get(0);
      in.copy(out);
      if (in.size() > 0 && !s.getName().equals("waiting") && !s.getName().equals("notified-entry")) {
         this.updateMonitor(in, unit);
      }

      String objName = s.getObject();
      if (s.getName().equals("entry") || s.getName().equals("exit")) {
         if (out.contains("&")) {
            out.remove("&");
         }

         Object obj = out.getMonitorDepth(objName);
         MonitorDepth md;
         if (obj == null) {
            if (s.getName().equals("entry")) {
               md = new MonitorDepth(objName, 1);
               out.add(md);
            }
         } else {
            if (!(obj instanceof MonitorDepth)) {
               throw new RuntimeException("MonitorSet contains non MonitorDepth element!");
            }

            md = (MonitorDepth)obj;
            if (s.getName().equals("entry")) {
               md.increaseDepth();
            } else if (md.getDepth() > 1) {
               md.decreaseDepth();
            } else {
               if (md.getDepth() != 1) {
                  throw new RuntimeException("The monitor depth can not be decreased at  " + unit);
               }

               out.remove(md);
            }
         }
      }

   }

   protected void copy(Object source, Object dest) {
      MonitorSet sourceSet = (MonitorSet)source;
      MonitorSet destSet = (MonitorSet)dest;
      sourceSet.copy(destSet);
   }

   protected Object entryInitialFlow() {
      return new MonitorSet();
   }

   protected Object newInitialFlow() {
      MonitorSet fullSet = new MonitorSet();
      fullSet.add("&");
      return fullSet;
   }

   private void updateMonitor(MonitorSet ms, Object unit) {
      Iterator it = ms.iterator();

      while(it.hasNext()) {
         Object obj = it.next();
         if (obj instanceof MonitorDepth) {
            MonitorDepth md = (MonitorDepth)obj;
            String objName = md.getObjName();
            if (this.monitor.containsKey(objName)) {
               if (md.getDepth() > 0) {
                  ((FlowSet)this.monitor.get(objName)).add(unit);
               }
            } else {
               FlowSet monitorObjs = new ArraySparseSet();
               monitorObjs.add(unit);
               this.monitor.put(objName, monitorObjs);
            }
         }
      }

   }

   private void createWorkList(LinkedList<Object> changedUnits, HashSet<Object> changedUnitsSet) {
      this.createWorkList(changedUnits, changedUnitsSet, this.g.getMainPegChain());
      Set maps = this.g.getStartToThread().entrySet();
      Iterator iter = maps.iterator();

      while(iter.hasNext()) {
         java.util.Map.Entry entry = (java.util.Map.Entry)iter.next();
         List runMethodChainList = (List)entry.getValue();
         Iterator it = runMethodChainList.iterator();

         while(it.hasNext()) {
            PegChain chain = (PegChain)it.next();
            this.createWorkList(changedUnits, changedUnitsSet, chain);
         }
      }

   }

   public void computeSynchNodes() {
      int num = 0;
      Set maps = this.monitor.entrySet();

      FlowSet fs;
      for(Iterator iter = maps.iterator(); iter.hasNext(); num += fs.size()) {
         java.util.Map.Entry entry = (java.util.Map.Entry)iter.next();
         fs = (FlowSet)entry.getValue();
      }

      System.err.println("synch objects: " + num);
   }

   private void createWorkList(LinkedList<Object> changedUnits, HashSet<Object> changedUnitsSet, PegChain chain) {
      Iterator it = chain.getHeads().iterator();
      HashSet gray = new HashSet();

      while(it.hasNext()) {
         Object head = it.next();
         if (!gray.contains(head)) {
            this.visitNode(gray, head, changedUnits, changedUnitsSet);
         }
      }

   }

   private void visitNode(Set<Object> gray, Object obj, LinkedList<Object> changedUnits, HashSet<Object> changedUnitsSet) {
      gray.add(obj);
      changedUnits.addLast(obj);
      changedUnitsSet.add(obj);
      this.nodes.add(obj);
      this.valueBefore.add(this.newInitialFlow());
      this.valueAfter.add(this.newInitialFlow());
      Iterator succsIt = this.graph.getSuccsOf(obj).iterator();
      if (this.g.getSuccsOf(obj).size() > 0) {
         while(succsIt.hasNext()) {
            Object succ = succsIt.next();
            if (!gray.contains(succ)) {
               this.visitNode(gray, succ, changedUnits, changedUnitsSet);
            }
         }
      }

   }

   public Map<String, FlowSet> getMonitor() {
      return this.monitor;
   }

   public void testMonitor() {
      System.out.println("=====test monitor size: " + this.monitor.size());
      Set maps = this.monitor.entrySet();
      Iterator iter = maps.iterator();

      while(true) {
         FlowSet list;
         do {
            if (!iter.hasNext()) {
               System.out.println("=========monitor--ends--------");
               return;
            }

            java.util.Map.Entry entry = (java.util.Map.Entry)iter.next();
            String key = (String)entry.getKey();
            System.out.println("---key=  " + key);
            list = (FlowSet)entry.getValue();
         } while(list.size() <= 0);

         System.out.println("**set:  " + list.size());
         Iterator it = list.iterator();

         while(it.hasNext()) {
            JPegStmt stmt = (JPegStmt)it.next();
            Tag tag1 = (Tag)stmt.getTags().get(0);
            System.out.println(tag1 + " " + stmt);
         }
      }
   }
}
