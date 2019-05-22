package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AIfStatement extends PStatement {
   private TIf _if_;
   private PBoolExpr _boolExpr_;
   private PGotoStmt _gotoStmt_;

   public AIfStatement() {
   }

   public AIfStatement(TIf _if_, PBoolExpr _boolExpr_, PGotoStmt _gotoStmt_) {
      this.setIf(_if_);
      this.setBoolExpr(_boolExpr_);
      this.setGotoStmt(_gotoStmt_);
   }

   public Object clone() {
      return new AIfStatement((TIf)this.cloneNode(this._if_), (PBoolExpr)this.cloneNode(this._boolExpr_), (PGotoStmt)this.cloneNode(this._gotoStmt_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAIfStatement(this);
   }

   public TIf getIf() {
      return this._if_;
   }

   public void setIf(TIf node) {
      if (this._if_ != null) {
         this._if_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._if_ = node;
   }

   public PBoolExpr getBoolExpr() {
      return this._boolExpr_;
   }

   public void setBoolExpr(PBoolExpr node) {
      if (this._boolExpr_ != null) {
         this._boolExpr_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._boolExpr_ = node;
   }

   public PGotoStmt getGotoStmt() {
      return this._gotoStmt_;
   }

   public void setGotoStmt(PGotoStmt node) {
      if (this._gotoStmt_ != null) {
         this._gotoStmt_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._gotoStmt_ = node;
   }

   public String toString() {
      return "" + this.toString(this._if_) + this.toString(this._boolExpr_) + this.toString(this._gotoStmt_);
   }

   void removeChild(Node child) {
      if (this._if_ == child) {
         this._if_ = null;
      } else if (this._boolExpr_ == child) {
         this._boolExpr_ = null;
      } else if (this._gotoStmt_ == child) {
         this._gotoStmt_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._if_ == oldChild) {
         this.setIf((TIf)newChild);
      } else if (this._boolExpr_ == oldChild) {
         this.setBoolExpr((PBoolExpr)newChild);
      } else if (this._gotoStmt_ == oldChild) {
         this.setGotoStmt((PGotoStmt)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
