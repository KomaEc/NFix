package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ACharBaseTypeNoName extends PBaseTypeNoName {
   private TChar _char_;

   public ACharBaseTypeNoName() {
   }

   public ACharBaseTypeNoName(TChar _char_) {
      this.setChar(_char_);
   }

   public Object clone() {
      return new ACharBaseTypeNoName((TChar)this.cloneNode(this._char_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseACharBaseTypeNoName(this);
   }

   public TChar getChar() {
      return this._char_;
   }

   public void setChar(TChar node) {
      if (this._char_ != null) {
         this._char_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._char_ = node;
   }

   public String toString() {
      return "" + this.toString(this._char_);
   }

   void removeChild(Node child) {
      if (this._char_ == child) {
         this._char_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._char_ == oldChild) {
         this.setChar((TChar)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
