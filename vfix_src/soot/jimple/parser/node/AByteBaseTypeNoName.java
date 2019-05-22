package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AByteBaseTypeNoName extends PBaseTypeNoName {
   private TByte _byte_;

   public AByteBaseTypeNoName() {
   }

   public AByteBaseTypeNoName(TByte _byte_) {
      this.setByte(_byte_);
   }

   public Object clone() {
      return new AByteBaseTypeNoName((TByte)this.cloneNode(this._byte_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAByteBaseTypeNoName(this);
   }

   public TByte getByte() {
      return this._byte_;
   }

   public void setByte(TByte node) {
      if (this._byte_ != null) {
         this._byte_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._byte_ = node;
   }

   public String toString() {
      return "" + this.toString(this._byte_);
   }

   void removeChild(Node child) {
      if (this._byte_ == child) {
         this._byte_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._byte_ == oldChild) {
         this.setByte((TByte)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
