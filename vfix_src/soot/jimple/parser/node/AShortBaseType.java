package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AShortBaseType extends PBaseType {
   private TShort _short_;

   public AShortBaseType() {
   }

   public AShortBaseType(TShort _short_) {
      this.setShort(_short_);
   }

   public Object clone() {
      return new AShortBaseType((TShort)this.cloneNode(this._short_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAShortBaseType(this);
   }

   public TShort getShort() {
      return this._short_;
   }

   public void setShort(TShort node) {
      if (this._short_ != null) {
         this._short_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._short_ = node;
   }

   public String toString() {
      return "" + this.toString(this._short_);
   }

   void removeChild(Node child) {
      if (this._short_ == child) {
         this._short_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._short_ == oldChild) {
         this.setShort((TShort)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
