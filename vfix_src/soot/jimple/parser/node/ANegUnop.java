package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ANegUnop extends PUnop {
   private TNeg _neg_;

   public ANegUnop() {
   }

   public ANegUnop(TNeg _neg_) {
      this.setNeg(_neg_);
   }

   public Object clone() {
      return new ANegUnop((TNeg)this.cloneNode(this._neg_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseANegUnop(this);
   }

   public TNeg getNeg() {
      return this._neg_;
   }

   public void setNeg(TNeg node) {
      if (this._neg_ != null) {
         this._neg_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._neg_ = node;
   }

   public String toString() {
      return "" + this.toString(this._neg_);
   }

   void removeChild(Node child) {
      if (this._neg_ == child) {
         this._neg_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._neg_ == oldChild) {
         this.setNeg((TNeg)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
