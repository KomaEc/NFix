package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TThrow extends Token {
   public TThrow() {
      super.setText("throw");
   }

   public TThrow(int line, int pos) {
      super.setText("throw");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TThrow(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTThrow(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TThrow text.");
   }
}
