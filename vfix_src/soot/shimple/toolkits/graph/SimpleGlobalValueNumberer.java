package soot.shimple.toolkits.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.CompleteBlockGraph;

public class SimpleGlobalValueNumberer implements GlobalValueNumberer {
   protected BlockGraph cfg;
   protected ValueGraph vg;
   protected Set<SimpleGlobalValueNumberer.Partition> partitions;
   protected Map<ValueGraph.Node, SimpleGlobalValueNumberer.Partition> nodeToPartition;
   protected int currentPartitionNumber;
   protected List<SimpleGlobalValueNumberer.Partition> newPartitions;

   public SimpleGlobalValueNumberer(BlockGraph cfg) {
      this.cfg = cfg;
      this.vg = new ValueGraph(cfg);
      this.partitions = new HashSet();
      this.nodeToPartition = new HashMap();
      this.currentPartitionNumber = 0;
      this.initPartition();
      this.iterPartition();
   }

   public static void main(String[] args) {
      Scene.v().loadClassAndSupport(args[0]);
      SootClass sc = Scene.v().getSootClass(args[0]);
      SootMethod sm = sc.getMethod(args[1]);
      Body b = sm.retrieveActiveBody();
      ShimpleBody sb = Shimple.v().newBody(b);
      CompleteBlockGraph cfg = new CompleteBlockGraph(sb);
      SimpleGlobalValueNumberer sgvn = new SimpleGlobalValueNumberer(cfg);
      System.out.println(sgvn);
   }

   public int getGlobalValueNumber(Local local) {
      ValueGraph.Node node = this.vg.getNode(local);
      return ((SimpleGlobalValueNumberer.Partition)this.nodeToPartition.get(node)).getPartitionNumber();
   }

   public boolean areEqual(Local local1, Local local2) {
      ValueGraph.Node node1 = this.vg.getNode(local1);
      ValueGraph.Node node2 = this.vg.getNode(local2);
      return this.nodeToPartition.get(node1) == this.nodeToPartition.get(node2);
   }

   protected void initPartition() {
      Map<String, SimpleGlobalValueNumberer.Partition> labelToPartition = new HashMap();
      Iterator topNodesIt = this.vg.getTopNodes().iterator();

      while(topNodesIt.hasNext()) {
         ValueGraph.Node node = (ValueGraph.Node)topNodesIt.next();
         String label = node.getLabel();
         SimpleGlobalValueNumberer.Partition partition = (SimpleGlobalValueNumberer.Partition)labelToPartition.get(label);
         if (partition == null) {
            partition = new SimpleGlobalValueNumberer.Partition();
            this.partitions.add(partition);
            labelToPartition.put(label, partition);
         }

         partition.add(node);
         this.nodeToPartition.put(node, partition);
      }

   }

   protected void iterPartition() {
      this.newPartitions = new ArrayList();
      Iterator partitionsIt = this.partitions.iterator();

      while(partitionsIt.hasNext()) {
         SimpleGlobalValueNumberer.Partition partition = (SimpleGlobalValueNumberer.Partition)partitionsIt.next();
         this.processPartition(partition);
      }

      this.partitions.addAll(this.newPartitions);
   }

   protected void processPartition(SimpleGlobalValueNumberer.Partition partition) {
      int size = partition.size();
      if (size != 0) {
         ValueGraph.Node firstNode = (ValueGraph.Node)partition.get(0);
         SimpleGlobalValueNumberer.Partition newPartition = new SimpleGlobalValueNumberer.Partition();
         boolean processNewPartition = false;

         for(int i = 1; i < size; ++i) {
            ValueGraph.Node node = (ValueGraph.Node)partition.get(i);
            if (!this.childrenAreInSamePartition(firstNode, node)) {
               partition.remove(i);
               --size;
               newPartition.add(node);
               this.newPartitions.add(newPartition);
               this.nodeToPartition.put(node, newPartition);
               processNewPartition = true;
            }
         }

         if (processNewPartition) {
            this.processPartition(newPartition);
         }

      }
   }

   protected boolean childrenAreInSamePartition(ValueGraph.Node node1, ValueGraph.Node node2) {
      boolean ordered = node1.isOrdered();
      if (ordered != node2.isOrdered()) {
         throw new RuntimeException("Assertion failed.");
      } else {
         List<ValueGraph.Node> children1 = node1.getChildren();
         List<ValueGraph.Node> children2 = node2.getChildren();
         int numberOfChildren = children1.size();
         if (children2.size() != numberOfChildren) {
            return false;
         } else {
            int i;
            ValueGraph.Node child1;
            if (ordered) {
               for(i = 0; i < numberOfChildren; ++i) {
                  child1 = (ValueGraph.Node)children1.get(i);
                  ValueGraph.Node child2 = (ValueGraph.Node)children2.get(i);
                  if (this.nodeToPartition.get(child1) != this.nodeToPartition.get(child2)) {
                     return false;
                  }
               }
            } else {
               for(i = 0; i < numberOfChildren; ++i) {
                  child1 = (ValueGraph.Node)children1.get(i);

                  for(int j = i; j < numberOfChildren; ++j) {
                     ValueGraph.Node child2 = (ValueGraph.Node)children2.get(j);
                     if (this.nodeToPartition.get(child1) == this.nodeToPartition.get(child2)) {
                        if (j != i) {
                           children2.remove(j);
                           children2.add(i, child2);
                        }
                        break;
                     }

                     if (j + 1 == numberOfChildren) {
                        return false;
                     }
                  }
               }
            }

            return true;
         }
      }
   }

   public String toString() {
      StringBuffer tmp = new StringBuffer();
      Body b = this.cfg.getBody();
      Iterator localsIt = b.getLocals().iterator();

      while(localsIt.hasNext()) {
         Local local = (Local)localsIt.next();
         tmp.append(local + "\t" + this.getGlobalValueNumber(local) + "\n");
      }

      return tmp.toString();
   }

   protected class Partition extends ArrayList {
      protected int partitionNumber;

      protected Partition() {
         this.partitionNumber = SimpleGlobalValueNumberer.this.currentPartitionNumber++;
      }

      protected int getPartitionNumber() {
         return this.partitionNumber;
      }
   }
}
