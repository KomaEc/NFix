package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AAndBinop extends PBinop {
   private TAnd _and_;

   public AAndBinop() {
   }

   public AAndBinop(TAnd _and_) {
      this.setAnd(_and_);
   }

   public Object clone() {
      return new AAndBinop((TAnd)this.cloneNode(this._and_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAAndBinop(this);
   }

   public TAnd getAnd() {
      return this._and_;
   }

   public void setAnd(TAnd node) {
      if (this._and_ != null) {
         this._and_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._and_ = node;
   }

   public String toString() {
      return "" + this.toString(this._and_);
   }

   void removeChild(Node child) {
      if (this._and_ == child) {
         this._and_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._and_ == oldChild) {
         this.setAnd((TAnd)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
