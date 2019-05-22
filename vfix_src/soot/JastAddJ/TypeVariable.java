package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class TypeVariable extends ReferenceType implements Cloneable {
   protected boolean toInterface_computed = false;
   protected TypeDecl toInterface_value;
   protected int involvesTypeParameters_visited = -1;
   protected boolean involvesTypeParameters_computed = false;
   protected boolean involvesTypeParameters_initialized = false;
   protected boolean involvesTypeParameters_value;
   protected Map memberFields_String_values;
   protected Map castingConversionTo_TypeDecl_values;
   protected boolean erasure_computed = false;
   protected TypeDecl erasure_value;
   protected boolean fullName_computed = false;
   protected String fullName_value;
   protected boolean lubType_computed = false;
   protected TypeDecl lubType_value;
   protected int usesTypeVariable_visited = -1;
   protected boolean usesTypeVariable_computed = false;
   protected boolean usesTypeVariable_initialized = false;
   protected boolean usesTypeVariable_value;
   protected Map accessibleFrom_TypeDecl_values;
   protected boolean typeName_computed = false;
   protected String typeName_value;
   protected Map sameStructure_TypeDecl_values;
   protected Map subtype_TypeDecl_values;
   protected Map getSubstitutedTypeBound_int_TypeDecl_values;
   protected Map instanceOf_TypeDecl_values;

   public void flushCache() {
      super.flushCache();
      this.toInterface_computed = false;
      this.toInterface_value = null;
      this.involvesTypeParameters_visited = -1;
      this.involvesTypeParameters_computed = false;
      this.involvesTypeParameters_initialized = false;
      this.memberFields_String_values = null;
      this.castingConversionTo_TypeDecl_values = null;
      this.erasure_computed = false;
      this.erasure_value = null;
      this.fullName_computed = false;
      this.fullName_value = null;
      this.lubType_computed = false;
      this.lubType_value = null;
      this.usesTypeVariable_visited = -1;
      this.usesTypeVariable_computed = false;
      this.usesTypeVariable_initialized = false;
      this.accessibleFrom_TypeDecl_values = null;
      this.typeName_computed = false;
      this.typeName_value = null;
      this.sameStructure_TypeDecl_values = null;
      this.subtype_TypeDecl_values = null;
      this.getSubstitutedTypeBound_int_TypeDecl_values = null;
      this.instanceOf_TypeDecl_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public TypeVariable clone() throws CloneNotSupportedException {
      TypeVariable node = (TypeVariable)super.clone();
      node.toInterface_computed = false;
      node.toInterface_value = null;
      node.involvesTypeParameters_visited = -1;
      node.involvesTypeParameters_computed = false;
      node.involvesTypeParameters_initialized = false;
      node.memberFields_String_values = null;
      node.castingConversionTo_TypeDecl_values = null;
      node.erasure_computed = false;
      node.erasure_value = null;
      node.fullName_computed = false;
      node.fullName_value = null;
      node.lubType_computed = false;
      node.lubType_value = null;
      node.usesTypeVariable_visited = -1;
      node.usesTypeVariable_computed = false;
      node.usesTypeVariable_initialized = false;
      node.accessibleFrom_TypeDecl_values = null;
      node.typeName_computed = false;
      node.typeName_value = null;
      node.sameStructure_TypeDecl_values = null;
      node.subtype_TypeDecl_values = null;
      node.getSubstitutedTypeBound_int_TypeDecl_values = null;
      node.instanceOf_TypeDecl_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public TypeVariable copy() {
      try {
         TypeVariable node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public TypeVariable fullCopy() {
      TypeVariable tree = this.copy();
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
      if (this.extractSingleType(this.lookupType(this.name())) != this) {
         this.error("*** Semantic Error: type variable " + this.name() + " is multiply declared");
      }

   }

   public void typeCheck() {
      if (!this.getTypeBound(0).type().isTypeVariable() && !this.getTypeBound(0).type().isClassDecl() && !this.getTypeBound(0).type().isInterfaceDecl()) {
         this.error("the first type bound must be either a type variable, or a class or interface type which " + this.getTypeBound(0).type().fullName() + " is not");
      }

      for(int i = 1; i < this.getNumTypeBound(); ++i) {
         if (!this.getTypeBound(i).type().isInterfaceDecl()) {
            this.error("type bound " + i + " must be an interface type which " + this.getTypeBound(i).type().fullName() + " is not");
         }
      }

      HashSet typeSet = new HashSet();

      int i;
      TypeDecl type;
      for(i = 0; i < this.getNumTypeBound(); ++i) {
         type = this.getTypeBound(i).type();
         TypeDecl erasure = type.erasure();
         if (typeSet.contains(erasure)) {
            if (type != erasure) {
               this.error("the erasure " + erasure.fullName() + " of typebound " + this.getTypeBound(i) + " is multiply declared in " + this);
            } else {
               this.error(type.fullName() + " is multiply declared");
            }
         }

         typeSet.add(erasure);
      }

      for(i = 0; i < this.getNumTypeBound(); ++i) {
         type = this.getTypeBound(i).type();
         Iterator iter = type.methodsIterator();

         while(iter.hasNext()) {
            MethodDecl m = (MethodDecl)iter.next();

            for(int j = i + 1; j < this.getNumTypeBound(); ++j) {
               TypeDecl destType = this.getTypeBound(j).type();
               Iterator destIter = destType.memberMethods(m.name()).iterator();

               while(destIter.hasNext()) {
                  MethodDecl n = (MethodDecl)destIter.next();
                  if (m.sameSignature(n) && m.type() != n.type()) {
                     this.error("the two bounds, " + type.name() + " and " + destType.name() + ", in type variable " + this.name() + " have a method " + m.signature() + " with conflicting return types " + m.type().name() + " and " + n.type().name());
                  }
               }
            }
         }
      }

   }

   public Access substitute(Parameterization parTypeDecl) {
      return parTypeDecl.isRawType() ? this.erasure().createBoundAccess() : parTypeDecl.substitute(this).createBoundAccess();
   }

   public Access substituteReturnType(Parameterization parTypeDecl) {
      if (parTypeDecl.isRawType()) {
         return this.erasure().createBoundAccess();
      } else {
         TypeDecl typeDecl = parTypeDecl.substitute(this);
         if (typeDecl instanceof WildcardType) {
            return this.createBoundAccess();
         } else if (typeDecl instanceof WildcardExtendsType) {
            return typeDecl.instanceOf(this) ? ((WildcardExtendsType)typeDecl).extendsType().createBoundAccess() : this.createBoundAccess();
         } else {
            return typeDecl instanceof WildcardSuperType ? this.createBoundAccess() : typeDecl.createBoundAccess();
         }
      }
   }

   public Access substituteParameterType(Parameterization parTypeDecl) {
      if (parTypeDecl.isRawType()) {
         return this.erasure().createBoundAccess();
      } else {
         TypeDecl typeDecl = parTypeDecl.substitute(this);
         if (typeDecl instanceof WildcardType) {
            return this.typeNull().createQualifiedAccess();
         } else if (typeDecl instanceof WildcardExtendsType) {
            return this.typeNull().createQualifiedAccess();
         } else {
            return typeDecl instanceof WildcardSuperType ? ((WildcardSuperType)typeDecl).superType().createBoundAccess() : typeDecl.createBoundAccess();
         }
      }
   }

   public Access createQualifiedAccess() {
      return this.createBoundAccess();
   }

   public void toString(StringBuffer s) {
      s.append(this.name());
      if (this.getNumTypeBound() > 0) {
         s.append(" extends ");
         s.append(this.getTypeBound(0).type().fullName());

         for(int i = 1; i < this.getNumTypeBound(); ++i) {
            s.append(" & ");
            s.append(this.getTypeBound(i).type().fullName());
         }
      }

   }

   public TypeVariable() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new List(), 1);
      this.setChild(new List(), 2);
   }

   public TypeVariable(Modifiers p0, String p1, List<BodyDecl> p2, List<Access> p3) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   public TypeVariable(Modifiers p0, Symbol p1, List<BodyDecl> p2, List<Access> p3) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   protected int numChildren() {
      return 3;
   }

   public boolean mayHaveRewrite() {
      return true;
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

   public void setTypeBoundList(List<Access> list) {
      this.setChild(list, 2);
   }

   public int getNumTypeBound() {
      return this.getTypeBoundList().getNumChild();
   }

   public int getNumTypeBoundNoTransform() {
      return this.getTypeBoundListNoTransform().getNumChildNoTransform();
   }

   public Access getTypeBound(int i) {
      return (Access)this.getTypeBoundList().getChild(i);
   }

   public void addTypeBound(Access node) {
      List<Access> list = this.parent != null && state != null ? this.getTypeBoundList() : this.getTypeBoundListNoTransform();
      list.addChild(node);
   }

   public void addTypeBoundNoTransform(Access node) {
      List<Access> list = this.getTypeBoundListNoTransform();
      list.addChild(node);
   }

   public void setTypeBound(Access node, int i) {
      List<Access> list = this.getTypeBoundList();
      list.setChild(node, i);
   }

   public List<Access> getTypeBounds() {
      return this.getTypeBoundList();
   }

   public List<Access> getTypeBoundsNoTransform() {
      return this.getTypeBoundListNoTransform();
   }

   public List<Access> getTypeBoundList() {
      List<Access> list = (List)this.getChild(2);
      list.getNumChild();
      return list;
   }

   public List<Access> getTypeBoundListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   public TypeDecl toInterface() {
      if (this.toInterface_computed) {
         return this.toInterface_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.toInterface_value = this.toInterface_compute();
         this.toInterface_value.setParent(this);
         this.toInterface_value.is$Final = true;
         this.toInterface_computed = true;
         return this.toInterface_value;
      }
   }

   private TypeDecl toInterface_compute() {
      InterfaceDecl ITj = new InterfaceDecl();
      ITj.setID("ITj_" + this.hashCode());

      for(int i = 0; i < this.getNumTypeBound(); ++i) {
         TypeDecl bound = this.getTypeBound(i).type();

         for(int j = 0; j < bound.getNumBodyDecl(); ++j) {
            BodyDecl bd = bound.getBodyDecl(j);
            if (bd instanceof FieldDeclaration) {
               FieldDeclaration fd = (FieldDeclaration)bd.fullCopy();
               if (fd.isPublic()) {
                  ITj.addBodyDecl(fd);
               }
            } else if (bd instanceof MethodDecl) {
               MethodDecl md = (MethodDecl)bd;
               if (md.isPublic()) {
                  ITj.addBodyDecl(md.fullCopy());
               }
            }
         }
      }

      return ITj;
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
      return true;
   }

   public TypeDecl lowerBound() {
      ASTNode$State state = this.state();
      return this.getTypeBound(0).type();
   }

   public Collection memberMethods(String name) {
      ASTNode$State state = this.state();
      Collection list = new HashSet();

      for(int i = 0; i < this.getNumTypeBound(); ++i) {
         Iterator iter = this.getTypeBound(i).type().memberMethods(name).iterator();

         while(iter.hasNext()) {
            MethodDecl decl = (MethodDecl)iter.next();
            list.add(decl);
         }
      }

      return list;
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
      SimpleSet set = SimpleSet.emptySet;

      FieldDeclaration decl;
      for(int i = 0; i < this.getNumTypeBound(); ++i) {
         for(Iterator iter = this.getTypeBound(i).type().memberFields(name).iterator(); iter.hasNext(); set = set.add(decl)) {
            decl = (FieldDeclaration)iter.next();
         }
      }

      return set;
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
      if (!type.isReferenceType()) {
         return false;
      } else if (this.getNumTypeBound() == 0) {
         return true;
      } else {
         for(int i = 0; i < this.getNumTypeBound(); ++i) {
            if (this.getTypeBound(i).type().castingConversionTo(type)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isNestedType() {
      ASTNode$State state = this.state();
      return false;
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
      return this.getTypeBound(0).type().erasure();
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
      if (this.getParent().getParent() instanceof TypeDecl) {
         TypeDecl typeDecl = (TypeDecl)this.getParent().getParent();
         return typeDecl.fullName() + "@" + this.name();
      } else {
         return super.fullName();
      }
   }

   public boolean sameSignature(Access a) {
      ASTNode$State state = this.state();
      return a.type() == this;
   }

   public TypeDecl lubType() {
      if (this.lubType_computed) {
         return this.lubType_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.lubType_value = this.lubType_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.lubType_computed = true;
         }

         return this.lubType_value;
      }
   }

   private TypeDecl lubType_compute() {
      ArrayList list = new ArrayList();

      for(int i = 0; i < this.getNumTypeBound(); ++i) {
         list.add(this.getTypeBound(i).type());
      }

      return this.lookupLUBType(list);
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
      return true;
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
      return true;
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
      return this.name();
   }

   public boolean isTypeVariable() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean supertypeWildcard(WildcardType type) {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean supertypeWildcardExtends(WildcardExtendsType type) {
      ASTNode$State state = this.state();
      return type.extendsType().subtype(this);
   }

   public boolean supertypeWildcardSuper(WildcardSuperType type) {
      ASTNode$State state = this.state();
      return type.superType().subtype(this);
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
      if (!(t instanceof TypeVariable)) {
         return false;
      } else if (t == this) {
         return true;
      } else {
         TypeVariable type = (TypeVariable)t;
         if (type.getNumTypeBound() != this.getNumTypeBound()) {
            return false;
         } else {
            for(int i = 0; i < this.getNumTypeBound(); ++i) {
               boolean found = false;

               for(int j = i; !found && j < this.getNumTypeBound(); ++j) {
                  if (this.getTypeBound(i).type().sameStructure(type.getTypeBound(j).type())) {
                     found = true;
                  }
               }

               if (!found) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public boolean supertypeArrayDecl(ArrayDecl type) {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumTypeBound(); ++i) {
         if (type.subtype(this.getTypeBound(i).type())) {
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
      return type.supertypeTypeVariable(this);
   }

   public boolean supertypeTypeVariable(TypeVariable type) {
      ASTNode$State state = this.state();
      if (type == this) {
         return true;
      } else {
         for(int i = 0; i < this.getNumTypeBound(); ++i) {
            boolean found = false;

            for(int j = 0; !found && j < type.getNumTypeBound(); ++j) {
               if (type.getSubstitutedTypeBound(j, this).type().subtype(this.getTypeBound(i).type())) {
                  found = true;
               }
            }

            if (!found) {
               return false;
            }
         }

         return true;
      }
   }

   public Access getSubstitutedTypeBound(int i, TypeDecl type) {
      java.util.List _parameters = new ArrayList(2);
      _parameters.add(i);
      _parameters.add(type);
      if (this.getSubstitutedTypeBound_int_TypeDecl_values == null) {
         this.getSubstitutedTypeBound_int_TypeDecl_values = new HashMap(4);
      }

      if (this.getSubstitutedTypeBound_int_TypeDecl_values.containsKey(_parameters)) {
         return (Access)this.getSubstitutedTypeBound_int_TypeDecl_values.get(_parameters);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         Access getSubstitutedTypeBound_int_TypeDecl_value = this.getSubstitutedTypeBound_compute(i, type);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getSubstitutedTypeBound_int_TypeDecl_values.put(_parameters, getSubstitutedTypeBound_int_TypeDecl_value);
         }

         return getSubstitutedTypeBound_int_TypeDecl_value;
      }
   }

   private Access getSubstitutedTypeBound_compute(int i, final TypeDecl type) {
      Access bound = this.getTypeBound(i);
      if (!bound.type().usesTypeVariable()) {
         return bound;
      } else {
         Access access = bound.type().substitute(new Parameterization() {
            public boolean isRawType() {
               return false;
            }

            public TypeDecl substitute(TypeVariable typeVariable) {
               return (TypeDecl)(typeVariable == TypeVariable.this ? type : typeVariable);
            }
         });
         access.setParent(this);
         return access;
      }
   }

   public boolean supertypeClassDecl(ClassDecl type) {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumTypeBound(); ++i) {
         if (!type.subtype(this.getSubstitutedTypeBound(i, type).type())) {
            return false;
         }
      }

      return true;
   }

   public boolean supertypeInterfaceDecl(InterfaceDecl type) {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumTypeBound(); ++i) {
         if (!type.subtype(this.getSubstitutedTypeBound(i, type).type())) {
            return false;
         }
      }

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

   public boolean isReifiable() {
      ASTNode$State state = this.state();
      return false;
   }

   public TypeDecl typeObject() {
      ASTNode$State state = this.state();
      TypeDecl typeObject_value = this.getParent().Define_TypeDecl_typeObject(this, (ASTNode)null);
      return typeObject_value;
   }

   public TypeDecl typeNull() {
      ASTNode$State state = this.state();
      TypeDecl typeNull_value = this.getParent().Define_TypeDecl_typeNull(this, (ASTNode)null);
      return typeNull_value;
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      if (caller == this.getTypeBoundListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.TYPE_NAME;
      } else {
         return super.Define_NameType_nameType(caller, child);
      }
   }

   public ASTNode rewriteTo() {
      if (this.getNumTypeBound() == 0) {
         ++this.state().duringGenericTypeVariables;
         ASTNode result = this.rewriteRule0();
         --this.state().duringGenericTypeVariables;
         return result;
      } else {
         return super.rewriteTo();
      }
   }

   private TypeVariable rewriteRule0() {
      this.addTypeBound(new TypeAccess("java.lang", "Object"));
      return this;
   }
}
