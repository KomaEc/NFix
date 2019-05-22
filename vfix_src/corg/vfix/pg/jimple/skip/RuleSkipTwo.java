package corg.vfix.pg.jimple.skip;

import com.github.javaparser.utils.Pair;
import corg.vfix.pg.jimple.OperationLib;
import corg.vfix.sa.analysis.DependencyAnalyzor;
import corg.vfix.sa.analysis.JimpleLoopFinder;
import corg.vfix.sa.vfg.VFGNode;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.toolkits.annotation.logic.Loop;

public class RuleSkipTwo {
   private static Stmt targetStmt;
   private static List<Unit> invalid;
   private static boolean isContinue;
   private static Pair<Integer, Integer> loopRange;

   public static void patch(VFGNode node) throws Exception {
      if (isContinue(node)) {
         patchContinue(node);
      } else {
         clearLoopRange();
         patchWithoutLoop(node);
      }

   }

   public static boolean isContinue() {
      return isContinue;
   }

   private static void patchWithoutLoop(VFGNode node) throws Exception {
      Body body = (Body)node.getBody().clone();
      Stmt stmt = OperationLib.locateStmt(body, node.getStmt());
      Stmt tStmt = OperationLib.locateStmt(body, targetStmt);
      Value value = OperationLib.locateValue(stmt, node.getETwo());
      if (stmt != null && tStmt != null) {
         if (value == null) {
            throw new Exception();
         } else {
            OperationLib.insertNullCheckSkipTo(body.getUnits(), value, stmt, tStmt);
            OperationLib.output(node, body, "SKIP-TWO");
            isContinue = false;
         }
      } else {
         throw new Exception();
      }
   }

   private static void patchContinue(VFGNode node) throws Exception {
      System.out.println("INVALID: " + invalid);
      Loop minLoop = JimpleLoopFinder.getMinLoop(node.getStmt());
      targetStmt = DependencyAnalyzor.findTargetStmt(node.getBody(), invalid, minLoop.getLoopStatements());
      Body body = (Body)node.getBody().clone();
      Stmt stmt = OperationLib.locateStmt(body, node.getStmt());
      Stmt tStmt = OperationLib.locateStmt(body, targetStmt);
      Value value = OperationLib.locateValue(stmt, node.getETwo());
      if (stmt != null && tStmt != null) {
         if (value == null) {
            throw new Exception();
         } else {
            OperationLib.insertNullCheckSkipTo(body.getUnits(), value, stmt, tStmt);
            OperationLib.output(node, body, "SKIP-TWO-Continue");
            isContinue = true;
         }
      } else {
         throw new Exception();
      }
   }

   private static boolean isContinue(VFGNode node) {
      if (!JimpleLoopFinder.inLoop(node.getBody(), node.getStmt())) {
         return false;
      } else {
         Loop minLoop = JimpleLoopFinder.getMinLoop(node.getStmt());
         List<Stmt> loopStmts = minLoop.getLoopStatements();
         Iterator var4 = invalid.iterator();

         while(var4.hasNext()) {
            Unit unit = (Unit)var4.next();
            if (!loopStmts.contains(unit)) {
               setLoopRange(minLoop);
               return true;
            }
         }

         return false;
      }
   }

   private static void setLoopRange(Loop loop) {
      List<Stmt> loopStmts = loop.getLoopStatements();
      int start = 100000;
      int end = -1;
      Iterator var5 = loopStmts.iterator();

      while(var5.hasNext()) {
         Stmt stmt = (Stmt)var5.next();
         int lineNumber = stmt.getJavaSourceStartLineNumber();
         if (lineNumber > end) {
            end = lineNumber;
         }

         if (lineNumber < start) {
            start = lineNumber;
         }
      }

      setLoopRange(start, end);
   }

   private static boolean isApplicable(VFGNode node) {
      if (node.getNodeType() == 0) {
         return false;
      } else {
         return node.getETwo() != null;
      }
   }

   private static boolean isPracticable(VFGNode node) {
      if (!(node.getETwo() instanceof Local)) {
         return false;
      } else {
         Stmt skippedStmt = node.getStmt();
         invalid = DependencyAnalyzor.collectInvalidStmts(node.getBody(), skippedStmt);
         System.out.println("invalid: " + invalid);
         if (invalid.size() <= 1) {
            return false;
         } else {
            targetStmt = (Stmt)DependencyAnalyzor.findTargetStmt(node.getBody(), invalid);
            if (isContinue(node)) {
               return true;
            } else if (targetStmt == null) {
               return false;
            } else {
               return !(targetStmt instanceof ReturnVoidStmt);
            }
         }
      }
   }

   public static boolean solve(VFGNode node) {
      return isApplicable(node) & isPracticable(node);
   }

   private static void setLoopRange(int start, int end) {
      System.out.println("LoopRange: " + start + " - " + end);
      loopRange = new Pair(start, end);
   }

   private static void clearLoopRange() {
      loopRange = null;
   }

   public static Pair<Integer, Integer> getLoopRange() {
      return loopRange;
   }

   public static Stmt getTarget() {
      return targetStmt;
   }

   public static List<Unit> getInvalids() {
      return invalid;
   }
}
