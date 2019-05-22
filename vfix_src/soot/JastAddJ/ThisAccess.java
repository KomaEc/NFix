package soot.JastAddJ;

import beaver.Symbol;
import soot.Value;

public class ThisAccess extends Access implements Cloneable {
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

   public ThisAccess clone() throws CloneNotSupportedException {
      ThisAccess node = (ThisAccess)super.clone();
      node.decl_computed = false;
      node.decl_value = null;
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ThisAccess copy() {
      try {
         ThisAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ThisAccess fullCopy() {
      ThisAccess tree = this.copy();
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
      s.append("this");
   }

   public void nameCheck() {
      if (this.inExplicitConstructorInvocation() && this.hostType() == this.type()) {
         this.error("this may not be accessed in an explicit constructor invocation");
      } else if (this.isQualified()) {
         if (this.inStaticContext()) {
            this.error("qualified this may not occur in static context");
         } else if (!this.hostType().isInnerTypeOf(this.decl()) && this.hostType() != this.decl()) {
            this.error("qualified this must name an enclosing type: " + this.getParent());
         }
      } else if (!this.isQualified() && this.inStaticContext()) {
         this.error("this may not be accessed in static context: " + this.enclosingStmt());
      }

   }

   public Value eval(Body b) {
      return this.emitThis(b, this.decl());
   }

   public ThisAccess() {
   }

   public void init$Children() {
   }

   public ThisAccess(String p0) {
      this.setID(p0);
   }

   public ThisAccess(Symbol p0) {
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

   private TypeDecl refined_TypeScopePropagation_ThisAccess_decl() {
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
      TypeDecl typeDecl = this.refined_TypeScopePropagation_ThisAccess_decl();
      if (typeDecl instanceof ParTypeDecl) {
         typeDecl = ((ParTypeDecl)typeDecl).genericDecl();
      }

      return typeDecl;
   }

   public boolean isThisAccess() {
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
      return this.decl();
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
