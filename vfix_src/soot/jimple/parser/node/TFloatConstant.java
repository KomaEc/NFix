package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TFloatConstant extends Token {
   public TFloatConstant(String text) {
      this.setText(text);
   }

   public TFloatConstant(String text, int line, int pos) {
      this.setText(text);
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TFloatConstant(this.getText(), this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTFloatConstant(this);
   }
}
