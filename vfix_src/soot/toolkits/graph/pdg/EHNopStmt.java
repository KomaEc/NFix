package soot.toolkits.graph.pdg;

import soot.jimple.internal.JNopStmt;

class EHNopStmt extends JNopStmt {
   public EHNopStmt() {
   }

   public Object clone() {
      return new EHNopStmt();
   }

   public boolean fallsThrough() {
      return true;
   }

   public boolean branches() {
      return false;
   }
}
