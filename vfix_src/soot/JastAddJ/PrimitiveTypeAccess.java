package soot.JastAddJ;

import beaver.Symbol;

public class PrimitiveTypeAccess extends TypeAccess implements Cloneable {
   protected String tokenString_Name;
   public int Namestart;
   public int Nameend;
   protected String tokenString_Package;
   protected String tokenString_ID;
   protected boolean decls_computed = false;
   protected SimpleSet decls_value;
   protected boolean getPackage_computed = false;
   protected String getPackage_value;
   protected boolean getID_computed = false;
   protected String getID_value;

   public void flushCache() {
      super.flushCache();
      this.decls_computed = false;
      this.decls_value = null;
      this.getPackage_computed = false;
      this.getPackage_value = null;
      this.getID_computed = false;
      this.getID_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public PrimitiveTypeAccess clone() throws CloneNotSupportedException {
      PrimitiveTypeAccess node = (PrimitiveTypeAccess)super.clone();
      node.decls_computed = false;
      node.decls_value = null;
      node.getPackage_computed = false;
      node.getPackage_value = null;
      node.getID_computed = false;
      node.getID_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public PrimitiveTypeAccess copy() {
      try {
         PrimitiveTypeAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public PrimitiveTypeAccess fullCopy() {
      PrimitiveTypeAccess tree = this.copy();
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

   public PrimitiveTypeAccess() {
   }

   public void init$Children() {
   }

   public PrimitiveTypeAccess(String p0) {
      this.setName(p0);
   }

   public PrimitiveTypeAccess(Symbol p0) {
      this.setName(p0);
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setName(String value) {
      this.tokenString_Name = value;
   }

   public void setName(Symbol symbol) {
      if (symbol.value != null && !(symbol.value instanceof String)) {
         throw new UnsupportedOperationException("setName is only valid for String lexemes");
      } else {
         this.tokenString_Name = (String)symbol.value;
         this.Namestart = symbol.getStart();
         this.Nameend = symbol.getEnd();
      }
   }

   public String getName() {
      return this.tokenString_Name != null ? this.tokenString_Name : "";
   }

   public void setPackage(String value) {
      this.tokenString_Package = value;
   }

   public void setID(String value) {
      this.tokenString_ID = value;
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
      return this.lookupType("@primitive", this.name());
   }

   public String getPackage() {
      if (this.getPackage_computed) {
         return this.getPackage_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getPackage_value = this.getPackage_compute();
         this.setPackage(this.getPackage_value);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getPackage_computed = true;
         }

         return this.getPackage_value;
      }
   }

   private String getPackage_compute() {
      return "@primitive";
   }

   public String getID() {
      if (this.getID_computed) {
         return this.getID_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getID_value = this.getID_compute();
         this.setID(this.getID_value);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getID_computed = true;
         }

         return this.getID_value;
      }
   }

   private String getID_compute() {
      return this.getName();
   }

   public String dumpString() {
      ASTNode$State state = this.state();
      return this.getClass().getName() + " [" + this.getName() + "]";
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
