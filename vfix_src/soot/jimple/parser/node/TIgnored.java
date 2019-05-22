package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TIgnored extends Token {
   public TIgnored(String text) {
      this.setText(text);
   }

   public TIgnored(String text, int line, int pos) {
      this.setText(text);
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TIgnored(this.getText(), this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTIgnored(this);
   }
}
