package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JList;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ListFactory extends AbstractFactory implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204469L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204469 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$ListFactory;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JListMetaMethods;
   // $FF: synthetic field
   private static Class $class$javax$swing$JList;
   // $FF: synthetic field
   private static Class array$$class$java$lang$Object;

   public ListFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
      CallSite[] var5 = $getCallSiteArray();
      JList list = (JList)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$swing$JList());
      Object items = var5[0].call(attributes, (Object)"items");
      Object list;
      if (items instanceof Vector) {
         list = var5[1].callConstructor($get$$class$javax$swing$JList(), (Object)var5[2].call(attributes, (Object)"items"));
      } else {
         List l;
         if (items instanceof List) {
            l = (List)ScriptBytecodeAdapter.castToType(var5[3].call(attributes, (Object)"items"), $get$$class$java$util$List());
            list = var5[4].callConstructor($get$$class$javax$swing$JList(), (Object)var5[5].call(l));
         } else if (items instanceof Object[]) {
            list = var5[6].callConstructor($get$$class$javax$swing$JList(), (Object)var5[7].call(attributes, (Object)"items"));
         } else if (value instanceof JList) {
            list = (JList)ScriptBytecodeAdapter.castToType(value, $get$$class$javax$swing$JList());
         } else if (value instanceof Vector) {
            list = var5[8].callConstructor($get$$class$javax$swing$JList(), (Object)value);
         } else if (value instanceof List) {
            l = (List)ScriptBytecodeAdapter.castToType(value, $get$$class$java$util$List());
            list = var5[9].callConstructor($get$$class$javax$swing$JList(), (Object)var5[10].call(l));
         } else if (value instanceof Object[]) {
            list = var5[11].callConstructor($get$$class$javax$swing$JList(), (Object)value);
         } else {
            list = var5[12].callConstructor($get$$class$javax$swing$JList());
         }
      }

      var5[13].call($get$$class$groovy$swing$binding$JListMetaMethods(), (Object)list);
      return (Object)ScriptBytecodeAdapter.castToType(list, $get$$class$java$lang$Object());
   }

   public boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
      CallSite[] var4 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(var4[14].call(attributes, (Object)"listData"))) {
         Object listData = var4[15].call(attributes, (Object)"listData");
         if (DefaultTypeTransformation.booleanUnbox(!(listData instanceof Vector) && !(listData instanceof Object[]) ? Boolean.FALSE : Boolean.TRUE)) {
            ScriptBytecodeAdapter.setProperty(listData, $get$$class$groovy$swing$factory$ListFactory(), node, "listData");
         } else if (listData instanceof Collection) {
            ScriptBytecodeAdapter.setProperty(var4[16].call(listData), $get$$class$groovy$swing$factory$ListFactory(), node, "listData");
         } else {
            ScriptBytecodeAdapter.setProperty((Object[])ScriptBytecodeAdapter.asType(var4[17].call(listData, ScriptBytecodeAdapter.createList(new Object[0]), new GeneratedClosure(this, this) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$swing$factory$ListFactory$_onHandleNodeAttributes_closure1;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return itx.get();
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$swing$factory$ListFactory$_onHandleNodeAttributes_closure1()) {
                     return ScriptBytecodeAdapter.initMetaClass(this);
                  } else {
                     ClassInfo var1 = $staticClassInfo;
                     if (var1 == null) {
                        $staticClassInfo = var1 = ClassInfo.getClassInfo(this.getClass());
                     }

                     return var1.getMetaClass();
                  }
               }

               // $FF: synthetic method
               private static void $createCallSiteArray_1(String[] var0) {
                  var0[0] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[1];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$swing$factory$ListFactory$_onHandleNodeAttributes_closure1(), var0);
               }

               // $FF: synthetic method
               private static CallSite[] $getCallSiteArray() {
                  CallSiteArray var0;
                  if ($callSiteArray == null || (var0 = (CallSiteArray)$callSiteArray.get()) == null) {
                     var0 = $createCallSiteArray();
                     $callSiteArray = new SoftReference(var0);
                  }

                  return var0.array;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$swing$factory$ListFactory$_onHandleNodeAttributes_closure1() {
                  Class var10000 = $class$groovy$swing$factory$ListFactory$_onHandleNodeAttributes_closure1;
                  if (var10000 == null) {
                     var10000 = $class$groovy$swing$factory$ListFactory$_onHandleNodeAttributes_closure1 = class$("groovy.swing.factory.ListFactory$_onHandleNodeAttributes_closure1");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$java$lang$Object() {
                  Class var10000 = $class$java$lang$Object;
                  if (var10000 == null) {
                     var10000 = $class$java$lang$Object = class$("java.lang.Object");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               static Class class$(String var0) {
                  try {
                     return Class.forName(var0);
                  } catch (ClassNotFoundException var2) {
                     throw new NoClassDefFoundError(var2.getMessage());
                  }
               }
            }), $get$array$$class$java$lang$Object()), $get$$class$groovy$swing$factory$ListFactory(), node, "listData");
         }
      }

      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.TRUE, $get$$class$java$lang$Boolean()));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$ListFactory()) {
         return ScriptBytecodeAdapter.initMetaClass(this);
      } else {
         ClassInfo var1 = $staticClassInfo;
         if (var1 == null) {
            $staticClassInfo = var1 = ClassInfo.getClassInfo(this.getClass());
         }

         return var1.getMetaClass();
      }
   }

   // $FF: synthetic method
   public Object this$dist$invoke$3(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$swing$factory$ListFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$ListFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$ListFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public MetaClass getMetaClass() {
      MetaClass var10000 = this.metaClass;
      if (var10000 != null) {
         return var10000;
      } else {
         this.metaClass = this.$getStaticMetaClass();
         return this.metaClass;
      }
   }

   // $FF: synthetic method
   public void setMetaClass(MetaClass var1) {
      this.metaClass = var1;
   }

   // $FF: synthetic method
   public Object invokeMethod(String var1, Object var2) {
      return this.getMetaClass().invokeMethod(this, var1, var2);
   }

   // $FF: synthetic method
   public Object getProperty(String var1) {
      return this.getMetaClass().getProperty(this, var1);
   }

   // $FF: synthetic method
   public void setProperty(String var1, Object var2) {
      this.getMetaClass().setProperty(this, var1, var2);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public boolean super$2$isLeaf() {
      return super.isLeaf();
   }

   // $FF: synthetic method
   public void super$2$setChild(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.setChild(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$2$onNodeCompleted(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.onNodeCompleted(var1, var2, var3);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$2$onFactoryRegistration(FactoryBuilderSupport var1, String var2, String var3) {
      super.onFactoryRegistration(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$2$onNodeChildren(FactoryBuilderSupport var1, Object var2, Closure var3) {
      return super.onNodeChildren(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public boolean super$2$isHandlesNodeChildren() {
      return super.isHandlesNodeChildren();
   }

   // $FF: synthetic method
   public void super$2$setParent(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.setParent(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$2$onHandleNodeAttributes(FactoryBuilderSupport var1, Object var2, Map var3) {
      return super.onHandleNodeAttributes(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "get";
      var0[1] = "<$constructor$>";
      var0[2] = "remove";
      var0[3] = "remove";
      var0[4] = "<$constructor$>";
      var0[5] = "toArray";
      var0[6] = "<$constructor$>";
      var0[7] = "remove";
      var0[8] = "<$constructor$>";
      var0[9] = "<$constructor$>";
      var0[10] = "toArray";
      var0[11] = "<$constructor$>";
      var0[12] = "<$constructor$>";
      var0[13] = "enhanceMetaClass";
      var0[14] = "containsKey";
      var0[15] = "remove";
      var0[16] = "toArray";
      var0[17] = "collect";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[18];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$ListFactory(), var0);
   }

   // $FF: synthetic method
   private static CallSite[] $getCallSiteArray() {
      CallSiteArray var0;
      if ($callSiteArray == null || (var0 = (CallSiteArray)$callSiteArray.get()) == null) {
         var0 = $createCallSiteArray();
         $callSiteArray = new SoftReference(var0);
      }

      return var0.array;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$ListFactory() {
      Class var10000 = $class$groovy$swing$factory$ListFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$ListFactory = class$("groovy.swing.factory.ListFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$MetaClass() {
      Class var10000 = $class$groovy$lang$MetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Object() {
      Class var10000 = $class$java$lang$Object;
      if (var10000 == null) {
         var10000 = $class$java$lang$Object = class$("java.lang.Object");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Boolean() {
      Class var10000 = $class$java$lang$Boolean;
      if (var10000 == null) {
         var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$List() {
      Class var10000 = $class$java$util$List;
      if (var10000 == null) {
         var10000 = $class$java$util$List = class$("java.util.List");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$String() {
      Class var10000 = $class$java$lang$String;
      if (var10000 == null) {
         var10000 = $class$java$lang$String = class$("java.lang.String");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$JListMetaMethods() {
      Class var10000 = $class$groovy$swing$binding$JListMetaMethods;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JListMetaMethods = class$("groovy.swing.binding.JListMetaMethods");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JList() {
      Class var10000 = $class$javax$swing$JList;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JList = class$("javax.swing.JList");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$array$$class$java$lang$Object() {
      Class var10000 = array$$class$java$lang$Object;
      if (var10000 == null) {
         var10000 = array$$class$java$lang$Object = class$("[Ljava.lang.Object;");
      }

      return var10000;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
