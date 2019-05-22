package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Map;

public class ParMethodDecl extends MethodDecl implements Cloneable, Parameterization {
   protected GenericMethodDecl tokenGenericMethodDecl_GenericMethodDecl;
   protected boolean genericMethodDecl_computed = false;
   protected GenericMethodDecl genericMethodDecl_value;
   protected boolean sourceMethodDecl_computed = false;
   protected MethodDecl sourceMethodDecl_value;
   protected Map moreSpecificThan_MethodDecl_values;

   public void flushCache() {
      super.flushCache();
      this.genericMethodDecl_computed = false;
      this.genericMethodDecl_value = null;
      this.sourceMethodDecl_computed = false;
      this.sourceMethodDecl_value = null;
      this.moreSpecificThan_MethodDecl_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ParMethodDecl clone() throws CloneNotSupportedException {
      ParMethodDecl node = (ParMethodDecl)super.clone();
      node.genericMethodDecl_computed = false;
      node.genericMethodDecl_value = null;
      node.sourceMethodDecl_computed = false;
      node.sourceMethodDecl_value = null;
      node.moreSpecificThan_MethodDecl_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ParMethodDecl copy() {
      try {
         ParMethodDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ParMethodDecl fullCopy() {
      ParMethodDecl tree = this.copy();
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

   public void collectErrors() {
   }

   public TypeDecl substitute(TypeVariable typeVariable) {
      for(int i = 0; i < this.numTypeParameter(); ++i) {
         if (this.typeParameter(i) == typeVariable) {
            return this.getTypeArgument(i).type();
         }
      }

      return this.genericMethodDecl().hostType().substitute(typeVariable);
   }

   public boolean isRawType() {
      return false;
   }

   public int numTypeParameter() {
      return this.genericMethodDecl().original().getNumTypeParameter();
   }

   public TypeVariable typeParameter(int index) {
      return this.genericMethodDecl().original().getTypeParameter(index);
   }

   public void transformation() {
   }

   public ParMethodDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[6];
      this.setChild(new List(), 2);
      this.setChild(new List(), 3);
      this.setChild(new Opt(), 4);
      this.setChild(new List(), 5);
   }

   public ParMethodDecl(Modifiers p0, Access p1, String p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, List<Access> p6, GenericMethodDecl p7) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setChild(p5, 4);
      this.setChild(p6, 5);
      this.setGenericMethodDecl(p7);
   }

   public ParMethodDecl(Modifiers p0, Access p1, Symbol p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, List<Access> p6, GenericMethodDecl p7) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setChild(p5, 4);
      this.setChild(p6, 5);
      this.setGenericMethodDecl(p7);
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

   public void setTypeArgumentList(List<Access> list) {
      this.setChild(list, 5);
   }

   public int getNumTypeArgument() {
      return this.getTypeArgumentList().getNumChild();
   }

   public int getNumTypeArgumentNoTransform() {
      return this.getTypeArgumentListNoTransform().getNumChildNoTransform();
   }

   public Access getTypeArgument(int i) {
      return (Access)this.getTypeArgumentList().getChild(i);
   }

   public void addTypeArgument(Access node) {
      List<Access> list = this.parent != null && state != null ? this.getTypeArgumentList() : this.getTypeArgumentListNoTransform();
      list.addChild(node);
   }

   public void addTypeArgumentNoTransform(Access node) {
      List<Access> list = this.getTypeArgumentListNoTransform();
      list.addChild(node);
   }

   public void setTypeArgument(Access node, int i) {
      List<Access> list = this.getTypeArgumentList();
      list.setChild(node, i);
   }

   public List<Access> getTypeArguments() {
      return this.getTypeArgumentList();
   }

   public List<Access> getTypeArgumentsNoTransform() {
      return this.getTypeArgumentListNoTransform();
   }

   public List<Access> getTypeArgumentList() {
      List<Access> list = (List)this.getChild(5);
      list.getNumChild();
      return list;
   }

   public List<Access> getTypeArgumentListNoTransform() {
      return (List)this.getChildNoTransform(5);
   }

   public void setGenericMethodDecl(GenericMethodDecl value) {
      this.tokenGenericMethodDecl_GenericMethodDecl = value;
   }

   public GenericMethodDecl getGenericMethodDecl() {
      return this.tokenGenericMethodDecl_GenericMethodDecl;
   }

   public GenericMethodDecl genericMethodDecl() {
      if (this.genericMethodDecl_computed) {
         return this.genericMethodDecl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.genericMethodDecl_value = this.genericMethodDecl_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.genericMethodDecl_computed = true;
         }

         return this.genericMethodDecl_value;
      }
   }

   private GenericMethodDecl genericMethodDecl_compute() {
      return this.getGenericMethodDecl();
   }

   public MethodDecl sourceMethodDecl() {
      if (this.sourceMethodDecl_computed) {
         return this.sourceMethodDecl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.sourceMethodDecl_value = this.sourceMethodDecl_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.sourceMethodDecl_computed = true;
         }

         return this.sourceMethodDecl_value;
      }
   }

   private MethodDecl sourceMethodDecl_compute() {
      return this.genericMethodDecl().original().sourceMethodDecl();
   }

   public boolean moreSpecificThan(MethodDecl m) {
      if (this.moreSpecificThan_MethodDecl_values == null) {
         this.moreSpecificThan_MethodDecl_values = new HashMap(4);
      }

      if (this.moreSpecificThan_MethodDecl_values.containsKey(m)) {
         return (Boolean)this.moreSpecificThan_MethodDecl_values.get(m);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean moreSpecificThan_MethodDecl_value = this.moreSpecificThan_compute(m);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.moreSpecificThan_MethodDecl_values.put(m, moreSpecificThan_MethodDecl_value);
         }

         return moreSpecificThan_MethodDecl_value;
      }
   }

   private boolean moreSpecificThan_compute(MethodDecl m) {
      return this.genericMethodDecl().moreSpecificThan((MethodDecl)(m instanceof ParMethodDecl ? ((ParMethodDecl)m).genericMethodDecl() : m));
   }

   public MethodDecl erasedMethod() {
      ASTNode$State state = this.state();
      return this.genericMethodDecl().erasedMethod();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
