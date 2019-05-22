package corg.vfix.fl.stack;

import soot.SootClass;
import soot.SootMethod;
import soot.Value;
import soot.jimple.Stmt;

public class StackTrace {
   private static int size;
   private static StackFrame head;
   private static StackFrame tail;

   public static void clearAll() {
      size = 0;
      head = null;
      tail = null;
   }

   public static StackFrame getHead() {
      return head;
   }

   public static StackFrame getTail() {
      return tail;
   }

   public static void insert(StackFrame frame) {
      if (size == 0) {
         frame.setNext((StackFrame)null);
         frame.setPre((StackFrame)null);
         tail = frame;
         head = frame;
      } else {
         frame.setNext(head);
         frame.setPre((StackFrame)null);
         head.setPre(frame);
         head = frame;
      }

      ++size;
   }

   public static String getTailClsName() {
      return tail == null ? "null" : tail.getClsName();
   }

   public static String getTailMtdName() {
      return tail == null ? "null" : tail.getMtdName();
   }

   public static int getTailLineNumber() {
      return tail == null ? -1 : tail.getLineNumber();
   }

   public static StackFrame getNext(StackFrame frame) {
      return frame.getNext();
   }

   public static StackFrame getPre(StackFrame frame) {
      return frame.getPre();
   }

   public static void forwardPrint() {
      System.out.println("Stack (forward):");

      for(StackFrame pointer = head; pointer != null; pointer = pointer.getNext()) {
         System.out.println(pointer);
      }

   }

   public static void backwardPrint() {
      System.out.println("Stack (backward):");

      for(StackFrame pointer = tail; pointer != null; pointer = pointer.getPre()) {
         System.out.println(pointer);
      }

   }

   public static void setNullPointer(Value obj) {
      tail.setObj(obj);
   }

   public static Value getNullPointer() {
      return tail.getObj();
   }

   public static void setNullStmt(Stmt stmt) {
      tail.setStmt(stmt);
   }

   public static Stmt getNullStmt() {
      return tail.getStmt();
   }

   public static void setNullMtd(SootMethod mtd) {
      tail.setMtd(mtd);
   }

   public static SootMethod getNullMtd() {
      return tail.getMtd();
   }

   public static void setNullCls(SootClass cls) {
      tail.setCls(cls);
   }

   public static SootClass getNullCls() {
      return tail.getCls();
   }
}
