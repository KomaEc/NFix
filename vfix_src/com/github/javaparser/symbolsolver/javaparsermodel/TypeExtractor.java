package com.github.javaparser.symbolsolver.javaparsermodel;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedClassDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedArrayType;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedVoidType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserSymbolDeclaration;
import com.github.javaparser.symbolsolver.logic.FunctionalInterfaceLogic;
import com.github.javaparser.symbolsolver.logic.InferenceContext;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.resolution.Value;
import com.github.javaparser.symbolsolver.model.typesystem.NullType;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.reflectionmodel.MyObjectProvider;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionClassDeclaration;
import com.github.javaparser.symbolsolver.resolution.SymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.Log;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;

public class TypeExtractor extends DefaultVisitorAdapter {
   private TypeSolver typeSolver;
   private JavaParserFacade facade;

   public TypeExtractor(TypeSolver typeSolver, JavaParserFacade facade) {
      this.typeSolver = typeSolver;
      this.facade = facade;
   }

   public ResolvedType visit(VariableDeclarator node, Boolean solveLambdas) {
      if (Navigator.requireParentNode(node) instanceof FieldDeclaration) {
         return this.facade.convertToUsageVariableType(node);
      } else if (Navigator.requireParentNode(node) instanceof VariableDeclarationExpr) {
         return this.facade.convertToUsageVariableType(node);
      } else {
         throw new UnsupportedOperationException(Navigator.requireParentNode(node).getClass().getCanonicalName());
      }
   }

   public ResolvedType visit(Parameter node, Boolean solveLambdas) {
      if (node.getType() instanceof UnknownType) {
         throw new IllegalStateException("Parameter has unknown type: " + node);
      } else {
         return this.facade.convertToUsage(node.getType(), (Node)node);
      }
   }

   public ResolvedType visit(ArrayAccessExpr node, Boolean solveLambdas) {
      ResolvedType arrayUsageType = (ResolvedType)node.getName().accept(this, solveLambdas);
      return arrayUsageType.isArray() ? ((ResolvedArrayType)arrayUsageType).getComponentType() : arrayUsageType;
   }

   public ResolvedType visit(ArrayCreationExpr node, Boolean solveLambdas) {
      ResolvedType res = this.facade.convertToUsage(node.getElementType(), JavaParserFactory.getContext(node, this.typeSolver));

      for(int i = 0; i < node.getLevels().size(); ++i) {
         res = new ResolvedArrayType((ResolvedType)res);
      }

      return (ResolvedType)res;
   }

   public ResolvedType visit(ArrayInitializerExpr node, Boolean solveLambdas) {
      throw new UnsupportedOperationException(node.getClass().getCanonicalName());
   }

   public ResolvedType visit(AssignExpr node, Boolean solveLambdas) {
      return (ResolvedType)node.getTarget().accept(this, solveLambdas);
   }

   public ResolvedType visit(BinaryExpr node, Boolean solveLambdas) {
      switch(node.getOperator()) {
      case PLUS:
      case MINUS:
      case DIVIDE:
      case MULTIPLY:
         return this.facade.getBinaryTypeConcrete(node.getLeft(), node.getRight(), solveLambdas, node.getOperator());
      case LESS_EQUALS:
      case LESS:
      case GREATER:
      case GREATER_EQUALS:
      case EQUALS:
      case NOT_EQUALS:
      case OR:
      case AND:
         return ResolvedPrimitiveType.BOOLEAN;
      case BINARY_AND:
      case BINARY_OR:
      case SIGNED_RIGHT_SHIFT:
      case UNSIGNED_RIGHT_SHIFT:
      case LEFT_SHIFT:
      case REMAINDER:
      case XOR:
         return (ResolvedType)node.getLeft().accept(this, solveLambdas);
      default:
         throw new UnsupportedOperationException("Operator " + node.getOperator().name());
      }
   }

