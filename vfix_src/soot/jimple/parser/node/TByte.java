package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TByte extends Token {
   public TByte() {
      super.setText("byte");
   }

   public TByte(int line, int pos) {
      super.setText("byte");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TByte(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTByte(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TByte text.");
   }
}
