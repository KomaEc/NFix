package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import soot.SootClass;

public class GLBType extends ReferenceType implements Cloneable {
   protected Map subtype_TypeDecl_values;
   protected boolean getSootClassDecl_computed = false;
   protected SootClass getSootClassDecl_value;

   public void flushCache() {
      super.flushCache();
      this.subtype_TypeDecl_values = null;
      this.getSootClassDecl_computed = false;
      this.getSootClassDecl_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public GLBType clone() throws CloneNotSupportedException {
      GLBType node = (GLBType)super.clone();
      node.subtype_TypeDecl_values = null;
      node.getSootClassDecl_computed = false;
      node.getSootClassDecl_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public GLBType copy() {
      try {
         GLBType node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public GLBType fullCopy() {
      GLBType tree = this.copy();
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

   public HashSet implementedInterfaces() {
      HashSet ret = new HashSet();

      for(int i = 0; i < this.getNumTypeBound(); ++i) {
         ret.addAll(this.getTypeBound(i).type().implementedInterfaces());
      }

      return ret;
   }

   public GLBType() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new List(), 1);
      this.setChild(new List(), 2);
   }

   public GLBType(Modifiers p0, String p1, List<BodyDecl> p2, List<Access> p3) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   public GLBType(Modifiers p0, Symbol p1, List<BodyDecl> p2, List<Access> p3) {
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

   public String typeName() {
      ASTNode$State state = this.state();
      if (this.getNumTypeBound() == 0) {
         return "<NOTYPE>";
      } else {
         StringBuffer s = new StringBuffer();
         s.append(this.getTypeBound(0).type().typeName());

         for(int i = 1; i < this.getNumTypeBound(); ++i) {
            s.append(" & " + this.getTypeBound(i).type().typeName());
         }

         return s.toString();
      }
   }

   public boolean supertypeLUBType(LUBType type) {
      ASTNode$State state = this.state();
      ArrayList bounds = new ArrayList(this.getNumTypeBound());

      for(int i = 0; i < this.getNumTypeBound(); ++i) {
         bounds.add(this.getTypeBound(i));
      }

      return type == this.lookupLUBType(bounds);
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
      return type.supertypeGLBType(this);
   }

   public boolean supertypeGLBType(GLBType type) {
      ASTNode$State state = this.state();
      return this == type;
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

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
