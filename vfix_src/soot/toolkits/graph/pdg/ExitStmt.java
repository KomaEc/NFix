package soot.toolkits.graph.pdg;

import soot.jimple.internal.JNopStmt;

class ExitStmt extends JNopStmt {
   public ExitStmt() {
   }

   public Object clone() {
      return new ExitStmt();
   }

   public boolean fallsThrough() {
      return true;
   }

   public boolean branches() {
      return false;
   }
}
