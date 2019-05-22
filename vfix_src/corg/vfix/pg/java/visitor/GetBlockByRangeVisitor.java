package corg.vfix.pg.java.visitor;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class GetBlockByRangeVisitor extends VoidVisitorAdapter<JavaBox> {
   public void visit(MethodDeclaration md, JavaBox box) {
      if (md.getBody().isPresent()) {
         BlockStmt block = (BlockStmt)md.getBody().get();
         Range range = (Range)block.getRange().get();
         if (range.begin.line <= box.begin && range.end.line >= box.end) {
            super.visit((MethodDeclaration)md, box);
         }
      }
   }

   public void visit(BlockStmt stmt, JavaBox box) {
      int begin = ((Position)stmt.getBegin().get()).line;
      int end = ((Position)stmt.getEnd().get()).line;
      int curBegin = -1;
      int curEnd = 1000000;
      if (box.blockStmt != null) {
         curBegin = ((Position)box.blockStmt.getBegin().get()).line;
         curEnd = ((Position)box.blockStmt.getEnd().get()).line;
      }

      int stmtLength = 0;
      if (box.stmt != null) {
         stmtLength = box.stmt.toString().length();
      }

      if (begin >= curBegin && end <= curEnd && begin <= box.begin && end >= box.end && stmt.toString().length() >= stmtLength) {
         box.blockStmt = stmt;
      }

      super.visit((BlockStmt)stmt, box);
   }
}
