package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TQuotedName extends Token {
   public TQuotedName(String text) {
      this.setText(text);
   }

   public TQuotedName(String text, int line, int pos) {
      this.setText(text);
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TQuotedName(this.getText(), this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTQuotedName(this);
   }
}
