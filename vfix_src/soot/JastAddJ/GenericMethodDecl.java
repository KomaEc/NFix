package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GenericMethodDecl extends MethodDecl implements Cloneable {
   public GenericMethodDecl original;
   protected boolean rawMethodDecl_computed = false;
   protected MethodDecl rawMethodDecl_value;
   protected Map lookupParMethodDecl_java_util_List_values;
   protected List lookupParMethodDecl_java_util_List_list;

   public void flushCache() {
      super.flushCache();
      this.rawMethodDecl_computed = false;
      this.rawMethodDecl_value = null;
      this.lookupParMethodDecl_java_util_List_values = null;
      this.lookupParMethodDecl_java_util_List_list = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public GenericMethodDecl clone() throws CloneNotSupportedException {
      GenericMethodDecl node = (GenericMethodDecl)super.clone();
      node.rawMethodDecl_computed = false;
      node.rawMethodDecl_value = null;
      node.lookupParMethodDecl_java_util_List_values = null;
      node.lookupParMethodDecl_java_util_List_list = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public GenericMethodDecl copy() {
      try {
         GenericMethodDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public GenericMethodDecl fullCopy() {
      GenericMethodDecl tree = this.copy();
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

   public ParMethodDecl newParMethodDecl(java.util.List typeArguments) {
      ParMethodDecl methodDecl = typeArguments.isEmpty() ? new RawMethodDecl() : new ParMethodDecl();
      ((ParMethodDecl)methodDecl).setGenericMethodDecl(this);
      List list = new List();
      if (typeArguments.isEmpty()) {
         GenericMethodDecl original = this.original();

         for(int i = 0; i < original.getNumTypeParameter(); ++i) {
            list.add(original.getTypeParameter(i).erasure().createBoundAccess());
         }
      } else {
         Iterator iter = typeArguments.iterator();

         while(iter.hasNext()) {
            list.add(((TypeDecl)iter.next()).createBoundAccess());
         }
      }

      ((ParMethodDecl)methodDecl).setTypeArgumentList(list);
      ((ParMethodDecl)methodDecl).setModifiers(this.getModifiers().fullCopy());
      ((ParMethodDecl)methodDecl).setTypeAccess(this.getTypeAccess().type().substituteReturnType((Parameterization)methodDecl));
      ((ParMethodDecl)methodDecl).setID(this.getID());
      ((ParMethodDecl)methodDecl).setParameterList(this.getParameterList().substitute((Parameterization)methodDecl));
      ((ParMethodDecl)methodDecl).setExceptionList(this.getExceptionList().substitute((Parameterization)methodDecl));
      return (ParMethodDecl)methodDecl;
   }

   private void ppTypeParameters(StringBuffer s) {
      s.append(" <");

      for(int i = 0; i < this.getNumTypeParameter(); ++i) {
         if (i != 0) {
            s.append(", ");
         }

         this.original().getTypeParameter(i).toString(s);
      }

      s.append("> ");
   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      this.getModifiers().toString(s);
      this.ppTypeParameters(s);
      this.getTypeAccess().toString(s);
      s.append(" " + this.getID());
      s.append("(");
      int i;
      if (this.getNumParameter() > 0) {
         this.getParameter(0).toString(s);

         for(i = 1; i < this.getNumParameter(); ++i) {
            s.append(", ");
            this.getParameter(i).toString(s);
         }
      }

      s.append(")");
      if (this.getNumException() > 0) {
         s.append(" throws ");
         this.getException(0).toString(s);

         for(i = 1; i < this.getNumException(); ++i) {
            s.append(", ");
            this.getException(i).toString(s);
         }
      }

      if (this.hasBlock()) {
         s.append(" ");
         this.getBlock().toString(s);
      } else {
         s.append(";\n");
      }

   }

   public BodyDecl substitutedBodyDecl(Parameterization parTypeDecl) {
      GenericMethodDecl m = new GenericMethodDecl(this.getModifiers().fullCopy(), this.getTypeAccess().type().substituteReturnType(parTypeDecl), this.getID(), this.getParameterList().substitute(parTypeDecl), this.getExceptionList().substitute(parTypeDecl), new Opt(), this.getTypeParameterList().fullCopy());
      m.original = this;
      return m;
   }

   public GenericMethodDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[6];
      this.setChild(new List(), 2);
      this.setChild(new List(), 3);
      this.setChild(new Opt(), 4);
      this.setChild(new List(), 5);
   }

   public GenericMethodDecl(Modifiers p0, Access p1, String p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, List<TypeVariable> p6) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setChild(p5, 4);
      this.setChild(p6, 5);
   }

   public GenericMethodDecl(Modifiers p0, Access p1, Symbol p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, List<TypeVariable> p6) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setChild(p5, 4);
      this.setChild(p6, 5);
   }

   protected int numChildren() {
      return 6;
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

   public void setTypeAccess(Access node) {
      this.setChild(node, 1);
   }

   public Access getTypeAccess() {
      return (Access)this.getChild(1);
   }

   public Access getTypeAccessNoTransform() {
      return (Access)this.getChildNoTransform(1);
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

   public void setParameterList(List<ParameterDeclaration> list) {
      this.setChild(list, 2);
   }

   public int getNumParameter() {
      return this.getParameterList().getNumChild();
   }

   public int getNumParameterNoTransform() {
      return this.getParameterListNoTransform().getNumChildNoTransform();
   }

   public ParameterDeclaration getParameter(int i) {
      return (ParameterDeclaration)this.getParameterList().getChild(i);
   }

   public void addParameter(ParameterDeclaration node) {
      List<ParameterDeclaration> list = this.parent != null && state != null ? this.getParameterList() : this.getParameterListNoTransform();
      list.addChild(node);
   }

   public void addParameterNoTransform(ParameterDeclaration node) {
      List<ParameterDeclaration> list = this.getParameterListNoTransform();
      list.addChild(node);
   }

   public void setParameter(ParameterDeclaration node, int i) {
      List<ParameterDeclaration> list = this.getParameterList();
      list.setChild(node, i);
   }

   public List<ParameterDeclaration> getParameters() {
      return this.getParameterList();
   }

   public List<ParameterDeclaration> getParametersNoTransform() {
      return this.getParameterListNoTransform();
   }

   public List<ParameterDeclaration> getParameterList() {
      List<ParameterDeclaration> list = (List)this.getChild(2);
      list.getNumChild();
      return list;
   }

   public List<ParameterDeclaration> getParameterListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   public void setExceptionList(List<Access> list) {
      this.setChild(list, 3);
   }

   public int getNumException() {
      return this.getExceptionList().getNumChild();
   }

   public int getNumExceptionNoTransform() {
      return this.getExceptionListNoTransform().getNumChildNoTransform();
   }

   public Access getException(int i) {
      return (Access)this.getExceptionList().getChild(i);
   }

   public void addException(Access node) {
      List<Access> list = this.parent != null && state != null ? this.getExceptionList() : this.getExceptionListNoTransform();
      list.addChild(node);
   }

   public void addExceptionNoTransform(Access node) {
      List<Access> list = this.getExceptionListNoTransform();
      list.addChild(node);
   }

   public void setException(Access node, int i) {
      List<Access> list = this.getExceptionList();
      list.setChild(node, i);
   }

   public List<Access> getExceptions() {
      return this.getExceptionList();
   }

   public List<Access> getExceptionsNoTransform() {
      return this.getExceptionListNoTransform();
   }

   public List<Access> getExceptionList() {
      List<Access> list = (List)this.getChild(3);
      list.getNumChild();
      return list;
   }

   public List<Access> getExceptionListNoTransform() {
      return (List)this.getChildNoTransform(3);
   }

   public void setBlockOpt(Opt<Block> opt) {
      this.setChild(opt, 4);
   }

   public boolean hasBlock() {
      return this.getBlockOpt().getNumChild() != 0;
   }

   public Block getBlock() {
      return (Block)this.getBlockOpt().getChild(0);
   }

   public void setBlock(Block node) {
      this.getBlockOpt().setChild(node, 0);
   }

   public Opt<Block> getBlockOpt() {
      return (Opt)this.getChild(4);
   }

   public Opt<Block> getBlockOptNoTransform() {
      return (Opt)this.getChildNoTransform(4);
   }

   public void setTypeParameterList(List<TypeVariable> list) {
      this.setChild(list, 5);
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
      List<TypeVariable> list = (List)this.getChild(5);
      list.getNumChild();
      return list;
   }

   public List<TypeVariable> getTypeParameterListNoTransform() {
      return (List)this.getChildNoTransform(5);
   }

   public MethodDecl rawMethodDecl() {
      if (this.rawMethodDecl_computed) {
         return this.rawMethodDecl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.rawMethodDecl_value = this.rawMethodDecl_compute();
         this.rawMethodDecl_computed = true;
         return this.rawMethodDecl_value;
      }
   }

   private MethodDecl rawMethodDecl_compute() {
      return this.lookupParMethodDecl(new ArrayList());
   }

   public MethodDecl lookupParMethodDecl(java.util.List typeArguments) {
      if (this.lookupParMethodDecl_java_util_List_values == null) {
         this.lookupParMethodDecl_java_util_List_values = new HashMap(4);
      }

      if (this.lookupParMethodDecl_java_util_List_values.containsKey(typeArguments)) {
         return (MethodDecl)this.lookupParMethodDecl_java_util_List_values.get(typeArguments);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         MethodDecl lookupParMethodDecl_java_util_List_value = this.lookupParMethodDecl_compute(typeArguments);
         if (this.lookupParMethodDecl_java_util_List_list == null) {
            this.lookupParMethodDecl_java_util_List_list = new List();
            this.lookupParMethodDecl_java_util_List_list.is$Final = true;
            this.lookupParMethodDecl_java_util_List_list.setParent(this);
         }

         this.lookupParMethodDecl_java_util_List_list.add(lookupParMethodDecl_java_util_List_value);
         if (lookupParMethodDecl_java_util_List_value != null) {
            lookupParMethodDecl_java_util_List_value.is$Final = true;
         }

         this.lookupParMethodDecl_java_util_List_values.put(typeArguments, lookupParMethodDecl_java_util_List_value);
         return lookupParMethodDecl_java_util_List_value;
      }
   }

   private MethodDecl lookupParMethodDecl_compute(java.util.List typeArguments) {
      return this.newParMethodDecl(typeArguments);
   }

   public SimpleSet localLookupType(String name) {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumTypeParameter(); ++i) {
         if (this.original().getTypeParameter(i).name().equals(name)) {
            return SimpleSet.emptySet.add(this.original().getTypeParameter(i));
         }
      }

      return SimpleSet.emptySet;
   }

   public GenericMethodDecl original() {
      ASTNode$State state = this.state();
      return this.original != null ? this.original : this;
   }

   public SimpleSet lookupType(String name) {
      ASTNode$State state = this.state();
      SimpleSet lookupType_String_value = this.getParent().Define_SimpleSet_lookupType(this, (ASTNode)null, name);
      return lookupType_String_value;
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      if (caller == this.getTypeParameterListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.TYPE_NAME;
      } else {
         return super.Define_NameType_nameType(caller, child);
      }
   }

   public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
      this.getIndexOfChild(caller);
      return this.localLookupType(name).isEmpty() ? this.lookupType(name) : this.localLookupType(name);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
