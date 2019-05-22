package corg.vfix.pg.jimple.element;

import corg.vfix.sa.SAMain;
import corg.vfix.sa.vfg.VFGEdge;
import corg.vfix.sa.vfg.VFGNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticInvokeExpr;

public class ExtractTransRules {
   public static boolean main(VFGNode node) throws Exception {
      if (node.getStmtType() == 0) {
         extractTransCopy(node);
      } else if (node.getStmtType() == 1) {
         extractTransLoad(node);
      } else if (node.getStmtType() == 2) {
         extractTransStore(node);
      } else if (node.getStmtType() == 4) {
         extractTransRet(node);
      } else if (node.getStmtType() == 8) {
         extractTransCast(node);
      } else if (node.getStmtType() == 3) {
         extractTransInstanceCall(node);
      } else if (node.getStmtType() == 9) {
         extractTransStaticCall(node);
      } else {
         if (node.getStmtType() != 10) {
            System.out.println("Cannot extract elements: " + node.getStmt());
            System.out.println(StmtType.typeToString(node.getStmtType()));
            return false;
         }

         extractTransStaticInvoke(node);
         System.out.println("ETwo: " + node.getETwo());
      }

      return true;
   }

   private static void extractTransStaticInvoke(VFGNode node) {
      InvokeStmt invokeStmt = (InvokeStmt)node.getStmt();
      List<Value> args = invokeStmt.getInvokeExpr().getArgs();
      node.setEOne((Value)null);
      ArrayList<VFGEdge> edges = SAMain.vfg.getEdgesOutOf(node);
      Iterator var5 = edges.iterator();

      while(var5.hasNext()) {
         VFGEdge edge = (VFGEdge)var5.next();
         Value value = edge.getValue();
         if (args.contains(value)) {
            node.setETwo(value);
            return;
         }
      }

   }

   private static void extractTransStaticCall(VFGNode node) {
      AssignStmt assign = (AssignStmt)node.getStmt();
      node.setEOne(assign.getLeftOp());
      StaticInvokeExpr sie = (StaticInvokeExpr)assign.getRightOp();
      ArrayList<Value> sinks = node.getSinks();
      Iterator var5 = sie.getArgs().iterator();

      while(var5.hasNext()) {
         Value value = (Value)var5.next();
         if (sinks.contains(value)) {
            node.setETwo(value);
            break;
         }
      }

   }

   private static void extractTransInstanceCall(VFGNode node) {
      AssignStmt assign = (AssignStmt)node.getStmt();
      node.setEOne(assign.getLeftOp());
      InstanceInvokeExpr sie = (InstanceInvokeExpr)assign.getRightOp();
      ArrayList<Value> sinks = node.getSinks();
      Iterator var5 = sie.getArgs().iterator();

      while(var5.hasNext()) {
         Value value = (Value)var5.next();
         if (sinks.contains(value)) {
            node.setETwo(value);
            break;
         }
      }

   }

   private static void extractTransCast(VFGNode node) {
      AssignStmt stmt = (AssignStmt)node.getStmt();
      CastExpr castExpr = (CastExpr)stmt.getRightOp();
      node.setEOne(stmt.getLeftOp());
      node.setETwo(castExpr.getOp());
   }

   private static void extractTransRet(VFGNode node) {
      ReturnStmt ret = (ReturnStmt)node.getStmt();
      node.setEOne((Value)null);
      node.setETwo(ret.getOp());
   }

   private static void extractTransStore(VFGNode node) {
      AssignStmt stmt = (AssignStmt)node.getStmt();
      node.setEOne(stmt.getLeftOp());
      node.setETwo(stmt.getRightOp());
   }

   private static void extractTransLoad(VFGNode node) {
      AssignStmt stmt = (AssignStmt)node.getStmt();
      node.setEOne(stmt.getLeftOp());
      node.setETwo(stmt.getRightOp());
   }

   private static void extractTransCopy(VFGNode node) {
      AssignStmt stmt = (AssignStmt)node.getStmt();
      node.setEOne(stmt.getLeftOp());
      node.setETwo(stmt.getRightOp());
   }
}
