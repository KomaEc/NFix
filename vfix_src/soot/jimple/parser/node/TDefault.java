package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TDefault extends Token {
   public TDefault() {
      super.setText("default");
   }

   public TDefault(int line, int pos) {
      super.setText("default");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TDefault(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTDefault(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TDefault text.");
   }
}
