package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TEntermonitor extends Token {
   public TEntermonitor() {
      super.setText("entermonitor");
   }

   public TEntermonitor(int line, int pos) {
      super.setText("entermonitor");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TEntermonitor(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTEntermonitor(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TEntermonitor text.");
   }
}
