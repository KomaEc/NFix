package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class ParClassDecl extends ClassDecl implements Cloneable, ParTypeDecl, MemberSubstitutor {
   protected int involvesTypeParameters_visited = -1;
   protected boolean involvesTypeParameters_computed = false;
   protected boolean involvesTypeParameters_initialized = false;
   protected boolean involvesTypeParameters_value;
   protected boolean erasure_computed = false;
   protected TypeDecl erasure_value;
   protected boolean getSuperClassAccessOpt_computed = false;
   protected Opt getSuperClassAccessOpt_value;
   protected boolean getImplementsList_computed = false;
   protected List getImplementsList_value;
   protected boolean getBodyDeclList_computed = false;
   protected List getBodyDeclList_value;
   protected Map subtype_TypeDecl_values;
   protected Map sameStructure_TypeDecl_values;
   protected Map instanceOf_TypeDecl_values;
   protected Map sameSignature_ArrayList_values;
   protected int usesTypeVariable_visited = -1;
   protected boolean usesTypeVariable_computed = false;
   protected boolean usesTypeVariable_initialized = false;
   protected boolean usesTypeVariable_value;
   protected boolean sourceTypeDecl_computed = false;
   protected TypeDecl sourceTypeDecl_value;
   protected boolean fullName_computed = false;
   protected String fullName_value;
   protected boolean typeName_computed = false;
   protected String typeName_value;
   protected boolean unimplementedMethods_computed = false;
   protected Collection unimplementedMethods_value;
   protected boolean localMethodsSignatureMap_computed = false;
   protected HashMap localMethodsSignatureMap_value;
   protected Map localFields_String_values;
   protected Map localTypeDecls_String_values;
   protected boolean constructors_computed = false;
   protected Collection constructors_value;
   protected boolean genericDecl_computed = false;
   protected TypeDecl genericDecl_value;

   public void flushCache() {
      super.flushCache();
      this.involvesTypeParameters_visited = -1;
      this.involvesTypeParameters_computed = false;
      this.involvesTypeParameters_initialized = false;
      this.erasure_computed = false;
      this.erasure_value = null;
      this.getSuperClassAccessOpt_computed = false;
      this.getSuperClassAccessOpt_value = null;
      this.getImplementsList_computed = false;
      this.getImplementsList_value = null;
      this.getBodyDeclList_computed = false;
      this.getBodyDeclList_value = null;
      this.subtype_TypeDecl_values = null;
      this.sameStructure_TypeDecl_values = null;
      this.instanceOf_TypeDecl_values = null;
      this.sameSignature_ArrayList_values = null;
      this.usesTypeVariable_visited = -1;
      this.usesTypeVariable_computed = false;
      this.usesTypeVariable_initialized = false;
      this.sourceTypeDecl_computed = false;
      this.sourceTypeDecl_value = null;
      this.fullName_computed = false;
      this.fullName_value = null;
      this.typeName_computed = false;
      this.typeName_value = null;
      this.unimplementedMethods_computed = false;
      this.unimplementedMethods_value = null;
      this.localMethodsSignatureMap_computed = false;
      this.localMethodsSignatureMap_value = null;
      this.localFields_String_values = null;
      this.localTypeDecls_String_values = null;
      this.constructors_computed = false;
      this.constructors_value = null;
      this.genericDecl_computed = false;
      this.genericDecl_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ParClassDecl clone() throws CloneNotSupportedException {
      ParClassDecl node = (ParClassDecl)super.clone();
      node.involvesTypeParameters_visited = -1;
      node.involvesTypeParameters_computed = false;
      node.involvesTypeParameters_initialized = false;
      node.erasure_computed = false;
      node.erasure_value = null;
      node.getSuperClassAccessOpt_computed = false;
      node.getSuperClassAccessOpt_value = null;
      node.getImplementsList_computed = false;
      node.getImplementsList_value = null;
      node.getBodyDeclList_computed = false;
      node.getBodyDeclList_value = null;
      node.subtype_TypeDecl_values = null;
      node.sameStructure_TypeDecl_values = null;
      node.instanceOf_TypeDecl_values = null;
      node.sameSignature_ArrayList_values = null;
      node.usesTypeVariable_visited = -1;
      node.usesTypeVariable_computed = false;
      node.usesTypeVariable_initialized = false;
      node.sourceTypeDecl_computed = false;
      node.sourceTypeDecl_value = null;
      node.fullName_computed = false;
      node.fullName_value = null;
      node.typeName_computed = false;
      node.typeName_value = null;
      node.unimplementedMethods_computed = false;
      node.unimplementedMethods_value = null;
      node.localMethodsSignatureMap_computed = false;
      node.localMethodsSignatureMap_value = null;
      node.localFields_String_values = null;
      node.localTypeDecls_String_values = null;
      node.constructors_computed = false;
      node.constructors_value = null;
      node.genericDecl_computed = false;
      node.genericDecl_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ParClassDecl copy() {
      try {
         ParClassDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ParClassDecl fullCopy() {
      ParClassDecl tree = this.copy();
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            switch(i) {
            case 3:
               tree.children[i] = new Opt();
               break;
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

   public void collectErrors() {
   }

   public void toString(StringBuffer s) {
      this.getModifiers().toString(s);
      s.append("class " + this.getID());
      s.append('<');
      int i;
      if (this.getNumArgument() > 0) {
         this.getArgument(0).toString(s);

         for(i = 1; i < this.getNumArgument(); ++i) {
            s.append(", ");
            this.getArgument(i).toString(s);
         }
      }

      s.append('>');
      if (this.hasSuperClassAccess()) {
         s.append(" extends ");
         this.getSuperClassAccess().toString(s);
      }

      if (this.getNumImplements() > 0) {
         s.append(" implements ");
         this.getImplements(0).toString(s);

         for(i = 1; i < this.getNumImplements(); ++i) {
            s.append(", ");
            this.getImplements(i).toString(s);
         }
      }

      this.ppBodyDecls(s);
   }

   public TypeDecl substitute(TypeVariable typeVariable) {
      for(int i = 0; i < this.numTypeParameter(); ++i) {
         if (this.typeParameter(i) == typeVariable) {
            return this.getArgument(i).type();
         }
      }

      return super.substitute(typeVariable);
   }

   public int numTypeParameter() {
      return ((GenericTypeDecl)this.original()).getNumTypeParameter();
   }

   public TypeVariable typeParameter(int index) {
      return ((GenericTypeDecl)this.original()).getTypeParameter(index);
   }

   public Access substitute(Parameterization parTypeDecl) {
      if (parTypeDecl.isRawType()) {
         return ((GenericTypeDecl)this.genericDecl()).rawType().createBoundAccess();
      } else if (!this.usesTypeVariable()) {
         return super.substitute(parTypeDecl);
      } else {
         List list = new List();

         for(int i = 0; i < this.getNumArgument(); ++i) {
            list.add(this.getArgument(i).type().substitute(parTypeDecl));
         }

         return new ParTypeAccess(this.genericDecl().createQualifiedAccess(), list);
      }
   }

   public Access createQualifiedAccess() {
      List typeArgumentList = new List();

      for(int i = 0; i < this.getNumArgument(); ++i) {
         Access a = this.getArgument(i);
         if (a instanceof TypeAccess) {
            typeArgumentList.add(a.type().createQualifiedAccess());
         } else {
            typeArgumentList.add(a.fullCopy());
         }
      }

      if (!this.isTopLevelType()) {
         if (this.isRawType()) {
            return this.enclosingType().createQualifiedAccess().qualifiesAccess(new TypeAccess("", this.getID()));
         } else {
            return this.enclosingType().createQualifiedAccess().qualifiesAccess(new ParTypeAccess(new TypeAccess("", this.getID()), typeArgumentList));
         }
      } else if (this.isRawType()) {
         return new TypeAccess(this.packageName(), this.getID());
      } else {
         return new ParTypeAccess(new TypeAccess(this.packageName(), this.getID()), typeArgumentList);
      }
   }

   public void transformation() {
   }

   public ParClassDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[5];
      this.setChild(new List(), 1);
      this.setChild(new Opt(), 2);
      this.setChild(new List(), 3);
      this.setChild(new List(), 4);
   }

   public ParClassDecl(Modifiers p0, String p1, List<Access> p2) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
   }

   public ParClassDecl(Modifiers p0, Symbol p1, List<Access> p2) {
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

   public void setArgumentList(List<Access> list) {
      this.setChild(list, 1);
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

   public List<Access> getArgumentList() {
      List<Access> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<Access> getArgumentListNoTransform() {
      return (List)this.getChildNoTransform(1);
   }

   public void setSuperClassAccessOpt(Opt<Access> opt) {
      this.setChild(opt, 2);
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
      return (Opt)this.getChildNoTransform(2);
   }

   protected int getSuperClassAccessOptChildPosition() {
      return 2;
   }

   public void setImplementsList(List<Access> list) {
      this.setChild(list, 3);
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
      return (List)this.getChildNoTransform(3);
   }

   protected int getImplementsListChildPosition() {
      return 3;
   }

   public void setBodyDeclList(List<BodyDecl> list) {
      this.setChild(list, 4);
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
      return (List)this.getChildNoTransform(4);
   }

   protected int getBodyDeclListChildPosition() {
      return 4;
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
      for(int i = 0; i < this.getNumArgument(); ++i) {
         if (this.getArgument(i).type().involvesTypeParameters()) {
            return true;
         }
      }

      return false;
   }

   public TypeDecl hostType() {
      ASTNode$State state = this.state();
      return this.original();
   }

   public TypeDecl topLevelType() {
      ASTNode$State state = this.state();
      return this.erasure().topLevelType();
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
      return this.genericDecl();
   }

   public Opt getSuperClassAccessOpt() {
      if (this.getSuperClassAccessOpt_computed) {
         return (Opt)this.getChild(this.getSuperClassAccessOptChildPosition());
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getSuperClassAccessOpt_value = this.getSuperClassAccessOpt_compute();
         this.setSuperClassAccessOpt(this.getSuperClassAccessOpt_value);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getSuperClassAccessOpt_computed = true;
         }

         return (Opt)this.getChild(this.getSuperClassAccessOptChildPosition());
      }
   }

   private Opt getSuperClassAccessOpt_compute() {
      GenericClassDecl decl = (GenericClassDecl)this.genericDecl();
      Opt opt;
      if (decl.hasSuperClassAccess()) {
         opt = new Opt(decl.getSuperClassAccess().type().substitute((Parameterization)this));
      } else {
         opt = new Opt();
      }

      return opt;
   }

   public List getImplementsList() {
      if (this.getImplementsList_computed) {
         return (List)this.getChild(this.getImplementsListChildPosition());
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getImplementsList_value = this.getImplementsList_compute();
         this.setImplementsList(this.getImplementsList_value);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getImplementsList_computed = true;
         }

         return (List)this.getChild(this.getImplementsListChildPosition());
      }
   }

   private List getImplementsList_compute() {
      GenericClassDecl decl = (GenericClassDecl)this.genericDecl();
      List list = decl.getImplementsList().substitute(this);
      return list;
   }

   public List getBodyDeclList() {
      if (this.getBodyDeclList_computed) {
         return (List)this.getChild(this.getBodyDeclListChildPosition());
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getBodyDeclList_value = this.getBodyDeclList_compute();
         this.setBodyDeclList(this.getBodyDeclList_value);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getBodyDeclList_computed = true;
         }

         return (List)this.getChild(this.getBodyDeclListChildPosition());
      }
   }

   private List getBodyDeclList_compute() {
      return new BodyDeclList();
   }

   public boolean supertypeGenericClassDecl(GenericClassDecl type) {
      ASTNode$State state = this.state();
      return type.subtype(this.genericDecl().original());
   }

   public boolean supertypeClassDecl(ClassDecl type) {
      ASTNode$State state = this.state();
      return super.supertypeClassDecl(type);
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
      return type.supertypeParClassDecl(this);
   }

   public boolean supertypeRawClassDecl(RawClassDecl type) {
      ASTNode$State state = this.state();
      return type.genericDecl().original().subtype(this.genericDecl().original());
   }

   public boolean supertypeRawInterfaceDecl(RawInterfaceDecl type) {
      ASTNode$State state = this.state();
      return type.genericDecl().original().subtype(this.genericDecl().original());
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
      if (!(t instanceof ParClassDecl)) {
         return false;
      } else {
         ParClassDecl type = (ParClassDecl)t;
         if (type.genericDecl().original() == this.genericDecl().original() && type.getNumArgument() == this.getNumArgument()) {
            for(int i = 0; i < this.getNumArgument(); ++i) {
               if (!type.getArgument(i).type().sameStructure(this.getArgument(i).type())) {
                  return false;
               }
            }

            if (this.isNestedType() && type.isNestedType()) {
               return type.enclosingType().sameStructure(this.enclosingType());
            } else {
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public boolean supertypeParClassDecl(ParClassDecl type) {
      ASTNode$State state = this.state();
      if (type.genericDecl().original() == this.genericDecl().original() && type.getNumArgument() == this.getNumArgument()) {
         for(int i = 0; i < this.getNumArgument(); ++i) {
            if (!type.getArgument(i).type().containedIn(this.getArgument(i).type())) {
               return false;
            }
         }

         if (this.isNestedType() && type.isNestedType()) {
            return type.enclosingType().subtype(this.enclosingType());
         } else {
            return true;
         }
      } else {
         return this.supertypeClassDecl(type);
      }
   }

   public boolean supertypeParInterfaceDecl(ParInterfaceDecl type) {
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

   public boolean isReifiable() {
      ASTNode$State state = this.state();
      if (this.isRawType()) {
         return true;
      } else {
         for(int i = 0; i < this.getNumArgument(); ++i) {
            if (!this.getArgument(i).type().isWildcard()) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean isParameterizedType() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean sameArgument(ParTypeDecl decl) {
      ASTNode$State state = this.state();
      if (this == decl) {
         return true;
      } else if (this.genericDecl() != decl.genericDecl()) {
         return false;
      } else {
         for(int i = 0; i < this.getNumArgument(); ++i) {
            TypeDecl t1 = this.getArgument(i).type();
            TypeDecl t2 = decl.getArgument(i).type();
            if (t1 instanceof ParTypeDecl && t2 instanceof ParTypeDecl) {
               if (!((ParTypeDecl)t1).sameArgument((ParTypeDecl)t2)) {
                  return false;
               }
            } else if (t1 != t2) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean sameSignature(Access a) {
      ASTNode$State state = this.state();
      if (a instanceof ParTypeAccess) {
         ParTypeAccess ta = (ParTypeAccess)a;
         if (this.genericDecl() != ta.genericDecl()) {
            return false;
         } else if (this.getNumArgument() != ta.getNumTypeArgument()) {
            return false;
         } else {
            for(int i = 0; i < this.getNumArgument(); ++i) {
               if (!this.getArgument(i).type().sameSignature(ta.getTypeArgument(i))) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return a instanceof TypeAccess && ((TypeAccess)a).isRaw() ? false : super.sameSignature(a);
      }
   }

   public boolean sameSignature(ArrayList list) {
      if (this.sameSignature_ArrayList_values == null) {
         this.sameSignature_ArrayList_values = new HashMap(4);
      }

      ASTNode$State.CircularValue _value;
      if (this.sameSignature_ArrayList_values.containsKey(list)) {
         Object _o = this.sameSignature_ArrayList_values.get(list);
         if (!(_o instanceof ASTNode$State.CircularValue)) {
            return (Boolean)_o;
         }

         _value = (ASTNode$State.CircularValue)_o;
      } else {
         _value = new ASTNode$State.CircularValue();
         this.sameSignature_ArrayList_values.put(list, _value);
         _value.value = true;
      }

      ASTNode$State state = this.state();
      if (state.IN_CIRCLE) {
         if (!(new Integer(state.CIRCLE_INDEX)).equals(_value.visited)) {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            boolean new_sameSignature_ArrayList_value = this.sameSignature_compute(list);
            if (state.RESET_CYCLE) {
               this.sameSignature_ArrayList_values.remove(list);
            } else if (new_sameSignature_ArrayList_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_sameSignature_ArrayList_value;
            }

            return new_sameSignature_ArrayList_value;
         } else {
            return (Boolean)_value.value;
         }
      } else {
         state.IN_CIRCLE = true;
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();

         boolean new_sameSignature_ArrayList_value;
         do {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            state.CHANGE = false;
            new_sameSignature_ArrayList_value = this.sameSignature_compute(list);
            if (new_sameSignature_ArrayList_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_sameSignature_ArrayList_value;
            }

            ++state.CIRCLE_INDEX;
         } while(state.CHANGE);

         if (isFinal && num == this.state().boundariesCrossed) {
            this.sameSignature_ArrayList_values.put(list, new_sameSignature_ArrayList_value);
         } else {
            this.sameSignature_ArrayList_values.remove(list);
            state.RESET_CYCLE = true;
            this.sameSignature_compute(list);
            state.RESET_CYCLE = false;
         }

         state.IN_CIRCLE = false;
         return new_sameSignature_ArrayList_value;
      }
   }

   private boolean sameSignature_compute(ArrayList list) {
      if (this.getNumArgument() != list.size()) {
         return false;
      } else {
         for(int i = 0; i < list.size(); ++i) {
            if (this.getArgument(i).type() != list.get(i)) {
               return false;
            }
         }

         return true;
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
      if (super.usesTypeVariable()) {
         return true;
      } else {
         for(int i = 0; i < this.getNumArgument(); ++i) {
            if (this.getArgument(i).type().usesTypeVariable()) {
               return true;
            }
         }

         return false;
      }
   }

   public TypeDecl original() {
      ASTNode$State state = this.state();
      return this.genericDecl().original();
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
      return this.genericDecl().original().sourceTypeDecl();
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
         return this.enclosingType().fullName() + "." + this.nameWithArgs();
      } else {
         String packageName = this.packageName();
         return packageName.equals("") ? this.nameWithArgs() : packageName + "." + this.nameWithArgs();
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
         return this.enclosingType().typeName() + "." + this.nameWithArgs();
      } else {
         String packageName = this.packageName();
         return !packageName.equals("") && !packageName.equals("@primitive") ? packageName + "." + this.nameWithArgs() : this.nameWithArgs();
      }
   }

   public String nameWithArgs() {
      ASTNode$State state = this.state();
      StringBuffer s = new StringBuffer();
      s.append(this.name());
      s.append("<");

      for(int i = 0; i < this.getNumArgument(); ++i) {
         if (i != 0) {
            s.append(", ");
         }

         s.append(this.getArgument(i).type().fullName());
      }

      s.append(">");
      return s.toString();
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
      HashSet set = new HashSet();
      HashSet result = new HashSet();
      Iterator iter = this.genericDecl().unimplementedMethods().iterator();

      MethodDecl m;
      while(iter.hasNext()) {
         m = (MethodDecl)iter.next();
         set.add(m.sourceMethodDecl());
      }

      iter = super.unimplementedMethods().iterator();

      while(iter.hasNext()) {
         m = (MethodDecl)iter.next();
         if (set.contains(m.sourceMethodDecl())) {
            result.add(m);
         }
      }

      return result;
   }

   public HashMap localMethodsSignatureMap() {
      if (this.localMethodsSignatureMap_computed) {
         return this.localMethodsSignatureMap_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.localMethodsSignatureMap_value = this.localMethodsSignatureMap_compute();
         this.localMethodsSignatureMap_computed = true;
         return this.localMethodsSignatureMap_value;
      }
   }

   private HashMap localMethodsSignatureMap_compute() {
      HashMap map = new HashMap();

      MethodDecl decl;
      for(Iterator iter = this.original().localMethodsIterator(); iter.hasNext(); map.put(decl.signature(), decl)) {
         decl = (MethodDecl)iter.next();
         if (!decl.isStatic() && (decl.usesTypeVariable() || this.isRawType())) {
            BodyDecl copyDecl = ((BodyDeclList)this.getBodyDeclList()).localMethodSignatureCopy(decl, this);
            decl = (MethodDecl)copyDecl;
         }
      }

      return map;
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
         this.localFields_String_values.put(name, localFields_String_value);
         return localFields_String_value;
      }
   }

   private SimpleSet localFields_compute(String name) {
      SimpleSet set = SimpleSet.emptySet;

      FieldDeclaration f;
      for(Iterator iter = this.original().localFields(name).iterator(); iter.hasNext(); set = set.add(f)) {
         f = (FieldDeclaration)iter.next();
         if (!f.isStatic() && (f.usesTypeVariable() || this.isRawType())) {
            BodyDecl fCopy = ((BodyDeclList)this.getBodyDeclList()).localFieldCopy(f, this);
            f = (FieldDeclaration)fCopy;
         }
      }

      return set;
   }

   public SimpleSet localTypeDecls(String name) {
      if (this.localTypeDecls_String_values == null) {
         this.localTypeDecls_String_values = new HashMap(4);
      }

      ASTNode$State.CircularValue _value;
      if (this.localTypeDecls_String_values.containsKey(name)) {
         Object _o = this.localTypeDecls_String_values.get(name);
         if (!(_o instanceof ASTNode$State.CircularValue)) {
            return (SimpleSet)_o;
         }

         _value = (ASTNode$State.CircularValue)_o;
      } else {
         _value = new ASTNode$State.CircularValue();
         this.localTypeDecls_String_values.put(name, _value);
         _value.value = SimpleSet.emptySet;
      }

      ASTNode$State state = this.state();
      if (!state.IN_CIRCLE) {
         state.IN_CIRCLE = true;
         int num = state.boundariesCrossed;
         boolean var6 = this.is$Final();

         SimpleSet new_localTypeDecls_String_value;
         do {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            state.CHANGE = false;
            new_localTypeDecls_String_value = this.localTypeDecls_compute(name);
            if (new_localTypeDecls_String_value == null && (SimpleSet)_value.value != null || new_localTypeDecls_String_value != null && !new_localTypeDecls_String_value.equals((SimpleSet)_value.value)) {
               state.CHANGE = true;
               _value.value = new_localTypeDecls_String_value;
            }

            ++state.CIRCLE_INDEX;
         } while(state.CHANGE);

         this.localTypeDecls_String_values.put(name, new_localTypeDecls_String_value);
         state.IN_CIRCLE = false;
         return new_localTypeDecls_String_value;
      } else if ((new Integer(state.CIRCLE_INDEX)).equals(_value.visited)) {
         return (SimpleSet)_value.value;
      } else {
         _value.visited = new Integer(state.CIRCLE_INDEX);
         SimpleSet new_localTypeDecls_String_value = this.localTypeDecls_compute(name);
         if (state.RESET_CYCLE) {
            this.localTypeDecls_String_values.remove(name);
         } else if (new_localTypeDecls_String_value == null && (SimpleSet)_value.value != null || new_localTypeDecls_String_value != null && !new_localTypeDecls_String_value.equals((SimpleSet)_value.value)) {
            state.CHANGE = true;
            _value.value = new_localTypeDecls_String_value;
         }

         return new_localTypeDecls_String_value;
      }
   }

   private SimpleSet localTypeDecls_compute(String name) {
      SimpleSet set = SimpleSet.emptySet;
      Iterator iter = this.original().localTypeDecls(name).iterator();

      while(iter.hasNext()) {
         TypeDecl t = (TypeDecl)iter.next();
         if (t.isStatic()) {
            set = set.add(t);
         } else if (t instanceof ClassDecl) {
            MemberClassDecl copy = ((BodyDeclList)this.getBodyDeclList()).localClassDeclCopy((ClassDecl)t, this);
            set = set.add(copy.getClassDecl());
         } else if (t instanceof InterfaceDecl) {
            MemberInterfaceDecl copy = ((BodyDeclList)this.getBodyDeclList()).localInterfaceDeclCopy((InterfaceDecl)t, this);
            set = set.add(copy.getInterfaceDecl());
         }
      }

      return set;
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
      Collection set = new ArrayList();
      Iterator iter = this.original().constructors().iterator();

      while(iter.hasNext()) {
         ConstructorDecl c = (ConstructorDecl)iter.next();
         BodyDecl b = ((BodyDeclList)this.getBodyDeclList()).constructorCopy(c, this);
         set.add(b);
      }

      return set;
   }

   public TypeDecl genericDecl() {
      if (this.genericDecl_computed) {
         return this.genericDecl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.genericDecl_value = this.getParent().Define_TypeDecl_genericDecl(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.genericDecl_computed = true;
         }

         return this.genericDecl_value;
      }
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      if (caller == this.getArgumentListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.TYPE_NAME;
      } else {
         return super.Define_NameType_nameType(caller, child);
      }
   }

   public TypeDecl Define_TypeDecl_genericDecl(ASTNode caller, ASTNode child) {
      if (caller == this.getBodyDeclListNoTransform()) {
         int index = caller.getIndexOfChild(child);
         if (this.getBodyDecl(index) instanceof MemberTypeDecl) {
            MemberTypeDecl m = (MemberTypeDecl)this.getBodyDecl(index);
            return this.extractSingleType(this.genericDecl().memberTypes(m.typeDecl().name()));
         } else {
            return this.genericDecl();
         }
      } else {
         return this.getParent().Define_TypeDecl_genericDecl(this, caller);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
