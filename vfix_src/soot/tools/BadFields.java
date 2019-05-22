package soot.tools;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Main;
import soot.PackManager;
import soot.PrimType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Transform;
import soot.Value;
import soot.ValueBox;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;

public class BadFields extends SceneTransformer {
   private static final Logger logger = LoggerFactory.getLogger(BadFields.class);
   private SootClass lastClass;
   private SootClass currentClass;

   public static void main(String[] args) {
      PackManager.v().getPack("cg").add(new Transform("cg.badfields", new BadFields()));
      Main.main(args);
   }

   protected void internalTransform(String phaseName, Map<String, String> options) {
      this.lastClass = null;
      Iterator clIt = Scene.v().getApplicationClasses().iterator();

      while(clIt.hasNext()) {
         SootClass cl = (SootClass)clIt.next();
         this.currentClass = cl;
         this.handleClass(cl);
         Iterator it = cl.methodIterator();

         while(it.hasNext()) {
            this.handleMethod((SootMethod)it.next());
         }
      }

      Scene.v().setCallGraph(Scene.v().internalMakeCallGraph());
   }

   private void handleClass(SootClass cl) {
      Iterator fIt = cl.getFields().iterator();

      while(true) {
         SootField f;
         String typeName;
         do {
            do {
               do {
                  if (!fIt.hasNext()) {
                     return;
                  }

                  f = (SootField)fIt.next();
               } while(!f.isStatic());

               typeName = f.getType().toString();
            } while(typeName.equals("java.lang.Class"));
         } while(f.isFinal() && (f.getType() instanceof PrimType || typeName.equals("java.io.PrintStream") || typeName.equals("java.lang.String") || typeName.equals("java.lang.Object") || typeName.equals("java.lang.Integer") || typeName.equals("java.lang.Boolean")));

         this.warn("Bad field " + f);
      }
   }

   private void warn(String warning) {
      if (this.lastClass != this.currentClass) {
         logger.debug("In class " + this.currentClass);
      }

      this.lastClass = this.currentClass;
      logger.debug("  " + warning);
   }

   private void handleMethod(SootMethod m) {
      if (m.isConcrete()) {
         Iterator sIt = m.retrieveActiveBody().getUseAndDefBoxes().iterator();

         while(sIt.hasNext()) {
            ValueBox b = (ValueBox)sIt.next();
            Value v = b.getValue();
            if (v instanceof StaticFieldRef) {
               StaticFieldRef sfr = (StaticFieldRef)v;
               SootField f = sfr.getField();
               if (f.getDeclaringClass().getName().equals("java.lang.System")) {
                  if (f.getName().equals("err")) {
                     logger.debug("Use of System.err in " + m);
                  }

                  if (f.getName().equals("out")) {
                     logger.debug("Use of System.out in " + m);
                  }
               }
            }
         }

         sIt = m.getActiveBody().getUnits().iterator();

         Stmt s;
         InvokeExpr ie;
         SootMethod target;
         while(sIt.hasNext()) {
            s = (Stmt)sIt.next();
            if (s.containsInvokeExpr()) {
               ie = s.getInvokeExpr();
               target = ie.getMethod();
               if (target.getDeclaringClass().getName().equals("java.lang.System") && target.getName().equals("exit")) {
                  this.warn("" + m + " calls System.exit");
               }
            }
         }

         if (m.getName().equals("<clinit>")) {
            sIt = m.getActiveBody().getUnits().iterator();

            while(sIt.hasNext()) {
               s = (Stmt)sIt.next();
               Iterator bIt = s.getUseBoxes().iterator();

               while(bIt.hasNext()) {
                  ValueBox b = (ValueBox)bIt.next();
                  Value v = b.getValue();
                  if (v instanceof FieldRef) {
                     this.warn(m.getName() + " reads field " + v);
                  }
               }

               if (s.containsInvokeExpr()) {
                  ie = s.getInvokeExpr();
                  target = ie.getMethod();
                  this.calls(target);
               }
            }
         }

      }
   }

   private void calls(SootMethod target) {
      if (target.getName().equals("<init>")) {
         if (target.getDeclaringClass().getName().equals("java.io.PrintStream")) {
            return;
         }

         if (target.getDeclaringClass().getName().equals("java.lang.Boolean")) {
            return;
         }

         if (target.getDeclaringClass().getName().equals("java.lang.Integer")) {
            return;
         }

         if (target.getDeclaringClass().getName().equals("java.lang.String")) {
            return;
         }

         if (target.getDeclaringClass().getName().equals("java.lang.Object")) {
            return;
         }
      }

      if (!target.getName().equals("getProperty") || !target.getDeclaringClass().getName().equals("java.lang.System")) {
         if (!target.getName().equals("charAt") || !target.getDeclaringClass().getName().equals("java.lang.String")) {
            this.warn("<clinit> invokes " + target);
         }
      }
   }
}
