package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TEnum extends Token {
   public TEnum() {
      super.setText("enum");
   }

   public TEnum(int line, int pos) {
      super.setText("enum");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TEnum(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTEnum(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TEnum text.");
   }
}
