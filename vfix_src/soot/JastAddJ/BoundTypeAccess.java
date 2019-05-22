package soot.JastAddJ;

import beaver.Symbol;

public class BoundTypeAccess extends TypeAccess implements Cloneable {
   protected TypeDecl tokenTypeDecl_TypeDecl;
   protected boolean decls_computed = false;
   protected SimpleSet decls_value;

   public void flushCache() {
      super.flushCache();
      this.decls_computed = false;
      this.decls_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public BoundTypeAccess clone() throws CloneNotSupportedException {
      BoundTypeAccess node = (BoundTypeAccess)super.clone();
      node.decls_computed = false;
      node.decls_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public BoundTypeAccess copy() {
      try {
         BoundTypeAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public BoundTypeAccess fullCopy() {
      BoundTypeAccess tree = this.copy();
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

   public boolean isRaw() {
      return this.getTypeDecl().isRawType();
   }

   public BoundTypeAccess() {
   }

   public void init$Children() {
   }

   public BoundTypeAccess(String p0, String p1, TypeDecl p2) {
      this.setPackage(p0);
      this.setID(p1);
      this.setTypeDecl(p2);
   }

   public BoundTypeAccess(Symbol p0, Symbol p1, TypeDecl p2) {
      this.setPackage(p0);
      this.setID(p1);
      this.setTypeDecl(p2);
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

   public void setTypeDecl(TypeDecl value) {
      this.tokenTypeDecl_TypeDecl = value;
   }

   public TypeDecl getTypeDecl() {
      return this.tokenTypeDecl_TypeDecl;
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
      return SimpleSet.emptySet.add(this.getTypeDecl());
   }

   public String dumpString() {
      ASTNode$State state = this.state();
      return this.getClass().getName() + " [" + this.getTypeDecl().fullName() + "]";
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
