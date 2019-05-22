package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TIf extends Token {
   public TIf() {
      super.setText("if");
   }

   public TIf(int line, int pos) {
      super.setText("if");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TIf(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTIf(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TIf text.");
   }
}
