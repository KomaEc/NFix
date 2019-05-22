package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AReturnStatement extends PStatement {
   private TReturn _return_;
   private PImmediate _immediate_;
   private TSemicolon _semicolon_;

   public AReturnStatement() {
   }

   public AReturnStatement(TReturn _return_, PImmediate _immediate_, TSemicolon _semicolon_) {
      this.setReturn(_return_);
      this.setImmediate(_immediate_);
      this.setSemicolon(_semicolon_);
   }

   public Object clone() {
      return new AReturnStatement((TReturn)this.cloneNode(this._return_), (PImmediate)this.cloneNode(this._immediate_), (TSemicolon)this.cloneNode(this._semicolon_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAReturnStatement(this);
   }

   public TReturn getReturn() {
      return this._return_;
   }

   public void setReturn(TReturn node) {
      if (this._return_ != null) {
         this._return_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._return_ = node;
   }

   public PImmediate getImmediate() {
      return this._immediate_;
   }

   public void setImmediate(PImmediate node) {
      if (this._immediate_ != null) {
         this._immediate_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._immediate_ = node;
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
      return "" + this.toString(this._return_) + this.toString(this._immediate_) + this.toString(this._semicolon_);
   }

   void removeChild(Node child) {
      if (this._return_ == child) {
         this._return_ = null;
      } else if (this._immediate_ == child) {
         this._immediate_ = null;
      } else if (this._semicolon_ == child) {
         this._semicolon_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._return_ == oldChild) {
         this.setReturn((TReturn)newChild);
      } else if (this._immediate_ == oldChild) {
         this.setImmediate((PImmediate)newChild);
      } else if (this._semicolon_ == oldChild) {
         this.setSemicolon((TSemicolon)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
