package soot.JastAddJ;

import beaver.Symbol;

public class PackageAccess extends Access implements Cloneable {
   protected String tokenString_Package;
   public int Packagestart;
   public int Packageend;

   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public PackageAccess clone() throws CloneNotSupportedException {
      PackageAccess node = (PackageAccess)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public PackageAccess copy() {
      try {
         PackageAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public PackageAccess fullCopy() {
      PackageAccess tree = this.copy();
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
      if (!this.hasPackage(this.packageName())) {
         this.error(this.packageName() + " not found");
      }

   }

   public PackageAccess(String name, int start, int end) {
      this(name);
      this.start = this.Packagestart = start;
      this.end = this.Packageend = end;
   }

   public void toString(StringBuffer s) {
      s.append(this.getPackage());
   }

   public PackageAccess() {
   }

   public void init$Children() {
   }

   public PackageAccess(String p0) {
      this.setPackage(p0);
   }

   public PackageAccess(Symbol p0) {
      this.setPackage(p0);
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

   public boolean hasQualifiedPackage(String packageName) {
      ASTNode$State state = this.state();
      return this.hasPackage(this.packageName() + "." + packageName);
   }

   public SimpleSet qualifiedLookupType(String name) {
      ASTNode$State state = this.state();
      SimpleSet c = SimpleSet.emptySet;
      TypeDecl typeDecl = this.lookupType(this.packageName(), name);
      if (this.nextAccess() instanceof ClassInstanceExpr) {
         if (typeDecl != null && typeDecl.accessibleFrom(this.hostType())) {
            c = c.add(typeDecl);
         }

         return c;
      } else {
         if (typeDecl != null) {
            if (this.hostType() != null && typeDecl.accessibleFrom(this.hostType())) {
               c = c.add(typeDecl);
            } else if (this.hostType() == null && typeDecl.accessibleFromPackage(this.hostPackage())) {
               c = c.add(typeDecl);
            }
         }

         return c;
      }
   }

   public SimpleSet qualifiedLookupVariable(String name) {
      ASTNode$State state = this.state();
      return SimpleSet.emptySet;
   }

   public String dumpString() {
      ASTNode$State state = this.state();
      return this.getClass().getName() + " [" + this.getPackage() + "]";
   }

   public String name() {
      ASTNode$State state = this.state();
      return this.getPackage();
   }

   public String packageName() {
      ASTNode$State state = this.state();
      StringBuffer s = new StringBuffer();
      if (this.hasPrevExpr()) {
         s.append(this.prevExpr().packageName());
         s.append(".");
      }

      s.append(this.getPackage());
      return s.toString();
   }

   public boolean isPackageAccess() {
      ASTNode$State state = this.state();
      return true;
   }

   public NameType predNameType() {
      ASTNode$State state = this.state();
      return NameType.PACKAGE_NAME;
   }

   public boolean isUnknown() {
      ASTNode$State state = this.state();
      return !this.hasPackage(this.packageName());
   }

   public boolean hasPackage(String packageName) {
      ASTNode$State state = this.state();
      boolean hasPackage_String_value = this.getParent().Define_boolean_hasPackage(this, (ASTNode)null, packageName);
      return hasPackage_String_value;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
