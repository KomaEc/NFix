package soot.dexpler;

import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.value.BooleanEncodedValue;
import org.jf.dexlib2.iface.value.ByteEncodedValue;
import org.jf.dexlib2.iface.value.CharEncodedValue;
import org.jf.dexlib2.iface.value.DoubleEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.FloatEncodedValue;
import org.jf.dexlib2.iface.value.IntEncodedValue;
import org.jf.dexlib2.iface.value.LongEncodedValue;
import org.jf.dexlib2.iface.value.ShortEncodedValue;
import org.jf.dexlib2.iface.value.StringEncodedValue;
import soot.Modifier;
import soot.Scene;
import soot.SootField;
import soot.Type;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.Tag;

public class DexField {
   private DexField() {
   }

   private static void addConstantTag(SootField df, Field sf) {
      Tag tag = null;
      EncodedValue ev = sf.getInitialValue();
      if (ev instanceof BooleanEncodedValue) {
         tag = new IntegerConstantValueTag(((BooleanEncodedValue)ev).getValue() ? 1 : 0);
      } else if (ev instanceof ByteEncodedValue) {
         tag = new IntegerConstantValueTag(((ByteEncodedValue)ev).getValue());
      } else if (ev instanceof CharEncodedValue) {
         tag = new IntegerConstantValueTag(((CharEncodedValue)ev).getValue());
      } else if (ev instanceof DoubleEncodedValue) {
         tag = new DoubleConstantValueTag(((DoubleEncodedValue)ev).getValue());
      } else if (ev instanceof FloatEncodedValue) {
         tag = new FloatConstantValueTag(((FloatEncodedValue)ev).getValue());
      } else if (ev instanceof IntEncodedValue) {
         tag = new IntegerConstantValueTag(((IntEncodedValue)ev).getValue());
      } else if (ev instanceof LongEncodedValue) {
         tag = new LongConstantValueTag(((LongEncodedValue)ev).getValue());
      } else if (ev instanceof ShortEncodedValue) {
         tag = new IntegerConstantValueTag(((ShortEncodedValue)ev).getValue());
      } else if (ev instanceof StringEncodedValue) {
         tag = new StringConstantValueTag(((StringEncodedValue)ev).getValue());
      }

      if (tag != null) {
         df.addTag((Tag)tag);
      }

   }

   public static SootField makeSootField(Field f) {
      String name = f.getName();
      Type type = DexType.toSoot(f.getType());
      int flags = f.getAccessFlags();
      SootField sf = Scene.v().makeSootField(name, type, flags);
      if (Modifier.isFinal(flags)) {
         addConstantTag(sf, f);
      }

      return sf;
   }
}
