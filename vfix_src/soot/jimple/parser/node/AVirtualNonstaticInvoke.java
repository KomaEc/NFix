package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AVirtualNonstaticInvoke extends PNonstaticInvoke {
   private TVirtualinvoke _virtualinvoke_;

   public AVirtualNonstaticInvoke() {
   }

   public AVirtualNonstaticInvoke(TVirtualinvoke _virtualinvoke_) {
      this.setVirtualinvoke(_virtualinvoke_);
   }

   public Object clone() {
      return new AVirtualNonstaticInvoke((TVirtualinvoke)this.cloneNode(this._virtualinvoke_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAVirtualNonstaticInvoke(this);
   }

   public TVirtualinvoke getVirtualinvoke() {
      return this._virtualinvoke_;
   }

   public void setVirtualinvoke(TVirtualinvoke node) {
      if (this._virtualinvoke_ != null) {
         this._virtualinvoke_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._virtualinvoke_ = node;
   }

   public String toString() {
      return "" + this.toString(this._virtualinvoke_);
   }

   void removeChild(Node child) {
      if (this._virtualinvoke_ == child) {
         this._virtualinvoke_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._virtualinvoke_ == oldChild) {
         this.setVirtualinvoke((TVirtualinvoke)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
