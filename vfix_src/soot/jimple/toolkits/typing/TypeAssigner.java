package soot.jimple.toolkits.typing;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.ByteType;
import soot.CharType;
import soot.ErroneousType;
import soot.G;
import soot.Local;
import soot.NullType;
import soot.PhaseOptions;
import soot.Scene;
import soot.ShortType;
import soot.Singletons;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
import soot.jimple.toolkits.scalar.ConstantPropagatorAndFolder;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.jimple.toolkits.typing.fast.AugHierarchy;
import soot.options.JBTROptions;
import soot.options.Options;
import soot.toolkits.scalar.UnusedLocalEliminator;

public class TypeAssigner extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(TypeAssigner.class);

   public TypeAssigner(Singletons.Global g) {
   }

   public static TypeAssigner v() {
      return G.v().soot_jimple_toolkits_typing_TypeAssigner();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      if (b == null) {
         throw new NullPointerException();
      } else {
         Date start = new Date();
         if (Options.v().verbose()) {
            logger.debug("[TypeAssigner] typing system started on " + start);
         }

         JBTROptions opt = new JBTROptions(options);
         if (opt.compare_type_assigners()) {
            this.compareTypeAssigners(b, opt.use_older_type_assigner());
         } else if (opt.use_older_type_assigner()) {
            TypeResolver.resolve((JimpleBody)b, Scene.v());
         } else {
            (new soot.jimple.toolkits.typing.fast.TypeResolver((JimpleBody)b)).inferTypes();
         }

         Date finish = new Date();
         if (Options.v().verbose()) {
            long runtime = finish.getTime() - start.getTime();
            long mins = runtime / 60000L;
            long secs = runtime % 60000L / 1000L;
            logger.debug("[TypeAssigner] typing system ended. It took " + mins + " mins and " + secs + " secs.");
         }

         if (!opt.ignore_nullpointer_dereferences()) {
            this.replaceNullType(b);
         }

         if (this.typingFailed((JimpleBody)b)) {
            throw new RuntimeException("type inference failed!");
         }
      }
   }

   private void replaceNullType(Body b) {
      List<Local> localsToRemove = new ArrayList();
      boolean hasNullType = false;
      Iterator var4 = b.getLocals().iterator();

      while(var4.hasNext()) {
         Local l = (Local)var4.next();
         if (l.getType() instanceof NullType) {
            localsToRemove.add(l);
            hasNullType = true;
         }
      }

      if (hasNullType) {
         Map<String, String> opts = PhaseOptions.v().getPhaseOptions("jop.cpf");
         if (opts.containsKey("enabled") && ((String)opts.get("enabled")).equals("true")) {
            ConstantPropagatorAndFolder.v().transform(b);
            List<Unit> unitToReplaceByException = new ArrayList();
            Iterator var6 = b.getUnits().iterator();

            Unit u;
            while(var6.hasNext()) {
               u = (Unit)var6.next();
               Iterator var8 = u.getUseBoxes().iterator();

               while(var8.hasNext()) {
                  ValueBox vb = (ValueBox)var8.next();
                  if (vb.getValue() instanceof Local && ((Local)vb.getValue()).getType() instanceof NullType) {
                     Local l = (Local)vb.getValue();
                     Stmt s = (Stmt)u;
                     boolean replace = false;
                     if (s.containsArrayRef()) {
                        ArrayRef r = s.getArrayRef();
                        if (r.getBase() == l) {
                           replace = true;
                        }
                     } else if (s.containsFieldRef()) {
                        FieldRef r = s.getFieldRef();
                        if (r instanceof InstanceFieldRef) {
                           InstanceFieldRef ir = (InstanceFieldRef)r;
                           if (ir.getBase() == l) {
                              replace = true;
                           }
                        }
                     } else if (s.containsInvokeExpr()) {
                        InvokeExpr ie = s.getInvokeExpr();
                        if (ie instanceof InstanceInvokeExpr) {
                           InstanceInvokeExpr iie = (InstanceInvokeExpr)ie;
                           if (iie.getBase() == l) {
                              replace = true;
                           }
                        }
                     }

                     if (replace) {
                        unitToReplaceByException.add(u);
                     }
                  }
               }
            }

            var6 = unitToReplaceByException.iterator();

            while(var6.hasNext()) {
               u = (Unit)var6.next();
               soot.dexpler.Util.addExceptionAfterUnit(b, "java.lang.NullPointerException", u, "This statement would have triggered an Exception: " + u);
               b.getUnits().remove(u);
            }

            DeadAssignmentEliminator.v().transform(b);
            UnusedLocalEliminator.v().transform(b);
         } else {
            logger.warn("Cannot run TypeAssigner.replaceNullType(Body). Try to enable jop.cfg.");
         }
      }
   }

   private void compareTypeAssigners(Body b, boolean useOlderTypeAssigner) {
      JimpleBody jb = (JimpleBody)b;
      int size = jb.getUnits().size();
      JimpleBody oldJb;
      JimpleBody newJb;
      long oldTime;
      long newTime;
      if (useOlderTypeAssigner) {
         newJb = (JimpleBody)jb.clone();
         newTime = System.currentTimeMillis();
         (new soot.jimple.toolkits.typing.fast.TypeResolver(newJb)).inferTypes();
         newTime = System.currentTimeMillis() - newTime;
         oldTime = System.currentTimeMillis();
         TypeResolver.resolve(jb, Scene.v());
         oldTime = System.currentTimeMillis() - oldTime;
         oldJb = jb;
      } else {
         oldJb = (JimpleBody)jb.clone();
         oldTime = System.currentTimeMillis();
         TypeResolver.resolve(oldJb, Scene.v());
         oldTime = System.currentTimeMillis() - oldTime;
         newTime = System.currentTimeMillis();
         (new soot.jimple.toolkits.typing.fast.TypeResolver(jb)).inferTypes();
         newTime = System.currentTimeMillis() - newTime;
         newJb = jb;
      }

      int cmp;
      if (newJb.getLocals().size() < oldJb.getLocals().size()) {
         cmp = 2;
      } else if (newJb.getLocals().size() > oldJb.getLocals().size()) {
         cmp = -2;
      } else {
         cmp = compareTypings(oldJb, newJb);
      }

      logger.debug("cmp;" + jb.getMethod() + ";" + size + ";" + oldTime + ";" + newTime + ";" + cmp);
   }

   private boolean typingFailed(JimpleBody b) {
      Iterator<Local> localIt = b.getLocals().iterator();
      UnknownType unknownType = UnknownType.v();
      ErroneousType errornousType = ErroneousType.v();

      Local l;
      do {
         if (!localIt.hasNext()) {
            return false;
         }

         l = (Local)localIt.next();
      } while(!l.getType().equals(unknownType) && !l.getType().equals(errornousType));

      return true;
   }

   private static int compareTypings(JimpleBody a, JimpleBody b) {
      int r = 0;
      Iterator<Local> ib = b.getLocals().iterator();
      Iterator var4 = a.getLocals().iterator();

      while(true) {
         Type ta;
         Type tb;
         do {
            do {
               do {
                  if (!var4.hasNext()) {
                     return r;
                  }

                  Local v = (Local)var4.next();
                  ta = v.getType();
                  tb = ((Local)ib.next()).getType();
               } while(soot.jimple.toolkits.typing.fast.TypeResolver.typesEqual(ta, tb));
            } while(ta instanceof CharType && (tb instanceof ByteType || tb instanceof ShortType));
         } while(tb instanceof CharType && (ta instanceof ByteType || ta instanceof ShortType));

         if (AugHierarchy.ancestor_(ta, tb)) {
            if (r == -1) {
               return 3;
            }

            r = 1;
         } else {
            if (!AugHierarchy.ancestor_(tb, ta)) {
               return 3;
            }

            if (r == 1) {
               return 3;
            }

            r = -1;
         }
      }
   }
}
