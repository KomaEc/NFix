package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AStrictfpModifier extends PModifier {
   private TStrictfp _strictfp_;

   public AStrictfpModifier() {
   }

   public AStrictfpModifier(TStrictfp _strictfp_) {
      this.setStrictfp(_strictfp_);
   }

   public Object clone() {
      return new AStrictfpModifier((TStrictfp)this.cloneNode(this._strictfp_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAStrictfpModifier(this);
   }

   public TStrictfp getStrictfp() {
      return this._strictfp_;
   }

   public void setStrictfp(TStrictfp node) {
      if (this._strictfp_ != null) {
         this._strictfp_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._strictfp_ = node;
   }

   public String toString() {
      return "" + this.toString(this._strictfp_);
   }

   void removeChild(Node child) {
      if (this._strictfp_ == child) {
         this._strictfp_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._strictfp_ == oldChild) {
         this.setStrictfp((TStrictfp)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
