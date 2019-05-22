package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedLambdaConstraintType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.logic.FunctionalInterfaceLogic;
import com.github.javaparser.symbolsolver.logic.InferenceContext;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.resolution.Value;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.reflectionmodel.MyObjectProvider;
import com.github.javaparser.symbolsolver.resolution.SymbolDeclarator;
import com.github.javaparser.utils.Pair;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LambdaExprContext extends AbstractJavaParserContext<LambdaExpr> {
   public LambdaExprContext(LambdaExpr wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public Optional<Value> solveSymbolAsValue(String name, TypeSolver typeSolver) {
      Iterator var3 = ((LambdaExpr)this.wrappedNode).getParameters().iterator();

      while(var3.hasNext()) {
         Parameter parameter = (Parameter)var3.next();
         SymbolDeclarator sb = JavaParserFactory.getSymbolDeclarator(parameter, typeSolver);
         int index = 0;

         for(Iterator var7 = sb.getSymbolDeclarations().iterator(); var7.hasNext(); ++index) {
            ResolvedValueDeclaration decl = (ResolvedValueDeclaration)var7.next();
            if (decl.getName().equals(name)) {
               ResolvedType lambdaType;
               if (Navigator.requireParentNode(this.wrappedNode) instanceof MethodCallExpr) {
                  MethodCallExpr methodCallExpr = (MethodCallExpr)Navigator.requireParentNode(this.wrappedNode);
                  MethodUsage methodUsage = JavaParserFacade.get(typeSolver).solveMethodAsUsage(methodCallExpr);
                  int i = this.pos(methodCallExpr, (Expression)this.wrappedNode);
                  lambdaType = (ResolvedType)methodUsage.getParamTypes().get(i);
                  Optional<MethodUsage> functionalMethodOpt = FunctionalInterfaceLogic.getFunctionalMethod(lambdaType);
                  if (!functionalMethodOpt.isPresent()) {
                     return Optional.empty();
                  }

                  MethodUsage functionalMethod = (MethodUsage)functionalMethodOpt.get();
                  InferenceContext inferenceContext = new InferenceContext(MyObjectProvider.INSTANCE);
                  inferenceContext.addPair(lambdaType, new ReferenceTypeImpl(lambdaType.asReferenceType().getTypeDeclaration(), typeSolver));
                  boolean found = false;

                  int lambdaParamIndex;
                  for(lambdaParamIndex = 0; lambdaParamIndex < ((LambdaExpr)this.wrappedNode).getParameters().size(); ++lambdaParamIndex) {
                     if (((LambdaExpr)this.wrappedNode).getParameter(lambdaParamIndex).getName().getIdentifier().equals(name)) {
                        found = true;
                        break;
                     }
                  }

                  if (!found) {
                     return Optional.empty();
                  }

                  ResolvedType argType = inferenceContext.resolve(inferenceContext.addSingle(functionalMethod.getParamType(lambdaParamIndex)));
                  ResolvedLambdaConstraintType conType;
                  if (argType.isWildcard()) {
                     conType = ResolvedLambdaConstraintType.bound(argType.asWildcard().getBoundedType());
                  } else {
                     conType = ResolvedLambdaConstraintType.bound(argType);
                  }

                  Value value = new Value(conType, name);
                  return Optional.of(value);
               }

               if (Navigator.requireParentNode(this.wrappedNode) instanceof VariableDeclarator) {
                  VariableDeclarator variableDeclarator = (VariableDeclarator)Navigator.requireParentNode(this.wrappedNode);
                  ResolvedType t = JavaParserFacade.get(typeSolver).convertToUsageVariableType(variableDeclarator);
                  Optional<MethodUsage> functionalMethod = FunctionalInterfaceLogic.getFunctionalMethod(t);
                  if (!functionalMethod.isPresent()) {
                     throw new UnsupportedOperationException();
                  }

                  lambdaType = ((MethodUsage)functionalMethod.get()).getParamType(index);
                  Map<ResolvedTypeParameterDeclaration, ResolvedType> inferredTypes = new HashMap();
                  if (lambdaType.isReferenceType()) {
                     Iterator var14 = lambdaType.asReferenceType().getTypeParametersMap().iterator();

                     while(var14.hasNext()) {
                        Pair<ResolvedTypeParameterDeclaration, ResolvedType> entry = (Pair)var14.next();
                        if (((ResolvedType)entry.b).isTypeVariable() && ((ResolvedType)entry.b).asTypeParameter().declaredOnType()) {
                           ResolvedType ot = t.asReferenceType().typeParametersMap().getValue((ResolvedTypeParameterDeclaration)entry.a);
                           lambdaType = lambdaType.replaceTypeVariables((ResolvedTypeParameterDeclaration)entry.a, ot, inferredTypes);
                        }
                     }
                  } else if (lambdaType.isTypeVariable() && lambdaType.asTypeParameter().declaredOnType()) {
                     lambdaType = t.asReferenceType().typeParametersMap().getValue(lambdaType.asTypeParameter());
                  }

                  Value value = new Value(lambdaType, name);
                  return Optional.of(value);
               }

               throw new UnsupportedOperationException();
            }
         }
      }

      return this.getParent().solveSymbolAsValue(name, typeSolver);
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      Iterator var3 = ((LambdaExpr)this.wrappedNode).getParameters().iterator();

      SymbolReference symbolReference;
      do {
         if (!var3.hasNext()) {
            return this.getParent().solveSymbol(name, typeSolver);
         }

         Parameter parameter = (Parameter)var3.next();
         SymbolDeclarator sb = JavaParserFactory.getSymbolDeclarator(parameter, typeSolver);
         symbolReference = solveWith(sb, name);
      } while(!symbolReference.isSolved());

      return symbolReference;
   }

   public SymbolReference<ResolvedTypeDeclaration> solveType(String name, TypeSolver typeSolver) {
      return this.getParent().solveType(name, typeSolver);
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      return this.getParent().solveMethod(name, argumentsTypes, false, typeSolver);
   }

   public List<Parameter> parametersExposedToChild(Node child) {
      return (List)(child == ((LambdaExpr)this.wrappedNode).getBody() ? ((LambdaExpr)this.wrappedNode).getParameters() : Collections.emptyList());
   }

   protected final Optional<Value> solveWithAsValue(SymbolDeclarator symbolDeclarator, String name, TypeSolver typeSolver) {
      Iterator var4 = symbolDeclarator.getSymbolDeclarations().iterator();

      ResolvedValueDeclaration decl;
      do {
         if (!var4.hasNext()) {
            return Optional.empty();
         }

         decl = (ResolvedValueDeclaration)var4.next();
      } while(!decl.getName().equals(name));

      throw new UnsupportedOperationException();
   }

   private int pos(MethodCallExpr callExpr, Expression param) {
      int i = 0;

      for(Iterator var4 = callExpr.getArguments().iterator(); var4.hasNext(); ++i) {
         Expression p = (Expression)var4.next();
         if (p == param) {
            return i;
         }
      }

      throw new IllegalArgumentException();
   }
}
