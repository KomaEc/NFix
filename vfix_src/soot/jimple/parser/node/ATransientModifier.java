package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ATransientModifier extends PModifier {
   private TTransient _transient_;

   public ATransientModifier() {
   }

   public ATransientModifier(TTransient _transient_) {
      this.setTransient(_transient_);
   }

   public Object clone() {
      return new ATransientModifier((TTransient)this.cloneNode(this._transient_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseATransientModifier(this);
   }

   public TTransient getTransient() {
      return this._transient_;
   }

   public void setTransient(TTransient node) {
      if (this._transient_ != null) {
         this._transient_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._transient_ = node;
   }

   public String toString() {
      return "" + this.toString(this._transient_);
   }

   void removeChild(Node child) {
      if (this._transient_ == child) {
         this._transient_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._transient_ == oldChild) {
         this.setTransient((TTransient)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
