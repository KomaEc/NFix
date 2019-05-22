package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TFinal extends Token {
   public TFinal() {
      super.setText("final");
   }

   public TFinal(int line, int pos) {
      super.setText("final");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TFinal(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTFinal(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TFinal text.");
   }
}
