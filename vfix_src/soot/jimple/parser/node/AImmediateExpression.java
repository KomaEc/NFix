package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AImmediateExpression extends PExpression {
   private PImmediate _immediate_;

   public AImmediateExpression() {
   }

   public AImmediateExpression(PImmediate _immediate_) {
      this.setImmediate(_immediate_);
   }

   public Object clone() {
      return new AImmediateExpression((PImmediate)this.cloneNode(this._immediate_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAImmediateExpression(this);
   }

   public PImmediate getImmediate() {
      return this._immediate_;
   }

   public void setImmediate(PImmediate node) {
      if (this._immediate_ != null) {
         this._immediate_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._immediate_ = node;
   }

   public String toString() {
      return "" + this.toString(this._immediate_);
   }

   void removeChild(Node child) {
      if (this._immediate_ == child) {
         this._immediate_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._immediate_ == oldChild) {
         this.setImmediate((PImmediate)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
