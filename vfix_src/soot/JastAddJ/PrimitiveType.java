package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Map;

public class PrimitiveType extends TypeDecl implements Cloneable {
   protected Map narrowingConversionTo_TypeDecl_values;
   protected Map instanceOf_TypeDecl_values;
   protected Map subtype_TypeDecl_values;

   public void flushCache() {
      super.flushCache();
      this.narrowingConversionTo_TypeDecl_values = null;
      this.instanceOf_TypeDecl_values = null;
      this.subtype_TypeDecl_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public PrimitiveType clone() throws CloneNotSupportedException {
      PrimitiveType node = (PrimitiveType)super.clone();
      node.narrowingConversionTo_TypeDecl_values = null;
      node.instanceOf_TypeDecl_values = null;
      node.subtype_TypeDecl_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public PrimitiveType copy() {
      try {
         PrimitiveType node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public PrimitiveType fullCopy() {
      PrimitiveType tree = this.copy();
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
      return new PrimitiveTypeAccess(this.name());
   }

   public boolean hasSuperclass() {
      return !this.isObject();
   }

   public PrimitiveType() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new Opt(), 1);
      this.setChild(new List(), 2);
   }

   public PrimitiveType(Modifiers p0, String p1, Opt<Access> p2, List<BodyDecl> p3) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   public PrimitiveType(Modifiers p0, Symbol p1, Opt<Access> p2, List<BodyDecl> p3) {
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
      return type.instanceOf(this);
   }

   public boolean isPrimitiveType() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isPrimitive() {
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

   public boolean isSupertypeOfPrimitiveType(PrimitiveType type) {
      ASTNode$State state = this.state();
      if (super.isSupertypeOfPrimitiveType(type)) {
         return true;
      } else {
         return type.hasSuperclass() && type.superclass().isPrimitive() && type.superclass().instanceOf(this);
      }
   }

   public TypeDecl superclass() {
      ASTNode$State state = this.state();
      return this.getSuperClassAccess().type();
   }

   public boolean isValidAnnotationMethodReturnType() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean boxingConversionTo(TypeDecl typeDecl) {
      ASTNode$State state = this.state();
      return this.boxed() == typeDecl;
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
      return type.supertypePrimitiveType(this);
   }

   public boolean supertypePrimitiveType(PrimitiveType type) {
      ASTNode$State state = this.state();
      if (super.supertypePrimitiveType(type)) {
         return true;
      } else {
         return type.hasSuperclass() && type.superclass().isPrimitive() && type.superclass().subtype(this);
      }
   }

   public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
      return caller == this.getSuperClassAccessOptNoTransform() ? this.hostType() : super.Define_TypeDecl_hostType(caller, child);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
