package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AUnknownJimpleType extends PJimpleType {
   private TUnknown _unknown_;

   public AUnknownJimpleType() {
   }

   public AUnknownJimpleType(TUnknown _unknown_) {
      this.setUnknown(_unknown_);
   }

   public Object clone() {
      return new AUnknownJimpleType((TUnknown)this.cloneNode(this._unknown_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAUnknownJimpleType(this);
   }

   public TUnknown getUnknown() {
      return this._unknown_;
   }

   public void setUnknown(TUnknown node) {
      if (this._unknown_ != null) {
         this._unknown_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._unknown_ = node;
   }

   public String toString() {
      return "" + this.toString(this._unknown_);
   }

   void removeChild(Node child) {
      if (this._unknown_ == child) {
         this._unknown_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._unknown_ == oldChild) {
         this.setUnknown((TUnknown)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
