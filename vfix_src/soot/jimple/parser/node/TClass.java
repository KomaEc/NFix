package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TClass extends Token {
   public TClass() {
      super.setText("class");
   }

   public TClass(int line, int pos) {
      super.setText("class");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TClass(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTClass(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TClass text.");
   }
}
