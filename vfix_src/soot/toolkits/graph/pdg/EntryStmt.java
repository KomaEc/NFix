package soot.toolkits.graph.pdg;

import soot.jimple.internal.JNopStmt;

class EntryStmt extends JNopStmt {
   public EntryStmt() {
   }

   public Object clone() {
      return new EntryStmt();
   }

   public boolean fallsThrough() {
      return true;
   }

   public boolean branches() {
      return false;
   }
}
