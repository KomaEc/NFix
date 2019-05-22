package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TThrows extends Token {
   public TThrows() {
      super.setText("throws");
   }

   public TThrows(int line, int pos) {
      super.setText("throws");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TThrows(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTThrows(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TThrows text.");
   }
}
