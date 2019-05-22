package test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.stmt.Statement;
import corg.vfix.parser.java.StmtParser;

public class MainTest {
   public static void main(String[] args) {
      String str = "byte[] b;";
      Statement stmt = JavaParser.parseStatement(str);
      StmtParser.main(stmt);
   }
}
