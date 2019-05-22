package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedArrayType;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.Iterator;

public class JavaParserSymbolDeclaration implements ResolvedValueDeclaration {
   private String name;
   private Node wrappedNode;
   private TypeSolver typeSolver;

   private JavaParserSymbolDeclaration(Node wrappedNode, String name, TypeSolver typeSolver) {
      this.name = name;
      this.wrappedNode = wrappedNode;
      this.typeSolver = typeSolver;
   }

   public static JavaParserFieldDeclaration field(VariableDeclarator wrappedNode, TypeSolver typeSolver) {
      return new JavaParserFieldDeclaration(wrappedNode, typeSolver);
   }

   public static JavaParserParameterDeclaration parameter(Parameter parameter, TypeSolver typeSolver) {
      return new JavaParserParameterDeclaration(parameter, typeSolver);
   }

   public static JavaParserSymbolDeclaration localVar(VariableDeclarator variableDeclarator, TypeSolver typeSolver) {
      return new JavaParserSymbolDeclaration(variableDeclarator, variableDeclarator.getName().getId(), typeSolver);
   }

   public static int getParamPos(Parameter parameter) {
      int pos = 0;
      Iterator var2 = Navigator.requireParentNode(parameter).getChildNodes().iterator();

      while(var2.hasNext()) {
         Node node = (Node)var2.next();
         if (node == parameter) {
            return pos;
         }

         if (node instanceof Parameter) {
            ++pos;
         }
      }

      return pos;
   }

   public static int getParamPos(Node node) {
      if (Navigator.requireParentNode(node) instanceof MethodCallExpr) {
         MethodCallExpr call = (MethodCallExpr)Navigator.requireParentNode(node);

         for(int i = 0; i < call.getArguments().size(); ++i) {
            if (call.getArguments().get(i) == node) {
               return i;
            }
         }

         throw new IllegalStateException();
      } else {
         throw new IllegalArgumentException();
      }
   }

   public String toString() {
      return "JavaParserSymbolDeclaration{name='" + this.name + '\'' + ", wrappedNode=" + this.wrappedNode + '}';
   }

   public String getName() {
      return this.name;
   }

   public boolean isField() {
      return false;
   }

   public boolean isParameter() {
      return false;
   }

   public boolean isType() {
      return false;
   }

   public ResolvedType getType() {
      if (this.wrappedNode instanceof Parameter) {
         Parameter parameter = (Parameter)this.wrappedNode;
         if (Navigator.requireParentNode(this.wrappedNode) instanceof LambdaExpr) {
            int pos = getParamPos(parameter);
            ResolvedType lambdaType = JavaParserFacade.get(this.typeSolver).getType(Navigator.requireParentNode(this.wrappedNode));
            throw new UnsupportedOperationException(this.wrappedNode.getClass().getCanonicalName());
         } else {
            ResolvedType rawType;
            if (parameter.getType() instanceof PrimitiveType) {
               rawType = ResolvedPrimitiveType.byName(((PrimitiveType)parameter.getType()).getType().name());
            } else {
               rawType = JavaParserFacade.get(this.typeSolver).convertToUsage(parameter.getType(), this.wrappedNode);
            }

            return (ResolvedType)(parameter.isVarArgs() ? new ResolvedArrayType(rawType) : rawType);
         }
      } else {
         if (this.wrappedNode instanceof VariableDeclarator) {
            VariableDeclarator variableDeclarator = (VariableDeclarator)this.wrappedNode;
            if (Navigator.requireParentNode(this.wrappedNode) instanceof VariableDeclarationExpr) {
               return JavaParserFacade.get(this.typeSolver).convert(variableDeclarator.getType(), JavaParserFactory.getContext(this.wrappedNode, this.typeSolver));
            }

            if (Navigator.requireParentNode(this.wrappedNode) instanceof FieldDeclaration) {
               return JavaParserFacade.get(this.typeSolver).convert(variableDeclarator.getType(), JavaParserFactory.getContext(this.wrappedNode, this.typeSolver));
            }
         }

         throw new UnsupportedOperationException(this.wrappedNode.getClass().getCanonicalName());
      }
   }

   public ResolvedTypeDeclaration asType() {
      throw new UnsupportedOperationException(this.getClass().getCanonicalName() + ": wrapping " + this.getWrappedNode().getClass().getCanonicalName());
   }

   public Node getWrappedNode() {
      return this.wrappedNode;
   }
}
