package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TProtected extends Token {
   public TProtected() {
      super.setText("protected");
   }

   public TProtected(int line, int pos) {
      super.setText("protected");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TProtected(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTProtected(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TProtected text.");
   }
}
