package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class TypeAccess extends Access implements Cloneable {
   protected String tokenString_Package;
   public int Packagestart;
   public int Packageend;
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;
   protected boolean decls_computed;
   protected SimpleSet decls_value;
   protected boolean decl_computed;
   protected TypeDecl decl_value;
   protected boolean type_computed;
   protected TypeDecl type_value;

   public void flushCache() {
      super.flushCache();
      this.decls_computed = false;
      this.decls_value = null;
      this.decl_computed = false;
      this.decl_value = null;
      this.type_computed = false;
      this.type_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public TypeAccess clone() throws CloneNotSupportedException {
      TypeAccess node = (TypeAccess)super.clone();
      node.decls_computed = false;
      node.decls_value = null;
      node.decl_computed = false;
      node.decl_value = null;
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public TypeAccess copy() {
      try {
         TypeAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public TypeAccess fullCopy() {
      TypeAccess tree = this.copy();
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            ASTNode child = this.children[i];
            if (child != null) {
               child = child.fullCopy();
               tree.setChild(child, i);
            }
         }
      }

      return tree;
   }

   public void accessControl() {
      super.accessControl();
      TypeDecl hostType = this.hostType();
      if (hostType != null && !hostType.isUnknown() && !this.type().accessibleFrom(hostType)) {
         this.error("" + this + " in " + this.hostType().fullName() + " can not access type " + this.type().fullName());
      } else if ((hostType == null || hostType.isUnknown()) && !this.type().accessibleFromPackage(this.hostPackage())) {
         this.error("" + this + " can not access type " + this.type().fullName());
      }

   }

   public void nameCheck() {
      if (this.isQualified() && !this.qualifier().isTypeAccess() && !this.qualifier().isPackageAccess()) {
         this.error("can not access the type named " + this.decl().typeName() + " in this context");
      }

      if (this.decls().isEmpty()) {
         this.error("no visible type named " + this.typeName());
      }

      if (this.decls().size() > 1) {
         StringBuffer s = new StringBuffer();
         s.append("several types named " + this.name() + ":");
         Iterator iter = this.decls().iterator();

         while(iter.hasNext()) {
            TypeDecl t = (TypeDecl)iter.next();
            s.append(" " + t.typeName());
         }

         this.error(s.toString());
      }

   }

   public TypeAccess(String name, int start, int end) {
      this(name);
      this.start = this.IDstart = start;
      this.end = this.IDend = end;
   }

   public TypeAccess(String typeName) {
      this("", typeName);
   }

   public void toString(StringBuffer s) {
      if (this.decl().isReferenceType()) {
         s.append(this.nameWithPackage());
      } else {
         s.append(this.decl().name());
      }

   }

   public void checkModifiers() {
      if (this.decl().isDeprecated() && !this.withinDeprecatedAnnotation() && (this.hostType() == null || this.hostType().topLevelType() != this.decl().topLevelType()) && !this.withinSuppressWarnings("deprecation")) {
         this.warning(this.decl().typeName() + " has been deprecated");
      }

   }

   public boolean isRaw() {
      ASTNode parent;
      for(parent = this.getParent(); parent instanceof AbstractDot; parent = parent.getParent()) {
      }

      if (parent instanceof ParTypeAccess) {
         return false;
      } else {
         return !(parent instanceof ImportDecl);
      }
   }

   public void typeCheck() {
      TypeDecl type = this.type();
      if (type.isRawType() && type.isNestedType() && type.enclosingType().isParameterizedType() && !type.enclosingType().isRawType()) {
         this.error("Can not access a member type of a paramterized type as a raw type");
      }

   }

   public void transformation() {
      super.transformation();
      if (this.type().elementType().isNestedType() && this.hostType() != null) {
         this.hostType().addUsedNestedType(this.type().elementType());
      }

   }

   public TypeAccess() {
      this.decls_computed = false;
      this.decl_computed = false;
      this.type_computed = false;
   }

   public void init$Children() {
   }

   public TypeAccess(String p0, String p1) {
      this.decls_computed = false;
      this.decl_computed = false;
      this.type_computed = false;
      this.setPackage(p0);
      this.setID(p1);
   }

   public TypeAccess(Symbol p0, Symbol p1) {
      this.decls_computed = false;
      this.decl_computed = false;
      this.type_computed = false;
      this.setPackage(p0);
      this.setID(p1);
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setPackage(String value) {
      this.tokenString_Package = value;
   }

   public void setPackage(Symbol symbol) {
      if (symbol.value != null && !(symbol.value instanceof String)) {
         throw new UnsupportedOperationException("setPackage is only valid for String lexemes");
      } else {
         this.tokenString_Package = (String)symbol.value;
         this.Packagestart = symbol.getStart();
         this.Packageend = symbol.getEnd();
      }
   }

   public String getPackage() {
      return this.tokenString_Package != null ? this.tokenString_Package : "";
   }

   public void setID(String value) {
      this.tokenString_ID = value;
   }

   public void setID(Symbol symbol) {
      if (symbol.value != null && !(symbol.value instanceof String)) {
         throw new UnsupportedOperationException("setID is only valid for String lexemes");
      } else {
         this.tokenString_ID = (String)symbol.value;
         this.IDstart = symbol.getStart();
         this.IDend = symbol.getEnd();
      }
   }

   public String getID() {
      return this.tokenString_ID != null ? this.tokenString_ID : "";
   }

   private TypeDecl refined_TypeScopePropagation_TypeAccess_decl() {
      SimpleSet decls = this.decls();
      return decls.size() == 1 ? (TypeDecl)decls.iterator().next() : this.unknownType();
   }

   public SimpleSet decls() {
      if (this.decls_computed) {
         return this.decls_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.decls_value = this.decls_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.decls_computed = true;
         }

         return this.decls_value;
      }
   }

   private SimpleSet decls_compute() {
      if (this.packageName().equals("")) {
         return this.lookupType(this.name());
      } else {
         TypeDecl typeDecl = this.lookupType(this.packageName(), this.name());
         return typeDecl != null ? SimpleSet.emptySet.add(typeDecl) : SimpleSet.emptySet;
      }
   }

   public TypeDecl decl() {
      if (this.decl_computed) {
         return this.decl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.decl_value = this.decl_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.decl_computed = true;
         }

         return this.decl_value;
      }
   }

   private TypeDecl decl_compute() {
      TypeDecl decl = this.refined_TypeScopePropagation_TypeAccess_decl();
      return decl instanceof GenericTypeDecl && this.isRaw() ? ((GenericTypeDecl)decl).lookupParTypeDecl(new ArrayList()) : decl;
   }

   public SimpleSet qualifiedLookupVariable(String name) {
      ASTNode$State state = this.state();
      if (this.type().accessibleFrom(this.hostType())) {
         SimpleSet c = this.type().memberFields(name);
         c = this.keepAccessibleFields(c);
         if (this.type().isClassDecl() && c.size() == 1) {
            c = this.removeInstanceVariables(c);
         }

         return c;
      } else {
         return SimpleSet.emptySet;
      }
   }

   public String dumpString() {
      ASTNode$State state = this.state();
      return this.getClass().getName() + " [" + this.getPackage() + ", " + this.getID() + "]";
   }

   public String name() {
      ASTNode$State state = this.state();
      return this.getID();
   }

   public String packageName() {
      ASTNode$State state = this.state();
      return this.getPackage();
   }

   public String nameWithPackage() {
      ASTNode$State state = this.state();
      return this.getPackage().equals("") ? this.name() : this.getPackage() + "." + this.name();
   }

   public String typeName() {
      ASTNode$State state = this.state();
      return this.isQualified() ? this.qualifier().typeName() + "." + this.name() : this.nameWithPackage();
   }

   public boolean isTypeAccess() {
      ASTNode$State state = this.state();
      return true;
   }

   public NameType predNameType() {
      ASTNode$State state = this.state();
      return NameType.PACKAGE_OR_TYPE_NAME;
   }

   public TypeDecl type() {
      if (this.type_computed) {
         return this.type_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.type_value = this.type_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.type_computed = true;
         }

         return this.type_value;
      }
   }

   private TypeDecl type_compute() {
      return this.decl();
   }

   public boolean staticContextQualifier() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean usesTypeVariable() {
      ASTNode$State state = this.state();
      return this.decl().usesTypeVariable() || super.usesTypeVariable();
   }

   public Access substituted(Collection<TypeVariable> original, List<TypeVariable> substitution) {
      ASTNode$State state = this.state();
      TypeDecl decl = this.decl();
      int i = 0;

      for(Iterator var6 = original.iterator(); var6.hasNext(); ++i) {
         TypeVariable typeVar = (TypeVariable)var6.next();
         if (typeVar == decl) {
            return new TypeAccess(((TypeVariable)substitution.getChild(i)).getID());
         }
      }

      return super.substituted(original, substitution);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
