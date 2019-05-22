package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WildcardsCompilationUnit extends CompilationUnit implements Cloneable {
   protected boolean typeWildcard_computed = false;
   protected TypeDecl typeWildcard_value;
   protected Map lookupWildcardExtends_TypeDecl_values;
   protected List lookupWildcardExtends_TypeDecl_list;
   protected Map lookupWildcardSuper_TypeDecl_values;
   protected List lookupWildcardSuper_TypeDecl_list;
   protected Map lookupLUBType_Collection_values;
   protected List lookupLUBType_Collection_list;
   protected Map lookupGLBType_ArrayList_values;
   protected List lookupGLBType_ArrayList_list;

   public void flushCache() {
      super.flushCache();
      this.typeWildcard_computed = false;
      this.typeWildcard_value = null;
      this.lookupWildcardExtends_TypeDecl_values = null;
      this.lookupWildcardExtends_TypeDecl_list = null;
      this.lookupWildcardSuper_TypeDecl_values = null;
      this.lookupWildcardSuper_TypeDecl_list = null;
      this.lookupLUBType_Collection_values = null;
      this.lookupLUBType_Collection_list = null;
      this.lookupGLBType_ArrayList_values = null;
      this.lookupGLBType_ArrayList_list = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public WildcardsCompilationUnit clone() throws CloneNotSupportedException {
      WildcardsCompilationUnit node = (WildcardsCompilationUnit)super.clone();
      node.typeWildcard_computed = false;
      node.typeWildcard_value = null;
      node.lookupWildcardExtends_TypeDecl_values = null;
      node.lookupWildcardExtends_TypeDecl_list = null;
      node.lookupWildcardSuper_TypeDecl_values = null;
      node.lookupWildcardSuper_TypeDecl_list = null;
      node.lookupLUBType_Collection_values = null;
      node.lookupLUBType_Collection_list = null;
      node.lookupGLBType_ArrayList_values = null;
      node.lookupGLBType_ArrayList_list = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public WildcardsCompilationUnit copy() {
      try {
         WildcardsCompilationUnit node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public WildcardsCompilationUnit fullCopy() {
      WildcardsCompilationUnit tree = this.copy();
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

   public static LUBType createLUBType(Collection bounds) {
      List boundList = new List();
      StringBuffer name = new StringBuffer();
      Iterator iter = bounds.iterator();

      while(iter.hasNext()) {
         TypeDecl typeDecl = (TypeDecl)iter.next();
         boundList.add(typeDecl.createBoundAccess());
         name.append("& " + typeDecl.typeName());
      }

      LUBType decl = new LUBType(new Modifiers((new List()).add(new Modifier("public"))), name.toString(), new List(), boundList);
      return decl;
   }

   public WildcardsCompilationUnit() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
      this.setChild(new List(), 0);
      this.setChild(new List(), 1);
   }

   public WildcardsCompilationUnit(String p0, List<ImportDecl> p1, List<TypeDecl> p2) {
      this.setPackageDecl(p0);
      this.setChild(p1, 0);
      this.setChild(p2, 1);
   }

   public WildcardsCompilationUnit(Symbol p0, List<ImportDecl> p1, List<TypeDecl> p2) {
      this.setPackageDecl(p0);
      this.setChild(p1, 0);
      this.setChild(p2, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setPackageDecl(String value) {
      this.tokenjava_lang_String_PackageDecl = value;
   }

   public void setPackageDecl(Symbol symbol) {
      if (symbol.value != null && !(symbol.value instanceof String)) {
         throw new UnsupportedOperationException("setPackageDecl is only valid for String lexemes");
      } else {
         this.tokenjava_lang_String_PackageDecl = (String)symbol.value;
         this.PackageDeclstart = symbol.getStart();
         this.PackageDeclend = symbol.getEnd();
      }
   }

   public String getPackageDecl() {
      return this.tokenjava_lang_String_PackageDecl != null ? this.tokenjava_lang_String_PackageDecl : "";
   }

   public void setImportDeclList(List<ImportDecl> list) {
      this.setChild(list, 0);
   }

   public int getNumImportDecl() {
      return this.getImportDeclList().getNumChild();
   }

   public int getNumImportDeclNoTransform() {
      return this.getImportDeclListNoTransform().getNumChildNoTransform();
   }

   public ImportDecl getImportDecl(int i) {
      return (ImportDecl)this.getImportDeclList().getChild(i);
   }

   public void addImportDecl(ImportDecl node) {
      List<ImportDecl> list = this.parent != null && state != null ? this.getImportDeclList() : this.getImportDeclListNoTransform();
      list.addChild(node);
   }

   public void addImportDeclNoTransform(ImportDecl node) {
      List<ImportDecl> list = this.getImportDeclListNoTransform();
      list.addChild(node);
   }

   public void setImportDecl(ImportDecl node, int i) {
      List<ImportDecl> list = this.getImportDeclList();
      list.setChild(node, i);
   }

   public List<ImportDecl> getImportDecls() {
      return this.getImportDeclList();
   }

   public List<ImportDecl> getImportDeclsNoTransform() {
      return this.getImportDeclListNoTransform();
   }

   public List<ImportDecl> getImportDeclList() {
      List<ImportDecl> list = (List)this.getChild(0);
      list.getNumChild();
      return list;
   }

   public List<ImportDecl> getImportDeclListNoTransform() {
      return (List)this.getChildNoTransform(0);
   }

   public void setTypeDeclList(List<TypeDecl> list) {
      this.setChild(list, 1);
   }

   public int getNumTypeDecl() {
      return this.getTypeDeclList().getNumChild();
   }

   public int getNumTypeDeclNoTransform() {
      return this.getTypeDeclListNoTransform().getNumChildNoTransform();
   }

   public TypeDecl getTypeDecl(int i) {
      return (TypeDecl)this.getTypeDeclList().getChild(i);
   }

   public void addTypeDecl(TypeDecl node) {
      List<TypeDecl> list = this.parent != null && state != null ? this.getTypeDeclList() : this.getTypeDeclListNoTransform();
      list.addChild(node);
   }

   public void addTypeDeclNoTransform(TypeDecl node) {
      List<TypeDecl> list = this.getTypeDeclListNoTransform();
      list.addChild(node);
   }

   public void setTypeDecl(TypeDecl node, int i) {
      List<TypeDecl> list = this.getTypeDeclList();
      list.setChild(node, i);
   }

   public List<TypeDecl> getTypeDecls() {
      return this.getTypeDeclList();
   }

   public List<TypeDecl> getTypeDeclsNoTransform() {
      return this.getTypeDeclListNoTransform();
   }

   public List<TypeDecl> getTypeDeclList() {
      List<TypeDecl> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<TypeDecl> getTypeDeclListNoTransform() {
      return (List)this.getChildNoTransform(1);
   }

   public TypeDecl typeWildcard() {
      if (this.typeWildcard_computed) {
         return this.typeWildcard_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeWildcard_value = this.typeWildcard_compute();
         this.typeWildcard_value.setParent(this);
         this.typeWildcard_value.is$Final = true;
         this.typeWildcard_computed = true;
         return this.typeWildcard_value;
      }
   }

   private TypeDecl typeWildcard_compute() {
      TypeDecl decl = new WildcardType(new Modifiers((new List()).add(new Modifier("public"))), "?", new List());
      return decl;
   }

   public TypeDecl lookupWildcardExtends(TypeDecl bound) {
      if (this.lookupWildcardExtends_TypeDecl_values == null) {
         this.lookupWildcardExtends_TypeDecl_values = new HashMap(4);
      }

      if (this.lookupWildcardExtends_TypeDecl_values.containsKey(bound)) {
         return (TypeDecl)this.lookupWildcardExtends_TypeDecl_values.get(bound);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         TypeDecl lookupWildcardExtends_TypeDecl_value = this.lookupWildcardExtends_compute(bound);
         if (this.lookupWildcardExtends_TypeDecl_list == null) {
            this.lookupWildcardExtends_TypeDecl_list = new List();
            this.lookupWildcardExtends_TypeDecl_list.is$Final = true;
            this.lookupWildcardExtends_TypeDecl_list.setParent(this);
         }

         this.lookupWildcardExtends_TypeDecl_list.add(lookupWildcardExtends_TypeDecl_value);
         if (lookupWildcardExtends_TypeDecl_value != null) {
            lookupWildcardExtends_TypeDecl_value.is$Final = true;
         }

         this.lookupWildcardExtends_TypeDecl_values.put(bound, lookupWildcardExtends_TypeDecl_value);
         return lookupWildcardExtends_TypeDecl_value;
      }
   }

   private TypeDecl lookupWildcardExtends_compute(TypeDecl bound) {
      TypeDecl decl = new WildcardExtendsType(new Modifiers((new List()).add(new Modifier("public"))), "? extends " + bound.fullName(), new List(), bound.createBoundAccess());
      return decl;
   }

   public TypeDecl lookupWildcardSuper(TypeDecl bound) {
      if (this.lookupWildcardSuper_TypeDecl_values == null) {
         this.lookupWildcardSuper_TypeDecl_values = new HashMap(4);
      }

      if (this.lookupWildcardSuper_TypeDecl_values.containsKey(bound)) {
         return (TypeDecl)this.lookupWildcardSuper_TypeDecl_values.get(bound);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         TypeDecl lookupWildcardSuper_TypeDecl_value = this.lookupWildcardSuper_compute(bound);
         if (this.lookupWildcardSuper_TypeDecl_list == null) {
            this.lookupWildcardSuper_TypeDecl_list = new List();
            this.lookupWildcardSuper_TypeDecl_list.is$Final = true;
            this.lookupWildcardSuper_TypeDecl_list.setParent(this);
         }

         this.lookupWildcardSuper_TypeDecl_list.add(lookupWildcardSuper_TypeDecl_value);
         if (lookupWildcardSuper_TypeDecl_value != null) {
            lookupWildcardSuper_TypeDecl_value.is$Final = true;
         }

         this.lookupWildcardSuper_TypeDecl_values.put(bound, lookupWildcardSuper_TypeDecl_value);
         return lookupWildcardSuper_TypeDecl_value;
      }
   }

   private TypeDecl lookupWildcardSuper_compute(TypeDecl bound) {
      TypeDecl decl = new WildcardSuperType(new Modifiers((new List()).add(new Modifier("public"))), "? super " + bound.fullName(), new List(), bound.createBoundAccess());
      return decl;
   }

   public LUBType lookupLUBType(Collection bounds) {
      if (this.lookupLUBType_Collection_values == null) {
         this.lookupLUBType_Collection_values = new HashMap(4);
      }

      if (this.lookupLUBType_Collection_values.containsKey(bounds)) {
         return (LUBType)this.lookupLUBType_Collection_values.get(bounds);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         LUBType lookupLUBType_Collection_value = this.lookupLUBType_compute(bounds);
         if (this.lookupLUBType_Collection_list == null) {
            this.lookupLUBType_Collection_list = new List();
            this.lookupLUBType_Collection_list.is$Final = true;
            this.lookupLUBType_Collection_list.setParent(this);
         }

         this.lookupLUBType_Collection_list.add(lookupLUBType_Collection_value);
         if (lookupLUBType_Collection_value != null) {
            lookupLUBType_Collection_value.is$Final = true;
         }

         this.lookupLUBType_Collection_values.put(bounds, lookupLUBType_Collection_value);
         return lookupLUBType_Collection_value;
      }
   }

   private LUBType lookupLUBType_compute(Collection bounds) {
      return createLUBType(bounds);
   }

   public GLBType lookupGLBType(ArrayList bounds) {
      if (this.lookupGLBType_ArrayList_values == null) {
         this.lookupGLBType_ArrayList_values = new HashMap(4);
      }

      if (this.lookupGLBType_ArrayList_values.containsKey(bounds)) {
         return (GLBType)this.lookupGLBType_ArrayList_values.get(bounds);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         GLBType lookupGLBType_ArrayList_value = this.lookupGLBType_compute(bounds);
         if (this.lookupGLBType_ArrayList_list == null) {
            this.lookupGLBType_ArrayList_list = new List();
            this.lookupGLBType_ArrayList_list.is$Final = true;
            this.lookupGLBType_ArrayList_list.setParent(this);
         }

         this.lookupGLBType_ArrayList_list.add(lookupGLBType_ArrayList_value);
         if (lookupGLBType_ArrayList_value != null) {
            lookupGLBType_ArrayList_value.is$Final = true;
         }

         this.lookupGLBType_ArrayList_values.put(bounds, lookupGLBType_ArrayList_value);
         return lookupGLBType_ArrayList_value;
      }
   }

   private GLBType lookupGLBType_compute(ArrayList bounds) {
      List boundList = new List();
      StringBuffer name = new StringBuffer();
      Iterator iter = bounds.iterator();

      while(iter.hasNext()) {
         TypeDecl typeDecl = (TypeDecl)iter.next();
         boundList.add(typeDecl.createBoundAccess());
         name.append("& " + typeDecl.typeName());
      }

      GLBType decl = new GLBType(new Modifiers((new List()).add(new Modifier("public"))), name.toString(), new List(), boundList);
      return decl;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
