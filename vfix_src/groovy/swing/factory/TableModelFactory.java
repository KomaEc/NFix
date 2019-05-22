package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.model.DefaultTableModel;
import groovy.model.ValueModel;
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import java.beans.PropertyChangeListener;
import java.lang.ref.SoftReference;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class TableModelFactory extends AbstractFactory implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204538L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204538 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$model$ValueModel;
   // $FF: synthetic field
   private static Class $class$groovy$model$DefaultTableModel;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$TableModelFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$beans$PropertyChangeListener;
   // $FF: synthetic field
   private static Class $class$java$beans$PropertyChangeEvent;
   // $FF: synthetic field
   private static Class $class$java$util$ArrayList;
   // $FF: synthetic field
   private static Class $class$javax$swing$table$TableModel;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$util$FactoryBuilderSupport;
   // $FF: synthetic field
   private static Class $class$groovy$model$ValueHolder;

   public TableModelFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
      CallSite[] var5 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(var5[0].call($get$$class$groovy$util$FactoryBuilderSupport(), value, name, $get$$class$javax$swing$table$TableModel()))) {
         return (Object)ScriptBytecodeAdapter.castToType(value, $get$$class$java$lang$Object());
      } else if (var5[1].call(attributes, (Object)name) instanceof TableModel) {
         return (Object)ScriptBytecodeAdapter.castToType(var5[2].call(attributes, (Object)name), $get$$class$java$lang$Object());
      } else {
         ValueModel model = (ValueModel)ScriptBytecodeAdapter.castToType(var5[3].call(attributes, (Object)"model"), $get$$class$groovy$model$ValueModel());
         if (ScriptBytecodeAdapter.compareEqual(model, (Object)null)) {
            Object list = var5[4].call(attributes, (Object)"list");
            if (ScriptBytecodeAdapter.compareEqual(list, (Object)null)) {
               list = var5[5].callConstructor($get$$class$java$util$ArrayList());
            }

            model = var5[6].callConstructor($get$$class$groovy$model$ValueHolder(), (Object)list);
         }

         return (Object)ScriptBytecodeAdapter.castToType(var5[7].callConstructor($get$$class$groovy$model$DefaultTableModel(), (Object)model), $get$$class$java$lang$Object());
      }
   }

   public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
      Object parent = new Reference(parent);
      CallSite[] var5 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareGreaterThan(var5[8].callGetProperty(node), $const$0) && parent.get() instanceof JTable ? Boolean.TRUE : Boolean.FALSE)) {
         ScriptBytecodeAdapter.setProperty(Boolean.FALSE, $get$$class$groovy$swing$factory$TableModelFactory(), parent.get(), "autoCreateColumnsFromModel");
         PropertyChangeListener listener = new Reference((PropertyChangeListener)ScriptBytecodeAdapter.asType(new GeneratedClosure(this, this) {
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$swing$factory$TableModelFactory$_onNodeCompleted_closure1;

            public {
               CallSite[] var3 = $getCallSiteArray();
            }

            public Object doCall(Object e) {
               Object ex = new Reference(e);
               CallSite[] var3 = $getCallSiteArray();
               if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(var3[0].callGetProperty(ex.get()), "model") && var3[1].callGetProperty(ex.get()) instanceof DefaultTableModel ? Boolean.TRUE : Boolean.FALSE)) {
                  ScriptBytecodeAdapter.setProperty(var3[2].callGetProperty(var3[3].callGetProperty(ex.get())), $get$$class$groovy$swing$factory$TableModelFactory$_onNodeCompleted_closure1(), var3[4].callGetProperty(ex.get()), "columnModel");
                  var3[5].call(var3[6].callGetProperty(ex.get()));
                  return var3[7].call(var3[8].callGetProperty(ex.get()));
               } else {
                  return null;
               }
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$swing$factory$TableModelFactory$_onNodeCompleted_closure1()) {
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
               var0[0] = "propertyName";
               var0[1] = "newValue";
               var0[2] = "columnModel";
               var0[3] = "newValue";
               var0[4] = "source";
               var0[5] = "revalidate";
               var0[6] = "source";
               var0[7] = "repaint";
               var0[8] = "source";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[9];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$swing$factory$TableModelFactory$_onNodeCompleted_closure1(), var0);
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
            private static Class $get$$class$groovy$swing$factory$TableModelFactory$_onNodeCompleted_closure1() {
               Class var10000 = $class$groovy$swing$factory$TableModelFactory$_onNodeCompleted_closure1;
               if (var10000 == null) {
                  var10000 = $class$groovy$swing$factory$TableModelFactory$_onNodeCompleted_closure1 = class$("groovy.swing.factory.TableModelFactory$_onNodeCompleted_closure1");
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
         }, $get$$class$java$beans$PropertyChangeListener()));
         var5[9].call(parent.get(), "model", listener.get());
         var5[10].call(builder, (Object)(new GeneratedClosure(this, this, listener, parent) {
            private Reference<T> listener;
            private Reference<T> parent;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$java$beans$PropertyChangeListener;
            // $FF: synthetic field
            private static Class $class$groovy$swing$factory$TableModelFactory$_onNodeCompleted_closure2;

            public {
               CallSite[] var5 = $getCallSiteArray();
               this.listener = (Reference)listener;
               this.parent = (Reference)parent;
            }

            public Object doCall(Object it) {
               CallSite[] var2 = $getCallSiteArray();
               return var2[0].call(this.parent.get(), "model", this.listener.get());
            }

            public PropertyChangeListener getListener() {
               CallSite[] var1 = $getCallSiteArray();
               return (PropertyChangeListener)ScriptBytecodeAdapter.castToType(this.listener.get(), $get$$class$java$beans$PropertyChangeListener());
            }

            public Object getParent() {
               CallSite[] var1 = $getCallSiteArray();
               return (Object)ScriptBytecodeAdapter.castToType(this.parent.get(), $get$$class$java$lang$Object());
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$swing$factory$TableModelFactory$_onNodeCompleted_closure2()) {
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
               var0[0] = "removePropertyChangeListener";
               var0[1] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[2];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$swing$factory$TableModelFactory$_onNodeCompleted_closure2(), var0);
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
            private static Class $get$$class$java$lang$Object() {
               Class var10000 = $class$java$lang$Object;
               if (var10000 == null) {
                  var10000 = $class$java$lang$Object = class$("java.lang.Object");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$java$beans$PropertyChangeListener() {
               Class var10000 = $class$java$beans$PropertyChangeListener;
               if (var10000 == null) {
                  var10000 = $class$java$beans$PropertyChangeListener = class$("java.beans.PropertyChangeListener");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$groovy$swing$factory$TableModelFactory$_onNodeCompleted_closure2() {
               Class var10000 = $class$groovy$swing$factory$TableModelFactory$_onNodeCompleted_closure2;
               if (var10000 == null) {
                  var10000 = $class$groovy$swing$factory$TableModelFactory$_onNodeCompleted_closure2 = class$("groovy.swing.factory.TableModelFactory$_onNodeCompleted_closure2");
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
         }));
         var5[11].call(listener.get(), var5[12].callConstructor($get$$class$java$beans$PropertyChangeEvent(), parent.get(), "model", (Object)null, node));
      }

   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$TableModelFactory()) {
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
      Class var10000 = $get$$class$groovy$swing$factory$TableModelFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$TableModelFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$TableModelFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "checkValueIsType";
      var0[1] = "get";
      var0[2] = "remove";
      var0[3] = "remove";
      var0[4] = "remove";
      var0[5] = "<$constructor$>";
      var0[6] = "<$constructor$>";
      var0[7] = "<$constructor$>";
      var0[8] = "columnCount";
      var0[9] = "addPropertyChangeListener";
      var0[10] = "addDisposalClosure";
      var0[11] = "propertyChange";
      var0[12] = "<$constructor$>";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[13];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$TableModelFactory(), var0);
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
   private static Class $get$$class$groovy$model$ValueModel() {
      Class var10000 = $class$groovy$model$ValueModel;
      if (var10000 == null) {
         var10000 = $class$groovy$model$ValueModel = class$("groovy.model.ValueModel");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$model$DefaultTableModel() {
      Class var10000 = $class$groovy$model$DefaultTableModel;
      if (var10000 == null) {
         var10000 = $class$groovy$model$DefaultTableModel = class$("groovy.model.DefaultTableModel");
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
   private static Class $get$$class$groovy$swing$factory$TableModelFactory() {
      Class var10000 = $class$groovy$swing$factory$TableModelFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$TableModelFactory = class$("groovy.swing.factory.TableModelFactory");
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
   private static Class $get$$class$java$beans$PropertyChangeListener() {
      Class var10000 = $class$java$beans$PropertyChangeListener;
      if (var10000 == null) {
         var10000 = $class$java$beans$PropertyChangeListener = class$("java.beans.PropertyChangeListener");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$beans$PropertyChangeEvent() {
      Class var10000 = $class$java$beans$PropertyChangeEvent;
      if (var10000 == null) {
         var10000 = $class$java$beans$PropertyChangeEvent = class$("java.beans.PropertyChangeEvent");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$ArrayList() {
      Class var10000 = $class$java$util$ArrayList;
      if (var10000 == null) {
         var10000 = $class$java$util$ArrayList = class$("java.util.ArrayList");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$table$TableModel() {
      Class var10000 = $class$javax$swing$table$TableModel;
      if (var10000 == null) {
         var10000 = $class$javax$swing$table$TableModel = class$("javax.swing.table.TableModel");
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
   private static Class $get$$class$groovy$util$FactoryBuilderSupport() {
      Class var10000 = $class$groovy$util$FactoryBuilderSupport;
      if (var10000 == null) {
         var10000 = $class$groovy$util$FactoryBuilderSupport = class$("groovy.util.FactoryBuilderSupport");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$model$ValueHolder() {
      Class var10000 = $class$groovy$model$ValueHolder;
      if (var10000 == null) {
         var10000 = $class$groovy$model$ValueHolder = class$("groovy.model.ValueHolder");
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
