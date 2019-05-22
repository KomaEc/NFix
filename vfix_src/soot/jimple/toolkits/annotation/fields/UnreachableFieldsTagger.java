package soot.jimple.toolkits.annotation.fields;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.G;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Value;
import soot.ValueBox;
import soot.jimple.FieldRef;
import soot.tagkit.ColorTag;
import soot.tagkit.StringTag;

public class UnreachableFieldsTagger extends SceneTransformer {
   public UnreachableFieldsTagger(Singletons.Global g) {
   }

   public static UnreachableFieldsTagger v() {
      return G.v().soot_jimple_toolkits_annotation_fields_UnreachableFieldsTagger();
   }

   protected void internalTransform(String phaseName, Map options) {
      ArrayList<SootField> fieldList = new ArrayList();
      Iterator getClassesIt = Scene.v().getApplicationClasses().iterator();

      SootClass appClass;
      Iterator mIt;
      while(getClassesIt.hasNext()) {
         appClass = (SootClass)getClassesIt.next();
         mIt = appClass.getFields().iterator();

         while(mIt.hasNext()) {
            SootField field = (SootField)mIt.next();
            fieldList.add(field);
         }
      }

      getClassesIt = Scene.v().getApplicationClasses().iterator();

      label58:
      while(getClassesIt.hasNext()) {
         appClass = (SootClass)getClassesIt.next();
         mIt = appClass.getMethods().iterator();

         while(true) {
            SootMethod sm;
            do {
               do {
                  if (!mIt.hasNext()) {
                     continue label58;
                  }

                  sm = (SootMethod)mIt.next();
               } while(!sm.hasActiveBody());
            } while(!Scene.v().getReachableMethods().contains(sm));

            Body b = sm.getActiveBody();
            Iterator usesIt = b.getUseBoxes().iterator();

            while(usesIt.hasNext()) {
               ValueBox vBox = (ValueBox)usesIt.next();
               Value v = vBox.getValue();
               if (v instanceof FieldRef) {
                  FieldRef fieldRef = (FieldRef)v;
                  SootField f = fieldRef.getField();
                  if (fieldList.contains(f)) {
                     int index = fieldList.indexOf(f);
                     fieldList.remove(index);
                  }
               }
            }
         }
      }

      Iterator unusedIt = fieldList.iterator();

      while(unusedIt.hasNext()) {
         SootField unusedField = (SootField)unusedIt.next();
         unusedField.addTag(new StringTag("Field " + unusedField.getName() + " is not used!", "Unreachable Fields"));
         unusedField.addTag(new ColorTag(0, true, "Unreachable Fields"));
      }

   }
}
