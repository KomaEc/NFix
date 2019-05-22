package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GenericClassDeclSubstituted extends GenericClassDecl implements Cloneable, MemberSubstitutor {
   protected TypeDecl tokenTypeDecl_Original;
   protected boolean getBodyDeclList_computed = false;
   protected List getBodyDeclList_value;
   protected boolean sourceTypeDecl_computed = false;
   protected TypeDecl sourceTypeDecl_value;
   protected Map instanceOf_TypeDecl_values;
   protected Map subtype_TypeDecl_values;
   protected boolean localMethodsSignatureMap_computed = false;
   protected HashMap localMethodsSignatureMap_value;
   protected Map localFields_String_values;
   protected Map localTypeDecls_String_values;
   protected boolean constructors_computed = false;
   protected Collection constructors_value;

   public void flushCache() {
      super.flushCache();
      this.getBodyDeclList_computed = false;
      this.getBodyDeclList_value = null;
      this.sourceTypeDecl_computed = false;
      this.sourceTypeDecl_value = null;
      this.instanceOf_TypeDecl_values = null;
      this.subtype_TypeDecl_values = null;
      this.localMethodsSignatureMap_computed = false;
      this.localMethodsSignatureMap_value = null;
      this.localFields_String_values = null;
      this.localTypeDecls_String_values = null;
      this.constructors_computed = false;
      this.constructors_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public GenericClassDeclSubstituted clone() throws CloneNotSupportedException {
      GenericClassDeclSubstituted node = (GenericClassDeclSubstituted)super.clone();
      node.getBodyDeclList_computed = false;
      node.getBodyDeclList_value = null;
      node.sourceTypeDecl_computed = false;
      node.sourceTypeDecl_value = null;
      node.instanceOf_TypeDecl_values = null;
      node.subtype_TypeDecl_values = null;
      node.localMethodsSignatureMap_computed = false;
      node.localMethodsSignatureMap_value = null;
      node.localFields_String_values = null;
      node.localTypeDecls_String_values = null;
      node.constructors_computed = false;
      node.constructors_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public GenericClassDeclSubstituted copy() {
      try {
         GenericClassDeclSubstituted node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public GenericClassDeclSubstituted fullCopy() {
      GenericClassDeclSubstituted tree = this.copy();
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            switch(i) {
            case 6:
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

   public GenericClassDeclSubstituted() {
   }

   public void init$Children() {
      this.children = new ASTNode[5];
      this.setChild(new Opt(), 1);
      this.setChild(new List(), 2);
      this.setChild(new List(), 3);
      this.setChild(new List(), 4);
   }

   public GenericClassDeclSubstituted(Modifiers p0, String p1, Opt<Access> p2, List<Access> p3, List<TypeVariable> p4, TypeDecl p5) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setOriginal(p5);
   }

   public GenericClassDeclSubstituted(Modifiers p0, Symbol p1, Opt<Access> p2, List<Access> p3, List<TypeVariable> p4, TypeDecl p5) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setOriginal(p5);
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

   public void setOriginal(TypeDecl value) {
      this.tokenTypeDecl_Original = value;
   }

   public TypeDecl getOriginal() {
      return this.tokenTypeDecl_Original;
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

   public TypeDecl hostType() {
      ASTNode$State state = this.state();
      return this.getOriginal();
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

   public TypeDecl original() {
      ASTNode$State state = this.state();
      return this.getOriginal().original();
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
      return this.original().sourceTypeDecl();
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
      return type.supertypeGenericClassDeclSubstituted(this);
   }

   public boolean supertypeGenericClassDeclSubstituted(GenericClassDeclSubstituted type) {
      ASTNode$State state = this.state();
      return this.original() == type.original() && type.enclosingType().subtype(this.enclosingType()) || super.supertypeGenericClassDeclSubstituted(type);
   }

   public boolean supertypeGenericClassDecl(GenericClassDecl type) {
      ASTNode$State state = this.state();
      return super.supertypeGenericClassDecl(type) || this.original().supertypeGenericClassDecl(type);
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

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
