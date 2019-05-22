package soot.jimple.toolkits.annotation.purity;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.RefLikeType;
import soot.SourceLocator;
import soot.Unit;
import soot.Value;
import soot.jimple.AnyNewExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.BreakpointStmt;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.Constant;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceOfExpr;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.MonitorStmt;
import soot.jimple.NopStmt;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.jimple.UnopExpr;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.util.dot.DotGraph;
import soot.util.dot.DotGraphEdge;
import soot.util.dot.DotGraphNode;

public class PurityIntraproceduralAnalysis extends ForwardFlowAnalysis<Unit, PurityGraphBox> {
   private static final Logger logger = LoggerFactory.getLogger(PurityIntraproceduralAnalysis.class);
   AbstractInterproceduralAnalysis<PurityGraphBox> inter;

   protected PurityGraphBox newInitialFlow() {
      return new PurityGraphBox();
   }

   protected PurityGraphBox entryInitialFlow() {
      return new PurityGraphBox();
   }

   protected void merge(PurityGraphBox in1, PurityGraphBox in2, PurityGraphBox out) {
      if (out != in1) {
         out.g = new PurityGraph(in1.g);
      }

      out.g.union(in2.g);
   }

   protected void copy(PurityGraphBox source, PurityGraphBox dest) {
      dest.g = new PurityGraph(source.g);
   }

   protected void flowThrough(PurityGraphBox inValue, Unit unit, PurityGraphBox outValue) {
      Stmt stmt = (Stmt)unit;
      outValue.g = new PurityGraph(inValue.g);
      if (stmt.containsInvokeExpr()) {
         this.inter.analyseCall(inValue, stmt, outValue);
      } else {
         Value v;
         Value rightOp;
         if (stmt instanceof AssignStmt) {
            v = ((AssignStmt)stmt).getLeftOp();
            rightOp = ((AssignStmt)stmt).getRightOp();
            Local left;
            Local right;
            if (v instanceof Local) {
               left = (Local)v;
               if (rightOp instanceof CastExpr) {
                  rightOp = ((CastExpr)rightOp).getOp();
               }

               if (left.getType() instanceof RefLikeType) {
                  if (rightOp instanceof Local) {
                     right = (Local)rightOp;
                     outValue.g.assignLocalToLocal(right, left);
                  } else if (rightOp instanceof ArrayRef) {
                     right = (Local)((ArrayRef)rightOp).getBase();
                     outValue.g.assignFieldToLocal(stmt, right, "[]", left);
                  } else if (rightOp instanceof InstanceFieldRef) {
                     right = (Local)((InstanceFieldRef)rightOp).getBase();
                     String field = ((InstanceFieldRef)rightOp).getField().getName();
                     outValue.g.assignFieldToLocal(stmt, right, field, left);
                  } else if (rightOp instanceof StaticFieldRef) {
                     outValue.g.localIsUnknown(left);
                  } else if (!(rightOp instanceof Constant)) {
                     if (rightOp instanceof AnyNewExpr) {
                        outValue.g.assignNewToLocal(stmt, left);
                     } else if (!(rightOp instanceof BinopExpr) && !(rightOp instanceof UnopExpr) && !(rightOp instanceof InstanceOfExpr)) {
                        throw new Error("AssignStmt match failure (rightOp)" + stmt);
                     }
                  }
               }
            } else if (v instanceof ArrayRef) {
               left = (Local)((ArrayRef)v).getBase();
               if (rightOp instanceof Local) {
                  right = (Local)rightOp;
                  if (right.getType() instanceof RefLikeType) {
                     outValue.g.assignLocalToField(right, left, "[]");
                  } else {
                     outValue.g.mutateField(left, "[]");
                  }
               } else {
                  if (!(rightOp instanceof Constant)) {
                     throw new Error("AssignStmt match failure (rightOp)" + stmt);
                  }

                  outValue.g.mutateField(left, "[]");
               }
            } else if (v instanceof InstanceFieldRef) {
               left = (Local)((InstanceFieldRef)v).getBase();
               String field = ((InstanceFieldRef)v).getField().getName();
               if (rightOp instanceof Local) {
                  Local right = (Local)rightOp;
                  if (right.getType() instanceof RefLikeType) {
                     outValue.g.assignLocalToField(right, left, field);
                  } else {
                     outValue.g.mutateField(left, field);
                  }
               } else {
                  if (!(rightOp instanceof Constant)) {
                     throw new Error("AssignStmt match failure (rightOp) " + stmt);
                  }

                  outValue.g.mutateField(left, field);
               }
            } else {
               if (!(v instanceof StaticFieldRef)) {
                  throw new Error("AssignStmt match failure (leftOp) " + stmt);
               }

               String field = ((StaticFieldRef)v).getField().getName();
               if (rightOp instanceof Local) {
                  right = (Local)rightOp;
                  if (right.getType() instanceof RefLikeType) {
                     outValue.g.assignLocalToStaticField(right, field);
                  } else {
                     outValue.g.mutateStaticField(field);
                  }
               } else {
                  if (!(rightOp instanceof Constant)) {
                     throw new Error("AssignStmt match failure (rightOp) " + stmt);
                  }

                  outValue.g.mutateStaticField(field);
               }
            }
         } else if (stmt instanceof IdentityStmt) {
            Local left = (Local)((IdentityStmt)stmt).getLeftOp();
            rightOp = ((IdentityStmt)stmt).getRightOp();
            if (rightOp instanceof ThisRef) {
               outValue.g.assignThisToLocal(left);
            } else if (rightOp instanceof ParameterRef) {
               ParameterRef p = (ParameterRef)rightOp;
               if (p.getType() instanceof RefLikeType) {
                  outValue.g.assignParamToLocal(p.getIndex(), left);
               }
            } else {
               if (!(rightOp instanceof CaughtExceptionRef)) {
                  throw new Error("IdentityStmt match failure (rightOp) " + stmt);
               }

               outValue.g.localIsUnknown(left);
            }
         } else if (stmt instanceof ThrowStmt) {
            v = ((ThrowStmt)stmt).getOp();
            if (v instanceof Local) {
               Local v = (Local)v;
               outValue.g.localEscapes(v);
            } else if (!(v instanceof Constant)) {
               throw new Error("ThrowStmt match failure " + stmt);
            }
         } else if (!(stmt instanceof ReturnVoidStmt)) {
            if (stmt instanceof ReturnStmt) {
               v = ((ReturnStmt)stmt).getOp();
               if (v instanceof Local) {
                  if (v.getType() instanceof RefLikeType) {
                     outValue.g.returnLocal((Local)v);
                  }
               } else if (!(v instanceof Constant)) {
                  throw new Error("ReturnStmt match failure " + stmt);
               }
            } else if (!(stmt instanceof IfStmt) && !(stmt instanceof GotoStmt) && !(stmt instanceof LookupSwitchStmt) && !(stmt instanceof TableSwitchStmt) && !(stmt instanceof MonitorStmt) && !(stmt instanceof BreakpointStmt) && !(stmt instanceof NopStmt)) {
               throw new Error("Stmt match faliure " + stmt);
            }
         }
      }

   }

