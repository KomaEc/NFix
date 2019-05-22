package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RawClassDecl extends ParClassDecl implements Cloneable {
   protected boolean getArgumentList_computed = false;
   protected List getArgumentList_value;
   protected Map subtype_TypeDecl_values;
   protected Map instanceOf_TypeDecl_values;

   public void flushCache() {
      super.flushCache();
      this.getArgumentList_computed = false;
      this.getArgumentList_value = null;
      this.subtype_TypeDecl_values = null;
      this.instanceOf_TypeDecl_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public RawClassDecl clone() throws CloneNotSupportedException {
      RawClassDecl node = (RawClassDecl)super.clone();
      node.getArgumentList_computed = false;
      node.getArgumentList_value = null;
      node.subtype_TypeDecl_values = null;
      node.instanceOf_TypeDecl_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public RawClassDecl copy() {
      try {
         RawClassDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public RawClassDecl fullCopy() {
      RawClassDecl tree = this.copy();
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            switch(i) {
            case 2:
               tree.children[i] = new Opt();
               break;
            case 3:
            case 4:
            case 5:
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

   public Access substitute(Parameterization parTypeDecl) {
      return this.createBoundAccess();
   }

   public Access substituteReturnType(Parameterization parTypeDecl) {
      return this.createBoundAccess();
   }

   public Access substituteParameterType(Parameterization parTypeDecl) {
      return this.createBoundAccess();
   }

   public RawClassDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[5];
      this.setChild(new Opt(), 1);
      this.setChild(new List(), 2);
      this.setChild(new List(), 3);
      this.setChild(new List(), 4);
   }

   public RawClassDecl(Modifiers p0, String p1) {
      this.setChild(p0, 0);
      this.setID(p1);
   }

   public RawClassDecl(Modifiers p0, Symbol p1) {
      this.setChild(p0, 0);
      this.setID(p1);
   }

   protected int numChildren() {
      return 1;
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

   public Opt<Access> getSuperClassAccessOptNoTransform() {
      return (Opt)this.getChildNoTransform(1);
   }

   protected int getSuperClassAccessOptChildPosition() {
      return 1;
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

   public List<Access> getImplementsListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   protected int getImplementsListChildPosition() {
      return 2;
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

   public List<BodyDecl> getBodyDeclListNoTransform() {
      return (List)this.getChildNoTransform(3);
   }

   protected int getBodyDeclListChildPosition() {
      return 3;
   }

   public void setArgumentList(List<Access> list) {
      this.setChild(list, 4);
   }

   public int getNumArgument() {
      return this.getArgumentList().getNumChild();
   }

   public int getNumArgumentNoTransform() {
      return this.getArgumentListNoTransform().getNumChildNoTransform();
   }

   public Access getArgument(int i) {
      return (Access)this.getArgumentList().getChild(i);
   }

   public void addArgument(Access node) {
      List<Access> list = this.parent != null && state != null ? this.getArgumentList() : this.getArgumentListNoTransform();
      list.addChild(node);
   }

   public void addArgumentNoTransform(Access node) {
      List<Access> list = this.getArgumentListNoTransform();
      list.addChild(node);
   }

   public void setArgument(Access node, int i) {
      List<Access> list = this.getArgumentList();
      list.setChild(node, i);
   }

   public List<Access> getArguments() {
      return this.getArgumentList();
   }

   public List<Access> getArgumentsNoTransform() {
      return this.getArgumentListNoTransform();
   }

   public List<Access> getArgumentListNoTransform() {
      return (List)this.getChildNoTransform(4);
   }

   protected int getArgumentListChildPosition() {
      return 4;
   }

   public TypeDecl hostType() {
      ASTNode$State state = this.state();
      return this.original();
   }

   public boolean isRawType() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean sameSignature(Access a) {
      ASTNode$State state = this.state();
      return a instanceof TypeAccess && a.type() == this;
   }

   public List getArgumentList() {
      if (this.getArgumentList_computed) {
         return (List)this.getChild(this.getArgumentListChildPosition());
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getArgumentList_value = this.getArgumentList_compute();
         this.setArgumentList(this.getArgumentList_value);
         this.getArgumentList_computed = true;
         return (List)this.getChild(this.getArgumentListChildPosition());
      }
   }

   private List getArgumentList_compute() {
      return ((GenericClassDecl)this.genericDecl()).createArgumentList(new ArrayList());
   }

   public String nameWithArgs() {
      ASTNode$State state = this.state();
      return this.name();
   }

   public boolean supertypeGenericClassDecl(GenericClassDecl type) {
      ASTNode$State state = this.state();
      return type.subtype(this.genericDecl().original());
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
      return type.supertypeRawClassDecl(this);
   }

   public boolean supertypeClassDecl(ClassDecl type) {
      ASTNode$State state = this.state();
      return type.subtype(this.genericDecl().original());
   }

   public boolean supertypeInterfaceDecl(InterfaceDecl type) {
      ASTNode$State state = this.state();
      return type.subtype(this.genericDecl().original());
   }

   public boolean supertypeParClassDecl(ParClassDecl type) {
      ASTNode$State state = this.state();
      return type.genericDecl().original().subtype(this.genericDecl().original());
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
