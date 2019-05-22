package soot.JastAddJ;

import beaver.Symbol;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.SootClass;
import soot.SootResolver;
import soot.tagkit.SourceFileTag;

public class InterfaceDecl extends ReferenceType implements Cloneable {
   private TypeDecl methodHolder = null;
   protected boolean methodsSignatureMap_computed = false;
   protected HashMap methodsSignatureMap_value;
   protected Map ancestorMethods_String_values;
   protected Map memberTypes_String_values;
   protected boolean memberFieldsMap_computed = false;
   protected HashMap memberFieldsMap_value;
   protected Map memberFields_String_values;
   protected boolean isStatic_computed = false;
   protected boolean isStatic_value;
   protected Map castingConversionTo_TypeDecl_values;
   protected Map instanceOf_TypeDecl_values;
   protected int isCircular_visited = -1;
   protected boolean isCircular_computed = false;
   protected boolean isCircular_initialized = false;
   protected boolean isCircular_value;
   protected boolean implementedInterfaces_computed = false;
   protected HashSet implementedInterfaces_value;
   protected Map subtype_TypeDecl_values;
   protected boolean sootClass_computed = false;
   protected SootClass sootClass_value;

   public void flushCache() {
      super.flushCache();
      this.methodsSignatureMap_computed = false;
      this.methodsSignatureMap_value = null;
      this.ancestorMethods_String_values = null;
      this.memberTypes_String_values = null;
      this.memberFieldsMap_computed = false;
      this.memberFieldsMap_value = null;
      this.memberFields_String_values = null;
      this.isStatic_computed = false;
      this.castingConversionTo_TypeDecl_values = null;
      this.instanceOf_TypeDecl_values = null;
      this.isCircular_visited = -1;
      this.isCircular_computed = false;
      this.isCircular_initialized = false;
      this.implementedInterfaces_computed = false;
      this.implementedInterfaces_value = null;
      this.subtype_TypeDecl_values = null;
      this.sootClass_computed = false;
      this.sootClass_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public InterfaceDecl clone() throws CloneNotSupportedException {
      InterfaceDecl node = (InterfaceDecl)super.clone();
      node.methodsSignatureMap_computed = false;
      node.methodsSignatureMap_value = null;
      node.ancestorMethods_String_values = null;
      node.memberTypes_String_values = null;
      node.memberFieldsMap_computed = false;
      node.memberFieldsMap_value = null;
      node.memberFields_String_values = null;
      node.isStatic_computed = false;
      node.castingConversionTo_TypeDecl_values = null;
      node.instanceOf_TypeDecl_values = null;
      node.isCircular_visited = -1;
      node.isCircular_computed = false;
      node.isCircular_initialized = false;
      node.implementedInterfaces_computed = false;
      node.implementedInterfaces_value = null;
      node.subtype_TypeDecl_values = null;
      node.sootClass_computed = false;
      node.sootClass_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public InterfaceDecl copy() {
      try {
         InterfaceDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public InterfaceDecl fullCopy() {
      InterfaceDecl tree = this.copy();
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

   public void accessControl() {
      super.accessControl();
      if (!this.isCircular()) {
         HashSet set = new HashSet();

         for(int i = 0; i < this.getNumSuperInterfaceId(); ++i) {
            TypeDecl decl = this.getSuperInterfaceId(i).type();
            if (!decl.isInterfaceDecl() && !decl.isUnknown()) {
               this.error("interface " + this.fullName() + " tries to extend non interface type " + decl.fullName());
            }

            if (!decl.isCircular() && !decl.accessibleFrom(this)) {
               this.error("interface " + this.fullName() + " can not extend non accessible type " + decl.fullName());
            }

            if (set.contains(decl)) {
               this.error("extended interface " + decl.fullName() + " mentionened multiple times in extends clause");
            }

            set.add(decl);
         }
      }

   }

   public void checkModifiers() {
      super.checkModifiers();
   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      this.getModifiers().toString(s);
      s.append("interface " + this.name());
      if (this.getNumSuperInterfaceId() > 0) {
         s.append(" extends ");
         this.getSuperInterfaceId(0).toString(s);

         for(int i = 1; i < this.getNumSuperInterfaceId(); ++i) {
            s.append(", ");
            this.getSuperInterfaceId(i).toString(s);
         }
      }

      this.ppBodyDecls(s);
   }

   public Iterator superinterfacesIterator() {
      return new Iterator() {
         private int index = 0;
         private TypeDecl current = null;

         public boolean hasNext() {
            this.computeNextCurrent();
            return this.current != null;
         }

         public Object next() {
            return this.current;
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }

         private void computeNextCurrent() {
            this.current = null;
            if (!InterfaceDecl.this.isCircular()) {
               TypeDecl typeDecl;
               do {
                  if (this.index >= InterfaceDecl.this.getNumSuperInterfaceId()) {
                     return;
                  }

                  typeDecl = InterfaceDecl.this.getSuperInterfaceId(this.index++).type();
               } while(typeDecl.isCircular() || !typeDecl.isInterfaceDecl());

               this.current = typeDecl;
            }
         }
      };
   }

   public void nameCheck() {
      super.nameCheck();
      if (this.isCircular()) {
         this.error("circular inheritance dependency in " + this.typeName());
      } else {
         for(int i = 0; i < this.getNumSuperInterfaceId(); ++i) {
            TypeDecl typeDecl = this.getSuperInterfaceId(i).type();
            if (typeDecl.isCircular()) {
               this.error("circular inheritance dependency in " + this.typeName());
            }
         }
      }

      Iterator iter = this.methodsSignatureMap().values().iterator();

      while(true) {
         SimpleSet set;
         do {
            if (!iter.hasNext()) {
               return;
            }

            set = (SimpleSet)iter.next();
         } while(set.size() <= 1);

         Iterator i2 = set.iterator();
         MethodDecl m = (MethodDecl)i2.next();

         while(i2.hasNext()) {
            MethodDecl n = (MethodDecl)i2.next();
            if (!n.mayOverrideReturn(m) && !m.mayOverrideReturn(n)) {
               this.error("multiply inherited methods with the same signature must have the same return type");
            }
         }
      }
   }

   public TypeDecl makeGeneric(Signatures.ClassSignature s) {
      if (s.hasFormalTypeParameters()) {
         ASTNode node = this.getParent();
         int index = node.getIndexOfChild(this);
         node.setChild(new GenericInterfaceDecl(this.getModifiersNoTransform(), this.getID(), s.hasSuperinterfaceSignature() ? s.superinterfaceSignature() : this.getSuperInterfaceIdListNoTransform(), this.getBodyDeclListNoTransform(), s.typeParameters()), index);
         return (TypeDecl)node.getChildNoTransform(index);
      } else {
         if (s.hasSuperinterfaceSignature()) {
            this.setSuperInterfaceIdList(s.superinterfaceSignature());
         }

         return this;
      }
   }

   public InterfaceDecl substitutedInterfaceDecl(Parameterization parTypeDecl) {
      InterfaceDecl c = new InterfaceDeclSubstituted(this.getModifiers().fullCopy(), this.getID(), this.getSuperInterfaceIdList().substitute(parTypeDecl), this);
      return c;
   }

   public FieldDeclaration createStaticClassField(String name) {
      return this.methodHolder().createStaticClassField(name);
   }

   public MethodDecl createStaticClassMethod() {
      return this.methodHolder().createStaticClassMethod();
   }

   public TypeDecl methodHolder() {
      if (this.methodHolder != null) {
         return this.methodHolder;
      } else {
         String name = "$" + this.nextAnonymousIndex();
         ClassDecl c = this.addMemberClass(new ClassDecl(new Modifiers(new List()), name, new Opt(), new List(), new List()));
         this.methodHolder = c;
         return c;
      }
   }

   public void jimplify1phase2() {
      SootClass sc = this.getSootClassDecl();
      sc.setResolvingLevel(0);
      sc.setModifiers(this.sootTypeModifiers());
      sc.setApplicationClass();
      SourceFileTag st = new SourceFileTag(this.sourceNameWithoutPath());
      st.setAbsolutePath(this.compilationUnit().pathName());
      sc.addTag(st);
      sc.setSuperclass(this.typeObject().getSootClassDecl());
      Iterator iter = this.superinterfacesIterator();

      while(iter.hasNext()) {
         TypeDecl typeDecl = (TypeDecl)iter.next();
         if (typeDecl != this.typeObject() && !sc.implementsInterface(typeDecl.getSootClassDecl().getName())) {
            sc.addInterface(typeDecl.getSootClassDecl());
         }
      }

      if (this.isNestedType()) {
         sc.setOuterClass(this.enclosingType().getSootClassDecl());
      }

      sc.setResolvingLevel(1);
      super.jimplify1phase2();
      sc.setResolvingLevel(2);
   }

   public InterfaceDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new List(), 1);
      this.setChild(new List(), 2);
   }

   public InterfaceDecl(Modifiers p0, String p1, List<Access> p2, List<BodyDecl> p3) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   public InterfaceDecl(Modifiers p0, Symbol p1, List<Access> p2, List<BodyDecl> p3) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   protected int numChildren() {
      return 3;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setModifiers(Modifiers node) {
      this.setChild(node, 0);
   }

   public Modifiers getModifiers() {
      return (Modifiers)this.getChild(0);
   }

   public Modifiers getModifiersNoTransform() {
      return (Modifiers)this.getChildNoTransform(0);
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

   public void setSuperInterfaceIdList(List<Access> list) {
      this.setChild(list, 1);
   }

   public int getNumSuperInterfaceId() {
      return this.getSuperInterfaceIdList().getNumChild();
   }

   public int getNumSuperInterfaceIdNoTransform() {
      return this.getSuperInterfaceIdListNoTransform().getNumChildNoTransform();
   }

   public Access getSuperInterfaceId(int i) {
      return (Access)this.getSuperInterfaceIdList().getChild(i);
   }

   public void addSuperInterfaceId(Access node) {
      List<Access> list = this.parent != null && state != null ? this.getSuperInterfaceIdList() : this.getSuperInterfaceIdListNoTransform();
      list.addChild(node);
   }

   public void addSuperInterfaceIdNoTransform(Access node) {
      List<Access> list = this.getSuperInterfaceIdListNoTransform();
      list.addChild(node);
   }

   public void setSuperInterfaceId(Access node, int i) {
      List<Access> list = this.getSuperInterfaceIdList();
      list.setChild(node, i);
   }

   public List<Access> getSuperInterfaceIds() {
      return this.getSuperInterfaceIdList();
   }

   public List<Access> getSuperInterfaceIdsNoTransform() {
      return this.getSuperInterfaceIdListNoTransform();
   }

   public List<Access> getSuperInterfaceIdList() {
      List<Access> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<Access> getSuperInterfaceIdListNoTransform() {
      return (List)this.getChildNoTransform(1);
   }

   public void setBodyDeclList(List<BodyDecl> list) {
      this.setChild(list, 2);
   }

   public int getNumBodyDecl() {
      return this.getBodyDeclList().getNumChild();
   }

   public int getNumBodyDeclNoTransform() {
      return this.getBodyDeclListNoTransform().getNumChildNoTransform();
   }

   public BodyDecl getBodyDecl(int i) {
      return (BodyDecl)this.getBodyDeclList().getChild(i);
   }

   public void addBodyDecl(BodyDecl node) {
      List<BodyDecl> list = this.parent != null && state != null ? this.getBodyDeclList() : this.getBodyDeclListNoTransform();
      list.addChild(node);
   }

   public void addBodyDeclNoTransform(BodyDecl node) {
      List<BodyDecl> list = this.getBodyDeclListNoTransform();
      list.addChild(node);
   }

   public void setBodyDecl(BodyDecl node, int i) {
      List<BodyDecl> list = this.getBodyDeclList();
      list.setChild(node, i);
   }

   public List<BodyDecl> getBodyDecls() {
      return this.getBodyDeclList();
   }

   public List<BodyDecl> getBodyDeclsNoTransform() {
      return this.getBodyDeclListNoTransform();
   }

   public List<BodyDecl> getBodyDeclList() {
      List<BodyDecl> list = (List)this.getChild(2);
      list.getNumChild();
      return list;
   }

   public List<BodyDecl> getBodyDeclListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   private boolean refined_Generics_InterfaceDecl_castingConversionTo_TypeDecl(TypeDecl type) {
      if (type.isArrayDecl()) {
         return type.instanceOf(this);
      } else {
         return type.isReferenceType() && !type.isFinal() ? true : type.instanceOf(this);
      }
   }

   public Collection lookupSuperConstructor() {
      ASTNode$State state = this.state();
      return this.typeObject().constructors();
   }

   public HashMap methodsSignatureMap() {
      if (this.methodsSignatureMap_computed) {
         return this.methodsSignatureMap_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.methodsSignatureMap_value = this.methodsSignatureMap_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.methodsSignatureMap_computed = true;
         }

         return this.methodsSignatureMap_value;
      }
   }

   private HashMap methodsSignatureMap_compute() {
      HashMap map = new HashMap(this.localMethodsSignatureMap());
      Iterator iter = this.superinterfacesIterator();

      label54:
      while(iter.hasNext()) {
         TypeDecl typeDecl = (TypeDecl)iter.next();
         Iterator iter = typeDecl.methodsIterator();

         while(true) {
            MethodDecl m;
            do {
               do {
                  do {
                     do {
                        if (!iter.hasNext()) {
                           continue label54;
                        }

                        m = (MethodDecl)iter.next();
                     } while(m.isPrivate());
                  } while(!m.accessibleFrom(this));
               } while(this.localMethodsSignatureMap().containsKey(m.signature()));
            } while(m instanceof MethodDeclSubstituted && this.localMethodsSignatureMap().containsKey(m.sourceMethodDecl().signature()));

            putSimpleSetElement(map, m.signature(), m);
         }
      }

      iter = this.typeObject().methodsIterator();

      while(iter.hasNext()) {
         MethodDecl m = (MethodDecl)iter.next();
         if (m.isPublic() && !map.containsKey(m.signature())) {
            putSimpleSetElement(map, m.signature(), m);
         }
      }

      return map;
   }

   public SimpleSet ancestorMethods(String signature) {
      if (this.ancestorMethods_String_values == null) {
         this.ancestorMethods_String_values = new HashMap(4);
      }

      if (this.ancestorMethods_String_values.containsKey(signature)) {
         return (SimpleSet)this.ancestorMethods_String_values.get(signature);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet ancestorMethods_String_value = this.ancestorMethods_compute(signature);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.ancestorMethods_String_values.put(signature, ancestorMethods_String_value);
         }

         return ancestorMethods_String_value;
      }
   }

   private SimpleSet ancestorMethods_compute(String signature) {
      SimpleSet set = SimpleSet.emptySet;
      Iterator iter = this.superinterfacesIterator();

      while(iter.hasNext()) {
         TypeDecl typeDecl = (TypeDecl)iter.next();

         MethodDecl m;
         for(Iterator iter = typeDecl.methodsSignature(signature).iterator(); iter.hasNext(); set = set.add(m)) {
            m = (MethodDecl)iter.next();
         }
      }

      if (!this.superinterfacesIterator().hasNext()) {
         iter = this.typeObject().methodsSignature(signature).iterator();

         while(iter.hasNext()) {
            MethodDecl m = (MethodDecl)iter.next();
            if (m.isPublic()) {
               set = set.add(m);
            }
         }
      }

      return set;
   }

   public SimpleSet memberTypes(String name) {
      if (this.memberTypes_String_values == null) {
         this.memberTypes_String_values = new HashMap(4);
      }

      if (this.memberTypes_String_values.containsKey(name)) {
         return (SimpleSet)this.memberTypes_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet memberTypes_String_value = this.memberTypes_compute(name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.memberTypes_String_values.put(name, memberTypes_String_value);
         }

         return memberTypes_String_value;
      }
   }

   private SimpleSet memberTypes_compute(String name) {
      SimpleSet set = this.localTypeDecls(name);
      if (!set.isEmpty()) {
         return set;
      } else {
         Iterator outerIter = this.superinterfacesIterator();

         while(outerIter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl)outerIter.next();
            Iterator iter = typeDecl.memberTypes(name).iterator();

            while(iter.hasNext()) {
               TypeDecl decl = (TypeDecl)iter.next();
               if (!decl.isPrivate()) {
                  set = set.add(decl);
               }
            }
         }

         return set;
      }
   }

   public HashMap memberFieldsMap() {
      if (this.memberFieldsMap_computed) {
         return this.memberFieldsMap_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.memberFieldsMap_value = this.memberFieldsMap_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.memberFieldsMap_computed = true;
         }

         return this.memberFieldsMap_value;
      }
   }

   private HashMap memberFieldsMap_compute() {
      HashMap map = new HashMap(this.localFieldsMap());
      Iterator outerIter = this.superinterfacesIterator();

      while(outerIter.hasNext()) {
         TypeDecl typeDecl = (TypeDecl)outerIter.next();
         Iterator iter = typeDecl.fieldsIterator();

         while(iter.hasNext()) {
            FieldDeclaration f = (FieldDeclaration)iter.next();
            if (f.accessibleFrom(this) && !f.isPrivate() && !this.localFieldsMap().containsKey(f.name())) {
               putSimpleSetElement(map, f.name(), f);
            }
         }
      }

      return map;
   }

   public SimpleSet memberFields(String name) {
      if (this.memberFields_String_values == null) {
         this.memberFields_String_values = new HashMap(4);
      }

      if (this.memberFields_String_values.containsKey(name)) {
         return (SimpleSet)this.memberFields_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet memberFields_String_value = this.memberFields_compute(name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.memberFields_String_values.put(name, memberFields_String_value);
         }

         return memberFields_String_value;
      }
   }

   private SimpleSet memberFields_compute(String name) {
      SimpleSet fields = this.localFields(name);
      if (!fields.isEmpty()) {
         return fields;
      } else {
         Iterator outerIter = this.superinterfacesIterator();

         while(outerIter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl)outerIter.next();
            Iterator iter = typeDecl.memberFields(name).iterator();

            while(iter.hasNext()) {
               FieldDeclaration f = (FieldDeclaration)iter.next();
               if (f.accessibleFrom(this) && !f.isPrivate()) {
                  fields = fields.add(f);
               }
            }
         }

         return fields;
      }
   }

   public boolean isAbstract() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isStatic() {
      if (this.isStatic_computed) {
         return this.isStatic_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isStatic_value = this.isStatic_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isStatic_computed = true;
         }

         return this.isStatic_value;
      }
   }

