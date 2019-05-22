package corg.vfix.parser.java;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.LiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import java.util.Iterator;

public class ExprParser {
   public static void main(Expression expr) {
      if (expr.isAnnotationExpr()) {
         parseAnnotationExpr((AnnotationExpr)expr);
      } else if (expr.isArrayAccessExpr()) {
         parseArrayAccessExpr((ArrayAccessExpr)expr);
      } else if (expr.isArrayCreationExpr()) {
         parseArrayCreationExpr((ArrayCreationExpr)expr);
      } else if (expr.isArrayInitializerExpr()) {
         parseArrayInitializerExpr((ArrayInitializerExpr)expr);
      } else if (expr.isAssignExpr()) {
         parseAssignExpr((AssignExpr)expr);
      } else if (expr.isBinaryExpr()) {
         parseBinaryExpr((BinaryExpr)expr);
      } else if (expr.isCastExpr()) {
         parseCastExpr((CastExpr)expr);
      } else if (expr.isClassExpr()) {
         parseClassExpr((ClassExpr)expr);
      } else if (expr.isConditionalExpr()) {
         parseConditionalExpr((ConditionalExpr)expr);
      } else if (expr.isEnclosedExpr()) {
         parseEnclosedExpr((EnclosedExpr)expr);
      } else if (expr.isFieldAccessExpr()) {
         parseFieldAccessExpr((FieldAccessExpr)expr);
      } else if (expr.isInstanceOfExpr()) {
         parseInstanceOfExpr((InstanceOfExpr)expr);
      } else if (expr.isLambdaExpr()) {
         parseLambdaExpr((LambdaExpr)expr);
      } else if (expr.isLiteralExpr()) {
         parseLiteralExpr((LiteralExpr)expr);
      } else if (expr.isMethodCallExpr()) {
         parseMethodCallExpr((MethodCallExpr)expr);
      } else if (expr.isMethodReferenceExpr()) {
         parseMethodReferenceExpr((MethodReferenceExpr)expr);
      } else if (expr.isNameExpr()) {
         parseNameExpr((NameExpr)expr);
      } else if (expr.isObjectCreationExpr()) {
         parseObjectCreationExpr((ObjectCreationExpr)expr);
      } else if (expr.isSuperExpr()) {
         parseSuperExpr((SuperExpr)expr);
      } else if (expr.isThisExpr()) {
         parseThisExpr((ThisExpr)expr);
      } else if (expr.isTypeExpr()) {
         parseTypeExpr((TypeExpr)expr);
      } else if (expr.isUnaryExpr()) {
         parseUnaryExpr((UnaryExpr)expr);
      } else if (expr.isVariableDeclarationExpr()) {
         parseVariableDeclarationExpr((VariableDeclarationExpr)expr);
      } else {
         System.out.println("not found");
      }

   }

   private static void parseAnnotationExpr(AnnotationExpr expr) {
      System.out.println("AnnotationExpr");
   }

   private static void parseArrayAccessExpr(ArrayAccessExpr expr) {
      System.out.println("ArrayAccessExpr");
      System.out.println("Name:" + expr.getName());
      main(expr.getName());
      System.out.println("Index:" + expr.getIndex());
      main(expr.getIndex());
   }

   private static void parseArrayCreationExpr(ArrayCreationExpr expr) {
      System.out.println("ArrayCreationExpr");
   }

   private static void parseArrayInitializerExpr(ArrayInitializerExpr expr) {
      System.out.println("ArrayInitializerExpr");
   }

   private static void parseAssignExpr(AssignExpr expr) {
      System.out.println("AssignExpr");
      System.out.println("Target: " + expr.getTarget());
      main(expr.getTarget());
      System.out.println("Value: " + expr.getValue());
      main(expr.getValue());
   }

   private static void parseBinaryExpr(BinaryExpr expr) {
      System.out.println("BinaryExpr");
   }

   private static void parseCastExpr(CastExpr expr) {
      System.out.println("CastExpr");
      System.out.println("Type: " + expr.getType());
      System.out.println("Exp: " + expr.getExpression());
      main(expr.getExpression());
   }

   private static void parseClassExpr(ClassExpr expr) {
      System.out.println("ClassExpr");
   }

   private static void parseConditionalExpr(ConditionalExpr expr) {
      System.out.println("ConditionalExpr");
   }

