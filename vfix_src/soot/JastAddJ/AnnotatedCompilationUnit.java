package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Iterator;
import soot.tagkit.Tag;

public class AnnotatedCompilationUnit extends CompilationUnit implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public AnnotatedCompilationUnit clone() throws CloneNotSupportedException {
      AnnotatedCompilationUnit node = (AnnotatedCompilationUnit)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public AnnotatedCompilationUnit copy() {
      try {
         AnnotatedCompilationUnit node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public AnnotatedCompilationUnit fullCopy() {
      AnnotatedCompilationUnit tree = this.copy();
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
      super.nameCheck();
      if (!this.relativeName().endsWith("package-info.java")) {
         this.error("package annotations should be in a file package-info.java");
      }

   }

   public void toString(StringBuffer s) {
      this.getModifiers().toString(s);
      super.toString(s);
   }

   public void jimplify1phase2() {
      super.jimplify1phase2();
      ArrayList c = new ArrayList();
      this.getModifiers().addAllAnnotations(c);

      Tag var3;
      for(Iterator iter = c.iterator(); iter.hasNext(); var3 = (Tag)iter.next()) {
      }

   }

   public AnnotatedCompilationUnit() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new List(), 0);
      this.setChild(new List(), 1);
   }

   public AnnotatedCompilationUnit(String p0, List<ImportDecl> p1, List<TypeDecl> p2, Modifiers p3) {
      this.setPackageDecl(p0);
      this.setChild(p1, 0);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   public AnnotatedCompilationUnit(Symbol p0, List<ImportDecl> p1, List<TypeDecl> p2, Modifiers p3) {
      this.setPackageDecl(p0);
      this.setChild(p1, 0);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   protected int numChildren() {
      return 3;
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

   public void setModifiers(Modifiers node) {
      this.setChild(node, 2);
   }

   public Modifiers getModifiers() {
      return (Modifiers)this.getChild(2);
   }

   public Modifiers getModifiersNoTransform() {
      return (Modifiers)this.getChildNoTransform(2);
   }

   public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
      return caller == this.getModifiersNoTransform() ? name.equals("PACKAGE") : this.getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
   }

   public String Define_String_hostPackage(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? this.packageName() : super.Define_String_hostPackage(caller, child);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
