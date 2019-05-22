package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Map;

public class AnnotationDecl extends InterfaceDecl implements Cloneable {
   protected boolean getSuperInterfaceIdList_computed = false;
   protected List getSuperInterfaceIdList_value;
   protected Map containsElementOf_TypeDecl_values;

   public void flushCache() {
      super.flushCache();
      this.getSuperInterfaceIdList_computed = false;
      this.getSuperInterfaceIdList_value = null;
      this.containsElementOf_TypeDecl_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public AnnotationDecl clone() throws CloneNotSupportedException {
      AnnotationDecl node = (AnnotationDecl)super.clone();
      node.getSuperInterfaceIdList_computed = false;
      node.getSuperInterfaceIdList_value = null;
      node.containsElementOf_TypeDecl_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public AnnotationDecl copy() {
      try {
         AnnotationDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public AnnotationDecl fullCopy() {
      AnnotationDecl tree = this.copy();
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            switch(i) {
            case 3:
               tree.children[i] = new List();
               break;
            default:
               ASTNode child = this.children[i];
               if (child != null) {
                  child = child.fullCopy();
                  tree.setChild(child, i);
               }
            }
         }
      }

      return tree;
   }

   public void typeCheck() {
      super.typeCheck();

      for(int i = 0; i < this.getNumBodyDecl(); ++i) {
         if (this.getBodyDecl(i) instanceof MethodDecl) {
            MethodDecl m = (MethodDecl)this.getBodyDecl(i);
            if (!m.type().isValidAnnotationMethodReturnType()) {
               m.error("invalid type for annotation member");
            }

            if (m.annotationMethodOverride()) {
               m.error("annotation method overrides " + m.signature());
            }
         }
      }

      if (this.containsElementOf(this)) {
         this.error("cyclic annotation element type");
      }

   }

   public void toString(StringBuffer s) {
      this.getModifiers().toString(s);
      s.append("@interface " + this.name());
      s.append(" {");

      for(int i = 0; i < this.getNumBodyDecl(); ++i) {
         this.getBodyDecl(i).toString(s);
      }

      s.append(this.indent() + "}");
   }

   public AnnotationDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new List(), 1);
      this.setChild(new List(), 2);
   }

   public AnnotationDecl(Modifiers p0, String p1, List<BodyDecl> p2) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
   }

   public AnnotationDecl(Modifiers p0, Symbol p1, List<BodyDecl> p2) {
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

   public void setSuperInterfaceIdList(List<Access> list) {
      this.setChild(list, 2);
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

   public List<Access> getSuperInterfaceIdListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   protected int getSuperInterfaceIdListChildPosition() {
      return 2;
   }

   public List getSuperInterfaceIdList() {
      if (this.getSuperInterfaceIdList_computed) {
         return (List)this.getChild(this.getSuperInterfaceIdListChildPosition());
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getSuperInterfaceIdList_value = this.getSuperInterfaceIdList_compute();
         this.setSuperInterfaceIdList(this.getSuperInterfaceIdList_value);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getSuperInterfaceIdList_computed = true;
         }

         return (List)this.getChild(this.getSuperInterfaceIdListChildPosition());
      }
   }

   private List getSuperInterfaceIdList_compute() {
      return (new List()).add(new TypeAccess("java.lang.annotation", "Annotation"));
   }

   public boolean isValidAnnotationMethodReturnType() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean containsElementOf(TypeDecl typeDecl) {
      if (this.containsElementOf_TypeDecl_values == null) {
         this.containsElementOf_TypeDecl_values = new HashMap(4);
      }

      ASTNode$State.CircularValue _value;
      if (this.containsElementOf_TypeDecl_values.containsKey(typeDecl)) {
         Object _o = this.containsElementOf_TypeDecl_values.get(typeDecl);
         if (!(_o instanceof ASTNode$State.CircularValue)) {
            return (Boolean)_o;
         }

         _value = (ASTNode$State.CircularValue)_o;
      } else {
         _value = new ASTNode$State.CircularValue();
         this.containsElementOf_TypeDecl_values.put(typeDecl, _value);
         _value.value = false;
      }

      ASTNode$State state = this.state();
      if (state.IN_CIRCLE) {
         if (!(new Integer(state.CIRCLE_INDEX)).equals(_value.visited)) {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            boolean new_containsElementOf_TypeDecl_value = this.containsElementOf_compute(typeDecl);
            if (state.RESET_CYCLE) {
               this.containsElementOf_TypeDecl_values.remove(typeDecl);
            } else if (new_containsElementOf_TypeDecl_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_containsElementOf_TypeDecl_value;
            }

            return new_containsElementOf_TypeDecl_value;
         } else {
            return (Boolean)_value.value;
         }
      } else {
         state.IN_CIRCLE = true;
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();

         boolean new_containsElementOf_TypeDecl_value;
         do {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            state.CHANGE = false;
            new_containsElementOf_TypeDecl_value = this.containsElementOf_compute(typeDecl);
            if (new_containsElementOf_TypeDecl_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_containsElementOf_TypeDecl_value;
            }

            ++state.CIRCLE_INDEX;
         } while(state.CHANGE);

         if (isFinal && num == this.state().boundariesCrossed) {
            this.containsElementOf_TypeDecl_values.put(typeDecl, new_containsElementOf_TypeDecl_value);
         } else {
            this.containsElementOf_TypeDecl_values.remove(typeDecl);
            state.RESET_CYCLE = true;
            this.containsElementOf_compute(typeDecl);
            state.RESET_CYCLE = false;
         }

         state.IN_CIRCLE = false;
         return new_containsElementOf_TypeDecl_value;
      }
   }

   private boolean containsElementOf_compute(TypeDecl typeDecl) {
      for(int i = 0; i < this.getNumBodyDecl(); ++i) {
         if (this.getBodyDecl(i) instanceof MethodDecl) {
            MethodDecl m = (MethodDecl)this.getBodyDecl(i);
            if (m.type() == typeDecl) {
               return true;
            }

            if (m.type() instanceof AnnotationDecl && ((AnnotationDecl)m.type()).containsElementOf(typeDecl)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean isAnnotationDecl() {
      ASTNode$State state = this.state();
      return true;
   }

   public int sootTypeModifiers() {
      ASTNode$State state = this.state();
      return super.sootTypeModifiers() | 8192;
   }

   public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
      if (caller != this.getModifiersNoTransform()) {
         return super.Define_boolean_mayUseAnnotationTarget(caller, child, name);
      } else {
         return name.equals("ANNOTATION_TYPE") || name.equals("TYPE");
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
