package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ALengthofUnop extends PUnop {
   private TLengthof _lengthof_;

   public ALengthofUnop() {
   }

   public ALengthofUnop(TLengthof _lengthof_) {
      this.setLengthof(_lengthof_);
   }

   public Object clone() {
      return new ALengthofUnop((TLengthof)this.cloneNode(this._lengthof_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseALengthofUnop(this);
   }

   public TLengthof getLengthof() {
      return this._lengthof_;
   }

   public void setLengthof(TLengthof node) {
      if (this._lengthof_ != null) {
         this._lengthof_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._lengthof_ = node;
   }

   public String toString() {
      return "" + this.toString(this._lengthof_);
   }

   void removeChild(Node child) {
      if (this._lengthof_ == child) {
         this._lengthof_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._lengthof_ == oldChild) {
         this.setLengthof((TLengthof)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
