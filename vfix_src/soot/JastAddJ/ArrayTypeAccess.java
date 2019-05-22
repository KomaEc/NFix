package soot.JastAddJ;

import java.util.ArrayList;

public class ArrayTypeAccess extends TypeAccess implements Cloneable {
   protected String tokenString_Package;
   protected String tokenString_ID;
   protected boolean getPackage_computed = false;
   protected String getPackage_value;
   protected boolean getID_computed = false;
   protected String getID_value;
   protected boolean decl_computed = false;
   protected TypeDecl decl_value;

   public void flushCache() {
      super.flushCache();
      this.getPackage_computed = false;
      this.getPackage_value = null;
      this.getID_computed = false;
      this.getID_value = null;
      this.decl_computed = false;
      this.decl_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ArrayTypeAccess clone() throws CloneNotSupportedException {
      ArrayTypeAccess node = (ArrayTypeAccess)super.clone();
      node.getPackage_computed = false;
      node.getPackage_value = null;
      node.getID_computed = false;
      node.getID_value = null;
      node.decl_computed = false;
      node.decl_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ArrayTypeAccess copy() {
      try {
         ArrayTypeAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ArrayTypeAccess fullCopy() {
      ArrayTypeAccess tree = this.copy();
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

   public void nameCheck() {
      if (this.decl().elementType().isUnknown()) {
         this.error("no type named " + this.decl().elementType().typeName());
      }

   }

   public void toString(StringBuffer s) {
      this.getAccess().toString(s);
      s.append("[]");
   }

   public void addArraySize(Body b, ArrayList list) {
      this.getAccess().addArraySize(b, list);
   }

   public ArrayTypeAccess() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public ArrayTypeAccess(Access p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setAccess(Access node) {
      this.setChild(node, 0);
   }

   public Access getAccess() {
      return (Access)this.getChild(0);
   }

   public Access getAccessNoTransform() {
      return (Access)this.getChildNoTransform(0);
   }

   public void setPackage(String value) {
      this.tokenString_Package = value;
   }

   public void setID(String value) {
      this.tokenString_ID = value;
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
      return this.getAccess().type().packageName();
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
      return this.getAccess().type().name();
   }

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getAccess().isDAafter(v);
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getAccess().isDUafter(v);
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
      return this.getAccess().type().arrayType();
   }

   public String dumpString() {
      ASTNode$State state = this.state();
      return this.getClass().getName();
   }

   public NameType predNameType() {
      ASTNode$State state = this.state();
      return NameType.AMBIGUOUS_NAME;
   }

   public boolean staticContextQualifier() {
      ASTNode$State state = this.state();
      return true;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
