package corg.vfix.sa.vfg;

import java.util.ArrayList;
import java.util.Iterator;
import soot.Value;
import soot.jimple.Stmt;

public class VFG {
   private ArrayList<VFGNode> srcs;
   private ArrayList<VFGNode> sinks;
   private ArrayList<VFGNode> nodes = new ArrayList();
   private ArrayList<VFGEdge> edges = new ArrayList();

   public void addNodes(ArrayList<VFGNode> nodes) {
      Iterator var3 = nodes.iterator();

      while(var3.hasNext()) {
         VFGNode node = (VFGNode)var3.next();
         this.addNode(node);
      }

   }

   public void addNode(VFGNode node) {
      if (!this.containNode(node)) {
         this.nodes.add(node);
      }

   }

   public void addEdge(VFGNode nodeFrom, VFGNode nodeTo, Value value, int type) {
      VFGEdge edge = new VFGEdge(nodeFrom, nodeTo, value, type);
      if (!this.hasEdge(edge)) {
         this.edges.add(edge);
      }

   }

   public void addEdge(VFGEdge edge) {
      if (!this.hasEdge(edge)) {
         this.edges.add(edge);
      }

   }

   private boolean hasEdge(VFGEdge edge) {
      Iterator var3 = this.edges.iterator();

      while(var3.hasNext()) {
         VFGEdge e = (VFGEdge)var3.next();
         if (e.equals(edge)) {
            return true;
         }
      }

      return false;
   }

   public void listNode() {
      Iterator var2 = this.nodes.iterator();

      while(var2.hasNext()) {
         VFGNode v = (VFGNode)var2.next();
         System.out.println("Node: " + v);
      }

   }

   public void listEdge() {
      Iterator var2 = this.edges.iterator();

      while(var2.hasNext()) {
         VFGEdge e = (VFGEdge)var2.next();
         System.out.println("Edge: from " + e.getFrom().getStmt() + " to " + e.getTo().getStmt());
      }

   }

   public ArrayList<VFGNode> getNodes() {
      return this.nodes;
   }

   public void removeIsolatedNodes() {
      ArrayList<VFGNode> newNodes = new ArrayList();
      Iterator var3 = this.edges.iterator();

      while(var3.hasNext()) {
         VFGEdge edge = (VFGEdge)var3.next();
         VFGNode from = edge.getFrom();
         if (!newNodes.contains(from)) {
            newNodes.add(from);
         }

         VFGNode to = edge.getTo();
         if (!newNodes.contains(to)) {
            newNodes.add(to);
         }
      }

      this.nodes = newNodes;
   }

   public ArrayList<VFGEdge> getEdges() {
      return this.edges;
   }

   public void setDegree() {
      Iterator var2 = this.nodes.iterator();

      while(var2.hasNext()) {
         VFGNode node = (VFGNode)var2.next();
         node.setInDegree(this.getIncomingEdgeCount(node));
         node.setOutDegree(this.getOutgoingEdgeCount(node));
      }

   }

   private int getOutgoingEdgeCount(VFGNode node) {
      int count = 0;
      Iterator var4 = this.edges.iterator();

      while(var4.hasNext()) {
         VFGEdge edge = (VFGEdge)var4.next();
         if (edge.getFrom().equals(node)) {
            ++count;
         }
      }

      return count;
   }

   private int getIncomingEdgeCount(VFGNode node) {
      int count = 0;
      Iterator var4 = this.edges.iterator();

      while(var4.hasNext()) {
         VFGEdge edge = (VFGEdge)var4.next();
         if (edge.getTo().equals(node)) {
            ++count;
         }
      }

      return count;
   }

   public void setType() {
      this.setSrcs();
      this.setSinks();
   }

   public void setSrcs() {
      this.srcs = new ArrayList();
      Iterator var2 = this.nodes.iterator();

      while(var2.hasNext()) {
         VFGNode node = (VFGNode)var2.next();
         if (node.getNodeType() == 0) {
            this.srcs.add(node);
         }
      }

   }

   public void setSinks() {
      this.sinks = new ArrayList();
      Iterator var2 = this.nodes.iterator();

      while(var2.hasNext()) {
         VFGNode node = (VFGNode)var2.next();
         if (node.getNodeType() == 2) {
            this.sinks.add(node);
         }
      }

   }

   public ArrayList<VFGNode> getSrcs() {
      return this.srcs;
   }

   public ArrayList<VFGNode> getSinks() {
      return this.sinks;
   }

   public void refineVFGFromSink(Stmt nullStmt, Value nullPointer) {
      VFGNode start = null;
      Iterator var5 = this.nodes.iterator();

      while(var5.hasNext()) {
         VFGNode node = (VFGNode)var5.next();
         if (node.getStmt().equals(nullStmt)) {
            start = node;
            break;
         }
      }

      if (start != null) {
         start.setFlag();
         this.findFromNeighbors(start, nullPointer);
         this.removeUnflagedNodes();
         this.removeUnflagedEdges();
      }
   }

   private void removeUnflagedNodes() {
      ArrayList<VFGNode> flagedNodes = new ArrayList();
      Iterator var3 = this.nodes.iterator();

      while(var3.hasNext()) {
         VFGNode node = (VFGNode)var3.next();
         if (node.getFlag()) {
            flagedNodes.add(node);
         }
      }

      this.nodes = flagedNodes;
   }

