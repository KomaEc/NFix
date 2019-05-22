package soot.JastAddJ;

import beaver.Symbol;

public class PrimitiveCompilationUnit extends CompilationUnit implements Cloneable {
   protected boolean typeBoolean_computed = false;
   protected TypeDecl typeBoolean_value;
   protected boolean typeByte_computed = false;
   protected TypeDecl typeByte_value;
   protected boolean typeShort_computed = false;
   protected TypeDecl typeShort_value;
   protected boolean typeChar_computed = false;
   protected TypeDecl typeChar_value;
   protected boolean typeInt_computed = false;
   protected TypeDecl typeInt_value;
   protected boolean typeLong_computed = false;
   protected TypeDecl typeLong_value;
   protected boolean typeFloat_computed = false;
   protected TypeDecl typeFloat_value;
   protected boolean typeDouble_computed = false;
   protected TypeDecl typeDouble_value;
   protected boolean typeVoid_computed = false;
   protected TypeDecl typeVoid_value;
   protected boolean typeNull_computed = false;
   protected TypeDecl typeNull_value;
   protected boolean unknownType_computed = false;
   protected TypeDecl unknownType_value;

   public void flushCache() {
      super.flushCache();
      this.typeBoolean_computed = false;
      this.typeBoolean_value = null;
      this.typeByte_computed = false;
      this.typeByte_value = null;
      this.typeShort_computed = false;
      this.typeShort_value = null;
      this.typeChar_computed = false;
      this.typeChar_value = null;
      this.typeInt_computed = false;
      this.typeInt_value = null;
      this.typeLong_computed = false;
      this.typeLong_value = null;
      this.typeFloat_computed = false;
      this.typeFloat_value = null;
      this.typeDouble_computed = false;
      this.typeDouble_value = null;
      this.typeVoid_computed = false;
      this.typeVoid_value = null;
      this.typeNull_computed = false;
      this.typeNull_value = null;
      this.unknownType_computed = false;
      this.unknownType_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public PrimitiveCompilationUnit clone() throws CloneNotSupportedException {
      PrimitiveCompilationUnit node = (PrimitiveCompilationUnit)super.clone();
      node.typeBoolean_computed = false;
      node.typeBoolean_value = null;
      node.typeByte_computed = false;
      node.typeByte_value = null;
      node.typeShort_computed = false;
      node.typeShort_value = null;
      node.typeChar_computed = false;
      node.typeChar_value = null;
      node.typeInt_computed = false;
      node.typeInt_value = null;
      node.typeLong_computed = false;
      node.typeLong_value = null;
      node.typeFloat_computed = false;
      node.typeFloat_value = null;
      node.typeDouble_computed = false;
      node.typeDouble_value = null;
      node.typeVoid_computed = false;
      node.typeVoid_value = null;
      node.typeNull_computed = false;
      node.typeNull_value = null;
      node.unknownType_computed = false;
      node.unknownType_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public PrimitiveCompilationUnit copy() {
      try {
         PrimitiveCompilationUnit node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public PrimitiveCompilationUnit fullCopy() {
      PrimitiveCompilationUnit tree = this.copy();
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

   public PrimitiveCompilationUnit() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
      this.setChild(new List(), 0);
      this.setChild(new List(), 1);
   }

   public PrimitiveCompilationUnit(String p0, List<ImportDecl> p1, List<TypeDecl> p2) {
      this.setPackageDecl(p0);
      this.setChild(p1, 0);
      this.setChild(p2, 1);
   }

   public PrimitiveCompilationUnit(Symbol p0, List<ImportDecl> p1, List<TypeDecl> p2) {
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

   public TypeDecl typeBoolean() {
      if (this.typeBoolean_computed) {
         return this.typeBoolean_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeBoolean_value = this.typeBoolean_compute();
         this.typeBoolean_value.setParent(this);
         this.typeBoolean_value.is$Final = true;
         this.typeBoolean_computed = true;
         return this.typeBoolean_value;
      }
   }

   private TypeDecl typeBoolean_compute() {
      BooleanType type = new BooleanType();
      type.setModifiers(new Modifiers((new List()).add(new Modifier("public"))));
      type.setID("boolean");
      type.setSuperClassAccess(this.unknownType().createQualifiedAccess());
      return type;
   }

   public TypeDecl typeByte() {
      if (this.typeByte_computed) {
         return this.typeByte_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeByte_value = this.typeByte_compute();
         this.typeByte_value.setParent(this);
         this.typeByte_value.is$Final = true;
         this.typeByte_computed = true;
         return this.typeByte_value;
      }
   }

   private TypeDecl typeByte_compute() {
      ByteType type = new ByteType();
      type.setModifiers(new Modifiers((new List()).add(new Modifier("public"))));
      type.setID("byte");
      type.setSuperClassAccess(this.typeShort().createQualifiedAccess());
      return type;
   }

   public TypeDecl typeShort() {
      if (this.typeShort_computed) {
         return this.typeShort_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeShort_value = this.typeShort_compute();
         this.typeShort_value.setParent(this);
         this.typeShort_value.is$Final = true;
         this.typeShort_computed = true;
         return this.typeShort_value;
      }
   }

   private TypeDecl typeShort_compute() {
      ShortType type = new ShortType();
      type.setModifiers(new Modifiers((new List()).add(new Modifier("public"))));
      type.setID("short");
      type.setSuperClassAccess(this.typeInt().createQualifiedAccess());
      return type;
   }

   public TypeDecl typeChar() {
      if (this.typeChar_computed) {
         return this.typeChar_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeChar_value = this.typeChar_compute();
         this.typeChar_value.setParent(this);
         this.typeChar_value.is$Final = true;
         this.typeChar_computed = true;
         return this.typeChar_value;
      }
   }

   private TypeDecl typeChar_compute() {
      CharType type = new CharType();
      type.setModifiers(new Modifiers((new List()).add(new Modifier("public"))));
      type.setID("char");
      type.setSuperClassAccess(this.typeInt().createQualifiedAccess());
      return type;
   }

   public TypeDecl typeInt() {
      if (this.typeInt_computed) {
         return this.typeInt_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeInt_value = this.typeInt_compute();
         this.typeInt_value.setParent(this);
         this.typeInt_value.is$Final = true;
         this.typeInt_computed = true;
         return this.typeInt_value;
      }
   }

   private TypeDecl typeInt_compute() {
      IntType type = new IntType();
      type.setModifiers(new Modifiers((new List()).add(new Modifier("public"))));
      type.setID("int");
      type.setSuperClassAccess(this.typeLong().createQualifiedAccess());
      return type;
   }

   public TypeDecl typeLong() {
      if (this.typeLong_computed) {
         return this.typeLong_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeLong_value = this.typeLong_compute();
         this.typeLong_value.setParent(this);
         this.typeLong_value.is$Final = true;
         this.typeLong_computed = true;
         return this.typeLong_value;
      }
   }

   private TypeDecl typeLong_compute() {
      LongType type = new LongType();
      type.setModifiers(new Modifiers((new List()).add(new Modifier("public"))));
      type.setID("long");
      type.setSuperClassAccess(this.typeFloat().createQualifiedAccess());
      return type;
   }

   public TypeDecl typeFloat() {
      if (this.typeFloat_computed) {
         return this.typeFloat_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeFloat_value = this.typeFloat_compute();
         this.typeFloat_value.setParent(this);
         this.typeFloat_value.is$Final = true;
         this.typeFloat_computed = true;
         return this.typeFloat_value;
      }
   }

   private TypeDecl typeFloat_compute() {
      FloatType type = new FloatType();
      type.setModifiers(new Modifiers((new List()).add(new Modifier("public"))));
      type.setID("float");
      type.setSuperClassAccess(this.typeDouble().createQualifiedAccess());
      return type;
   }

   public TypeDecl typeDouble() {
      if (this.typeDouble_computed) {
         return this.typeDouble_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeDouble_value = this.typeDouble_compute();
         this.typeDouble_value.setParent(this);
         this.typeDouble_value.is$Final = true;
         this.typeDouble_computed = true;
         return this.typeDouble_value;
      }
   }

   private TypeDecl typeDouble_compute() {
      DoubleType type = new DoubleType();
      type.setModifiers(new Modifiers((new List()).add(new Modifier("public"))));
      type.setID("double");
      type.setSuperClassAccess(this.unknownType().createQualifiedAccess());
      return type;
   }

   public TypeDecl typeVoid() {
      if (this.typeVoid_computed) {
         return this.typeVoid_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeVoid_value = this.typeVoid_compute();
         this.typeVoid_value.setParent(this);
         this.typeVoid_value.is$Final = true;
         this.typeVoid_computed = true;
         return this.typeVoid_value;
      }
   }

   private TypeDecl typeVoid_compute() {
      VoidType classDecl = new VoidType();
      classDecl.setModifiers(new Modifiers((new List()).add(new Modifier("public"))));
      classDecl.setID("void");
      return classDecl;
   }

   public TypeDecl typeNull() {
      if (this.typeNull_computed) {
         return this.typeNull_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeNull_value = this.typeNull_compute();
         this.typeNull_value.setParent(this);
         this.typeNull_value.is$Final = true;
         this.typeNull_computed = true;
         return this.typeNull_value;
      }
   }

   private TypeDecl typeNull_compute() {
      NullType classDecl = new NullType();
      classDecl.setModifiers(new Modifiers((new List()).add(new Modifier("public"))));
      classDecl.setID("null");
      return classDecl;
   }

   public TypeDecl unknownType() {
      if (this.unknownType_computed) {
         return this.unknownType_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.unknownType_value = this.unknownType_compute();
         this.unknownType_value.setParent(this);
         this.unknownType_value.is$Final = true;
         this.unknownType_computed = true;
         return this.unknownType_value;
      }
   }

   private TypeDecl unknownType_compute() {
      ClassDecl classDecl = new UnknownType();
      classDecl.setModifiers(new Modifiers((new List()).add(new Modifier("public"))));
      classDecl.setID("Unknown");
      MethodDecl methodDecl = new MethodDecl(new Modifiers((new List()).add(new Modifier("public"))), new PrimitiveTypeAccess("Unknown"), "unknown", new List(), new List(), new Opt());
      classDecl.addBodyDecl(methodDecl);
      FieldDeclaration fieldDecl = new FieldDeclaration(new Modifiers((new List()).add(new Modifier("public"))), new PrimitiveTypeAccess("Unknown"), "unknown", new Opt());
      classDecl.addBodyDecl(fieldDecl);
      ConstructorDecl constrDecl = new ConstructorDecl(new Modifiers((new List()).add(new Modifier("public"))), "Unknown", new List(), new List(), new Opt(), new Block());
      classDecl.addBodyDecl(constrDecl);
      return classDecl;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
