package soot.jimple.toolkits.annotation.purity;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Local;
import soot.RefLikeType;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.PurityOptions;
import soot.tagkit.GenericAttribute;
import soot.tagkit.StringTag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.util.dot.DotGraph;

public class PurityInterproceduralAnalysis extends AbstractInterproceduralAnalysis<PurityGraphBox> {
   private static final Logger logger = LoggerFactory.getLogger(PurityInterproceduralAnalysis.class);
   private static final String[][] pureMethods = new String[][]{{"java.lang.", "valueOf"}, {"java.", "equals"}, {"javax.", "equals"}, {"sun.", "equals"}, {"java.", "compare"}, {"javax.", "compare"}, {"sun.", "compare"}, {"java.", "getClass"}, {"javax.", "getClass"}, {"sun.", "getClass"}, {"java.", "hashCode"}, {"javax.", "hashCode"}, {"sun.", "hashCode"}, {"java.", "toString"}, {"javax.", "toString"}, {"sun.", "toString"}, {"java.", "valueOf"}, {"javax.", "valueOf"}, {"sun.", "valueOf"}, {"java.", "compareTo"}, {"javax.", "compareTo"}, {"sun.", "compareTo"}, {"java.lang.System", "identityHashCode"}, {"java.", "<clinit>"}, {"javax.", "<clinit>"}, {"sun.", "<clinit>"}, {"java.lang.Math", "abs"}, {"java.lang.Math", "acos"}, {"java.lang.Math", "asin"}, {"java.lang.Math", "atan"}, {"java.lang.Math", "atan2"}, {"java.lang.Math", "ceil"}, {"java.lang.Math", "cos"}, {"java.lang.Math", "exp"}, {"java.lang.Math", "floor"}, {"java.lang.Math", "IEEEremainder"}, {"java.lang.Math", "log"}, {"java.lang.Math", "max"}, {"java.lang.Math", "min"}, {"java.lang.Math", "pow"}, {"java.lang.Math", "rint"}, {"java.lang.Math", "round"}, {"java.lang.Math", "sin"}, {"java.lang.Math", "sqrt"}, {"java.lang.Math", "tan"}, {"java.lang.Throwable", "<init>"}, {"java.lang.StringIndexOutOfBoundsException", "<init>"}};
   private static final String[][] impureMethods = new String[][]{{"java.io.", "<init>"}, {"java.io.", "close"}, {"java.io.", "read"}, {"java.io.", "write"}, {"java.io.", "flush"}, {"java.io.", "flushBuffer"}, {"java.io.", "print"}, {"java.io.", "println"}, {"java.lang.Runtime", "exit"}};
   private static final String[][] alterMethods = new String[][]{{"java.lang.System", "arraycopy"}, {"java.lang.FloatingDecimal", "dtoa"}, {"java.lang.FloatingDecimal", "developLongDigits"}, {"java.lang.FloatingDecimal", "big5pow"}, {"java.lang.FloatingDecimal", "getChars"}, {"java.lang.FloatingDecimal", "roundup"}};

   PurityInterproceduralAnalysis(CallGraph cg, Iterator<SootMethod> heads, PurityOptions opts) {
      super(cg, new PurityInterproceduralAnalysis.Filter(), heads, opts.dump_cg());
      if (opts.dump_cg()) {
         logger.debug("[AM] Dumping empty .dot call-graph");
         this.drawAsOneDot("EmptyCallGraph");
      }

      Date start = new Date();
      logger.debug("[AM] Analysis began");
      this.doAnalysis(opts.verbose());
      logger.debug("[AM] Analysis finished");
      Date finish = new Date();
      long runtime = finish.getTime() - start.getTime();
      logger.debug("[AM] run time: " + (double)runtime / 1000.0D + " s");
      if (opts.dump_cg()) {
         logger.debug("[AM] Dumping annotated .dot call-graph");
         this.drawAsOneDot("CallGraph");
      }

      if (opts.dump_summaries()) {
         logger.debug("[AM] Dumping .dot summaries of analysed methods");
         this.drawAsManyDot("Summary_", false);
      }

      Iterator it;
      SootMethod m;
      if (opts.dump_intra()) {
         logger.debug("[AM] Dumping .dot full intra-procedural method analyses");
         it = this.getAnalysedMethods();

         while(it.hasNext()) {
            m = (SootMethod)it.next();
            Body body = m.retrieveActiveBody();
            ExceptionalUnitGraph graph = new ExceptionalUnitGraph(body);
            if (opts.verbose()) {
               logger.debug("  |- " + m);
            }

            PurityIntraproceduralAnalysis r = new PurityIntraproceduralAnalysis(graph, this);
            r.drawAsOneDot("Intra_", m.toString());
            PurityGraphBox b = new PurityGraphBox();
            r.copyResult(b);
         }
      }

      logger.debug("[AM] Annotate methods. ");
      it = this.getAnalysedMethods();

      while(it.hasNext()) {
         m = (SootMethod)it.next();
         PurityGraphBox b = (PurityGraphBox)this.getSummaryFor(m);
         boolean isPure = m.toString().contains("<init>") ? b.g.isPureConstructor() : b.g.isPure();
         m.addTag(new StringTag("purity: " + (isPure ? "pure" : "impure")));
         if (isPure && opts.annotate()) {
            m.addTag(new GenericAttribute("Pure", new byte[0]));
         }

         if (opts.print()) {
            logger.debug("  |- method " + m.toString() + " is " + (isPure ? "pure" : "impure"));
         }

         if (!m.isStatic()) {
            String s;
            switch(b.g.thisStatus()) {
            case 0:
               s = "read/write";
               break;
            case 1:
               s = "read-only";
               break;
            case 2:
               s = "Safe";
               break;
            default:
               s = "unknown";
            }

            m.addTag(new StringTag("this: " + s));
            if (opts.print()) {
               logger.debug("  |   |- this is " + s);
            }
         }

         int i = 0;

         for(Iterator var19 = m.getParameterTypes().iterator(); var19.hasNext(); ++i) {
            Type t = (Type)var19.next();
            if (t instanceof RefLikeType) {
               String s;
               switch(b.g.paramStatus(i)) {
               case 0:
                  s = "read/write";
                  break;
               case 1:
                  s = "read-only";
                  break;
               case 2:
                  s = "safe";
                  break;
               default:
                  s = "unknown";
               }

               m.addTag(new StringTag("param" + i + ": " + s));
               if (opts.print()) {
                  logger.debug("  |   |- param " + i + " is " + s);
               }
            }
         }
      }

   }