   private boolean isStatic_compute() {
      return this.getModifiers().isStatic() || this.isMemberType();
   }

   public boolean castingConversionTo(TypeDecl type) {
      if (this.castingConversionTo_TypeDecl_values == null) {
         this.castingConversionTo_TypeDecl_values = new HashMap(4);
      }

      if (this.castingConversionTo_TypeDecl_values.containsKey(type)) {
         return (Boolean)this.castingConversionTo_TypeDecl_values.get(type);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean castingConversionTo_TypeDecl_value = this.castingConversionTo_compute(type);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.castingConversionTo_TypeDecl_values.put(type, castingConversionTo_TypeDecl_value);
         }

         return castingConversionTo_TypeDecl_value;
      }
   }

   private boolean castingConversionTo_compute(TypeDecl type) {
      if (this.refined_Generics_InterfaceDecl_castingConversionTo_TypeDecl(type)) {
         return true;
      } else {
         boolean canUnboxThis = !this.unboxed().isUnknown();
         boolean canUnboxType = !type.unboxed().isUnknown();
         return canUnboxThis && !canUnboxType ? this.unboxed().wideningConversionTo(type) : false;
      }
   }

   public boolean isInterfaceDecl() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean instanceOf(TypeDecl type) {
      if (this.instanceOf_TypeDecl_values == null) {
         this.instanceOf_TypeDecl_values = new HashMap(4);
      }

      if (this.instanceOf_TypeDecl_values.containsKey(type)) {
         return (Boolean)this.instanceOf_TypeDecl_values.get(type);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean instanceOf_TypeDecl_value = this.instanceOf_compute(type);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.instanceOf_TypeDecl_values.put(type, instanceOf_TypeDecl_value);
         }

         return instanceOf_TypeDecl_value;
      }
   }

   private boolean instanceOf_compute(TypeDecl type) {
      return this.subtype(type);
   }

   public boolean isSupertypeOfClassDecl(ClassDecl type) {
      ASTNode$State state = this.state();
      if (super.isSupertypeOfClassDecl(type)) {
         return true;
      } else {
         Iterator iter = type.interfacesIterator();

         while(iter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl)iter.next();
            if (typeDecl.instanceOf(this)) {
               return true;
            }
         }

         return type.hasSuperclass() && type.superclass() != null && type.superclass().instanceOf(this);
      }
   }

   public boolean isSupertypeOfInterfaceDecl(InterfaceDecl type) {
      ASTNode$State state = this.state();
      if (super.isSupertypeOfInterfaceDecl(type)) {
         return true;
      } else {
         Iterator iter = type.superinterfacesIterator();

         TypeDecl superinterface;
         do {
            if (!iter.hasNext()) {
               return false;
            }

            superinterface = (TypeDecl)iter.next();
         } while(!superinterface.instanceOf(this));

         return true;
      }
   }

   public boolean isSupertypeOfArrayDecl(ArrayDecl type) {
      ASTNode$State state = this.state();
      if (super.isSupertypeOfArrayDecl(type)) {
         return true;
      } else {
         Iterator iter = type.interfacesIterator();

         TypeDecl typeDecl;
         do {
            if (!iter.hasNext()) {
               return false;
            }

            typeDecl = (TypeDecl)iter.next();
         } while(!typeDecl.instanceOf(this));

         return true;
      }
   }

   public boolean isCircular() {
      if (this.isCircular_computed) {
         return this.isCircular_value;
      } else {
         ASTNode$State state = this.state();
         if (!this.isCircular_initialized) {
            this.isCircular_initialized = true;
            this.isCircular_value = true;
         }

         if (state.IN_CIRCLE) {
            if (this.isCircular_visited != state.CIRCLE_INDEX) {
               this.isCircular_visited = state.CIRCLE_INDEX;
               if (state.RESET_CYCLE) {
                  this.isCircular_computed = false;
                  this.isCircular_initialized = false;
                  this.isCircular_visited = -1;
                  return this.isCircular_value;
               } else {
                  boolean new_isCircular_value = this.isCircular_compute();
                  if (new_isCircular_value != this.isCircular_value) {
                     state.CHANGE = true;
                  }

                  this.isCircular_value = new_isCircular_value;
                  return this.isCircular_value;
               }
            } else {
               return this.isCircular_value;
            }
         } else {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = this.is$Final();

            do {
               this.isCircular_visited = state.CIRCLE_INDEX;
               state.CHANGE = false;
               boolean new_isCircular_value = this.isCircular_compute();
               if (new_isCircular_value != this.isCircular_value) {
                  state.CHANGE = true;
               }

               this.isCircular_value = new_isCircular_value;
               ++state.CIRCLE_INDEX;
            } while(state.CHANGE);

            if (isFinal && num == this.state().boundariesCrossed) {
               this.isCircular_computed = true;
            } else {
               state.RESET_CYCLE = true;
               this.isCircular_compute();
               state.RESET_CYCLE = false;
               this.isCircular_computed = false;
               this.isCircular_initialized = false;
            }

            state.IN_CIRCLE = false;
            return this.isCircular_value;
         }
      }
   }

   private boolean isCircular_compute() {
      for(int i = 0; i < this.getNumSuperInterfaceId(); ++i) {
         for(Access a = this.getSuperInterfaceId(i).lastAccess(); a != null; a = a.isQualified() && a.qualifier().isTypeAccess() ? (Access)a.qualifier() : null) {
            if (a.type().isCircular()) {
               return true;
            }
         }
      }

      return false;
   }

   public HashSet implementedInterfaces() {
      if (this.implementedInterfaces_computed) {
         return this.implementedInterfaces_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.implementedInterfaces_value = this.implementedInterfaces_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.implementedInterfaces_computed = true;
         }

         return this.implementedInterfaces_value;
      }
   }

   private HashSet implementedInterfaces_compute() {
      HashSet set = new HashSet();
      set.addAll(this.typeObject().implementedInterfaces());
      Iterator iter = this.superinterfacesIterator();

      while(iter.hasNext()) {
         InterfaceDecl decl = (InterfaceDecl)iter.next();
         set.add(decl);
         set.addAll(decl.implementedInterfaces());
      }

      return set;
   }

   public boolean subtype(TypeDecl type) {
      if (this.subtype_TypeDecl_values == null) {
         this.subtype_TypeDecl_values = new HashMap(4);
      }

      ASTNode$State.CircularValue _value;
      if (this.subtype_TypeDecl_values.containsKey(type)) {
         Object _o = this.subtype_TypeDecl_values.get(type);
         if (!(_o instanceof ASTNode$State.CircularValue)) {
            return (Boolean)_o;
         }

         _value = (ASTNode$State.CircularValue)_o;
      } else {
         _value = new ASTNode$State.CircularValue();
         this.subtype_TypeDecl_values.put(type, _value);
         _value.value = true;
      }

      ASTNode$State state = this.state();
      if (state.IN_CIRCLE) {
         if (!(new Integer(state.CIRCLE_INDEX)).equals(_value.visited)) {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            boolean new_subtype_TypeDecl_value = this.subtype_compute(type);
            if (state.RESET_CYCLE) {
               this.subtype_TypeDecl_values.remove(type);
            } else if (new_subtype_TypeDecl_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_subtype_TypeDecl_value;
            }

            return new_subtype_TypeDecl_value;
         } else {
            return (Boolean)_value.value;
         }
      } else {
         state.IN_CIRCLE = true;
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();

         boolean new_subtype_TypeDecl_value;
         do {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            state.CHANGE = false;
            new_subtype_TypeDecl_value = this.subtype_compute(type);
            if (new_subtype_TypeDecl_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_subtype_TypeDecl_value;
            }

            ++state.CIRCLE_INDEX;
         } while(state.CHANGE);

         if (isFinal && num == this.state().boundariesCrossed) {
            this.subtype_TypeDecl_values.put(type, new_subtype_TypeDecl_value);
         } else {
            this.subtype_TypeDecl_values.remove(type);
            state.RESET_CYCLE = true;
            this.subtype_compute(type);
            state.RESET_CYCLE = false;
         }

         state.IN_CIRCLE = false;
         return new_subtype_TypeDecl_value;
      }
   }

   private boolean subtype_compute(TypeDecl type) {
      return type.supertypeInterfaceDecl(this);
   }

   public boolean supertypeClassDecl(ClassDecl type) {
      ASTNode$State state = this.state();
      if (super.supertypeClassDecl(type)) {
         return true;
      } else {
         Iterator iter = type.interfacesIterator();

         while(iter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl)iter.next();
            if (typeDecl.subtype(this)) {
               return true;
            }
         }

         return type.hasSuperclass() && type.superclass() != null && type.superclass().subtype(this);
      }
   }

   public boolean supertypeInterfaceDecl(InterfaceDecl type) {
      ASTNode$State state = this.state();
      if (super.supertypeInterfaceDecl(type)) {
         return true;
      } else {
         Iterator iter = type.superinterfacesIterator();

         TypeDecl superinterface;
         do {
            if (!iter.hasNext()) {
               return false;
            }

            superinterface = (TypeDecl)iter.next();
         } while(!superinterface.subtype(this));

         return true;
      }
   }

   public boolean supertypeArrayDecl(ArrayDecl type) {
      ASTNode$State state = this.state();
      if (super.supertypeArrayDecl(type)) {
         return true;
      } else {
         Iterator iter = type.interfacesIterator();

         TypeDecl typeDecl;
         do {
            if (!iter.hasNext()) {
               return false;
            }

            typeDecl = (TypeDecl)iter.next();
         } while(!typeDecl.subtype(this));

         return true;
      }
   }

   public SootClass sootClass() {
      if (this.sootClass_computed) {
         return this.sootClass_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.sootClass_value = this.sootClass_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.sootClass_computed = true;
         }

         return this.sootClass_value;
      }
   }

   private SootClass sootClass_compute() {
      if (this.options().verbose()) {
         System.out.println("Creating from source " + this.jvmName());
      }

      SootClass sc = SootResolver.v().makeClassRef(this.jvmName());
      sc.setModifiers(this.sootTypeModifiers());
      return sc;
   }

   public int sootTypeModifiers() {
      ASTNode$State state = this.state();
      return super.sootTypeModifiers() | 512;
   }

   public String typeDescriptor() {
      ASTNode$State state = this.state();
      return "L" + this.jvmName().replace('.', '/') + ";";
   }

   public SimpleSet bridgeCandidates(String signature) {
      ASTNode$State state = this.state();
      return this.ancestorMethods(signature);
   }

   public MethodDecl unknownMethod() {
      ASTNode$State state = this.state();
      MethodDecl unknownMethod_value = this.getParent().Define_MethodDecl_unknownMethod(this, (ASTNode)null);
      return unknownMethod_value;
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      if (caller == this.getSuperInterfaceIdListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.TYPE_NAME;
      } else {
         return super.Define_NameType_nameType(caller, child);
      }
   }

   public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
      if (caller == this.getSuperInterfaceIdListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.hostType();
      } else {
         return super.Define_TypeDecl_hostType(caller, child);
      }
   }

   public boolean Define_boolean_withinSuppressWarnings(ASTNode caller, ASTNode child, String s) {
      if (caller != this.getSuperInterfaceIdListNoTransform()) {
         return super.Define_boolean_withinSuppressWarnings(caller, child, s);
      } else {
         caller.getIndexOfChild(child);
         return this.hasAnnotationSuppressWarnings(s) || this.withinSuppressWarnings(s);
      }
   }

   public boolean Define_boolean_withinDeprecatedAnnotation(ASTNode caller, ASTNode child) {
      if (caller != this.getSuperInterfaceIdListNoTransform()) {
         return super.Define_boolean_withinDeprecatedAnnotation(caller, child);
      } else {
         caller.getIndexOfChild(child);
         return this.isDeprecated() || this.withinDeprecatedAnnotation();
      }
   }

   public boolean Define_boolean_inExtendsOrImplements(ASTNode caller, ASTNode child) {
      if (caller == this.getSuperInterfaceIdListNoTransform()) {
         caller.getIndexOfChild(child);
         return true;
      } else {
         return this.getParent().Define_boolean_inExtendsOrImplements(this, caller);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