   public void drawAsOneDot(String prefix, String name) {
      DotGraph dot = new DotGraph(name);
      dot.setGraphLabel(name);
      dot.setGraphAttribute("compound", "true");
      dot.setGraphAttribute("rankdir", "LR");
      Map<Unit, Integer> node = new HashMap();
      int id = 0;

      Iterator var6;
      Unit src;
      for(var6 = this.graph.iterator(); var6.hasNext(); ++id) {
         src = (Unit)var6.next();
         PurityGraphBox ref = (PurityGraphBox)this.getFlowAfter(src);
         DotGraph sub = dot.createSubGraph("cluster" + id);
         DotGraphNode label = sub.drawNode("head" + id);
         String lbl = src.toString();
         if (lbl.startsWith("lookupswitch")) {
            lbl = "lookupswitch...";
         }

         if (lbl.startsWith("tableswitch")) {
            lbl = "tableswitch...";
         }

         sub.setGraphLabel(" ");
         label.setLabel(lbl);
         label.setAttribute("fontsize", "18");
         label.setShape("box");
         ref.g.fillDotGraph("X" + id, sub);
         node.put(src, id);
      }

      var6 = this.graph.iterator();

      while(var6.hasNext()) {
         src = (Unit)var6.next();
         Iterator var13 = this.graph.getSuccsOf(src).iterator();

         while(var13.hasNext()) {
            Unit dst = (Unit)var13.next();
            DotGraphEdge edge = dot.drawEdge("head" + node.get(src), "head" + node.get(dst));
            edge.setAttribute("ltail", "cluster" + node.get(src));
            edge.setAttribute("lhead", "cluster" + node.get(dst));
         }
      }

      File f = new File(SourceLocator.v().getOutputDir(), prefix + name + ".dot");
      dot.plot(f.getPath());
   }

   public void copyResult(PurityGraphBox dst) {
      PurityGraph r = new PurityGraph();
      Iterator var3 = this.graph.getTails().iterator();

      while(var3.hasNext()) {
         Unit u = (Unit)var3.next();
         r.union(((PurityGraphBox)this.getFlowAfter(u)).g);
      }

      r.removeLocals();
      dst.g = r;
   }

   PurityIntraproceduralAnalysis(UnitGraph g, AbstractInterproceduralAnalysis<PurityGraphBox> inter) {
      super(g);
      this.inter = inter;
      this.doAnalysis();
   }
}
