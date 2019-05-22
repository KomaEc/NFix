package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AVolatileModifier extends PModifier {
   private TVolatile _volatile_;

   public AVolatileModifier() {
   }

   public AVolatileModifier(TVolatile _volatile_) {
      this.setVolatile(_volatile_);
   }

   public Object clone() {
      return new AVolatileModifier((TVolatile)this.cloneNode(this._volatile_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAVolatileModifier(this);
   }

   public TVolatile getVolatile() {
      return this._volatile_;
   }

   public void setVolatile(TVolatile node) {
      if (this._volatile_ != null) {
         this._volatile_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._volatile_ = node;
   }

   public String toString() {
      return "" + this.toString(this._volatile_);
   }

   void removeChild(Node child) {
      if (this._volatile_ == child) {
         this._volatile_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._volatile_ == oldChild) {
         this.setVolatile((TVolatile)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
