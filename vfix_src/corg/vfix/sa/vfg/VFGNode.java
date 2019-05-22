package corg.vfix.sa.vfg;

import corg.vfix.fl.spectrum.FLSpectrum;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LengthExpr;
import soot.jimple.Stmt;
import soot.util.Chain;

public class VFGNode {
   private SootClass cls;
   private SootMethod mtd;
   private String id;
   private Stmt stmt;
   private String newClsName;
   private int lineNumber;
   private int inDegree;
   private int outDegree;
   private int congestion;
   private int stmtType;
   private Value EOne = null;
   private Value ETwo = null;
   private int type;
   private ArrayList<Value> defs;
   private ArrayList<Value> uses;
   private ArrayList<Value> srcs;
   private ArrayList<Value> sinks;
   private ArrayList<Value> trans;
   private boolean executed;
   private boolean flag;
   private ArrayList<Integer> ops = null;

   public VFGNode(SootMethod m, Stmt s) {
      this.stmt = s;
      this.mtd = m;
      this.congestion = 0;
      this.type = -1;
      this.inDegree = -1;
      this.outDegree = -1;
      this.lineNumber = -1;
      this.stmtType = -1;
      this.setDefs();
      this.setUses();
      this.setCls();
      this.setLineNumber();
      this.setExecuted();
      this.setVFGSets();
      this.flag = false;
   }

   public boolean getFlag() {
      return this.flag;
   }

   public void setNewClsName(SootMethod constructor) {
      if (constructor == null) {
         this.newClsName = null;
      } else {
         this.newClsName = constructor.getDeclaringClass().toString();
      }

   }

   public String getNewClsName() {
      return this.newClsName;
   }

   public void setFlag() {
      this.flag = true;
   }

   public void printSrcSinkTrans() {
      System.out.println("Stmt: " + this.stmt);
      System.out.println("Src: " + this.srcs);
      System.out.println("Sink: " + this.sinks);
      System.out.println("Trans: " + this.trans);
   }

   private void setVFGSets() {
      this.setSrcs();
      this.setSinks();
      this.setTrans();
   }

   private void setSrcs() {
      this.srcs = this.defs;
   }

   private void setTrans() {
      this.trans = new ArrayList();
      Value t = this.getCastOrAssignValue();
      if (t != null) {
         this.trans.add(t);
      }

   }

   public void addSinkValue(Value value) {
      if (this.sinks == null) {
         this.sinks = new ArrayList();
      }

      if (!this.sinks.contains(value)) {
         this.sinks.add(value);
      }

   }

   private void setSinks() {
      this.sinks = new ArrayList();
      Value base;
      Value base;
      if (this.stmt instanceof AssignStmt) {
         AssignStmt assign = (AssignStmt)this.stmt;
         base = assign.getRightOp();
         if (base instanceof LengthExpr) {
            LengthExpr lengthExpr = (LengthExpr)base;
            base = lengthExpr.getOp();
            if (!this.sinks.contains(base)) {
               this.sinks.add(base);
            }
         }
      }

      Iterator var7 = this.uses.iterator();

      while(var7.hasNext()) {
         Value use = (Value)var7.next();
         if (use instanceof InstanceFieldRef) {
            InstanceFieldRef ifr = (InstanceFieldRef)use;
            base = ifr.getBase();
            if (!this.sinks.contains(base)) {
               this.sinks.add(base);
            }
         }
      }

      InstanceInvokeExpr iie = this.getInstanceInvoke();
      if (iie != null) {
         base = iie.getBase();
         if (!this.sinks.contains(base)) {
            this.sinks.add(base);
         }
      }

   }

   private InstanceInvokeExpr getInstanceInvoke() {
      if (this.stmt instanceof InvokeStmt) {
         InvokeStmt iStmt = (InvokeStmt)this.stmt;
         InvokeExpr ie = iStmt.getInvokeExpr();
         if (ie instanceof InstanceInvokeExpr) {
            return (InstanceInvokeExpr)ie;
         }
      } else if (this.stmt instanceof AssignStmt) {
         AssignStmt assignStmt = (AssignStmt)this.stmt;
         Value rightValue = assignStmt.getRightOp();
         if (rightValue instanceof InstanceInvokeExpr) {
            return (InstanceInvokeExpr)rightValue;
         }
      }

      return null;
   }

