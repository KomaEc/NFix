package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.resolution.Value;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionClassDeclaration;
import com.github.javaparser.symbolsolver.resolution.SymbolDeclarator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

public abstract class AbstractJavaParserContext<N extends Node> implements Context {
   protected N wrappedNode;
   protected TypeSolver typeSolver;

   public static SymbolReference<ResolvedValueDeclaration> solveWith(SymbolDeclarator symbolDeclarator, String name) {
      Iterator var2 = symbolDeclarator.getSymbolDeclarations().iterator();

      ResolvedValueDeclaration decl;
      do {
         if (!var2.hasNext()) {
            return SymbolReference.unsolved(ResolvedValueDeclaration.class);
         }

         decl = (ResolvedValueDeclaration)var2.next();
      } while(!decl.getName().equals(name));

      return SymbolReference.solved(decl);
   }

   public AbstractJavaParserContext(N wrappedNode, TypeSolver typeSolver) {
      if (wrappedNode == null) {
         throw new NullPointerException();
      } else {
         this.wrappedNode = wrappedNode;
         this.typeSolver = typeSolver;
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         AbstractJavaParserContext<?> that = (AbstractJavaParserContext)o;
         return this.wrappedNode != null ? this.wrappedNode.equals(that.wrappedNode) : that.wrappedNode == null;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.wrappedNode != null ? this.wrappedNode.hashCode() : 0;
   }

   public Optional<ResolvedType> solveGenericType(String name, TypeSolver typeSolver) {
      Context parent = this.getParent();
      return parent == null ? Optional.empty() : parent.solveGenericType(name, typeSolver);
   }

   public final Context getParent() {
      Node parent = (Node)this.wrappedNode.getParentNode().orElse((Object)null);
      if (parent instanceof MethodCallExpr) {
         MethodCallExpr parentCall = (MethodCallExpr)parent;
         boolean found = false;
         if (parentCall.getArguments() != null) {
            Iterator var4 = parentCall.getArguments().iterator();

            while(var4.hasNext()) {
               Expression expression = (Expression)var4.next();
               if (expression == this.wrappedNode) {
                  found = true;
               }
            }
         }

         if (found) {
            Node notMethod;
            for(notMethod = parent; notMethod instanceof MethodCallExpr; notMethod = Navigator.requireParentNode(notMethod)) {
            }

            return JavaParserFactory.getContext(notMethod, this.typeSolver);
         }
      }

      Node notMethod;
      for(notMethod = parent; notMethod instanceof MethodCallExpr || notMethod instanceof FieldAccessExpr; notMethod = (Node)notMethod.getParentNode().orElse((Object)null)) {
      }

      if (notMethod == null) {
         return null;
      } else {
         return JavaParserFactory.getContext(notMethod, this.typeSolver);
      }
   }

   protected Optional<Value> solveWithAsValue(SymbolDeclarator symbolDeclarator, String name, TypeSolver typeSolver) {
      return symbolDeclarator.getSymbolDeclarations().stream().filter((d) -> {
         return d.getName().equals(name);
      }).map(Value::from).findFirst();
   }

   protected Collection<ResolvedReferenceTypeDeclaration> findTypeDeclarations(Optional<Expression> optScope, TypeSolver typeSolver) {
      if (!optScope.isPresent()) {
         ResolvedType typeOfScope = JavaParserFacade.get(typeSolver).getTypeOfThisIn(this.wrappedNode);
         return Collections.singletonList(typeOfScope.asReferenceType().getTypeDeclaration());
      } else {
         Expression scope = (Expression)optScope.get();
         if (scope instanceof NameExpr) {
            NameExpr scopeAsName = scope.asNameExpr();
            SymbolReference<ResolvedTypeDeclaration> symbolReference = this.solveType(scopeAsName.getName().getId(), typeSolver);
            if (symbolReference.isSolved() && ((ResolvedTypeDeclaration)symbolReference.getCorrespondingDeclaration()).isType()) {
               return Collections.singletonList(((ResolvedTypeDeclaration)symbolReference.getCorrespondingDeclaration()).asReferenceType());
            }
         }

         ResolvedType typeOfScope;
         try {
            typeOfScope = JavaParserFacade.get(typeSolver).getType(scope);
         } catch (Exception var8) {
            if (scope instanceof FieldAccessExpr) {
               FieldAccessExpr scopeName = (FieldAccessExpr)scope;
               if (this.solveType(scopeName.toString(), typeSolver).isSolved()) {
                  return Collections.emptyList();
               }
            }

            throw new UnsolvedSymbolException(scope.toString(), this.wrappedNode.toString(), var8);
         }

         if (typeOfScope.isWildcard()) {
            return !typeOfScope.asWildcard().isExtends() && !typeOfScope.asWildcard().isSuper() ? Collections.singletonList((new ReflectionClassDeclaration(Object.class, typeSolver)).asReferenceType()) : Collections.singletonList(typeOfScope.asWildcard().getBoundedType().asReferenceType().getTypeDeclaration());
         } else if (typeOfScope.isArray()) {
            return Collections.singletonList((new ReflectionClassDeclaration(Object.class, typeSolver)).asReferenceType());
         } else if (!typeOfScope.isTypeVariable()) {
            if (typeOfScope.isConstraint()) {
               return Collections.singletonList(typeOfScope.asConstraintType().getBound().asReferenceType().getTypeDeclaration());
            } else {
               return (Collection)(typeOfScope.isUnionType() ? (Collection)typeOfScope.asUnionType().getCommonAncestor().map(ResolvedReferenceType::getTypeDeclaration).map(Collections::singletonList).orElseThrow(() -> {
                  return new UnsolvedSymbolException("No common ancestor available for UnionType" + typeOfScope.describe());
               }) : Collections.singletonList(typeOfScope.asReferenceType().getTypeDeclaration()));
            }
         } else {
            Collection<ResolvedReferenceTypeDeclaration> result = new ArrayList();
            Iterator var12 = typeOfScope.asTypeParameter().getBounds().iterator();

            while(var12.hasNext()) {
               ResolvedTypeParameterDeclaration.Bound bound = (ResolvedTypeParameterDeclaration.Bound)var12.next();
               result.add(bound.getType().asReferenceType().getTypeDeclaration());
            }

            return result;
         }
      }
   }

   public N getWrappedNode() {
      return this.wrappedNode;
   }
}
