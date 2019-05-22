package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AReferenceExpression extends PExpression {
   private PReference _reference_;

   public AReferenceExpression() {
   }

   public AReferenceExpression(PReference _reference_) {
      this.setReference(_reference_);
   }

   public Object clone() {
      return new AReferenceExpression((PReference)this.cloneNode(this._reference_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAReferenceExpression(this);
   }

   public PReference getReference() {
      return this._reference_;
   }

   public void setReference(PReference node) {
      if (this._reference_ != null) {
         this._reference_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._reference_ = node;
   }

   public String toString() {
      return "" + this.toString(this._reference_);
   }

   void removeChild(Node child) {
      if (this._reference_ == child) {
         this._reference_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._reference_ == oldChild) {
         this.setReference((PReference)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