   public ResolvedType visit(CastExpr node, Boolean solveLambdas) {
      return this.facade.convertToUsage(node.getType(), JavaParserFactory.getContext(node, this.typeSolver));
   }

   public ResolvedType visit(ClassExpr node, Boolean solveLambdas) {
      Type astType = node.getType();
      ResolvedType jssType = this.facade.convertToUsage(astType, (Node)node.getType());
      return new ReferenceTypeImpl(new ReflectionClassDeclaration(Class.class, this.typeSolver), ImmutableList.of(jssType), this.typeSolver);
   }

   public ResolvedType visit(ConditionalExpr node, Boolean solveLambdas) {
      return (ResolvedType)node.getThenExpr().accept(this, solveLambdas);
   }

   public ResolvedType visit(EnclosedExpr node, Boolean solveLambdas) {
      return (ResolvedType)node.getInner().accept(this, solveLambdas);
   }

   private ResolvedType solveDotExpressionType(ResolvedReferenceTypeDeclaration parentType, FieldAccessExpr node) {
      if (parentType.isEnum() && parentType.asEnum().hasEnumConstant(node.getName().getId())) {
         return parentType.asEnum().getEnumConstant(node.getName().getId()).getType();
      } else if (parentType.hasField(node.getName().getId())) {
         return parentType.getField(node.getName().getId()).getType();
      } else if (parentType.hasInternalType(node.getName().getId())) {
         return new ReferenceTypeImpl(parentType.getInternalType(node.getName().getId()), this.typeSolver);
      } else {
         throw new com.github.javaparser.resolution.UnsolvedSymbolException(node.getName().getId());
      }
   }

   public ResolvedType visit(FieldAccessExpr node, Boolean solveLambdas) {
      if (!(node.getScope() instanceof NameExpr) && !(node.getScope() instanceof FieldAccessExpr)) {
         SymbolReference sr;
         if (node.getScope() instanceof ThisExpr) {
            sr = this.facade.solve((ThisExpr)node.getScope());
            if (sr.isSolved()) {
               ResolvedTypeDeclaration correspondingDeclaration = (ResolvedTypeDeclaration)sr.getCorrespondingDeclaration();
               if (correspondingDeclaration instanceof ResolvedReferenceTypeDeclaration) {
                  return this.solveDotExpressionType(correspondingDeclaration.asReferenceType(), node);
               }
            }
         } else if (node.getScope().toString().indexOf(46) > 0) {
            sr = this.typeSolver.tryToSolveType(node.getScope().toString());
            if (sr.isSolved()) {
               return this.solveDotExpressionType((ResolvedReferenceTypeDeclaration)sr.getCorrespondingDeclaration(), node);
            }
         }
      } else {
         Expression staticValue = node.getScope();
         SymbolReference<ResolvedTypeDeclaration> typeAccessedStatically = JavaParserFactory.getContext(node, this.typeSolver).solveType(staticValue.toString(), this.typeSolver);
         if (typeAccessedStatically.isSolved()) {
            return this.solveDotExpressionType(((ResolvedTypeDeclaration)typeAccessedStatically.getCorrespondingDeclaration()).asReferenceType(), node);
         }
      }

      Optional value = Optional.empty();

      try {
         value = (new SymbolSolver(this.typeSolver)).solveSymbolAsValue(node.getName().getId(), (Node)node);
      } catch (com.github.javaparser.resolution.UnsolvedSymbolException var6) {
         SymbolReference<ResolvedReferenceTypeDeclaration> sref = this.typeSolver.tryToSolveType(node.toString());
         if (sref.isSolved()) {
            return new ReferenceTypeImpl((ResolvedReferenceTypeDeclaration)sref.getCorrespondingDeclaration(), this.typeSolver);
         }
      }

      if (value.isPresent()) {
         return ((Value)value.get()).getType();
      } else {
         throw new com.github.javaparser.resolution.UnsolvedSymbolException(node.getName().getId());
      }
   }

