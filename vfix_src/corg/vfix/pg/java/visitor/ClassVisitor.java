package corg.vfix.pg.java.visitor;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.Pair;
import java.util.ArrayList;

public class ClassVisitor extends VoidVisitorAdapter<ArrayList<Pair<String, Range>>> {
   public void visit(ClassOrInterfaceDeclaration cd, ArrayList<Pair<String, Range>> clss) {
      super.visit((ClassOrInterfaceDeclaration)cd, clss);
      String name = cd.getName().toString();
      Range range = (Range)cd.getRange().get();
      Pair<String, Range> cls = new Pair(name, range);
      clss.add(cls);
   }
}
