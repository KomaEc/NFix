package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ANopStatement extends PStatement {
   private TNop _nop_;
   private TSemicolon _semicolon_;

   public ANopStatement() {
   }

   public ANopStatement(TNop _nop_, TSemicolon _semicolon_) {
      this.setNop(_nop_);
      this.setSemicolon(_semicolon_);
   }

   public Object clone() {
      return new ANopStatement((TNop)this.cloneNode(this._nop_), (TSemicolon)this.cloneNode(this._semicolon_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseANopStatement(this);
   }

   public TNop getNop() {
      return this._nop_;
   }

   public void setNop(TNop node) {
      if (this._nop_ != null) {
         this._nop_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._nop_ = node;
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
      return "" + this.toString(this._nop_) + this.toString(this._semicolon_);
   }

   void removeChild(Node child) {
      if (this._nop_ == child) {
         this._nop_ = null;
      } else if (this._semicolon_ == child) {
         this._semicolon_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._nop_ == oldChild) {
         this.setNop((TNop)newChild);
      } else if (this._semicolon_ == oldChild) {
         this.setSemicolon((TSemicolon)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
