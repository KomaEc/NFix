package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TStringConstant extends Token {
   public TStringConstant(String text) {
      this.setText(text);
   }

   public TStringConstant(String text, int line, int pos) {
      this.setText(text);
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TStringConstant(this.getText(), this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTStringConstant(this);
   }
}
