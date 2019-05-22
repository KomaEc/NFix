package soot.JastAddJ;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TypeImportOnDemandDecl extends ImportDecl implements Cloneable {
   protected Map importedTypes_String_values;

   public void flushCache() {
      super.flushCache();
      this.importedTypes_String_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public TypeImportOnDemandDecl clone() throws CloneNotSupportedException {
      TypeImportOnDemandDecl node = (TypeImportOnDemandDecl)super.clone();
      node.importedTypes_String_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public TypeImportOnDemandDecl copy() {
      try {
         TypeImportOnDemandDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public TypeImportOnDemandDecl fullCopy() {
      TypeImportOnDemandDecl tree = this.copy();
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
      if (this.getAccess().lastAccess().isTypeAccess() && !this.getAccess().type().typeName().equals(this.typeName())) {
         this.error("On demand type import " + this.typeName() + ".* is not the canonical name of type " + this.getAccess().type().typeName());
      }

   }

   public void toString(StringBuffer s) {
      s.append("import ");
      this.getAccess().toString(s);
      s.append(".*;\n");
   }

   public TypeImportOnDemandDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public TypeImportOnDemandDecl(Access p0) {
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
      TypeDecl decl;
      if (this.getAccess() instanceof PackageAccess) {
         String packageName = ((PackageAccess)this.getAccess()).getPackage();
         decl = this.lookupType(packageName, name);
         if (decl != null && decl.accessibleFromPackage(this.packageName()) && decl.typeName().equals(packageName + "." + name)) {
            set = set.add(decl);
         }
      } else {
         Iterator iter = this.getAccess().type().memberTypes(name).iterator();

         while(iter.hasNext()) {
            decl = (TypeDecl)iter.next();
            if (decl.accessibleFromPackage(this.packageName()) && decl.typeName().equals(this.getAccess().typeName() + "." + name)) {
               set = set.add(decl);
            }
         }
      }

      return set;
   }

   public boolean isOnDemand() {
      ASTNode$State state = this.state();
      return true;
   }

   public TypeDecl lookupType(String packageName, String typeName) {
      ASTNode$State state = this.state();
      TypeDecl lookupType_String_String_value = this.getParent().Define_TypeDecl_lookupType(this, (ASTNode)null, packageName, typeName);
      return lookupType_String_String_value;
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getAccessNoTransform() ? NameType.PACKAGE_OR_TYPE_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
