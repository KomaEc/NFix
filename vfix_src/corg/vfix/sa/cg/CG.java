package corg.vfix.sa.cg;

import java.util.ArrayList;
import java.util.Iterator;
import soot.SootMethod;
import soot.Unit;

public class CG {
   private ArrayList<CGNode> nodes = new ArrayList();
   private ArrayList<CGEdge> edges = new ArrayList();
   private ArrayList<CGNode> visited = new ArrayList();
   private CGNode mainNode;
   private CGNode sinkNode;

   public CG(CGNode m, CGNode s) {
      this.mainNode = m;
      this.sinkNode = s;
      this.addNode(m);
      this.addNode(s);
   }

   public boolean isMainNode(CGNode n) {
      return this.mainNode.equals(n);
   }

   public boolean isSinkNode(CGNode n) {
      return this.sinkNode.equals(n);
   }

   public boolean isVisited(CGNode n) {
      Iterator var3 = this.visited.iterator();

      while(var3.hasNext()) {
         CGNode node = (CGNode)var3.next();
         if (node.equals(n)) {
            return true;
         }
      }

      return false;
   }

   public void addVisited(CGNode n) {
      this.visited.add(n);
   }

   public CGNode getMainNode() {
      return this.mainNode;
   }

   public CGNode getSinkNode() {
      return this.sinkNode;
   }

   public void addNode(CGNode node) {
      if (!this.containNode(node)) {
         this.nodes.add(node);
      }

   }

   public boolean addEdge(CGEdge edge) {
      if (!this.containEdge(edge)) {
         this.edges.add(edge);
         CGNode s = edge.getSrc();
         CGNode t = edge.getTgt();
         if (!this.containNode(s)) {
            this.addNode(s);
         }

         if (!this.containNode(t)) {
            this.addNode(t);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean containEdge(CGEdge e) {
      Iterator var3 = this.edges.iterator();

      while(var3.hasNext()) {
         CGEdge edge = (CGEdge)var3.next();
         if (edge.equals(e)) {
            return true;
         }
      }

      return false;
   }

   public boolean containNode(CGNode n) {
      Iterator var3 = this.nodes.iterator();

      while(var3.hasNext()) {
         CGNode node = (CGNode)var3.next();
         if (node.equals(n)) {
            return true;
         }
      }

      return false;
   }

   public ArrayList<CGNode> getNodes() {
      return this.nodes;
   }

   public ArrayList<SootMethod> getMtds() {
      ArrayList<SootMethod> mtds = new ArrayList();
      Iterator var3 = this.nodes.iterator();

      while(var3.hasNext()) {
         CGNode node = (CGNode)var3.next();
         SootMethod mtd = node.getMtd();
         if (mtd != null && !mtds.contains(mtd)) {
            mtds.add(mtd);
         }
      }

      return mtds;
   }

   public ArrayList<CGEdge> getEdges() {
      return this.edges;
   }

   public ArrayList<CGEdge> getOutOf(CGNode node) {
      ArrayList<CGEdge> tgts = new ArrayList();
      Iterator var4 = this.edges.iterator();

      while(var4.hasNext()) {
         CGEdge edge = (CGEdge)var4.next();
         if (edge.getSrc().equals(node) && !tgts.contains(edge)) {
            tgts.add(edge);
         }
      }

      return tgts;
   }

   public ArrayList<CGEdge> getInTo(SootMethod mtd) {
      return this.getInTo(new CGNode(mtd));
   }

   public ArrayList<Unit> getInToUnits(SootMethod mtd) {
      ArrayList<CGEdge> intoEdges = this.getInTo(mtd);
      ArrayList<Unit> units = new ArrayList();
      Iterator var5 = intoEdges.iterator();

      while(var5.hasNext()) {
         CGEdge edge = (CGEdge)var5.next();
         Unit unit = edge.getUnit();
         if (!units.contains(unit)) {
            units.add(unit);
         }
      }

      return units;
   }

   public ArrayList<CGEdge> getInTo(CGNode node) {
      ArrayList<CGEdge> tgts = new ArrayList();
      Iterator var4 = this.edges.iterator();

      while(var4.hasNext()) {
         CGEdge edge = (CGEdge)var4.next();
         if (edge.getTgt().equals(node) && !tgts.contains(edge)) {
            tgts.add(edge);
         }
      }

      return tgts;
   }
}
