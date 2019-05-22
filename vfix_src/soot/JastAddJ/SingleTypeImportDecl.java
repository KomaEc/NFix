package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;

public class SingleTypeImportDecl extends ImportDecl implements Cloneable {
   protected Map importedTypes_String_values;

   public void flushCache() {
      super.flushCache();
      this.importedTypes_String_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public SingleTypeImportDecl clone() throws CloneNotSupportedException {
      SingleTypeImportDecl node = (SingleTypeImportDecl)super.clone();
      node.importedTypes_String_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public SingleTypeImportDecl copy() {
      try {
         SingleTypeImportDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public SingleTypeImportDecl fullCopy() {
      SingleTypeImportDecl tree = this.copy();
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
      if (!this.getAccess().type().typeName().equals(this.typeName()) && !this.getAccess().type().isUnknown()) {
         this.error("Single-type import " + this.typeName() + " is not the canonical name of type " + this.getAccess().type().typeName());
      } else if (this.allImportedTypes(this.getAccess().type().name()).size() > 1) {
         this.error(this.getAccess().type().name() + " is imported multiple times");
      }

   }

   public void toString(StringBuffer s) {
      s.append("import ");
      this.getAccess().toString(s);
      s.append(";\n");
   }

   public SingleTypeImportDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public SingleTypeImportDecl(Access p0) {
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

   public SimpleSet importedTypes(String name) {
      if (this.importedTypes_String_values == null) {
         this.importedTypes_String_values = new HashMap(4);
      }

      if (this.importedTypes_String_values.containsKey(name)) {
         return (SimpleSet)this.importedTypes_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet importedTypes_String_value = this.importedTypes_compute(name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.importedTypes_String_values.put(name, importedTypes_String_value);
         }

         return importedTypes_String_value;
      }
   }

   private SimpleSet importedTypes_compute(String name) {
      SimpleSet set = SimpleSet.emptySet;
      if (this.getAccess().type().name().equals(name)) {
         set = set.add(this.getAccess().type());
      }

      return set;
   }

   public SimpleSet allImportedTypes(String name) {
      ASTNode$State state = this.state();
      SimpleSet allImportedTypes_String_value = this.getParent().Define_SimpleSet_allImportedTypes(this, (ASTNode)null, name);
      return allImportedTypes_String_value;
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getAccessNoTransform() ? NameType.TYPE_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
