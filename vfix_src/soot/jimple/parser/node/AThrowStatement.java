package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AThrowStatement extends PStatement {
   private TThrow _throw_;
   private PImmediate _immediate_;
   private TSemicolon _semicolon_;

   public AThrowStatement() {
   }

   public AThrowStatement(TThrow _throw_, PImmediate _immediate_, TSemicolon _semicolon_) {
      this.setThrow(_throw_);
      this.setImmediate(_immediate_);
      this.setSemicolon(_semicolon_);
   }

   public Object clone() {
      return new AThrowStatement((TThrow)this.cloneNode(this._throw_), (PImmediate)this.cloneNode(this._immediate_), (TSemicolon)this.cloneNode(this._semicolon_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAThrowStatement(this);
   }

   public TThrow getThrow() {
      return this._throw_;
   }

   public void setThrow(TThrow node) {
      if (this._throw_ != null) {
         this._throw_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._throw_ = node;
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
      return "" + this.toString(this._throw_) + this.toString(this._immediate_) + this.toString(this._semicolon_);
   }

   void removeChild(Node child) {
      if (this._throw_ == child) {
         this._throw_ = null;
      } else if (this._immediate_ == child) {
         this._immediate_ = null;
      } else if (this._semicolon_ == child) {
         this._semicolon_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._throw_ == oldChild) {
         this.setThrow((TThrow)newChild);
      } else if (this._immediate_ == oldChild) {
         this.setImmediate((PImmediate)newChild);
      } else if (this._semicolon_ == oldChild) {
         this.setSemicolon((TSemicolon)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
