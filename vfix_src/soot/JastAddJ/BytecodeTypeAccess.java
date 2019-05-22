package soot.JastAddJ;

import beaver.Symbol;

public class BytecodeTypeAccess extends TypeAccess implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public BytecodeTypeAccess clone() throws CloneNotSupportedException {
      BytecodeTypeAccess node = (BytecodeTypeAccess)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public BytecodeTypeAccess copy() {
      try {
         BytecodeTypeAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public BytecodeTypeAccess fullCopy() {
      BytecodeTypeAccess tree = this.copy();
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

   public BytecodeTypeAccess() {
   }

   public void init$Children() {
   }

   public BytecodeTypeAccess(String p0, String p1) {
      this.setPackage(p0);
      this.setID(p1);
   }

   public BytecodeTypeAccess(Symbol p0, Symbol p1) {
      this.setPackage(p0);
      this.setID(p1);
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return true;
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

   public ASTNode rewriteTo() {
      ++this.state().duringBoundNames;
      ASTNode result = this.rewriteRule0();
      --this.state().duringBoundNames;
      return result;
   }

   private Access rewriteRule0() {
      if (this.name().indexOf("$") == -1) {
         return new TypeAccess(this.packageName(), this.name());
      } else {
         String[] names = this.name().split("\\$");
         Access a = null;
         String newName = null;
         TypeDecl type = null;

         for(int i = 0; i < names.length; ++i) {
            newName = newName == null ? names[i] : newName + "$" + names[i];
            SimpleSet set;
            if (type != null) {
               set = type.memberTypes(newName);
            } else if (this.packageName().equals("")) {
               set = this.lookupType(newName);
            } else {
               TypeDecl typeDecl = this.lookupType(this.packageName(), newName);
               set = SimpleSet.emptySet;
               if (typeDecl != null) {
                  set = set.add(typeDecl);
               }
            }

            if (!set.isEmpty()) {
               a = a == null ? new TypeAccess(this.packageName(), newName) : ((Access)a).qualifiesAccess(new TypeAccess(newName));
               type = (TypeDecl)set.iterator().next();
               newName = null;
            }
         }

         if (a == null) {
            a = new TypeAccess(this.packageName(), this.name());
         }

         return (Access)a;
      }
   }
}
