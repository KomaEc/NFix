package soot.jbco.jimpleTransformations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BooleanType;
import soot.IntegerType;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;
import soot.jimple.FieldRef;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;

public class FieldRenamer extends SceneTransformer implements IJbcoTransform {
   private static final Logger logger = LoggerFactory.getLogger(FieldRenamer.class);
   public static String[] dependancies = new String[]{"wjtp.jbco_fr"};
   public static String name = "wjtp.jbco_fr";
   private static final char[][] stringChars = new char[][]{{'S', '5', '$'}, {'l', '1', 'I'}, {'_'}};
   private static final String booleanClassName = Boolean.class.getName();
   public static List<String> namesToNotRename = new ArrayList();
   public static Map<String, String> oldToNewFieldNames = new HashMap();
   public static Map<SootClass, SootField> opaquePreds1ByClass = new HashMap();
   public static Map<SootClass, SootField> opaquePreds2ByClass = new HashMap();
   public static List<SootField> sootFieldsRenamed = new ArrayList();
   public static SootField[][] opaquePairs = (SootField[][])null;
   public static int[] handedOutPairs = null;
   public static int[] handedOutRunPairs = null;
   public static boolean rename_fields = false;

   public void outputSummary() {
   }

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   protected void internalTransform(String phaseName, Map<String, String> options) {
      if (output) {
         if (rename_fields) {
            out.println("Transforming Field Names and Adding Opaque Predicates...");
         } else {
            out.println("Adding Opaques...");
         }
      }

      RefType boolRef = Scene.v().getRefType(booleanClassName);
      BodyBuilder.retrieveAllBodies();
      BodyBuilder.retrieveAllNames();
      Iterator var4 = Scene.v().getApplicationClasses().iterator();

      SootClass sc;
      while(var4.hasNext()) {
         sc = (SootClass)var4.next();
         String className = sc.getName();
         if (className.contains(".")) {
            className = className.substring(className.lastIndexOf(".") + 1, className.length());
         }

         oldToNewFieldNames.put(className, className);
         if (rename_fields) {
            if (output) {
               out.println("\tClassName: " + className);
            }

            Iterator var7 = sc.getFields().iterator();

            while(var7.hasNext()) {
               SootField f = (SootField)var7.next();
               int weight = Main.getWeight(phaseName, f.getName());
               if (weight > 0) {
                  this.renameField(className, f);
               }
            }
         }

         if (!sc.isInterface()) {
            String bool = "opPred1";

            Object t;
            for(t = Rand.getInt() % 2 == 0 ? BooleanType.v() : boolRef; oldToNewFieldNames.containsKey(bool); bool = bool + "_") {
            }

            SootField f = Scene.v().makeSootField(bool, (Type)t, 9);
            this.renameField(className, f);
            opaquePreds1ByClass.put(sc, f);
            sc.addField(f);
            this.setBooleanTo(sc, f, true);
            bool = "opPred2";

            for(t = t == BooleanType.v() ? boolRef : BooleanType.v(); oldToNewFieldNames.containsKey(bool); bool = bool + "_") {
            }

            f = Scene.v().makeSootField(bool, (Type)t, 9);
            this.renameField(className, f);
            opaquePreds2ByClass.put(sc, f);
            sc.addField(f);
            if (t == boolRef) {
               this.setBooleanTo(sc, f, false);
            }
         }
      }

      this.buildOpaquePairings();
      if (rename_fields) {
         if (output) {
            out.println("\r\tUpdating field references in bytecode");
         }

         var4 = Scene.v().getApplicationClasses().iterator();

         label119:
         while(var4.hasNext()) {
            sc = (SootClass)var4.next();
            Iterator var20 = sc.getMethods().iterator();

            while(true) {
               SootMethod m;
               do {
                  if (!var20.hasNext()) {
                     continue label119;
                  }

                  m = (SootMethod)var20.next();
               } while(!m.isConcrete());

               if (!m.hasActiveBody()) {
                  m.retrieveActiveBody();
               }

               Iterator var24 = m.getActiveBody().getUnits().iterator();

               while(var24.hasNext()) {
                  Unit unit = (Unit)var24.next();
                  Iterator var10 = unit.getUseAndDefBoxes().iterator();

                  while(var10.hasNext()) {
                     ValueBox box = (ValueBox)var10.next();
                     Value value = box.getValue();
                     if (value instanceof FieldRef) {
                        FieldRef fieldRef = (FieldRef)value;
                        SootFieldRef sootFieldRef = fieldRef.getFieldRef();
                        if (!sootFieldRef.declaringClass().isLibraryClass()) {
                           String oldName = sootFieldRef.name();
                           String fullName = sootFieldRef.declaringClass().getName() + '.' + oldName;
                           String newName = (String)oldToNewFieldNames.get(oldName);
                           if (newName != null && !namesToNotRename.contains(fullName)) {
                              if (newName.equals(oldName)) {
                                 System.out.println("Strange.. Should not find a field with the same old and new name.");
                              }

                              sootFieldRef = Scene.v().makeFieldRef(sootFieldRef.declaringClass(), newName, sootFieldRef.type(), sootFieldRef.isStatic());
                              fieldRef.setFieldRef(sootFieldRef);

                              try {
                                 sootFieldRef.resolve();
                              } catch (Exception var19) {
                                 System.err.println("********ERROR Updating " + sootFieldRef.name() + " to " + newName);
                                 System.err.println("Fields of " + sootFieldRef.declaringClass().getName() + ": " + sootFieldRef.declaringClass().getFields());
                                 throw new RuntimeException(var19);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

      }
   }

   protected void setBooleanTo(SootClass sc, SootField f, boolean value) {
      if (value || !(f.getType() instanceof IntegerType) || Rand.getInt() % 2 <= 0) {
         RefType boolRef = Scene.v().getRefType(booleanClassName);
         boolean newInit = false;
         Object body;
         SootMethod m;
         if (!sc.declaresMethodByName("<clinit>")) {
            m = Scene.v().makeSootMethod("<clinit>", Collections.emptyList(), VoidType.v(), 8);
            sc.addMethod(m);
            body = Jimple.v().newBody(m);
            m.setActiveBody((Body)body);
            newInit = true;
         } else {
            m = sc.getMethodByName("<clinit>");
            body = m.getActiveBody();
         }

         PatchingChain<Unit> units = ((Body)body).getUnits();
         if (f.getType() instanceof IntegerType) {
            units.addFirst((Unit)Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(f.makeRef()), IntConstant.v(value ? 1 : 0)));
         } else {
            Local bool = Jimple.v().newLocal("boolLcl", boolRef);
            ((Body)body).getLocals().add(bool);
            SootMethod boolInit = boolRef.getSootClass().getMethod("void <init>(boolean)");
            units.addFirst((Unit)Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(f.makeRef()), bool));
            units.addFirst((Unit)Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(bool, boolInit.makeRef(), (Value)IntConstant.v(value ? 1 : 0))));
            units.addFirst((Unit)Jimple.v().newAssignStmt(bool, Jimple.v().newNewExpr(boolRef)));
         }

         if (newInit) {
            units.addLast((Unit)Jimple.v().newReturnVoidStmt());
         }

      }
   }

   protected void renameField(String className, SootField f) {
      if (!sootFieldsRenamed.contains(f)) {
         String newName = (String)oldToNewFieldNames.get(f.getName());
         if (newName == null) {
            newName = getNewName();
            oldToNewFieldNames.put(f.getName(), newName);
         }

         if (output) {
            logger.debug("\t\tChanged " + f.getName() + " to " + newName);
         }

         f.setName(newName);
         sootFieldsRenamed.add(f);
      }
   }

   public static String getNewName() {
      int size = 3;
      int tries = 0;
      int index = Rand.getInt(stringChars.length);
      int length = stringChars[index].length;
      char[] cNewName = new char[size];

      String result;
      do {
         if (tries == 10) {
            ++size;
            cNewName = new char[size];
            index = Rand.getInt(stringChars.length);
            length = stringChars[index].length;
            tries = 0;
         }

         int i;
         if (size < 12) {
            do {
               i = Rand.getInt(length);
               cNewName[0] = stringChars[index][i];
            } while(!Character.isJavaIdentifierStart(cNewName[0]));

            for(i = 1; i < cNewName.length; ++i) {
               int rand = Rand.getInt(length);
               cNewName[i] = stringChars[index][rand];
            }

            result = String.copyValueOf(cNewName);
         } else {
            cNewName = new char[size - 6];

            do {
               for(i = 0; i < cNewName.length; ++i) {
                  cNewName[i] = (char)Rand.getInt();
               }

               result = String.copyValueOf(cNewName);
            } while(!isJavaIdentifier(result));
         }

         ++tries;
      } while(oldToNewFieldNames.containsValue(result) || BodyBuilder.nameList.contains(result));

      BodyBuilder.nameList.add(result);
      return result;
   }

   public static void addOldAndNewName(String oldn, String newn) {
      oldToNewFieldNames.put(oldn, newn);
   }

   public static boolean isJavaIdentifier(String s) {
      if (s != null && s.length() != 0 && Character.isJavaIdentifierStart(s.charAt(0))) {
         for(int i = 1; i < s.length(); ++i) {
            if (!Character.isJavaIdentifierPart(s.charAt(i))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static SootField[] getRandomOpaques() {
      if (handedOutPairs == null) {
         handedOutPairs = new int[opaquePairs.length];
      }

      int lowValue = 99999;
      List<Integer> available = new ArrayList();
      int[] var2 = handedOutPairs;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int element = var2[var4];
         if (lowValue > element) {
            lowValue = element;
         }
      }

      int i;
      for(i = 0; i < handedOutPairs.length; ++i) {
         if (handedOutPairs[i] == lowValue) {
            available.add(i);
         }
      }

      i = (Integer)available.get(Rand.getInt(available.size()));
      int var10002 = handedOutPairs[i]++;
      return opaquePairs[i];
   }

   public static int getRandomOpaquesForRunnable() {
      if (handedOutRunPairs == null) {
         handedOutRunPairs = new int[opaquePairs.length];
      }

      int lowValue = 99999;
      List<Integer> available = new ArrayList();
      int[] var2 = handedOutRunPairs;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int element = var2[var4];
         if (lowValue > element) {
            lowValue = element;
         }
      }

      if (lowValue > 2) {
         return -1;
      } else {
         for(int i = 0; i < handedOutRunPairs.length; ++i) {
            if (handedOutRunPairs[i] == lowValue) {
               available.add(i);
            }
         }

         return (Integer)available.get(Rand.getInt(available.size()));
      }
   }

   public static void updateOpaqueRunnableCount(int i) {
      int var10002 = handedOutRunPairs[i]++;
   }

   private void buildOpaquePairings() {
      Object[] fields1 = opaquePreds1ByClass.values().toArray();
      Object[] fields2 = opaquePreds2ByClass.values().toArray();
      int length = fields1.length;
      int i;
      if (length > 1) {
         for(i = length * 2; i > 1; --i) {
            int rand1 = Rand.getInt(length);
            int rand2 = Rand.getInt(length);
            int rand3 = Rand.getInt(length);

            int rand4;
            for(rand4 = Rand.getInt(length); rand1 == rand2; rand2 = Rand.getInt(length)) {
            }

            while(rand3 == rand4) {
               rand4 = Rand.getInt(length);
            }

            Object value = fields1[rand1];
            fields1[rand1] = fields1[rand2];
            fields1[rand2] = value;
            value = fields2[rand3];
            fields2[rand3] = fields2[rand4];
            fields2[rand4] = value;
         }
      }

      opaquePairs = new SootField[length][2];

      for(i = 0; i < length; ++i) {
         opaquePairs[i] = new SootField[]{(SootField)fields1[i], (SootField)fields2[i]};
      }

   }
}
