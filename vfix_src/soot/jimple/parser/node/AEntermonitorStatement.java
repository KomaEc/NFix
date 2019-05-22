package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AEntermonitorStatement extends PStatement {
   private TEntermonitor _entermonitor_;
   private PImmediate _immediate_;
   private TSemicolon _semicolon_;

   public AEntermonitorStatement() {
   }

   public AEntermonitorStatement(TEntermonitor _entermonitor_, PImmediate _immediate_, TSemicolon _semicolon_) {
      this.setEntermonitor(_entermonitor_);
      this.setImmediate(_immediate_);
      this.setSemicolon(_semicolon_);
   }

   public Object clone() {
      return new AEntermonitorStatement((TEntermonitor)this.cloneNode(this._entermonitor_), (PImmediate)this.cloneNode(this._immediate_), (TSemicolon)this.cloneNode(this._semicolon_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAEntermonitorStatement(this);
   }

   public TEntermonitor getEntermonitor() {
      return this._entermonitor_;
   }

   public void setEntermonitor(TEntermonitor node) {
      if (this._entermonitor_ != null) {
         this._entermonitor_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._entermonitor_ = node;
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
      return "" + this.toString(this._entermonitor_) + this.toString(this._immediate_) + this.toString(this._semicolon_);
   }

   void removeChild(Node child) {
      if (this._entermonitor_ == child) {
         this._entermonitor_ = null;
      } else if (this._immediate_ == child) {
         this._immediate_ = null;
      } else if (this._semicolon_ == child) {
         this._semicolon_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._entermonitor_ == oldChild) {
         this.setEntermonitor((TEntermonitor)newChild);
      } else if (this._immediate_ == oldChild) {
         this.setImmediate((PImmediate)newChild);
      } else if (this._semicolon_ == oldChild) {
         this.setSemicolon((TSemicolon)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
