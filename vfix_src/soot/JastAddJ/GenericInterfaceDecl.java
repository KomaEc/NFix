package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GenericInterfaceDecl extends InterfaceDecl implements Cloneable, GenericTypeDecl {
   protected boolean rawType_computed = false;
   protected TypeDecl rawType_value;
   protected Map lookupParTypeDecl_ArrayList_values;
   protected List lookupParTypeDecl_ArrayList_list;
   protected int usesTypeVariable_visited = -1;
   protected boolean usesTypeVariable_computed = false;
   protected boolean usesTypeVariable_initialized = false;
   protected boolean usesTypeVariable_value;
   protected Map subtype_TypeDecl_values;
   protected Map instanceOf_TypeDecl_values;
   protected Map lookupParTypeDecl_ParTypeAccess_values;

   public void flushCache() {
      super.flushCache();
      this.rawType_computed = false;
      this.rawType_value = null;
      this.lookupParTypeDecl_ArrayList_values = null;
      this.lookupParTypeDecl_ArrayList_list = null;
      this.usesTypeVariable_visited = -1;
      this.usesTypeVariable_computed = false;
      this.usesTypeVariable_initialized = false;
      this.subtype_TypeDecl_values = null;
      this.instanceOf_TypeDecl_values = null;
      this.lookupParTypeDecl_ParTypeAccess_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public GenericInterfaceDecl clone() throws CloneNotSupportedException {
      GenericInterfaceDecl node = (GenericInterfaceDecl)super.clone();
      node.rawType_computed = false;
      node.rawType_value = null;
      node.lookupParTypeDecl_ArrayList_values = null;
      node.lookupParTypeDecl_ArrayList_list = null;
      node.usesTypeVariable_visited = -1;
      node.usesTypeVariable_computed = false;
      node.usesTypeVariable_initialized = false;
      node.subtype_TypeDecl_values = null;
      node.instanceOf_TypeDecl_values = null;
      node.lookupParTypeDecl_ParTypeAccess_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public GenericInterfaceDecl copy() {
      try {
         GenericInterfaceDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public GenericInterfaceDecl fullCopy() {
      GenericInterfaceDecl tree = this.copy();
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

   public void typeCheck() {
      super.typeCheck();
      if (this.instanceOf(this.typeThrowable())) {
         this.error(" generic interface " + this.typeName() + " may not directly or indirectly inherit java.lang.Throwable");
      }

   }

   public InterfaceDecl substitutedInterfaceDecl(Parameterization parTypeDecl) {
      GenericInterfaceDecl c = new GenericInterfaceDeclSubstituted(this.getModifiers().fullCopy(), this.getID(), this.getSuperInterfaceIdList().substitute(parTypeDecl), new List(), this);
      return c;
   }

   public void toString(StringBuffer s) {
      this.getModifiers().toString(s);
      s.append("interface " + this.getID());
      s.append('<');
      int i;
      if (this.getNumTypeParameter() > 0) {
         this.getTypeParameter(0).toString(s);

         for(i = 1; i < this.getNumTypeParameter(); ++i) {
            s.append(", ");
            this.getTypeParameter(i).toString(s);
         }
      }

      s.append('>');
      if (this.getNumSuperInterfaceId() > 0) {
         s.append(" extends ");
         this.getSuperInterfaceId(0).toString(s);

         for(i = 1; i < this.getNumSuperInterfaceId(); ++i) {
            s.append(", ");
            this.getSuperInterfaceId(i).toString(s);
         }
      }

      this.ppBodyDecls(s);
   }

   public TypeDecl makeGeneric(Signatures.ClassSignature s) {
      return this;
   }

   public SimpleSet addTypeVariables(SimpleSet c, String name) {
      GenericTypeDecl original = (GenericTypeDecl)this.original();

      for(int i = 0; i < original.getNumTypeParameter(); ++i) {
         TypeVariable p = original.getTypeParameter(i);
         if (p.name().equals(name)) {
            c = c.add(p);
         }
      }

      return c;
   }

   public List createArgumentList(ArrayList params) {
      GenericTypeDecl original = (GenericTypeDecl)this.original();
      List list = new List();
      if (params.isEmpty()) {
         for(int i = 0; i < original.getNumTypeParameter(); ++i) {
            list.add(original.getTypeParameter(i).erasure().createBoundAccess());
         }
      } else {
         Iterator iter = params.iterator();

         while(iter.hasNext()) {
            list.add(((TypeDecl)iter.next()).createBoundAccess());
         }
      }

      return list;
   }

   public GenericInterfaceDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[4];
      this.setChild(new List(), 1);
      this.setChild(new List(), 2);
      this.setChild(new List(), 3);
   }

   public GenericInterfaceDecl(Modifiers p0, String p1, List<Access> p2, List<BodyDecl> p3, List<TypeVariable> p4) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
   }

   public GenericInterfaceDecl(Modifiers p0, Symbol p1, List<Access> p2, List<BodyDecl> p3, List<TypeVariable> p4) {
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

   public void setTypeParameterList(List<TypeVariable> list) {
      this.setChild(list, 3);
   }

   public int getNumTypeParameter() {
      return this.getTypeParameterList().getNumChild();
   }

   public int getNumTypeParameterNoTransform() {
      return this.getTypeParameterListNoTransform().getNumChildNoTransform();
   }

   public TypeVariable getTypeParameter(int i) {
      return (TypeVariable)this.getTypeParameterList().getChild(i);
   }

   public void addTypeParameter(TypeVariable node) {
      List<TypeVariable> list = this.parent != null && state != null ? this.getTypeParameterList() : this.getTypeParameterListNoTransform();
      list.addChild(node);
   }

   public void addTypeParameterNoTransform(TypeVariable node) {
      List<TypeVariable> list = this.getTypeParameterListNoTransform();
      list.addChild(node);
   }

   public void setTypeParameter(TypeVariable node, int i) {
      List<TypeVariable> list = this.getTypeParameterList();
      list.setChild(node, i);
   }

   public List<TypeVariable> getTypeParameters() {
      return this.getTypeParameterList();
   }

   public List<TypeVariable> getTypeParametersNoTransform() {
      return this.getTypeParameterListNoTransform();
   }

   public List<TypeVariable> getTypeParameterList() {
      List<TypeVariable> list = (List)this.getChild(3);
      list.getNumChild();
      return list;
   }

   public List<TypeVariable> getTypeParameterListNoTransform() {
      return (List)this.getChildNoTransform(3);
   }

   public TypeDecl rawType() {
      if (this.rawType_computed) {
         return this.rawType_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.rawType_value = this.rawType_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.rawType_computed = true;
         }

         return this.rawType_value;
      }
   }

   private TypeDecl rawType_compute() {
      return this.lookupParTypeDecl(new ArrayList());
   }

   public TypeDecl lookupParTypeDecl(ArrayList list) {
      if (this.lookupParTypeDecl_ArrayList_values == null) {
         this.lookupParTypeDecl_ArrayList_values = new HashMap(4);
      }

      if (this.lookupParTypeDecl_ArrayList_values.containsKey(list)) {
         return (TypeDecl)this.lookupParTypeDecl_ArrayList_values.get(list);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         TypeDecl lookupParTypeDecl_ArrayList_value = this.lookupParTypeDecl_compute(list);
         if (this.lookupParTypeDecl_ArrayList_list == null) {
            this.lookupParTypeDecl_ArrayList_list = new List();
            this.lookupParTypeDecl_ArrayList_list.is$Final = true;
            this.lookupParTypeDecl_ArrayList_list.setParent(this);
         }

         this.lookupParTypeDecl_ArrayList_list.add(lookupParTypeDecl_ArrayList_value);
         if (lookupParTypeDecl_ArrayList_value != null) {
            lookupParTypeDecl_ArrayList_value.is$Final = true;
         }

         this.lookupParTypeDecl_ArrayList_values.put(list, lookupParTypeDecl_ArrayList_value);
         return lookupParTypeDecl_ArrayList_value;
      }
   }

   private TypeDecl lookupParTypeDecl_compute(ArrayList list) {
      ParInterfaceDecl typeDecl = list.size() == 0 ? new RawInterfaceDecl() : new ParInterfaceDecl();
      ((ParInterfaceDecl)typeDecl).setModifiers(this.getModifiers().fullCopy());
      ((ParInterfaceDecl)typeDecl).setID(this.getID());
      if (!(typeDecl instanceof RawInterfaceDecl)) {
         ((ParInterfaceDecl)typeDecl).setArgumentList(this.createArgumentList(list));
      }

      return (TypeDecl)typeDecl;
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
      return type.supertypeGenericInterfaceDecl(this);
   }

   public boolean supertypeParClassDecl(ParClassDecl type) {
      ASTNode$State state = this.state();
      return type.genericDecl().original().subtype(this);
   }

   public boolean supertypeParInterfaceDecl(ParInterfaceDecl type) {
      ASTNode$State state = this.state();
      return type.genericDecl().original().subtype(this);
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

   public boolean isGenericType() {
      ASTNode$State state = this.state();
      return true;
   }

   public TypeDecl lookupParTypeDecl(ParTypeAccess p) {
      if (this.lookupParTypeDecl_ParTypeAccess_values == null) {
         this.lookupParTypeDecl_ParTypeAccess_values = new HashMap(4);
      }

      if (this.lookupParTypeDecl_ParTypeAccess_values.containsKey(p)) {
         return (TypeDecl)this.lookupParTypeDecl_ParTypeAccess_values.get(p);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         TypeDecl lookupParTypeDecl_ParTypeAccess_value = this.lookupParTypeDecl_compute(p);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.lookupParTypeDecl_ParTypeAccess_values.put(p, lookupParTypeDecl_ParTypeAccess_value);
         }

         return lookupParTypeDecl_ParTypeAccess_value;
      }
   }

   private TypeDecl lookupParTypeDecl_compute(ParTypeAccess p) {
      ArrayList typeArguments = new ArrayList();

      for(int i = 0; i < p.getNumTypeArgument(); ++i) {
         typeArguments.add(p.getTypeArgument(i).type());
      }

      return this.lookupParTypeDecl(typeArguments);
   }

   public TypeDecl typeThrowable() {
      ASTNode$State state = this.state();
      TypeDecl typeThrowable_value = this.getParent().Define_TypeDecl_typeThrowable(this, (ASTNode)null);
      return typeThrowable_value;
   }

   public boolean Define_boolean_isNestedType(ASTNode caller, ASTNode child) {
      if (caller == this.getTypeParameterListNoTransform()) {
         caller.getIndexOfChild(child);
         return true;
      } else {
         return super.Define_boolean_isNestedType(caller, child);
      }
   }

   public TypeDecl Define_TypeDecl_enclosingType(ASTNode caller, ASTNode child) {
      if (caller == this.getTypeParameterListNoTransform()) {
         caller.getIndexOfChild(child);
         return this;
      } else {
         return super.Define_TypeDecl_enclosingType(caller, child);
      }
   }

   public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
      SimpleSet c;
      Iterator iter;
      TypeDecl d;
      if (caller == this.getBodyDeclListNoTransform()) {
         int index = caller.getIndexOfChild(child);
         c = this.memberTypes(name);
         if (this.getBodyDecl(index).visibleTypeParameters()) {
            c = this.addTypeVariables(c, name);
         }

         if (!c.isEmpty()) {
            return c;
         } else if (this.isClassDecl() && this.isStatic() && !this.isTopLevelType()) {
            iter = this.lookupType(name).iterator();

            while(true) {
               do {
                  if (!iter.hasNext()) {
                     return !c.isEmpty() ? c : this.topLevelType().lookupType(name);
                  }

                  d = (TypeDecl)iter.next();
               } while(!d.isStatic() && (d.enclosingType() == null || !this.instanceOf(d.enclosingType())));

               c = c.add(d);
            }
         } else {
            c = this.lookupType(name);
            return !c.isEmpty() ? c : this.topLevelType().lookupType(name);
         }
      } else if (caller == this.getTypeParameterListNoTransform()) {
         caller.getIndexOfChild(child);
         c = this.memberTypes(name);
         c = this.addTypeVariables(c, name);
         if (!c.isEmpty()) {
            return c;
         } else if (this.isClassDecl() && this.isStatic() && !this.isTopLevelType()) {
            iter = this.lookupType(name).iterator();

            while(true) {
               do {
                  if (!iter.hasNext()) {
                     return !c.isEmpty() ? c : this.topLevelType().lookupType(name);
                  }

                  d = (TypeDecl)iter.next();
               } while(!d.isStatic() && (d.enclosingType() == null || !this.instanceOf(d.enclosingType())));

               c = c.add(d);
            }
         } else {
            c = this.lookupType(name);
            return !c.isEmpty() ? c : this.topLevelType().lookupType(name);
         }
      } else if (caller == this.getSuperInterfaceIdListNoTransform()) {
         caller.getIndexOfChild(child);
         c = this.addTypeVariables(SimpleSet.emptySet, name);
         return !c.isEmpty() ? c : this.lookupType(name);
      } else {
         return super.Define_SimpleSet_lookupType(caller, child, name);
      }
   }

   public TypeDecl Define_TypeDecl_genericDecl(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
