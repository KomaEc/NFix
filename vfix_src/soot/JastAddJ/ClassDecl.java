package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.MethodSource;
import soot.Scene;
import soot.SootClass;
import soot.coffi.CoffiMethodSource;
import soot.tagkit.SourceFileTag;

public class ClassDecl extends ReferenceType implements Cloneable {
   protected boolean interfacesMethodsSignatureMap_computed = false;
   protected HashMap interfacesMethodsSignatureMap_value;
   protected boolean methodsSignatureMap_computed = false;
   protected HashMap methodsSignatureMap_value;
   protected Map ancestorMethods_String_values;
   protected Map memberTypes_String_values;
   protected boolean memberFieldsMap_computed = false;
   protected HashMap memberFieldsMap_value;
   protected Map memberFields_String_values;
   protected boolean unimplementedMethods_computed = false;
   protected Collection unimplementedMethods_value;
   protected boolean hasAbstract_computed = false;
   protected boolean hasAbstract_value;
   protected Map castingConversionTo_TypeDecl_values;
   protected boolean isString_computed = false;
   protected boolean isString_value;
   protected boolean isObject_computed = false;
   protected boolean isObject_value;
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
      this.interfacesMethodsSignatureMap_computed = false;
      this.interfacesMethodsSignatureMap_value = null;
      this.methodsSignatureMap_computed = false;
      this.methodsSignatureMap_value = null;
      this.ancestorMethods_String_values = null;
      this.memberTypes_String_values = null;
      this.memberFieldsMap_computed = false;
      this.memberFieldsMap_value = null;
      this.memberFields_String_values = null;
      this.unimplementedMethods_computed = false;
      this.unimplementedMethods_value = null;
      this.hasAbstract_computed = false;
      this.castingConversionTo_TypeDecl_values = null;
      this.isString_computed = false;
      this.isObject_computed = false;
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

