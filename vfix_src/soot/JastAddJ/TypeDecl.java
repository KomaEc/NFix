package soot.JastAddJ;

import beaver.Symbol;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.SootResolver;
import soot.Type;
import soot.Value;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.tagkit.InnerClassAttribute;
import soot.tagkit.InnerClassTag;
import soot.tagkit.Tag;

public abstract class TypeDecl extends ASTNode<ASTNode> implements Cloneable, SimpleSet, Iterator, VariableScope {
   public int anonymousIndex = 0;
   private TypeDecl iterElem;
   private Collection nestedTypes;
   private Collection usedNestedTypes;
   public int accessorCounter = 0;
   private HashMap accessorMap = null;
   private boolean addEnclosingVariables = true;
   int uniqueIndexCounter = 1;
   private FieldDeclaration createAssertionsDisabled = null;
   private HashMap createStaticClassField = null;
   private MethodDecl createStaticClassMethod = null;
   public SootMethod clinit = null;
   private HashMap createEnumIndexMap = null;
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;
   protected Map accessibleFromPackage_String_values;
   protected Map accessibleFromExtend_TypeDecl_values;
   protected Map accessibleFrom_TypeDecl_values;
   protected boolean dimension_computed = false;
   protected int dimension_value;
   protected boolean elementType_computed = false;
   protected TypeDecl elementType_value;
   protected boolean arrayType_computed = false;
   protected TypeDecl arrayType_value;
   protected boolean isException_computed = false;
   protected boolean isException_value;
   protected boolean isCheckedException_computed = false;
   protected boolean isCheckedException_value;
   protected boolean isUncheckedException_computed = false;
   protected boolean isUncheckedException_value;
   protected Map mayCatch_TypeDecl_values;
   protected boolean constructors_computed = false;
   protected Collection constructors_value;
   protected Map unqualifiedLookupMethod_String_values;
   protected boolean methodsNameMap_computed = false;
   protected HashMap methodsNameMap_value;
   protected boolean localMethodsSignatureMap_computed = false;
   protected HashMap localMethodsSignatureMap_value;
   protected boolean methodsSignatureMap_computed = false;
   protected HashMap methodsSignatureMap_value;
   protected Map ancestorMethods_String_values;
   protected Map localTypeDecls_String_values;
   protected Map memberTypes_String_values;
   protected Map localFields_String_values;
   protected boolean localFieldsMap_computed = false;
   protected HashMap localFieldsMap_value;
   protected boolean memberFieldsMap_computed = false;
   protected HashMap memberFieldsMap_value;
   protected Map memberFields_String_values;
   protected boolean hasAbstract_computed = false;
   protected boolean hasAbstract_value;
   protected boolean unimplementedMethods_computed = false;
   protected Collection unimplementedMethods_value;
   protected boolean isPublic_computed = false;
   protected boolean isPublic_value;
   protected boolean isStatic_computed = false;
   protected boolean isStatic_value;
   protected boolean fullName_computed = false;
   protected String fullName_value;
   protected boolean typeName_computed = false;
   protected String typeName_value;
   protected Map narrowingConversionTo_TypeDecl_values;
   protected Map methodInvocationConversionTo_TypeDecl_values;
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
   protected boolean boxed_computed = false;
   protected TypeDecl boxed_value;
   protected boolean unboxed_computed = false;
   protected TypeDecl unboxed_value;
   protected boolean isIterable_computed = false;
   protected boolean isIterable_value;
   protected int involvesTypeParameters_visited = -1;
   protected boolean involvesTypeParameters_computed = false;
   protected boolean involvesTypeParameters_initialized = false;
   protected boolean involvesTypeParameters_value;
   protected boolean erasure_computed = false;
   protected TypeDecl erasure_value;
   protected boolean implementedInterfaces_computed = false;
   protected HashSet implementedInterfaces_value;
   protected int usesTypeVariable_visited = -1;
   protected boolean usesTypeVariable_computed = false;
   protected boolean usesTypeVariable_initialized = false;
   protected boolean usesTypeVariable_value;
   protected boolean sourceTypeDecl_computed = false;
   protected TypeDecl sourceTypeDecl_value;
   protected Map containedIn_TypeDecl_values;
   protected Map sameStructure_TypeDecl_values;
   protected Map subtype_TypeDecl_values;
   protected boolean enclosingVariables_computed = false;
   protected Collection enclosingVariables_value;
   protected boolean uniqueIndex_computed = false;
   protected int uniqueIndex_value;
   protected boolean jvmName_computed = false;
   protected String jvmName_value;
   protected boolean getSootClassDecl_computed = false;
   protected SootClass getSootClassDecl_value;
   protected boolean getSootType_computed = false;
   protected Type getSootType_value;
   protected boolean sootClass_computed = false;
   protected SootClass sootClass_value;
   protected boolean needsClinit_computed = false;
   protected boolean needsClinit_value;
   protected boolean innerClassesAttributeEntries_computed = false;
   protected Collection innerClassesAttributeEntries_value;
   protected Map getSootField_String_TypeDecl_values;
   protected Map createEnumMethod_TypeDecl_values;
   protected Map createEnumIndex_EnumConstant_values;
   protected Map createEnumArray_TypeDecl_values;
   protected boolean componentType_computed = false;
   protected TypeDecl componentType_value;
   protected Map isDAbefore_Variable_values;
   protected Map isDUbefore_Variable_values;
   protected boolean typeException_computed = false;
   protected TypeDecl typeException_value;
   protected boolean typeRuntimeException_computed = false;
   protected TypeDecl typeRuntimeException_value;
   protected boolean typeError_computed = false;
   protected TypeDecl typeError_value;
   protected Map lookupMethod_String_values;
   protected boolean typeObject_computed = false;
   protected TypeDecl typeObject_value;
   protected Map lookupType_String_values;
   protected Map lookupVariable_String_values;
   protected boolean packageName_computed = false;
   protected String packageName_value;
   protected boolean isAnonymous_computed = false;
   protected boolean isAnonymous_value;
   protected boolean unknownType_computed = false;
   protected TypeDecl unknownType_value;
   protected boolean inExplicitConstructorInvocation_computed = false;
   protected boolean inExplicitConstructorInvocation_value;
   protected boolean inStaticContext_computed = false;
   protected boolean inStaticContext_value;

