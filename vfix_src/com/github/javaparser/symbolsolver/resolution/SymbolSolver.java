package com.github.javaparser.symbolsolver.resolution;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserClassDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserEnumDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserInterfaceDeclaration;
import com.github.javaparser.symbolsolver.javassistmodel.JavassistClassDeclaration;
import com.github.javaparser.symbolsolver.javassistmodel.JavassistEnumDeclaration;
import com.github.javaparser.symbolsolver.javassistmodel.JavassistInterfaceDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.resolution.Value;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionClassDeclaration;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionInterfaceDeclaration;
import java.util.List;
import java.util.Optional;

public class SymbolSolver {
   private TypeSolver typeSolver;

   public SymbolSolver(TypeSolver typeSolver) {
      if (typeSolver == null) {
         throw new IllegalArgumentException();
      } else {
         this.typeSolver = typeSolver;
      }
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, Context context) {
      return context.solveSymbol(name, this.typeSolver);
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, Node node) {
      return this.solveSymbol(name, JavaParserFactory.getContext(node, this.typeSolver));
   }

   public Optional<Value> solveSymbolAsValue(String name, Context context) {
      return context.solveSymbolAsValue(name, this.typeSolver);
   }

   public Optional<Value> solveSymbolAsValue(String name, Node node) {
      Context context = JavaParserFactory.getContext(node, this.typeSolver);
      return this.solveSymbolAsValue(name, context);
   }

   public SymbolReference<? extends ResolvedTypeDeclaration> solveType(String name, Context context) {
      return context.solveType(name, this.typeSolver);
   }

   public SymbolReference<? extends ResolvedTypeDeclaration> solveType(String name, Node node) {
      return this.solveType(name, JavaParserFactory.getContext(node, this.typeSolver));
   }

   public MethodUsage solveMethod(String methodName, List<ResolvedType> argumentsTypes, Context context) {
      SymbolReference<ResolvedMethodDeclaration> decl = context.solveMethod(methodName, argumentsTypes, false, this.typeSolver);
      if (!decl.isSolved()) {
         throw new UnsolvedSymbolException(context.toString(), methodName);
      } else {
         return new MethodUsage((ResolvedMethodDeclaration)decl.getCorrespondingDeclaration());
      }
   }

   public MethodUsage solveMethod(String methodName, List<ResolvedType> argumentsTypes, Node node) {
      return this.solveMethod(methodName, argumentsTypes, JavaParserFactory.getContext(node, this.typeSolver));
   }

   public ResolvedTypeDeclaration solveType(Type type) {
      if (type instanceof ClassOrInterfaceType) {
         String name = ((ClassOrInterfaceType)type).getName().getId();
         SymbolReference<ResolvedTypeDeclaration> ref = JavaParserFactory.getContext(type, this.typeSolver).solveType(name, this.typeSolver);
         if (!ref.isSolved()) {
            throw new UnsolvedSymbolException(JavaParserFactory.getContext(type, this.typeSolver).toString(), name);
         } else {
            return (ResolvedTypeDeclaration)ref.getCorrespondingDeclaration();
         }
      } else {
         throw new UnsupportedOperationException(type.getClass().getCanonicalName());
      }
   }

   public ResolvedType solveTypeUsage(String name, Context context) {
      Optional<ResolvedType> genericType = context.solveGenericType(name, this.typeSolver);
      if (genericType.isPresent()) {
         return (ResolvedType)genericType.get();
      } else {
         ResolvedReferenceTypeDeclaration typeDeclaration = this.typeSolver.solveType(name);
         return new ReferenceTypeImpl(typeDeclaration, this.typeSolver);
      }
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbolInType(ResolvedTypeDeclaration typeDeclaration, String name) {
      Context ctx;
      if (typeDeclaration instanceof JavaParserClassDeclaration) {
         ctx = ((JavaParserClassDeclaration)typeDeclaration).getContext();
         return ctx.solveSymbol(name, this.typeSolver);
      } else if (typeDeclaration instanceof JavaParserInterfaceDeclaration) {
         ctx = ((JavaParserInterfaceDeclaration)typeDeclaration).getContext();
         return ctx.solveSymbol(name, this.typeSolver);
      } else if (typeDeclaration instanceof JavaParserEnumDeclaration) {
         ctx = ((JavaParserEnumDeclaration)typeDeclaration).getContext();
         return ctx.solveSymbol(name, this.typeSolver);
      } else if (typeDeclaration instanceof ReflectionClassDeclaration) {
         return ((ReflectionClassDeclaration)typeDeclaration).solveSymbol(name, this.typeSolver);
      } else if (typeDeclaration instanceof ReflectionInterfaceDeclaration) {
         return ((ReflectionInterfaceDeclaration)typeDeclaration).solveSymbol(name, this.typeSolver);
      } else if (typeDeclaration instanceof JavassistClassDeclaration) {
         return ((JavassistClassDeclaration)typeDeclaration).solveSymbol(name, this.typeSolver);
      } else if (typeDeclaration instanceof JavassistEnumDeclaration) {
         return ((JavassistEnumDeclaration)typeDeclaration).solveSymbol(name, this.typeSolver);
      } else {
         return typeDeclaration instanceof JavassistInterfaceDeclaration ? ((JavassistInterfaceDeclaration)typeDeclaration).solveSymbol(name, this.typeSolver) : SymbolReference.unsolved(ResolvedValueDeclaration.class);
      }
   }

   /** @deprecated */
   @Deprecated
   public SymbolReference<ResolvedTypeDeclaration> solveTypeInType(ResolvedTypeDeclaration typeDeclaration, String name) {
      if (typeDeclaration instanceof JavaParserClassDeclaration) {
         return ((JavaParserClassDeclaration)typeDeclaration).solveType(name, this.typeSolver);
      } else {
         return typeDeclaration instanceof JavaParserInterfaceDeclaration ? ((JavaParserInterfaceDeclaration)typeDeclaration).solveType(name, this.typeSolver) : SymbolReference.unsolved(ResolvedReferenceTypeDeclaration.class);
      }
   }
}
