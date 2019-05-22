package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TExitmonitor extends Token {
   public TExitmonitor() {
      super.setText("exitmonitor");
   }

   public TExitmonitor(int line, int pos) {
      super.setText("exitmonitor");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TExitmonitor(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTExitmonitor(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TExitmonitor text.");
   }
}
