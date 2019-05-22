package soot.dava.toolkits.base.AST;

import soot.G;
import soot.Singletons;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.Expr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.Ref;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.UnopExpr;

public class ASTWalker {
   public ASTWalker(Singletons.Global g) {
   }

   public static ASTWalker v() {
      return G.v().soot_dava_toolkits_base_AST_ASTWalker();
   }

   public void walk_stmt(ASTAnalysis a, Stmt s) {
      if (a.getAnalysisDepth() >= 1) {
         if (s instanceof DefinitionStmt) {
            DefinitionStmt ds = (DefinitionStmt)s;
            this.walk_value(a, ds.getRightOp());
            this.walk_value(a, ds.getLeftOp());
            a.analyseDefinitionStmt(ds);
         } else if (s instanceof ReturnStmt) {
            ReturnStmt rs = (ReturnStmt)s;
            this.walk_value(a, rs.getOp());
            a.analyseReturnStmt(rs);
         } else if (s instanceof InvokeStmt) {
            InvokeStmt is = (InvokeStmt)s;
            this.walk_value(a, is.getInvokeExpr());
            a.analyseInvokeStmt(is);
         } else if (s instanceof ThrowStmt) {
            ThrowStmt ts = (ThrowStmt)s;
            this.walk_value(a, ts.getOp());
            a.analyseThrowStmt(ts);
         } else {
            a.analyseStmt(s);
         }

      }
   }

   public void walk_value(ASTAnalysis a, Value v) {
      if (a.getAnalysisDepth() >= 2) {
         if (v instanceof Expr) {
            Expr e = (Expr)v;
            if (e instanceof BinopExpr) {
               BinopExpr be = (BinopExpr)e;
               this.walk_value(a, be.getOp1());
               this.walk_value(a, be.getOp2());
               a.analyseBinopExpr(be);
            } else if (e instanceof UnopExpr) {
               UnopExpr ue = (UnopExpr)e;
               this.walk_value(a, ue.getOp());
               a.analyseUnopExpr(ue);
            } else if (e instanceof CastExpr) {
               CastExpr ce = (CastExpr)e;
               this.walk_value(a, ce.getOp());
               a.analyseExpr(ce);
            } else if (e instanceof NewArrayExpr) {
               NewArrayExpr nae = (NewArrayExpr)e;
               this.walk_value(a, nae.getSize());
               a.analyseNewArrayExpr(nae);
            } else {
               int i;
               if (e instanceof NewMultiArrayExpr) {
                  NewMultiArrayExpr nmae = (NewMultiArrayExpr)e;

                  for(i = 0; i < nmae.getSizeCount(); ++i) {
                     this.walk_value(a, nmae.getSize(i));
                  }

                  a.analyseNewMultiArrayExpr(nmae);
               } else if (e instanceof InstanceOfExpr) {
                  InstanceOfExpr ioe = (InstanceOfExpr)e;
                  this.walk_value(a, ioe.getOp());
                  a.analyseInstanceOfExpr(ioe);
               } else if (e instanceof InvokeExpr) {
                  InvokeExpr ie = (InvokeExpr)e;

                  for(i = 0; i < ie.getArgCount(); ++i) {
                     this.walk_value(a, ie.getArg(i));
                  }

                  if (ie instanceof InstanceInvokeExpr) {
                     InstanceInvokeExpr iie = (InstanceInvokeExpr)ie;
                     this.walk_value(a, iie.getBase());
                     a.analyseInstanceInvokeExpr(iie);
                  } else {
                     a.analyseInvokeExpr(ie);
                  }
               } else {
                  a.analyseExpr(e);
               }
            }
         } else if (v instanceof Ref) {
            Ref r = (Ref)v;
            if (r instanceof ArrayRef) {
               ArrayRef ar = (ArrayRef)r;
               this.walk_value(a, ar.getBase());
               this.walk_value(a, ar.getIndex());
               a.analyseArrayRef(ar);
            } else if (r instanceof InstanceFieldRef) {
               InstanceFieldRef ifr = (InstanceFieldRef)r;
               this.walk_value(a, ifr.getBase());
               a.analyseInstanceFieldRef(ifr);
            } else {
               a.analyseRef(r);
            }
         } else {
            a.analyseValue(v);
         }

      }
   }
}
