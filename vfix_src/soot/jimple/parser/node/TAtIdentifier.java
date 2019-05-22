package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TAtIdentifier extends Token {
   public TAtIdentifier(String text) {
      this.setText(text);
   }

   public TAtIdentifier(String text, int line, int pos) {
      this.setText(text);
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TAtIdentifier(this.getText(), this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTAtIdentifier(this);
   }
}
