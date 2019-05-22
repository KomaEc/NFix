package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AUshrBinop extends PBinop {
   private TUshr _ushr_;

   public AUshrBinop() {
   }

   public AUshrBinop(TUshr _ushr_) {
      this.setUshr(_ushr_);
   }

   public Object clone() {
      return new AUshrBinop((TUshr)this.cloneNode(this._ushr_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAUshrBinop(this);
   }

   public TUshr getUshr() {
      return this._ushr_;
   }

   public void setUshr(TUshr node) {
      if (this._ushr_ != null) {
         this._ushr_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._ushr_ = node;
   }

   public String toString() {
      return "" + this.toString(this._ushr_);
   }

   void removeChild(Node child) {
      if (this._ushr_ == child) {
         this._ushr_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._ushr_ == oldChild) {
         this.setUshr((TUshr)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
