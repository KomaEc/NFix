package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ASpecialNonstaticInvoke extends PNonstaticInvoke {
   private TSpecialinvoke _specialinvoke_;

   public ASpecialNonstaticInvoke() {
   }

   public ASpecialNonstaticInvoke(TSpecialinvoke _specialinvoke_) {
      this.setSpecialinvoke(_specialinvoke_);
   }

   public Object clone() {
      return new ASpecialNonstaticInvoke((TSpecialinvoke)this.cloneNode(this._specialinvoke_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseASpecialNonstaticInvoke(this);
   }

   public TSpecialinvoke getSpecialinvoke() {
      return this._specialinvoke_;
   }

   public void setSpecialinvoke(TSpecialinvoke node) {
      if (this._specialinvoke_ != null) {
         this._specialinvoke_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._specialinvoke_ = node;
   }

   public String toString() {
      return "" + this.toString(this._specialinvoke_);
   }

   void removeChild(Node child) {
      if (this._specialinvoke_ == child) {
         this._specialinvoke_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._specialinvoke_ == oldChild) {
         this.setSpecialinvoke((TSpecialinvoke)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