   private Value getCastOrAssignValue() {
      if (this.stmt instanceof AssignStmt) {
         AssignStmt assignStmt = (AssignStmt)this.stmt;
         Value rightValue = assignStmt.getRightOp();
         if (rightValue instanceof CastExpr) {
            return ((CastExpr)rightValue).getOp();
         }

         if (rightValue instanceof InstanceFieldRef) {
            return rightValue;
         }

         if (rightValue instanceof Local) {
            return rightValue;
         }

         if (rightValue instanceof LengthExpr) {
            return ((LengthExpr)rightValue).getOp();
         }
      }

      return null;
   }

   public ArrayList<Value> getSrcs() {
      return this.srcs;
   }

   public ArrayList<Value> getSinks() {
      return this.sinks;
   }

   public ArrayList<Value> getTrans() {
      return this.trans;
   }

   public ArrayList<Integer> getOperations() {
      return this.ops;
   }

   public void setOperations(ArrayList<Integer> o) {
      this.ops = o;
   }

   public String getID() {
      return this.id;
   }

   public void setCongestion(int con) {
      this.congestion = con;
   }

   public int getCongestion() {
      return this.congestion;
   }

   public void addCongestion() {
      ++this.congestion;
   }

   public void setID(String i) {
      this.id = i;
   }

   public int getStmtType() {
      return this.stmtType;
   }

   public void setStmtType(int t) {
      this.stmtType = t;
   }

   public void setEOne(Value v) {
      this.EOne = v;
   }

   public void setETwo(Value v) {
      this.ETwo = v;
   }

   public Value getEOne() {
      return this.EOne;
   }

   public Value getETwo() {
      return this.ETwo;
   }

   public int getInDegree() {
      return this.inDegree;
   }

   public void setInDegree(int in) {
      this.inDegree = in;
   }

   public int getOutDegree() {
      return this.outDegree;
   }

   public void setOutDegree(int out) {
      this.outDegree = out;
   }

   private void setLineNumber() {
      this.lineNumber = this.stmt.getJavaSourceStartLineNumber();
   }

   private void setCls() {
      this.cls = this.mtd.getDeclaringClass();
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public boolean isExecuted() {
      return this.executed;
   }

   public Stmt getStmt() {
      return this.stmt;
   }

   public SootMethod getMethod() {
      return this.mtd;
   }

   public void setNodeType(int t) {
      this.type = t;
   }

   public int getNodeType() {
      return this.type;
   }

   private void setDefs() {
      ArrayList<Value> values = new ArrayList();
      List<ValueBox> valueBoxes = this.stmt.getDefBoxes();
      Iterator var4 = valueBoxes.iterator();

      while(var4.hasNext()) {
         ValueBox valuebox = (ValueBox)var4.next();
         values.add(valuebox.getValue());
      }

      this.defs = values;
   }

   private void setUses() {
      ArrayList<Value> values = new ArrayList();
      List<ValueBox> valueBoxes = this.stmt.getUseBoxes();
      Iterator var4 = valueBoxes.iterator();

      while(var4.hasNext()) {
         ValueBox valuebox = (ValueBox)var4.next();
         values.add(valuebox.getValue());
      }

      this.uses = values;
   }

   public void setDefs(ArrayList<Value> s) {
      this.defs = s;
   }

   public void setUses(ArrayList<Value> s) {
      this.uses = s;
   }

   public ArrayList<Value> getDefs() {
      return this.defs;
   }

   public ArrayList<Value> getUses() {
      return this.uses;
   }

   private void setExecuted() {
      this.executed = FLSpectrum.query(this);
   }

   public String getClsName() {
      return this.cls.getName();
   }

   public String toString() {
      return this.stmt.toString();
   }

   public Chain<Unit> getUnits() {
      return this.mtd.getActiveBody().getUnits();
   }

   public Body getBody() {
      return this.mtd.getActiveBody();
   }

   public boolean equals(VFGNode node) {
      return this.stmt.equals(node.getStmt());
   }
}
