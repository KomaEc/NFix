package soot.jbco.jimpleTransformations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Hierarchy;
import soot.NullType;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.jbco.IJbcoTransform;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;
import soot.jimple.ClassConstant;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.StringConstant;
import soot.util.Chain;

public class CollectConstants extends SceneTransformer implements IJbcoTransform {
   int updatedConstants = 0;
   int constants = 0;
   public static String[] dependancies = new String[]{"wjtp.jbco_cc"};
   public static String name = "wjtp.jbco_cc";
   public static HashMap<Constant, SootField> constantsToFields = new HashMap();
   public static HashMap<Type, List<Constant>> typesToValues = new HashMap();
   public static SootField field = null;

   public void outputSummary() {
      out.println(this.constants + " constants found");
      out.println(this.updatedConstants + " static fields created");
   }

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   protected void internalTransform(String phaseName, Map<String, String> options) {
      if (output) {
         out.println("Collecting Constant Data");
      }

      BodyBuilder.retrieveAllNames();
      Chain<SootClass> appClasses = Scene.v().getApplicationClasses();
      Iterator var4 = appClasses.iterator();

      label74:
      while(var4.hasNext()) {
         SootClass sc = (SootClass)var4.next();
         Iterator var6 = sc.getMethods().iterator();

         while(true) {
            SootMethod m;
            do {
               do {
                  if (!var6.hasNext()) {
                     continue label74;
                  }

                  m = (SootMethod)var6.next();
               } while(!m.hasActiveBody());
            } while(m.getName().contains("<clinit>"));

            Iterator var8 = m.getActiveBody().getUseBoxes().iterator();

            while(var8.hasNext()) {
               ValueBox useBox = (ValueBox)var8.next();
               Value v = useBox.getValue();
               if (v instanceof Constant) {
                  Constant constant = (Constant)v;
                  Type type = constant.getType();
                  List<Constant> constants = (List)typesToValues.computeIfAbsent(type, (t) -> {
                     return new ArrayList();
                  });
                  if (!constants.contains(constant)) {
                     ++this.constants;
                     constants.add(constant);
                  }
               }
            }
         }
      }

      int count = 0;
      String name = "newConstantJbcoName";
      SootClass[] classes = (SootClass[])appClasses.toArray(new SootClass[appClasses.size()]);
      Iterator var17 = typesToValues.keySet().iterator();

      while(true) {
         Type type;
         do {
            if (!var17.hasNext()) {
               this.updatedConstants += count;
               return;
            }

            type = (Type)var17.next();
         } while(type instanceof NullType);

         Iterator var19 = ((List)typesToValues.get(type)).iterator();

         while(var19.hasNext()) {
            Constant constant = (Constant)var19.next();
            name = name + "_";

            SootClass randomClass;
            do {
               randomClass = classes[Rand.getInt(classes.length)];
            } while(!this.isSuitableClassToAddFieldConstant(randomClass, constant));

            SootField newField = Scene.v().makeSootField(FieldRenamer.getNewName(), type, 9);
            randomClass.addField(newField);
            FieldRenamer.sootFieldsRenamed.add(newField);
            FieldRenamer.addOldAndNewName(name, newField.getName());
            constantsToFields.put(constant, newField);
            this.addInitializingValue(randomClass, newField, constant);
            FieldRenamer.addOldAndNewName("addedConstant" + count++, newField.getName());
         }
      }
   }

   private boolean isSuitableClassToAddFieldConstant(SootClass sc, Constant constant) {
      if (sc.isInterface()) {
         return false;
      } else if (constant instanceof ClassConstant) {
         ClassConstant classConstant = (ClassConstant)constant;
         RefType type = (RefType)classConstant.toSootType();
         SootClass classFromConstant = type.getSootClass();
         Hierarchy hierarchy = Scene.v().getActiveHierarchy();
         return hierarchy.isVisible(sc, classFromConstant);
      } else {
         return true;
      }
   }

   private void addInitializingValue(SootClass sc, SootField f, Constant constant) {
      if (!(constant instanceof NullConstant)) {
         if (constant instanceof IntConstant) {
            if (((IntConstant)constant).value == 0) {
               return;
            }
         } else if (constant instanceof LongConstant) {
            if (((LongConstant)constant).value == 0L) {
               return;
            }
         } else if (constant instanceof StringConstant) {
            if (((StringConstant)constant).value == null) {
               return;
            }
         } else if (constant instanceof DoubleConstant) {
            if (((DoubleConstant)constant).value == 0.0D) {
               return;
            }
         } else if (constant instanceof FloatConstant && ((FloatConstant)constant).value == 0.0F) {
            return;
         }

         boolean newInit = false;
         Object b;
         SootMethod m;
         if (!sc.declaresMethodByName("<clinit>")) {
            m = Scene.v().makeSootMethod("<clinit>", Collections.emptyList(), VoidType.v(), 8);
            sc.addMethod(m);
            b = Jimple.v().newBody(m);
            m.setActiveBody((Body)b);
            newInit = true;
         } else {
            m = sc.getMethodByName("<clinit>");
            if (!m.hasActiveBody()) {
               b = Jimple.v().newBody(m);
               m.setActiveBody((Body)b);
               newInit = true;
            } else {
               b = m.getActiveBody();
            }
         }

         PatchingChain<Unit> units = ((Body)b).getUnits();
         units.addFirst((Unit)Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(f.makeRef()), constant));
         if (newInit) {
            units.addLast((Unit)Jimple.v().newReturnVoidStmt());
         }

      }
   }
}