   public ResolvedType visit(InstanceOfExpr node, Boolean solveLambdas) {
      return ResolvedPrimitiveType.BOOLEAN;
   }

   public ResolvedType visit(StringLiteralExpr node, Boolean solveLambdas) {
      return new ReferenceTypeImpl((new ReflectionTypeSolver()).solveType(String.class.getCanonicalName()), this.typeSolver);
   }

   public ResolvedType visit(IntegerLiteralExpr node, Boolean solveLambdas) {
      return ResolvedPrimitiveType.INT;
   }

   public ResolvedType visit(LongLiteralExpr node, Boolean solveLambdas) {
      return ResolvedPrimitiveType.LONG;
   }

   public ResolvedType visit(CharLiteralExpr node, Boolean solveLambdas) {
      return ResolvedPrimitiveType.CHAR;
   }

   public ResolvedType visit(DoubleLiteralExpr node, Boolean solveLambdas) {
      return node.getValue().toLowerCase().endsWith("f") ? ResolvedPrimitiveType.FLOAT : ResolvedPrimitiveType.DOUBLE;
   }

   public ResolvedType visit(BooleanLiteralExpr node, Boolean solveLambdas) {
      return ResolvedPrimitiveType.BOOLEAN;
   }

   public ResolvedType visit(NullLiteralExpr node, Boolean solveLambdas) {
      return NullType.INSTANCE;
   }

   public ResolvedType visit(MethodCallExpr node, Boolean solveLambdas) {
      Log.trace("getType on method call %s", node);
      MethodUsage ref = this.facade.solveMethodAsUsage(node);
      Log.trace("getType on method call %s resolved to %s", node, ref);
      Log.trace("getType on method call %s return type is %s", node, ref.returnType());
      return ref.returnType();
   }

   public ResolvedType visit(NameExpr node, Boolean solveLambdas) {
      Log.trace("getType on name expr %s", node);
      Optional<Value> value = (new SymbolSolver(this.typeSolver)).solveSymbolAsValue(node.getName().getId(), (Node)node);
      if (!value.isPresent()) {
         throw new com.github.javaparser.resolution.UnsolvedSymbolException("Solving " + node, node.getName().getId());
      } else {
         return ((Value)value.get()).getType();
      }
   }

   public ResolvedType visit(ObjectCreationExpr node, Boolean solveLambdas) {
      return this.facade.convertToUsage(node.getType(), (Node)node);
   }

   public ResolvedType visit(ThisExpr node, Boolean solveLambdas) {
      if (node.getClassExpr().isPresent()) {
         String className = ((Expression)node.getClassExpr().get()).toString();
         SymbolReference<ResolvedReferenceTypeDeclaration> clazz = this.typeSolver.tryToSolveType(className);
         if (clazz.isSolved()) {
            return new ReferenceTypeImpl((ResolvedReferenceTypeDeclaration)clazz.getCorrespondingDeclaration(), this.typeSolver);
         }

         Optional<CompilationUnit> cu = node.findAncestor(CompilationUnit.class);
         if (cu.isPresent()) {
            Optional<ClassOrInterfaceDeclaration> classByName = ((CompilationUnit)cu.get()).getClassByName(className);
            if (classByName.isPresent()) {
               return new ReferenceTypeImpl(this.facade.getTypeDeclaration((ClassOrInterfaceDeclaration)classByName.get()), this.typeSolver);
            }
         }
      }

      return new ReferenceTypeImpl(this.facade.getTypeDeclaration(this.facade.findContainingTypeDeclOrObjectCreationExpr(node)), this.typeSolver);
   }

   public ResolvedType visit(SuperExpr node, Boolean solveLambdas) {
      ResolvedTypeDeclaration typeOfNode = this.facade.getTypeDeclaration(this.facade.findContainingTypeDecl(node));
      if (typeOfNode instanceof ResolvedClassDeclaration) {
         return ((ResolvedClassDeclaration)typeOfNode).getSuperClass();
      } else {
         throw new UnsupportedOperationException(node.getClass().getCanonicalName());
      }
   }

