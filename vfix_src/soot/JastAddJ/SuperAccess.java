package soot.JastAddJ;

import beaver.Symbol;
import soot.Value;

public class SuperAccess extends Access implements Cloneable {
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;
   protected boolean decl_computed = false;
   protected TypeDecl decl_value;
   protected boolean type_computed = false;
   protected TypeDecl type_value;

   public void flushCache() {
      super.flushCache();
      this.decl_computed = false;
      this.decl_value = null;
      this.type_computed = false;
      this.type_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public SuperAccess clone() throws CloneNotSupportedException {
      SuperAccess node = (SuperAccess)super.clone();
      node.decl_computed = false;
      node.decl_value = null;
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public SuperAccess copy() {
      try {
         SuperAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public SuperAccess fullCopy() {
      SuperAccess tree = this.copy();
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

   public void toString(StringBuffer s) {
      s.append("super");
   }

   public void nameCheck() {
      if (this.isQualified()) {
         if (!this.hostType().isInnerTypeOf(this.decl()) && this.hostType() != this.decl()) {
            this.error("qualified super must name an enclosing type");
         }

         if (this.inStaticContext()) {
            this.error("*** Qualified super may not occur in static context");
         }
      }

      if (this.inExplicitConstructorInvocation() && this.hostType().instanceOf(this.decl().hostType())) {
         this.error("super may not be accessed in an explicit constructor invocation");
      }

      if (this.inStaticContext()) {
         this.error("super may not be accessed in a static context");
      }

   }

   public Value eval(Body b) {
      return this.emitThis(b, this.decl());
   }

   public SuperAccess() {
   }

   public void init$Children() {
   }

   public SuperAccess(String p0) {
      this.setID(p0);
   }

   public SuperAccess(Symbol p0) {
      this.setID(p0);
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return false;
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

   private TypeDecl refined_TypeScopePropagation_SuperAccess_decl() {
      return this.isQualified() ? this.qualifier().type() : this.hostType();
   }

   public SimpleSet decls() {
      ASTNode$State state = this.state();
      return SimpleSet.emptySet;
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
      TypeDecl typeDecl = this.refined_TypeScopePropagation_SuperAccess_decl();
      if (typeDecl instanceof ParTypeDecl) {
         typeDecl = ((ParTypeDecl)typeDecl).genericDecl();
      }

      return typeDecl;
   }

   public boolean isSuperAccess() {
      ASTNode$State state = this.state();
      return true;
   }

   public NameType predNameType() {
      ASTNode$State state = this.state();
      return NameType.TYPE_NAME;
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
      TypeDecl typeDecl = this.decl();
      if (!typeDecl.isClassDecl()) {
         return this.unknownType();
      } else {
         ClassDecl classDecl = (ClassDecl)typeDecl;
         return (TypeDecl)(!classDecl.hasSuperclass() ? this.unknownType() : classDecl.superclass());
      }
   }

   public boolean inExplicitConstructorInvocation() {
      ASTNode$State state = this.state();
      boolean inExplicitConstructorInvocation_value = this.getParent().Define_boolean_inExplicitConstructorInvocation(this, (ASTNode)null);
      return inExplicitConstructorInvocation_value;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
