package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AIdentName extends PName {
   private TIdentifier _identifier_;

   public AIdentName() {
   }

   public AIdentName(TIdentifier _identifier_) {
      this.setIdentifier(_identifier_);
   }

   public Object clone() {
      return new AIdentName((TIdentifier)this.cloneNode(this._identifier_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAIdentName(this);
   }

   public TIdentifier getIdentifier() {
      return this._identifier_;
   }

   public void setIdentifier(TIdentifier node) {
      if (this._identifier_ != null) {
         this._identifier_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._identifier_ = node;
   }

   public String toString() {
      return "" + this.toString(this._identifier_);
   }

   void removeChild(Node child) {
      if (this._identifier_ == child) {
         this._identifier_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._identifier_ == oldChild) {
         this.setIdentifier((TIdentifier)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