   public void flushCache() {
      super.flushCache();
      this.accessibleFromPackage_String_values = null;
      this.accessibleFromExtend_TypeDecl_values = null;
      this.accessibleFrom_TypeDecl_values = null;
      this.dimension_computed = false;
      this.elementType_computed = false;
      this.elementType_value = null;
      this.arrayType_computed = false;
      this.arrayType_value = null;
      this.isException_computed = false;
      this.isCheckedException_computed = false;
      this.isUncheckedException_computed = false;
      this.mayCatch_TypeDecl_values = null;
      this.constructors_computed = false;
      this.constructors_value = null;
      this.unqualifiedLookupMethod_String_values = null;
      this.methodsNameMap_computed = false;
      this.methodsNameMap_value = null;
      this.localMethodsSignatureMap_computed = false;
      this.localMethodsSignatureMap_value = null;
      this.methodsSignatureMap_computed = false;
      this.methodsSignatureMap_value = null;
      this.ancestorMethods_String_values = null;
      this.localTypeDecls_String_values = null;
      this.memberTypes_String_values = null;
      this.localFields_String_values = null;
      this.localFieldsMap_computed = false;
      this.localFieldsMap_value = null;
      this.memberFieldsMap_computed = false;
      this.memberFieldsMap_value = null;
      this.memberFields_String_values = null;
      this.hasAbstract_computed = false;
      this.unimplementedMethods_computed = false;
      this.unimplementedMethods_value = null;
      this.isPublic_computed = false;
      this.isStatic_computed = false;
      this.fullName_computed = false;
      this.fullName_value = null;
      this.typeName_computed = false;
      this.typeName_value = null;
      this.narrowingConversionTo_TypeDecl_values = null;
      this.methodInvocationConversionTo_TypeDecl_values = null;
      this.castingConversionTo_TypeDecl_values = null;
      this.isString_computed = false;
      this.isObject_computed = false;
      this.instanceOf_TypeDecl_values = null;
      this.isCircular_visited = -1;
      this.isCircular_computed = false;
      this.isCircular_initialized = false;
      this.boxed_computed = false;
      this.boxed_value = null;
      this.unboxed_computed = false;
      this.unboxed_value = null;
      this.isIterable_computed = false;
      this.involvesTypeParameters_visited = -1;
      this.involvesTypeParameters_computed = false;
      this.involvesTypeParameters_initialized = false;
      this.erasure_computed = false;
      this.erasure_value = null;
      this.implementedInterfaces_computed = false;
      this.implementedInterfaces_value = null;
      this.usesTypeVariable_visited = -1;
      this.usesTypeVariable_computed = false;
      this.usesTypeVariable_initialized = false;
      this.sourceTypeDecl_computed = false;
      this.sourceTypeDecl_value = null;
      this.containedIn_TypeDecl_values = null;
      this.sameStructure_TypeDecl_values = null;
      this.subtype_TypeDecl_values = null;
      this.enclosingVariables_computed = false;
      this.enclosingVariables_value = null;
      this.uniqueIndex_computed = false;
      this.jvmName_computed = false;
      this.jvmName_value = null;
      this.getSootClassDecl_computed = false;
      this.getSootClassDecl_value = null;
      this.getSootType_computed = false;
      this.getSootType_value = null;
      this.sootClass_computed = false;
      this.sootClass_value = null;
      this.needsClinit_computed = false;
      this.innerClassesAttributeEntries_computed = false;
      this.innerClassesAttributeEntries_value = null;
      this.getSootField_String_TypeDecl_values = null;
      this.createEnumMethod_TypeDecl_values = null;
      this.createEnumIndex_EnumConstant_values = null;
      this.createEnumArray_TypeDecl_values = null;
      this.componentType_computed = false;
      this.componentType_value = null;
      this.isDAbefore_Variable_values = null;
      this.isDUbefore_Variable_values = null;
      this.typeException_computed = false;
      this.typeException_value = null;
      this.typeRuntimeException_computed = false;
      this.typeRuntimeException_value = null;
      this.typeError_computed = false;
      this.typeError_value = null;
      this.lookupMethod_String_values = null;
      this.typeObject_computed = false;
      this.typeObject_value = null;
      this.lookupType_String_values = null;
      this.lookupVariable_String_values = null;
      this.packageName_computed = false;
      this.packageName_value = null;
      this.isAnonymous_computed = false;
      this.unknownType_computed = false;
      this.unknownType_value = null;
      this.inExplicitConstructorInvocation_computed = false;
      this.inStaticContext_computed = false;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public TypeDecl clone() throws CloneNotSupportedException {
      TypeDecl node = (TypeDecl)super.clone();
      node.accessibleFromPackage_String_values = null;
      node.accessibleFromExtend_TypeDecl_values = null;
      node.accessibleFrom_TypeDecl_values = null;
      node.dimension_computed = false;
      node.elementType_computed = false;
      node.elementType_value = null;
      node.arrayType_computed = false;
      node.arrayType_value = null;
      node.isException_computed = false;
      node.isCheckedException_computed = false;
      node.isUncheckedException_computed = false;
      node.mayCatch_TypeDecl_values = null;
      node.constructors_computed = false;
      node.constructors_value = null;
      node.unqualifiedLookupMethod_String_values = null;
      node.methodsNameMap_computed = false;
      node.methodsNameMap_value = null;
      node.localMethodsSignatureMap_computed = false;
      node.localMethodsSignatureMap_value = null;
      node.methodsSignatureMap_computed = false;
      node.methodsSignatureMap_value = null;
      node.ancestorMethods_String_values = null;
      node.localTypeDecls_String_values = null;
      node.memberTypes_String_values = null;
      node.localFields_String_values = null;
      node.localFieldsMap_computed = false;
      node.localFieldsMap_value = null;
      node.memberFieldsMap_computed = false;
      node.memberFieldsMap_value = null;
      node.memberFields_String_values = null;
      node.hasAbstract_computed = false;
      node.unimplementedMethods_computed = false;
      node.unimplementedMethods_value = null;
      node.isPublic_computed = false;
      node.isStatic_computed = false;
      node.fullName_computed = false;
      node.fullName_value = null;
      node.typeName_computed = false;
      node.typeName_value = null;
      node.narrowingConversionTo_TypeDecl_values = null;
      node.methodInvocationConversionTo_TypeDecl_values = null;
      node.castingConversionTo_TypeDecl_values = null;
      node.isString_computed = false;
      node.isObject_computed = false;
      node.instanceOf_TypeDecl_values = null;
      node.isCircular_visited = -1;
      node.isCircular_computed = false;
      node.isCircular_initialized = false;
      node.boxed_computed = false;
      node.boxed_value = null;
      node.unboxed_computed = false;
      node.unboxed_value = null;
      node.isIterable_computed = false;
      node.involvesTypeParameters_visited = -1;
      node.involvesTypeParameters_computed = false;
      node.involvesTypeParameters_initialized = false;
      node.erasure_computed = false;
      node.erasure_value = null;
      node.implementedInterfaces_computed = false;
      node.implementedInterfaces_value = null;
      node.usesTypeVariable_visited = -1;
      node.usesTypeVariable_computed = false;
      node.usesTypeVariable_initialized = false;
      node.sourceTypeDecl_computed = false;
      node.sourceTypeDecl_value = null;
      node.containedIn_TypeDecl_values = null;
      node.sameStructure_TypeDecl_values = null;
      node.subtype_TypeDecl_values = null;
      node.enclosingVariables_computed = false;
      node.enclosingVariables_value = null;
      node.uniqueIndex_computed = false;
      node.jvmName_computed = false;
      node.jvmName_value = null;
      node.getSootClassDecl_computed = false;
      node.getSootClassDecl_value = null;
      node.getSootType_computed = false;
      node.getSootType_value = null;
      node.sootClass_computed = false;
      node.sootClass_value = null;
      node.needsClinit_computed = false;
      node.innerClassesAttributeEntries_computed = false;
      node.innerClassesAttributeEntries_value = null;
      node.getSootField_String_TypeDecl_values = null;
      node.createEnumMethod_TypeDecl_values = null;
      node.createEnumIndex_EnumConstant_values = null;
      node.createEnumArray_TypeDecl_values = null;
      node.componentType_computed = false;
      node.componentType_value = null;
      node.isDAbefore_Variable_values = null;
      node.isDUbefore_Variable_values = null;
      node.typeException_computed = false;
      node.typeException_value = null;
      node.typeRuntimeException_computed = false;
      node.typeRuntimeException_value = null;
      node.typeError_computed = false;
      node.typeError_value = null;
      node.lookupMethod_String_values = null;
      node.typeObject_computed = false;
      node.typeObject_value = null;
      node.lookupType_String_values = null;
      node.lookupVariable_String_values = null;
      node.packageName_computed = false;
      node.packageName_value = null;
      node.isAnonymous_computed = false;
      node.unknownType_computed = false;
      node.unknownType_value = null;
      node.inExplicitConstructorInvocation_computed = false;
      node.inStaticContext_computed = false;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public int nextAnonymousIndex() {
      return this.isNestedType() ? this.enclosingType().nextAnonymousIndex() : this.anonymousIndex++;
   }

   public MethodDecl addMemberMethod(MethodDecl m) {
      this.addBodyDecl(m);
      return (MethodDecl)this.getBodyDecl(this.getNumBodyDecl() - 1);
   }

   public ConstructorDecl addConstructor(ConstructorDecl c) {
      this.addBodyDecl(c);
      return (ConstructorDecl)this.getBodyDecl(this.getNumBodyDecl() - 1);
   }

   public ClassDecl addMemberClass(ClassDecl c) {
      this.addBodyDecl(new MemberClassDecl(c));
      return ((MemberClassDecl)this.getBodyDecl(this.getNumBodyDecl() - 1)).getClassDecl();
   }

   public FieldDeclaration addMemberField(FieldDeclaration f) {
      this.addBodyDecl(f);
      return (FieldDeclaration)this.getBodyDecl(this.getNumBodyDecl() - 1);
   }

   public TypeAccess createBoundAccess() {
      return new BoundTypeAccess("", this.name(), this);
   }

   public SimpleSet add(Object o) {
      return (new SimpleSet.SimpleSetImpl()).add(this).add(o);
   }

   public boolean isSingleton() {
      return true;
   }

   public boolean isSingleton(Object o) {
      return this.contains(o);
   }

   public Iterator iterator() {
      this.iterElem = this;
      return this;
   }

   public boolean hasNext() {
      return this.iterElem != null;
   }

   public Object next() {
      Object o = this.iterElem;
      this.iterElem = null;
      return o;
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }

   public boolean declaredBeforeUse(Variable decl, ASTNode use) {
      int indexDecl = ((ASTNode)decl).varChildIndex(this);
      int indexUse = use.varChildIndex(this);
      return indexDecl < indexUse;
   }

   public boolean declaredBeforeUse(Variable decl, int indexUse) {
      int indexDecl = ((ASTNode)decl).varChildIndex(this);
      return indexDecl < indexUse;
   }

   public ConstructorDecl lookupConstructor(ConstructorDecl signature) {
      Iterator iter = this.constructors().iterator();

      ConstructorDecl decl;
      do {
         if (!iter.hasNext()) {
            return null;
         }

         decl = (ConstructorDecl)iter.next();
      } while(!decl.sameSignature(signature));

      return decl;
   }

   public boolean mayAccess(MethodAccess access, MethodDecl method) {
      if (this.instanceOf(method.hostType()) && access.qualifier().type().instanceOf(this)) {
         return true;
      } else {
         return this.isNestedType() ? this.enclosingType().mayAccess(access, method) : false;
      }
   }

   public Iterator localMethodsIterator() {
      return new Iterator() {
         private Iterator outer = TypeDecl.this.localMethodsSignatureMap().values().iterator();
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

   public Iterator methodsIterator() {
      return new Iterator() {
         private Iterator outer = TypeDecl.this.methodsSignatureMap().values().iterator();
         private Iterator inner = null;

         public boolean hasNext() {
            if ((this.inner == null || !this.inner.hasNext()) && this.outer.hasNext()) {
               this.inner = ((SimpleSet)this.outer.next()).iterator();
            }

            return this.inner != null ? this.inner.hasNext() : false;
         }

         public Object next() {
            return this.inner.next();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   protected boolean allMethodsAbstract(SimpleSet set) {
      if (set == null) {
         return true;
      } else {
         Iterator iter = set.iterator();

         MethodDecl m;
         do {
            if (!iter.hasNext()) {
               return true;
            }

            m = (MethodDecl)iter.next();
         } while(m.isAbstract());

         return false;
      }
   }

   public boolean mayAccess(Expr expr, FieldDeclaration field) {
      if (!this.instanceOf(field.hostType()) || field.isInstanceVariable() && !expr.isSuperAccess() && !expr.type().instanceOf(this)) {
         return this.isNestedType() ? this.enclosingType().mayAccess(expr, field) : false;
      } else {
         return true;
      }
   }

   public Iterator fieldsIterator() {
      return new Iterator() {
         private Iterator outer = TypeDecl.this.memberFieldsMap().values().iterator();
         private Iterator inner = null;

         public boolean hasNext() {
            if ((this.inner == null || !this.inner.hasNext()) && this.outer.hasNext()) {
               this.inner = ((SimpleSet)this.outer.next()).iterator();
            }

            return this.inner != null ? this.inner.hasNext() : false;
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
      if (this.isPublic() && !this.isTopLevelType() && !this.isMemberType()) {
         this.error("public pertains only to top level types and member types");
      }

      if ((this.isProtected() || this.isPrivate()) && (!this.isMemberType() || !this.enclosingType().isClassDecl())) {
         this.error("protected and private may only be used on member types within a directly enclosing class declaration");
      }

      if (this.isStatic() && !this.isMemberType()) {
         this.error("static pertains only to member types");
      }

      if (!this.isAbstract() && this.hasAbstract()) {
         StringBuffer s = new StringBuffer();
         s.append("" + this.name() + " is not declared abstract but contains abstract members: \n");
         Iterator iter = this.unimplementedMethods().iterator();

         while(iter.hasNext()) {
            MethodDecl m = (MethodDecl)iter.next();
            s.append("  " + m.signature() + " in " + m.hostType().typeName() + "\n");
         }

         this.error(s.toString());
      }

   }

   public void nameCheck() {
      if (this.isTopLevelType() && this.lookupType(this.packageName(), this.name()) != this) {
         this.error("duplicate type " + this.name() + " in package " + this.packageName());
      }

      if (!this.isTopLevelType() && !this.isAnonymous() && !this.isLocalClass() && this.extractSingleType(this.enclosingType().memberTypes(this.name())) != this) {
         this.error("duplicate member type " + this.name() + " in type " + this.enclosingType().typeName());
      }

      if (this.isLocalClass()) {
         TypeDecl typeDecl = this.extractSingleType(this.lookupType(this.name()));
         if (typeDecl != null && typeDecl != this && typeDecl.isLocalClass() && this.enclosingBlock() == typeDecl.enclosingBlock()) {
            this.error("local class named " + this.name() + " may not be redeclared as a local class in the same block");
         }
      }

      if (!this.packageName().equals("") && this.hasPackage(this.fullName())) {
         this.error("type name conflicts with a package using the same name: " + this.name());
      }

      if (this.hasEnclosingTypeDecl(this.name())) {
         this.error("type may not have the same simple name as an enclosing type declaration");
      }

   }

   protected void ppBodyDecls(StringBuffer s) {
      s.append(" {");

      for(int i = 0; i < this.getNumBodyDecl(); ++i) {
         this.getBodyDecl(i).toString(s);
      }

      s.append(this.indent() + "}");
   }

   public Access createQualifiedAccess() {
      if (!this.isLocalClass() && !this.isAnonymous()) {
         return (Access)(!this.isTopLevelType() ? this.enclosingType().createQualifiedAccess().qualifiesAccess(new TypeAccess(this.name())) : new TypeAccess(this.packageName(), this.name()));
      } else {
         return new TypeAccess(this.name());
      }
   }

   public FieldDeclaration findSingleVariable(String name) {
      return (FieldDeclaration)this.memberFields(name).iterator().next();
   }

   public void refined_TypeHierarchyCheck_TypeDecl_typeCheck() {
      Iterator iter1 = this.localMethodsIterator();

      label165:
      while(iter1.hasNext()) {
         MethodDecl m = (MethodDecl)iter1.next();
         ASTNode target = m.hostType() == this ? m : this;
         Iterator i2 = this.ancestorMethods(m.signature()).iterator();

         while(true) {
            MethodDecl decl;
            int i;
            Access e;
            boolean found;
            int j;
            do {
               if (!i2.hasNext()) {
                  continue label165;
               }

               decl = (MethodDecl)i2.next();
               if (m.overrides(decl)) {
                  if (!m.isStatic() && decl.isStatic()) {
                     ((ASTNode)target).error("an instance method may not override a static method");
                  }

                  if (!m.mayOverrideReturn(decl)) {
                     ((ASTNode)target).error("the return type of method " + m.signature() + " in " + m.hostType().typeName() + " does not match the return type of method " + decl.signature() + " in " + decl.hostType().typeName() + " and may thus not be overriden");
                  }

                  for(i = 0; i < m.getNumException(); ++i) {
                     e = m.getException(i);
                     found = false;

                     for(j = 0; !found && j < decl.getNumException(); ++j) {
                        if (e.type().instanceOf(decl.getException(j).type())) {
                           found = true;
                        }
                     }

                     if (!found && e.type().isUncheckedException()) {
                        ((ASTNode)target).error(m.signature() + " in " + m.hostType().typeName() + " may not throw more checked exceptions than overridden method " + decl.signature() + " in " + decl.hostType().typeName());
                     }
                  }

                  if (decl.isPublic() && !m.isPublic()) {
                     ((ASTNode)target).error("overriding access modifier error");
                  }

                  if (decl.isProtected() && !m.isPublic() && !m.isProtected()) {
                     ((ASTNode)target).error("overriding access modifier error");
                  }

                  if (!decl.isPrivate() && !decl.isProtected() && !decl.isPublic() && m.isPrivate()) {
                     ((ASTNode)target).error("overriding access modifier error");
                  }

                  if (decl.isFinal()) {
                     ((ASTNode)target).error("method " + m.signature() + " in " + this.hostType().typeName() + " can not override final method " + decl.signature() + " in " + decl.hostType().typeName());
                  }
               }
            } while(!m.hides(decl));

            if (m.isStatic() && !decl.isStatic()) {
               ((ASTNode)target).error("a static method may not hide an instance method");
            }

            if (!m.mayOverrideReturn(decl)) {
               ((ASTNode)target).error("can not hide a method with a different return type");
            }

            for(i = 0; i < m.getNumException(); ++i) {
               e = m.getException(i);
               found = false;

               for(j = 0; !found && j < decl.getNumException(); ++j) {
                  if (e.type().instanceOf(decl.getException(j).type())) {
                     found = true;
                  }
               }

               if (!found) {
                  ((ASTNode)target).error("may not throw more checked exceptions than hidden method");
               }
            }

            if (decl.isPublic() && !m.isPublic()) {
               ((ASTNode)target).error("hiding access modifier error: public method " + decl.signature() + " in " + decl.hostType().typeName() + " is hidden by non public method " + m.signature() + " in " + m.hostType().typeName());
            }

            if (decl.isProtected() && !m.isPublic() && !m.isProtected()) {
               ((ASTNode)target).error("hiding access modifier error: protected method " + decl.signature() + " in " + decl.hostType().typeName() + " is hidden by non (public|protected) method " + m.signature() + " in " + m.hostType().typeName());
            }

            if (!decl.isPrivate() && !decl.isProtected() && !decl.isPublic() && m.isPrivate()) {
               ((ASTNode)target).error("hiding access modifier error: default method " + decl.signature() + " in " + decl.hostType().typeName() + " is hidden by private method " + m.signature() + " in " + m.hostType().typeName());
            }

            if (decl.isFinal()) {
               ((ASTNode)target).error("method " + m.signature() + " in " + this.hostType().typeName() + " can not hide final method " + decl.signature() + " in " + decl.hostType().typeName());
            }
         }
      }

   }

   public TypeDecl makeGeneric(Signatures.ClassSignature s) {
      return this;
   }

   public TypeDecl substitute(TypeVariable typeVariable) {
      return (TypeDecl)(this.isTopLevelType() ? typeVariable : this.enclosingType().substitute(typeVariable));
   }

   public Access substitute(Parameterization parTypeDecl) {
      if (parTypeDecl instanceof ParTypeDecl && ((ParTypeDecl)parTypeDecl).genericDecl() == this) {
         return ((TypeDecl)parTypeDecl).createBoundAccess();
      } else {
         return (Access)(this.isTopLevelType() ? this.createBoundAccess() : this.enclosingType().substitute(parTypeDecl).qualifiesAccess(new TypeAccess(this.name())));
      }
   }

   public Access substituteReturnType(Parameterization parTypeDecl) {
      return this.substitute(parTypeDecl);
   }

   public Access substituteParameterType(Parameterization parTypeDecl) {
      return this.substitute(parTypeDecl);
   }

   public boolean hasField(String name) {
      if (!this.memberFields(name).isEmpty()) {
         return true;
      } else {
         for(int i = 0; i < this.getNumBodyDecl(); ++i) {
            if (this.getBodyDecl(i) instanceof FieldDeclaration) {
               FieldDeclaration decl = (FieldDeclaration)this.getBodyDecl(i);
               if (decl.name().equals(name)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean hasMethod(String id) {
      if (!this.memberMethods(id).isEmpty()) {
         return true;
      } else {
         for(int i = 0; i < this.getNumBodyDecl(); ++i) {
            if (this.getBodyDecl(i) instanceof MethodDecl) {
               MethodDecl decl = (MethodDecl)this.getBodyDecl(i);
               if (decl.name().equals(id)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public Collection nestedTypes() {
      return (Collection)(this.nestedTypes != null ? this.nestedTypes : new HashSet());
   }

   public void addNestedType(TypeDecl typeDecl) {
      if (this.nestedTypes == null) {
         this.nestedTypes = new HashSet();
      }

      if (typeDecl != this) {
         this.nestedTypes.add(typeDecl);
      }

   }

   public Collection usedNestedTypes() {
      return (Collection)(this.usedNestedTypes != null ? this.usedNestedTypes : new HashSet());
   }

   public void addUsedNestedType(TypeDecl typeDecl) {
      if (this.usedNestedTypes == null) {
         this.usedNestedTypes = new HashSet();
      }

      this.usedNestedTypes.add(typeDecl);
   }

   public ASTNode getAccessor(ASTNode source, String name) {
      ArrayList key = new ArrayList(2);
      key.add(source);
      key.add(name);
      return this.accessorMap != null && this.accessorMap.containsKey(key) ? (ASTNode)this.accessorMap.get(key) : null;
   }

   public void addAccessor(ASTNode source, String name, ASTNode accessor) {
      ArrayList key = new ArrayList(2);
      key.add(source);
      key.add(name);
      if (this.accessorMap == null) {
         this.accessorMap = new HashMap();
      }

      this.accessorMap.put(key, accessor);
   }

   public ASTNode getAccessorSource(ASTNode accessor) {
      Iterator i = this.accessorMap.entrySet().iterator();

      Entry entry;
      do {
         if (!i.hasNext()) {
            return null;
         }

         entry = (Entry)i.next();
      } while(entry.getValue() != accessor);

      return (ASTNode)((ArrayList)entry.getKey()).get(0);
   }

   public void addEnclosingVariables() {
      if (this.addEnclosingVariables) {
         this.addEnclosingVariables = false;
         Iterator iter = this.enclosingVariables().iterator();

         while(iter.hasNext()) {
            Variable v = (Variable)iter.next();
            Modifiers m = new Modifiers();
            m.addModifier(new Modifier("public"));
            m.addModifier(new Modifier("synthetic"));
            m.addModifier(new Modifier("final"));
            this.addMemberField(new FieldDeclaration(m, v.type().createQualifiedAccess(), "val$" + v.name(), new Opt()));
         }

      }
   }

   public FieldDeclaration createAssertionsDisabled() {
      if (this.createAssertionsDisabled != null) {
         return this.createAssertionsDisabled;
      } else {
         this.createAssertionsDisabled = new FieldDeclaration(new Modifiers((new List()).add(new Modifier("public")).add(new Modifier("static")).add(new Modifier("final"))), new PrimitiveTypeAccess("boolean"), "$assertionsDisabled", new Opt(new LogNotExpr(this.topLevelType().createQualifiedAccess().qualifiesAccess((new ClassAccess()).qualifiesAccess(new MethodAccess("desiredAssertionStatus", new List()))))));
         this.getBodyDeclList().insertChild(this.createAssertionsDisabled, 0);
         this.createAssertionsDisabled = (FieldDeclaration)this.getBodyDeclList().getChild(0);
         this.createAssertionsDisabled.transformation();
         return this.createAssertionsDisabled;
      }
   }

   public FieldDeclaration createStaticClassField(String name) {
      if (this.createStaticClassField == null) {
         this.createStaticClassField = new HashMap();
      }

      if (this.createStaticClassField.containsKey(name)) {
         return (FieldDeclaration)this.createStaticClassField.get(name);
      } else {
         FieldDeclaration f = new FieldDeclaration(new Modifiers((new List()).add(new Modifier("public")).add(new Modifier("static"))), this.lookupType("java.lang", "Class").createQualifiedAccess(), name, new Opt()) {
            public boolean isConstant() {
               return true;
            }
         };
         this.createStaticClassField.put(name, f);
         return this.addMemberField(f);
      }
   }

   public MethodDecl createStaticClassMethod() {
      if (this.createStaticClassMethod != null) {
         return this.createStaticClassMethod;
      } else {
         this.createStaticClassMethod = new MethodDecl(new Modifiers((new List()).add(new Modifier("public")).add(new Modifier("static"))), this.lookupType("java.lang", "Class").createQualifiedAccess(), "class$", (new List()).add(new ParameterDeclaration(new Modifiers(new List()), this.lookupType("java.lang", "String").createQualifiedAccess(), "name")), new List(), new Opt(new Block((new List()).add(new TryStmt(new Block((new List()).add(new ReturnStmt(new Opt(this.lookupType("java.lang", "Class").createQualifiedAccess().qualifiesAccess(new MethodAccess("forName", (new List()).add(new VarAccess("name")))))))), (new List()).add(new BasicCatch(new ParameterDeclaration(new Modifiers(new List()), this.lookupType("java.lang", "ClassNotFoundException").createQualifiedAccess(), "e"), new Block((new List()).add(new ThrowStmt(new ClassInstanceExpr(this.lookupType("java.lang", "NoClassDefFoundError").createQualifiedAccess(), (new List()).add((new VarAccess("e")).qualifiesAccess(new MethodAccess("getMessage", new List()))), new Opt())))))), new Opt()))))) {
            public boolean isConstant() {
               return true;
            }
         };
         return this.addMemberMethod(this.createStaticClassMethod);
      }
   }

   public void transformation() {
      this.addEnclosingVariables();
      super.transformation();
      if (this.isNestedType()) {
         this.enclosingType().addNestedType(this);
      }

   }

   public void jimplify1phase2() {
      if (this.needsClinit() && !this.getSootClassDecl().declaresMethod("<clinit>", new ArrayList())) {
         this.clinit = Scene.v().makeSootMethod("<clinit>", new ArrayList(), soot.VoidType.v(), 8, new ArrayList());
         this.getSootClassDecl().addMethod(this.clinit);
      }

      Iterator iter = this.nestedTypes().iterator();

      while(iter.hasNext()) {
         TypeDecl typeDecl = (TypeDecl)iter.next();
         typeDecl.jimplify1phase2();
      }

      for(int i = 0; i < this.getNumBodyDecl(); ++i) {
         if (this.getBodyDecl(i).generate()) {
            this.getBodyDecl(i).jimplify1phase2();
         }
      }

      this.addAttributes();
   }

   public Value emitCastTo(Body b, Value v, TypeDecl type, ASTNode location) {
      if (this == type) {
         return v;
      } else if (this.isReferenceType() && type.isReferenceType() && this.instanceOf(type)) {
         return v;
      } else if ((this.isLong() || this instanceof FloatingPointType) && type.isIntegralType()) {
         Value v = b.newCastExpr(this.asImmediate(b, v), this.typeInt().getSootType(), location);
         return this.typeInt().emitCastTo(b, v, type, location);
      } else {
         return b.newCastExpr(this.asImmediate(b, v), type.getSootType(), location);
      }
   }

   public void jimplify2clinit() {
      SootMethod m = this.clinit;
      JimpleBody body = Jimple.v().newBody(m);
      m.setActiveBody(body);
      Body b = new Body(this, body, this);

      for(int i = 0; i < this.getNumBodyDecl(); ++i) {
         BodyDecl bodyDecl = this.getBodyDecl(i);
         if (bodyDecl instanceof FieldDeclaration && bodyDecl.generate()) {
            FieldDeclaration f = (FieldDeclaration)bodyDecl;
            if (f.isStatic() && f.hasInit()) {
               Local l = this.asLocal(b, f.getInit().type().emitCastTo(b, f.getInit(), f.type()), f.type().getSootType());
               b.setLine(f);
               b.add(b.newAssignStmt(b.newStaticFieldRef(f.sootRef(), f), l, f));
            }
         } else if (bodyDecl instanceof StaticInitializer && bodyDecl.generate()) {
            bodyDecl.jimplify2(b);
         }
      }

      b.add(b.newReturnVoidStmt((ASTNode)null));
   }

   public void jimplify2() {
      super.jimplify2();
      if (this.clinit != null) {
         this.jimplify2clinit();
      }

      Iterator iter = this.nestedTypes().iterator();

      while(iter.hasNext()) {
         TypeDecl typeDecl = (TypeDecl)iter.next();
         typeDecl.jimplify2();
      }

      ArrayList tags = new ArrayList();
      Iterator iter = this.innerClassesAttributeEntries().iterator();

      while(iter.hasNext()) {
         TypeDecl type = (TypeDecl)iter.next();
         tags.add(new InnerClassTag(type.jvmName().replace('.', '/'), type.isMemberType() ? type.enclosingType().jvmName().replace('.', '/') : null, type.isAnonymous() ? null : type.name(), type.sootTypeModifiers()));
      }

      if (!tags.isEmpty()) {
         this.getSootClassDecl().addTag(new InnerClassAttribute(tags));
      }

      this.addAttributes();
      this.getSootClassDecl().setResolvingLevel(3);
   }

   public void addAttributes() {
      super.addAttributes();
      ArrayList c = new ArrayList();
      this.getModifiers().addRuntimeVisibleAnnotationsAttribute(c);
      this.getModifiers().addRuntimeInvisibleAnnotationsAttribute(c);
      this.getModifiers().addSourceOnlyAnnotations(c);
      Iterator iter = c.iterator();

      while(iter.hasNext()) {
         Tag tag = (Tag)iter.next();
         this.getSootClassDecl().addTag(tag);
      }

   }

   protected Value emitBoxingOperation(Body b, Value v, ASTNode location) {
      ArrayList parameters = new ArrayList();
      parameters.add(this.unboxed().getSootType());
      SootMethodRef ref = Scene.v().makeMethodRef(this.getSootClassDecl(), "valueOf", parameters, this.getSootType(), true);
      ArrayList args = new ArrayList();
      args.add(this.asLocal(b, v));
      return b.newStaticInvokeExpr(ref, (java.util.List)args, location);
   }

   protected Value emitUnboxingOperation(Body b, Value v, ASTNode location) {
      SootMethodRef ref = Scene.v().makeMethodRef(this.getSootClassDecl(), this.unboxed().name() + "Value", new ArrayList(), this.unboxed().getSootType(), false);
      return b.newVirtualInvokeExpr(this.asLocal(b, v), ref, (java.util.List)(new ArrayList()), location);
   }

   public TypeDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
      this.setChild(new List(), 1);
   }

   public TypeDecl(Modifiers p0, String p1, List<BodyDecl> p2) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
   }

   public TypeDecl(Modifiers p0, Symbol p1, List<BodyDecl> p2) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
   }

   protected int numChildren() {
      return 2;
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

   public void setBodyDeclList(List<BodyDecl> list) {
      this.setChild(list, 1);
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
      List<BodyDecl> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<BodyDecl> getBodyDeclListNoTransform() {
      return (List)this.getChildNoTransform(1);
   }

   public void typeCheck() {
      this.refined_TypeHierarchyCheck_TypeDecl_typeCheck();
      ArrayList list = new ArrayList();
      list.addAll(this.implementedInterfaces());

      for(int i = 0; i < list.size(); ++i) {
         InterfaceDecl decl = (InterfaceDecl)list.get(i);
         if (decl instanceof ParInterfaceDecl) {
            ParInterfaceDecl p = (ParInterfaceDecl)decl;
            ListIterator i2 = list.listIterator(i);

            while(i2.hasNext()) {
               InterfaceDecl decl2 = (InterfaceDecl)i2.next();
               if (decl2 instanceof ParInterfaceDecl) {
                  ParInterfaceDecl q = (ParInterfaceDecl)decl2;
                  if (p != q && p.genericDecl() == q.genericDecl() && !p.sameArgument(q)) {
                     this.error(p.genericDecl().name() + " cannot be inherited with different arguments: " + p.typeName() + " and " + q.typeName());
                  }
               }
            }
         }
      }

   }

   public Value emitCastTo(Body b, Expr expr, TypeDecl type) {
      if (!(type instanceof LUBType) && !(type instanceof GLBType) && !(type instanceof AbstractWildcardType)) {
         if (expr.isConstant() && this.isPrimitive() && type.isReferenceType()) {
            return this.boxed().emitBoxingOperation(b, emitConstant(this.cast(expr.constant())), expr);
         }

         if (expr.isConstant() && !expr.type().isEnumDecl()) {
            if (type.isPrimitive()) {
               return emitConstant(type.cast(expr.constant()));
            }

            return emitConstant(expr.constant());
         }
      } else {
         type = this.typeObject();
      }

      return this.emitCastTo(b, expr.eval(b), type, expr);
   }

   private boolean refined_TypeConversion_TypeDecl_assignConversionTo_TypeDecl_Expr(TypeDecl type, Expr expr) {
      boolean sourceIsConstant = expr != null ? expr.isConstant() : false;
      if (!this.identityConversionTo(type) && !this.wideningConversionTo(type)) {
         return sourceIsConstant && (this.isInt() || this.isChar() || this.isShort() || this.isByte()) && (type.isByte() || type.isShort() || type.isChar()) && this.narrowingConversionTo(type) && expr.representableIn(type);
      } else {
         return true;
      }
   }

   private boolean refined_TypeConversion_TypeDecl_methodInvocationConversionTo_TypeDecl(TypeDecl type) {
      return this.identityConversionTo(type) || this.wideningConversionTo(type);
   }

   private boolean refined_TypeConversion_TypeDecl_castingConversionTo_TypeDecl(TypeDecl type) {
      return this.identityConversionTo(type) || this.wideningConversionTo(type) || this.narrowingConversionTo(type);
   }

   private SootClass refined_EmitJimple_TypeDecl_getSootClassDecl() {
      if (this.compilationUnit().fromSource()) {
         return this.sootClass();
      } else {
         if (this.options().verbose()) {
            System.out.println("Loading .class file " + this.jvmName());
         }

         SootClass sc = Scene.v().loadClass(this.jvmName(), 2);
         sc.setLibraryClass();
         return sc;
      }
   }

   private Type refined_EmitJimple_TypeDecl_getSootType() {
      return this.getSootClassDecl().getType();
   }

   private SootClass refined_EmitJimple_TypeDecl_sootClass() {
      return null;
   }

   public Constant cast(Constant c) {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation cast not supported for type " + this.getClass().getName());
   }

   public Constant plus(Constant c) {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation plus not supported for type " + this.getClass().getName());
   }

   public Constant minus(Constant c) {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation minus not supported for type " + this.getClass().getName());
   }

   public Constant bitNot(Constant c) {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation bitNot not supported for type " + this.getClass().getName());
   }

   public Constant mul(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation mul not supported for type " + this.getClass().getName());
   }

   public Constant div(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation div not supported for type " + this.getClass().getName());
   }

   public Constant mod(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation mod not supported for type " + this.getClass().getName());
   }

   public Constant add(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation add not supported for type " + this.getClass().getName());
   }

   public Constant sub(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation sub not supported for type " + this.getClass().getName());
   }

   public Constant lshift(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation lshift not supported for type " + this.getClass().getName());
   }

   public Constant rshift(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation rshift not supported for type " + this.getClass().getName());
   }

   public Constant urshift(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation urshift not supported for type " + this.getClass().getName());
   }

   public Constant andBitwise(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation andBitwise not supported for type " + this.getClass().getName());
   }

   public Constant xorBitwise(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation xorBitwise not supported for type " + this.getClass().getName());
   }

   public Constant orBitwise(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation orBitwise not supported for type " + this.getClass().getName());
   }

   public Constant questionColon(Constant cond, Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation questionColon not supported for type " + this.getClass().getName());
   }

   public boolean eqIsTrue(Expr left, Expr right) {
      ASTNode$State state = this.state();
      System.err.println("Evaluation eqIsTrue for unknown type: " + this.getClass().getName());
      return false;
   }

   public boolean ltIsTrue(Expr left, Expr right) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean leIsTrue(Expr left, Expr right) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean accessibleFromPackage(String packageName) {
      if (this.accessibleFromPackage_String_values == null) {
         this.accessibleFromPackage_String_values = new HashMap(4);
      }

      if (this.accessibleFromPackage_String_values.containsKey(packageName)) {
         return (Boolean)this.accessibleFromPackage_String_values.get(packageName);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean accessibleFromPackage_String_value = this.accessibleFromPackage_compute(packageName);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.accessibleFromPackage_String_values.put(packageName, accessibleFromPackage_String_value);
         }

         return accessibleFromPackage_String_value;
      }
   }

   private boolean accessibleFromPackage_compute(String packageName) {
      return !this.isPrivate() && (this.isPublic() || this.hostPackage().equals(packageName));
   }

   public boolean accessibleFromExtend(TypeDecl type) {
      if (this.accessibleFromExtend_TypeDecl_values == null) {
         this.accessibleFromExtend_TypeDecl_values = new HashMap(4);
      }

      if (this.accessibleFromExtend_TypeDecl_values.containsKey(type)) {
         return (Boolean)this.accessibleFromExtend_TypeDecl_values.get(type);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean accessibleFromExtend_TypeDecl_value = this.accessibleFromExtend_compute(type);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.accessibleFromExtend_TypeDecl_values.put(type, accessibleFromExtend_TypeDecl_value);
         }

         return accessibleFromExtend_TypeDecl_value;
      }
   }

   private boolean accessibleFromExtend_compute(TypeDecl type) {
      if (type == this) {
         return true;
      } else if (this.isInnerType() && !this.enclosingType().accessibleFrom(type)) {
         return false;
      } else if (this.isPublic()) {
         return true;
      } else if (this.isProtected()) {
         if (this.hostPackage().equals(type.hostPackage())) {
            return true;
         } else {
            return type.isNestedType() && type.enclosingType().withinBodyThatSubclasses(this.enclosingType()) != null;
         }
      } else if (this.isPrivate()) {
         return this.topLevelType() == type.topLevelType();
      } else {
         return this.hostPackage().equals(type.hostPackage());
      }
   }

   public boolean accessibleFrom(TypeDecl type) {
      if (this.accessibleFrom_TypeDecl_values == null) {
         this.accessibleFrom_TypeDecl_values = new HashMap(4);
      }

      if (this.accessibleFrom_TypeDecl_values.containsKey(type)) {
         return (Boolean)this.accessibleFrom_TypeDecl_values.get(type);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean accessibleFrom_TypeDecl_value = this.accessibleFrom_compute(type);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.accessibleFrom_TypeDecl_values.put(type, accessibleFrom_TypeDecl_value);
         }

         return accessibleFrom_TypeDecl_value;
      }
   }

   private boolean accessibleFrom_compute(TypeDecl type) {
      if (type == this) {
         return true;
      } else if (this.isInnerType() && !this.enclosingType().accessibleFrom(type)) {
         return false;
      } else if (this.isPublic()) {
         return true;
      } else if (this.isProtected()) {
         if (this.hostPackage().equals(type.hostPackage())) {
            return true;
         } else {
            if (this.isMemberType()) {
               TypeDecl typeDecl;
               for(typeDecl = type; typeDecl != null && !typeDecl.instanceOf(this.enclosingType()); typeDecl = typeDecl.enclosingType()) {
               }

               if (typeDecl != null) {
                  return true;
               }
            }

            return false;
         }
      } else if (this.isPrivate()) {
         return this.topLevelType() == type.topLevelType();
      } else {
         return this.hostPackage().equals(type.hostPackage());
      }
   }

   public int dimension() {
      if (this.dimension_computed) {
         return this.dimension_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.dimension_value = this.dimension_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.dimension_computed = true;
         }

         return this.dimension_value;
      }
   }

   private int dimension_compute() {
      return 0;
   }

   public TypeDecl elementType() {
      if (this.elementType_computed) {
         return this.elementType_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.elementType_value = this.elementType_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.elementType_computed = true;
         }

         return this.elementType_value;
      }
   }

   private TypeDecl elementType_compute() {
      return this;
   }

   public TypeDecl arrayType() {
      if (this.arrayType_computed) {
         return this.arrayType_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.arrayType_value = this.arrayType_compute();
         this.arrayType_value.setParent(this);
         this.arrayType_value.is$Final = true;
         this.arrayType_computed = true;
         return this.arrayType_value;
      }
   }

   private TypeDecl arrayType_compute() {
      String name = this.name() + "[]";
      List body = new List();
      body.add(new FieldDeclaration(new Modifiers((new List()).add(new Modifier("public")).add(new Modifier("final"))), new PrimitiveTypeAccess("int"), "length", new Opt()));
      MethodDecl clone = null;
      TypeDecl typeObject = this.typeObject();

      for(int i = 0; clone == null && i < typeObject.getNumBodyDecl(); ++i) {
         if (typeObject.getBodyDecl(i) instanceof MethodDecl) {
            MethodDecl m = (MethodDecl)typeObject.getBodyDecl(i);
            if (m.name().equals("clone")) {
               clone = m;
            }
         }
      }

      if (clone != null) {
         body.add(new MethodDeclSubstituted(new Modifiers((new List()).add(new Modifier("public"))), new ArrayTypeAccess(this.createQualifiedAccess()), "clone", new List(), new List(), new Opt(new Block()), (MethodDecl)this.typeObject().memberMethods("clone").iterator().next()));
      }

      TypeDecl typeDecl = new ArrayDecl(new Modifiers((new List()).add(new Modifier("public"))), name, new Opt(this.typeObject().createQualifiedAccess()), (new List()).add(this.typeCloneable().createQualifiedAccess()).add(this.typeSerializable().createQualifiedAccess()), body);
      return typeDecl;
   }

   public int size() {
      ASTNode$State state = this.state();
      return 1;
   }

   public boolean isEmpty() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean contains(Object o) {
      ASTNode$State state = this.state();
      return this == o;
   }

   public boolean isException() {
      if (this.isException_computed) {
         return this.isException_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isException_value = this.isException_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isException_computed = true;
         }

         return this.isException_value;
      }
   }

   private boolean isException_compute() {
      return this.instanceOf(this.typeException());
   }

   public boolean isCheckedException() {
      if (this.isCheckedException_computed) {
         return this.isCheckedException_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isCheckedException_value = this.isCheckedException_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isCheckedException_computed = true;
         }

         return this.isCheckedException_value;
      }
   }

   private boolean isCheckedException_compute() {
      return this.isException() && (this.instanceOf(this.typeRuntimeException()) || this.instanceOf(this.typeError()));
   }

   public boolean isUncheckedException() {
      if (this.isUncheckedException_computed) {
         return this.isUncheckedException_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isUncheckedException_value = this.isUncheckedException_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isUncheckedException_computed = true;
         }

         return this.isUncheckedException_value;
      }
   }

   private boolean isUncheckedException_compute() {
      return this.isException() && !this.isCheckedException();
   }

   public boolean mayCatch(TypeDecl thrownType) {
      if (this.mayCatch_TypeDecl_values == null) {
         this.mayCatch_TypeDecl_values = new HashMap(4);
      }

      if (this.mayCatch_TypeDecl_values.containsKey(thrownType)) {
         return (Boolean)this.mayCatch_TypeDecl_values.get(thrownType);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean mayCatch_TypeDecl_value = this.mayCatch_compute(thrownType);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.mayCatch_TypeDecl_values.put(thrownType, mayCatch_TypeDecl_value);
         }

         return mayCatch_TypeDecl_value;
      }
   }

   private boolean mayCatch_compute(TypeDecl thrownType) {
      return thrownType.instanceOf(this) || this.instanceOf(thrownType);
   }

   public Collection lookupSuperConstructor() {
      ASTNode$State state = this.state();
      return Collections.EMPTY_LIST;
   }

   public Collection constructors() {
      if (this.constructors_computed) {
         return this.constructors_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.constructors_value = this.constructors_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.constructors_computed = true;
         }

         return this.constructors_value;
      }
   }

   private Collection constructors_compute() {
      Collection c = new ArrayList();

      for(int i = 0; i < this.getNumBodyDecl(); ++i) {
         if (this.getBodyDecl(i) instanceof ConstructorDecl) {
            c.add(this.getBodyDecl(i));
         }
      }

      return c;
   }

   public Collection unqualifiedLookupMethod(String name) {
      if (this.unqualifiedLookupMethod_String_values == null) {
         this.unqualifiedLookupMethod_String_values = new HashMap(4);
      }

      if (this.unqualifiedLookupMethod_String_values.containsKey(name)) {
         return (Collection)this.unqualifiedLookupMethod_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         Collection unqualifiedLookupMethod_String_value = this.unqualifiedLookupMethod_compute(name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.unqualifiedLookupMethod_String_values.put(name, unqualifiedLookupMethod_String_value);
         }

         return unqualifiedLookupMethod_String_value;
      }
   }

   private Collection unqualifiedLookupMethod_compute(String name) {
      Collection c = this.memberMethods(name);
      if (!c.isEmpty()) {
         return c;
      } else {
         return this.isInnerType() ? this.lookupMethod(name) : removeInstanceMethods(this.lookupMethod(name));
      }
   }

   public Collection memberMethods(String name) {
      ASTNode$State state = this.state();
      Collection c = (Collection)this.methodsNameMap().get(name);
      return (Collection)(c != null ? c : Collections.EMPTY_LIST);
   }

   public HashMap methodsNameMap() {
      if (this.methodsNameMap_computed) {
         return this.methodsNameMap_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.methodsNameMap_value = this.methodsNameMap_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.methodsNameMap_computed = true;
         }

         return this.methodsNameMap_value;
      }
   }

   private HashMap methodsNameMap_compute() {
      HashMap map = new HashMap();

      MethodDecl m;
      ArrayList list;
      for(Iterator iter = this.methodsIterator(); iter.hasNext(); list.add(m)) {
         m = (MethodDecl)iter.next();
         list = (ArrayList)map.get(m.name());
         if (list == null) {
            list = new ArrayList(4);
            map.put(m.name(), list);
         }
      }

      return map;
   }

   public SimpleSet localMethodsSignature(String signature) {
      ASTNode$State state = this.state();
      SimpleSet set = (SimpleSet)this.localMethodsSignatureMap().get(signature);
      return set != null ? set : SimpleSet.emptySet;
   }

   public HashMap localMethodsSignatureMap() {
      if (this.localMethodsSignatureMap_computed) {
         return this.localMethodsSignatureMap_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.localMethodsSignatureMap_value = this.localMethodsSignatureMap_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.localMethodsSignatureMap_computed = true;
         }

         return this.localMethodsSignatureMap_value;
      }
   }

   private HashMap localMethodsSignatureMap_compute() {
      HashMap map = new HashMap(this.getNumBodyDecl());

      for(int i = 0; i < this.getNumBodyDecl(); ++i) {
         if (this.getBodyDecl(i) instanceof MethodDecl) {
            MethodDecl decl = (MethodDecl)this.getBodyDecl(i);
            map.put(decl.signature(), decl);
         }
      }

      return map;
   }

   public SimpleSet methodsSignature(String signature) {
      ASTNode$State state = this.state();
      SimpleSet set = (SimpleSet)this.methodsSignatureMap().get(signature);
      return set != null ? set : SimpleSet.emptySet;
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
      return this.localMethodsSignatureMap();
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
      return SimpleSet.emptySet;
   }

   public boolean hasType(String name) {
      ASTNode$State state = this.state();
      return !this.memberTypes(name).isEmpty();
   }

   public SimpleSet localTypeDecls(String name) {
      if (this.localTypeDecls_String_values == null) {
         this.localTypeDecls_String_values = new HashMap(4);
      }

      if (this.localTypeDecls_String_values.containsKey(name)) {
         return (SimpleSet)this.localTypeDecls_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet localTypeDecls_String_value = this.localTypeDecls_compute(name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.localTypeDecls_String_values.put(name, localTypeDecls_String_value);
         }

         return localTypeDecls_String_value;
      }
   }

   private SimpleSet localTypeDecls_compute(String name) {
      SimpleSet set = SimpleSet.emptySet;

      for(int i = 0; i < this.getNumBodyDecl(); ++i) {
         if (this.getBodyDecl(i).declaresType(name)) {
            set = set.add(this.getBodyDecl(i).type(name));
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
      return SimpleSet.emptySet;
   }

   public SimpleSet localFields(String name) {
      if (this.localFields_String_values == null) {
         this.localFields_String_values = new HashMap(4);
      }

      if (this.localFields_String_values.containsKey(name)) {
         return (SimpleSet)this.localFields_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet localFields_String_value = this.localFields_compute(name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.localFields_String_values.put(name, localFields_String_value);
         }

         return localFields_String_value;
      }
   }

   private SimpleSet localFields_compute(String name) {
      return this.localFieldsMap().containsKey(name) ? (SimpleSet)this.localFieldsMap().get(name) : SimpleSet.emptySet;
   }

   public HashMap localFieldsMap() {
      if (this.localFieldsMap_computed) {
         return this.localFieldsMap_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.localFieldsMap_value = this.localFieldsMap_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.localFieldsMap_computed = true;
         }

         return this.localFieldsMap_value;
      }
   }

   private HashMap localFieldsMap_compute() {
      HashMap map = new HashMap();

      for(int i = 0; i < this.getNumBodyDecl(); ++i) {
         if (this.getBodyDecl(i) instanceof FieldDeclaration) {
            FieldDeclaration decl = (FieldDeclaration)this.getBodyDecl(i);
            SimpleSet fields = (SimpleSet)map.get(decl.name());
            if (fields == null) {
               fields = SimpleSet.emptySet;
            }

            fields = fields.add(decl);
            map.put(decl.name(), fields);
         }
      }

      return map;
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
      return this.localFieldsMap();
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
      return this.localFields(name);
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
      return false;
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
      return Collections.EMPTY_LIST;
   }

   public boolean isPublic() {
      if (this.isPublic_computed) {
         return this.isPublic_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isPublic_value = this.isPublic_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isPublic_computed = true;
         }

         return this.isPublic_value;
      }
   }

   private boolean isPublic_compute() {
      return this.getModifiers().isPublic() || this.isMemberType() && this.enclosingType().isInterfaceDecl();
   }

   public boolean isPrivate() {
      ASTNode$State state = this.state();
      return this.getModifiers().isPrivate();
   }

   public boolean isProtected() {
      ASTNode$State state = this.state();
      return this.getModifiers().isProtected();
   }

   public boolean isAbstract() {
      ASTNode$State state = this.state();
      return this.getModifiers().isAbstract();
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
      return this.getModifiers().isStatic() || this.isMemberType() && this.enclosingType().isInterfaceDecl();
   }

   public boolean isFinal() {
      ASTNode$State state = this.state();
      return this.getModifiers().isFinal();
   }

   public boolean isStrictfp() {
      ASTNode$State state = this.state();
      return this.getModifiers().isStrictfp();
   }

   public boolean isSynthetic() {
      ASTNode$State state = this.state();
      return this.getModifiers().isSynthetic();
   }

   public boolean hasEnclosingTypeDecl(String name) {
      ASTNode$State state = this.state();
      TypeDecl enclosingType = this.enclosingType();
      if (enclosingType == null) {
         return false;
      } else {
         return enclosingType.name().equals(name) || enclosingType.hasEnclosingTypeDecl(name);
      }
   }

   public boolean assignableToInt() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean addsIndentationLevel() {
      ASTNode$State state = this.state();
      return true;
   }

   public String dumpString() {
      ASTNode$State state = this.state();
      return this.getClass().getName() + " [" + this.getID() + "]";
   }

   public String name() {
      ASTNode$State state = this.state();
      return this.getID();
   }

   public String fullName() {
      if (this.fullName_computed) {
         return this.fullName_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.fullName_value = this.fullName_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.fullName_computed = true;
         }

         return this.fullName_value;
      }
   }

   private String fullName_compute() {
      if (this.isNestedType()) {
         return this.enclosingType().fullName() + "." + this.name();
      } else {
         String packageName = this.packageName();
         return packageName.equals("") ? this.name() : packageName + "." + this.name();
      }
   }

   public String typeName() {
      if (this.typeName_computed) {
         return this.typeName_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeName_value = this.typeName_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeName_computed = true;
         }

         return this.typeName_value;
      }
   }

   private String typeName_compute() {
      if (this.isNestedType()) {
         return this.enclosingType().typeName() + "." + this.name();
      } else {
         String packageName = this.packageName();
         return !packageName.equals("") && !packageName.equals("@primitive") ? packageName + "." + this.name() : this.name();
      }
   }

   public boolean identityConversionTo(TypeDecl type) {
      ASTNode$State state = this.state();
      return this == type;
   }

   public boolean wideningConversionTo(TypeDecl type) {
      ASTNode$State state = this.state();
      return this.instanceOf(type);
   }

   public boolean narrowingConversionTo(TypeDecl type) {
      if (this.narrowingConversionTo_TypeDecl_values == null) {
         this.narrowingConversionTo_TypeDecl_values = new HashMap(4);
      }

      if (this.narrowingConversionTo_TypeDecl_values.containsKey(type)) {
         return (Boolean)this.narrowingConversionTo_TypeDecl_values.get(type);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean narrowingConversionTo_TypeDecl_value = this.narrowingConversionTo_compute(type);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.narrowingConversionTo_TypeDecl_values.put(type, narrowingConversionTo_TypeDecl_value);
         }

         return narrowingConversionTo_TypeDecl_value;
      }
   }

   private boolean narrowingConversionTo_compute(TypeDecl type) {
      return this.instanceOf(type);
   }

   public boolean stringConversion() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean assignConversionTo(TypeDecl type, Expr expr) {
      ASTNode$State state = this.state();
      if (this.refined_TypeConversion_TypeDecl_assignConversionTo_TypeDecl_Expr(type, expr)) {
         return true;
      } else {
         boolean canBoxThis = this instanceof PrimitiveType;
         boolean canBoxType = type instanceof PrimitiveType;
         boolean canUnboxThis = !this.unboxed().isUnknown();
         boolean canUnboxType = !type.unboxed().isUnknown();
         TypeDecl t = !canUnboxThis && canUnboxType ? type.unboxed() : type;
         boolean sourceIsConstant = expr != null ? expr.isConstant() : false;
         if (sourceIsConstant && (this.isInt() || this.isChar() || this.isShort() || this.isByte()) && (t.isByte() || t.isShort() || t.isChar()) && this.narrowingConversionTo(t) && expr.representableIn(t)) {
            return true;
         } else if (canBoxThis && !canBoxType && this.boxed().wideningConversionTo(type)) {
            return true;
         } else {
            return canUnboxThis && !canUnboxType && this.unboxed().wideningConversionTo(type);
         }
      }
   }

   public boolean methodInvocationConversionTo(TypeDecl type) {
      if (this.methodInvocationConversionTo_TypeDecl_values == null) {
         this.methodInvocationConversionTo_TypeDecl_values = new HashMap(4);
      }

      if (this.methodInvocationConversionTo_TypeDecl_values.containsKey(type)) {
         return (Boolean)this.methodInvocationConversionTo_TypeDecl_values.get(type);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean methodInvocationConversionTo_TypeDecl_value = this.methodInvocationConversionTo_compute(type);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.methodInvocationConversionTo_TypeDecl_values.put(type, methodInvocationConversionTo_TypeDecl_value);
         }

         return methodInvocationConversionTo_TypeDecl_value;
      }
   }

   private boolean methodInvocationConversionTo_compute(TypeDecl type) {
      if (this.refined_TypeConversion_TypeDecl_methodInvocationConversionTo_TypeDecl(type)) {
         return true;
      } else {
         boolean canBoxThis = this instanceof PrimitiveType;
         boolean canBoxType = type instanceof PrimitiveType;
         boolean canUnboxThis = !this.unboxed().isUnknown();
         boolean canUnboxType = !type.unboxed().isUnknown();
         if (canBoxThis && !canBoxType) {
            return this.boxed().wideningConversionTo(type);
         } else {
            return canUnboxThis && !canUnboxType ? this.unboxed().wideningConversionTo(type) : false;
         }
      }
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
      if (this.refined_TypeConversion_TypeDecl_castingConversionTo_TypeDecl(type)) {
         return true;
      } else {
         boolean canBoxThis = this instanceof PrimitiveType;
         boolean canBoxType = type instanceof PrimitiveType;
         boolean canUnboxThis = !this.unboxed().isUnknown();
         boolean canUnboxType = !type.unboxed().isUnknown();
         if (canBoxThis && !canBoxType) {
            return this.boxed().wideningConversionTo(type);
         } else {
            return canUnboxThis && !canUnboxType ? this.unboxed().wideningConversionTo(type) : false;
         }
      }
   }

   public TypeDecl unaryNumericPromotion() {
      ASTNode$State state = this.state();
      return this;
   }

   public TypeDecl binaryNumericPromotion(TypeDecl type) {
      ASTNode$State state = this.state();
      return this.unknownType();
   }

   public boolean isReferenceType() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isPrimitiveType() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isNumericType() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isIntegralType() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isBoolean() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isByte() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isChar() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isShort() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isInt() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isFloat() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isLong() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isDouble() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isVoid() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isNull() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isClassDecl() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isInterfaceDecl() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isArrayDecl() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isPrimitive() {
      ASTNode$State state = this.state();
      return false;
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
      return false;
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
      return false;
   }

   public boolean isUnknown() {
      ASTNode$State state = this.state();
      return false;
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
      return type == this;
   }

   public boolean isSupertypeOfInterfaceDecl(InterfaceDecl type) {
      ASTNode$State state = this.state();
      return type == this;
   }

   public boolean isSupertypeOfArrayDecl(ArrayDecl type) {
      ASTNode$State state = this.state();
      return this == type;
   }

   public boolean isSupertypeOfPrimitiveType(PrimitiveType type) {
      ASTNode$State state = this.state();
      return type == this;
   }

   public boolean isSupertypeOfNullType(NullType type) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isSupertypeOfVoidType(VoidType type) {
      ASTNode$State state = this.state();
      return false;
   }

   public TypeDecl topLevelType() {
      ASTNode$State state = this.state();
      return this.isTopLevelType() ? this : this.enclosingType().topLevelType();
   }

   public boolean isTopLevelType() {
      ASTNode$State state = this.state();
      return !this.isNestedType();
   }

   public boolean isInnerClass() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isInnerType() {
      ASTNode$State state = this.state();
      return (this.isLocalClass() || this.isAnonymous() || this.isMemberType() && !this.isStatic()) && !this.inStaticContext();
   }

   public boolean isInnerTypeOf(TypeDecl typeDecl) {
      ASTNode$State state = this.state();
      return typeDecl == this || this.isInnerType() && this.enclosingType().isInnerTypeOf(typeDecl);
   }

   public TypeDecl withinBodyThatSubclasses(TypeDecl type) {
      ASTNode$State state = this.state();
      if (this.instanceOf(type)) {
         return this;
      } else {
         return !this.isTopLevelType() ? this.enclosingType().withinBodyThatSubclasses(type) : null;
      }
   }

   public boolean encloses(TypeDecl type) {
      ASTNode$State state = this.state();
      return type.enclosedBy(this);
   }

   public boolean enclosedBy(TypeDecl type) {
      ASTNode$State state = this.state();
      if (this == type) {
         return true;
      } else {
         return this.isTopLevelType() ? false : this.enclosingType().enclosedBy(type);
      }
   }

   public TypeDecl hostType() {
      ASTNode$State state = this.state();
      return this;
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
      return false;
   }

   public boolean isValidAnnotationMethodReturnType() {
      ASTNode$State state = this.state();
      return false;
   }

   public Annotation annotation(TypeDecl typeDecl) {
      ASTNode$State state = this.state();
      return this.getModifiers().annotation(typeDecl);
   }

   public boolean hasAnnotationSuppressWarnings(String s) {
      ASTNode$State state = this.state();
      return this.getModifiers().hasAnnotationSuppressWarnings(s);
   }

   public boolean isDeprecated() {
      ASTNode$State state = this.state();
      return this.getModifiers().hasDeprecatedAnnotation();
   }

   public boolean commensurateWith(ElementValue value) {
      ASTNode$State state = this.state();
      return value.commensurateWithTypeDecl(this);
   }

   public boolean isAnnotationDecl() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean boxingConversionTo(TypeDecl typeDecl) {
      ASTNode$State state = this.state();
      return false;
   }

   public TypeDecl boxed() {
      if (this.boxed_computed) {
         return this.boxed_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.boxed_value = this.boxed_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.boxed_computed = true;
         }

         return this.boxed_value;
      }
   }

   private TypeDecl boxed_compute() {
      return this.unknownType();
   }

   public boolean unboxingConversionTo(TypeDecl typeDecl) {
      ASTNode$State state = this.state();
      return false;
   }

   public TypeDecl unboxed() {
      if (this.unboxed_computed) {
         return this.unboxed_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.unboxed_value = this.unboxed_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.unboxed_computed = true;
         }

         return this.unboxed_value;
      }
   }

