package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ABreakpointStatement extends PStatement {
   private TBreakpoint _breakpoint_;
   private TSemicolon _semicolon_;

   public ABreakpointStatement() {
   }

   public ABreakpointStatement(TBreakpoint _breakpoint_, TSemicolon _semicolon_) {
      this.setBreakpoint(_breakpoint_);
      this.setSemicolon(_semicolon_);
   }

   public Object clone() {
      return new ABreakpointStatement((TBreakpoint)this.cloneNode(this._breakpoint_), (TSemicolon)this.cloneNode(this._semicolon_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseABreakpointStatement(this);
   }

   public TBreakpoint getBreakpoint() {
      return this._breakpoint_;
   }

   public void setBreakpoint(TBreakpoint node) {
      if (this._breakpoint_ != null) {
         this._breakpoint_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._breakpoint_ = node;
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
      return "" + this.toString(this._breakpoint_) + this.toString(this._semicolon_);
   }

   void removeChild(Node child) {
      if (this._breakpoint_ == child) {
         this._breakpoint_ = null;
      } else if (this._semicolon_ == child) {
         this._semicolon_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._breakpoint_ == oldChild) {
         this.setBreakpoint((TBreakpoint)newChild);
      } else if (this._semicolon_ == oldChild) {
         this.setSemicolon((TSemicolon)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
