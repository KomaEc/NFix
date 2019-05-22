package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import soot.tagkit.AnnotationDefaultTag;
import soot.tagkit.AnnotationElem;

public class AnnotationMethodDecl extends MethodDecl implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public AnnotationMethodDecl clone() throws CloneNotSupportedException {
      AnnotationMethodDecl node = (AnnotationMethodDecl)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public AnnotationMethodDecl copy() {
      try {
         AnnotationMethodDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public AnnotationMethodDecl fullCopy() {
      AnnotationMethodDecl tree = this.copy();
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

   public void typeCheck() {
      super.typeCheck();
      if (this.hasDefaultValue() && !this.type().commensurateWith(this.getDefaultValue())) {
         this.error(this.type().typeName() + " is not commensurate with " + this.getDefaultValue().type().typeName());
      }

   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      this.getModifiers().toString(s);
      this.getTypeAccess().toString(s);
      s.append(" " + this.name() + "()");
      if (this.hasDefaultValue()) {
         s.append(" default ");
         this.getDefaultValue().toString(s);
      }

      s.append(";\n");
   }

   public void addAttributes() {
      super.addAttributes();
      if (this.hasDefaultValue()) {
         ArrayList elements = new ArrayList(1);
         this.getDefaultValue().appendAsAttributeTo(elements, "default");
         AnnotationDefaultTag tag = new AnnotationDefaultTag((AnnotationElem)elements.get(0));
         this.sootMethod.addTag(tag);
      }

   }

   public AnnotationMethodDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[6];
      this.setChild(new List(), 2);
      this.setChild(new List(), 3);
      this.setChild(new Opt(), 4);
      this.setChild(new Opt(), 5);
   }

   public AnnotationMethodDecl(Modifiers p0, Access p1, String p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, Opt<ElementValue> p6) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setChild(p5, 4);
      this.setChild(p6, 5);
   }

   public AnnotationMethodDecl(Modifiers p0, Access p1, Symbol p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, Opt<ElementValue> p6) {
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

   public void setDefaultValueOpt(Opt<ElementValue> opt) {
      this.setChild(opt, 5);
   }

   public boolean hasDefaultValue() {
      return this.getDefaultValueOpt().getNumChild() != 0;
   }

   public ElementValue getDefaultValue() {
      return (ElementValue)this.getDefaultValueOpt().getChild(0);
   }

   public void setDefaultValue(ElementValue node) {
      this.getDefaultValueOpt().setChild(node, 0);
   }

   public Opt<ElementValue> getDefaultValueOpt() {
      return (Opt)this.getChild(5);
   }

   public Opt<ElementValue> getDefaultValueOptNoTransform() {
      return (Opt)this.getChildNoTransform(5);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
