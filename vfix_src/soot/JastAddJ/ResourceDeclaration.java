package soot.JastAddJ;

import beaver.Symbol;

public class ResourceDeclaration extends VariableDeclaration implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ResourceDeclaration clone() throws CloneNotSupportedException {
      ResourceDeclaration node = (ResourceDeclaration)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ResourceDeclaration copy() {
      try {
         ResourceDeclaration node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ResourceDeclaration fullCopy() {
      ResourceDeclaration tree = this.copy();
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
      TypeDecl typeAutoCloseable = this.lookupType("java.lang", "AutoCloseable");
      if (typeAutoCloseable == null) {
         this.error("java.lang.AutoCloseable not found");
      } else if (!this.getTypeAccess().type().instanceOf(typeAutoCloseable)) {
         this.error("Resource specification must declare an AutoCloseable resource");
      }

   }

   public void nameCheck() {
      if (this.resourcePreviouslyDeclared(this.name())) {
         this.error("A resource with the name " + this.name() + " has already been declared in this try statement.");
      }

      super.nameCheck();
   }

   public ResourceDeclaration() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new Opt(), 2);
   }

   public ResourceDeclaration(Modifiers p0, Access p1, String p2, Opt<Expr> p3) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
   }

   public ResourceDeclaration(Modifiers p0, Access p1, Symbol p2, Opt<Expr> p3) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
   }

   protected int numChildren() {
      return 3;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setModifiers(Modifiers node) {
      this.setChild(node, 0);
   }

   public Modifiers getModifiers() {
      return (Modifiers)this.getChild(0);
   }

   public Modifiers getModifiersNoTransform() {
      return (Modifiers)this.getChildNoTransform(0);
   }

   public void setTypeAccess(Access node) {
      this.setChild(node, 1);
   }

   public Access getTypeAccess() {
      return (Access)this.getChild(1);
   }

   public Access getTypeAccessNoTransform() {
      return (Access)this.getChildNoTransform(1);
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

   public void setInitOpt(Opt<Expr> opt) {
      this.setChild(opt, 2);
   }

   public boolean hasInit() {
      return this.getInitOpt().getNumChild() != 0;
   }

   public Expr getInit() {
      return (Expr)this.getInitOpt().getChild(0);
   }

   public void setInit(Expr node) {
      this.getInitOpt().setChild(node, 0);
   }

   public Opt<Expr> getInitOpt() {
      return (Opt)this.getChild(2);
   }

   public Opt<Expr> getInitOptNoTransform() {
      return (Opt)this.getChildNoTransform(2);
   }

   public TypeDecl lookupType(String packageName, String typeName) {
      ASTNode$State state = this.state();
      TypeDecl lookupType_String_String_value = this.getParent().Define_TypeDecl_lookupType(this, (ASTNode)null, packageName, typeName);
      return lookupType_String_String_value;
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getTypeAccessNoTransform() ? NameType.TYPE_NAME : super.Define_NameType_nameType(caller, child);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
