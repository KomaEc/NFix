package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedClassDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.logic.AbstractClassDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.LazyType;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.resolution.SymbolSolver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaParserClassDeclaration extends AbstractClassDeclaration {
   private TypeSolver typeSolver;
   private ClassOrInterfaceDeclaration wrappedNode;
   private JavaParserTypeAdapter<ClassOrInterfaceDeclaration> javaParserTypeAdapter;

   public JavaParserClassDeclaration(ClassOrInterfaceDeclaration wrappedNode, TypeSolver typeSolver) {
      if (wrappedNode.isInterface()) {
         throw new IllegalArgumentException("Interface given");
      } else {
         this.wrappedNode = wrappedNode;
         this.typeSolver = typeSolver;
         this.javaParserTypeAdapter = new JavaParserTypeAdapter(wrappedNode, typeSolver);
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         JavaParserClassDeclaration that = (JavaParserClassDeclaration)o;
         return this.wrappedNode.equals(that.wrappedNode);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.wrappedNode.hashCode();
   }

   public String toString() {
      return "JavaParserClassDeclaration{wrappedNode=" + this.wrappedNode + '}';
   }

   public List<ResolvedFieldDeclaration> getAllFields() {
      List<ResolvedFieldDeclaration> fields = this.javaParserTypeAdapter.getFieldsForDeclaredVariables();
      this.getAncestors(true).forEach((ancestor) -> {
         ancestor.getTypeDeclaration().getAllFields().forEach((f) -> {
            fields.add(new ResolvedFieldDeclaration() {
               public AccessSpecifier accessSpecifier() {
                  return f.accessSpecifier();
               }

               public String getName() {
                  return f.getName();
               }

               public ResolvedType getType() {
                  return ancestor.useThisTypeParametersOnTheGivenType(f.getType());
               }

               public boolean isStatic() {
                  return f.isStatic();
               }

               public ResolvedTypeDeclaration declaringType() {
                  return f.declaringType();
               }
            });
         });
      });
      return fields;
   }

   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> parameterTypes) {
      Context ctx = this.getContext();
      return ctx.solveMethod(name, parameterTypes, false, this.typeSolver);
   }

   /** @deprecated */
   @Deprecated
   public Context getContext() {
      return JavaParserFactory.getContext(this.wrappedNode, this.typeSolver);
   }

   public ResolvedType getUsage(Node node) {
      throw new UnsupportedOperationException();
   }

   public String getName() {
      return this.wrappedNode.getName().getId();
   }

   public ResolvedReferenceType getSuperClass() {
      return this.wrappedNode.getExtendedTypes().isEmpty() ? this.object() : this.toReferenceType((ClassOrInterfaceType)this.wrappedNode.getExtendedTypes().get(0));
   }

   public List<ResolvedReferenceType> getInterfaces() {
      List<ResolvedReferenceType> interfaces = new ArrayList();
      if (this.wrappedNode.getImplementedTypes() != null) {
         Iterator var2 = this.wrappedNode.getImplementedTypes().iterator();

         while(var2.hasNext()) {
            ClassOrInterfaceType t = (ClassOrInterfaceType)var2.next();
            interfaces.add(this.toReferenceType(t));
         }
      }

      return interfaces;
   }

   public List<ResolvedConstructorDeclaration> getConstructors() {
      return AstResolutionUtils.getConstructors(this.wrappedNode, this.typeSolver, this);
   }

   public boolean hasDirectlyAnnotation(String canonicalName) {
      return AstResolutionUtils.hasDirectlyAnnotation(this.wrappedNode, this.typeSolver, canonicalName);
   }

   public boolean isInterface() {
      return this.wrappedNode.isInterface();
   }

   public String getPackageName() {
      return this.javaParserTypeAdapter.getPackageName();
   }

   public String getClassName() {
      return this.javaParserTypeAdapter.getClassName();
   }

   public String getQualifiedName() {
      return this.javaParserTypeAdapter.getQualifiedName();
   }

   public boolean isAssignableBy(ResolvedReferenceTypeDeclaration other) {
      return this.javaParserTypeAdapter.isAssignableBy(other);
   }

   public boolean isAssignableBy(ResolvedType type) {
      return this.javaParserTypeAdapter.isAssignableBy(type);
   }

   public boolean canBeAssignedTo(ResolvedReferenceTypeDeclaration other) {
      if (this.getQualifiedName().equals(other.getQualifiedName())) {
         return true;
      } else {
         ResolvedClassDeclaration superclass = (ResolvedClassDeclaration)this.getSuperClass().getTypeDeclaration();
         if (superclass != null) {
            if (Object.class.getCanonicalName().equals(superclass.getQualifiedName())) {
               return true;
            }

            if (superclass.canBeAssignedTo(other)) {
               return true;
            }
         }

         if (this.wrappedNode.getImplementedTypes() != null) {
            Iterator var3 = this.wrappedNode.getImplementedTypes().iterator();

            while(var3.hasNext()) {
               ClassOrInterfaceType type = (ClassOrInterfaceType)var3.next();
               ResolvedReferenceTypeDeclaration ancestor = (ResolvedReferenceTypeDeclaration)(new SymbolSolver(this.typeSolver)).solveType(type);
               if (ancestor.canBeAssignedTo(other)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean isTypeParameter() {
      return false;
   }

   /** @deprecated */
   @Deprecated
   public SymbolReference<ResolvedTypeDeclaration> solveType(String name, TypeSolver typeSolver) {
      if (this.wrappedNode.getName().getId().equals(name)) {
         return SymbolReference.solved(this);
      } else {
         SymbolReference<ResolvedTypeDeclaration> ref = this.javaParserTypeAdapter.solveType(name, typeSolver);
         if (ref.isSolved()) {
            return ref;
         } else {
            String prefix = this.wrappedNode.getName() + ".";
            return name.startsWith(prefix) && name.length() > prefix.length() ? (new JavaParserClassDeclaration(this.wrappedNode, typeSolver)).solveType(name.substring(prefix.length()), typeSolver) : this.getContext().getParent().solveType(name, typeSolver);
         }
      }
   }

   public List<ResolvedReferenceType> getAncestors(boolean acceptIncompleteList) {
      List<ResolvedReferenceType> ancestors = new ArrayList();
      if (!Object.class.getCanonicalName().equals(this.getQualifiedName())) {
         ResolvedReferenceType superclass;
         try {
            superclass = this.getSuperClass();
         } catch (UnsolvedSymbolException var9) {
            if (!acceptIncompleteList) {
               throw var9;
            }

            superclass = null;
         }

         if (superclass != null) {
            ancestors.add(superclass);
         }

         if (this.wrappedNode.getImplementedTypes() != null) {
            Iterator var4 = this.wrappedNode.getImplementedTypes().iterator();

            while(var4.hasNext()) {
               ClassOrInterfaceType implemented = (ClassOrInterfaceType)var4.next();

               ResolvedReferenceType ancestor;
               try {
                  ancestor = this.toReferenceType(implemented);
               } catch (UnsolvedSymbolException var8) {
                  if (!acceptIncompleteList) {
                     throw var8;
                  }

                  ancestor = null;
               }

               if (ancestor != null) {
                  ancestors.add(ancestor);
               }
            }
         }
      }

      return ancestors;
   }

   public Set<ResolvedMethodDeclaration> getDeclaredMethods() {
      Set<ResolvedMethodDeclaration> methods = new HashSet();
      Iterator var2 = this.wrappedNode.getMembers().iterator();

      while(var2.hasNext()) {
         BodyDeclaration<?> member = (BodyDeclaration)var2.next();
         if (member instanceof MethodDeclaration) {
            methods.add(new JavaParserMethodDeclaration((MethodDeclaration)member, this.typeSolver));
         }
      }

      return methods;
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return (List)this.wrappedNode.getTypeParameters().stream().map((tp) -> {
         return new JavaParserTypeParameter(tp, this.typeSolver);
      }).collect(Collectors.toList());
   }

   public ClassOrInterfaceDeclaration getWrappedNode() {
      return this.wrappedNode;
   }

   public AccessSpecifier accessSpecifier() {
      return AstResolutionUtils.toAccessLevel(this.wrappedNode.getModifiers());
   }

   public Optional<Node> toAst() {
      return Optional.of(this.wrappedNode);
   }

   protected ResolvedReferenceType object() {
      return new ReferenceTypeImpl(this.typeSolver.solveType(Object.class.getCanonicalName()), this.typeSolver);
   }

   public Set<ResolvedReferenceTypeDeclaration> internalTypes() {
      Set<ResolvedReferenceTypeDeclaration> res = new HashSet();
      Iterator var2 = this.wrappedNode.getMembers().iterator();

      while(var2.hasNext()) {
         BodyDeclaration<?> member = (BodyDeclaration)var2.next();
         if (member instanceof TypeDeclaration) {
            res.add(JavaParserFacade.get(this.typeSolver).getTypeDeclaration((TypeDeclaration)member));
         }
      }

      return res;
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      return this.javaParserTypeAdapter.containerType();
   }

   private ResolvedReferenceType toReferenceType(ClassOrInterfaceType classOrInterfaceType) {
      String className = classOrInterfaceType.getName().getId();
      if (classOrInterfaceType.getScope().isPresent()) {
         className = ((ClassOrInterfaceType)classOrInterfaceType.getScope().get()).toString() + "." + className;
      }

      SymbolReference<ResolvedTypeDeclaration> ref = this.solveType(className, this.typeSolver);
      if (!ref.isSolved()) {
         Optional<ClassOrInterfaceType> localScope = classOrInterfaceType.getScope();
         if (localScope.isPresent()) {
            String localName = ((ClassOrInterfaceType)localScope.get()).getName().getId() + "." + classOrInterfaceType.getName().getId();
            ref = this.solveType(localName, this.typeSolver);
         }
      }

      if (!ref.isSolved()) {
         throw new UnsolvedSymbolException(classOrInterfaceType.getName().getId());
      } else if (!classOrInterfaceType.getTypeArguments().isPresent()) {
         return new ReferenceTypeImpl(((ResolvedTypeDeclaration)ref.getCorrespondingDeclaration()).asReferenceType(), this.typeSolver);
      } else {
         List<ResolvedType> superClassTypeParameters = (List)((NodeList)classOrInterfaceType.getTypeArguments().get()).stream().map((ta) -> {
            return new LazyType((v) -> {
               return JavaParserFacade.get(this.typeSolver).convert(ta, (Node)ta);
            });
         }).collect(Collectors.toList());
         return new ReferenceTypeImpl(((ResolvedTypeDeclaration)ref.getCorrespondingDeclaration()).asReferenceType(), superClassTypeParameters, this.typeSolver);
      }
   }
}
