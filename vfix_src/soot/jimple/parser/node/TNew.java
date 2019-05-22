package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TNew extends Token {
   public TNew() {
      super.setText("new");
   }

   public TNew(int line, int pos) {
      super.setText("new");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TNew(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTNew(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TNew text.");
   }
}
