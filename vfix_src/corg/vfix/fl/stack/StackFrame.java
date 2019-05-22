package corg.vfix.fl.stack;

import soot.SootClass;
import soot.SootMethod;
import soot.Value;
import soot.jimple.Stmt;

public class StackFrame {
   private int lineNumber = -1;
   private String clsName;
   private String mtdName;
   private StackFrame pre = null;
   private StackFrame next = null;
   private Value obj = null;
   private Stmt stmt = null;
   private SootMethod sMtd = null;
   private SootClass sCls = null;

   public StackFrame(String mName, int number) {
      this.parseName(mName);
      this.lineNumber = number;
   }

   private void parseName(String name) {
      int i = name.lastIndexOf(".");
      this.clsName = name.substring(0, i);
      this.mtdName = name.substring(i + 1);
   }

   public String toString() {
      return this.clsName + ":" + this.mtdName + ": " + this.lineNumber;
   }

   public void setPre(StackFrame frame) {
      this.pre = frame;
   }

   public void setNext(StackFrame frame) {
      this.next = frame;
   }

   public void setCls(SootClass cls) {
      this.sCls = cls;
   }

   public void setMtd(SootMethod mtd) {
      this.sMtd = mtd;
   }

   public void setStmt(Stmt s) {
      this.stmt = s;
   }

   public void setObj(Value o) {
      this.obj = o;
   }

   public Stmt getStmt() {
      return this.stmt;
   }

   public StackFrame getNext() {
      return this.next;
   }

   public StackFrame getPre() {
      return this.pre;
   }

   public String getMtdName() {
      return this.mtdName;
   }

   public SootMethod getMtd() {
      return this.sMtd;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public String getClsName() {
      return this.clsName;
   }

   public SootClass getCls() {
      return this.sCls;
   }

   public Value getObj() {
      return this.obj;
   }
}
