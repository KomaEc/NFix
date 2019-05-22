package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AFloatBaseTypeNoName extends PBaseTypeNoName {
   private TFloat _float_;

   public AFloatBaseTypeNoName() {
   }

   public AFloatBaseTypeNoName(TFloat _float_) {
      this.setFloat(_float_);
   }

   public Object clone() {
      return new AFloatBaseTypeNoName((TFloat)this.cloneNode(this._float_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAFloatBaseTypeNoName(this);
   }

   public TFloat getFloat() {
      return this._float_;
   }

   public void setFloat(TFloat node) {
      if (this._float_ != null) {
         this._float_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._float_ = node;
   }

   public String toString() {
      return "" + this.toString(this._float_);
   }

   void removeChild(Node child) {
      if (this._float_ == child) {
         this._float_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._float_ == oldChild) {
         this.setFloat((TFloat)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
