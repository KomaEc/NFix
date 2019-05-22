package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TInstanceof extends Token {
   public TInstanceof() {
      super.setText("instanceof");
   }

   public TInstanceof(int line, int pos) {
      super.setText("instanceof");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TInstanceof(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTInstanceof(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TInstanceof text.");
   }
}
