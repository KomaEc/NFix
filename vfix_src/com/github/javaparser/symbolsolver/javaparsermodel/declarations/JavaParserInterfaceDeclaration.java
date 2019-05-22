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
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.logic.AbstractTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.LazyType;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.resolution.SymbolSolver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaParserInterfaceDeclaration extends AbstractTypeDeclaration implements ResolvedInterfaceDeclaration {
   private TypeSolver typeSolver;
   private ClassOrInterfaceDeclaration wrappedNode;
   private JavaParserTypeAdapter<ClassOrInterfaceDeclaration> javaParserTypeAdapter;

   public JavaParserInterfaceDeclaration(ClassOrInterfaceDeclaration wrappedNode, TypeSolver typeSolver) {
      if (!wrappedNode.isInterface()) {
         throw new IllegalArgumentException();
      } else {
         this.wrappedNode = wrappedNode;
         this.typeSolver = typeSolver;
         this.javaParserTypeAdapter = new JavaParserTypeAdapter(wrappedNode, typeSolver);
      }
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

   public Context getContext() {
      return JavaParserFactory.getContext(this.wrappedNode, this.typeSolver);
   }

   public ResolvedType getUsage(Node node) {
      throw new UnsupportedOperationException();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         JavaParserInterfaceDeclaration that = (JavaParserInterfaceDeclaration)o;
         return this.wrappedNode.equals(that.wrappedNode);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.wrappedNode.hashCode();
   }

   public String getName() {
      return this.wrappedNode.getName().getId();
   }

   public ResolvedInterfaceDeclaration asInterface() {
      return this;
   }

   public boolean hasDirectlyAnnotation(String canonicalName) {
      return AstResolutionUtils.hasDirectlyAnnotation(this.wrappedNode, this.typeSolver, canonicalName);
   }

   public boolean isInterface() {
      return true;
   }

   public List<ResolvedReferenceType> getInterfacesExtended() {
      List<ResolvedReferenceType> interfaces = new ArrayList();
      Iterator var2 = this.wrappedNode.getExtendedTypes().iterator();

      while(var2.hasNext()) {
         ClassOrInterfaceType t = (ClassOrInterfaceType)var2.next();
         interfaces.add(new ReferenceTypeImpl(((ResolvedTypeDeclaration)this.solveType(t.getName().getId(), this.typeSolver).getCorrespondingDeclaration()).asInterface(), this.typeSolver));
      }

      return interfaces;
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
         Iterator var2;
         ClassOrInterfaceType type;
         ResolvedReferenceTypeDeclaration ancestor;
         if (this.wrappedNode.getExtendedTypes() != null) {
            var2 = this.wrappedNode.getExtendedTypes().iterator();

            while(var2.hasNext()) {
               type = (ClassOrInterfaceType)var2.next();
               ancestor = (ResolvedReferenceTypeDeclaration)(new SymbolSolver(this.typeSolver)).solveType(type);
               if (ancestor.canBeAssignedTo(other)) {
                  return true;
               }
            }
         }

         if (this.wrappedNode.getImplementedTypes() != null) {
            var2 = this.wrappedNode.getImplementedTypes().iterator();

            while(var2.hasNext()) {
               type = (ClassOrInterfaceType)var2.next();
               ancestor = (ResolvedReferenceTypeDeclaration)(new SymbolSolver(this.typeSolver)).solveType(type);
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

   public List<ResolvedFieldDeclaration> getAllFields() {
      List<ResolvedFieldDeclaration> fields = this.javaParserTypeAdapter.getFieldsForDeclaredVariables();
      this.getAncestors().forEach((ancestor) -> {
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

   public String toString() {
      return "JavaParserInterfaceDeclaration{wrappedNode=" + this.wrappedNode + '}';
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
            return name.startsWith(prefix) && name.length() > prefix.length() ? (new JavaParserInterfaceDeclaration(this.wrappedNode, typeSolver)).solveType(name.substring(prefix.length()), typeSolver) : this.getContext().getParent().solveType(name, typeSolver);
         }
      }
   }

   public List<ResolvedReferenceType> getAncestors(boolean acceptIncompleteList) {
      List<ResolvedReferenceType> ancestors = new ArrayList();
      Iterator var3;
      ClassOrInterfaceType implemented;
      if (this.wrappedNode.getExtendedTypes() != null) {
         var3 = this.wrappedNode.getExtendedTypes().iterator();

         while(var3.hasNext()) {
            implemented = (ClassOrInterfaceType)var3.next();

            try {
               ancestors.add(this.toReferenceType(implemented));
            } catch (UnsolvedSymbolException var7) {
               if (!acceptIncompleteList) {
                  throw var7;
               }
            }
         }
      }

      if (this.wrappedNode.getImplementedTypes() != null) {
         var3 = this.wrappedNode.getImplementedTypes().iterator();

         while(var3.hasNext()) {
            implemented = (ClassOrInterfaceType)var3.next();

            try {
               ancestors.add(this.toReferenceType(implemented));
            } catch (UnsolvedSymbolException var6) {
               if (!acceptIncompleteList) {
                  throw var6;
               }
            }
         }
      }

      return ancestors;
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return this.wrappedNode.getTypeParameters() == null ? Collections.emptyList() : (List)this.wrappedNode.getTypeParameters().stream().map((tp) -> {
         return new JavaParserTypeParameter(tp, this.typeSolver);
      }).collect(Collectors.toList());
   }

   public ClassOrInterfaceDeclaration getWrappedNode() {
      return this.wrappedNode;
   }

   public AccessSpecifier accessSpecifier() {
      return AstResolutionUtils.toAccessLevel(this.wrappedNode.getModifiers());
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

   public List<ResolvedConstructorDeclaration> getConstructors() {
      return Collections.emptyList();
   }

   public Optional<ClassOrInterfaceDeclaration> toAst() {
      return Optional.of(this.wrappedNode);
   }

   private ResolvedReferenceType toReferenceType(ClassOrInterfaceType classOrInterfaceType) {
      SymbolReference<? extends ResolvedTypeDeclaration> ref = null;
      String typeName = classOrInterfaceType.asString();
      if (typeName.indexOf(46) > -1) {
         ref = this.typeSolver.tryToSolveType(typeName);
      }

      if (ref == null || !ref.isSolved()) {
         ref = this.solveType(typeName, this.typeSolver);
      }

      if (!ref.isSolved()) {
         ref = this.solveType(classOrInterfaceType.getName().getId(), this.typeSolver);
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
