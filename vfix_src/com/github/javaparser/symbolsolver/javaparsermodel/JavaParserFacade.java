package com.github.javaparser.symbolsolver.javaparsermodel;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.DataKey;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedAnnotationDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedClassDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedArrayType;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.resolution.types.ResolvedUnionType;
import com.github.javaparser.resolution.types.ResolvedVoidType;
import com.github.javaparser.resolution.types.ResolvedWildcard;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.FieldAccessContext;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserAnonymousClassDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserEnumDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserTypeVariableDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionClassDeclaration;
import com.github.javaparser.symbolsolver.resolution.ConstructorResolutionLogic;
import com.github.javaparser.symbolsolver.resolution.SymbolSolver;
import com.github.javaparser.utils.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public class JavaParserFacade {
   private static final DataKey<ResolvedType> TYPE_WITH_LAMBDAS_RESOLVED = new DataKey<ResolvedType>() {
   };
   private static final DataKey<ResolvedType> TYPE_WITHOUT_LAMBDAS_RESOLVED = new DataKey<ResolvedType>() {
   };
   private static final Map<TypeSolver, JavaParserFacade> instances = new WeakHashMap();
   private final TypeSolver typeSolver;
   private final TypeExtractor typeExtractor;
   private final SymbolSolver symbolSolver;

   private JavaParserFacade(TypeSolver typeSolver) {
      this.typeSolver = typeSolver.getRoot();
      this.symbolSolver = new SymbolSolver(typeSolver);
      this.typeExtractor = new TypeExtractor(typeSolver, this);
   }

   public TypeSolver getTypeSolver() {
      return this.typeSolver;
   }

   public SymbolSolver getSymbolSolver() {
      return this.symbolSolver;
   }

   public static JavaParserFacade get(TypeSolver typeSolver) {
      return (JavaParserFacade)instances.computeIfAbsent(typeSolver, JavaParserFacade::new);
   }

   public static void clearInstances() {
      instances.clear();
   }

   protected static ResolvedType solveGenericTypes(ResolvedType type, Context context, TypeSolver typeSolver) {
      if (type.isTypeVariable()) {
         return (ResolvedType)context.solveGenericType(type.describe(), typeSolver).orElse(type);
      } else if (type.isWildcard() && (type.asWildcard().isExtends() || type.asWildcard().isSuper())) {
         ResolvedWildcard wildcardUsage = type.asWildcard();
         ResolvedType boundResolved = solveGenericTypes(wildcardUsage.getBoundedType(), context, typeSolver);
         return wildcardUsage.isExtends() ? ResolvedWildcard.extendsBound(boundResolved) : ResolvedWildcard.superBound(boundResolved);
      } else {
         return type;
      }
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solve(NameExpr nameExpr) {
      return this.symbolSolver.solveSymbol(nameExpr.getName().getId(), (Node)nameExpr);
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solve(SimpleName nameExpr) {
      return this.symbolSolver.solveSymbol(nameExpr.getId(), (Node)nameExpr);
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solve(Expression expr) {
      return (SymbolReference)expr.toNameExpr().map(this::solve).orElseThrow(() -> {
         return new IllegalArgumentException(expr.getClass().getCanonicalName());
      });
   }

   public SymbolReference<ResolvedMethodDeclaration> solve(MethodCallExpr methodCallExpr) {
      return this.solve(methodCallExpr, true);
   }

   public SymbolReference<ResolvedConstructorDeclaration> solve(ObjectCreationExpr objectCreationExpr) {
      return this.solve(objectCreationExpr, true);
   }

   public SymbolReference<ResolvedConstructorDeclaration> solve(ExplicitConstructorInvocationStmt explicitConstructorInvocationStmt) {
      return this.solve(explicitConstructorInvocationStmt, true);
   }

   public SymbolReference<ResolvedConstructorDeclaration> solve(ExplicitConstructorInvocationStmt explicitConstructorInvocationStmt, boolean solveLambdas) {
      List<ResolvedType> argumentTypes = new LinkedList();
      List<LambdaArgumentTypePlaceholder> placeholders = new LinkedList();
      this.solveArguments(explicitConstructorInvocationStmt, explicitConstructorInvocationStmt.getArguments(), solveLambdas, argumentTypes, placeholders);
      Optional<ClassOrInterfaceDeclaration> optAncestor = explicitConstructorInvocationStmt.findAncestor(ClassOrInterfaceDeclaration.class);
      if (!optAncestor.isPresent()) {
         return SymbolReference.unsolved(ResolvedConstructorDeclaration.class);
      } else {
         ClassOrInterfaceDeclaration classNode = (ClassOrInterfaceDeclaration)optAncestor.get();
         ResolvedTypeDeclaration typeDecl = null;
         SymbolReference res;
         if (!explicitConstructorInvocationStmt.isThis()) {
            ResolvedType classDecl = get(this.typeSolver).convert(classNode.getExtendedTypes(0), (Node)classNode);
            if (classDecl.isReferenceType()) {
               typeDecl = classDecl.asReferenceType().getTypeDeclaration();
            }
         } else {
            res = JavaParserFactory.getContext(classNode, this.typeSolver).solveType(classNode.getNameAsString(), this.typeSolver);
            if (res.isSolved()) {
               typeDecl = (ResolvedTypeDeclaration)res.getCorrespondingDeclaration();
            }
         }

         if (typeDecl == null) {
            return SymbolReference.unsolved(ResolvedConstructorDeclaration.class);
         } else {
            res = ConstructorResolutionLogic.findMostApplicable(((ResolvedClassDeclaration)typeDecl).getConstructors(), argumentTypes, this.typeSolver);
            Iterator var9 = placeholders.iterator();

            while(var9.hasNext()) {
               LambdaArgumentTypePlaceholder placeholder = (LambdaArgumentTypePlaceholder)var9.next();
               placeholder.setMethod(res);
            }

            return res;
         }
      }
   }

   public SymbolReference<ResolvedTypeDeclaration> solve(ThisExpr node) {
      if (node.getClassExpr().isPresent()) {
         String className = ((Expression)node.getClassExpr().get()).toString();
         SymbolReference<ResolvedReferenceTypeDeclaration> clazz = this.typeSolver.tryToSolveType(className);
         if (clazz.isSolved()) {
            return SymbolReference.solved(clazz.getCorrespondingDeclaration());
         }

         Optional<CompilationUnit> cu = node.findAncestor(CompilationUnit.class);
         if (cu.isPresent()) {
            Optional<ClassOrInterfaceDeclaration> classByName = ((CompilationUnit)cu.get()).getClassByName(className);
            if (classByName.isPresent()) {
               return SymbolReference.solved(this.getTypeDeclaration((ClassOrInterfaceDeclaration)classByName.get()));
            }
         }
      }

      return SymbolReference.solved(this.getTypeDeclaration(this.findContainingTypeDeclOrObjectCreationExpr(node)));
   }

   public SymbolReference<ResolvedConstructorDeclaration> solve(ObjectCreationExpr objectCreationExpr, boolean solveLambdas) {
      List<ResolvedType> argumentTypes = new LinkedList();
      List<LambdaArgumentTypePlaceholder> placeholders = new LinkedList();
      this.solveArguments(objectCreationExpr, objectCreationExpr.getArguments(), solveLambdas, argumentTypes, placeholders);
      ResolvedType classDecl = get(this.typeSolver).convert(objectCreationExpr.getType(), (Node)objectCreationExpr);
      if (!classDecl.isReferenceType()) {
         return SymbolReference.unsolved(ResolvedConstructorDeclaration.class);
      } else {
         SymbolReference<ResolvedConstructorDeclaration> res = ConstructorResolutionLogic.findMostApplicable(classDecl.asReferenceType().getTypeDeclaration().getConstructors(), argumentTypes, this.typeSolver);
         Iterator var7 = placeholders.iterator();

         while(var7.hasNext()) {
            LambdaArgumentTypePlaceholder placeholder = (LambdaArgumentTypePlaceholder)var7.next();
            placeholder.setMethod(res);
         }

         return res;
      }
   }

   private void solveArguments(Node node, NodeList<Expression> args, boolean solveLambdas, List<ResolvedType> argumentTypes, List<LambdaArgumentTypePlaceholder> placeholders) {
      int i = 0;

      for(Iterator var7 = args.iterator(); var7.hasNext(); ++i) {
         Expression parameterValue = (Expression)var7.next();
         if (!(parameterValue instanceof LambdaExpr) && !(parameterValue instanceof MethodReferenceExpr)) {
            try {
               argumentTypes.add(get(this.typeSolver).getType(parameterValue, solveLambdas));
            } catch (com.github.javaparser.resolution.UnsolvedSymbolException var10) {
               throw var10;
            } catch (Exception var11) {
               throw new RuntimeException(String.format("Unable to calculate the type of a parameter of a method call. Method call: %s, Parameter: %s", node, parameterValue), var11);
            }
         } else {
            LambdaArgumentTypePlaceholder placeholder = new LambdaArgumentTypePlaceholder(i);
            argumentTypes.add(placeholder);
            placeholders.add(placeholder);
         }
      }

   }

   public SymbolReference<ResolvedMethodDeclaration> solve(MethodCallExpr methodCallExpr, boolean solveLambdas) {
      List<ResolvedType> argumentTypes = new LinkedList();
      List<LambdaArgumentTypePlaceholder> placeholders = new LinkedList();
      this.solveArguments(methodCallExpr, methodCallExpr.getArguments(), solveLambdas, argumentTypes, placeholders);
      SymbolReference<ResolvedMethodDeclaration> res = JavaParserFactory.getContext(methodCallExpr, this.typeSolver).solveMethod(methodCallExpr.getName().getId(), argumentTypes, false, this.typeSolver);
      Iterator var6 = placeholders.iterator();

      while(var6.hasNext()) {
         LambdaArgumentTypePlaceholder placeholder = (LambdaArgumentTypePlaceholder)var6.next();
         placeholder.setMethod(res);
      }

      return res;
   }

   public SymbolReference<ResolvedAnnotationDeclaration> solve(AnnotationExpr annotationExpr) {
      Context context = JavaParserFactory.getContext(annotationExpr, this.typeSolver);
      SymbolReference<ResolvedTypeDeclaration> typeDeclarationSymbolReference = context.solveType(annotationExpr.getNameAsString(), this.typeSolver);
      if (typeDeclarationSymbolReference.isSolved()) {
         ResolvedAnnotationDeclaration annotationDeclaration = (ResolvedAnnotationDeclaration)typeDeclarationSymbolReference.getCorrespondingDeclaration();
         return SymbolReference.solved(annotationDeclaration);
      } else {
         return SymbolReference.unsolved(ResolvedAnnotationDeclaration.class);
      }
   }

   public SymbolReference<ResolvedValueDeclaration> solve(FieldAccessExpr fieldAccessExpr) {
      return ((FieldAccessContext)JavaParserFactory.getContext(fieldAccessExpr, this.typeSolver)).solveField(fieldAccessExpr.getName().getId(), this.typeSolver);
   }

   public ResolvedType getType(Node node) {
      try {
         return this.getType(node, true);
      } catch (com.github.javaparser.resolution.UnsolvedSymbolException var6) {
         if (node instanceof NameExpr) {
            NameExpr nameExpr = (NameExpr)node;
            SymbolReference<ResolvedTypeDeclaration> typeDeclaration = JavaParserFactory.getContext(node, this.typeSolver).solveType(nameExpr.getNameAsString(), this.typeSolver);
            if (typeDeclaration.isSolved() && typeDeclaration.getCorrespondingDeclaration() instanceof ResolvedReferenceTypeDeclaration) {
               ResolvedReferenceTypeDeclaration resolvedReferenceTypeDeclaration = (ResolvedReferenceTypeDeclaration)typeDeclaration.getCorrespondingDeclaration();
               return ReferenceTypeImpl.undeterminedParameters(resolvedReferenceTypeDeclaration, this.typeSolver);
            }
         }

         throw var6;
      }
   }

   public ResolvedType getType(Node node, boolean solveLambdas) {
      if (!solveLambdas) {
         Optional<ResolvedType> res = this.find(TYPE_WITH_LAMBDAS_RESOLVED, node);
         if (res.isPresent()) {
            return (ResolvedType)res.get();
         } else {
            res = this.find(TYPE_WITHOUT_LAMBDAS_RESOLVED, node);
            if (!res.isPresent()) {
               ResolvedType resType = this.getTypeConcrete(node, solveLambdas);
               node.setData(TYPE_WITHOUT_LAMBDAS_RESOLVED, resType);
               Log.trace("getType on %s (no solveLambdas) -> %s", node, res);
               return resType;
            } else {
               return (ResolvedType)res.get();
            }
         }
      } else {
         if (!node.containsData(TYPE_WITH_LAMBDAS_RESOLVED)) {
            ResolvedType res = this.getTypeConcrete(node, solveLambdas);
            node.setData(TYPE_WITH_LAMBDAS_RESOLVED, res);
            boolean secondPassNecessary = false;
            if (node instanceof MethodCallExpr) {
               MethodCallExpr methodCallExpr = (MethodCallExpr)node;
               Iterator var6 = methodCallExpr.getArguments().iterator();

               while(var6.hasNext()) {
                  Node arg = (Node)var6.next();
                  if (!arg.containsData(TYPE_WITH_LAMBDAS_RESOLVED)) {
                     this.getType(arg, true);
                     secondPassNecessary = true;
                  }
               }
            }

            if (secondPassNecessary) {
               node.removeData(TYPE_WITH_LAMBDAS_RESOLVED);
               ResolvedType type = this.getType(node, true);
               node.setData(TYPE_WITH_LAMBDAS_RESOLVED, type);
            }

            Log.trace("getType on %s  -> %s", node, res);
         }

         return (ResolvedType)node.getData(TYPE_WITH_LAMBDAS_RESOLVED);
      }
   }

   private Optional<ResolvedType> find(DataKey<ResolvedType> dataKey, Node node) {
      return node.containsData(dataKey) ? Optional.of(node.getData(dataKey)) : Optional.empty();
   }

   protected MethodUsage toMethodUsage(MethodReferenceExpr methodReferenceExpr) {
      if (!(methodReferenceExpr.getScope() instanceof TypeExpr)) {
         throw new UnsupportedOperationException();
      } else {
         TypeExpr typeExpr = (TypeExpr)methodReferenceExpr.getScope();
         if (!(typeExpr.getType() instanceof ClassOrInterfaceType)) {
            throw new UnsupportedOperationException(typeExpr.getType().getClass().getCanonicalName());
         } else {
            ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType)typeExpr.getType();
            SymbolReference<ResolvedTypeDeclaration> typeDeclarationSymbolReference = JavaParserFactory.getContext(classOrInterfaceType, this.typeSolver).solveType(classOrInterfaceType.getName().getId(), this.typeSolver);
            if (!typeDeclarationSymbolReference.isSolved()) {
               throw new UnsupportedOperationException();
            } else {
               List<MethodUsage> methodUsages = (List)((ResolvedReferenceTypeDeclaration)typeDeclarationSymbolReference.getCorrespondingDeclaration()).getAllMethods().stream().filter((it) -> {
                  return it.getName().equals(methodReferenceExpr.getIdentifier());
               }).collect(Collectors.toList());
               switch(methodUsages.size()) {
               case 0:
                  throw new UnsupportedOperationException();
               case 1:
                  return (MethodUsage)methodUsages.get(0);
               default:
                  throw new UnsupportedOperationException();
               }
            }
         }
      }
   }

   protected ResolvedType getBinaryTypeConcrete(Node left, Node right, boolean solveLambdas, BinaryExpr.Operator operator) {
      ResolvedType leftType = this.getTypeConcrete(left, solveLambdas);
      ResolvedType rightType = this.getTypeConcrete(right, solveLambdas);
      boolean isLeftString;
      boolean isRightNumeric;
      if (operator == BinaryExpr.Operator.PLUS) {
         isLeftString = leftType.isReferenceType() && leftType.asReferenceType().getQualifiedName().equals(String.class.getCanonicalName());
         isRightNumeric = rightType.isReferenceType() && rightType.asReferenceType().getQualifiedName().equals(String.class.getCanonicalName());
         if (isLeftString || isRightNumeric) {
            return isLeftString ? leftType : rightType;
         }
      }

      isLeftString = leftType.isPrimitive() && leftType.asPrimitive().isNumeric();
      isRightNumeric = rightType.isPrimitive() && rightType.asPrimitive().isNumeric();
      if (isLeftString && isRightNumeric) {
         if (!leftType.asPrimitive().equals(ResolvedPrimitiveType.DOUBLE) && !rightType.asPrimitive().equals(ResolvedPrimitiveType.DOUBLE)) {
            if (!leftType.asPrimitive().equals(ResolvedPrimitiveType.FLOAT) && !rightType.asPrimitive().equals(ResolvedPrimitiveType.FLOAT)) {
               return !leftType.asPrimitive().equals(ResolvedPrimitiveType.LONG) && !rightType.asPrimitive().equals(ResolvedPrimitiveType.LONG) ? ResolvedPrimitiveType.INT : ResolvedPrimitiveType.LONG;
            } else {
               return ResolvedPrimitiveType.FLOAT;
            }
         } else {
            return ResolvedPrimitiveType.DOUBLE;
         }
      } else {
         return rightType.isAssignableBy(leftType) ? rightType : leftType;
      }
   }

   private ResolvedType getTypeConcrete(Node node, boolean solveLambdas) {
      if (node == null) {
         throw new IllegalArgumentException();
      } else {
         return (ResolvedType)node.accept(this.typeExtractor, solveLambdas);
      }
   }

   protected TypeDeclaration<?> findContainingTypeDecl(Node node) {
      if (node instanceof ClassOrInterfaceDeclaration) {
         return (ClassOrInterfaceDeclaration)node;
      } else {
         return (TypeDeclaration)(node instanceof EnumDeclaration ? (EnumDeclaration)node : this.findContainingTypeDecl(Navigator.requireParentNode(node)));
      }
   }

   protected Node findContainingTypeDeclOrObjectCreationExpr(Node node) {
      if (node instanceof ClassOrInterfaceDeclaration) {
         return node;
      } else if (node instanceof EnumDeclaration) {
         return node;
      } else {
         Node parent = Navigator.requireParentNode(node);
         return parent instanceof ObjectCreationExpr && !((ObjectCreationExpr)parent).getArguments().contains((Object)node) ? parent : this.findContainingTypeDeclOrObjectCreationExpr(parent);
      }
   }

   public ResolvedType convertToUsageVariableType(VariableDeclarator var) {
      return get(this.typeSolver).convertToUsage(var.getType(), (Node)var);
   }

   public ResolvedType convertToUsage(Type type, Node context) {
      if (type.isUnknownType()) {
         throw new IllegalArgumentException("Inferred lambda parameter type");
      } else {
         return this.convertToUsage(type, JavaParserFactory.getContext(context, this.typeSolver));
      }
   }

   public ResolvedType convertToUsage(Type type) {
      return this.convertToUsage(type, (Node)type);
   }

   private String qName(ClassOrInterfaceType classOrInterfaceType) {
      String name = classOrInterfaceType.getName().getId();
      return classOrInterfaceType.getScope().isPresent() ? this.qName((ClassOrInterfaceType)classOrInterfaceType.getScope().get()) + "." + name : name;
   }

   protected ResolvedType convertToUsage(Type type, Context context) {
      if (context == null) {
         throw new NullPointerException("Context should not be null");
      } else if (type instanceof ClassOrInterfaceType) {
         ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType)type;
         String name = this.qName(classOrInterfaceType);
         SymbolReference<ResolvedTypeDeclaration> ref = context.solveType(name, this.typeSolver);
         if (!ref.isSolved()) {
            throw new com.github.javaparser.resolution.UnsolvedSymbolException(name);
         } else {
            ResolvedTypeDeclaration typeDeclaration = (ResolvedTypeDeclaration)ref.getCorrespondingDeclaration();
            List<ResolvedType> typeParameters = Collections.emptyList();
            if (classOrInterfaceType.getTypeArguments().isPresent()) {
               typeParameters = (List)((NodeList)classOrInterfaceType.getTypeArguments().get()).stream().map((pt) -> {
                  return this.convertToUsage(pt, context);
               }).collect(Collectors.toList());
            }

            if (typeDeclaration.isTypeParameter()) {
               if (typeDeclaration instanceof ResolvedTypeParameterDeclaration) {
                  return new ResolvedTypeVariable((ResolvedTypeParameterDeclaration)typeDeclaration);
               } else {
                  JavaParserTypeVariableDeclaration javaParserTypeVariableDeclaration = (JavaParserTypeVariableDeclaration)typeDeclaration;
                  return new ResolvedTypeVariable(javaParserTypeVariableDeclaration.asTypeParameter());
               }
            } else {
               return new ReferenceTypeImpl((ResolvedReferenceTypeDeclaration)typeDeclaration, typeParameters, this.typeSolver);
            }
         }
      } else if (type instanceof PrimitiveType) {
         return ResolvedPrimitiveType.byName(((PrimitiveType)type).getType().name());
      } else if (type instanceof WildcardType) {
         WildcardType wildcardType = (WildcardType)type;
         if (wildcardType.getExtendedType().isPresent() && !wildcardType.getSuperType().isPresent()) {
            return ResolvedWildcard.extendsBound(this.convertToUsage((Type)wildcardType.getExtendedType().get(), context));
         } else if (!wildcardType.getExtendedType().isPresent() && wildcardType.getSuperType().isPresent()) {
            return ResolvedWildcard.superBound(this.convertToUsage((Type)wildcardType.getSuperType().get(), context));
         } else if (!wildcardType.getExtendedType().isPresent() && !wildcardType.getSuperType().isPresent()) {
            return ResolvedWildcard.UNBOUNDED;
         } else {
            throw new UnsupportedOperationException(wildcardType.toString());
         }
      } else if (type instanceof VoidType) {
         return ResolvedVoidType.INSTANCE;
      } else if (type instanceof ArrayType) {
         ArrayType jpArrayType = (ArrayType)type;
         return new ResolvedArrayType(this.convertToUsage(jpArrayType.getComponentType(), context));
      } else if (type instanceof UnionType) {
         UnionType unionType = (UnionType)type;
         return new ResolvedUnionType((List)unionType.getElements().stream().map((el) -> {
            return this.convertToUsage(el, (Context)context);
         }).collect(Collectors.toList()));
      } else if (type instanceof VarType) {
         Node parent = (Node)type.getParentNode().get();
         if (!(parent instanceof VariableDeclarator)) {
            throw new IllegalStateException("Trying to resolve a `var` which is not in a variable declaration.");
         } else {
            VariableDeclarator variableDeclarator = (VariableDeclarator)parent;
            return (ResolvedType)variableDeclarator.getInitializer().map(Expression::calculateResolvedType).orElseThrow(() -> {
               return new IllegalStateException("Cannot resolve `var` which has no initializer.");
            });
         }
      } else {
         throw new UnsupportedOperationException(type.getClass().getCanonicalName());
      }
   }

   public ResolvedType convert(Type type, Node node) {
      return this.convert(type, JavaParserFactory.getContext(node, this.typeSolver));
   }

   public ResolvedType convert(Type type, Context context) {
      return this.convertToUsage(type, context);
   }

   public MethodUsage solveMethodAsUsage(MethodCallExpr call) {
      List<ResolvedType> params = new ArrayList();
      if (call.getArguments() != null) {
         Iterator var3 = call.getArguments().iterator();

         while(var3.hasNext()) {
            Expression param = (Expression)var3.next();

            try {
               params.add(this.getType(param, false));
            } catch (Exception var6) {
               throw new RuntimeException(String.format("Error calculating the type of parameter %s of method call %s", param, call), var6);
            }
         }
      }

      Context context = JavaParserFactory.getContext(call, this.typeSolver);
      Optional<MethodUsage> methodUsage = context.solveMethodAsUsage(call.getName().getId(), params, this.typeSolver);
      if (!methodUsage.isPresent()) {
         throw new RuntimeException("Method '" + call.getName() + "' cannot be resolved in context " + call + " (line: " + (String)call.getRange().map((r) -> {
            return "" + r.begin.line;
         }).orElse("??") + ") " + context + ". Parameter types: " + params);
      } else {
         return (MethodUsage)methodUsage.get();
      }
   }

   public ResolvedReferenceTypeDeclaration getTypeDeclaration(Node node) {
      if (node instanceof TypeDeclaration) {
         return this.getTypeDeclaration((TypeDeclaration)node);
      } else if (node instanceof ObjectCreationExpr) {
         return new JavaParserAnonymousClassDeclaration((ObjectCreationExpr)node, this.typeSolver);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public ResolvedReferenceTypeDeclaration getTypeDeclaration(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
      return JavaParserFactory.toTypeDeclaration(classOrInterfaceDeclaration, this.typeSolver);
   }

   public ResolvedType getTypeOfThisIn(Node node) {
      if (node instanceof ClassOrInterfaceDeclaration) {
         return new ReferenceTypeImpl(this.getTypeDeclaration((ClassOrInterfaceDeclaration)node), this.typeSolver);
      } else if (node instanceof EnumDeclaration) {
         JavaParserEnumDeclaration enumDeclaration = new JavaParserEnumDeclaration((EnumDeclaration)node, this.typeSolver);
         return new ReferenceTypeImpl(enumDeclaration, this.typeSolver);
      } else if (node instanceof ObjectCreationExpr && ((ObjectCreationExpr)node).getAnonymousClassBody().isPresent()) {
         JavaParserAnonymousClassDeclaration anonymousDeclaration = new JavaParserAnonymousClassDeclaration((ObjectCreationExpr)node, this.typeSolver);
         return new ReferenceTypeImpl(anonymousDeclaration, this.typeSolver);
      } else {
         return this.getTypeOfThisIn(Navigator.requireParentNode(node));
      }
   }

   public ResolvedReferenceTypeDeclaration getTypeDeclaration(TypeDeclaration<?> typeDeclaration) {
      return JavaParserFactory.toTypeDeclaration(typeDeclaration, this.typeSolver);
   }

   public ResolvedType classToResolvedType(Class<?> clazz) {
      return (ResolvedType)(clazz.isPrimitive() ? ResolvedPrimitiveType.byName(clazz.getName()) : new ReferenceTypeImpl(new ReflectionClassDeclaration(clazz, this.typeSolver), this.typeSolver));
   }
}
