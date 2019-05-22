package soot.JastAddJ;

import java.util.Iterator;

public class EnumInstanceExpr extends ClassInstanceExpr implements Cloneable {
   protected boolean getAccess_computed = false;
   protected Access getAccess_value;
   protected boolean getArgList_computed = false;
   protected List<Expr> getArgList_value;

   public void flushCache() {
      super.flushCache();
      this.getAccess_computed = false;
      this.getAccess_value = null;
      this.getArgList_computed = false;
      this.getArgList_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public EnumInstanceExpr clone() throws CloneNotSupportedException {
      EnumInstanceExpr node = (EnumInstanceExpr)super.clone();
      node.getAccess_computed = false;
      node.getAccess_value = null;
      node.getArgList_computed = false;
      node.getArgList_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public EnumInstanceExpr copy() {
      try {
         EnumInstanceExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public EnumInstanceExpr fullCopy() {
      EnumInstanceExpr tree = this.copy();
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            switch(i) {
            case 1:
               tree.children[i] = null;
               break;
            case 2:
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

   public EnumInstanceExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new Opt(), 0);
      this.setChild(new List(), 2);
   }

   public EnumInstanceExpr(Opt<TypeDecl> p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setTypeDeclOpt(Opt<TypeDecl> opt) {
      this.setChild(opt, 0);
   }

   public boolean hasTypeDecl() {
      return this.getTypeDeclOpt().getNumChild() != 0;
   }

   public TypeDecl getTypeDecl() {
      return (TypeDecl)this.getTypeDeclOpt().getChild(0);
   }

   public void setTypeDecl(TypeDecl node) {
      this.getTypeDeclOpt().setChild(node, 0);
   }

   public Opt<TypeDecl> getTypeDeclOpt() {
      return (Opt)this.getChild(0);
   }

   public Opt<TypeDecl> getTypeDeclOptNoTransform() {
      return (Opt)this.getChildNoTransform(0);
   }

   public void setAccess(Access node) {
      this.setChild(node, 1);
   }

   public Access getAccessNoTransform() {
      return (Access)this.getChildNoTransform(1);
   }

   protected int getAccessChildPosition() {
      return 1;
   }

   public void setArgList(List<Expr> list) {
      this.setChild(list, 2);
   }

   public int getNumArg() {
      return this.getArgList().getNumChild();
   }

   public int getNumArgNoTransform() {
      return this.getArgListNoTransform().getNumChildNoTransform();
   }

   public Expr getArg(int i) {
      return (Expr)this.getArgList().getChild(i);
   }

   public void addArg(Expr node) {
      List<Expr> list = this.parent != null && state != null ? this.getArgList() : this.getArgListNoTransform();
      list.addChild(node);
   }

   public void addArgNoTransform(Expr node) {
      List<Expr> list = this.getArgListNoTransform();
      list.addChild(node);
   }

   public void setArg(Expr node, int i) {
      List<Expr> list = this.getArgList();
      list.setChild(node, i);
   }

   public List<Expr> getArgs() {
      return this.getArgList();
   }

   public List<Expr> getArgsNoTransform() {
      return this.getArgListNoTransform();
   }

   public List<Expr> getArgListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   protected int getArgListChildPosition() {
      return 2;
   }

   public Access getAccess() {
      if (this.getAccess_computed) {
         return (Access)this.getChild(this.getAccessChildPosition());
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getAccess_value = this.getAccess_compute();
         this.setAccess(this.getAccess_value);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getAccess_computed = true;
         }

         return (Access)this.getChild(this.getAccessChildPosition());
      }
   }

   private Access getAccess_compute() {
      return this.hostType().createQualifiedAccess();
   }

   public List<Expr> getArgList() {
      if (this.getArgList_computed) {
         return (List)this.getChild(this.getArgListChildPosition());
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getArgList_value = this.getArgList_compute();
         this.setArgList(this.getArgList_value);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getArgList_computed = true;
         }

         return (List)this.getChild(this.getArgListChildPosition());
      }
   }

   private List<Expr> getArgList_compute() {
      EnumConstant ec = (EnumConstant)this.getParent().getParent();
      List<EnumConstant> ecs = (List)ec.getParent();
      int idx = ecs.getIndexOfChild(ec);
      if (idx == -1) {
         throw new Error("internal: cannot determine numeric value of enum constant");
      } else {
         List<Expr> argList = new List();
         argList.add(Literal.buildStringLiteral(ec.name()));
         argList.add(Literal.buildIntegerLiteral(idx));
         Iterator var5 = ec.getArgs().iterator();

         while(var5.hasNext()) {
            Expr arg = (Expr)var5.next();
            argList.add((Expr)arg.fullCopy());
         }

         return argList;
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
