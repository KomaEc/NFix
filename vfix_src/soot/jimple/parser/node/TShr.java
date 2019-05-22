package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TShr extends Token {
   public TShr() {
      super.setText(">>");
   }

   public TShr(int line, int pos) {
      super.setText(">>");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TShr(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTShr(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TShr text.");
   }
}
