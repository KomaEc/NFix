package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.Optional;

public class JavaParserFieldDeclaration implements ResolvedFieldDeclaration {
   private VariableDeclarator variableDeclarator;
   private FieldDeclaration wrappedNode;
   private TypeSolver typeSolver;

   public JavaParserFieldDeclaration(VariableDeclarator variableDeclarator, TypeSolver typeSolver) {
      if (typeSolver == null) {
         throw new IllegalArgumentException("typeSolver should not be null");
      } else {
         this.variableDeclarator = variableDeclarator;
         this.typeSolver = typeSolver;
         if (!(Navigator.requireParentNode(variableDeclarator) instanceof FieldDeclaration)) {
            throw new IllegalStateException(Navigator.requireParentNode(variableDeclarator).getClass().getCanonicalName());
         } else {
            this.wrappedNode = (FieldDeclaration)Navigator.requireParentNode(variableDeclarator);
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public JavaParserFieldDeclaration(EnumConstantDeclaration enumConstantDeclaration, TypeSolver typeSolver) {
      throw new UnsupportedOperationException();
   }

   public ResolvedType getType() {
      return JavaParserFacade.get(this.typeSolver).convert(this.variableDeclarator.getType(), (Node)this.wrappedNode);
   }

   public String getName() {
      return this.variableDeclarator.getName().getId();
   }

   public boolean isStatic() {
      return this.wrappedNode.getModifiers().contains(Modifier.STATIC);
   }

   public boolean isField() {
      return true;
   }

   public FieldDeclaration getWrappedNode() {
      return this.wrappedNode;
   }

   public VariableDeclarator getVariableDeclarator() {
      return this.variableDeclarator;
   }

   public String toString() {
      return "JavaParserFieldDeclaration{" + this.getName() + "}";
   }

   public AccessSpecifier accessSpecifier() {
      return AstResolutionUtils.toAccessLevel(this.wrappedNode.getModifiers());
   }

   public ResolvedTypeDeclaration declaringType() {
      Optional<TypeDeclaration> typeDeclaration = this.wrappedNode.findAncestor(TypeDeclaration.class);
      if (typeDeclaration.isPresent()) {
         return JavaParserFacade.get(this.typeSolver).getTypeDeclaration((TypeDeclaration)typeDeclaration.get());
      } else {
         throw new IllegalStateException();
      }
   }
}
