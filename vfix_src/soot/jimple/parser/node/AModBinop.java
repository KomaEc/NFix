package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AModBinop extends PBinop {
   private TMod _mod_;

   public AModBinop() {
   }

   public AModBinop(TMod _mod_) {
      this.setMod(_mod_);
   }

   public Object clone() {
      return new AModBinop((TMod)this.cloneNode(this._mod_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAModBinop(this);
   }

   public TMod getMod() {
      return this._mod_;
   }

   public void setMod(TMod node) {
      if (this._mod_ != null) {
         this._mod_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._mod_ = node;
   }

   public String toString() {
      return "" + this.toString(this._mod_);
   }

   void removeChild(Node child) {
      if (this._mod_ == child) {
         this._mod_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._mod_ == oldChild) {
         this.setMod((TMod)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