   public ResolvedType visit(UnaryExpr node, Boolean solveLambdas) {
      switch(node.getOperator()) {
      case MINUS:
      case PLUS:
         return (ResolvedType)node.getExpression().accept(this, solveLambdas);
      case LOGICAL_COMPLEMENT:
         return ResolvedPrimitiveType.BOOLEAN;
      case POSTFIX_DECREMENT:
      case PREFIX_DECREMENT:
      case POSTFIX_INCREMENT:
      case PREFIX_INCREMENT:
         return (ResolvedType)node.getExpression().accept(this, solveLambdas);
      default:
         throw new UnsupportedOperationException(node.getOperator().name());
      }
   }

   public ResolvedType visit(VariableDeclarationExpr node, Boolean solveLambdas) {
      if (node.getVariables().size() != 1) {
         throw new UnsupportedOperationException();
      } else {
         return this.facade.convertToUsageVariableType((VariableDeclarator)node.getVariables().get(0));
      }
   }

   public ResolvedType visit(LambdaExpr node, Boolean solveLambdas) {
      if (Navigator.requireParentNode(node) instanceof MethodCallExpr) {
         MethodCallExpr callExpr = (MethodCallExpr)Navigator.requireParentNode(node);
         int pos = JavaParserSymbolDeclaration.getParamPos((Node)node);
         SymbolReference<ResolvedMethodDeclaration> refMethod = this.facade.solve(callExpr);
         if (!refMethod.isSolved()) {
            throw new com.github.javaparser.resolution.UnsolvedSymbolException(Navigator.requireParentNode(node).toString(), callExpr.getName().getId());
         } else {
            Log.trace("getType on lambda expr %s", ((ResolvedMethodDeclaration)refMethod.getCorrespondingDeclaration()).getName());
            if (solveLambdas) {
               ResolvedType result = ((ResolvedMethodDeclaration)refMethod.getCorrespondingDeclaration()).getParam(pos).getType();
               if (callExpr.getScope().isPresent()) {
                  Expression scope = (Expression)callExpr.getScope().get();
                  boolean staticCall = false;
                  if (scope instanceof NameExpr) {
                     NameExpr nameExpr = (NameExpr)scope;

                     try {
                        SymbolReference<ResolvedTypeDeclaration> type = JavaParserFactory.getContext(nameExpr, this.typeSolver).solveType(nameExpr.getName().getId(), this.typeSolver);
                        if (type.isSolved()) {
                           staticCall = true;
                        }
                     } catch (Exception var16) {
                     }
                  }

                  if (!staticCall) {
                     ResolvedType scopeType = this.facade.getType(scope);
                     if (scopeType.isReferenceType()) {
                        result = scopeType.asReferenceType().useThisTypeParametersOnTheGivenType(result);
                     }
                  }
               }

               Context ctx = JavaParserFactory.getContext(node, this.typeSolver);
               result = JavaParserFacade.solveGenericTypes(result, ctx, this.typeSolver);
               Optional<MethodUsage> functionalMethod = FunctionalInterfaceLogic.getFunctionalMethod(result);
               if (functionalMethod.isPresent()) {
                  InferenceContext lambdaCtx = new InferenceContext(MyObjectProvider.INSTANCE);
                  InferenceContext funcInterfaceCtx = new InferenceContext(MyObjectProvider.INSTANCE);
                  ResolvedType functionalInterfaceType = ReferenceTypeImpl.undeterminedParameters(((MethodUsage)functionalMethod.get()).getDeclaration().declaringType(), this.typeSolver);
                  lambdaCtx.addPair(result, functionalInterfaceType);
                  ResolvedType actualType;
                  if (node.getBody() instanceof ExpressionStmt) {
                     actualType = this.facade.getType(((ExpressionStmt)node.getBody()).getExpression());
                  } else {
                     if (!(node.getBody() instanceof BlockStmt)) {
                        throw new UnsupportedOperationException();
                     }

                     BlockStmt blockStmt = (BlockStmt)node.getBody();
                     List<ReturnStmt> returnStmts = blockStmt.findAll(ReturnStmt.class);
                     if (returnStmts.size() <= 0) {
                        return ResolvedVoidType.INSTANCE;
                     }

                     actualType = (ResolvedType)returnStmts.stream().map((returnStmt) -> {
                        return (ResolvedType)returnStmt.getExpression().map((e) -> {
                           return this.facade.getType(e);
                        }).orElse(ResolvedVoidType.INSTANCE);
                     }).filter((x) -> {
                        return x != null && !x.isVoid() && !x.isNull();
                     }).findFirst().orElse(ResolvedVoidType.INSTANCE);
                  }

                  ResolvedType formalType = ((MethodUsage)functionalMethod.get()).returnType();
                  funcInterfaceCtx.addPair(formalType, actualType);
                  ResolvedType functionalTypeWithReturn = funcInterfaceCtx.resolve(funcInterfaceCtx.addSingle(functionalInterfaceType));
                  if (!(formalType instanceof ResolvedVoidType)) {
                     lambdaCtx.addPair(result, functionalTypeWithReturn);
                     result = lambdaCtx.resolve(lambdaCtx.addSingle(result));
                  }
               }

               return result;
            } else {
               return ((ResolvedMethodDeclaration)refMethod.getCorrespondingDeclaration()).getParam(pos).getType();
            }
         }
      } else {
         throw new UnsupportedOperationException("The type of a lambda expr depends on the position and its return value");
      }
   }

