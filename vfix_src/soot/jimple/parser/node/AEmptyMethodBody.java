package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AEmptyMethodBody extends PMethodBody {
   private TSemicolon _semicolon_;

   public AEmptyMethodBody() {
   }

   public AEmptyMethodBody(TSemicolon _semicolon_) {
      this.setSemicolon(_semicolon_);
   }

   public Object clone() {
      return new AEmptyMethodBody((TSemicolon)this.cloneNode(this._semicolon_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAEmptyMethodBody(this);
   }

   public TSemicolon getSemicolon() {
      return this._semicolon_;
   }

   public void setSemicolon(TSemicolon node) {
      if (this._semicolon_ != null) {
         this._semicolon_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._semicolon_ = node;
   }

   public String toString() {
      return "" + this.toString(this._semicolon_);
   }

   void removeChild(Node child) {
      if (this._semicolon_ == child) {
         this._semicolon_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._semicolon_ == oldChild) {
         this.setSemicolon((TSemicolon)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
