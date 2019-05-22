package soot.JastAddJ;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ImportDecl extends ASTNode<ASTNode> implements Cloneable {
   protected Map importedTypes_String_values;
   protected Map importedFields_String_values;
   protected Map importedMethods_String_values;

   public void flushCache() {
      super.flushCache();
      this.importedTypes_String_values = null;
      this.importedFields_String_values = null;
      this.importedMethods_String_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ImportDecl clone() throws CloneNotSupportedException {
      ImportDecl node = (ImportDecl)super.clone();
      node.importedTypes_String_values = null;
      node.importedFields_String_values = null;
      node.importedMethods_String_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ImportDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public ImportDecl(Access p0) {
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
      return SimpleSet.emptySet;
   }

   public boolean isOnDemand() {
      ASTNode$State state = this.state();
      return false;
   }

   public String typeName() {
      ASTNode$State state = this.state();
      Access a = this.getAccess().lastAccess();

      String name;
      Access pred;
      for(name = a.isTypeAccess() ? ((TypeAccess)a).nameWithPackage() : ""; a.hasPrevExpr() && a.prevExpr() instanceof Access; a = pred) {
         pred = (Access)a.prevExpr();
         if (pred.isTypeAccess()) {
            name = ((TypeAccess)pred).nameWithPackage() + "." + name;
         }
      }

      return name;
   }

   public SimpleSet importedFields(String name) {
      if (this.importedFields_String_values == null) {
         this.importedFields_String_values = new HashMap(4);
      }

      if (this.importedFields_String_values.containsKey(name)) {
         return (SimpleSet)this.importedFields_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet importedFields_String_value = this.importedFields_compute(name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.importedFields_String_values.put(name, importedFields_String_value);
         }

         return importedFields_String_value;
      }
   }

   private SimpleSet importedFields_compute(String name) {
      return SimpleSet.emptySet;
   }

   public Collection importedMethods(String name) {
      if (this.importedMethods_String_values == null) {
         this.importedMethods_String_values = new HashMap(4);
      }

      if (this.importedMethods_String_values.containsKey(name)) {
         return (Collection)this.importedMethods_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         Collection importedMethods_String_value = this.importedMethods_compute(name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.importedMethods_String_values.put(name, importedMethods_String_value);
         }

         return importedMethods_String_value;
      }
   }

   private Collection importedMethods_compute(String name) {
      return Collections.EMPTY_LIST;
   }

   public String packageName() {
      ASTNode$State state = this.state();
      String packageName_value = this.getParent().Define_String_packageName(this, (ASTNode)null);
      return packageName_value;
   }

   public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
      return caller == this.getAccessNoTransform() ? false : this.getParent().Define_boolean_isDest(this, caller);
   }

   public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
      return caller == this.getAccessNoTransform() ? true : this.getParent().Define_boolean_isSource(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
