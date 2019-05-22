package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TSemicolon extends Token {
   public TSemicolon() {
      super.setText(";");
   }

   public TSemicolon(int line, int pos) {
      super.setText(";");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TSemicolon(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTSemicolon(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TSemicolon text.");
   }
}
