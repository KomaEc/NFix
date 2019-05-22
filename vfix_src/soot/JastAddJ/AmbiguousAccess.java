package soot.JastAddJ;

import beaver.Symbol;

public class AmbiguousAccess extends Access implements Cloneable {
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;

   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public AmbiguousAccess clone() throws CloneNotSupportedException {
      AmbiguousAccess node = (AmbiguousAccess)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public AmbiguousAccess copy() {
      try {
         AmbiguousAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public AmbiguousAccess fullCopy() {
      AmbiguousAccess tree = this.copy();
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
      this.error("ambiguous name " + this.name());
   }

   public AmbiguousAccess(String name, int start, int end) {
      this(name);
      this.start = this.IDstart = start;
      this.end = this.IDend = end;
   }

   public AmbiguousAccess() {
   }

   public void init$Children() {
   }

   public AmbiguousAccess(String p0) {
      this.setID(p0);
   }

   public AmbiguousAccess(Symbol p0) {
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

   public NameType predNameType() {
      ASTNode$State state = this.state();
      return NameType.AMBIGUOUS_NAME;
   }

   public ASTNode rewriteTo() {
      if (!this.duringSyntacticClassification()) {
         ++this.state().duringNameResolution;
         ASTNode result = this.rewriteRule0();
         --this.state().duringNameResolution;
         return result;
      } else {
         return super.rewriteTo();
      }
   }

   private Access rewriteRule0() {
      if (!this.lookupVariable(this.name()).isEmpty()) {
         return new VarAccess(this.name(), this.start(), this.end());
      } else {
         return (Access)(!this.lookupType(this.name()).isEmpty() ? new TypeAccess(this.name(), this.start(), this.end()) : new PackageAccess(this.name(), this.start(), this.end()));
      }
   }
}
