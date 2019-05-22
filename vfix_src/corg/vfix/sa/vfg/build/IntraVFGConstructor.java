package corg.vfix.sa.vfg.build;

import corg.vfix.sa.vfg.NodeType;
import corg.vfix.sa.vfg.VFG;
import corg.vfix.sa.vfg.VFGNode;
import corg.vfix.sa.vfg.build.reach.IntraReach;
import java.util.ArrayList;
import java.util.Iterator;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.Stmt;
import soot.util.Chain;

public class IntraVFGConstructor {
   private static SootMethod sMethod;

   public static VFG buildFromSrc(SootMethod sMtd, Stmt srcStmt) throws Exception {
      sMethod = sMtd;
      if (sMethod != null && sMethod.hasActiveBody()) {
         VFG vfg = new VFG();
         ArrayList<VFGNode> nodes = getVFGNodes();
         vfg.addNodes(nodes);
         Iterator var5 = nodes.iterator();

         while(var5.hasNext()) {
            VFGNode node1 = (VFGNode)var5.next();
            Iterator var7 = nodes.iterator();

            while(var7.hasNext()) {
               VFGNode node2 = (VFGNode)var7.next();
               if (!node1.equals(node2)) {
                  addTransEdge(vfg, node1, node2);
                  addSinksEdge(vfg, node1, node2);
               }
            }
         }

         vfg.refineVFGFromSrc(srcStmt);
         NodeType.setType(vfg);
         return vfg;
      } else {
         System.out.println("method not found or has no active body.");
         throw new Exception();
      }
   }

   public static VFG buildFromCaller(SootMethod sMtd, Stmt nullStmt, Value nullPointer) throws Exception {
      sMethod = sMtd;
      if (sMethod != null && sMethod.hasActiveBody()) {
         VFG vfg = new VFG();
         ArrayList<VFGNode> nodes = getVFGNodes();
         vfg.addNodes(nodes);
         VFGNode node = vfg.getNodeByStmt(nullStmt);
         node.addSinkValue(nullPointer);
         Iterator var7 = nodes.iterator();

         while(var7.hasNext()) {
            VFGNode node1 = (VFGNode)var7.next();
            Iterator var9 = nodes.iterator();

            while(var9.hasNext()) {
               VFGNode node2 = (VFGNode)var9.next();
               if (!node1.equals(node2)) {
                  addTransEdge(vfg, node1, node2);
                  addSinksEdge(vfg, node1, node2);
               }
            }
         }

         vfg.refineVFGFromSink(nullStmt, nullPointer);
         NodeType.setType(vfg);
         return vfg;
      } else {
         System.out.println("method not found or has no active body.");
         throw new Exception();
      }
   }

   public static VFG buildFromSink(SootMethod sMtd, Stmt nullStmt, Value nullPointer) throws Exception {
      sMethod = sMtd;
      if (sMethod != null && sMethod.hasActiveBody()) {
         VFG vfg = new VFG();
         ArrayList<VFGNode> nodes = getVFGNodes();
         vfg.addNodes(nodes);
         Iterator var6 = nodes.iterator();

         while(var6.hasNext()) {
            VFGNode node1 = (VFGNode)var6.next();
            Iterator var8 = nodes.iterator();

            while(var8.hasNext()) {
               VFGNode node2 = (VFGNode)var8.next();
               if (!node1.equals(node2)) {
                  addTransEdge(vfg, node1, node2);
                  addSinksEdge(vfg, node1, node2);
               }
            }
         }

         vfg.refineVFGFromSink(nullStmt, nullPointer);
         NodeType.setType(vfg);
         return vfg;
      } else {
         System.out.println("method not found or has no active body.");
         throw new Exception();
      }
   }

   private static void addSinksEdge(VFG vfg, VFGNode node1, VFGNode node2) throws Exception {
      ArrayList<Value> srcs = node1.getSrcs();
      ArrayList<Value> sinks = node2.getSinks();
      ArrayList<Value> intersection = BuildRules.getValueIntersection(srcs, sinks);
      if (intersection != null && !intersection.isEmpty()) {
         Iterator var7 = intersection.iterator();

         while(var7.hasNext()) {
            Value value = (Value)var7.next();
            if (IntraReach.immediateReach(node1, node2, value)) {
               vfg.addEdge(node1, node2, value, 2);
            }
         }

      }
   }

   private static void addTransEdge(VFG vfg, VFGNode node1, VFGNode node2) throws Exception {
      ArrayList<Value> srcs = node1.getSrcs();
      ArrayList<Value> sinks = node2.getTrans();
      ArrayList<Value> intersection = BuildRules.getValueIntersection(srcs, sinks);
      if (intersection != null && !intersection.isEmpty()) {
         Iterator var7 = intersection.iterator();

         while(var7.hasNext()) {
            Value value = (Value)var7.next();
            if (IntraReach.immediateReach(node1, node2, value)) {
               vfg.addEdge(node1, node2, value, 1);
            }
         }

      }
   }

   private static ArrayList<VFGNode> getVFGNodes() {
      ArrayList<VFGNode> nodes = new ArrayList();
      Chain<Unit> units = sMethod.getActiveBody().getUnits();
      Iterator var3 = units.iterator();

      while(var3.hasNext()) {
         Unit unit = (Unit)var3.next();
         if (unit instanceof Stmt) {
            VFGNode node = new VFGNode(sMethod, (Stmt)unit);
            nodes.add(node);
         }
      }

      return nodes;
   }

   public static void print(ArrayList<VFGNode> nodes) {
      Iterator var2 = nodes.iterator();

      while(var2.hasNext()) {
         VFGNode node = (VFGNode)var2.next();
         System.out.println(node);
      }

   }
}
