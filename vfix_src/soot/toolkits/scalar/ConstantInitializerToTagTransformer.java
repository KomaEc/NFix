package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ConflictingFieldRefException;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.StaticFieldRef;
import soot.jimple.StringConstant;
import soot.tagkit.ConstantValueTag;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.Tag;

public class ConstantInitializerToTagTransformer extends SceneTransformer {
   private static final Logger logger = LoggerFactory.getLogger(ConstantInitializerToTagTransformer.class);
   private static final ConstantInitializerToTagTransformer INSTANCE = new ConstantInitializerToTagTransformer();

   public static ConstantInitializerToTagTransformer v() {
      return INSTANCE;
   }

   protected void internalTransform(String phaseName, Map<String, String> options) {
      Iterator var3 = Scene.v().getClasses().iterator();

      while(var3.hasNext()) {
         SootClass sc = (SootClass)var3.next();
         this.transformClass(sc, false);
      }

   }

   public void transformClass(SootClass sc, boolean removeAssignments) {
      SootMethod smInit = sc.getMethodByNameUnsafe("<clinit>");
      if (smInit != null && smInit.isConcrete()) {
         Set<SootField> nonConstantFields = new HashSet();
         Map<SootField, ConstantValueTag> newTags = new HashMap();
         Set<SootField> removeTagList = new HashSet();
         Iterator itU = smInit.getActiveBody().getUnits().snapshotIterator();

         while(true) {
            AssignStmt assign;
            SootField field;
            do {
               do {
                  label147:
                  do {
                     while(true) {
                        while(true) {
                           Unit u;
                           do {
                              if (!itU.hasNext()) {
                                 itU = newTags.entrySet().iterator();

                                 while(itU.hasNext()) {
                                    Entry<SootField, ConstantValueTag> entry = (Entry)itU.next();
                                    SootField field = (SootField)entry.getKey();
                                    if (!removeTagList.contains(field)) {
                                       field.addTag((Tag)entry.getValue());
                                    }
                                 }

                                 if (removeAssignments && !newTags.isEmpty()) {
                                    itU = smInit.getActiveBody().getUnits().snapshotIterator();

                                    while(itU.hasNext()) {
                                       u = (Unit)itU.next();
                                       if (u instanceof AssignStmt) {
                                          assign = (AssignStmt)u;
                                          if (assign.getLeftOp() instanceof FieldRef) {
                                             try {
                                                field = ((FieldRef)assign.getLeftOp()).getField();
                                                if (field != null && newTags.containsKey(field)) {
                                                   itU.remove();
                                                }
                                             } catch (ConflictingFieldRefException var14) {
                                             }
                                          }
                                       }
                                    }
                                 }

                                 itU = removeTagList.iterator();

                                 while(true) {
                                    SootField sf;
                                    do {
                                       if (!itU.hasNext()) {
                                          return;
                                       }

                                       sf = (SootField)itU.next();
                                    } while(!removeTagList.contains(sf));

                                    List<Tag> toRemoveTagList = new ArrayList();
                                    Iterator var21 = sf.getTags().iterator();

                                    Tag t;
                                    while(var21.hasNext()) {
                                       t = (Tag)var21.next();
                                       if (t instanceof ConstantValueTag) {
                                          toRemoveTagList.add(t);
                                       }
                                    }

                                    var21 = toRemoveTagList.iterator();

                                    while(var21.hasNext()) {
                                       t = (Tag)var21.next();
                                       sf.getTags().remove(t);
                                    }
                                 }
                              }

                              u = (Unit)itU.next();
                           } while(!(u instanceof AssignStmt));

                           assign = (AssignStmt)u;
                           if (assign.getLeftOp() instanceof StaticFieldRef && assign.getRightOp() instanceof Constant) {
                              field = null;

                              try {
                                 field = ((StaticFieldRef)assign.getLeftOp()).getField();
                                 if (field != null && !nonConstantFields.contains(field)) {
                                    continue label147;
                                 }
                              } catch (ConflictingFieldRefException var16) {
                              }
                           } else if (assign.getLeftOp() instanceof StaticFieldRef) {
                              try {
                                 field = ((StaticFieldRef)assign.getLeftOp()).getField();
                                 if (field != null) {
                                    removeTagList.add(field);
                                 }
                              } catch (ConflictingFieldRefException var15) {
                              }
                           }
                        }
                     }
                  } while(!field.getDeclaringClass().equals(sc));
               } while(!field.isStatic());
            } while(!field.isFinal());

            boolean found = false;
            Iterator var12 = field.getTags().iterator();

            while(var12.hasNext()) {
               Tag t = (Tag)var12.next();
               if (t instanceof ConstantValueTag) {
                  if (this.checkConstantValue((ConstantValueTag)t, (Constant)assign.getRightOp())) {
                     if (removeAssignments) {
                        itU.remove();
                     }
                  } else {
                     logger.debug("WARNING: Constant value for field '" + field + "' mismatch between code (" + assign.getRightOp() + ") and constant table (" + t + ")");
                     removeTagList.add(field);
                  }

                  found = true;
                  break;
               }
            }

            if (!found) {
               if (!this.checkConstantValue((ConstantValueTag)newTags.get(field), (Constant)assign.getRightOp())) {
                  nonConstantFields.add(field);
                  newTags.remove(field);
                  removeTagList.add(field);
               } else {
                  ConstantValueTag newTag = this.createConstantTagFromValue((Constant)assign.getRightOp());
                  if (newTag != null) {
                     newTags.put(field, newTag);
                  }
               }
            }
         }
      }
   }

