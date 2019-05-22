package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TPrivate extends Token {
   public TPrivate() {
      super.setText("private");
   }

   public TPrivate(int line, int pos) {
      super.setText("private");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TPrivate(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTPrivate(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TPrivate text.");
   }
}
