package corg.vfix.sa.vfg.cgst;

import corg.vfix.fl.stack.StackTrace;
import corg.vfix.sa.plot.CongestionPlot;
import corg.vfix.sa.vfg.VFG;
import corg.vfix.sa.vfg.VFGEdge;
import corg.vfix.sa.vfg.VFGNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;
import soot.Unit;
import soot.util.Chain;

public class CongestionCalculator {
   private static Stack<VFGNode> stack = new Stack();
   private static ArrayList<ArrayList<VFGNode>> paths = new ArrayList();
   private static VFG vfg;

   public static void main(VFG g) throws IOException {
      vfg = g;
      findAllPaths();
      countCongestion();
      CongestionPlot.plot(vfg);
   }

   private static void countCongestion() {
      Iterator var1 = paths.iterator();

      while(var1.hasNext()) {
         ArrayList<VFGNode> path = (ArrayList)var1.next();
         Iterator var3 = path.iterator();

         while(var3.hasNext()) {
            VFGNode node = (VFGNode)var3.next();
            node.addCongestion();
         }
      }

   }

   private static void findAllPaths() {
      ArrayList<VFGNode> srcs = vfg.getSrcs();
      ArrayList<VFGNode> sinks = vfg.getSinks();
      Iterator var3 = srcs.iterator();

      while(var3.hasNext()) {
         VFGNode v1 = (VFGNode)var3.next();
         Iterator var5 = sinks.iterator();

         while(var5.hasNext()) {
            VFGNode v2 = (VFGNode)var5.next();
            stack.push(v1);
            findPath(v1, v2);
            stack.clear();
         }
      }

   }

   private static void findPath(VFGNode v1, VFGNode v2) {
      if (v1.equals(v2)) {
         pathRecord();
         stack.pop();
      } else {
         ArrayList<VFGNode> neighbors = getNeighbors(v1);
         Iterator var4 = neighbors.iterator();

         while(var4.hasNext()) {
            VFGNode node = (VFGNode)var4.next();
            if (!stack.contains(node)) {
               stack.push(node);
               findPath(node, v2);
            }
         }

         stack.pop();
      }
   }

   private static ArrayList<VFGNode> getNeighbors(VFGNode node) {
      ArrayList<VFGNode> neighbors = new ArrayList();
      ArrayList<VFGEdge> edges = vfg.getEdges();
      Iterator var4 = edges.iterator();

      while(var4.hasNext()) {
         VFGEdge edge = (VFGEdge)var4.next();
         VFGNode from = edge.getFrom();
         VFGNode to = edge.getTo();
         if (node.equals(from) && !stack.contains(to)) {
            neighbors.add(to);
         }
      }

      return neighbors;
   }

   private static void pathRecord() {
      int size = stack.size();
      ArrayList<VFGNode> path = new ArrayList();

      for(int i = 0; i < size; ++i) {
         path.add((VFGNode)stack.get(i));
      }

      paths.add(path);
   }

   public static ArrayList<VFGNode> getSortedRepairLocations() {
      ArrayList<VFGNode> nodes = vfg.getNodes();
      nodes.sort(new Comparator<VFGNode>() {
         public int compare(VFGNode node1, VFGNode node2) {
            int c1 = node1.getCongestion();
            int c2 = node2.getCongestion();
            if (c2 != c1) {
               return c2 - c1;
            } else if (node1.getStmt().equals(StackTrace.getNullStmt())) {
               return -1;
            } else if (node2.getStmt().equals(StackTrace.getNullStmt())) {
               return 1;
            } else {
               int t1 = CongestionCalculator.getIndex(node1);
               int t2 = CongestionCalculator.getIndex(node2);
               return t2 - t1;
            }
         }
      });
      return nodes;
   }

   private static int getIndex(VFGNode node) {
      Chain<Unit> units = node.getMethod().getActiveBody().getUnits().getNonPatchingChain();
      if (!units.contains(node.getStmt())) {
         return -1;
      } else {
         Iterator<Unit> iterator = units.iterator();

         for(int id = 0; iterator.hasNext(); ++id) {
            Unit unit = (Unit)iterator.next();
            if (unit.equals(node.getStmt())) {
               return id;
            }
         }

         return -1;
      }
   }

   public static void print() {
      Iterator var1 = paths.iterator();

      while(var1.hasNext()) {
         ArrayList<VFGNode> path = (ArrayList)var1.next();
         System.out.println("Path:");
         Iterator var3 = path.iterator();

         while(var3.hasNext()) {
            VFGNode node = (VFGNode)var3.next();
            System.out.print(node + " ||| ");
         }

         System.out.println();
      }

   }
}
