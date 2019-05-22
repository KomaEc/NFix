package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Map;
import soot.ArrayType;
import soot.SootClass;
import soot.Type;

public class ArrayDecl extends ClassDecl implements Cloneable {
   protected Map accessibleFrom_TypeDecl_values;
   protected boolean dimension_computed = false;
   protected int dimension_value;
   protected boolean elementType_computed = false;
   protected TypeDecl elementType_value;
   protected boolean fullName_computed = false;
   protected String fullName_value;
   protected boolean typeName_computed = false;
   protected String typeName_value;
   protected Map castingConversionTo_TypeDecl_values;
   protected Map instanceOf_TypeDecl_values;
   protected int involvesTypeParameters_visited = -1;
   protected boolean involvesTypeParameters_computed = false;
   protected boolean involvesTypeParameters_initialized = false;
   protected boolean involvesTypeParameters_value;
   protected boolean erasure_computed = false;
   protected TypeDecl erasure_value;
   protected int usesTypeVariable_visited = -1;
   protected boolean usesTypeVariable_computed = false;
   protected boolean usesTypeVariable_initialized = false;
   protected boolean usesTypeVariable_value;
   protected Map subtype_TypeDecl_values;
   protected boolean jvmName_computed = false;
   protected String jvmName_value;
   protected boolean getSootClassDecl_computed = false;
   protected SootClass getSootClassDecl_value;
   protected boolean getSootType_computed = false;
   protected Type getSootType_value;

