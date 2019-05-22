package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TFloat extends Token {
   public TFloat() {
      super.setText("float");
   }

   public TFloat(int line, int pos) {
      super.setText("float");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TFloat(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTFloat(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TFloat text.");
   }
}
