package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TIntegerConstant extends Token {
   public TIntegerConstant(String text) {
      this.setText(text);
   }

   public TIntegerConstant(String text, int line, int pos) {
      this.setText(text);
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TIntegerConstant(this.getText(), this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTIntegerConstant(this);
   }
}
