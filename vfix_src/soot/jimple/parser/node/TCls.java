package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TCls extends Token {
   public TCls() {
      super.setText("cls");
   }

   public TCls(int line, int pos) {
      super.setText("cls");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TCls(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTCls(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TCls text.");
   }
}
