package polyglot.util.typedump;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.TypeEncoder;

class TypeDumper {
   static Set dontExpand;
   Type theType;
   String rawName;
   String compilerVersion;
   Date timestamp;
   // $FF: synthetic field
   static Class class$java$lang$Void;
   // $FF: synthetic field
   static Class class$java$lang$Boolean;
   // $FF: synthetic field
   static Class class$java$lang$Short;
   // $FF: synthetic field
   static Class class$java$lang$Integer;
   // $FF: synthetic field
   static Class class$java$lang$Long;
   // $FF: synthetic field
   static Class class$java$lang$Float;
   // $FF: synthetic field
   static Class class$java$lang$Double;
   // $FF: synthetic field
   static Class class$java$lang$Class;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$lang$Object;

   TypeDumper(String rawName, Type t, String compilerVersion, Long timestamp) {
      this.theType = t;
      this.rawName = rawName;
      this.compilerVersion = compilerVersion;
      this.timestamp = new Date(timestamp);
   }

   public static TypeDumper load(String name, TypeSystem ts) throws ClassNotFoundException, NoSuchFieldException, IOException, SecurityException {
      Class c = Class.forName(name);

      try {
         Field jlcVersion = c.getDeclaredField("jlc$CompilerVersion");
         Field jlcTimestamp = c.getDeclaredField("jlc$SourceLastModified");
         Field jlcType = c.getDeclaredField("jlc$ClassType");
         String t = (String)jlcType.get((Object)null);
         TypeEncoder te = new TypeEncoder(ts);
         return new TypeDumper(name, te.decode(t), (String)jlcVersion.get((Object)null), (Long)jlcTimestamp.get((Object)null));
      } catch (IllegalAccessException var8) {
         throw new SecurityException("illegal access: " + var8.getMessage());
      }
   }

   public void dump(CodeWriter w) {
      Map cache = new HashMap();
      cache.put(this.theType, this.theType);
      w.write("Type " + this.rawName + " {");
      w.allowBreak(2);
      w.begin(0);
      w.write("Compiled with polyglot version " + this.compilerVersion + ".  ");
      w.allowBreak(0);
      w.write("Last modified: " + this.timestamp.toString() + ".  ");
      w.allowBreak(0);
      w.write(this.theType.toString());
      w.allowBreak(4);
      w.write("<" + this.theType.getClass().toString() + ">");
      w.allowBreak(0);
      this.dumpObject(w, this.theType, cache);
      w.allowBreak(0);
      w.end();
      w.allowBreak(0);
      w.write("}");
      w.newline(0);
   }

   protected void dumpObject(CodeWriter w, Object obj, Map cache) {
      w.write(" fields {");
      w.allowBreak(2);
      w.begin(0);

      try {
         Field[] declaredFields = obj.getClass().getDeclaredFields();
         AccessibleObject.setAccessible(declaredFields, true);

         for(int i = 0; i < declaredFields.length; ++i) {
            if (!Modifier.isStatic(declaredFields[i].getModifiers())) {
               w.begin(4);
               w.write(declaredFields[i].getName() + ": ");
               w.allowBreak(0);

               try {
                  Object o = declaredFields[i].get(obj);
                  if (o != null) {
                     Class rtType = o.getClass();
                     w.write("<" + rtType.toString() + ">:");
                     w.allowBreak(0);
                     w.write(o.toString());
                     w.allowBreak(4);
                     if (!(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object).equals(rtType) && !dontDump(rtType) && !rtType.isArray() && (!cache.containsKey(o) || cache.get(o) != o)) {
                        cache.put(o, o);
                        this.dumpObject(w, o, cache);
                     }
                  } else {
                     w.write("null");
                  }
               } catch (IllegalAccessException var13) {
                  w.write("##[" + var13.getMessage() + "]");
               }

               w.end();
               w.allowBreak(0);
            }
         }
      } catch (SecurityException var14) {
      } finally {
         w.end();
         w.allowBreak(0);
         w.write("}");
      }

   }

   static boolean dontDump(Class c) {
      return dontExpand.contains(c);
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      Object[] primitiveLike = new Object[]{class$java$lang$Void == null ? (class$java$lang$Void = class$("java.lang.Void")) : class$java$lang$Void, class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean, class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short, class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer, class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long, class$java$lang$Float == null ? (class$java$lang$Float = class$("java.lang.Float")) : class$java$lang$Float, class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double, class$java$lang$Class == null ? (class$java$lang$Class = class$("java.lang.Class")) : class$java$lang$Class, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String};
      dontExpand = new HashSet(Arrays.asList(primitiveLike));
   }
}
