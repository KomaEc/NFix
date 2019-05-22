package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserAnnotationDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserClassDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserEnumDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserInterfaceDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.MethodResolutionLogic;
import com.github.javaparser.symbolsolver.resolution.SymbolSolver;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CompilationUnitContext extends AbstractJavaParserContext<CompilationUnit> {
   private static boolean isQualifiedName(String name) {
      return name.contains(".");
   }

   public CompilationUnitContext(CompilationUnit wrappedNode, TypeSolver typeSolver) {
      super(wrappedNode, typeSolver);
   }

   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      String typeName;
      for(String itName = name; itName.contains("."); itName = typeName) {
         typeName = this.getType(itName);
         String memberName = this.getMember(itName);
         SymbolReference<ResolvedTypeDeclaration> type = this.solveType(typeName, typeSolver);
         if (type.isSolved()) {
            return (new SymbolSolver(typeSolver)).solveSymbolInType((ResolvedTypeDeclaration)type.getCorrespondingDeclaration(), memberName);
         }
      }

      if (((CompilationUnit)this.wrappedNode).getImports() != null) {
         Iterator var10 = ((CompilationUnit)this.wrappedNode).getImports().iterator();

         while(var10.hasNext()) {
            ImportDeclaration importDecl = (ImportDeclaration)var10.next();
            if (importDecl.isStatic()) {
               String qName;
               if (importDecl.isAsterisk()) {
                  qName = importDecl.getNameAsString();
                  ResolvedTypeDeclaration importedType = typeSolver.solveType(qName);
                  SymbolReference<? extends ResolvedValueDeclaration> ref = (new SymbolSolver(typeSolver)).solveSymbolInType(importedType, name);
                  if (ref.isSolved()) {
                     return ref;
                  }
               } else {
                  qName = importDecl.getNameAsString();
                  String memberName = this.getMember(qName);
                  String typeName = this.getType(qName);
                  if (memberName.equals(name)) {
                     ResolvedTypeDeclaration importedType = typeSolver.solveType(typeName);
                     return (new SymbolSolver(typeSolver)).solveSymbolInType(importedType, memberName);
                  }
               }
            }
         }
      }

      return SymbolReference.unsolved(ResolvedValueDeclaration.class);
   }

   public SymbolReference<ResolvedTypeDeclaration> solveType(String name, TypeSolver typeSolver) {
      SymbolReference ref;
      SymbolReference outerMostRef;
      if (((CompilationUnit)this.wrappedNode).getTypes() != null) {
         Iterator var3 = ((CompilationUnit)this.wrappedNode).getTypes().iterator();

         while(var3.hasNext()) {
            TypeDeclaration<?> type = (TypeDeclaration)var3.next();
            if (type.getName().getId().equals(name)) {
               if (type instanceof ClassOrInterfaceDeclaration) {
                  return SymbolReference.solved(JavaParserFacade.get(typeSolver).getTypeDeclaration((ClassOrInterfaceDeclaration)type));
               }

               if (type instanceof AnnotationDeclaration) {
                  return SymbolReference.solved(new JavaParserAnnotationDeclaration((AnnotationDeclaration)type, typeSolver));
               }

               if (type instanceof EnumDeclaration) {
                  return SymbolReference.solved(new JavaParserEnumDeclaration((EnumDeclaration)type, typeSolver));
               }

               throw new UnsupportedOperationException(type.getClass().getCanonicalName());
            }
         }

         if (name.indexOf(46) > -1) {
            ref = null;
            outerMostRef = this.solveType(name.substring(0, name.indexOf(".")), typeSolver);
            if (outerMostRef != null && outerMostRef.isSolved() && outerMostRef.getCorrespondingDeclaration() instanceof JavaParserClassDeclaration) {
               ref = ((JavaParserClassDeclaration)outerMostRef.getCorrespondingDeclaration()).solveType(name.substring(name.indexOf(".") + 1), typeSolver);
            } else if (outerMostRef != null && outerMostRef.isSolved() && outerMostRef.getCorrespondingDeclaration() instanceof JavaParserInterfaceDeclaration) {
               ref = ((JavaParserInterfaceDeclaration)outerMostRef.getCorrespondingDeclaration()).solveType(name.substring(name.indexOf(".") + 1), typeSolver);
            }

            if (ref != null && ref.isSolved()) {
               return ref;
            }
         }
      }

      if (((CompilationUnit)this.wrappedNode).getPackageDeclaration().isPresent()) {
         String qName = ((PackageDeclaration)((CompilationUnit)this.wrappedNode).getPackageDeclaration().get()).getName().toString() + "." + name;
         outerMostRef = typeSolver.tryToSolveType(qName);
         if (outerMostRef != null && outerMostRef.isSolved()) {
            return SymbolReference.adapt(outerMostRef, ResolvedTypeDeclaration.class);
         }
      } else {
         outerMostRef = typeSolver.tryToSolveType(name);
         if (outerMostRef != null && outerMostRef.isSolved()) {
            return SymbolReference.adapt(outerMostRef, ResolvedTypeDeclaration.class);
         }
      }

      if (((CompilationUnit)this.wrappedNode).getImports() != null) {
         int dotPos = name.indexOf(46);
         String prefix = null;
         if (dotPos > -1) {
            prefix = name.substring(0, dotPos);
         }

         Iterator var5 = ((CompilationUnit)this.wrappedNode).getImports().iterator();

         label124:
         while(true) {
            ImportDeclaration importDecl;
            String qName;
            do {
               if (!var5.hasNext()) {
                  var5 = ((CompilationUnit)this.wrappedNode).getImports().iterator();

                  while(var5.hasNext()) {
                     importDecl = (ImportDeclaration)var5.next();
                     if (importDecl.isAsterisk()) {
                        qName = importDecl.getNameAsString() + "." + name;
                        SymbolReference<ResolvedReferenceTypeDeclaration> ref = typeSolver.tryToSolveType(qName);
                        if (ref != null && ref.isSolved()) {
                           return SymbolReference.adapt(ref, ResolvedTypeDeclaration.class);
                        }
                     }
                  }
                  break label124;
               }

               importDecl = (ImportDeclaration)var5.next();
            } while(importDecl.isAsterisk());

            qName = importDecl.getNameAsString();
            boolean defaultPackage = !importDecl.getName().getQualifier().isPresent();
            boolean found = !defaultPackage && importDecl.getName().getIdentifier().equals(name);
            if (!found && prefix != null) {
               found = qName.endsWith("." + prefix);
               if (found) {
                  qName = qName + name.substring(dotPos);
               }
            }

            if (found) {
               SymbolReference<ResolvedReferenceTypeDeclaration> ref = typeSolver.tryToSolveType(qName);
               if (ref != null && ref.isSolved()) {
                  return SymbolReference.adapt(ref, ResolvedTypeDeclaration.class);
               }
            }
         }
      }

      ref = typeSolver.tryToSolveType("java.lang." + name);
      if (ref != null && ref.isSolved()) {
         return SymbolReference.adapt(ref, ResolvedTypeDeclaration.class);
      } else {
         return isQualifiedName(name) ? SymbolReference.adapt(typeSolver.tryToSolveType(name), ResolvedTypeDeclaration.class) : SymbolReference.unsolved(ResolvedReferenceTypeDeclaration.class);
      }
   }

   private String qName(ClassOrInterfaceType type) {
      return type.getScope().isPresent() ? this.qName((ClassOrInterfaceType)type.getScope().get()) + "." + type.getName().getId() : type.getName().getId();
   }

   private String qName(Name name) {
      return name.getQualifier().isPresent() ? this.qName((Name)name.getQualifier().get()) + "." + name.getId() : name.getId();
   }

   private String toSimpleName(String qName) {
      String[] parts = qName.split("\\.");
      return parts[parts.length - 1];
   }

   private String packageName(String qName) {
      int lastDot = qName.lastIndexOf(46);
      if (lastDot == -1) {
         throw new UnsupportedOperationException();
      } else {
         return qName.substring(0, lastDot);
      }
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly, TypeSolver typeSolver) {
      Iterator var5 = ((CompilationUnit)this.wrappedNode).getImports().iterator();

      SymbolReference method;
      do {
         while(true) {
            ImportDeclaration importDecl;
            do {
               if (!var5.hasNext()) {
                  return SymbolReference.unsolved(ResolvedMethodDeclaration.class);
               }

               importDecl = (ImportDeclaration)var5.next();
            } while(!importDecl.isStatic());

            String qName;
            if (importDecl.isAsterisk()) {
               qName = importDecl.getNameAsString();
               if (((CompilationUnit)this.wrappedNode).getPackageDeclaration().isPresent() && ((PackageDeclaration)((CompilationUnit)this.wrappedNode).getPackageDeclaration().get()).getName().getIdentifier().equals(this.packageName(qName)) && ((CompilationUnit)this.wrappedNode).getTypes().stream().anyMatch((it) -> {
                  return it.getName().getIdentifier().equals(this.toSimpleName(qName));
               })) {
                  return SymbolReference.unsolved(ResolvedMethodDeclaration.class);
               }

               ResolvedTypeDeclaration ref = typeSolver.solveType(qName);
               method = MethodResolutionLogic.solveMethodInType(ref, name, argumentsTypes, true, typeSolver);
               break;
            }

            qName = importDecl.getNameAsString();
            if (qName.equals(name) || qName.endsWith("." + name)) {
               String typeName = this.getType(qName);
               ResolvedTypeDeclaration ref = typeSolver.solveType(typeName);
               SymbolReference<ResolvedMethodDeclaration> method = MethodResolutionLogic.solveMethodInType(ref, name, argumentsTypes, true, typeSolver);
               if (method.isSolved()) {
                  return method;
               }
            }
         }
      } while(!method.isSolved());

      return method;
   }

   public List<ResolvedFieldDeclaration> fieldsExposedToChild(Node child) {
      List<ResolvedFieldDeclaration> res = new LinkedList();
      Iterator var3 = ((CompilationUnit)this.wrappedNode).getImports().iterator();

      while(var3.hasNext()) {
         ImportDeclaration importDeclaration = (ImportDeclaration)var3.next();
         if (importDeclaration.isStatic()) {
            Name typeNameAsNode = importDeclaration.isAsterisk() ? importDeclaration.getName() : (Name)importDeclaration.getName().getQualifier().get();
            String typeName = typeNameAsNode.asString();
            ResolvedReferenceTypeDeclaration typeDeclaration = this.typeSolver.solveType(typeName);
            res.addAll((Collection)typeDeclaration.getAllFields().stream().filter((f) -> {
               return f.isStatic();
            }).filter((f) -> {
               return importDeclaration.isAsterisk() || importDeclaration.getName().getIdentifier().equals(f.getName());
            }).collect(Collectors.toList()));
         }
      }

      return res;
   }

   private String getType(String qName) {
      int index = qName.lastIndexOf(46);
      if (index == -1) {
         throw new UnsupportedOperationException();
      } else {
         String typeName = qName.substring(0, index);
         return typeName;
      }
   }

   private String getMember(String qName) {
      int index = qName.lastIndexOf(46);
      if (index == -1) {
         throw new UnsupportedOperationException();
      } else {
         String memberName = qName.substring(index + 1);
         return memberName;
      }
   }
}
