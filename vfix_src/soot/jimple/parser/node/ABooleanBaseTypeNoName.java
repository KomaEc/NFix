package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ABooleanBaseTypeNoName extends PBaseTypeNoName {
   private TBoolean _boolean_;

   public ABooleanBaseTypeNoName() {
   }

   public ABooleanBaseTypeNoName(TBoolean _boolean_) {
      this.setBoolean(_boolean_);
   }

   public Object clone() {
      return new ABooleanBaseTypeNoName((TBoolean)this.cloneNode(this._boolean_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseABooleanBaseTypeNoName(this);
   }

   public TBoolean getBoolean() {
      return this._boolean_;
   }

   public void setBoolean(TBoolean node) {
      if (this._boolean_ != null) {
         this._boolean_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._boolean_ = node;
   }

   public String toString() {
      return "" + this.toString(this._boolean_);
   }

   void removeChild(Node child) {
      if (this._boolean_ == child) {
         this._boolean_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._boolean_ == oldChild) {
         this.setBoolean((TBoolean)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
