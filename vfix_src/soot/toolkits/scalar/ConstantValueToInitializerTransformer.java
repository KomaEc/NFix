package soot.toolkits.scalar;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.ValueBox;
import soot.VoidType;
import soot.jimple.DoubleConstant;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.Tag;
import soot.util.Chain;

public class ConstantValueToInitializerTransformer extends SceneTransformer {
   public static ConstantValueToInitializerTransformer v() {
      return new ConstantValueToInitializerTransformer();
   }

   protected void internalTransform(String phaseName, Map<String, String> options) {
      Iterator var3 = Scene.v().getClasses().iterator();

      while(var3.hasNext()) {
         SootClass sc = (SootClass)var3.next();
         this.transformClass(sc);
      }

   }

   public void transformClass(SootClass sc) {
      SootMethod smInit = null;
      Set<SootField> alreadyInitialized = new HashSet();
      Iterator var4 = sc.getFields().iterator();

      while(true) {
         SootField sf;
         do {
            do {
               do {
                  if (!var4.hasNext()) {
                     if (smInit != null) {
                        Chain<Unit> units = smInit.getActiveBody().getUnits();
                        if (units.isEmpty() || !(units.getLast() instanceof ReturnVoidStmt)) {
                           units.add(Jimple.v().newReturnVoidStmt());
                        }
                     }

                     return;
                  }

                  sf = (SootField)var4.next();
               } while(!sf.isStatic());
            } while(!sf.isFinal());
         } while(alreadyInitialized.contains(sf));

         Iterator var6 = sf.getTags().iterator();

         while(var6.hasNext()) {
            Tag t = (Tag)var6.next();
            Stmt initStmt = null;
            if (t instanceof DoubleConstantValueTag) {
               double value = ((DoubleConstantValueTag)t).getDoubleValue();
               initStmt = Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(sf.makeRef()), DoubleConstant.v(value));
            } else if (t instanceof FloatConstantValueTag) {
               float value = ((FloatConstantValueTag)t).getFloatValue();
               initStmt = Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(sf.makeRef()), FloatConstant.v(value));
            } else if (t instanceof IntegerConstantValueTag) {
               int value = ((IntegerConstantValueTag)t).getIntValue();
               initStmt = Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(sf.makeRef()), IntConstant.v(value));
            } else if (t instanceof LongConstantValueTag) {
               long value = ((LongConstantValueTag)t).getLongValue();
               initStmt = Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(sf.makeRef()), LongConstant.v(value));
            } else if (t instanceof StringConstantValueTag) {
               String value = ((StringConstantValueTag)t).getStringValue();
               initStmt = Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(sf.makeRef()), StringConstant.v(value));
            }

            if (initStmt != null) {
               if (smInit == null) {
                  smInit = this.getOrCreateInitializer(sc, alreadyInitialized);
               }

               if (smInit != null) {
                  smInit.getActiveBody().getUnits().addFirst((Unit)initStmt);
               }
            }
         }
      }
   }

   private SootMethod getOrCreateInitializer(SootClass sc, Set<SootField> alreadyInitialized) {
      SootMethod smInit = sc.getMethodByNameUnsafe("<clinit>");
      if (smInit == null) {
         smInit = Scene.v().makeSootMethod("<clinit>", Collections.emptyList(), VoidType.v());
         smInit.setActiveBody(Jimple.v().newBody(smInit));
         sc.addMethod(smInit);
         smInit.setModifiers(9);
      } else {
         if (smInit.isPhantom()) {
            return null;
         }

         smInit.retrieveActiveBody();
         Iterator var4 = smInit.getActiveBody().getUnits().iterator();

         while(var4.hasNext()) {
            Unit u = (Unit)var4.next();
            Stmt s = (Stmt)u;
            Iterator var7 = s.getDefBoxes().iterator();

            while(var7.hasNext()) {
               ValueBox vb = (ValueBox)var7.next();
               if (vb.getValue() instanceof FieldRef) {
                  alreadyInitialized.add(((FieldRef)vb.getValue()).getField());
               }
            }
         }
      }

      return smInit;
   }
}
