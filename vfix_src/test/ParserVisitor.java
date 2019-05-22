package test;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import corg.vfix.parser.java.StmtParser;

public class ParserVisitor extends VoidVisitorAdapter<Void> {
   public void visit(MethodDeclaration md, Void arg) {
      super.visit((MethodDeclaration)md, (Object)null);
      if (md.getBody().isPresent()) {
         StmtParser.main((Statement)md.getBody().get());
      }

   }

   public boolean inRange(Range range, int line) {
      int beginLine = range.begin.line;
      int endLine = range.end.line;
      return line >= beginLine && line <= endLine;
   }
}
