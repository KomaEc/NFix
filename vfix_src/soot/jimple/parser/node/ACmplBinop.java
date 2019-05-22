package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ACmplBinop extends PBinop {
   private TCmpl _cmpl_;

   public ACmplBinop() {
   }

   public ACmplBinop(TCmpl _cmpl_) {
      this.setCmpl(_cmpl_);
   }

   public Object clone() {
      return new ACmplBinop((TCmpl)this.cloneNode(this._cmpl_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseACmplBinop(this);
   }

   public TCmpl getCmpl() {
      return this._cmpl_;
   }

   public void setCmpl(TCmpl node) {
      if (this._cmpl_ != null) {
         this._cmpl_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._cmpl_ = node;
   }

   public String toString() {
      return "" + this.toString(this._cmpl_);
   }

   void removeChild(Node child) {
      if (this._cmpl_ == child) {
         this._cmpl_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._cmpl_ == oldChild) {
         this.setCmpl((TCmpl)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