   private void removeUnflagedEdges() {
      ArrayList<VFGEdge> flagedEdges = new ArrayList();
      Iterator var3 = this.edges.iterator();

      while(var3.hasNext()) {
         VFGEdge edge = (VFGEdge)var3.next();
         if (edge.getFlag()) {
            flagedEdges.add(edge);
         }
      }

      this.edges = flagedEdges;
   }

   private void findToNeighbors(VFGNode node, Value value) {
      Iterator var4 = this.edges.iterator();

      while(true) {
         VFGEdge edge;
         VFGNode toNode;
         do {
            do {
               do {
                  do {
                     if (!var4.hasNext()) {
                        return;
                     }

                     edge = (VFGEdge)var4.next();
                  } while(!edge.getValue().equals(value));
               } while(!edge.getFrom().equals(node));
            } while(edge.getFlag());

            edge.setFlag();
            toNode = edge.getTo();
            toNode.setFlag();
         } while(edge.getType() == 2);

         Iterator var7 = toNode.getSrcs().iterator();

         Value v;
         while(var7.hasNext()) {
            v = (Value)var7.next();
            this.findToNeighbors(toNode, v);
         }

         var7 = toNode.getTrans().iterator();

         while(var7.hasNext()) {
            v = (Value)var7.next();
            this.findFromNeighbors(toNode, v);
         }
      }
   }

   private void findFromNeighbors(VFGNode node, Value value) {
      Iterator var4 = this.edges.iterator();

      while(true) {
         VFGEdge edge;
         do {
            do {
               do {
                  if (!var4.hasNext()) {
                     return;
                  }

                  edge = (VFGEdge)var4.next();
               } while(!edge.getValue().equals(value));
            } while(!edge.getTo().equals(node));
         } while(edge.getFlag());

         edge.setFlag();
         VFGNode fromNode = edge.getFrom();
         fromNode.setFlag();
         Iterator var7 = fromNode.getTrans().iterator();

         Value v;
         while(var7.hasNext()) {
            v = (Value)var7.next();
            this.findFromNeighbors(fromNode, v);
         }

         var7 = fromNode.getSrcs().iterator();

         while(var7.hasNext()) {
            v = (Value)var7.next();
            this.findToNeighbors(fromNode, v);
         }
      }
   }

   public ArrayList<VFGEdge> getEdgesOutOf(VFGNode node) {
      ArrayList<VFGEdge> outs = new ArrayList();
      Iterator var4 = this.edges.iterator();

      while(var4.hasNext()) {
         VFGEdge edge = (VFGEdge)var4.next();
         if (edge.getFrom().equals(node) && !outs.contains(edge)) {
            outs.add(edge);
         }
      }

      return outs;
   }

   public void refineVFGFromSrc(Stmt srcStmt) {
      VFGNode start = null;
      Iterator var4 = this.nodes.iterator();

      while(var4.hasNext()) {
         VFGNode node = (VFGNode)var4.next();
         if (node.getStmt().equals(srcStmt)) {
            start = node;
            break;
         }
      }

      if (start != null) {
         start.setFlag();
         var4 = start.getSrcs().iterator();

         while(var4.hasNext()) {
            Value v = (Value)var4.next();
            this.findToNeighbors(start, v);
         }

         this.removeUnflagedNodes();
         this.removeUnflagedEdges();
      }
   }

   public VFGNode getNodeByStmt(Stmt stmt) {
      Iterator var3 = this.nodes.iterator();

      while(var3.hasNext()) {
         VFGNode node = (VFGNode)var3.next();
         if (node.getStmt().equals(stmt)) {
            return node;
         }
      }

      return null;
   }

   public boolean containNode(VFGNode n) {
      Iterator var3 = this.nodes.iterator();

      while(var3.hasNext()) {
         VFGNode node = (VFGNode)var3.next();
         if (node.equals(n)) {
            return true;
         }
      }

      return false;
   }

   public void removeNode(VFGNode n) {
      if (n != null && this.nodes.contains(n)) {
         this.nodes.remove(n);
      }

   }

   public void removeEdge(VFGEdge e) {
      if (e != null && this.edges.contains(e)) {
         this.edges.remove(e);
      }

   }

   public void removeNodes(ArrayList<VFGNode> ns) {
      Iterator var3 = ns.iterator();

      while(var3.hasNext()) {
         VFGNode n = (VFGNode)var3.next();
         this.removeNode(n);
      }

   }

   public void removeEdges(ArrayList<VFGEdge> es) {
      Iterator var3 = es.iterator();

      while(var3.hasNext()) {
         VFGEdge e = (VFGEdge)var3.next();
         this.removeEdge(e);
      }

   }

   public ArrayList<VFGNode> getExecutedNodes() {
      ArrayList<VFGNode> executedNodes = new ArrayList();
      Iterator var3 = this.nodes.iterator();

      while(var3.hasNext()) {
         VFGNode node = (VFGNode)var3.next();
         if (node.isExecuted()) {
            executedNodes.add(node);
         }
      }

      return executedNodes;
   }
}
