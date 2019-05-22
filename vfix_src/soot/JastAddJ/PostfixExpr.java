package soot.JastAddJ;

public abstract class PostfixExpr extends Unary implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public PostfixExpr clone() throws CloneNotSupportedException {
      PostfixExpr node = (PostfixExpr)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public void definiteAssignment() {
      if (this.getOperand().isVariable()) {
         Variable v = this.getOperand().varDecl();
         if (v != null && v.isFinal()) {
            this.error("++ and -- can not be applied to final variable " + v);
         }
      }

   }

   protected boolean checkDUeverywhere(Variable v) {
      return this.getOperand().isVariable() && this.getOperand().varDecl() == v && !this.isDAbefore(v) ? false : super.checkDUeverywhere(v);
   }

   public void typeCheck() {
      if (!this.getOperand().isVariable()) {
         this.error("postfix expressions only work on variables");
      } else if (!this.getOperand().type().isNumericType()) {
         this.error("postfix expressions only operates on numeric types");
      }

   }

   public PostfixExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public PostfixExpr(Expr p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setOperand(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getOperand() {
      return (Expr)this.getChild(0);
   }

   public Expr getOperandNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
      return caller == this.getOperandNoTransform() ? true : this.getParent().Define_boolean_isDest(this, caller);
   }

   public boolean Define_boolean_isIncOrDec(ASTNode caller, ASTNode child) {
      return caller == this.getOperandNoTransform() ? true : this.getParent().Define_boolean_isIncOrDec(this, caller);
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getOperandNoTransform() ? NameType.EXPRESSION_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
