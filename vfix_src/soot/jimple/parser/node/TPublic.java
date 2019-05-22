package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TPublic extends Token {
   public TPublic() {
      super.setText("public");
   }

   public TPublic(int line, int pos) {
      super.setText("public");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TPublic(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTPublic(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TPublic text.");
   }
}
