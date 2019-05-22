package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TWith extends Token {
   public TWith() {
      super.setText("with");
   }

   public TWith(int line, int pos) {
      super.setText("with");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TWith(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTWith(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TWith text.");
   }
}
