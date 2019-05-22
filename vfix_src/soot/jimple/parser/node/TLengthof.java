package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TLengthof extends Token {
   public TLengthof() {
      super.setText("lengthof");
   }

   public TLengthof(int line, int pos) {
      super.setText("lengthof");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TLengthof(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTLengthof(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TLengthof text.");
   }
}
