package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TCase extends Token {
   public TCase() {
      super.setText("case");
   }

   public TCase(int line, int pos) {
      super.setText("case");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TCase(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTCase(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TCase text.");
   }
}
