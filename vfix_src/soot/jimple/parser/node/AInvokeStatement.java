package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AInvokeStatement extends PStatement {
   private PInvokeExpr _invokeExpr_;
   private TSemicolon _semicolon_;

   public AInvokeStatement() {
   }

   public AInvokeStatement(PInvokeExpr _invokeExpr_, TSemicolon _semicolon_) {
      this.setInvokeExpr(_invokeExpr_);
      this.setSemicolon(_semicolon_);
   }

   public Object clone() {
      return new AInvokeStatement((PInvokeExpr)this.cloneNode(this._invokeExpr_), (TSemicolon)this.cloneNode(this._semicolon_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAInvokeStatement(this);
   }

   public PInvokeExpr getInvokeExpr() {
      return this._invokeExpr_;
   }

   public void setInvokeExpr(PInvokeExpr node) {
      if (this._invokeExpr_ != null) {
         this._invokeExpr_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._invokeExpr_ = node;
   }

   public TSemicolon getSemicolon() {
      return this._semicolon_;
   }

   public void setSemicolon(TSemicolon node) {
      if (this._semicolon_ != null) {
         this._semicolon_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._semicolon_ = node;
   }

   public String toString() {
      return "" + this.toString(this._invokeExpr_) + this.toString(this._semicolon_);
   }

   void removeChild(Node child) {
      if (this._invokeExpr_ == child) {
         this._invokeExpr_ = null;
      } else if (this._semicolon_ == child) {
         this._semicolon_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._invokeExpr_ == oldChild) {
         this.setInvokeExpr((PInvokeExpr)newChild);
      } else if (this._semicolon_ == oldChild) {
         this.setSemicolon((TSemicolon)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
