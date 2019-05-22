package soot.jimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.ErroneousType;
import soot.FloatType;
import soot.G;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.PhaseOptions;
import soot.ShortType;
import soot.Singletons;
import soot.StmtAddressType;
import soot.Type;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.util.Chain;

public class LocalNameStandardizer extends BodyTransformer {
   public LocalNameStandardizer(Singletons.Global g) {
   }

   public static LocalNameStandardizer v() {
      return G.v().soot_jimple_toolkits_scalar_LocalNameStandardizer();
   }

   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      boolean onlyStackName = PhaseOptions.getBoolean(options, "only-stack-locals");
      boolean sortLocals = PhaseOptions.getBoolean(options, "sort-locals");
      BooleanType booleanType = BooleanType.v();
      ByteType byteType = ByteType.v();
      ShortType shortType = ShortType.v();
      CharType charType = CharType.v();
      IntType intType = IntType.v();
      LongType longType = LongType.v();
      DoubleType doubleType = DoubleType.v();
      FloatType floatType = FloatType.v();
      ErroneousType erroneousType = ErroneousType.v();
      UnknownType unknownType = UnknownType.v();
      StmtAddressType stmtAddressType = StmtAddressType.v();
      NullType nullType = NullType.v();
      int objectCount = 0;
      int intCount = 0;
      int longCount = 0;
      int floatCount = 0;
      int doubleCount = 0;
      int addressCount = 0;
      int errorCount = 0;
      int nullCount = 0;
      if (sortLocals) {
         Chain<Local> locals = body.getLocals();
         final List<ValueBox> defs = body.getDefBoxes();
         ArrayList<Local> sortedLocals = new ArrayList(locals);
         Collections.sort(sortedLocals, new Comparator<Local>() {
            private Map<Local, Integer> firstOccuranceCache = new HashMap();

            public int compare(Local arg0, Local arg1) {
               int ret = arg0.getType().toString().compareTo(arg1.getType().toString());
               if (ret == 0) {
                  ret = Integer.compare(this.getFirstOccurance(arg0), this.getFirstOccurance(arg1));
               }

               return ret;
            }

            private int getFirstOccurance(Local l) {
               Integer cur = (Integer)this.firstOccuranceCache.get(l);
               if (cur != null) {
                  return cur;
               } else {
                  int count = 0;
                  int first = -1;

                  for(Iterator var5 = defs.iterator(); var5.hasNext(); ++count) {
                     ValueBox vb = (ValueBox)var5.next();
                     Value v = vb.getValue();
                     if (v instanceof Local && v.equals(l)) {
                        first = count;
                        break;
                     }
                  }

                  this.firstOccuranceCache.put(l, first);
                  return first;
               }
            }
         });
         locals.clear();
         locals.addAll(sortedLocals);
      }

      Iterator var30 = body.getLocals().iterator();

      while(true) {
         while(true) {
            Local l;
            String prefix;
            do {
               if (!var30.hasNext()) {
                  return;
               }

               l = (Local)var30.next();
               prefix = "";
               if (l.getName().startsWith("$")) {
                  prefix = "$";
                  break;
               }
            } while(onlyStackName);

            Type type = l.getType();
            if (type.equals(booleanType)) {
               l.setName(prefix + "z" + intCount++);
            } else if (type.equals(byteType)) {
               l.setName(prefix + "b" + longCount++);
            } else if (type.equals(shortType)) {
               l.setName(prefix + "s" + longCount++);
            } else if (type.equals(charType)) {
               l.setName(prefix + "c" + longCount++);
            } else if (type.equals(intType)) {
               l.setName(prefix + "i" + longCount++);
            } else if (type.equals(longType)) {
               l.setName(prefix + "l" + longCount++);
            } else if (type.equals(doubleType)) {
               l.setName(prefix + "d" + doubleCount++);
            } else if (type.equals(floatType)) {
               l.setName(prefix + "f" + floatCount++);
            } else if (type.equals(stmtAddressType)) {
               l.setName(prefix + "a" + addressCount++);
            } else if (!type.equals(erroneousType) && !type.equals(unknownType)) {
               if (type.equals(nullType)) {
                  l.setName(prefix + "n" + nullCount++);
               } else {
                  l.setName(prefix + "r" + objectCount++);
               }
            } else {
               l.setName(prefix + "e" + errorCount++);
            }
         }
      }
   }
}
