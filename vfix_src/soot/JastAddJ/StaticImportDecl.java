package soot.JastAddJ;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public abstract class StaticImportDecl extends ImportDecl implements Cloneable {
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

   public StaticImportDecl clone() throws CloneNotSupportedException {
      StaticImportDecl node = (StaticImportDecl)super.clone();
      node.importedTypes_String_values = null;
      node.importedFields_String_values = null;
      node.importedMethods_String_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public StaticImportDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public StaticImportDecl(Access p0) {
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

   public abstract TypeDecl type();

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
      Iterator iter = this.type().memberTypes(name).iterator();

      while(iter.hasNext()) {
         TypeDecl decl = (TypeDecl)iter.next();
         if (decl.isStatic() && decl.accessibleFromPackage(this.packageName())) {
            set = set.add(decl);
         }
      }

      return set;
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
      SimpleSet set = SimpleSet.emptySet;
      Iterator iter = this.type().memberFields(name).iterator();

      while(true) {
         FieldDeclaration decl;
         do {
            do {
               if (!iter.hasNext()) {
                  return set;
               }

               decl = (FieldDeclaration)iter.next();
            } while(!decl.isStatic());
         } while(!decl.isPublic() && (decl.isPrivate() || !decl.hostType().topLevelType().packageName().equals(this.packageName())));

         set = set.add(decl);
      }
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
      Collection set = new HashSet();
      Iterator iter = this.type().memberMethods(name).iterator();

      while(true) {
         MethodDecl decl;
         do {
            do {
               if (!iter.hasNext()) {
                  return set;
               }

               decl = (MethodDecl)iter.next();
            } while(!decl.isStatic());
         } while(!decl.isPublic() && (decl.isPrivate() || !decl.hostType().topLevelType().packageName().equals(this.packageName())));

         set.add(decl);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
