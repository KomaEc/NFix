package soot.JastAddJ;

import beaver.Symbol;
import soot.Type;

public class DoubleType extends FloatingPointType implements Cloneable {
   protected boolean boxed_computed = false;
   protected TypeDecl boxed_value;
   protected boolean jvmName_computed = false;
   protected String jvmName_value;
   protected boolean getSootType_computed = false;
   protected Type getSootType_value;

   public void flushCache() {
      super.flushCache();
      this.boxed_computed = false;
      this.boxed_value = null;
      this.jvmName_computed = false;
      this.jvmName_value = null;
      this.getSootType_computed = false;
      this.getSootType_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public DoubleType clone() throws CloneNotSupportedException {
      DoubleType node = (DoubleType)super.clone();
      node.boxed_computed = false;
      node.boxed_value = null;
      node.jvmName_computed = false;
      node.jvmName_value = null;
      node.getSootType_computed = false;
      node.getSootType_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public DoubleType copy() {
      try {
         DoubleType node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public DoubleType fullCopy() {
      DoubleType tree = this.copy();
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
      s.append("double");
   }

   public DoubleType() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new Opt(), 1);
      this.setChild(new List(), 2);
   }

   public DoubleType(Modifiers p0, String p1, Opt<Access> p2, List<BodyDecl> p3) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   public DoubleType(Modifiers p0, Symbol p1, Opt<Access> p2, List<BodyDecl> p3) {
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

   public Constant cast(Constant c) {
      ASTNode$State state = this.state();
      return Constant.create(c.doubleValue());
   }

   public Constant plus(Constant c) {
      ASTNode$State state = this.state();
      return c;
   }

   public Constant minus(Constant c) {
      ASTNode$State state = this.state();
      return Constant.create(-c.doubleValue());
   }

   public Constant mul(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.doubleValue() * c2.doubleValue());
   }

   public Constant div(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.doubleValue() / c2.doubleValue());
   }

   public Constant mod(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.doubleValue() % c2.doubleValue());
   }

   public Constant add(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.doubleValue() + c2.doubleValue());
   }

   public Constant sub(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.doubleValue() - c2.doubleValue());
   }

   public Constant questionColon(Constant cond, Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(cond.booleanValue() ? c1.doubleValue() : c2.doubleValue());
   }

   public boolean eqIsTrue(Expr left, Expr right) {
      ASTNode$State state = this.state();
      return left.constant().doubleValue() == right.constant().doubleValue();
   }

   public boolean ltIsTrue(Expr left, Expr right) {
      ASTNode$State state = this.state();
      return left.constant().doubleValue() < right.constant().doubleValue();
   }

   public boolean leIsTrue(Expr left, Expr right) {
      ASTNode$State state = this.state();
      return left.constant().doubleValue() <= right.constant().doubleValue();
   }

   public boolean isDouble() {
      ASTNode$State state = this.state();
      return true;
   }

   public TypeDecl boxed() {
      if (this.boxed_computed) {
         return this.boxed_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.boxed_value = this.boxed_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.boxed_computed = true;
         }

         return this.boxed_value;
      }
   }

   private TypeDecl boxed_compute() {
      return this.lookupType("java.lang", "Double");
   }

   public String jvmName() {
      if (this.jvmName_computed) {
         return this.jvmName_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.jvmName_value = this.jvmName_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.jvmName_computed = true;
         }

         return this.jvmName_value;
      }
   }

   private String jvmName_compute() {
      return "D";
   }

   public String primitiveClassName() {
      ASTNode$State state = this.state();
      return "Double";
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
      return soot.DoubleType.v();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
