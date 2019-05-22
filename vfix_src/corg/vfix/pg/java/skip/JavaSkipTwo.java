package corg.vfix.pg.java.skip;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.utils.Pair;
import corg.vfix.pg.java.JavaNode;
import corg.vfix.pg.java.JavaOperationLib;
import corg.vfix.pg.java.visitor.GetBlockByRangeVisitor;
import corg.vfix.pg.java.visitor.GetStmtByLineVisitor;
import corg.vfix.pg.java.visitor.JavaBox;
import corg.vfix.pg.jimple.OperationLib;
import corg.vfix.pg.jimple.skip.RuleSkipTwo;
import corg.vfix.sa.vfg.VFGNode;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Unit;
import soot.jimple.Stmt;

public class JavaSkipTwo {
   public static void patch(JavaNode node) throws Exception {
      if (RuleSkipTwo.isContinue()) {
         if (inLoop(node.getStatement())) {
            patchContinue(node);
         } else {
            patchSkipOutLoop(node);
         }
      } else {
         patchSkip(node);
      }

   }

   private static boolean inLoop(Statement stmt) {
      int stmtStart = ((Range)stmt.getRange().get()).begin.line;
      int stmtEnd = ((Range)stmt.getRange().get()).end.line;
      int loopStart = (Integer)RuleSkipTwo.getLoopRange().a;
      int loopEnd = (Integer)RuleSkipTwo.getLoopRange().b;
      System.out.println("Statement Range: " + stmtStart + "-" + stmtEnd);
      System.out.println("Loop Range: " + loopStart + "-" + loopEnd);
      return stmtStart > loopStart;
   }

   public static void patchSkip(JavaNode node) throws Exception {
      Pair<Integer, Integer> range = getInvalidRange(node);
      BlockStmt body = getBlockStmtByRange(node, range, node.getStatement());
      JavaOperationLib.insertNullCheckSkip(body, node.getStatement(), range, node.getETwo());
      String filename = JavaOperationLib.name2Path(node.getVFGNode().getClsName());
      OperationLib.outputJavaFile(node.getCompilationUnit(), filename);
   }

   public static void patchSkipOutLoop(JavaNode node) throws Exception {
      Pair<Integer, Integer> range = RuleSkipTwo.getLoopRange();
      BlockStmt body = getBlockStmtByRange(node, range, node.getStatement());
      System.out.println("Range: " + range);
      System.out.println("Body: " + body);
      JavaOperationLib.insertNullCheckSkip(body, node.getStatement(), range, node.getETwo());
      String filename = JavaOperationLib.name2Path(node.getVFGNode().getClsName());
      OperationLib.outputJavaFile(node.getCompilationUnit(), filename);
   }

   private static void patchContinue(JavaNode node) throws FileNotFoundException {
      Pair<Integer, Integer> range = new Pair(node.getLineNumber(), node.getLineNumber());
      BlockStmt body = getBlockStmtByRange(node, range, node.getStatement());
      JavaOperationLib.insertNullCheckContinue(body, node.getStatement(), node.getETwo());
      String filename = JavaOperationLib.name2Path(node.getVFGNode().getClsName());
      OperationLib.outputJavaFile(node.getCompilationUnit(), filename);
   }

   public static void patch(VFGNode vfgnode) throws Exception {
      patch(new JavaNode(vfgnode));
   }

   private static Pair<Integer, Integer> getInvalidRange(JavaNode node) {
      List<Unit> invalidStmts = RuleSkipTwo.getInvalids();
      ArrayList<Statement> invalids = new ArrayList();
      Iterator var4 = invalidStmts.iterator();

      while(var4.hasNext()) {
         Unit unit = (Unit)var4.next();
         Statement s = getTargetStmt(node.getCompilationUnit(), (Stmt)unit);
         if (s != null && !invalids.contains(s)) {
            invalids.add(s);
         }
      }

      return getRange(invalids);
   }

   private static Pair<Integer, Integer> getRange(ArrayList<Statement> invalids) {
      int begin = 1000000;
      int end = -2;
      Iterator var4 = invalids.iterator();

      while(var4.hasNext()) {
         Statement stmt = (Statement)var4.next();
         int tmpBegin = ((Position)stmt.getBegin().get()).line;
         int tmpEnd = ((Position)stmt.getEnd().get()).line;
         if (begin > tmpBegin) {
            begin = tmpBegin;
         }

         if (end < tmpEnd) {
            end = tmpEnd;
         }
      }

      return new Pair(begin, end);
   }

   public static Statement getTargetStmt(CompilationUnit cu, Stmt targetStmt) {
      JavaBox javaBox = new JavaBox();
      javaBox.line = targetStmt.getJavaSourceStartLineNumber();
      GetStmtByLineVisitor gsvlVisitor = new GetStmtByLineVisitor();
      gsvlVisitor.visit((CompilationUnit)cu, (Object)javaBox);
      return javaBox.stmt;
   }

   public static BlockStmt getBlockStmtByRange(JavaNode node, Pair<Integer, Integer> range, Statement stmt) {
      JavaBox box = new JavaBox();
      box.stmt = stmt;
      box.begin = (Integer)range.a;
      box.end = (Integer)range.b;
      GetBlockByRangeVisitor visitor = new GetBlockByRangeVisitor();
      visitor.visit((CompilationUnit)node.getCompilationUnit(), (Object)box);
      return box.blockStmt;
   }
}
