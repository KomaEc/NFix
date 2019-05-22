package corg.vfix.parser.java;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.UnparsableStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import java.util.Iterator;

public class StmtParser {
   public static void main(Statement stmt) {
      System.out.println();
      System.out.println(stmt);
      if (stmt.isAssertStmt()) {
         parseAssertStmt((AssertStmt)stmt);
      } else if (stmt.isBlockStmt()) {
         parseBlockStmt((BlockStmt)stmt);
      } else if (stmt.isBreakStmt()) {
         parseBreakStmt((BreakStmt)stmt);
      } else if (stmt.isContinueStmt()) {
         parseContinueStmt((ContinueStmt)stmt);
      } else if (stmt.isDoStmt()) {
         parseDoStmt((DoStmt)stmt);
      } else if (stmt.isEmptyStmt()) {
         parseEmptyStmt((EmptyStmt)stmt);
      } else if (stmt.isExplicitConstructorInvocationStmt()) {
         parseExplicitConstructorInvocationStmt((ExplicitConstructorInvocationStmt)stmt);
      } else if (stmt.isExpressionStmt()) {
         parseExpressionStmt((ExpressionStmt)stmt);
      } else if (stmt.isForeachStmt()) {
         parseForeachStmt((ForeachStmt)stmt);
      } else if (stmt.isForStmt()) {
         parseForStmt((ForStmt)stmt);
      } else if (stmt.isIfStmt()) {
         parseIfStmt((IfStmt)stmt);
      } else if (stmt.isLabeledStmt()) {
         parseLabeledStmt((LabeledStmt)stmt);
      } else if (stmt.isLocalClassDeclarationStmt()) {
         parseLocalClassDeclarationStmt((LocalClassDeclarationStmt)stmt);
      } else if (stmt.isReturnStmt()) {
         parseReturnStmt((ReturnStmt)stmt);
      } else if (stmt.isSwitchEntryStmt()) {
         parseSwitchEntryStmt((SwitchEntryStmt)stmt);
      } else if (stmt.isSwitchStmt()) {
         parseSwitchStmt((SwitchStmt)stmt);
      } else if (stmt.isSynchronizedStmt()) {
         parseSynchronizedStmt((SynchronizedStmt)stmt);
      } else if (stmt.isThrowStmt()) {
         parseThrowStmt((ThrowStmt)stmt);
      } else if (stmt.isTryStmt()) {
         parseTryStmt((TryStmt)stmt);
      } else if (stmt.isUnparsableStmt()) {
         parseUnparsableStmt((UnparsableStmt)stmt);
      } else if (stmt.isWhileStmt()) {
         parseWhileStmt((WhileStmt)stmt);
      }

   }

   private static void parseUnparsableStmt(UnparsableStmt stmt) {
      System.out.println("UnparsableStmt");
   }

   private static void parseWhileStmt(WhileStmt stmt) {
      System.out.println("WhileStmt");
   }

   private static void parseTryStmt(TryStmt stmt) {
      System.out.println("TryStmt");
   }

   private static void parseThrowStmt(ThrowStmt stmt) {
      System.out.println("ThrowStmt");
   }

   private static void parseSynchronizedStmt(SynchronizedStmt stmt) {
      System.out.println("SynchronizedStmt");
   }

   private static void parseSwitchStmt(SwitchStmt stmt) {
      System.out.println("SwitchStmt");
   }

   private static void parseSwitchEntryStmt(SwitchEntryStmt stmt) {
      System.out.println("SwitchEntryStmt");
   }

   private static void parseReturnStmt(ReturnStmt stmt) {
      System.out.println("ReturnStmt");
      if (stmt.getExpression().isPresent()) {
         System.out.println("Exp: " + stmt.getExpression().get());
         ExprParser.main((Expression)stmt.getExpression().get());
      }

   }

   private static void parseLocalClassDeclarationStmt(LocalClassDeclarationStmt stmt) {
      System.out.println("LocalClassDeclarationStmt");
   }

   private static void parseLabeledStmt(LabeledStmt stmt) {
      System.out.println("LabeledStmt");
   }

   private static void parseIfStmt(IfStmt stmt) {
      System.out.println("IfStmt");
      System.out.println("Condition: " + stmt.getCondition());
      ExprParser.main(stmt.getCondition());
      if (stmt.hasThenBlock()) {
         System.out.println("Then: " + stmt.getThenStmt());
         main(stmt.getThenStmt());
      }

      if (stmt.hasElseBlock()) {
         System.out.println("Else: " + stmt.getElseStmt().get());
         main((Statement)stmt.getElseStmt().get());
      }

   }

   private static void parseForStmt(ForStmt stmt) {
      System.out.println("ForStmt");
   }

   private static void parseForeachStmt(ForeachStmt stmt) {
      System.out.println("ForeachStmt");
   }

   private static void parseExpressionStmt(ExpressionStmt stmt) {
      System.out.println("ExpressionStmt");
      ExprParser.main(stmt.getExpression());
   }

   private static void parseExplicitConstructorInvocationStmt(ExplicitConstructorInvocationStmt stmt) {
      System.out.println("ExplicitConstructorInvocationStmt");
   }

   private static void parseEmptyStmt(EmptyStmt stmt) {
      System.out.println("EmptyStmt");
   }

   private static void parseDoStmt(DoStmt stmt) {
      System.out.println("DoStmt");
   }

   private static void parseContinueStmt(ContinueStmt stmt) {
      System.out.println("ContinueStmt");
   }

   private static void parseBreakStmt(BreakStmt stmt) {
      System.out.println("BreakStmt");
   }

   private static void parseBlockStmt(BlockStmt stmt) {
      System.out.println("BlockStmt");
      Iterator var2 = stmt.getStatements().iterator();

      while(var2.hasNext()) {
         Statement s = (Statement)var2.next();
         main(s);
      }

   }

   private static void parseAssertStmt(AssertStmt stmt) {
      System.out.println("AssertStmt");
   }

   public static void main(String[] args) {
      String Type = "AssertStmt";
      System.out.println("if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "BlockStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "BreakStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "ContinueStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "DoStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "EmptyStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "ExplicitConstructorInvocationStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "ExpressionStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "ForeachStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "ForStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "IfStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "LabeledStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "LocalClassDeclarationStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "ReturnStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "SwitchEntryStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "SwitchStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "SynchronizedStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "ThrowStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "TryStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "UnparsableStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "WhileStmt";
      System.out.println("else if(stmt.is" + Type + "())");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
   }
}
