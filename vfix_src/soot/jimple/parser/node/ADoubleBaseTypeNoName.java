package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ADoubleBaseTypeNoName extends PBaseTypeNoName {
   private TDouble _double_;

   public ADoubleBaseTypeNoName() {
   }

   public ADoubleBaseTypeNoName(TDouble _double_) {
      this.setDouble(_double_);
   }

   public Object clone() {
      return new ADoubleBaseTypeNoName((TDouble)this.cloneNode(this._double_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseADoubleBaseTypeNoName(this);
   }

   public TDouble getDouble() {
      return this._double_;
   }

   public void setDouble(TDouble node) {
      if (this._double_ != null) {
         this._double_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._double_ = node;
   }

   public String toString() {
      return "" + this.toString(this._double_);
   }

   void removeChild(Node child) {
      if (this._double_ == child) {
         this._double_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._double_ == oldChild) {
         this.setDouble((TDouble)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
