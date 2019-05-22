package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CompilationUnit extends ASTNode<ASTNode> implements Cloneable {
   private String relativeName;
   private String pathName;
   private boolean fromSource;
   protected ArrayList errors = new ArrayList();
   protected ArrayList warnings = new ArrayList();
   protected Collection parseErrors = new ArrayList();
   public boolean isResolved = false;
   protected String tokenjava_lang_String_PackageDecl;
   public int PackageDeclstart;
   public int PackageDeclend;
   protected boolean packageName_computed = false;
   protected String packageName_value;
   protected Map lookupType_String_values;

   public void flushCache() {
      super.flushCache();
      this.packageName_computed = false;
      this.packageName_value = null;
      this.lookupType_String_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public CompilationUnit clone() throws CloneNotSupportedException {
      CompilationUnit node = (CompilationUnit)super.clone();
      node.packageName_computed = false;
      node.packageName_value = null;
      node.lookupType_String_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public CompilationUnit copy() {
      try {
         CompilationUnit node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public CompilationUnit fullCopy() {
      CompilationUnit tree = this.copy();
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

   public void setRelativeName(String name) {
      this.relativeName = name;
   }

   public void setPathName(String name) {
      this.pathName = name;
   }

   public void setFromSource(boolean value) {
      this.fromSource = value;
   }

   public Collection parseErrors() {
      return this.parseErrors;
   }

   public void addParseError(Problem msg) {
      this.parseErrors.add(msg);
   }

   public void errorCheck(Collection collection) {
      this.collectErrors();
      collection.addAll(this.errors);
   }

   public void errorCheck(Collection err, Collection warn) {
      this.collectErrors();
      err.addAll(this.errors);
      warn.addAll(this.warnings);
   }

   public void refined_NameCheck_CompilationUnit_nameCheck() {
      for(int i = 0; i < this.getNumImportDecl(); ++i) {
         ImportDecl decl = this.getImportDecl(i);
         if (decl instanceof SingleTypeImportDecl) {
            TypeDecl importedType = decl.getAccess().type();
            Iterator iter = this.localLookupType(importedType.name()).iterator();

            while(iter.hasNext()) {
               TypeDecl local = (TypeDecl)iter.next();
               if (local != importedType) {
                  this.error("imported type " + decl + " is conflicting with visible type");
               }
            }
         }
      }

   }

   public void toString(StringBuffer s) {
      try {
         if (!this.getPackageDecl().equals("")) {
            s.append("package " + this.getPackageDecl() + ";\n");
         }

         int i;
         for(i = 0; i < this.getNumImportDecl(); ++i) {
            this.getImportDecl(i).toString(s);
         }

         for(i = 0; i < this.getNumTypeDecl(); ++i) {
            this.getTypeDecl(i).toString(s);
            s.append("\n");
         }

      } catch (NullPointerException var3) {
         System.out.print("Error in compilation unit hosting " + this.getTypeDecl(0).typeName());
         throw var3;
      }
   }

   public void transformation() {
      if (this.fromSource()) {
         for(int i = 0; i < this.getNumTypeDecl(); ++i) {
            this.getTypeDecl(i).transformation();
         }
      }

   }

   public CompilationUnit() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
      this.setChild(new List(), 0);
      this.setChild(new List(), 1);
   }

   public CompilationUnit(String p0, List<ImportDecl> p1, List<TypeDecl> p2) {
      this.setPackageDecl(p0);
      this.setChild(p1, 0);
      this.setChild(p2, 1);
   }

   public CompilationUnit(Symbol p0, List<ImportDecl> p1, List<TypeDecl> p2) {
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

   public void nameCheck() {
      this.refined_NameCheck_CompilationUnit_nameCheck();

      for(int i = 0; i < this.getNumImportDecl(); ++i) {
         if (this.getImportDecl(i) instanceof SingleStaticImportDecl) {
            SingleStaticImportDecl decl = (SingleStaticImportDecl)this.getImportDecl(i);
            String name = decl.name();
            if (!decl.importedTypes(name).isEmpty()) {
               TypeDecl type = (TypeDecl)decl.importedTypes(name).iterator().next();
               if (this.localLookupType(name).contains(type)) {
                  decl.error(this.packageName() + "." + name + " is already defined in this compilation unit");
               }
            }
         }
      }

   }

   private SimpleSet refined_TypeScopePropagation_CompilationUnit_Child_lookupType_String(String name) {
      SimpleSet set = this.localLookupType(name);
      if (!set.isEmpty()) {
         return set;
      } else {
         set = this.importedTypes(name);
         if (!set.isEmpty()) {
            return set;
         } else {
            TypeDecl result = this.lookupType(this.packageName(), name);
            if (result != null && result.accessibleFromPackage(this.packageName())) {
               return SimpleSet.emptySet.add(result);
            } else {
               set = this.importedTypesOnDemand(name);
               if (!set.isEmpty()) {
                  return set;
               } else {
                  result = this.lookupType("@primitive", name);
                  if (result != null) {
                     return SimpleSet.emptySet.add(result);
                  } else {
                     result = this.lookupType("java.lang", name);
                     return result != null && result.accessibleFromPackage(this.packageName()) ? SimpleSet.emptySet.add(result) : this.lookupType(name);
                  }
               }
            }
         }
      }
   }

   public String relativeName() {
      ASTNode$State state = this.state();
      return this.relativeName;
   }

   public String pathName() {
      ASTNode$State state = this.state();
      return this.pathName;
   }

   public boolean fromSource() {
      ASTNode$State state = this.state();
      return this.fromSource;
   }

   public SimpleSet localLookupType(String name) {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumTypeDecl(); ++i) {
         if (this.getTypeDecl(i).name().equals(name)) {
            return SimpleSet.emptySet.add(this.getTypeDecl(i));
         }
      }

      return SimpleSet.emptySet;
   }

   public SimpleSet importedTypes(String name) {
      ASTNode$State state = this.state();
      SimpleSet set = SimpleSet.emptySet;

      for(int i = 0; i < this.getNumImportDecl(); ++i) {
         if (!this.getImportDecl(i).isOnDemand()) {
            for(Iterator iter = this.getImportDecl(i).importedTypes(name).iterator(); iter.hasNext(); set = set.add(iter.next())) {
            }
         }
      }

      return set;
   }

   public SimpleSet importedTypesOnDemand(String name) {
      ASTNode$State state = this.state();
      SimpleSet set = SimpleSet.emptySet;

      for(int i = 0; i < this.getNumImportDecl(); ++i) {
         if (this.getImportDecl(i).isOnDemand()) {
            for(Iterator iter = this.getImportDecl(i).importedTypes(name).iterator(); iter.hasNext(); set = set.add(iter.next())) {
            }
         }
      }

      return set;
   }

   public String dumpString() {
      ASTNode$State state = this.state();
      return this.getClass().getName() + " [" + this.getPackageDecl() + "]";
   }

   public String packageName() {
      if (this.packageName_computed) {
         return this.packageName_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.packageName_value = this.packageName_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.packageName_computed = true;
         }

         return this.packageName_value;
      }
   }

   private String packageName_compute() {
      return this.getPackageDecl();
   }

   public SimpleSet importedFields(String name) {
      ASTNode$State state = this.state();
      SimpleSet set = SimpleSet.emptySet;

      for(int i = 0; i < this.getNumImportDecl(); ++i) {
         if (!this.getImportDecl(i).isOnDemand()) {
            for(Iterator iter = this.getImportDecl(i).importedFields(name).iterator(); iter.hasNext(); set = set.add(iter.next())) {
            }
         }
      }

      return set;
   }

   public SimpleSet importedFieldsOnDemand(String name) {
      ASTNode$State state = this.state();
      SimpleSet set = SimpleSet.emptySet;

      for(int i = 0; i < this.getNumImportDecl(); ++i) {
         if (this.getImportDecl(i).isOnDemand()) {
            for(Iterator iter = this.getImportDecl(i).importedFields(name).iterator(); iter.hasNext(); set = set.add(iter.next())) {
            }
         }
      }

      return set;
   }

   public Collection importedMethods(String name) {
      ASTNode$State state = this.state();
      Collection list = new ArrayList();

      for(int i = 0; i < this.getNumImportDecl(); ++i) {
         if (!this.getImportDecl(i).isOnDemand()) {
            list.addAll(this.getImportDecl(i).importedMethods(name));
         }
      }

      return list;
   }

   public Collection importedMethodsOnDemand(String name) {
      ASTNode$State state = this.state();
      Collection list = new ArrayList();

      for(int i = 0; i < this.getNumImportDecl(); ++i) {
         if (this.getImportDecl(i).isOnDemand()) {
            list.addAll(this.getImportDecl(i).importedMethods(name));
         }
      }

      return list;
   }

   public TypeDecl lookupType(String packageName, String typeName) {
      ASTNode$State state = this.state();
      TypeDecl lookupType_String_String_value = this.getParent().Define_TypeDecl_lookupType(this, (ASTNode)null, packageName, typeName);
      return lookupType_String_String_value;
   }

   public SimpleSet lookupType(String name) {
      if (this.lookupType_String_values == null) {
         this.lookupType_String_values = new HashMap(4);
      }

      if (this.lookupType_String_values.containsKey(name)) {
         return (SimpleSet)this.lookupType_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet lookupType_String_value = this.getParent().Define_SimpleSet_lookupType(this, (ASTNode)null, name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.lookupType_String_values.put(name, lookupType_String_value);
         }

         return lookupType_String_value;
      }
   }

   public SimpleSet lookupVariable(String name) {
      ASTNode$State state = this.state();
      SimpleSet lookupVariable_String_value = this.getParent().Define_SimpleSet_lookupVariable(this, (ASTNode)null, name);
      return lookupVariable_String_value;
   }

   public Collection lookupMethod(String name) {
      ASTNode$State state = this.state();
      Collection lookupMethod_String_value = this.getParent().Define_Collection_lookupMethod(this, (ASTNode)null, name);
      return lookupMethod_String_value;
   }

   public CompilationUnit Define_CompilationUnit_compilationUnit(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this;
   }

   public boolean Define_boolean_isIncOrDec(ASTNode caller, ASTNode child) {
      if (caller == this.getTypeDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_isIncOrDec(this, caller);
      }
   }

   public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
      if (caller == this.getImportDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return !exceptionType.isUncheckedException();
      } else if (caller == this.getTypeDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return !exceptionType.isUncheckedException();
      } else {
         return this.getParent().Define_boolean_handlesException(this, caller, exceptionType);
      }
   }

   public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getImportDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.lookupType(name);
      } else {
         this.getIndexOfChild(caller);
         SimpleSet result = SimpleSet.emptySet;
         Iterator iter = this.refined_TypeScopePropagation_CompilationUnit_Child_lookupType_String(name).iterator();

         while(iter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl)iter.next();
            if (typeDecl instanceof ParTypeDecl) {
               result = result.add(((ParTypeDecl)typeDecl).genericDecl());
            } else {
               result = result.add(typeDecl);
            }
         }

         return result;
      }
   }

   public SimpleSet Define_SimpleSet_allImportedTypes(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getImportDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.importedTypes(name);
      } else {
         return this.getParent().Define_SimpleSet_allImportedTypes(this, caller, name);
      }
   }

   public String Define_String_packageName(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.packageName();
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      if (caller == this.getImportDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.PACKAGE_NAME;
      } else {
         return this.getParent().Define_NameType_nameType(this, caller);
      }
   }

   public TypeDecl Define_TypeDecl_enclosingType(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return null;
   }

   public boolean Define_boolean_isNestedType(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_isMemberType(ASTNode caller, ASTNode child) {
      if (caller == this.getTypeDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_isMemberType(this, caller);
      }
   }

   public boolean Define_boolean_isLocalClass(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public String Define_String_hostPackage(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.packageName();
   }

   public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
      if (caller == this.getImportDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return null;
      } else {
         return this.getParent().Define_TypeDecl_hostType(this, caller);
      }
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getTypeDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         SimpleSet set = this.importedFields(name);
         if (!set.isEmpty()) {
            return set;
         } else {
            set = this.importedFieldsOnDemand(name);
            return !set.isEmpty() ? set : this.lookupVariable(name);
         }
      } else {
         return this.getParent().Define_SimpleSet_lookupVariable(this, caller, name);
      }
   }

   public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getTypeDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         Collection list = this.importedMethods(name);
         if (!list.isEmpty()) {
            return list;
         } else {
            list = this.importedMethodsOnDemand(name);
            return !list.isEmpty() ? list : this.lookupMethod(name);
         }
      } else {
         return this.getParent().Define_Collection_lookupMethod(this, caller, name);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
