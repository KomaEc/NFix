package soot.JastAddJ;

import beaver.Symbol;

public class ParseName extends Access implements Cloneable {
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;

   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ParseName clone() throws CloneNotSupportedException {
      ParseName node = (ParseName)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ParseName copy() {
      try {
         ParseName node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ParseName fullCopy() {
      ParseName tree = this.copy();
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

   public void toString(StringBuffer sb) {
      sb.append(this.getID());
   }

   public ParseName() {
   }

   public void init$Children() {
   }

   public ParseName(String p0) {
      this.setID(p0);
   }

   public ParseName(Symbol p0) {
      this.setID(p0);
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return true;
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

   public SimpleSet qualifiedLookupType(String name) {
      ASTNode$State state = this.state();
      return SimpleSet.emptySet;
   }

   public SimpleSet qualifiedLookupVariable(String name) {
      ASTNode$State state = this.state();
      return SimpleSet.emptySet;
   }

   public String dumpString() {
      ASTNode$State state = this.state();
      return this.getClass().getName() + " [" + this.getID() + "]";
   }

   public String name() {
      ASTNode$State state = this.state();
      return this.getID();
   }

   public ASTNode rewriteTo() {
      ++this.state().duringSyntacticClassification;
      ASTNode result = this.rewriteRule0();
      --this.state().duringSyntacticClassification;
      return result;
   }

   private Access rewriteRule0() {
      return this.nameType().reclassify(this.name(), this.start, this.end);
   }
}
