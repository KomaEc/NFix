package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AStringConstant extends PConstant {
   private TStringConstant _stringConstant_;

   public AStringConstant() {
   }

   public AStringConstant(TStringConstant _stringConstant_) {
      this.setStringConstant(_stringConstant_);
   }

   public Object clone() {
      return new AStringConstant((TStringConstant)this.cloneNode(this._stringConstant_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAStringConstant(this);
   }

   public TStringConstant getStringConstant() {
      return this._stringConstant_;
   }

   public void setStringConstant(TStringConstant node) {
      if (this._stringConstant_ != null) {
         this._stringConstant_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._stringConstant_ = node;
   }

   public String toString() {
      return "" + this.toString(this._stringConstant_);
   }

   void removeChild(Node child) {
      if (this._stringConstant_ == child) {
         this._stringConstant_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._stringConstant_ == oldChild) {
         this.setStringConstant((TStringConstant)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