   private TypeDecl unboxed_compute() {
      return this.unknownType();
   }

   public boolean isIterable() {
      if (this.isIterable_computed) {
         return this.isIterable_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isIterable_value = this.isIterable_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isIterable_computed = true;
         }

         return this.isIterable_value;
      }
   }

   private boolean isIterable_compute() {
      return this.instanceOf(this.lookupType("java.lang", "Iterable"));
   }

   public boolean isEnumDecl() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isUnboxedPrimitive() {
      ASTNode$State state = this.state();
      return this instanceof PrimitiveType && this.isPrimitive();
   }

   public boolean involvesTypeParameters() {
      if (this.involvesTypeParameters_computed) {
         return this.involvesTypeParameters_value;
      } else {
         ASTNode$State state = this.state();
         if (!this.involvesTypeParameters_initialized) {
            this.involvesTypeParameters_initialized = true;
            this.involvesTypeParameters_value = false;
         }

         if (state.IN_CIRCLE) {
            if (this.involvesTypeParameters_visited != state.CIRCLE_INDEX) {
               this.involvesTypeParameters_visited = state.CIRCLE_INDEX;
               if (state.RESET_CYCLE) {
                  this.involvesTypeParameters_computed = false;
                  this.involvesTypeParameters_initialized = false;
                  this.involvesTypeParameters_visited = -1;
                  return this.involvesTypeParameters_value;
               } else {
                  boolean new_involvesTypeParameters_value = this.involvesTypeParameters_compute();
                  if (new_involvesTypeParameters_value != this.involvesTypeParameters_value) {
                     state.CHANGE = true;
                  }

                  this.involvesTypeParameters_value = new_involvesTypeParameters_value;
                  return this.involvesTypeParameters_value;
               }
            } else {
               return this.involvesTypeParameters_value;
            }
         } else {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = this.is$Final();

            do {
               this.involvesTypeParameters_visited = state.CIRCLE_INDEX;
               state.CHANGE = false;
               boolean new_involvesTypeParameters_value = this.involvesTypeParameters_compute();
               if (new_involvesTypeParameters_value != this.involvesTypeParameters_value) {
                  state.CHANGE = true;
               }

               this.involvesTypeParameters_value = new_involvesTypeParameters_value;
               ++state.CIRCLE_INDEX;
            } while(state.CHANGE);

            if (isFinal && num == this.state().boundariesCrossed) {
               this.involvesTypeParameters_computed = true;
            } else {
               state.RESET_CYCLE = true;
               this.involvesTypeParameters_compute();
               state.RESET_CYCLE = false;
               this.involvesTypeParameters_computed = false;
               this.involvesTypeParameters_initialized = false;
            }

            state.IN_CIRCLE = false;
            return this.involvesTypeParameters_value;
         }
      }
   }

   private boolean involvesTypeParameters_compute() {
      return false;
   }

   public boolean isGenericType() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isParameterizedType() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isRawType() {
      ASTNode$State state = this.state();
      return this.isNestedType() && this.enclosingType().isRawType();
   }

   public TypeDecl erasure() {
      if (this.erasure_computed) {
         return this.erasure_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.erasure_value = this.erasure_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.erasure_computed = true;
         }

         return this.erasure_value;
      }
   }

   private TypeDecl erasure_compute() {
      if (!this.isAnonymous() && !this.isLocalClass()) {
         return !this.isNestedType() ? this : this.extractSingleType(this.enclosingType().erasure().memberTypes(this.name()));
      } else {
         return this;
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
      return new HashSet();
   }

   public boolean sameSignature(Access a) {
      ASTNode$State state = this.state();
      if (a instanceof ParTypeAccess) {
         return false;
      } else if (a instanceof AbstractWildcard) {
         return false;
      } else {
         return this == a.type();
      }
   }

   public boolean usesTypeVariable() {
      if (this.usesTypeVariable_computed) {
         return this.usesTypeVariable_value;
      } else {
         ASTNode$State state = this.state();
         if (!this.usesTypeVariable_initialized) {
            this.usesTypeVariable_initialized = true;
            this.usesTypeVariable_value = false;
         }

         if (state.IN_CIRCLE) {
            if (this.usesTypeVariable_visited != state.CIRCLE_INDEX) {
               this.usesTypeVariable_visited = state.CIRCLE_INDEX;
               if (state.RESET_CYCLE) {
                  this.usesTypeVariable_computed = false;
                  this.usesTypeVariable_initialized = false;
                  this.usesTypeVariable_visited = -1;
                  return this.usesTypeVariable_value;
               } else {
                  boolean new_usesTypeVariable_value = this.usesTypeVariable_compute();
                  if (new_usesTypeVariable_value != this.usesTypeVariable_value) {
                     state.CHANGE = true;
                  }

                  this.usesTypeVariable_value = new_usesTypeVariable_value;
                  return this.usesTypeVariable_value;
               }
            } else {
               return this.usesTypeVariable_value;
            }
         } else {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = this.is$Final();

            do {
               this.usesTypeVariable_visited = state.CIRCLE_INDEX;
               state.CHANGE = false;
               boolean new_usesTypeVariable_value = this.usesTypeVariable_compute();
               if (new_usesTypeVariable_value != this.usesTypeVariable_value) {
                  state.CHANGE = true;
               }

               this.usesTypeVariable_value = new_usesTypeVariable_value;
               ++state.CIRCLE_INDEX;
            } while(state.CHANGE);

            if (isFinal && num == this.state().boundariesCrossed) {
               this.usesTypeVariable_computed = true;
            } else {
               state.RESET_CYCLE = true;
               this.usesTypeVariable_compute();
               state.RESET_CYCLE = false;
               this.usesTypeVariable_computed = false;
               this.usesTypeVariable_initialized = false;
            }

            state.IN_CIRCLE = false;
            return this.usesTypeVariable_value;
         }
      }
   }

   private boolean usesTypeVariable_compute() {
      return this.isNestedType() && this.enclosingType().usesTypeVariable();
   }

   public TypeDecl original() {
      ASTNode$State state = this.state();
      return this;
   }

   public TypeDecl asWildcardExtends() {
      ASTNode$State state = this.state();
      return this.lookupWildcardExtends(this);
   }

   public TypeDecl asWildcardSuper() {
      ASTNode$State state = this.state();
      return this.lookupWildcardSuper(this);
   }

   public TypeDecl sourceTypeDecl() {
      if (this.sourceTypeDecl_computed) {
         return this.sourceTypeDecl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.sourceTypeDecl_value = this.sourceTypeDecl_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.sourceTypeDecl_computed = true;
         }

         return this.sourceTypeDecl_value;
      }
   }

   private TypeDecl sourceTypeDecl_compute() {
      return this;
   }

   public boolean isTypeVariable() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean supertypeGenericClassDecl(GenericClassDecl type) {
      ASTNode$State state = this.state();
      return this.supertypeClassDecl(type);
   }

   public boolean supertypeGenericInterfaceDecl(GenericInterfaceDecl type) {
      ASTNode$State state = this.state();
      return this == type || this.supertypeInterfaceDecl(type);
   }

   public boolean supertypeRawClassDecl(RawClassDecl type) {
      ASTNode$State state = this.state();
      return this.supertypeParClassDecl(type);
   }

   public boolean supertypeRawInterfaceDecl(RawInterfaceDecl type) {
      ASTNode$State state = this.state();
      return this.supertypeParInterfaceDecl(type);
   }

   public boolean supertypeWildcard(WildcardType type) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean supertypeWildcardExtends(WildcardExtendsType type) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean supertypeWildcardSuper(WildcardSuperType type) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isWildcard() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean supertypeParClassDecl(ParClassDecl type) {
      ASTNode$State state = this.state();
      return this.supertypeClassDecl(type);
   }

   public boolean supertypeParInterfaceDecl(ParInterfaceDecl type) {
      ASTNode$State state = this.state();
      return this.supertypeInterfaceDecl(type);
   }

   public boolean containedIn(TypeDecl type) {
      if (this.containedIn_TypeDecl_values == null) {
         this.containedIn_TypeDecl_values = new HashMap(4);
      }

      ASTNode$State.CircularValue _value;
      if (this.containedIn_TypeDecl_values.containsKey(type)) {
         Object _o = this.containedIn_TypeDecl_values.get(type);
         if (!(_o instanceof ASTNode$State.CircularValue)) {
            return (Boolean)_o;
         }

         _value = (ASTNode$State.CircularValue)_o;
      } else {
         _value = new ASTNode$State.CircularValue();
         this.containedIn_TypeDecl_values.put(type, _value);
         _value.value = true;
      }

      ASTNode$State state = this.state();
      if (state.IN_CIRCLE) {
         if (!(new Integer(state.CIRCLE_INDEX)).equals(_value.visited)) {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            boolean new_containedIn_TypeDecl_value = this.containedIn_compute(type);
            if (state.RESET_CYCLE) {
               this.containedIn_TypeDecl_values.remove(type);
            } else if (new_containedIn_TypeDecl_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_containedIn_TypeDecl_value;
            }

            return new_containedIn_TypeDecl_value;
         } else {
            return (Boolean)_value.value;
         }
      } else {
         state.IN_CIRCLE = true;
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();

         boolean new_containedIn_TypeDecl_value;
         do {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            state.CHANGE = false;
            new_containedIn_TypeDecl_value = this.containedIn_compute(type);
            if (new_containedIn_TypeDecl_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_containedIn_TypeDecl_value;
            }

            ++state.CIRCLE_INDEX;
         } while(state.CHANGE);

         if (isFinal && num == this.state().boundariesCrossed) {
            this.containedIn_TypeDecl_values.put(type, new_containedIn_TypeDecl_value);
         } else {
            this.containedIn_TypeDecl_values.remove(type);
            state.RESET_CYCLE = true;
            this.containedIn_compute(type);
            state.RESET_CYCLE = false;
         }

         state.IN_CIRCLE = false;
         return new_containedIn_TypeDecl_value;
      }
   }

   private boolean containedIn_compute(TypeDecl type) {
      if (type != this && !(type instanceof WildcardType)) {
         if (type instanceof WildcardExtendsType) {
            return this.subtype(((WildcardExtendsType)type).extendsType());
         } else if (type instanceof WildcardSuperType) {
            return ((WildcardSuperType)type).superType().subtype(this);
         } else {
            return type instanceof TypeVariable ? this.subtype(type) : this.sameStructure(type);
         }
      } else {
         return true;
      }
   }

   public boolean sameStructure(TypeDecl t) {
      if (this.sameStructure_TypeDecl_values == null) {
         this.sameStructure_TypeDecl_values = new HashMap(4);
      }

      ASTNode$State.CircularValue _value;
      if (this.sameStructure_TypeDecl_values.containsKey(t)) {
         Object _o = this.sameStructure_TypeDecl_values.get(t);
         if (!(_o instanceof ASTNode$State.CircularValue)) {
            return (Boolean)_o;
         }

         _value = (ASTNode$State.CircularValue)_o;
      } else {
         _value = new ASTNode$State.CircularValue();
         this.sameStructure_TypeDecl_values.put(t, _value);
         _value.value = true;
      }

      ASTNode$State state = this.state();
      if (state.IN_CIRCLE) {
         if (!(new Integer(state.CIRCLE_INDEX)).equals(_value.visited)) {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            boolean new_sameStructure_TypeDecl_value = this.sameStructure_compute(t);
            if (state.RESET_CYCLE) {
               this.sameStructure_TypeDecl_values.remove(t);
            } else if (new_sameStructure_TypeDecl_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_sameStructure_TypeDecl_value;
            }

            return new_sameStructure_TypeDecl_value;
         } else {
            return (Boolean)_value.value;
         }
      } else {
         state.IN_CIRCLE = true;
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();

         boolean new_sameStructure_TypeDecl_value;
         do {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            state.CHANGE = false;
            new_sameStructure_TypeDecl_value = this.sameStructure_compute(t);
            if (new_sameStructure_TypeDecl_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_sameStructure_TypeDecl_value;
            }

            ++state.CIRCLE_INDEX;
         } while(state.CHANGE);

         if (isFinal && num == this.state().boundariesCrossed) {
            this.sameStructure_TypeDecl_values.put(t, new_sameStructure_TypeDecl_value);
         } else {
            this.sameStructure_TypeDecl_values.remove(t);
            state.RESET_CYCLE = true;
            this.sameStructure_compute(t);
            state.RESET_CYCLE = false;
         }

         state.IN_CIRCLE = false;
         return new_sameStructure_TypeDecl_value;
      }
   }

   private boolean sameStructure_compute(TypeDecl t) {
      return t == this;
   }

   public boolean supertypeTypeVariable(TypeVariable type) {
      ASTNode$State state = this.state();
      if (type == this) {
         return true;
      } else {
         for(int i = 0; i < type.getNumTypeBound(); ++i) {
            if (type.getTypeBound(i).type().subtype(this)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean supertypeLUBType(LUBType type) {
      ASTNode$State state = this.state();

      for(int i = 0; i < type.getNumTypeBound(); ++i) {
         if (!type.getTypeBound(i).type().subtype(this)) {
            return false;
         }
      }

      return true;
   }

   public boolean supertypeGLBType(GLBType type) {
      ASTNode$State state = this.state();

      for(int i = 0; i < type.getNumTypeBound(); ++i) {
         if (type.getTypeBound(i).type().subtype(this)) {
            return true;
         }
      }

      return false;
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
      return type == this;
   }

   public boolean supertypeClassDecl(ClassDecl type) {
      ASTNode$State state = this.state();
      return type == this;
   }

   public boolean supertypeInterfaceDecl(InterfaceDecl type) {
      ASTNode$State state = this.state();
      return type == this;
   }

   public boolean supertypeArrayDecl(ArrayDecl type) {
      ASTNode$State state = this.state();
      return this == type;
   }

   public boolean supertypePrimitiveType(PrimitiveType type) {
      ASTNode$State state = this.state();
      return type == this;
   }

   public boolean supertypeNullType(NullType type) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean supertypeVoidType(VoidType type) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean supertypeClassDeclSubstituted(ClassDeclSubstituted type) {
      ASTNode$State state = this.state();
      return type.original() == this || this.supertypeClassDecl(type);
   }

   public boolean supertypeInterfaceDeclSubstituted(InterfaceDeclSubstituted type) {
      ASTNode$State state = this.state();
      return type.original() == this || this.supertypeInterfaceDecl(type);
   }

   public boolean supertypeGenericClassDeclSubstituted(GenericClassDeclSubstituted type) {
      ASTNode$State state = this.state();
      return type.original() == this || this.supertypeGenericClassDecl(type);
   }

   public boolean supertypeGenericInterfaceDeclSubstituted(GenericInterfaceDeclSubstituted type) {
      ASTNode$State state = this.state();
      return type.original() == this || this.supertypeGenericInterfaceDecl(type);
   }

   public TypeDecl stringPromotion() {
      ASTNode$State state = this.state();
      return this;
   }

   public MethodDecl methodWithArgs(String name, TypeDecl[] args) {
      ASTNode$State state = this.state();
      Iterator iter = this.memberMethods(name).iterator();

      while(true) {
         MethodDecl m;
         do {
            if (!iter.hasNext()) {
               return null;
            }

            m = (MethodDecl)iter.next();
         } while(m.getNumParameter() != args.length);

         for(int i = 0; i < args.length; ++i) {
            if (m.getParameter(i).type() == args[i]) {
               return m;
            }
         }
      }
   }

   public Collection enclosingVariables() {
      if (this.enclosingVariables_computed) {
         return this.enclosingVariables_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.enclosingVariables_value = this.enclosingVariables_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.enclosingVariables_computed = true;
         }

         return this.enclosingVariables_value;
      }
   }

   private Collection enclosingVariables_compute() {
      HashSet set = new HashSet();

      for(TypeDecl e = this; e != null; e = e.enclosingType()) {
         if (e.isLocalClass() || e.isAnonymous()) {
            this.collectEnclosingVariables(set, e.enclosingType());
         }
      }

      if (this.isClassDecl()) {
         ClassDecl classDecl = (ClassDecl)this;
         if (classDecl.isNestedType() && classDecl.hasSuperclass()) {
            set.addAll(classDecl.superclass().enclosingVariables());
         }
      }

      return set;
   }

   public boolean isAnonymousInNonStaticContext() {
      ASTNode$State state = this.state();
      return this.isAnonymous() && !((ClassInstanceExpr)this.getParent().getParent()).unqualifiedScope().inStaticContext() && (!this.inExplicitConstructorInvocation() || this.enclosingBodyDecl().hostType().isInnerType());
   }

   public boolean needsEnclosing() {
      ASTNode$State state = this.state();
      if (this.isAnonymous()) {
         return this.isAnonymousInNonStaticContext();
      } else if (this.isLocalClass()) {
         return !this.inStaticContext();
      } else {
         return this.isInnerType();
      }
   }

   public boolean needsSuperEnclosing() {
      ASTNode$State state = this.state();
      if (!this.isAnonymous()) {
         return false;
      } else {
         TypeDecl superClass = ((ClassDecl)this).superclass();
         if (superClass.isLocalClass()) {
            return !superClass.inStaticContext();
         } else if (superClass.isInnerType()) {
            return true;
         } else {
            return this.needsEnclosing() && this.enclosing() == this.superEnclosing() ? false : false;
         }
      }
   }

   public TypeDecl enclosing() {
      ASTNode$State state = this.state();
      if (!this.needsEnclosing()) {
         return null;
      } else {
         TypeDecl typeDecl = this.enclosingType();
         if (this.isAnonymous() && this.inExplicitConstructorInvocation()) {
            typeDecl = typeDecl.enclosingType();
         }

         return typeDecl;
      }
   }

   public TypeDecl superEnclosing() {
      ASTNode$State state = this.state();
      return null;
   }

   public int uniqueIndex() {
      if (this.uniqueIndex_computed) {
         return this.uniqueIndex_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.uniqueIndex_value = this.uniqueIndex_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.uniqueIndex_computed = true;
         }

         return this.uniqueIndex_value;
      }
   }

   private int uniqueIndex_compute() {
      return this.topLevelType().uniqueIndexCounter++;
   }

   public String jvmName() {
      if (this.jvmName_computed) {
         return this.jvmName_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.jvmName_value = this.jvmName_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.jvmName_computed = true;
         }

         return this.jvmName_value;
      }
   }

   private String jvmName_compute() {
      throw new Error("Jvm name only supported for reference types and not " + this.getClass().getName());
   }

   public String primitiveClassName() {
      ASTNode$State state = this.state();
      throw new Error("primitiveClassName not supported for " + this.name() + " of type " + this.getClass().getName());
   }

   public String referenceClassFieldName() {
      ASTNode$State state = this.state();
      throw new Error("referenceClassFieldName not supported for " + this.name() + " of type " + this.getClass().getName());
   }

   public SootClass getSootClassDecl() {
      if (this.getSootClassDecl_computed) {
         return this.getSootClassDecl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getSootClassDecl_value = this.getSootClassDecl_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getSootClassDecl_computed = true;
         }

         return this.getSootClassDecl_value;
      }
   }

   private SootClass getSootClassDecl_compute() {
      if (this.erasure() != this) {
         return this.erasure().getSootClassDecl();
      } else if (this.compilationUnit().fromSource()) {
         return this.sootClass();
      } else {
         if (this.options().verbose()) {
            System.out.println("Loading .class file " + this.jvmName());
         }

         return SootResolver.v().makeClassRef(this.jvmName());
      }
   }

   public Type getSootType() {
      if (this.getSootType_computed) {
         return this.getSootType_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getSootType_value = this.getSootType_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getSootType_computed = true;
         }

         return this.getSootType_value;
      }
   }

   private Type getSootType_compute() {
      return RefType.v(this.erasure().jvmName());
   }

   public RefType sootRef() {
      ASTNode$State state = this.state();
      return (RefType)this.getSootType();
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
      return this.erasure() != this ? this.erasure().sootClass() : this.refined_EmitJimple_TypeDecl_sootClass();
   }

   public String sourceNameWithoutPath() {
      ASTNode$State state = this.state();
      String s = this.sourceFile();
      return s != null ? s.substring(s.lastIndexOf(File.separatorChar) + 1) : "Unknown";
   }

   public int sootTypeModifiers() {
      ASTNode$State state = this.state();
      int result = 0;
      if (this.isNestedType()) {
         result |= 1;
      } else {
         if (this.isPublic()) {
            result |= 1;
         }

         if (this.isProtected()) {
            result |= 4;
         }

         if (this.isPrivate()) {
            result |= 2;
         }
      }

      if (this.isFinal()) {
         result |= 16;
      }

      if (this.isStatic()) {
         result |= 8;
      }

      if (this.isAbstract()) {
         result |= 1024;
      }

      return result;
   }

   public boolean needsClinit() {
      if (this.needsClinit_computed) {
         return this.needsClinit_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.needsClinit_value = this.needsClinit_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.needsClinit_computed = true;
         }

         return this.needsClinit_value;
      }
   }

   private boolean needsClinit_compute() {
      for(int i = 0; i < this.getNumBodyDecl(); ++i) {
         BodyDecl b = this.getBodyDecl(i);
         if (b instanceof FieldDeclaration) {
            FieldDeclaration f = (FieldDeclaration)b;
            if (f.isStatic() && f.hasInit() && f.generate()) {
               return true;
            }
         } else if (b instanceof StaticInitializer && b.generate()) {
            return true;
         }
      }

      return false;
   }

   public Collection innerClassesAttributeEntries() {
      if (this.innerClassesAttributeEntries_computed) {
         return this.innerClassesAttributeEntries_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.innerClassesAttributeEntries_value = this.innerClassesAttributeEntries_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.innerClassesAttributeEntries_computed = true;
         }

         return this.innerClassesAttributeEntries_value;
      }
   }

   private Collection innerClassesAttributeEntries_compute() {
      HashSet list = new HashSet();
      if (this.isNestedType()) {
         list.add(this);
      }

      Iterator iter = this.nestedTypes().iterator();

      while(iter.hasNext()) {
         list.add(iter.next());
      }

      iter = this.usedNestedTypes().iterator();

      while(iter.hasNext()) {
         list.add(iter.next());
      }

      return list;
   }

   public SootField getSootField(String name, TypeDecl type) {
      java.util.List _parameters = new ArrayList(2);
      _parameters.add(name);
      _parameters.add(type);
      if (this.getSootField_String_TypeDecl_values == null) {
         this.getSootField_String_TypeDecl_values = new HashMap(4);
      }

      if (this.getSootField_String_TypeDecl_values.containsKey(_parameters)) {
         return (SootField)this.getSootField_String_TypeDecl_values.get(_parameters);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SootField getSootField_String_TypeDecl_value = this.getSootField_compute(name, type);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getSootField_String_TypeDecl_values.put(_parameters, getSootField_String_TypeDecl_value);
         }

         return getSootField_String_TypeDecl_value;
      }
   }

   private SootField getSootField_compute(String name, TypeDecl type) {
      SootField f = Scene.v().makeSootField(name, type.getSootType(), 0);
      this.getSootClassDecl().addField(f);
      return f;
   }

   public int variableSize() {
      ASTNode$State state = this.state();
      return 1;
   }

   public String typeDescriptor() {
      ASTNode$State state = this.state();
      return this.jvmName();
   }

   public MethodDecl createEnumMethod(TypeDecl enumDecl) {
      if (this.createEnumMethod_TypeDecl_values == null) {
         this.createEnumMethod_TypeDecl_values = new HashMap(4);
      }

      if (this.createEnumMethod_TypeDecl_values.containsKey(enumDecl)) {
         return (MethodDecl)this.createEnumMethod_TypeDecl_values.get(enumDecl);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         MethodDecl createEnumMethod_TypeDecl_value = this.createEnumMethod_compute(enumDecl);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.createEnumMethod_TypeDecl_values.put(enumDecl, createEnumMethod_TypeDecl_value);
         }

         return createEnumMethod_TypeDecl_value;
      }
   }

   private MethodDecl createEnumMethod_compute(TypeDecl enumDecl) {
      MethodDecl m = new MethodDecl(new Modifiers((new List()).add(new Modifier("static")).add(new Modifier("final")).add(new Modifier("private"))), this.typeInt().arrayType().createQualifiedAccess(), "$SwitchMap$" + enumDecl.fullName().replace('.', '$'), new List(), new List(), new Opt(new Block((new List()).add(new IfStmt(new EQExpr(this.createEnumArray(enumDecl).createBoundFieldAccess(), new NullLiteral("null")), AssignExpr.asStmt(this.createEnumArray(enumDecl).createBoundFieldAccess(), new ArrayCreationExpr(new ArrayTypeWithSizeAccess(this.typeInt().createQualifiedAccess(), enumDecl.createQualifiedAccess().qualifiesAccess(new MethodAccess("values", new List())).qualifiesAccess(new VarAccess("length"))), new Opt())), new Opt())).add(new ReturnStmt(this.createEnumArray(enumDecl).createBoundFieldAccess())))));
      this.getBodyDeclList().insertChild(m, 1);
      return (MethodDecl)this.getBodyDeclList().getChild(1);
   }

   public int createEnumIndex(EnumConstant e) {
      if (this.createEnumIndex_EnumConstant_values == null) {
         this.createEnumIndex_EnumConstant_values = new HashMap(4);
      }

      if (this.createEnumIndex_EnumConstant_values.containsKey(e)) {
         return (Integer)this.createEnumIndex_EnumConstant_values.get(e);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         int createEnumIndex_EnumConstant_value = this.createEnumIndex_compute(e);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.createEnumIndex_EnumConstant_values.put(e, createEnumIndex_EnumConstant_value);
         }

         return createEnumIndex_EnumConstant_value;
      }
   }

   private int createEnumIndex_compute(EnumConstant e) {
      if (this.createEnumIndexMap == null) {
         this.createEnumIndexMap = new HashMap();
      }

      if (!this.createEnumIndexMap.containsKey(e.hostType())) {
         this.createEnumIndexMap.put(e.hostType(), new Integer(0));
      }

      Integer i = (Integer)this.createEnumIndexMap.get(e.hostType());
      i = new Integer(i + 1);
      this.createEnumIndexMap.put(e.hostType(), i);
      MethodDecl m = this.createEnumMethod(e.hostType());
      List list = m.getBlock().getStmtList();
      list.insertChild(new TryStmt(new Block((new List()).add(AssignExpr.asStmt(this.createEnumArray(e.hostType()).createBoundFieldAccess().qualifiesAccess(new ArrayAccess(e.createBoundFieldAccess().qualifiesAccess(new MethodAccess("ordinal", new List())))), new IntegerLiteral(i.toString())))), (new List()).add(new BasicCatch(new ParameterDeclaration(this.lookupType("java.lang", "NoSuchFieldError").createQualifiedAccess(), "e"), new Block(new List()))), new Opt()), list.getNumChild() - 1);
      return i;
   }

   public FieldDeclaration createEnumArray(TypeDecl enumDecl) {
      if (this.createEnumArray_TypeDecl_values == null) {
         this.createEnumArray_TypeDecl_values = new HashMap(4);
      }

      if (this.createEnumArray_TypeDecl_values.containsKey(enumDecl)) {
         return (FieldDeclaration)this.createEnumArray_TypeDecl_values.get(enumDecl);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         FieldDeclaration createEnumArray_TypeDecl_value = this.createEnumArray_compute(enumDecl);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.createEnumArray_TypeDecl_values.put(enumDecl, createEnumArray_TypeDecl_value);
         }

         return createEnumArray_TypeDecl_value;
      }
   }

   private FieldDeclaration createEnumArray_compute(TypeDecl enumDecl) {
      FieldDeclaration f = new FieldDeclaration(new Modifiers((new List()).add(new Modifier("static")).add(new Modifier("final")).add(new Modifier("private"))), this.typeInt().arrayType().createQualifiedAccess(), "$SwitchMap$" + enumDecl.fullName().replace('.', '$'), new Opt());
      this.getBodyDeclList().insertChild(f, 0);
      return (FieldDeclaration)this.getBodyDeclList().getChild(0);
   }

   public SimpleSet bridgeCandidates(String signature) {
      ASTNode$State state = this.state();
      return SimpleSet.emptySet;
   }

   public boolean hasAnnotationSafeVarargs() {
      ASTNode$State state = this.state();
      return this.getModifiers().hasAnnotationSafeVarargs();
   }

   public boolean isReifiable() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isUncheckedConversionTo(TypeDecl dest) {
      ASTNode$State state = this.state();
      return !dest.isRawType() && this.isRawType();
   }

   public TypeDecl componentType() {
      if (this.componentType_computed) {
         return this.componentType_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.componentType_value = this.getParent().Define_TypeDecl_componentType(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.componentType_computed = true;
         }

         return this.componentType_value;
      }
   }

   public TypeDecl typeCloneable() {
      ASTNode$State state = this.state();
      TypeDecl typeCloneable_value = this.getParent().Define_TypeDecl_typeCloneable(this, (ASTNode)null);
      return typeCloneable_value;
   }

   public TypeDecl typeSerializable() {
      ASTNode$State state = this.state();
      TypeDecl typeSerializable_value = this.getParent().Define_TypeDecl_typeSerializable(this, (ASTNode)null);
      return typeSerializable_value;
   }

   public CompilationUnit compilationUnit() {
      ASTNode$State state = this.state();
      CompilationUnit compilationUnit_value = this.getParent().Define_CompilationUnit_compilationUnit(this, (ASTNode)null);
      return compilationUnit_value;
   }

   public boolean isDAbefore(Variable v) {
      if (this.isDAbefore_Variable_values == null) {
         this.isDAbefore_Variable_values = new HashMap(4);
      }

      if (this.isDAbefore_Variable_values.containsKey(v)) {
         return (Boolean)this.isDAbefore_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDAbefore_Variable_value = this.getParent().Define_boolean_isDAbefore(this, (ASTNode)null, v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDAbefore_Variable_values.put(v, isDAbefore_Variable_value);
         }

         return isDAbefore_Variable_value;
      }
   }

   public boolean isDUbefore(Variable v) {
      if (this.isDUbefore_Variable_values == null) {
         this.isDUbefore_Variable_values = new HashMap(4);
      }

      if (this.isDUbefore_Variable_values.containsKey(v)) {
         return (Boolean)this.isDUbefore_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDUbefore_Variable_value = this.getParent().Define_boolean_isDUbefore(this, (ASTNode)null, v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDUbefore_Variable_values.put(v, isDUbefore_Variable_value);
         }

         return isDUbefore_Variable_value;
      }
   }

   public TypeDecl typeException() {
      if (this.typeException_computed) {
         return this.typeException_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeException_value = this.getParent().Define_TypeDecl_typeException(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeException_computed = true;
         }

         return this.typeException_value;
      }
   }

   public TypeDecl typeRuntimeException() {
      if (this.typeRuntimeException_computed) {
         return this.typeRuntimeException_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeRuntimeException_value = this.getParent().Define_TypeDecl_typeRuntimeException(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeRuntimeException_computed = true;
         }

         return this.typeRuntimeException_value;
      }
   }

   public TypeDecl typeError() {
      if (this.typeError_computed) {
         return this.typeError_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeError_value = this.getParent().Define_TypeDecl_typeError(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeError_computed = true;
         }

         return this.typeError_value;
      }
   }

   public Collection lookupMethod(String name) {
      if (this.lookupMethod_String_values == null) {
         this.lookupMethod_String_values = new HashMap(4);
      }

      if (this.lookupMethod_String_values.containsKey(name)) {
         return (Collection)this.lookupMethod_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         Collection lookupMethod_String_value = this.getParent().Define_Collection_lookupMethod(this, (ASTNode)null, name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.lookupMethod_String_values.put(name, lookupMethod_String_value);
         }

         return lookupMethod_String_value;
      }
   }

   public TypeDecl typeInt() {
      ASTNode$State state = this.state();
      TypeDecl typeInt_value = this.getParent().Define_TypeDecl_typeInt(this, (ASTNode)null);
      return typeInt_value;
   }

   public TypeDecl typeObject() {
      if (this.typeObject_computed) {
         return this.typeObject_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeObject_value = this.getParent().Define_TypeDecl_typeObject(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeObject_computed = true;
         }

         return this.typeObject_value;
      }
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
      if (this.lookupVariable_String_values == null) {
         this.lookupVariable_String_values = new HashMap(4);
      }

      if (this.lookupVariable_String_values.containsKey(name)) {
         return (SimpleSet)this.lookupVariable_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet lookupVariable_String_value = this.getParent().Define_SimpleSet_lookupVariable(this, (ASTNode)null, name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.lookupVariable_String_values.put(name, lookupVariable_String_value);
         }

         return lookupVariable_String_value;
      }
   }

   public boolean hasPackage(String packageName) {
      ASTNode$State state = this.state();
      boolean hasPackage_String_value = this.getParent().Define_boolean_hasPackage(this, (ASTNode)null, packageName);
      return hasPackage_String_value;
   }

   public ASTNode enclosingBlock() {
      ASTNode$State state = this.state();
      ASTNode enclosingBlock_value = this.getParent().Define_ASTNode_enclosingBlock(this, (ASTNode)null);
      return enclosingBlock_value;
   }

   public String packageName() {
      if (this.packageName_computed) {
         return this.packageName_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.packageName_value = this.getParent().Define_String_packageName(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.packageName_computed = true;
         }

         return this.packageName_value;
      }
   }

   public boolean isAnonymous() {
      if (this.isAnonymous_computed) {
         return this.isAnonymous_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isAnonymous_value = this.getParent().Define_boolean_isAnonymous(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isAnonymous_computed = true;
         }

         return this.isAnonymous_value;
      }
   }

   public TypeDecl enclosingType() {
      ASTNode$State state = this.state();
      TypeDecl enclosingType_value = this.getParent().Define_TypeDecl_enclosingType(this, (ASTNode)null);
      return enclosingType_value;
   }

   public BodyDecl enclosingBodyDecl() {
      ASTNode$State state = this.state();
      BodyDecl enclosingBodyDecl_value = this.getParent().Define_BodyDecl_enclosingBodyDecl(this, (ASTNode)null);
      return enclosingBodyDecl_value;
   }

   public boolean isNestedType() {
      ASTNode$State state = this.state();
      boolean isNestedType_value = this.getParent().Define_boolean_isNestedType(this, (ASTNode)null);
      return isNestedType_value;
   }

   public boolean isMemberType() {
      ASTNode$State state = this.state();
      boolean isMemberType_value = this.getParent().Define_boolean_isMemberType(this, (ASTNode)null);
      return isMemberType_value;
   }

   public boolean isLocalClass() {
      ASTNode$State state = this.state();
      boolean isLocalClass_value = this.getParent().Define_boolean_isLocalClass(this, (ASTNode)null);
      return isLocalClass_value;
   }

   public String hostPackage() {
      ASTNode$State state = this.state();
      String hostPackage_value = this.getParent().Define_String_hostPackage(this, (ASTNode)null);
      return hostPackage_value;
   }

   public TypeDecl unknownType() {
      if (this.unknownType_computed) {
         return this.unknownType_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.unknownType_value = this.getParent().Define_TypeDecl_unknownType(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.unknownType_computed = true;
         }

         return this.unknownType_value;
      }
   }

   public TypeDecl typeVoid() {
      ASTNode$State state = this.state();
      TypeDecl typeVoid_value = this.getParent().Define_TypeDecl_typeVoid(this, (ASTNode)null);
      return typeVoid_value;
   }

   public TypeDecl enclosingInstance() {
      ASTNode$State state = this.state();
      TypeDecl enclosingInstance_value = this.getParent().Define_TypeDecl_enclosingInstance(this, (ASTNode)null);
      return enclosingInstance_value;
   }

   public boolean inExplicitConstructorInvocation() {
      if (this.inExplicitConstructorInvocation_computed) {
         return this.inExplicitConstructorInvocation_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.inExplicitConstructorInvocation_value = this.getParent().Define_boolean_inExplicitConstructorInvocation(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.inExplicitConstructorInvocation_computed = true;
         }

         return this.inExplicitConstructorInvocation_value;
      }
   }

   public boolean inStaticContext() {
      if (this.inStaticContext_computed) {
         return this.inStaticContext_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.inStaticContext_value = this.getParent().Define_boolean_inStaticContext(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.inStaticContext_computed = true;
         }

         return this.inStaticContext_value;
      }
   }

   public boolean withinSuppressWarnings(String s) {
      ASTNode$State state = this.state();
      boolean withinSuppressWarnings_String_value = this.getParent().Define_boolean_withinSuppressWarnings(this, (ASTNode)null, s);
      return withinSuppressWarnings_String_value;
   }

   public boolean withinDeprecatedAnnotation() {
      ASTNode$State state = this.state();
      boolean withinDeprecatedAnnotation_value = this.getParent().Define_boolean_withinDeprecatedAnnotation(this, (ASTNode)null);
      return withinDeprecatedAnnotation_value;
   }

   public TypeDecl typeWildcard() {
      ASTNode$State state = this.state();
      TypeDecl typeWildcard_value = this.getParent().Define_TypeDecl_typeWildcard(this, (ASTNode)null);
      return typeWildcard_value;
   }

   public TypeDecl lookupWildcardExtends(TypeDecl typeDecl) {
      ASTNode$State state = this.state();
      TypeDecl lookupWildcardExtends_TypeDecl_value = this.getParent().Define_TypeDecl_lookupWildcardExtends(this, (ASTNode)null, typeDecl);
      return lookupWildcardExtends_TypeDecl_value;
   }

   public TypeDecl lookupWildcardSuper(TypeDecl typeDecl) {
      ASTNode$State state = this.state();
      TypeDecl lookupWildcardSuper_TypeDecl_value = this.getParent().Define_TypeDecl_lookupWildcardSuper(this, (ASTNode)null, typeDecl);
      return lookupWildcardSuper_TypeDecl_value;
   }

   public LUBType lookupLUBType(Collection bounds) {
      ASTNode$State state = this.state();
      LUBType lookupLUBType_Collection_value = this.getParent().Define_LUBType_lookupLUBType(this, (ASTNode)null, bounds);
      return lookupLUBType_Collection_value;
   }

   public GLBType lookupGLBType(ArrayList bounds) {
      ASTNode$State state = this.state();
      GLBType lookupGLBType_ArrayList_value = this.getParent().Define_GLBType_lookupGLBType(this, (ASTNode)null, bounds);
      return lookupGLBType_ArrayList_value;
   }

   public TypeDecl Define_TypeDecl_componentType(ASTNode caller, ASTNode child) {
      return caller == this.arrayType_value ? this : this.getParent().Define_TypeDecl_componentType(this, caller);
   }

   public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_isDest(this, caller);
      }
   }

   public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return true;
      } else {
         return this.getParent().Define_boolean_isSource(this, caller);
      }
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller != this.getBodyDeclListNoTransform()) {
         return this.getParent().Define_boolean_isDAbefore(this, caller, v);
      } else {
         int childIndex = caller.getIndexOfChild(child);
         BodyDecl b = this.getBodyDecl(childIndex);
         if (!v.isInstanceVariable() && !v.isClassVariable()) {
            return v.hostType() != this ? this.isDAbefore(v) : false;
         } else if (b instanceof FieldDeclaration && !((FieldDeclaration)b).isStatic() && v.isClassVariable()) {
            return true;
         } else if (b instanceof MethodDecl) {
            return true;
         } else if (b instanceof MemberTypeDecl && v.isBlank() && v.isFinal() && v.hostType() == this) {
            return true;
         } else {
            if (v.isClassVariable() || v.isInstanceVariable()) {
               if (v.isFinal() && v.hostType() != this && this.instanceOf(v.hostType())) {
                  return true;
               }

               int index = childIndex - 1;
               if (b instanceof ConstructorDecl) {
                  index = this.getNumBodyDecl() - 1;
               }

               for(int i = index; i >= 0; --i) {
                  b = this.getBodyDecl(i);
                  if (b instanceof FieldDeclaration) {
                     FieldDeclaration f = (FieldDeclaration)b;
                     if (v.isClassVariable() && f.isStatic() || v.isInstanceVariable() && !f.isStatic()) {
                        boolean c = f.isDAafter(v);
                        return c;
                     }
                  } else {
                     if (b instanceof StaticInitializer && v.isClassVariable()) {
                        StaticInitializer si = (StaticInitializer)b;
                        return si.isDAafter(v);
                     }

                     if (b instanceof InstanceInitializer && v.isInstanceVariable()) {
                        InstanceInitializer ii = (InstanceInitializer)b;
                        return ii.isDAafter(v);
                     }
                  }
               }
            }

            return this.isDAbefore(v);
         }
      }
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller != this.getBodyDeclListNoTransform()) {
         return this.getParent().Define_boolean_isDUbefore(this, caller, v);
      } else {
         int childIndex = caller.getIndexOfChild(child);
         BodyDecl b = this.getBodyDecl(childIndex);
         if (!(b instanceof MethodDecl) && !(b instanceof MemberTypeDecl)) {
            if (v.isClassVariable() || v.isInstanceVariable()) {
               int index = childIndex - 1;
               if (b instanceof ConstructorDecl) {
                  index = this.getNumBodyDecl() - 1;
               }

               for(int i = index; i >= 0; --i) {
                  b = this.getBodyDecl(i);
                  if (b instanceof FieldDeclaration) {
                     FieldDeclaration f = (FieldDeclaration)b;
                     if (f == v) {
                        return !f.hasInit();
                     }

                     if (v.isClassVariable() && f.isStatic() || v.isInstanceVariable() && !f.isStatic()) {
                        return f.isDUafter(v);
                     }
                  } else {
                     if (b instanceof StaticInitializer && v.isClassVariable()) {
                        StaticInitializer si = (StaticInitializer)b;
                        return si.isDUafter(v);
                     }

                     if (b instanceof InstanceInitializer && v.isInstanceVariable()) {
                        InstanceInitializer ii = (InstanceInitializer)b;
                        return ii.isDUafter(v);
                     }
                  }
               }
            }

            return this.isDUbefore(v);
         } else {
            return false;
         }
      }
   }

   public Collection Define_Collection_lookupConstructor(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.constructors();
      } else {
         return this.getParent().Define_Collection_lookupConstructor(this, caller);
      }
   }

   public Collection Define_Collection_lookupSuperConstructor(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.lookupSuperConstructor();
      } else {
         return this.getParent().Define_Collection_lookupSuperConstructor(this, caller);
      }
   }

   public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.unqualifiedLookupMethod(name);
      } else {
         return this.getParent().Define_Collection_lookupMethod(this, caller, name);
      }
   }

   public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
      if (caller != this.getBodyDeclListNoTransform()) {
         return this.getParent().Define_SimpleSet_lookupType(this, caller, name);
      } else {
         caller.getIndexOfChild(child);
         SimpleSet c = this.memberTypes(name);
         if (!c.isEmpty()) {
            return c;
         } else if (this.name().equals(name)) {
            return SimpleSet.emptySet.add(this);
         } else {
            c = this.lookupType(name);
            if (this.isClassDecl() && this.isStatic() && !this.isTopLevelType()) {
               SimpleSet newSet = SimpleSet.emptySet;

               TypeDecl d;
               for(Iterator iter = c.iterator(); iter.hasNext(); newSet = newSet.add(d)) {
                  d = (TypeDecl)iter.next();
               }

               c = newSet;
            }

            return c;
         }
      }
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         SimpleSet list = this.memberFields(name);
         if (!list.isEmpty()) {
            return list;
         } else {
            list = this.lookupVariable(name);
            if (this.inStaticContext() || this.isStatic()) {
               list = this.removeInstanceVariables(list);
            }

            return list;
         }
      } else {
         return this.getParent().Define_SimpleSet_lookupVariable(this, caller, name);
      }
   }

   public boolean Define_boolean_mayBePublic(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBePublic(this, caller);
      }
   }

   public boolean Define_boolean_mayBeProtected(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeProtected(this, caller);
      }
   }

   public boolean Define_boolean_mayBePrivate(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBePrivate(this, caller);
      }
   }

   public boolean Define_boolean_mayBeAbstract(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeAbstract(this, caller);
      }
   }

   public boolean Define_boolean_mayBeStatic(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeStatic(this, caller);
      }
   }

   public boolean Define_boolean_mayBeStrictfp(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeStrictfp(this, caller);
      }
   }

   public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_mayBeFinal(this, caller);
      }
   }

   public boolean Define_boolean_mayBeVolatile(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_mayBeVolatile(this, caller);
      }
   }

   public boolean Define_boolean_mayBeTransient(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_mayBeTransient(this, caller);
      }
   }

   public boolean Define_boolean_mayBeSynchronized(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_mayBeSynchronized(this, caller);
      }
   }

   public boolean Define_boolean_mayBeNative(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_mayBeNative(this, caller);
      }
   }

   public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return this;
      } else {
         return this.getParent().Define_VariableScope_outerScope(this, caller);
      }
   }

   public boolean Define_boolean_insideLoop(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_insideLoop(this, caller);
      }
   }

   public boolean Define_boolean_insideSwitch(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_insideSwitch(this, caller);
      }
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.EXPRESSION_NAME;
      } else {
         return this.getParent().Define_NameType_nameType(this, caller);
      }
   }

   public boolean Define_boolean_isAnonymous(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_isAnonymous(this, caller);
      }
   }

   public TypeDecl Define_TypeDecl_enclosingType(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return this;
      } else {
         return this.getParent().Define_TypeDecl_enclosingType(this, caller);
      }
   }

   public boolean Define_boolean_isNestedType(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return true;
      } else {
         return this.getParent().Define_boolean_isNestedType(this, caller);
      }
   }

   public boolean Define_boolean_isLocalClass(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_isLocalClass(this, caller);
      }
   }

   public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
      if (caller == this.getModifiersNoTransform()) {
         return this.hostType();
      } else if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.hostType();
      } else {
         this.getIndexOfChild(caller);
         return this.hostType();
      }
   }

   public TypeDecl Define_TypeDecl_returnType(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.typeVoid();
      } else {
         return this.getParent().Define_TypeDecl_returnType(this, caller);
      }
   }

   public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         int childIndex = caller.getIndexOfChild(child);
         if (this.getBodyDecl(childIndex) instanceof MemberTypeDecl && !((MemberTypeDecl)this.getBodyDecl(childIndex)).typeDecl().isInnerType()) {
            return null;
         } else {
            return this.getBodyDecl(childIndex) instanceof ConstructorDecl ? this.enclosingInstance() : this;
         }
      } else {
         return this.getParent().Define_TypeDecl_enclosingInstance(this, caller);
      }
   }

   public String Define_String_methodHost(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.typeName();
      } else {
         return this.getParent().Define_String_methodHost(this, caller);
      }
   }

   public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
      if (caller != this.getBodyDeclListNoTransform()) {
         return this.getParent().Define_boolean_inStaticContext(this, caller);
      } else {
         caller.getIndexOfChild(child);
         return this.isStatic() || this.inStaticContext();
      }
   }

   public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return true;
   }

   public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
      return caller == this.getModifiersNoTransform() ? name.equals("TYPE") : this.getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
   }

   public boolean Define_boolean_withinSuppressWarnings(ASTNode caller, ASTNode child, String s) {
      if (caller != this.getBodyDeclListNoTransform()) {
         return this.getParent().Define_boolean_withinSuppressWarnings(this, caller, s);
      } else {
         int i = caller.getIndexOfChild(child);
         return this.getBodyDecl(i).hasAnnotationSuppressWarnings(s) || this.hasAnnotationSuppressWarnings(s) || this.withinSuppressWarnings(s);
      }
   }

   public boolean Define_boolean_withinDeprecatedAnnotation(ASTNode caller, ASTNode child) {
      if (caller != this.getBodyDeclListNoTransform()) {
         return this.getParent().Define_boolean_withinDeprecatedAnnotation(this, caller);
      } else {
         int i = caller.getIndexOfChild(child);
         return this.getBodyDecl(i).isDeprecated() || this.isDeprecated() || this.withinDeprecatedAnnotation();
      }
   }

   public boolean Define_boolean_enclosedByExceptionHandler(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         caller.getIndexOfChild(child);
         return false;
      } else {
         return this.getParent().Define_boolean_enclosedByExceptionHandler(this, caller);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