   public void flushCache() {
      super.flushCache();
      this.accessibleFrom_TypeDecl_values = null;
      this.dimension_computed = false;
      this.elementType_computed = false;
      this.elementType_value = null;
      this.fullName_computed = false;
      this.fullName_value = null;
      this.typeName_computed = false;
      this.typeName_value = null;
      this.castingConversionTo_TypeDecl_values = null;
      this.instanceOf_TypeDecl_values = null;
      this.involvesTypeParameters_visited = -1;
      this.involvesTypeParameters_computed = false;
      this.involvesTypeParameters_initialized = false;
      this.erasure_computed = false;
      this.erasure_value = null;
      this.usesTypeVariable_visited = -1;
      this.usesTypeVariable_computed = false;
      this.usesTypeVariable_initialized = false;
      this.subtype_TypeDecl_values = null;
      this.jvmName_computed = false;
      this.jvmName_value = null;
      this.getSootClassDecl_computed = false;
      this.getSootClassDecl_value = null;
      this.getSootType_computed = false;
      this.getSootType_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ArrayDecl clone() throws CloneNotSupportedException {
      ArrayDecl node = (ArrayDecl)super.clone();
      node.accessibleFrom_TypeDecl_values = null;
      node.dimension_computed = false;
      node.elementType_computed = false;
      node.elementType_value = null;
      node.fullName_computed = false;
      node.fullName_value = null;
      node.typeName_computed = false;
      node.typeName_value = null;
      node.castingConversionTo_TypeDecl_values = null;
      node.instanceOf_TypeDecl_values = null;
      node.involvesTypeParameters_visited = -1;
      node.involvesTypeParameters_computed = false;
      node.involvesTypeParameters_initialized = false;
      node.erasure_computed = false;
      node.erasure_value = null;
      node.usesTypeVariable_visited = -1;
      node.usesTypeVariable_computed = false;
      node.usesTypeVariable_initialized = false;
      node.subtype_TypeDecl_values = null;
      node.jvmName_computed = false;
      node.jvmName_value = null;
      node.getSootClassDecl_computed = false;
      node.getSootClassDecl_value = null;
      node.getSootType_computed = false;
      node.getSootType_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ArrayDecl copy() {
      try {
         ArrayDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ArrayDecl fullCopy() {
      ArrayDecl tree = this.copy();
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

   public Access createQualifiedAccess() {
      return new ArrayTypeAccess(this.componentType().createQualifiedAccess());
   }

   public Access substitute(Parameterization parTypeDecl) {
      return new ArrayTypeAccess(this.componentType().substitute(parTypeDecl));
   }

   public Access substituteReturnType(Parameterization parTypeDecl) {
      return new ArrayTypeAccess(this.componentType().substituteReturnType(parTypeDecl));
   }

   public ArrayDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[4];
      this.setChild(new Opt(), 1);
      this.setChild(new List(), 2);
      this.setChild(new List(), 3);
   }

   public ArrayDecl(Modifiers p0, String p1, Opt<Access> p2, List<Access> p3, List<BodyDecl> p4) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
   }

   public ArrayDecl(Modifiers p0, Symbol p1, Opt<Access> p2, List<Access> p3, List<BodyDecl> p4) {
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

   private boolean refined_TypeConversion_ArrayDecl_castingConversionTo_TypeDecl(TypeDecl type) {
      if (type.isArrayDecl()) {
         TypeDecl SC = this.componentType();
         TypeDecl TC = type.componentType();
         if (SC.isPrimitiveType() && TC.isPrimitiveType() && SC == TC) {
            return true;
         } else {
            return SC.isReferenceType() && TC.isReferenceType() ? SC.castingConversionTo(TC) : false;
         }
      } else if (type.isClassDecl()) {
         return type.isObject();
      } else if (!type.isInterfaceDecl()) {
         return super.castingConversionTo(type);
      } else {
         return type == this.typeSerializable() || type == this.typeCloneable();
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
      return this.elementType().accessibleFrom(type);
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
      return this.componentType().dimension() + 1;
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
      return this.componentType().elementType();
   }

   public String name() {
      ASTNode$State state = this.state();
      return this.fullName();
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
      return this.getID();
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
      return this.componentType().typeName() + "[]";
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
      if (!(type instanceof TypeVariable)) {
         return this.refined_TypeConversion_ArrayDecl_castingConversionTo_TypeDecl(type);
      } else {
         TypeVariable t = (TypeVariable)type;
         if (!type.isReferenceType()) {
            return false;
         } else if (t.getNumTypeBound() == 0) {
            return true;
         } else {
            for(int i = 0; i < t.getNumTypeBound(); ++i) {
               TypeDecl bound = t.getTypeBound(i).type();
               if (bound.isObject() || bound == this.typeSerializable() || bound == this.typeCloneable()) {
                  return true;
               }

               if (bound.isTypeVariable() && this.castingConversionTo(bound)) {
                  return true;
               }

               if (bound.isArrayDecl() && this.castingConversionTo(bound)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public boolean isArrayDecl() {
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

   public boolean isSupertypeOfArrayDecl(ArrayDecl type) {
      ASTNode$State state = this.state();
      if (type.elementType().isPrimitive() && this.elementType().isPrimitive()) {
         return type.dimension() == this.dimension() && type.elementType() == this.elementType();
      } else {
         return type.componentType().instanceOf(this.componentType());
      }
   }

   public boolean isValidAnnotationMethodReturnType() {
      ASTNode$State state = this.state();
      return this.componentType().isValidAnnotationMethodReturnType();
   }

   public boolean commensurateWith(ElementValue value) {
      ASTNode$State state = this.state();
      return value.commensurateWithArrayDecl(this);
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
      return this.componentType().involvesTypeParameters();
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
      return this.componentType().erasure().arrayType();
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
      return this.elementType().usesTypeVariable();
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
      return type.supertypeArrayDecl(this);
   }

   public boolean supertypeArrayDecl(ArrayDecl type) {
      ASTNode$State state = this.state();
      if (type.elementType().isPrimitive() && this.elementType().isPrimitive()) {
         return type.dimension() == this.dimension() && type.elementType() == this.elementType();
      } else {
         return type.componentType().subtype(this.componentType());
      }
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
      StringBuffer dim = new StringBuffer();

      for(int i = 0; i < this.dimension(); ++i) {
         dim.append("[");
      }

      return this.elementType().isReferenceType() ? dim.toString() + "L" + this.elementType().jvmName() + ";" : dim.toString() + this.elementType().jvmName();
   }

   public String referenceClassFieldName() {
      ASTNode$State state = this.state();
      return "array" + this.jvmName().replace('[', '$').replace('.', '$').replace(';', ' ').trim();
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
      return this.typeObject().getSootClassDecl();
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
      return ArrayType.v(this.elementType().getSootType(), this.dimension());
   }

   public boolean isReifiable() {
      ASTNode$State state = this.state();
      return this.elementType().isReifiable();
   }

   public TypeDecl typeSerializable() {
      ASTNode$State state = this.state();
      TypeDecl typeSerializable_value = this.getParent().Define_TypeDecl_typeSerializable(this, (ASTNode)null);
      return typeSerializable_value;
   }

   public TypeDecl typeCloneable() {
      ASTNode$State state = this.state();
      TypeDecl typeCloneable_value = this.getParent().Define_TypeDecl_typeCloneable(this, (ASTNode)null);
      return typeCloneable_value;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
