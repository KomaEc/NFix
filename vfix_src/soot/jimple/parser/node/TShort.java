package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TShort extends Token {
   public TShort() {
      super.setText("short");
   }

   public TShort(int line, int pos) {
      super.setText("short");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TShort(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTShort(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TShort text.");
   }
}
