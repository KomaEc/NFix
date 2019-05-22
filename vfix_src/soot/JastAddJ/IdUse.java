package soot.JastAddJ;

import beaver.Symbol;

public class IdUse extends ASTNode<ASTNode> implements Cloneable {
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;

   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public IdUse clone() throws CloneNotSupportedException {
      IdUse node = (IdUse)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public IdUse copy() {
      try {
         IdUse node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public IdUse fullCopy() {
      IdUse tree = this.copy();
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

   public IdUse() {
      this.is$Final(true);
   }

   public void init$Children() {
   }

   public IdUse(String p0) {
      this.setID(p0);
      this.is$Final(true);
   }

   public IdUse(Symbol p0) {
      this.setID(p0);
      this.is$Final(true);
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

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
