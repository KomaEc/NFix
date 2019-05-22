package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TColonEquals extends Token {
   public TColonEquals() {
      super.setText(":=");
   }

   public TColonEquals(int line, int pos) {
      super.setText(":=");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TColonEquals(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTColonEquals(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TColonEquals text.");
   }
}
