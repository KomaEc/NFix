package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TBreakpoint extends Token {
   public TBreakpoint() {
      super.setText("breakpoint");
   }

   public TBreakpoint(int line, int pos) {
      super.setText("breakpoint");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TBreakpoint(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTBreakpoint(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TBreakpoint text.");
   }
}
