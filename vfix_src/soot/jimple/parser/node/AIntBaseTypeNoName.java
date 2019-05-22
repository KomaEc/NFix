package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AIntBaseTypeNoName extends PBaseTypeNoName {
   private TInt _int_;

   public AIntBaseTypeNoName() {
   }

   public AIntBaseTypeNoName(TInt _int_) {
      this.setInt(_int_);
   }

   public Object clone() {
      return new AIntBaseTypeNoName((TInt)this.cloneNode(this._int_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAIntBaseTypeNoName(this);
   }

   public TInt getInt() {
      return this._int_;
   }

   public void setInt(TInt node) {
      if (this._int_ != null) {
         this._int_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._int_ = node;
   }

   public String toString() {
      return "" + this.toString(this._int_);
   }

   void removeChild(Node child) {
      if (this._int_ == child) {
         this._int_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._int_ == oldChild) {
         this.setInt((TInt)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
