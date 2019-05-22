package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Map;

public class WildcardSuperType extends AbstractWildcardType implements Cloneable {
   protected int involvesTypeParameters_visited = -1;
   protected boolean involvesTypeParameters_computed = false;
   protected boolean involvesTypeParameters_initialized = false;
   protected boolean involvesTypeParameters_value;
   protected int usesTypeVariable_visited = -1;
   protected boolean usesTypeVariable_computed = false;
   protected boolean usesTypeVariable_initialized = false;
   protected boolean usesTypeVariable_value;
   protected Map subtype_TypeDecl_values;
   protected Map containedIn_TypeDecl_values;
   protected Map sameStructure_TypeDecl_values;
   protected Map instanceOf_TypeDecl_values;

   public void flushCache() {
      super.flushCache();
      this.involvesTypeParameters_visited = -1;
      this.involvesTypeParameters_computed = false;
      this.involvesTypeParameters_initialized = false;
      this.usesTypeVariable_visited = -1;
      this.usesTypeVariable_computed = false;
      this.usesTypeVariable_initialized = false;
      this.subtype_TypeDecl_values = null;
      this.containedIn_TypeDecl_values = null;
      this.sameStructure_TypeDecl_values = null;
      this.instanceOf_TypeDecl_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public WildcardSuperType clone() throws CloneNotSupportedException {
      WildcardSuperType node = (WildcardSuperType)super.clone();
      node.involvesTypeParameters_visited = -1;
      node.involvesTypeParameters_computed = false;
      node.involvesTypeParameters_initialized = false;
      node.usesTypeVariable_visited = -1;
      node.usesTypeVariable_computed = false;
      node.usesTypeVariable_initialized = false;
      node.subtype_TypeDecl_values = null;
      node.containedIn_TypeDecl_values = null;
      node.sameStructure_TypeDecl_values = null;
      node.instanceOf_TypeDecl_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public WildcardSuperType copy() {
      try {
         WildcardSuperType node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public WildcardSuperType fullCopy() {
      WildcardSuperType tree = this.copy();
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

   public Access substitute(Parameterization parTypeDecl) {
      return (Access)(!this.usesTypeVariable() ? super.substitute(parTypeDecl) : new WildcardSuper(this.getAccess().type().substitute(parTypeDecl)));
   }

   public WildcardSuperType() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new List(), 1);
   }

   public WildcardSuperType(Modifiers p0, String p1, List<BodyDecl> p2, Access p3) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   public WildcardSuperType(Modifiers p0, Symbol p1, List<BodyDecl> p2, Access p3) {
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

   public void setAccess(Access node) {
      this.setChild(node, 2);
   }

   public Access getAccess() {
      return (Access)this.getChild(2);
   }

   public Access getAccessNoTransform() {
      return (Access)this.getChildNoTransform(2);
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
      return this.superType().involvesTypeParameters();
   }

   public boolean sameSignature(Access a) {
      ASTNode$State state = this.state();
      return a instanceof WildcardSuper ? this.getAccess().type().sameSignature(((WildcardSuper)a).getAccess()) : super.sameSignature(a);
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
      return this.getAccess().type().usesTypeVariable();
   }

   public TypeDecl superType() {
      ASTNode$State state = this.state();
      return this.getAccess().type();
   }

   public boolean supertypeWildcard(WildcardType type) {
      ASTNode$State state = this.state();
      return this.superType().subtype(this.typeObject());
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
      return type.supertypeWildcardSuper(this);
   }

   public boolean supertypeWildcardSuper(WildcardSuperType type) {
      ASTNode$State state = this.state();
      return type.superType().subtype(this.superType());
   }

   public boolean supertypeClassDecl(ClassDecl type) {
      ASTNode$State state = this.state();
      return this.superType().subtype(type);
   }

   public boolean supertypeInterfaceDecl(InterfaceDecl type) {
      ASTNode$State state = this.state();
      return this.superType().subtype(type);
   }

   public boolean supertypeParClassDecl(ParClassDecl type) {
      ASTNode$State state = this.state();
      return this.superType().subtype(type);
   }

   public boolean supertypeParInterfaceDecl(ParInterfaceDecl type) {
      ASTNode$State state = this.state();
      return this.superType().subtype(type);
   }

   public boolean supertypeRawClassDecl(RawClassDecl type) {
      ASTNode$State state = this.state();
      return this.superType().subtype(type);
   }

   public boolean supertypeRawInterfaceDecl(RawInterfaceDecl type) {
      ASTNode$State state = this.state();
      return this.superType().subtype(type);
   }

   public boolean supertypeTypeVariable(TypeVariable type) {
      ASTNode$State state = this.state();
      return this.superType().subtype(type);
   }

   public boolean supertypeArrayDecl(ArrayDecl type) {
      ASTNode$State state = this.state();
      return this.superType().subtype(type);
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
         return type instanceof WildcardSuperType ? ((WildcardSuperType)type).superType().subtype(this.superType()) : false;
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
      return super.sameStructure(t) || t instanceof WildcardSuperType && ((WildcardSuperType)t).superType().sameStructure(this.superType());
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

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