   public ResolvedType visit(MethodReferenceExpr node, Boolean solveLambdas) {
      if (Navigator.requireParentNode(node) instanceof MethodCallExpr) {
         MethodCallExpr callExpr = (MethodCallExpr)Navigator.requireParentNode(node);
         int pos = JavaParserSymbolDeclaration.getParamPos((Node)node);
         SymbolReference<ResolvedMethodDeclaration> refMethod = this.facade.solve(callExpr, false);
         if (!refMethod.isSolved()) {
            throw new com.github.javaparser.resolution.UnsolvedSymbolException(Navigator.requireParentNode(node).toString(), callExpr.getName().getId());
         } else {
            Log.trace("getType on method reference expr %s", ((ResolvedMethodDeclaration)refMethod.getCorrespondingDeclaration()).getName());
            if (solveLambdas) {
               MethodUsage usage = this.facade.solveMethodAsUsage(callExpr);
               ResolvedType result = usage.getParamType(pos);
               Context ctx = JavaParserFactory.getContext(node, this.typeSolver);
               result = JavaParserFacade.solveGenericTypes(result, ctx, this.typeSolver);
               if (FunctionalInterfaceLogic.getFunctionalMethod(result).isPresent()) {
                  ResolvedType actualType = this.facade.toMethodUsage(node).returnType();
                  ResolvedType formalType = ((MethodUsage)FunctionalInterfaceLogic.getFunctionalMethod(result).get()).returnType();
                  InferenceContext inferenceContext = new InferenceContext(MyObjectProvider.INSTANCE);
                  inferenceContext.addPair(formalType, actualType);
                  result = inferenceContext.resolve(inferenceContext.addSingle(result));
               }

               return result;
            } else {
               return ((ResolvedMethodDeclaration)refMethod.getCorrespondingDeclaration()).getParam(pos).getType();
            }
         }
      } else {
         throw new UnsupportedOperationException("The type of a method reference expr depends on the position and its return value");
      }
   }

   public ResolvedType visit(FieldDeclaration node, Boolean solveLambdas) {
      if (node.getVariables().size() == 1) {
         return (ResolvedType)((VariableDeclarator)node.getVariables().get(0)).accept((GenericVisitor)this, solveLambdas);
      } else {
         throw new IllegalArgumentException("Cannot resolve the type of a field with multiple variable declarations. Pick one");
      }
   }
}
