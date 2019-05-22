package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TLong extends Token {
   public TLong() {
      super.setText("long");
   }

   public TLong(int line, int pos) {
      super.setText("long");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TLong(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTLong(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TLong text.");
   }
}