   protected PurityGraphBox newInitialSummary() {
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

   protected void analyseMethod(SootMethod method, PurityGraphBox dst) {
      Body body = method.retrieveActiveBody();
      ExceptionalUnitGraph graph = new ExceptionalUnitGraph(body);
      (new PurityIntraproceduralAnalysis(graph, this)).copyResult(dst);
   }

   protected PurityGraphBox summaryOfUnanalysedMethod(SootMethod method) {
      PurityGraphBox b = new PurityGraphBox();
      String c = method.getDeclaringClass().toString();
      String m = method.getName();
      b.g = PurityGraph.conservativeGraph(method, true);
      String[][] var5 = pureMethods;
      int var6 = var5.length;

      int var7;
      String[] element;
      for(var7 = 0; var7 < var6; ++var7) {
         element = var5[var7];
         if (m.equals(element[1]) && c.startsWith(element[0])) {
            b.g = PurityGraph.freshGraph(method);
         }
      }

      var5 = alterMethods;
      var6 = var5.length;

      for(var7 = 0; var7 < var6; ++var7) {
         element = var5[var7];
         if (m.equals(element[1]) && c.startsWith(element[0])) {
            b.g = PurityGraph.conservativeGraph(method, false);
         }
      }

      return b;
   }

   protected void applySummary(PurityGraphBox src, Stmt stmt, PurityGraphBox summary, PurityGraphBox dst) {
      InvokeExpr e = stmt.getInvokeExpr();
      Local ret = null;
      Local obj;
      if (stmt instanceof AssignStmt) {
         obj = (Local)((AssignStmt)stmt).getLeftOp();
         if (obj.getType() instanceof RefLikeType) {
            ret = obj;
         }
      }

      obj = null;
      if (!(e instanceof StaticInvokeExpr)) {
         obj = (Local)((InstanceInvokeExpr)e).getBase();
      }

      List<Value> args = e.getArgs();
      PurityGraph g = new PurityGraph(src.g);
      g.methodCall(summary.g, obj, args, ret);
      dst.g = g;
   }

   protected void fillDotGraph(String prefix, PurityGraphBox o, DotGraph out) {
      o.g.fillDotGraph(prefix, out);
   }

   private static class Filter implements SootMethodFilter {
      private Filter() {
      }

      public boolean want(SootMethod method) {
         String c = method.getDeclaringClass().toString();
         String m = method.getName();
         String[][] var4 = PurityInterproceduralAnalysis.pureMethods;
         int var5 = var4.length;

         int var6;
         String[] element;
         for(var6 = 0; var6 < var5; ++var6) {
            element = var4[var6];
            if (m.equals(element[1]) && c.startsWith(element[0])) {
               return false;
            }
         }

         var4 = PurityInterproceduralAnalysis.impureMethods;
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            element = var4[var6];
            if (m.equals(element[1]) && c.startsWith(element[0])) {
               return false;
            }
         }

         var4 = PurityInterproceduralAnalysis.alterMethods;
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            element = var4[var6];
            if (m.equals(element[1]) && c.startsWith(element[0])) {
               return false;
            }
         }

         return true;
      }

      // $FF: synthetic method
      Filter(Object x0) {
         this();
      }
   }
}
