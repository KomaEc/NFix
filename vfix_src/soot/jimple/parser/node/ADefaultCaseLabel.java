package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ADefaultCaseLabel extends PCaseLabel {
   private TDefault _default_;

   public ADefaultCaseLabel() {
   }

   public ADefaultCaseLabel(TDefault _default_) {
      this.setDefault(_default_);
   }

   public Object clone() {
      return new ADefaultCaseLabel((TDefault)this.cloneNode(this._default_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseADefaultCaseLabel(this);
   }

   public TDefault getDefault() {
      return this._default_;
   }

   public void setDefault(TDefault node) {
      if (this._default_ != null) {
         this._default_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._default_ = node;
   }

   public String toString() {
      return "" + this.toString(this._default_);
   }

   void removeChild(Node child) {
      if (this._default_ == child) {
         this._default_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._default_ == oldChild) {
         this.setDefault((TDefault)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
