package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AGotoStatement extends PStatement {
   private PGotoStmt _gotoStmt_;

   public AGotoStatement() {
   }

   public AGotoStatement(PGotoStmt _gotoStmt_) {
      this.setGotoStmt(_gotoStmt_);
   }

   public Object clone() {
      return new AGotoStatement((PGotoStmt)this.cloneNode(this._gotoStmt_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAGotoStatement(this);
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
      return "" + this.toString(this._gotoStmt_);
   }

   void removeChild(Node child) {
      if (this._gotoStmt_ == child) {
         this._gotoStmt_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._gotoStmt_ == oldChild) {
         this.setGotoStmt((PGotoStmt)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
