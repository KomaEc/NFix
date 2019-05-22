package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AStaticModifier extends PModifier {
   private TStatic _static_;

   public AStaticModifier() {
   }

   public AStaticModifier(TStatic _static_) {
      this.setStatic(_static_);
   }

   public Object clone() {
      return new AStaticModifier((TStatic)this.cloneNode(this._static_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAStaticModifier(this);
   }

   public TStatic getStatic() {
      return this._static_;
   }

   public void setStatic(TStatic node) {
      if (this._static_ != null) {
         this._static_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._static_ = node;
   }

   public String toString() {
      return "" + this.toString(this._static_);
   }

   void removeChild(Node child) {
      if (this._static_ == child) {
         this._static_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._static_ == oldChild) {
         this.setStatic((TStatic)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
