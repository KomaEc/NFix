package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TFullIdentifier extends Token {
   public TFullIdentifier(String text) {
      this.setText(text);
   }

   public TFullIdentifier(String text, int line, int pos) {
      this.setText(text);
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TFullIdentifier(this.getText(), this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTFullIdentifier(this);
   }
}