   private ConstantValueTag createConstantTagFromValue(Constant rightOp) {
      if (rightOp instanceof DoubleConstant) {
         return new DoubleConstantValueTag(((DoubleConstant)rightOp).value);
      } else if (rightOp instanceof FloatConstant) {
         return new FloatConstantValueTag(((FloatConstant)rightOp).value);
      } else if (rightOp instanceof IntConstant) {
         return new IntegerConstantValueTag(((IntConstant)rightOp).value);
      } else if (rightOp instanceof LongConstant) {
         return new LongConstantValueTag(((LongConstant)rightOp).value);
      } else {
         return rightOp instanceof StringConstant ? new StringConstantValueTag(((StringConstant)rightOp).value) : null;
      }
   }

   private boolean checkConstantValue(ConstantValueTag t, Constant rightOp) {
      if (t != null && rightOp != null) {
         if (t instanceof DoubleConstantValueTag) {
            if (!(rightOp instanceof DoubleConstant)) {
               return false;
            } else {
               return ((DoubleConstantValueTag)t).getDoubleValue() == ((DoubleConstant)rightOp).value;
            }
         } else if (t instanceof FloatConstantValueTag) {
            if (!(rightOp instanceof FloatConstant)) {
               return false;
            } else {
               return ((FloatConstantValueTag)t).getFloatValue() == ((FloatConstant)rightOp).value;
            }
         } else if (t instanceof IntegerConstantValueTag) {
            if (!(rightOp instanceof IntConstant)) {
               return false;
            } else {
               return ((IntegerConstantValueTag)t).getIntValue() == ((IntConstant)rightOp).value;
            }
         } else if (t instanceof LongConstantValueTag) {
            if (!(rightOp instanceof LongConstant)) {
               return false;
            } else {
               return ((LongConstantValueTag)t).getLongValue() == ((LongConstant)rightOp).value;
            }
         } else if (t instanceof StringConstantValueTag) {
            return !(rightOp instanceof StringConstant) ? false : ((StringConstantValueTag)t).getStringValue().equals(((StringConstant)rightOp).value);
         } else {
            return true;
         }
      } else {
         return true;
      }
   }
}
