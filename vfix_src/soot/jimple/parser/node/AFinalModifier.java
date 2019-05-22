package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AFinalModifier extends PModifier {
   private TFinal _final_;

   public AFinalModifier() {
   }

   public AFinalModifier(TFinal _final_) {
      this.setFinal(_final_);
   }

   public Object clone() {
      return new AFinalModifier((TFinal)this.cloneNode(this._final_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAFinalModifier(this);
   }

   public TFinal getFinal() {
      return this._final_;
   }

   public void setFinal(TFinal node) {
      if (this._final_ != null) {
         this._final_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._final_ = node;
   }

   public String toString() {
      return "" + this.toString(this._final_);
   }

   void removeChild(Node child) {
      if (this._final_ == child) {
         this._final_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._final_ == oldChild) {
         this.setFinal((TFinal)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