   public ClassDecl clone() throws CloneNotSupportedException {
      ClassDecl node = (ClassDecl)super.clone();
      node.interfacesMethodsSignatureMap_computed = false;
      node.interfacesMethodsSignatureMap_value = null;
      node.methodsSignatureMap_computed = false;
      node.methodsSignatureMap_value = null;
      node.ancestorMethods_String_values = null;
      node.memberTypes_String_values = null;
      node.memberFieldsMap_computed = false;
      node.memberFieldsMap_value = null;
      node.memberFields_String_values = null;
      node.unimplementedMethods_computed = false;
      node.unimplementedMethods_value = null;
      node.hasAbstract_computed = false;
      node.castingConversionTo_TypeDecl_values = null;
      node.isString_computed = false;
      node.isObject_computed = false;
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

   public ClassDecl copy() {
      try {
         ClassDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ClassDecl fullCopy() {
      ClassDecl tree = this.copy();
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
      TypeDecl typeDecl = this.hasSuperclass() ? this.superclass() : null;
      if (typeDecl != null && !typeDecl.accessibleFromExtend(this)) {
         this.error("class " + this.fullName() + " may not extend non accessible type " + typeDecl.fullName());
      }

      if (this.hasSuperclass() && !this.superclass().accessibleFrom(this)) {
         this.error("a superclass must be accessible which " + this.superclass().name() + " is not");
      }

      for(int i = 0; i < this.getNumImplements(); ++i) {
         TypeDecl decl = this.getImplements(i).type();
         if (!decl.isCircular() && !decl.accessibleFrom(this)) {
            this.error("class " + this.fullName() + " can not implement non accessible type " + decl.fullName());
         }
      }

   }

   public void exceptionHandling() {
      this.constructors();
      super.exceptionHandling();
   }

   public Iterator interfacesMethodsIterator() {
      return new Iterator() {
         private Iterator outer = ClassDecl.this.interfacesMethodsSignatureMap().values().iterator();
         private Iterator inner = null;

         public boolean hasNext() {
            if ((this.inner == null || !this.inner.hasNext()) && this.outer.hasNext()) {
               this.inner = ((SimpleSet)this.outer.next()).iterator();
            }

            return this.inner == null ? false : this.inner.hasNext();
         }

         public Object next() {
            return this.inner.next();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public void checkModifiers() {
      super.checkModifiers();
      TypeDecl typeDecl = this.hasSuperclass() ? this.superclass() : null;
      if (typeDecl != null && typeDecl.isFinal()) {
         this.error("class " + this.fullName() + " may not extend final class " + typeDecl.fullName());
      }

   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      this.getModifiers().toString(s);
      s.append("class " + this.name());
      if (this.hasSuperClassAccess()) {
         s.append(" extends ");
         this.getSuperClassAccess().toString(s);
      }

      if (this.getNumImplements() > 0) {
         s.append(" implements ");
         this.getImplements(0).toString(s);

         for(int i = 1; i < this.getNumImplements(); ++i) {
            s.append(", ");
            this.getImplements(i).toString(s);
         }
      }

      this.ppBodyDecls(s);
   }

   public boolean hasSuperclass() {
      return !this.isObject();
   }

   public ClassDecl superclass() {
      if (this.isObject()) {
         return null;
      } else {
         return this.hasSuperClassAccess() && !this.isCircular() && this.getSuperClassAccess().type().isClassDecl() ? (ClassDecl)this.getSuperClassAccess().type() : (ClassDecl)this.typeObject();
      }
   }

   public Iterator interfacesIterator() {
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
            if (!ClassDecl.this.isObject() && !ClassDecl.this.isCircular()) {
               TypeDecl typeDecl;
               do {
                  if (this.index >= ClassDecl.this.getNumImplements()) {
                     return;
                  }

                  typeDecl = ClassDecl.this.getImplements(this.index++).type();
               } while(typeDecl.isCircular() || !typeDecl.isInterfaceDecl());

               this.current = typeDecl;
            }
         }
      };
   }

   public void nameCheck() {
      super.nameCheck();
      if (this.hasSuperClassAccess() && !this.getSuperClassAccess().type().isClassDecl()) {
         this.error("class may only inherit a class and not " + this.getSuperClassAccess().type().typeName());
      }

      if (this.isObject() && this.hasSuperClassAccess()) {
         this.error("class Object may not have superclass");
      }

      if (this.isObject() && this.getNumImplements() != 0) {
         this.error("class Object may not implement interfaces");
      }

      if (this.isCircular()) {
         this.error("circular inheritance dependency in " + this.typeName());
      }

      HashSet set = new HashSet();

      for(int i = 0; i < this.getNumImplements(); ++i) {
         TypeDecl decl = this.getImplements(i).type();
         if (!decl.isInterfaceDecl() && !decl.isUnknown()) {
            this.error("type " + this.fullName() + " tries to implement non interface type " + decl.fullName());
         }

         if (set.contains(decl)) {
            this.error("type " + decl.fullName() + " mentionened multiple times in implements clause");
         }

         set.add(decl);
      }

      Iterator iter = this.interfacesMethodsIterator();

      while(true) {
         SimpleSet s;
         Iterator i2;
         MethodDecl n;
         MethodDecl m;
         do {
            do {
               if (!iter.hasNext()) {
                  return;
               }

               m = (MethodDecl)iter.next();
            } while(!this.localMethodsSignature(m.signature()).isEmpty());

            s = this.superclass().methodsSignature(m.signature());
            i2 = s.iterator();

            while(i2.hasNext()) {
               n = (MethodDecl)i2.next();
               if (n.accessibleFrom(this)) {
                  this.interfaceMethodCompatibleWithInherited(m, n);
               }
            }
         } while(!s.isEmpty());

         i2 = this.interfacesMethodsSignature(m.signature()).iterator();

         while(i2.hasNext()) {
            n = (MethodDecl)i2.next();
            if (!n.mayOverrideReturn(m) && !m.mayOverrideReturn(n)) {
               this.error("Xthe return type of method " + m.signature() + " in " + m.hostType().typeName() + " does not match the return type of method " + n.signature() + " in " + n.hostType().typeName() + " and may thus not be overriden");
            }
         }
      }
   }

   private void interfaceMethodCompatibleWithInherited(MethodDecl m, MethodDecl n) {
      if (n.isStatic()) {
         this.error("Xa static method may not hide an instance method");
      }

      if (!n.isAbstract() && !n.isPublic()) {
         this.error("Xoverriding access modifier error for " + m.signature() + " in " + m.hostType().typeName() + " and " + n.hostType().typeName());
      }

      if (!n.mayOverrideReturn(m) && !m.mayOverrideReturn(m)) {
         this.error("Xthe return type of method " + m.signature() + " in " + m.hostType().typeName() + " does not match the return type of method " + n.signature() + " in " + n.hostType().typeName() + " and may thus not be overriden");
      }

      if (!n.isAbstract()) {
         for(int i = 0; i < n.getNumException(); ++i) {
            Access e = n.getException(i);
            boolean found = false;

            for(int j = 0; !found && j < m.getNumException(); ++j) {
               if (e.type().instanceOf(m.getException(j).type())) {
                  found = true;
               }
            }

            if (!found && e.type().isUncheckedException()) {
               this.error("X" + n.signature() + " in " + n.hostType().typeName() + " may not throw more checked exceptions than overridden method " + m.signature() + " in " + m.hostType().typeName());
            }
         }
      }

   }

   public TypeDecl makeGeneric(Signatures.ClassSignature s) {
      if (s.hasFormalTypeParameters()) {
         ASTNode node = this.getParent();
         int index = node.getIndexOfChild(this);
         node.setChild(new GenericClassDecl(this.getModifiersNoTransform(), this.getID(), s.hasSuperclassSignature() ? new Opt(s.superclassSignature()) : this.getSuperClassAccessOptNoTransform(), s.hasSuperinterfaceSignature() ? s.superinterfaceSignature() : this.getImplementsListNoTransform(), this.getBodyDeclListNoTransform(), s.typeParameters()), index);
         return (TypeDecl)node.getChildNoTransform(index);
      } else {
         if (s.hasSuperclassSignature()) {
            this.setSuperClassAccessOpt(new Opt(s.superclassSignature()));
         }

         if (s.hasSuperinterfaceSignature()) {
            this.setImplementsList(s.superinterfaceSignature());
         }

         return this;
      }
   }

   public ClassDecl substitutedClassDecl(Parameterization parTypeDecl) {
      ClassDecl c = new ClassDeclSubstituted(this.getModifiers().fullCopy(), this.getID(), this.hasSuperClassAccess() ? new Opt(this.getSuperClassAccess().type().substitute(parTypeDecl)) : new Opt(), this.getImplementsList().substitute(parTypeDecl), this);
      return c;
   }

   public void jimplify1phase2() {
      SootClass sc = this.getSootClassDecl();
      sc.setResolvingLevel(0);
      sc.setModifiers(this.sootTypeModifiers());
      sc.setApplicationClass();
      SourceFileTag st = new SourceFileTag(this.sourceNameWithoutPath());
      st.setAbsolutePath(this.compilationUnit().pathName());
      sc.addTag(st);
      if (this.hasSuperclass()) {
         sc.setSuperclass(this.superclass().getSootClassDecl());
      }

      Iterator iter = this.interfacesIterator();

      while(iter.hasNext()) {
         TypeDecl typeDecl = (TypeDecl)iter.next();
         if (!sc.implementsInterface(typeDecl.getSootClassDecl().getName())) {
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

   public ClassDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[4];
      this.setChild(new Opt(), 1);
      this.setChild(new List(), 2);
      this.setChild(new List(), 3);
   }

   public ClassDecl(Modifiers p0, String p1, Opt<Access> p2, List<Access> p3, List<BodyDecl> p4) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
   }

   public ClassDecl(Modifiers p0, Symbol p1, Opt<Access> p2, List<Access> p3, List<BodyDecl> p4) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
   }

   protected int numChildren() {
      return 4;
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

   public void setSuperClassAccessOpt(Opt<Access> opt) {
      this.setChild(opt, 1);
   }

   public boolean hasSuperClassAccess() {
      return this.getSuperClassAccessOpt().getNumChild() != 0;
   }

   public Access getSuperClassAccess() {
      return (Access)this.getSuperClassAccessOpt().getChild(0);
   }

   public void setSuperClassAccess(Access node) {
      this.getSuperClassAccessOpt().setChild(node, 0);
   }

   public Opt<Access> getSuperClassAccessOpt() {
      return (Opt)this.getChild(1);
   }

   public Opt<Access> getSuperClassAccessOptNoTransform() {
      return (Opt)this.getChildNoTransform(1);
   }

   public void setImplementsList(List<Access> list) {
      this.setChild(list, 2);
   }

   public int getNumImplements() {
      return this.getImplementsList().getNumChild();
   }

   public int getNumImplementsNoTransform() {
      return this.getImplementsListNoTransform().getNumChildNoTransform();
   }

   public Access getImplements(int i) {
      return (Access)this.getImplementsList().getChild(i);
   }

   public void addImplements(Access node) {
      List<Access> list = this.parent != null && state != null ? this.getImplementsList() : this.getImplementsListNoTransform();
      list.addChild(node);
   }

   public void addImplementsNoTransform(Access node) {
      List<Access> list = this.getImplementsListNoTransform();
      list.addChild(node);
   }

   public void setImplements(Access node, int i) {
      List<Access> list = this.getImplementsList();
      list.setChild(node, i);
   }

   public List<Access> getImplementss() {
      return this.getImplementsList();
   }

   public List<Access> getImplementssNoTransform() {
      return this.getImplementsListNoTransform();
   }

   public List<Access> getImplementsList() {
      List<Access> list = (List)this.getChild(2);
      list.getNumChild();
      return list;
   }

   public List<Access> getImplementsListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   public void setBodyDeclList(List<BodyDecl> list) {
      this.setChild(list, 3);
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
      List<BodyDecl> list = (List)this.getChild(3);
      list.getNumChild();
      return list;
   }

   public List<BodyDecl> getBodyDeclListNoTransform() {
      return (List)this.getChildNoTransform(3);
   }

   private boolean refined_TypeConversion_ClassDecl_castingConversionTo_TypeDecl(TypeDecl type) {
      if (type.isArrayDecl()) {
         return this.isObject();
      } else if (type.isClassDecl()) {
         return this == type || this.instanceOf(type) || type.instanceOf(this);
      } else if (!type.isInterfaceDecl()) {
         return super.castingConversionTo(type);
      } else {
         return !this.isFinal() || this.instanceOf(type);
      }
   }

   private boolean refined_Generics_ClassDecl_castingConversionTo_TypeDecl(TypeDecl type) {
      if (type instanceof TypeVariable) {
         TypeVariable t = (TypeVariable)type;
         if (t.getNumTypeBound() == 0) {
            return true;
         } else {
            for(int i = 0; i < t.getNumTypeBound(); ++i) {
               if (this.castingConversionTo(t.getTypeBound(i).type())) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return !type.isClassDecl() || this.erasure() == this && type.erasure() == type ? this.refined_TypeConversion_ClassDecl_castingConversionTo_TypeDecl(type) : this.erasure().castingConversionTo(type.erasure());
      }
   }

   private SootClass refined_EmitJimpleRefinements_ClassDecl_sootClass() {
      boolean needAddclass = false;
      SootClass sc = null;
      if (Scene.v().containsClass(this.jvmName())) {
         SootClass cl = Scene.v().getSootClass(this.jvmName());

         try {
            MethodSource source = cl.getMethodByName("<clinit>").getSource();
            if (source instanceof CoffiMethodSource) {
               Scene.v().removeClass(cl);
               needAddclass = true;
            }
         } catch (RuntimeException var5) {
         }

         sc = cl;
      } else {
         needAddclass = true;
      }

      if (needAddclass) {
         if (this.options().verbose()) {
            System.out.println("Creating from source " + this.jvmName());
         }

         sc = new SootClass(this.jvmName());
         sc.setResolvingLevel(0);
         Scene.v().addClass(sc);
      }

      return sc;
   }

   public Constant cast(Constant c) {
      ASTNode$State state = this.state();
      return Constant.create(c.stringValue());
   }

   public Constant add(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.stringValue() + c2.stringValue());
   }

   public Constant questionColon(Constant cond, Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(cond.booleanValue() ? c1.stringValue() : c2.stringValue());
   }

   public boolean eqIsTrue(Expr left, Expr right) {
      ASTNode$State state = this.state();
      return this.isString() && left.constant().stringValue().equals(right.constant().stringValue());
   }

   public int lineNumber() {
      ASTNode$State state = this.state();
      return getLine(this.IDstart);
   }

   public Collection lookupSuperConstructor() {
      ASTNode$State state = this.state();
      return (Collection)(this.hasSuperclass() ? this.superclass().constructors() : Collections.EMPTY_LIST);
   }

   public boolean noConstructor() {
      ASTNode$State state = this.state();
      if (!this.compilationUnit().fromSource()) {
         return false;
      } else {
         for(int i = 0; i < this.getNumBodyDecl(); ++i) {
            if (this.getBodyDecl(i) instanceof ConstructorDecl) {
               return false;
            }
         }

         return true;
      }
   }

   public SimpleSet interfacesMethodsSignature(String signature) {
      ASTNode$State state = this.state();
      SimpleSet set = (SimpleSet)this.interfacesMethodsSignatureMap().get(signature);
      return set != null ? set : SimpleSet.emptySet;
   }

   public HashMap interfacesMethodsSignatureMap() {
      if (this.interfacesMethodsSignatureMap_computed) {
         return this.interfacesMethodsSignatureMap_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.interfacesMethodsSignatureMap_value = this.interfacesMethodsSignatureMap_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.interfacesMethodsSignatureMap_computed = true;
         }

         return this.interfacesMethodsSignatureMap_value;
      }
   }

   private HashMap interfacesMethodsSignatureMap_compute() {
      HashMap map = new HashMap();
      Iterator iter = this.interfacesIterator();

      while(iter.hasNext()) {
         TypeDecl typeDecl = (InterfaceDecl)iter.next();
         Iterator i2 = typeDecl.methodsIterator();

         while(i2.hasNext()) {
            MethodDecl m = (MethodDecl)i2.next();
            putSimpleSetElement(map, m.signature(), m);
         }
      }

      return map;
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
      Iterator iter;
      if (this.hasSuperclass()) {
         iter = this.superclass().methodsIterator();

         label88:
         while(true) {
            MethodDecl m;
            do {
               do {
                  do {
                     do {
                        if (!iter.hasNext()) {
                           break label88;
                        }

                        m = (MethodDecl)iter.next();
                     } while(m.isPrivate());
                  } while(!m.accessibleFrom(this));
               } while(this.localMethodsSignatureMap().containsKey(m.signature()));
            } while(m instanceof MethodDeclSubstituted && this.localMethodsSignatureMap().containsKey(m.sourceMethodDecl().signature()));

            putSimpleSetElement(map, m.signature(), m);
         }
      }

      iter = this.interfacesIterator();

      label65:
      while(iter.hasNext()) {
         TypeDecl typeDecl = (TypeDecl)iter.next();
         Iterator iter = typeDecl.methodsIterator();

         while(true) {
            MethodDecl m;
            do {
               do {
                  do {
                     do {
                        do {
                           do {
                              if (!iter.hasNext()) {
                                 continue label65;
                              }

                              m = (MethodDecl)iter.next();
                           } while(m.isPrivate());
                        } while(!m.accessibleFrom(this));
                     } while(this.localMethodsSignatureMap().containsKey(m.signature()));
                  } while(m instanceof MethodDeclSubstituted && this.localMethodsSignatureMap().containsKey(m.sourceMethodDecl().signature()));
               } while(!this.allMethodsAbstract((SimpleSet)map.get(m.signature())));
            } while(m instanceof MethodDeclSubstituted && !this.allMethodsAbstract((SimpleSet)map.get(m.sourceMethodDecl().signature())));

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
      Iterator iter;
      MethodDecl m;
      if (this.hasSuperclass()) {
         iter = this.superclass().localMethodsSignature(signature).iterator();

         while(iter.hasNext()) {
            m = (MethodDecl)iter.next();
            if (!m.isPrivate()) {
               set = set.add(m);
            }
         }
      }

      if (set.size() != 1 || ((MethodDecl)set.iterator().next()).isAbstract()) {
         for(iter = this.interfacesMethodsSignature(signature).iterator(); iter.hasNext(); set = set.add(m)) {
            m = (MethodDecl)iter.next();
         }
      }

      if (!this.hasSuperclass()) {
         return set;
      } else {
         if (set.size() == 1) {
            MethodDecl m = (MethodDecl)set.iterator().next();
            if (!m.isAbstract()) {
               boolean done = true;
               Iterator iter = this.superclass().ancestorMethods(signature).iterator();

               label49:
               while(true) {
                  MethodDecl n;
                  do {
                     if (!iter.hasNext()) {
                        if (done) {
                           return set;
                        }
                        break label49;
                     }

                     n = (MethodDecl)iter.next();
                  } while(!n.isPrivate() && n.accessibleFrom(m.hostType()));

                  done = false;
               }
            }
         }

         for(iter = this.superclass().ancestorMethods(signature).iterator(); iter.hasNext(); set = set.add(m)) {
            m = (MethodDecl)iter.next();
         }

         return set;
      }
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
         Iterator iter = this.interfacesIterator();

         TypeDecl decl;
         while(iter.hasNext()) {
            decl = (TypeDecl)iter.next();
            Iterator iter = decl.memberTypes(name).iterator();

            while(iter.hasNext()) {
               TypeDecl decl = (TypeDecl)iter.next();
               if (!decl.isPrivate() && decl.accessibleFrom(this)) {
                  set = set.add(decl);
               }
            }
         }

         if (this.hasSuperclass()) {
            iter = this.superclass().memberTypes(name).iterator();

            while(iter.hasNext()) {
               decl = (TypeDecl)iter.next();
               if (!decl.isPrivate() && decl.accessibleFrom(this)) {
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
      Iterator outerIter;
      if (this.hasSuperclass()) {
         outerIter = this.superclass().fieldsIterator();

         while(outerIter.hasNext()) {
            FieldDeclaration decl = (FieldDeclaration)outerIter.next();
            if (!decl.isPrivate() && decl.accessibleFrom(this) && !this.localFieldsMap().containsKey(decl.name())) {
               putSimpleSetElement(map, decl.name(), decl);
            }
         }
      }

      outerIter = this.interfacesIterator();

      while(outerIter.hasNext()) {
         TypeDecl type = (TypeDecl)outerIter.next();
         Iterator iter = type.fieldsIterator();

         while(iter.hasNext()) {
            FieldDeclaration decl = (FieldDeclaration)iter.next();
            if (!decl.isPrivate() && decl.accessibleFrom(this) && !this.localFieldsMap().containsKey(decl.name())) {
               putSimpleSetElement(map, decl.name(), decl);
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
         Iterator outerIter;
         if (this.hasSuperclass()) {
            outerIter = this.superclass().memberFields(name).iterator();

            while(outerIter.hasNext()) {
               FieldDeclaration decl = (FieldDeclaration)outerIter.next();
               if (!decl.isPrivate() && decl.accessibleFrom(this)) {
                  fields = fields.add(decl);
               }
            }
         }

         outerIter = this.interfacesIterator();

         while(outerIter.hasNext()) {
            TypeDecl type = (TypeDecl)outerIter.next();
            Iterator iter = type.memberFields(name).iterator();

            while(iter.hasNext()) {
               FieldDeclaration decl = (FieldDeclaration)iter.next();
               if (!decl.isPrivate() && decl.accessibleFrom(this)) {
                  fields = fields.add(decl);
               }
            }
         }

         return fields;
      }
   }

   public Collection unimplementedMethods() {
      if (this.unimplementedMethods_computed) {
         return this.unimplementedMethods_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.unimplementedMethods_value = this.unimplementedMethods_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.unimplementedMethods_computed = true;
         }

         return this.unimplementedMethods_value;
      }
   }

   private Collection unimplementedMethods_compute() {
      Collection c = new ArrayList();
      Iterator iter = this.interfacesMethodsIterator();

      MethodDecl m;
      while(iter.hasNext()) {
         m = (MethodDecl)iter.next();
         boolean implemented = false;
         SimpleSet set = this.localMethodsSignature(m.signature());
         if (set.size() == 1) {
            MethodDecl n = (MethodDecl)set.iterator().next();
            if (!n.isAbstract()) {
               implemented = true;
            }
         }

         if (!implemented) {
            set = this.ancestorMethods(m.signature());
            Iterator i2 = set.iterator();

            while(i2.hasNext()) {
               MethodDecl n = (MethodDecl)i2.next();
               if (!n.isAbstract()) {
                  implemented = true;
                  break;
               }
            }
         }

         if (!implemented) {
            c.add(m);
         }
      }

      if (this.hasSuperclass()) {
         iter = this.superclass().unimplementedMethods().iterator();

         label56:
         while(true) {
            MethodDecl n;
            do {
               while(true) {
                  if (!iter.hasNext()) {
                     break label56;
                  }

                  m = (MethodDecl)iter.next();
                  SimpleSet set = this.localMethodsSignature(m.signature());
                  if (set.size() == 1) {
                     n = (MethodDecl)set.iterator().next();
                     break;
                  }

                  c.add(m);
               }
            } while(!n.isAbstract() && n.overrides(m));

            c.add(m);
         }
      }

      iter = this.localMethodsIterator();

      while(iter.hasNext()) {
         m = (MethodDecl)iter.next();
         if (m.isAbstract()) {
            c.add(m);
         }
      }

      return c;
   }

   public boolean hasAbstract() {
      if (this.hasAbstract_computed) {
         return this.hasAbstract_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.hasAbstract_value = this.hasAbstract_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.hasAbstract_computed = true;
         }

         return this.hasAbstract_value;
      }
   }

   private boolean hasAbstract_compute() {
      return !this.unimplementedMethods().isEmpty();
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
      if (this.refined_Generics_ClassDecl_castingConversionTo_TypeDecl(type)) {
         return true;
      } else {
         boolean canUnboxThis = !this.unboxed().isUnknown();
         boolean canUnboxType = !type.unboxed().isUnknown();
         return canUnboxThis && !canUnboxType ? this.unboxed().wideningConversionTo(type) : false;
      }
   }

   public boolean isClassDecl() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isString() {
      if (this.isString_computed) {
         return this.isString_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isString_value = this.isString_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isString_computed = true;
         }

         return this.isString_value;
      }
   }

   private boolean isString_compute() {
      return this.fullName().equals("java.lang.String");
   }

   public boolean isObject() {
      if (this.isObject_computed) {
         return this.isObject_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isObject_value = this.isObject_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isObject_computed = true;
         }

         return this.isObject_value;
      }
   }

   private boolean isObject_compute() {
      return this.name().equals("Object") && this.packageName().equals("java.lang");
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
         return type.hasSuperclass() && type.superclass() != null && type.superclass().instanceOf(this);
      }
   }

   public boolean isSupertypeOfInterfaceDecl(InterfaceDecl type) {
      ASTNode$State state = this.state();
      return this.isObject();
   }

   public boolean isSupertypeOfArrayDecl(ArrayDecl type) {
      ASTNode$State state = this.state();
      if (super.isSupertypeOfArrayDecl(type)) {
         return true;
      } else {
         return type.hasSuperclass() && type.superclass() != null && type.superclass().instanceOf(this);
      }
   }

   public boolean isInnerClass() {
      ASTNode$State state = this.state();
      return this.isNestedType() && !this.isStatic() && this.enclosingType().isClassDecl();
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
      if (this.hasSuperClassAccess()) {
         for(Access a = this.getSuperClassAccess().lastAccess(); a != null; a = a.isQualified() && a.qualifier().isTypeAccess() ? (Access)a.qualifier() : null) {
            if (a.type().isCircular()) {
               return true;
            }
         }
      }

      for(int i = 0; i < this.getNumImplements(); ++i) {
         for(Access a = this.getImplements(i).lastAccess(); a != null; a = a.isQualified() && a.qualifier().isTypeAccess() ? (Access)a.qualifier() : null) {
            if (a.type().isCircular()) {
               return true;
            }
         }
      }

      return false;
   }

   public Annotation annotation(TypeDecl typeDecl) {
      ASTNode$State state = this.state();
      Annotation a = super.annotation(typeDecl);
      if (a != null) {
         return a;
      } else {
         return this.hasSuperclass() && typeDecl.annotation(this.lookupType("java.lang.annotation", "Inherited")) != null ? this.superclass().annotation(typeDecl) : null;
      }
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
      if (this.hasSuperclass()) {
         set.addAll(this.superclass().implementedInterfaces());
      }

      Iterator iter = this.interfacesIterator();

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
      return type.supertypeClassDecl(this);
   }

   public boolean supertypeClassDecl(ClassDecl type) {
      ASTNode$State state = this.state();
      return super.supertypeClassDecl(type) || type.hasSuperclass() && type.superclass() != null && type.superclass().subtype(this);
   }

   public boolean supertypeInterfaceDecl(InterfaceDecl type) {
      ASTNode$State state = this.state();
      return this.isObject();
   }

   public boolean supertypeArrayDecl(ArrayDecl type) {
      ASTNode$State state = this.state();
      if (super.supertypeArrayDecl(type)) {
         return true;
      } else {
         return type.hasSuperclass() && type.superclass() != null && type.superclass().subtype(this);
      }
   }

   public TypeDecl superEnclosing() {
      ASTNode$State state = this.state();
      return this.superclass().erasure().enclosing();
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
      if (!Scene.v().isIncrementalBuild()) {
         return this.refined_EmitJimpleRefinements_ClassDecl_sootClass();
      } else {
         if (Scene.v().containsClass(this.jvmName())) {
            Scene.v().removeClass(Scene.v().getSootClass(this.jvmName()));
         }

         SootClass sc = null;
         if (this.options().verbose()) {
            System.out.println("Creating from source " + this.jvmName());
         }

         sc = new SootClass(this.jvmName());
         sc.setResolvingLevel(0);
         Scene.v().addClass(sc);
         return sc;
      }
   }

   public String typeDescriptor() {
      ASTNode$State state = this.state();
      return "L" + this.jvmName().replace('.', '/') + ";";
   }

   public SimpleSet bridgeCandidates(String signature) {
      ASTNode$State state = this.state();
      SimpleSet set = this.ancestorMethods(signature);

      for(Iterator iter = this.interfacesMethodsSignature(signature).iterator(); iter.hasNext(); set = set.add(iter.next())) {
      }

      return set;
   }

   public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? true : super.Define_boolean_mayBeFinal(caller, child);
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      if (caller == this.getImplementsListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.TYPE_NAME;
      } else {
         return caller == this.getSuperClassAccessOptNoTransform() ? NameType.TYPE_NAME : super.Define_NameType_nameType(caller, child);
      }
   }

   public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
      if (caller == this.getImplementsListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.hostType();
      } else {
         return caller == this.getSuperClassAccessOptNoTransform() ? this.hostType() : super.Define_TypeDecl_hostType(caller, child);
      }
   }

   public boolean Define_boolean_withinSuppressWarnings(ASTNode caller, ASTNode child, String s) {
      if (caller == this.getImplementsListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.hasAnnotationSuppressWarnings(s) || this.withinSuppressWarnings(s);
      } else if (caller != this.getSuperClassAccessOptNoTransform()) {
         return super.Define_boolean_withinSuppressWarnings(caller, child, s);
      } else {
         return this.hasAnnotationSuppressWarnings(s) || this.withinSuppressWarnings(s);
      }
   }

   public boolean Define_boolean_withinDeprecatedAnnotation(ASTNode caller, ASTNode child) {
      if (caller == this.getImplementsListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.isDeprecated() || this.withinDeprecatedAnnotation();
      } else if (caller != this.getSuperClassAccessOptNoTransform()) {
         return super.Define_boolean_withinDeprecatedAnnotation(caller, child);
      } else {
         return this.isDeprecated() || this.withinDeprecatedAnnotation();
      }
   }

   public boolean Define_boolean_inExtendsOrImplements(ASTNode caller, ASTNode child) {
      if (caller == this.getImplementsListNoTransform()) {
         caller.getIndexOfChild(child);
         return true;
      } else {
         return caller == this.getSuperClassAccessOptNoTransform() ? true : this.getParent().Define_boolean_inExtendsOrImplements(this, caller);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