   private static void parseEnclosedExpr(EnclosedExpr expr) {
      System.out.println("EnclosedExpr");
   }

   private static void parseFieldAccessExpr(FieldAccessExpr expr) {
      System.out.println("FieldAccessExpr");
      System.out.println("Scope: " + expr.getScope());
      main(expr.getScope());
      System.out.println("Name: " + expr.getName());
   }

   private static void parseInstanceOfExpr(InstanceOfExpr expr) {
      System.out.println("InstanceOfExpr");
   }

   private static void parseLambdaExpr(LambdaExpr expr) {
      System.out.println("LambdaExpr");
   }

   private static void parseLiteralExpr(LiteralExpr expr) {
      System.out.println("LiteralExpr");
   }

   private static void parseMethodCallExpr(MethodCallExpr expr) {
      System.out.println("MethodCallExpr");
      if (expr.getScope().isPresent()) {
         System.out.println("Base: " + expr.getScope().get());
         main((Expression)expr.getScope().get());
      }

      System.out.println("MethodName: " + expr.getName());
      System.out.println("Args: " + expr.getArguments());
      Iterator var2 = expr.getArguments().iterator();

      while(var2.hasNext()) {
         Expression arg = (Expression)var2.next();
         main(arg);
      }

   }

   private static void parseMethodReferenceExpr(MethodReferenceExpr expr) {
      System.out.println("MethodReferenceExpr");
   }

   private static void parseNameExpr(NameExpr expr) {
      System.out.println("NameExpr");
      System.out.println("Name: " + expr.getName());
   }

   private static void parseObjectCreationExpr(ObjectCreationExpr expr) {
      System.out.println("ObjectCreationExpr");
      System.out.println("Args: " + expr.getArguments());
      Iterator var2 = expr.getArguments().iterator();

      while(var2.hasNext()) {
         Expression arg = (Expression)var2.next();
         main(arg);
      }

      if (expr.getScope().isPresent()) {
         System.out.println("Scope: " + expr.getScope().get());
      }

      System.out.println("Type: " + expr.getType());
      System.out.println("Type Args: " + expr.getTypeArguments());
   }

   private static void parseSuperExpr(SuperExpr expr) {
      System.out.println("SuperExpr");
   }

   private static void parseThisExpr(ThisExpr expr) {
      System.out.println("ThisExpr");
   }

   private static void parseTypeExpr(TypeExpr expr) {
      System.out.println("TypeExpr");
   }

   private static void parseUnaryExpr(UnaryExpr expr) {
      System.out.println("UnaryExpr");
   }

   private static void parseVariableDeclarationExpr(VariableDeclarationExpr expr) {
      System.out.println("VariableDeclarationExpr");
      Iterator var2 = expr.getVariables().iterator();

      while(var2.hasNext()) {
         VariableDeclarator vd = (VariableDeclarator)var2.next();
         System.out.println("Type: " + vd.getType());
         TypeParser.main(vd.getType());
         System.out.println("Name: " + vd.getName());
         if (vd.getInitializer().isPresent()) {
            System.out.println("Initializer: " + vd.getInitializer().get());
            main((Expression)vd.getInitializer().get());
         }
      }

   }

   public static void main(String[] args) {
      printIf("Annotation");
      printElseIf("ArrayAccess");
      printElseIf("ArrayCreation");
      printElseIf("ArrayInitializer");
      printElseIf("Assign");
      printElseIf("Binary");
      printElseIf("Cast");
      printElseIf("Class");
      printElseIf("Conditional");
      printElseIf("Enclosed");
      printElseIf("FieldAccess");
      printElseIf("InstanceOf");
      printElseIf("Lambda");
      printElseIf("Literal");
      printElseIf("MethodReference");
      printElseIf("Name");
      printElseIf("ObjectCreation");
      printElseIf("Super");
      printElseIf("This");
      printElseIf("Type");
      printElseIf("Unary");
      printElseIf("VariableDeclaration");
   }

   private static void printIf(String str) {
      System.out.println("private static void parse" + str + "Expr(" + str + "Expr expr){");
      System.out.println("    System.out.println(\"" + str + "Expr\");");
      System.out.println("}");
   }

   private static void printElseIf(String str) {
      System.out.println("private static void parse" + str + "Expr(" + str + "Expr expr){");
      System.out.println("    System.out.println(\"" + str + "Expr\");");
      System.out.println("}");
   }
}
