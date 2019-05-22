package soot.jimple.toolkits.thread.synchronization;

import java.util.ArrayList;
import java.util.List;
import soot.jimple.Stmt;
import soot.toolkits.scalar.Pair;

public class SynchronizedRegion {
   public Stmt prepStmt;
   public Stmt entermonitor;
   public Stmt beginning;
   public List<Pair<Stmt, Stmt>> earlyEnds;
   public Pair<Stmt, Stmt> exceptionalEnd;
   public Pair<Stmt, Stmt> end;
   public Stmt last;
   public Stmt after;

   public SynchronizedRegion() {
      this.prepStmt = null;
      this.entermonitor = null;
      this.beginning = null;
      this.earlyEnds = new ArrayList();
      this.exceptionalEnd = null;
      this.end = null;
      this.last = null;
      this.after = null;
   }

   public SynchronizedRegion(SynchronizedRegion sr) {
      this.prepStmt = sr.prepStmt;
      this.entermonitor = sr.entermonitor;
      this.beginning = sr.beginning;
      this.earlyEnds = new ArrayList();
      this.earlyEnds.addAll(sr.earlyEnds);
      this.exceptionalEnd = null;
      this.end = sr.end;
      this.last = sr.last;
      this.after = sr.after;
   }

   protected Object clone() {
      return new SynchronizedRegion(this);
   }
}
