package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.GString;
import groovy.lang.MetaClass;
import groovy.lang.MissingPropertyException;
import groovy.lang.Reference;
import groovy.util.FactoryBuilderSupport;
import java.awt.Component;
import java.awt.Window;
import java.lang.ref.SoftReference;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class TabbedPaneFactory extends BeanFactory {
   public static final String DELEGATE_PROPERTY_TITLE = (String)"_delegateProperty:title";
   public static final String DEFAULT_DELEGATE_PROPERTY_TITLE = (String)"title";
   public static final String DELEGATE_PROPERTY_TAB_ICON = (String)"_delegateProperty:tabIcon";
   public static final String DEFAULT_DELEGATE_PROPERTY_TAB_ICON = (String)"tabIcon";
   public static final String DELEGATE_PROPERTY_TAB_DISABLED_ICON = (String)"_delegateProperty:tabDisabledIcon";
   public static final String DEFAULT_DELEGATE_PROPERTY_TAB_DISABLED_ICON = (String)"tabDisabledIcon";
   public static final String DELEGATE_PROPERTY_TAB_TOOL_TIP = (String)"_delegateProperty:tabToolTip";
   public static final String DEFAULT_DELEGATE_PROPERTY_TAB_TOOL_TIP = (String)"tabToolTip";
   public static final String DELEGATE_PROPERTY_TAB_FOREGROUND = (String)"_delegateProperty:tabForeground";
   public static final String DEFAULT_DELEGATE_PROPERTY_TAB_FOREGROUND = (String)"tabForeground";
   public static final String DELEGATE_PROPERTY_TAB_BACKGROUND = (String)"_delegateProperty:tabBackground";
   public static final String DEFAULT_DELEGATE_PROPERTY_TAB_BACKGROUND = (String)"tabBackground";
   public static final String DELEGATE_PROPERTY_TAB_ENABLED = (String)"_delegateProperty:tabEnabled";
   public static final String DEFAULT_DELEGATE_PROPERTY_TAB_ENABLED = (String)"tabEnabled";
   public static final String DELEGATE_PROPERTY_TAB_MNEMONIC = (String)"_delegateProperty:tabMnemonic";
   public static final String DEFAULT_DELEGATE_PROPERTY_TAB_MNEMONIC = (String)"tabMnemonic";
   public static final String DELEGATE_PROPERTY_TAB_DISPLAYED_MNEMONIC_INDEX = (String)"_delegateProperty:tabDisplayedMnemonicIndex";
   public static final String DEFAULT_DELEGATE_PROPERTY_TAB_DISPLAYED_MNEMONIC_INDEX = (String)"tabDisplayedMnemonicIndex";
   public static final String CONTEXT_DATA_KEY = (String)"TabbdePaneFactoryData";
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)3;
   // $FF: synthetic field
   private static final Integer $const$3 = (Integer)2;
   // $FF: synthetic field
   private static final Integer $const$4 = (Integer)4;
   // $FF: synthetic field
   private static final Integer $const$5 = (Integer)5;
   // $FF: synthetic field
   private static final Integer $const$6 = (Integer)6;
   // $FF: synthetic field
   private static final Integer $const$7 = (Integer)7;
   // $FF: synthetic field
   private static final Integer $const$8 = (Integer)8;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205034L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205034 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$BeanFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$TabbedPaneFactory;

   public TabbedPaneFactory(Class beanClass) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{beanClass, Boolean.FALSE};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 2, $get$$class$groovy$swing$factory$BeanFactory());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super((Class)var10001[0]);
         break;
      case 1:
         super((Class)var10001[0], DefaultTypeTransformation.booleanUnbox(var10001[1]));
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
      FactoryBuilderSupport builder = new Reference(builder);
      CallSite[] var6 = $getCallSiteArray();
      Object newChild = new Reference(ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$swing$factory$BeanFactory(), this, "newInstance", new Object[]{builder.get(), name, value, attributes}));
      ScriptBytecodeAdapter.setProperty(new GeneratedClosure(this, this, builder, newChild) {
         private Reference<T> builder;
         private Reference<T> newChild;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$swing$factory$TabbedPaneFactory$_newInstance_closure1;
         // $FF: synthetic field
         private static Class $class$groovy$util$FactoryBuilderSupport;
         // $FF: synthetic field
         private static Class $class$groovy$swing$factory$TabbedPaneFactory;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.builder = (Reference)builder;
            this.newChild = (Reference)newChild;
         }

         public Object doCall(FactoryBuilderSupport cBuilder, Object cNode, Map cAttributesx) {
            FactoryBuilderSupport cBuilderx = new Reference(cBuilder);
            Object cNodex = new Reference(cNode);
            Map cAttributes = new Reference(cAttributesx);
            CallSite[] var7 = $getCallSiteArray();
            return ScriptBytecodeAdapter.compareEqual(var7[0].callGroovyObjectGetProperty(this.builder.get()), this.newChild.get()) ? var7[1].callStatic($get$$class$groovy$swing$factory$TabbedPaneFactory(), cBuilderx.get(), cNodex.get(), cAttributes.get()) : null;
         }

         public Object call(FactoryBuilderSupport cBuilder, Object cNode, Map cAttributesx) {
            FactoryBuilderSupport cBuilderx = new Reference(cBuilder);
            Object cNodex = new Reference(cNode);
            Map cAttributes = new Reference(cAttributesx);
            CallSite[] var7 = $getCallSiteArray();
            return var7[2].callCurrent(this, cBuilderx.get(), cNodex.get(), cAttributes.get());
         }

         public FactoryBuilderSupport getBuilder() {
            CallSite[] var1 = $getCallSiteArray();
            return (FactoryBuilderSupport)ScriptBytecodeAdapter.castToType(this.builder.get(), $get$$class$groovy$util$FactoryBuilderSupport());
         }

         public Object getNewChild() {
            CallSite[] var1 = $getCallSiteArray();
            return this.newChild.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$swing$factory$TabbedPaneFactory$_newInstance_closure1()) {
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
            var0[0] = "current";
            var0[1] = "inspectChild";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$swing$factory$TabbedPaneFactory$_newInstance_closure1(), var0);
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
         private static Class $get$$class$groovy$swing$factory$TabbedPaneFactory$_newInstance_closure1() {
            Class var10000 = $class$groovy$swing$factory$TabbedPaneFactory$_newInstance_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$factory$TabbedPaneFactory$_newInstance_closure1 = class$("groovy.swing.factory.TabbedPaneFactory$_newInstance_closure1");
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
         private static Class $get$$class$groovy$swing$factory$TabbedPaneFactory() {
            Class var10000 = $class$groovy$swing$factory$TabbedPaneFactory;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$factory$TabbedPaneFactory = class$("groovy.swing.factory.TabbedPaneFactory");
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
      }, $get$$class$groovy$swing$factory$TabbedPaneFactory(), var6[0].callGroovyObjectGetProperty(builder.get()), "tabbedPaneFactoryClosure");
      var6[1].call(builder.get(), var6[2].callGetProperty(var6[3].callGroovyObjectGetProperty(builder.get())));
      ScriptBytecodeAdapter.setProperty(var6[4].call(attributes, (Object)"selectedIndex"), $get$$class$groovy$swing$factory$TabbedPaneFactory(), var6[5].callGroovyObjectGetProperty(builder.get()), "selectedIndex");
      ScriptBytecodeAdapter.setProperty(var6[6].call(attributes, (Object)"selectedComponent"), $get$$class$groovy$swing$factory$TabbedPaneFactory(), var6[7].callGroovyObjectGetProperty(builder.get()), "selectedComponent");
      CallSite var10000 = var6[8];
      Object var10001 = var6[9].callGroovyObjectGetProperty(builder.get());
      String var10002 = DELEGATE_PROPERTY_TITLE;
      Object var10003 = var6[10].call(attributes, (Object)"titleProperty");
      if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
         var10003 = DEFAULT_DELEGATE_PROPERTY_TITLE;
      }

      Object var8 = var10003;
      var10000.call(var10001, var10002, var8);
      var10000 = var6[11];
      var10001 = var6[12].callGroovyObjectGetProperty(builder.get());
      var10002 = DELEGATE_PROPERTY_TAB_ICON;
      var10003 = var6[13].call(attributes, (Object)"tabIconProperty");
      if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
         var10003 = DEFAULT_DELEGATE_PROPERTY_TAB_ICON;
      }

      var8 = var10003;
      var10000.call(var10001, var10002, var8);
      var10000 = var6[14];
      var10001 = var6[15].callGroovyObjectGetProperty(builder.get());
      var10002 = DELEGATE_PROPERTY_TAB_DISABLED_ICON;
      var10003 = var6[16].call(attributes, (Object)"tabDisabledIconProperty");
      if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
         var10003 = DEFAULT_DELEGATE_PROPERTY_TAB_DISABLED_ICON;
      }

      var8 = var10003;
      var10000.call(var10001, var10002, var8);
      var10000 = var6[17];
      var10001 = var6[18].callGroovyObjectGetProperty(builder.get());
      var10002 = DELEGATE_PROPERTY_TAB_TOOL_TIP;
      var10003 = var6[19].call(attributes, (Object)"tabToolTipProperty");
      if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
         var10003 = DEFAULT_DELEGATE_PROPERTY_TAB_TOOL_TIP;
      }

      var8 = var10003;
      var10000.call(var10001, var10002, var8);
      var10000 = var6[20];
      var10001 = var6[21].callGroovyObjectGetProperty(builder.get());
      var10002 = DELEGATE_PROPERTY_TAB_BACKGROUND;
      var10003 = var6[22].call(attributes, (Object)"tabBackgroundProperty");
      if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
         var10003 = DEFAULT_DELEGATE_PROPERTY_TAB_BACKGROUND;
      }

      var8 = var10003;
      var10000.call(var10001, var10002, var8);
      var10000 = var6[23];
      var10001 = var6[24].callGroovyObjectGetProperty(builder.get());
      var10002 = DELEGATE_PROPERTY_TAB_FOREGROUND;
      var10003 = var6[25].call(attributes, (Object)"tabForegroundProperty");
      if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
         var10003 = DEFAULT_DELEGATE_PROPERTY_TAB_FOREGROUND;
      }

      var8 = var10003;
      var10000.call(var10001, var10002, var8);
      var10000 = var6[26];
      var10001 = var6[27].callGroovyObjectGetProperty(builder.get());
      var10002 = DELEGATE_PROPERTY_TAB_ENABLED;
      var10003 = var6[28].call(attributes, (Object)"tabEnabledProperty");
      if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
         var10003 = DEFAULT_DELEGATE_PROPERTY_TAB_ENABLED;
      }

      var8 = var10003;
      var10000.call(var10001, var10002, var8);
      var10000 = var6[29];
      var10001 = var6[30].callGroovyObjectGetProperty(builder.get());
      var10002 = DELEGATE_PROPERTY_TAB_MNEMONIC;
      var10003 = var6[31].call(attributes, (Object)"tabMnemonicProperty");
      if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
         var10003 = DEFAULT_DELEGATE_PROPERTY_TAB_MNEMONIC;
      }

      var8 = var10003;
      var10000.call(var10001, var10002, var8);
      var10000 = var6[32];
      var10001 = var6[33].callGroovyObjectGetProperty(builder.get());
      var10002 = DELEGATE_PROPERTY_TAB_DISPLAYED_MNEMONIC_INDEX;
      var10003 = var6[34].call(attributes, (Object)"tabDisplayedMnemonicIndexProperty");
      if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
         var10003 = DEFAULT_DELEGATE_PROPERTY_TAB_DISPLAYED_MNEMONIC_INDEX;
      }

      var8 = var10003;
      var10000.call(var10001, var10002, var8);
      return (Object)ScriptBytecodeAdapter.castToType(newChild.get(), $get$$class$java$lang$Object());
   }

   public static void inspectChild(FactoryBuilderSupport builder, Object node, Map attributes) {
      CallSite[] var3 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(node instanceof Component && !(node instanceof Window) ? Boolean.FALSE : Boolean.TRUE)) {
         CallSite var10000 = var3[35];
         Object var10002 = var3[36].callSafe(var3[37].callGroovyObjectGetPropertySafe(builder), (Object)DELEGATE_PROPERTY_TITLE);
         if (!DefaultTypeTransformation.booleanUnbox(var10002)) {
            var10002 = DEFAULT_DELEGATE_PROPERTY_TITLE;
         }

         Object name = var10000.call(attributes, (Object)var10002);
         var10000 = var3[38];
         var10002 = var3[39].callSafe(var3[40].callGroovyObjectGetPropertySafe(builder), (Object)DELEGATE_PROPERTY_TAB_ICON);
         if (!DefaultTypeTransformation.booleanUnbox(var10002)) {
            var10002 = DEFAULT_DELEGATE_PROPERTY_TAB_ICON;
         }

         Object icon = var10000.call(attributes, (Object)var10002);
         var10000 = var3[41];
         var10002 = var3[42].callSafe(var3[43].callGroovyObjectGetPropertySafe(builder), (Object)DELEGATE_PROPERTY_TAB_DISABLED_ICON);
         if (!DefaultTypeTransformation.booleanUnbox(var10002)) {
            var10002 = DEFAULT_DELEGATE_PROPERTY_TAB_DISABLED_ICON;
         }

         Object disabledIcon = var10000.call(attributes, (Object)var10002);
         var10000 = var3[44];
         var10002 = var3[45].callSafe(var3[46].callGroovyObjectGetPropertySafe(builder), (Object)DELEGATE_PROPERTY_TAB_TOOL_TIP);
         if (!DefaultTypeTransformation.booleanUnbox(var10002)) {
            var10002 = DEFAULT_DELEGATE_PROPERTY_TAB_TOOL_TIP;
         }

         Object toolTip = var10000.call(attributes, (Object)var10002);
         var10000 = var3[47];
         var10002 = var3[48].callSafe(var3[49].callGroovyObjectGetPropertySafe(builder), (Object)DELEGATE_PROPERTY_TAB_BACKGROUND);
         if (!DefaultTypeTransformation.booleanUnbox(var10002)) {
            var10002 = DEFAULT_DELEGATE_PROPERTY_TAB_BACKGROUND;
         }

         Object background = var10000.call(attributes, (Object)var10002);
         var10000 = var3[50];
         var10002 = var3[51].callSafe(var3[52].callGroovyObjectGetPropertySafe(builder), (Object)DELEGATE_PROPERTY_TAB_FOREGROUND);
         if (!DefaultTypeTransformation.booleanUnbox(var10002)) {
            var10002 = DEFAULT_DELEGATE_PROPERTY_TAB_FOREGROUND;
         }

         Object foreground = var10000.call(attributes, (Object)var10002);
         var10000 = var3[53];
         var10002 = var3[54].callSafe(var3[55].callGroovyObjectGetPropertySafe(builder), (Object)DELEGATE_PROPERTY_TAB_ENABLED);
         if (!DefaultTypeTransformation.booleanUnbox(var10002)) {
            var10002 = DEFAULT_DELEGATE_PROPERTY_TAB_ENABLED;
         }

         Object enabled = var10000.call(attributes, (Object)var10002);
         var10000 = var3[56];
         var10002 = var3[57].callSafe(var3[58].callGroovyObjectGetPropertySafe(builder), (Object)DELEGATE_PROPERTY_TAB_MNEMONIC);
         if (!DefaultTypeTransformation.booleanUnbox(var10002)) {
            var10002 = DEFAULT_DELEGATE_PROPERTY_TAB_MNEMONIC;
         }

         Object mnemonic = var10000.call(attributes, (Object)var10002);
         var10000 = var3[59];
         var10002 = var3[60].callSafe(var3[61].callGroovyObjectGetPropertySafe(builder), (Object)DELEGATE_PROPERTY_TAB_DISPLAYED_MNEMONIC_INDEX);
         if (!DefaultTypeTransformation.booleanUnbox(var10002)) {
            var10002 = DEFAULT_DELEGATE_PROPERTY_TAB_DISPLAYED_MNEMONIC_INDEX;
         }

         Object displayedMnemonicIndex = var10000.call(attributes, (Object)var10002);
         Object var14 = var3[62].call(var3[63].callGroovyObjectGetProperty(builder), (Object)CONTEXT_DATA_KEY);
         if (!DefaultTypeTransformation.booleanUnbox(var14)) {
            var14 = ScriptBytecodeAdapter.createMap(new Object[0]);
         }

         Object tabbedPaneContext = var14;
         if (DefaultTypeTransformation.booleanUnbox(var3[64].call(tabbedPaneContext))) {
            var3[65].call(var3[66].callGroovyObjectGetProperty(builder), CONTEXT_DATA_KEY, tabbedPaneContext);
         }

         var3[67].call(tabbedPaneContext, node, ScriptBytecodeAdapter.createList(new Object[]{name, icon, disabledIcon, toolTip, background, foreground, enabled, mnemonic, displayedMnemonicIndex}));
      }
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      CallSite[] var4 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(child instanceof Component && !(child instanceof Window) ? Boolean.FALSE : Boolean.TRUE)) {
         try {
            Object var10000 = var4[68].callSafe(var4[69].call(var4[70].callGroovyObjectGetProperty(builder), (Object)CONTEXT_DATA_KEY), child);
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = ScriptBytecodeAdapter.createList(new Object[]{null, null, null, null, null, null, null, null, null});
            }

            Object title = var10000;
            if (ScriptBytecodeAdapter.compareEqual(var4[71].call(title, (Object)$const$0), (Object)null)) {
               CallSite var13 = var4[72];
               Integer var10002 = $const$0;
               Object var6 = var4[73].callGetProperty(child);
               var13.call(title, var10002, var6);
            }

            var4[74].call(parent, var4[75].call(title, (Object)$const$0), var4[76].call(title, (Object)$const$1), child, var4[77].call(title, (Object)$const$2));
            Integer index = (Integer)ScriptBytecodeAdapter.castToType(var4[78].call(parent, child), $get$$class$java$lang$Integer());
            if (DefaultTypeTransformation.booleanUnbox(var4[79].call(title, (Object)$const$3))) {
               var4[80].call(parent, index, var4[81].call(title, (Object)$const$3));
            }

            if (DefaultTypeTransformation.booleanUnbox(var4[82].call(title, (Object)$const$4))) {
               var4[83].call(parent, index, var4[84].call(title, (Object)$const$4));
            }

            if (DefaultTypeTransformation.booleanUnbox(var4[85].call(title, (Object)$const$5))) {
               var4[86].call(parent, index, var4[87].call(title, (Object)$const$5));
            }

            if (ScriptBytecodeAdapter.compareNotEqual(var4[88].call(title, (Object)$const$6), (Object)null)) {
               var4[89].call(parent, index, var4[90].call(title, (Object)$const$6));
            }

            if (DefaultTypeTransformation.booleanUnbox(var4[91].call(title, (Object)$const$7))) {
               Object mnemonic = var4[92].call(title, (Object)$const$7);
               if (DefaultTypeTransformation.booleanUnbox(!(mnemonic instanceof String) && !(mnemonic instanceof GString) ? Boolean.FALSE : Boolean.TRUE)) {
                  var4[93].call(parent, index, ScriptBytecodeAdapter.createPojoWrapper((Integer)ScriptBytecodeAdapter.asType(var4[94].call(mnemonic, (Object)$const$0), $get$$class$java$lang$Integer()), Integer.TYPE));
               } else {
                  var4[95].call(parent, index, ScriptBytecodeAdapter.createPojoWrapper((Integer)ScriptBytecodeAdapter.asType(mnemonic, $get$$class$java$lang$Integer()), Integer.TYPE));
               }
            }

            if (DefaultTypeTransformation.booleanUnbox(var4[96].call(title, (Object)$const$8))) {
               var4[97].call(parent, index, var4[98].call(title, (Object)$const$8));
            }
         } catch (MissingPropertyException var10) {
            var4[99].call(parent, child);
         } finally {
            ;
         }

      }
   }

   public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
      CallSite[] var4 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$swing$factory$BeanFactory(), this, "onNodeCompleted", new Object[]{builder, parent, node});
      var4[100].call(builder, (Object)var4[101].callGetProperty(var4[102].callGroovyObjectGetProperty(builder)));
      if (ScriptBytecodeAdapter.compareNotEqual(var4[103].callGetProperty(var4[104].callGroovyObjectGetProperty(builder)), (Object)null)) {
         ScriptBytecodeAdapter.setProperty(var4[105].callGetProperty(var4[106].callGroovyObjectGetProperty(builder)), $get$$class$groovy$swing$factory$TabbedPaneFactory(), node, "selectedComponent");
      }

      if (ScriptBytecodeAdapter.compareNotEqual(var4[107].callGetProperty(var4[108].callGroovyObjectGetProperty(builder)), (Object)null)) {
         ScriptBytecodeAdapter.setProperty(var4[109].callGetProperty(var4[110].callGroovyObjectGetProperty(builder)), $get$$class$groovy$swing$factory$TabbedPaneFactory(), node, "selectedIndex");
      }

   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$TabbedPaneFactory()) {
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
   public Object this$dist$invoke$4(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$swing$factory$TabbedPaneFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$TabbedPaneFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$TabbedPaneFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object super$3$this$dist$get$3(String var1) {
      return super.this$dist$get$3(var1);
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public boolean super$3$isLeaf() {
      return super.isLeaf();
   }

   // $FF: synthetic method
   public void super$3$setProperty(String var1, Object var2) {
      super.setProperty(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$setChild(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.setChild(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$3$this$dist$set$3(String var1, Object var2) {
      super.this$dist$set$3(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$setParent(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.setParent(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public MetaClass super$3$getMetaClass() {
      return super.getMetaClass();
   }

   // $FF: synthetic method
   public void super$3$setMetaClass(MetaClass var1) {
      super.setMetaClass(var1);
   }

   // $FF: synthetic method
   public boolean super$2$onNodeChildren(FactoryBuilderSupport var1, Object var2, Closure var3) {
      return super.onNodeChildren(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$2$onHandleNodeAttributes(FactoryBuilderSupport var1, Object var2, Map var3) {
      return super.onHandleNodeAttributes(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object super$3$invokeMethod(String var1, Object var2) {
      return super.invokeMethod(var1, var2);
   }

   // $FF: synthetic method
   public Class super$3$getBeanClass() {
      return super.getBeanClass();
   }

   // $FF: synthetic method
   public Object super$3$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public Object super$3$newInstance(FactoryBuilderSupport var1, Object var2, Object var3, Map var4) {
      return super.newInstance(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public Object super$3$this$dist$invoke$3(String var1, Object var2) {
      return super.this$dist$invoke$3(var1, var2);
   }

   // $FF: synthetic method
   public MetaClass super$3$$getStaticMetaClass() {
      return super.$getStaticMetaClass();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "context";
      var0[1] = "addAttributeDelegate";
      var0[2] = "tabbedPaneFactoryClosure";
      var0[3] = "context";
      var0[4] = "remove";
      var0[5] = "context";
      var0[6] = "remove";
      var0[7] = "context";
      var0[8] = "putAt";
      var0[9] = "context";
      var0[10] = "remove";
      var0[11] = "putAt";
      var0[12] = "context";
      var0[13] = "remove";
      var0[14] = "putAt";
      var0[15] = "context";
      var0[16] = "remove";
      var0[17] = "putAt";
      var0[18] = "context";
      var0[19] = "remove";
      var0[20] = "putAt";
      var0[21] = "context";
      var0[22] = "remove";
      var0[23] = "putAt";
      var0[24] = "context";
      var0[25] = "remove";
      var0[26] = "putAt";
      var0[27] = "context";
      var0[28] = "remove";
      var0[29] = "putAt";
      var0[30] = "context";
      var0[31] = "remove";
      var0[32] = "putAt";
      var0[33] = "context";
      var0[34] = "remove";
      var0[35] = "remove";
      var0[36] = "getAt";
      var0[37] = "parentContext";
      var0[38] = "remove";
      var0[39] = "getAt";
      var0[40] = "parentContext";
      var0[41] = "remove";
      var0[42] = "getAt";
      var0[43] = "parentContext";
      var0[44] = "remove";
      var0[45] = "getAt";
      var0[46] = "parentContext";
      var0[47] = "remove";
      var0[48] = "getAt";
      var0[49] = "parentContext";
      var0[50] = "remove";
      var0[51] = "getAt";
      var0[52] = "parentContext";
      var0[53] = "remove";
      var0[54] = "getAt";
      var0[55] = "parentContext";
      var0[56] = "remove";
      var0[57] = "getAt";
      var0[58] = "parentContext";
      var0[59] = "remove";
      var0[60] = "getAt";
      var0[61] = "parentContext";
      var0[62] = "get";
      var0[63] = "context";
      var0[64] = "isEmpty";
      var0[65] = "put";
      var0[66] = "context";
      var0[67] = "put";
      var0[68] = "get";
      var0[69] = "getAt";
      var0[70] = "context";
      var0[71] = "getAt";
      var0[72] = "putAt";
      var0[73] = "name";
      var0[74] = "addTab";
      var0[75] = "getAt";
      var0[76] = "getAt";
      var0[77] = "getAt";
      var0[78] = "indexOfComponent";
      var0[79] = "getAt";
      var0[80] = "setDisabledIconAt";
      var0[81] = "getAt";
      var0[82] = "getAt";
      var0[83] = "setBackgroundAt";
      var0[84] = "getAt";
      var0[85] = "getAt";
      var0[86] = "setForegroundAt";
      var0[87] = "getAt";
      var0[88] = "getAt";
      var0[89] = "setEnabledAt";
      var0[90] = "getAt";
      var0[91] = "getAt";
      var0[92] = "getAt";
      var0[93] = "setMnemonicAt";
      var0[94] = "charAt";
      var0[95] = "setMnemonicAt";
      var0[96] = "getAt";
      var0[97] = "setDisplayedMnemonicIndexAt";
      var0[98] = "getAt";
      var0[99] = "add";
      var0[100] = "removeAttributeDelegate";
      var0[101] = "tabbedPaneFactoryClosure";
      var0[102] = "context";
      var0[103] = "selectedComponent";
      var0[104] = "context";
      var0[105] = "selectedComponent";
      var0[106] = "context";
      var0[107] = "selectedIndex";
      var0[108] = "context";
      var0[109] = "selectedIndex";
      var0[110] = "context";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[111];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$TabbedPaneFactory(), var0);
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
   private static Class $get$$class$java$lang$Integer() {
      Class var10000 = $class$java$lang$Integer;
      if (var10000 == null) {
         var10000 = $class$java$lang$Integer = class$("java.lang.Integer");
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
   private static Class $get$$class$groovy$swing$factory$BeanFactory() {
      Class var10000 = $class$groovy$swing$factory$BeanFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$BeanFactory = class$("groovy.swing.factory.BeanFactory");
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
   private static Class $get$$class$groovy$swing$factory$TabbedPaneFactory() {
      Class var10000 = $class$groovy$swing$factory$TabbedPaneFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$TabbedPaneFactory = class$("groovy.swing.factory.TabbedPaneFactory");
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
