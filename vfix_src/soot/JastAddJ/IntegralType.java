package soot.JastAddJ;

import beaver.Symbol;

public abstract class IntegralType extends NumericType implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public IntegralType clone() throws CloneNotSupportedException {
      IntegralType node = (IntegralType)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public IntegralType() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new Opt(), 1);
      this.setChild(new List(), 2);
   }

   public IntegralType(Modifiers p0, String p1, Opt<Access> p2, List<BodyDecl> p3) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   public IntegralType(Modifiers p0, Symbol p1, Opt<Access> p2, List<BodyDecl> p3) {
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
      return Constant.create(c.intValue());
   }

   public Constant plus(Constant c) {
      ASTNode$State state = this.state();
      return c;
   }

   public Constant minus(Constant c) {
      ASTNode$State state = this.state();
      return Constant.create(-c.intValue());
   }

   public Constant bitNot(Constant c) {
      ASTNode$State state = this.state();
      return Constant.create(~c.intValue());
   }

   public Constant mul(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.intValue() * c2.intValue());
   }

   public Constant div(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.intValue() / c2.intValue());
   }

   public Constant mod(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.intValue() % c2.intValue());
   }

   public Constant add(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.intValue() + c2.intValue());
   }

   public Constant sub(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.intValue() - c2.intValue());
   }

   public Constant lshift(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.intValue() << c2.intValue());
   }

   public Constant rshift(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.intValue() >> c2.intValue());
   }

   public Constant urshift(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.intValue() >>> c2.intValue());
   }

   public Constant andBitwise(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.intValue() & c2.intValue());
   }

   public Constant xorBitwise(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.intValue() ^ c2.intValue());
   }

   public Constant orBitwise(Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(c1.intValue() | c2.intValue());
   }

   public Constant questionColon(Constant cond, Constant c1, Constant c2) {
      ASTNode$State state = this.state();
      return Constant.create(cond.booleanValue() ? c1.intValue() : c2.intValue());
   }

   public boolean eqIsTrue(Expr left, Expr right) {
      ASTNode$State state = this.state();
      return left.constant().intValue() == right.constant().intValue();
   }

   public boolean ltIsTrue(Expr left, Expr right) {
      ASTNode$State state = this.state();
      return left.constant().intValue() < right.constant().intValue();
   }

   public boolean leIsTrue(Expr left, Expr right) {
      ASTNode$State state = this.state();
      return left.constant().intValue() <= right.constant().intValue();
   }

   public boolean assignableToInt() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isIntegralType() {
      ASTNode$State state = this.state();
      return true;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
