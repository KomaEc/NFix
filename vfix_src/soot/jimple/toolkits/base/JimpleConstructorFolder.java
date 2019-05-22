package soot.jimple.toolkits.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.util.Chain;
import soot.util.HashMultiMap;
import soot.util.MultiMap;

public class JimpleConstructorFolder extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(JimpleConstructorFolder.class);

   static boolean isNew(Stmt s) {
      if (!(s instanceof AssignStmt)) {
         return false;
      } else {
         return rhs(s) instanceof NewExpr;
      }
   }

   static boolean isConstructor(Stmt s) {
      if (!(s instanceof InvokeStmt)) {
         return false;
      } else {
         InvokeStmt is = (InvokeStmt)s;
         InvokeExpr expr = is.getInvokeExpr();
         if (!(expr instanceof SpecialInvokeExpr)) {
            return false;
         } else {
            SpecialInvokeExpr sie = (SpecialInvokeExpr)expr;
            return sie.getMethodRef().name().equals("<init>");
         }
      }
   }

   static Local base(Stmt s) {
      InvokeStmt is = (InvokeStmt)s;
      InstanceInvokeExpr expr = (InstanceInvokeExpr)is.getInvokeExpr();
      return (Local)expr.getBase();
   }

   static void setBase(Stmt s, Local l) {
      InvokeStmt is = (InvokeStmt)s;
      InstanceInvokeExpr expr = (InstanceInvokeExpr)is.getInvokeExpr();
      expr.getBaseBox().setValue(l);
   }

   static boolean isCopy(Stmt s) {
      if (!(s instanceof AssignStmt)) {
         return false;
      } else if (!(rhs(s) instanceof Local)) {
         return false;
      } else {
         return lhs(s) instanceof Local;
      }
   }

   static Value rhs(Stmt s) {
      AssignStmt as = (AssignStmt)s;
      return as.getRightOp();
   }

   static Value lhs(Stmt s) {
      AssignStmt as = (AssignStmt)s;
      return as.getLeftOp();
   }

   static Local rhsLocal(Stmt s) {
      return (Local)rhs(s);
   }

   static Local lhsLocal(Stmt s) {
      return (Local)lhs(s);
   }

   public void internalTransform(Body b, String phaseName, Map<String, String> options) {
      JimpleBody body = (JimpleBody)b;
      if (Options.v().verbose()) {
         logger.debug("[" + body.getMethod().getName() + "] Folding Jimple constructors...");
      }

      JimpleConstructorFolder.Analysis analysis = new JimpleConstructorFolder.Analysis(new BriefUnitGraph(body));
      Chain<Unit> units = body.getUnits();
      List<Unit> stmtList = new ArrayList();
      stmtList.addAll(units);
      Iterator var8 = stmtList.iterator();

      while(true) {
         Stmt s;
         JimpleConstructorFolder.Fact before;
         Local l;
         do {
            do {
               Unit u;
               if (!var8.hasNext()) {
                  var8 = stmtList.iterator();

                  while(var8.hasNext()) {
                     u = (Unit)var8.next();
                     s = (Stmt)u;
                     if (isNew(s)) {
                        units.remove(s);
                     }
                  }

                  var8 = stmtList.iterator();

                  while(true) {
                     while(var8.hasNext()) {
                        u = (Unit)var8.next();
                        s = (Stmt)u;
                        before = (JimpleConstructorFolder.Fact)analysis.getFlowBefore(s);
                        JimpleConstructorFolder.Fact after = (JimpleConstructorFolder.Fact)analysis.getFlowAfter(s);
                        Stmt newStmt;
                        if (isCopy(s)) {
                           newStmt = before.get(rhsLocal(s));
                           if (newStmt != null) {
                              units.remove(s);
                           }
                        } else if (after.alloc() != null) {
                           newStmt = before.get(base(s));
                           setBase(s, lhsLocal(newStmt));
                           units.insertBefore((Object)newStmt, s);
                           Iterator var18 = before.get(newStmt).iterator();

                           while(var18.hasNext()) {
                              l = (Local)var18.next();
                              if (!l.equals(base(s))) {
                                 units.insertAfter((Object)Jimple.v().newAssignStmt(l, base(s)), s);
                              }
                           }
                        }
                     }

                     return;
                  }
               }

               u = (Unit)var8.next();
               s = (Stmt)u;
            } while(isCopy(s));
         } while(isConstructor(s));

         before = (JimpleConstructorFolder.Fact)analysis.getFlowBefore(s);
         Iterator var12 = s.getUseBoxes().iterator();

         while(var12.hasNext()) {
            ValueBox usebox = (ValueBox)var12.next();
            Value value = usebox.getValue();
            if (value instanceof Local) {
               l = (Local)value;
               if (before.get(l) != null) {
                  throw new RuntimeException("Use of an unitialized value before constructor call; are you sure this bytecode is verifiable?\n" + s);
               }
            }
         }
      }
   }

   private class Analysis extends ForwardFlowAnalysis<Unit, JimpleConstructorFolder.Fact> {
      public Analysis(DirectedGraph<Unit> graph) {
         super(graph);
         this.doAnalysis();
      }

      protected JimpleConstructorFolder.Fact newInitialFlow() {
         return JimpleConstructorFolder.this.new Fact();
      }

      public void flowThrough(JimpleConstructorFolder.Fact in, Unit u, JimpleConstructorFolder.Fact out) {
         Stmt s = (Stmt)u;
         this.copy(in, out);
         out.setAlloc((Stmt)null);
         if (JimpleConstructorFolder.isNew(s)) {
            out.add(JimpleConstructorFolder.lhsLocal(s), s);
         } else {
            Stmt newStmt;
            if (JimpleConstructorFolder.isCopy(s)) {
               newStmt = out.get(JimpleConstructorFolder.rhsLocal(s));
               if (newStmt != null) {
                  out.add(JimpleConstructorFolder.lhsLocal(s), newStmt);
               }
            } else if (JimpleConstructorFolder.isConstructor(s)) {
               newStmt = out.get(JimpleConstructorFolder.base(s));
               if (newStmt != null) {
                  out.removeAll(newStmt);
                  out.setAlloc(newStmt);
               }
            }
         }

      }

      public void copy(JimpleConstructorFolder.Fact source, JimpleConstructorFolder.Fact dest) {
         dest.copyFrom(source);
      }

      public void merge(JimpleConstructorFolder.Fact in1, JimpleConstructorFolder.Fact in2, JimpleConstructorFolder.Fact out) {
         out.mergeFrom(in1, in2);
      }
   }

   private class Fact {
      private Map<Local, Stmt> varToStmt;
      private MultiMap<Stmt, Local> stmtToVar;
      private Stmt alloc;

      private Fact() {
         this.varToStmt = new HashMap();
         this.stmtToVar = new HashMultiMap();
         this.alloc = null;
      }

      public void add(Local l, Stmt s) {
         this.varToStmt.put(l, s);
         this.stmtToVar.put(s, l);
      }

      public Stmt get(Local l) {
         return (Stmt)this.varToStmt.get(l);
      }

      public Set<Local> get(Stmt s) {
         return this.stmtToVar.get(s);
      }

      public void removeAll(Stmt s) {
         Iterator var2 = this.stmtToVar.get(s).iterator();

         while(var2.hasNext()) {
            Local var = (Local)var2.next();
            this.varToStmt.remove(var);
         }

         this.stmtToVar.remove(s);
      }

      public void copyFrom(JimpleConstructorFolder.Fact in) {
         this.varToStmt = new HashMap(in.varToStmt);
         this.stmtToVar = new HashMultiMap(in.stmtToVar);
         this.alloc = in.alloc;
      }

      public void mergeFrom(JimpleConstructorFolder.Fact in1, JimpleConstructorFolder.Fact in2) {
         this.varToStmt = new HashMap();

         Iterator var3;
         Entry e;
         Local l;
         Stmt newStmt;
         for(var3 = in1.varToStmt.entrySet().iterator(); var3.hasNext(); this.add(l, newStmt)) {
            e = (Entry)var3.next();
            l = (Local)e.getKey();
            newStmt = (Stmt)e.getValue();
            if (in2.varToStmt.containsKey(l)) {
               Stmt newStmt2 = (Stmt)in2.varToStmt.get(l);
               if (!newStmt.equals(newStmt2)) {
                  throw new RuntimeException("Merge of different uninitialized values; are you sure this bytecode is verifiable?");
               }
            }
         }

         var3 = in2.varToStmt.entrySet().iterator();

         while(var3.hasNext()) {
            e = (Entry)var3.next();
            l = (Local)e.getKey();
            newStmt = (Stmt)e.getValue();
            this.add(l, newStmt);
         }

         if (in1.alloc != null && in1.alloc.equals(in2.alloc)) {
            this.alloc = in1.alloc;
         } else {
            this.alloc = null;
         }

      }

      public boolean equals(Object other) {
         if (!(other instanceof JimpleConstructorFolder.Fact)) {
            return false;
         } else {
            JimpleConstructorFolder.Fact o = (JimpleConstructorFolder.Fact)other;
            if (!this.stmtToVar.equals(o.stmtToVar)) {
               return false;
            } else if (this.alloc == null && o.alloc != null) {
               return false;
            } else if (this.alloc != null && o.alloc == null) {
               return false;
            } else {
               return this.alloc == null || this.alloc.equals(o.alloc);
            }
         }
      }

      public Stmt alloc() {
         return this.alloc;
      }

      public void setAlloc(Stmt newAlloc) {
         this.alloc = newAlloc;
      }

      // $FF: synthetic method
      Fact(Object x1) {
         this();
      }
   }
}
