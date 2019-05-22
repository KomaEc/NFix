package corg.vfix.pg.java.visitor;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import corg.vfix.pg.java.JavaNode;

public class JavaNodeMtdVisitor extends VoidVisitorAdapter<JavaNode> {
   public void visit(ConstructorDeclaration md, JavaNode node) {
      if (this.inRange((Range)md.getRange().get(), node.getLineNumber())) {
         node.setMethod(md);
         super.visit((ConstructorDeclaration)md, node);
      }
   }

   public void visit(MethodDeclaration md, JavaNode node) {
      if (this.inRange((Range)md.getRange().get(), node.getLineNumber())) {
         node.setMethod(md);
         super.visit((MethodDeclaration)md, node);
      }
   }

   private boolean inRange(Range range, int line) {
      int beginLine = range.begin.line;
      int endLine = range.end.line;
      return line >= beginLine && line <= endLine;
   }
}
