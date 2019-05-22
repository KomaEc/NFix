package corg.vfix.pg.java.visitor;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class GetStmtByLineVisitor extends VoidVisitorAdapter<JavaBox> {
   private boolean finished = false;

   public void visit(ExplicitConstructorInvocationStmt stmt, JavaBox box) {
      if (this.inRange((Range)stmt.getRange().get(), box.line)) {
         box.stmt = stmt;
         this.finished = true;
      }

   }

   public void visit(ExpressionStmt stmt, JavaBox box) {
      if (this.inRange((Range)stmt.getRange().get(), box.line)) {
         box.stmt = stmt;
         this.finished = true;
      }

   }

   private boolean inRange(Range range, int lineNumber) {
      int begin = range.begin.line;
      int end = range.end.line;
      return lineNumber >= begin && lineNumber <= end;
   }

   public void visit(MethodDeclaration md, JavaBox box) {
      if (!this.finished) {
         if (this.inRange((Range)md.getRange().get(), box.line)) {
            super.visit((MethodDeclaration)md, box);
         }
      }
   }

   public void visit(ReturnStmt stmt, JavaBox box) {
      if (this.inRange((Range)stmt.getRange().get(), box.line)) {
         box.stmt = stmt;
      }

   }

   public void visit(IfStmt stmt, JavaBox box) {
      if (box.line == ((Position)stmt.getBegin().get()).line) {
         box.stmt = stmt;
      }

      super.visit((IfStmt)stmt, box);
   }

   public void visit(BlockStmt stmt, JavaBox box) {
      if (!this.finished) {
         super.visit((BlockStmt)stmt, box);
      }
   }

   public void visit(WhileStmt stmt, JavaBox box) {
      super.visit((WhileStmt)stmt, box);
      if (box.line == ((Position)stmt.getBegin().get()).line) {
         box.stmt = stmt;
      }

   }

   public void visit(ForStmt stmt, JavaBox box) {
      super.visit((ForStmt)stmt, box);
      if (box.line == ((Position)stmt.getBegin().get()).line) {
         box.stmt = stmt;
      }

   }
}
