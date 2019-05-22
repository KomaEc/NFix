package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Map;
import soot.Type;

public class VoidType extends TypeDecl implements Cloneable {
   protected Map instanceOf_TypeDecl_values;
   protected Map subtype_TypeDecl_values;
   protected boolean getSootType_computed = false;
   protected Type getSootType_value;

   public void flushCache() {
      super.flushCache();
      this.instanceOf_TypeDecl_values = null;
      this.subtype_TypeDecl_values = null;
      this.getSootType_computed = false;
      this.getSootType_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public VoidType clone() throws CloneNotSupportedException {
      VoidType node = (VoidType)super.clone();
      node.instanceOf_TypeDecl_values = null;
      node.subtype_TypeDecl_values = null;
      node.getSootType_computed = false;
      node.getSootType_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public VoidType copy() {
      try {
         VoidType node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public VoidType fullCopy() {
      VoidType tree = this.copy();
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

   public void toString(StringBuffer s) {
      s.append("void");
   }

   public VoidType() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
      this.setChild(new List(), 1);
   }

   public VoidType(Modifiers p0, String p1, List<BodyDecl> p2) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
   }

   public VoidType(Modifiers p0, Symbol p1, List<BodyDecl> p2) {
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

   public boolean stringConversion() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isVoid() {
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

   public boolean isSupertypeOfVoidType(VoidType type) {
      ASTNode$State state = this.state();
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
      return type.supertypeVoidType(this);
   }

   public boolean supertypeVoidType(VoidType type) {
      ASTNode$State state = this.state();
      return true;
   }

   public String primitiveClassName() {
      ASTNode$State state = this.state();
      return "Void";
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
      return soot.VoidType.v();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
