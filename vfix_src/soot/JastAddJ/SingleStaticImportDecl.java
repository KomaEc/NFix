package soot.JastAddJ;

import beaver.Symbol;

public class SingleStaticImportDecl extends StaticImportDecl implements Cloneable {
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;

   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public SingleStaticImportDecl clone() throws CloneNotSupportedException {
      SingleStaticImportDecl node = (SingleStaticImportDecl)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public SingleStaticImportDecl copy() {
      try {
         SingleStaticImportDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public SingleStaticImportDecl fullCopy() {
      SingleStaticImportDecl tree = this.copy();
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

   public void typeCheck() {
      if (!this.getAccess().type().typeName().equals(this.typeName()) && !this.getAccess().type().isUnknown()) {
         this.error("Single-type import " + this.typeName() + " is not the canonical name of type " + this.getAccess().type().typeName());
      }

   }

   public void nameCheck() {
      if (this.importedFields(this.name()).isEmpty() && this.importedMethods(this.name()).isEmpty() && this.importedTypes(this.name()).isEmpty() && !this.getAccess().type().isUnknown()) {
         this.error("Semantic Error: At least one static member named " + this.name() + " must be available in static imported type " + this.type().fullName());
      }

   }

   public void toString(StringBuffer s) {
      s.append("import static ");
      this.getAccess().toString(s);
      s.append("." + this.getID());
      s.append(";\n");
   }

   public SingleStaticImportDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public SingleStaticImportDecl(Access p0, String p1) {
      this.setChild(p0, 0);
      this.setID(p1);
   }

   public SingleStaticImportDecl(Access p0, Symbol p1) {
      this.setChild(p0, 0);
      this.setID(p1);
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

   public TypeDecl type() {
      ASTNode$State state = this.state();
      return this.getAccess().type();
   }

   public String name() {
      ASTNode$State state = this.state();
      return this.getID();
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getAccessNoTransform() ? NameType.TYPE_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
