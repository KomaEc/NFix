package test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.io.File;
import java.io.FileNotFoundException;

public class MyJavaParser {
   public static void main(String[] args) throws FileNotFoundException {
      String filename = "/home/xuezheng/workspace-kepler/vfix/src/main/java/test/Demo.java";
      CompilationUnit cu = JavaParser.parse(new File(filename));
      ParserVisitor parserVisitor = new ParserVisitor();
      parserVisitor.visit((CompilationUnit)cu, (Object)null);
   }
}
