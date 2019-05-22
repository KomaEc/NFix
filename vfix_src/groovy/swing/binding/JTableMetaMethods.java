package groovy.swing.binding;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JTableMetaMethods implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)-1;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)0;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203124L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203124 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$javax$swing$table$TableColumn;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$javax$swing$table$TableModel;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$AbstractSyntheticMetaMethods;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JTableMetaMethods;
   // $FF: synthetic field
   private static Class $class$javax$swing$table$TableColumnModel;

   public JTableMetaMethods() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public static void enhanceMetaClass(Object table) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$groovy$swing$binding$AbstractSyntheticMetaMethods(), table, ScriptBytecodeAdapter.createMap(new Object[]{"getElements", new GeneratedClosure($get$$class$groovy$swing$binding$JTableMetaMethods(), $get$$class$groovy$swing$binding$JTableMetaMethods()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$util$Collections;
         // $FF: synthetic field
         private static Class $class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure1;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            Object model = new Reference(var1[0].callGetProperty(var1[1].callGroovyObjectGetProperty(this)));
            if (model.get() instanceof DefaultTableModel) {
               return var1[2].call($get$$class$java$util$Collections(), (Object)var1[3].call(model.get()));
            } else {
               return model.get() instanceof groovy.model.DefaultTableModel ? var1[4].call($get$$class$java$util$Collections(), (Object)var1[5].callGetProperty(model.get())) : null;
            }
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure1()) {
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
            var0[0] = "model";
            var0[1] = "delegate";
            var0[2] = "unmodifiableList";
            var0[3] = "getDataVector";
            var0[4] = "unmodifiableList";
            var0[5] = "rows";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[6];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure1(), var0);
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
         private static Class $get$$class$java$util$Collections() {
            Class var10000 = $class$java$util$Collections;
            if (var10000 == null) {
               var10000 = $class$java$util$Collections = class$("java.util.Collections");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure1() {
            Class var10000 = $class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure1 = class$("groovy.swing.binding.JTableMetaMethods$_enhanceMetaClass_closure1");
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
      }, "getSelectedElement", new GeneratedClosure($get$$class$groovy$swing$binding$JTableMetaMethods(), $get$$class$groovy$swing$binding$JTableMetaMethods()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure2;
         // $FF: synthetic field
         private static Class $class$groovy$swing$binding$JTableMetaMethods;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callStatic($get$$class$groovy$swing$binding$JTableMetaMethods(), var1[1].callGroovyObjectGetProperty(this), var1[2].callGetProperty(var1[3].callGroovyObjectGetProperty(this)));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure2()) {
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
            var0[0] = "getElement";
            var0[1] = "delegate";
            var0[2] = "selectedRow";
            var0[3] = "delegate";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[4];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure2(), var0);
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
         private static Class $get$$class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure2() {
            Class var10000 = $class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure2;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure2 = class$("groovy.swing.binding.JTableMetaMethods$_enhanceMetaClass_closure2");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$swing$binding$JTableMetaMethods() {
            Class var10000 = $class$groovy$swing$binding$JTableMetaMethods;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$binding$JTableMetaMethods = class$("groovy.swing.binding.JTableMetaMethods");
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
      }, "getSelectedElements", new GeneratedClosure($get$$class$groovy$swing$binding$JTableMetaMethods(), $get$$class$groovy$swing$binding$JTableMetaMethods()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure3;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            Object myTable = new Reference(var1[0].callGroovyObjectGetProperty(this));
            return var1[1].call(var1[2].call(myTable.get()), (Object)(new GeneratedClosure(this, this.getThisObject(), myTable) {
               private Reference<T> myTable;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$swing$binding$JTableMetaMethods;
               // $FF: synthetic field
               private static Class $class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure3_closure4;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.myTable = (Reference)myTable;
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].callStatic($get$$class$groovy$swing$binding$JTableMetaMethods(), this.myTable.get(), itx.get());
               }

               public Object getMyTable() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.myTable.get();
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure3_closure4()) {
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
                  var0[0] = "getElement";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure3_closure4(), var0);
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
               private static Class $get$$class$groovy$swing$binding$JTableMetaMethods() {
                  Class var10000 = $class$groovy$swing$binding$JTableMetaMethods;
                  if (var10000 == null) {
                     var10000 = $class$groovy$swing$binding$JTableMetaMethods = class$("groovy.swing.binding.JTableMetaMethods");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure3_closure4() {
                  Class var10000 = $class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure3_closure4;
                  if (var10000 == null) {
                     var10000 = $class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure3_closure4 = class$("groovy.swing.binding.JTableMetaMethods$_enhanceMetaClass_closure3_closure4");
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
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure3()) {
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
            var0[0] = "delegate";
            var0[1] = "collect";
            var0[2] = "getSelectedRows";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure3(), var0);
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
         private static Class $get$$class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure3() {
            Class var10000 = $class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure3;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$binding$JTableMetaMethods$_enhanceMetaClass_closure3 = class$("groovy.swing.binding.JTableMetaMethods$_enhanceMetaClass_closure3");
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
      }}));
   }

   public static Object getElement(JTable table, int row) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(DefaultTypeTransformation.box(row), $const$0)) {
         return (Object)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Object());
      } else {
         TableModel model = (TableModel)ScriptBytecodeAdapter.castToType(var2[1].callGetProperty(table), $get$$class$javax$swing$table$TableModel());
         if (!(model instanceof DefaultTableModel)) {
            if (model instanceof groovy.model.DefaultTableModel) {
               Object rowValue = var2[10].callGetProperty(var2[11].call(model));
               return ScriptBytecodeAdapter.compareEqual(rowValue, (Object)null) ? (Object)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Object()) : (Object)ScriptBytecodeAdapter.castToType(var2[12].call(var2[13].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), (Object)rowValue), DefaultTypeTransformation.box(row)), $get$$class$java$lang$Object());
            } else {
               return (Object)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Object());
            }
         } else {
            Map value = ScriptBytecodeAdapter.createMap(new Object[0]);
            TableColumnModel cmodel = (TableColumnModel)ScriptBytecodeAdapter.castToType(var2[2].callGetProperty(table), $get$$class$javax$swing$table$TableColumnModel());

            for(Object i = $const$1; ScriptBytecodeAdapter.compareLessThan(i, var2[3].call(cmodel)); i = var2[9].call(i)) {
               TableColumn c = (TableColumn)ScriptBytecodeAdapter.castToType(var2[4].call(cmodel, (Object)i), $get$$class$javax$swing$table$TableColumn());
               var2[5].call(value, var2[6].call(c), var2[7].call(table, DefaultTypeTransformation.box(row), var2[8].call(c)));
            }

            return (Object)ScriptBytecodeAdapter.castToType(value, $get$$class$java$lang$Object());
         }
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$binding$JTableMetaMethods()) {
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
   public Object this$dist$invoke$2(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$swing$binding$JTableMetaMethods();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$binding$JTableMetaMethods(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$binding$JTableMetaMethods(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public void super$1$wait(long var1) {
      super.wait(var1);
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
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "enhance";
      var0[1] = "model";
      var0[2] = "columnModel";
      var0[3] = "getColumnCount";
      var0[4] = "getColumn";
      var0[5] = "put";
      var0[6] = "getIdentifier";
      var0[7] = "getValueAt";
      var0[8] = "getModelIndex";
      var0[9] = "next";
      var0[10] = "value";
      var0[11] = "getRowsModel";
      var0[12] = "getAt";
      var0[13] = "asList";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[14];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$binding$JTableMetaMethods(), var0);
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
   private static Class $get$$class$javax$swing$table$TableColumn() {
      Class var10000 = $class$javax$swing$table$TableColumn;
      if (var10000 == null) {
         var10000 = $class$javax$swing$table$TableColumn = class$("javax.swing.table.TableColumn");
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
   private static Class $get$$class$org$codehaus$groovy$runtime$InvokerHelper() {
      Class var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper = class$("org.codehaus.groovy.runtime.InvokerHelper");
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
   private static Class $get$$class$groovy$swing$binding$AbstractSyntheticMetaMethods() {
      Class var10000 = $class$groovy$swing$binding$AbstractSyntheticMetaMethods;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$AbstractSyntheticMetaMethods = class$("groovy.swing.binding.AbstractSyntheticMetaMethods");
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
   private static Class $get$$class$groovy$swing$binding$JTableMetaMethods() {
      Class var10000 = $class$groovy$swing$binding$JTableMetaMethods;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JTableMetaMethods = class$("groovy.swing.binding.JTableMetaMethods");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$table$TableColumnModel() {
      Class var10000 = $class$javax$swing$table$TableColumnModel;
      if (var10000 == null) {
         var10000 = $class$javax$swing$table$TableColumnModel = class$("javax.swing.table.TableColumnModel");
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
