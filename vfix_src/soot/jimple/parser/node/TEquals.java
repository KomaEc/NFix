package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TEquals extends Token {
   public TEquals() {
      super.setText("=");
   }

   public TEquals(int line, int pos) {
      super.setText("=");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TEquals(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTEquals(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TEquals text.");
   }
}
