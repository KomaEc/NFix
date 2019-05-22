package soot.jimple.toolkits.base;

import soot.SootClass;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.tagkit.SourceLnPosTag;

public class ExceptionCheckerError extends Exception {
   private SootMethod method;
   private SootClass excType;
   private Stmt throwing;
   private SourceLnPosTag position;

   public ExceptionCheckerError(SootMethod m, SootClass sc, Stmt s, SourceLnPosTag pos) {
      this.method(m);
      this.excType(sc);
      this.throwing(s);
      this.position(pos);
   }

   public SootMethod method() {
      return this.method;
   }

   public void method(SootMethod sm) {
      this.method = sm;
   }

   public SootClass excType() {
      return this.excType;
   }

   public void excType(SootClass sc) {
      this.excType = sc;
   }

   public Stmt throwing() {
      return this.throwing;
   }

   public void throwing(Stmt s) {
      this.throwing = s;
   }

   public SourceLnPosTag position() {
      return this.position;
   }

   public void position(SourceLnPosTag pos) {
      this.position = pos;
   }
}
