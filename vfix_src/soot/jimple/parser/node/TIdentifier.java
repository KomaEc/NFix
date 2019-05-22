package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TIdentifier extends Token {
   public TIdentifier(String text) {
      this.setText(text);
   }

   public TIdentifier(String text, int line, int pos) {
      this.setText(text);
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TIdentifier(this.getText(), this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTIdentifier(this);
   }
}
