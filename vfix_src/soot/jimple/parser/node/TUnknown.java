package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TUnknown extends Token {
   public TUnknown() {
      super.setText("unknown");
   }

   public TUnknown(int line, int pos) {
      super.setText("unknown");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TUnknown(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTUnknown(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TUnknown text.");
   }
}
