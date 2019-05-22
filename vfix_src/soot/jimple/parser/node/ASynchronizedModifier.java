package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ASynchronizedModifier extends PModifier {
   private TSynchronized _synchronized_;

   public ASynchronizedModifier() {
   }

   public ASynchronizedModifier(TSynchronized _synchronized_) {
      this.setSynchronized(_synchronized_);
   }

   public Object clone() {
      return new ASynchronizedModifier((TSynchronized)this.cloneNode(this._synchronized_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseASynchronizedModifier(this);
   }

   public TSynchronized getSynchronized() {
      return this._synchronized_;
   }

   public void setSynchronized(TSynchronized node) {
      if (this._synchronized_ != null) {
         this._synchronized_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._synchronized_ = node;
   }

   public String toString() {
      return "" + this.toString(this._synchronized_);
   }

   void removeChild(Node child) {
      if (this._synchronized_ == child) {
         this._synchronized_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._synchronized_ == oldChild) {
         this.setSynchronized((TSynchronized)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
