package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TQuote extends Token {
   public TQuote() {
      super.setText("'");
   }

   public TQuote(int line, int pos) {
      super.setText("'");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TQuote(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTQuote(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TQuote text.");
   }
}
