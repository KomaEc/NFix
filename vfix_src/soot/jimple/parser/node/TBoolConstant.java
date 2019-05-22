package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TBoolConstant extends Token {
   public TBoolConstant(String text) {
      this.setText(text);
   }

   public TBoolConstant(String text, int line, int pos) {
      this.setText(text);
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TBoolConstant(this.getText(), this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTBoolConstant(this);
   }
}
