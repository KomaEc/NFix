package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.groovy.binding.AggregateBinding;
import org.codehaus.groovy.binding.BindingUpdatable;
import org.codehaus.groovy.binding.ClosureTriggerBinding;
import org.codehaus.groovy.binding.FullBinding;
import org.codehaus.groovy.binding.MutualPropertyBinding;
import org.codehaus.groovy.binding.PropertyBinding;
import org.codehaus.groovy.binding.SourceBinding;
import org.codehaus.groovy.binding.TargetBinding;
import org.codehaus.groovy.binding.TriggerBinding;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class BindFactory extends AbstractFactory implements GroovyObject {
   public static final String CONTEXT_DATA_KEY = (String)"BindFactoryData";
   private final Map<String, TriggerBinding> syntheticBindings;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204384L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204384 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JComponentProperties;
   // $FF: synthetic field
   private static Class $class$java$lang$RuntimeException;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JSpinnerProperties;
   // $FF: synthetic field
   private static Class $class$groovy$util$AbstractFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$util$Map;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JComboBoxProperties;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$binding$PropertyBinding;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JTableProperties;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$AbstractButtonProperties;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$BindFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JTextComponentProperties;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$binding$ClosureSourceBinding;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JListProperties;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$binding$ClosureTriggerBinding;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$binding$SourceBinding;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$binding$EventTriggerBinding;
   // $FF: synthetic field
   private static Class $class$java$lang$Class;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$binding$FullBinding;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Closure;
   // $FF: synthetic field
   private static Class $class$java$util$Iterator;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JSliderProperties;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$java$util$Map$Entry;
   // $FF: synthetic field
   private static Class $class$groovy$swing$SwingBuilder;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$binding$TargetBinding;
   // $FF: synthetic field
   private static Class $class$java$util$HashMap;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$binding$MutualPropertyBinding;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$binding$TriggerBinding;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JScrollBarProperties;

   public BindFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      this.syntheticBindings = (Map)ScriptBytecodeAdapter.castToType(var1[0].callConstructor($get$$class$java$util$HashMap()), $get$$class$java$util$Map());
      var1[1].call(this.syntheticBindings, (Object)var1[2].callGetProperty($get$$class$groovy$swing$binding$JTextComponentProperties()));
      var1[3].call(this.syntheticBindings, (Object)var1[4].callGetProperty($get$$class$groovy$swing$binding$AbstractButtonProperties()));
      var1[5].call(this.syntheticBindings, (Object)var1[6].callGetProperty($get$$class$groovy$swing$binding$JSliderProperties()));
      var1[7].call(this.syntheticBindings, (Object)var1[8].callGetProperty($get$$class$groovy$swing$binding$JScrollBarProperties()));
      var1[9].call(this.syntheticBindings, (Object)var1[10].callGetProperty($get$$class$groovy$swing$binding$JComboBoxProperties()));
      var1[11].call(this.syntheticBindings, (Object)var1[12].callGetProperty($get$$class$groovy$swing$binding$JListProperties()));
      var1[13].call(this.syntheticBindings, (Object)var1[14].callGetProperty($get$$class$groovy$swing$binding$JSpinnerProperties()));
      var1[15].call(this.syntheticBindings, (Object)var1[16].callGetProperty($get$$class$groovy$swing$binding$JTableProperties()));
      var1[17].call(this.syntheticBindings, (Object)var1[18].callGetProperty($get$$class$groovy$swing$binding$JComponentProperties()));
   }

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
      CallSite[] var5 = $getCallSiteArray();
      Object source = var5[19].call(attributes, (Object)"source");
      Object target = var5[20].call(attributes, (Object)"target");
      Object var10000 = var5[21].call(var5[22].callGroovyObjectGetProperty(builder), (Object)CONTEXT_DATA_KEY);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = ScriptBytecodeAdapter.createMap(new Object[0]);
      }

      Map bindContext = (Map)ScriptBytecodeAdapter.castToType(var10000, $get$$class$java$util$Map());
      if (DefaultTypeTransformation.booleanUnbox(var5[23].call(bindContext))) {
         var5[24].call(var5[25].callGroovyObjectGetProperty(builder), CONTEXT_DATA_KEY, bindContext);
      }

      TargetBinding tb = (TargetBinding)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$org$codehaus$groovy$binding$TargetBinding());
      Boolean sea;
      if (ScriptBytecodeAdapter.compareNotEqual(target, (Object)null)) {
         var10000 = var5[26].call(attributes, (Object)"targetProperty");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = value;
         }

         String targetProperty = (String)ScriptBytecodeAdapter.castToType(var10000, $get$$class$java$lang$String());
         tb = var5[27].callConstructor($get$$class$org$codehaus$groovy$binding$PropertyBinding(), target, targetProperty);
         if (ScriptBytecodeAdapter.compareEqual(source, (Object)null)) {
            sea = null;
            Object result;
            if (DefaultTypeTransformation.booleanUnbox(var5[28].call(attributes, (Object)"mutual"))) {
               result = var5[29].callConstructor($get$$class$org$codehaus$groovy$binding$MutualPropertyBinding(), (Object)null, (Object)null, tb, ScriptBytecodeAdapter.getMethodPointer(this, "getTriggerBinding"));
            } else {
               result = tb;
            }

            Object newAttributes = ScriptBytecodeAdapter.createMap(new Object[0]);
            var5[30].call(newAttributes, (Object)attributes);
            var5[31].call(bindContext, result, newAttributes);
            var5[32].call(attributes);
            return (Object)ScriptBytecodeAdapter.castToType(result, $get$$class$java$lang$Object());
         }
      }

      FullBinding fb = (FullBinding)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$org$codehaus$groovy$binding$FullBinding());
      sea = (Boolean)ScriptBytecodeAdapter.castToType(var5[33].call(attributes, (Object)"sourceEvent"), $get$$class$java$lang$Boolean());
      Boolean sva = (Boolean)ScriptBytecodeAdapter.castToType(var5[34].call(attributes, (Object)"sourceValue"), $get$$class$java$lang$Boolean());
      Boolean spa = (Boolean)ScriptBytecodeAdapter.castToType(!DefaultTypeTransformation.booleanUnbox(var5[35].call(attributes, (Object)"sourceProperty")) && !DefaultTypeTransformation.booleanUnbox(value) ? Boolean.FALSE : Boolean.TRUE, $get$$class$java$lang$Boolean());
      Object pb;
      Object sb;
      Object fb;
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(sea) && DefaultTypeTransformation.booleanUnbox(sva) ? Boolean.TRUE : Boolean.FALSE) && !DefaultTypeTransformation.booleanUnbox(spa) ? Boolean.TRUE : Boolean.FALSE)) {
         Closure queryValue = (Closure)ScriptBytecodeAdapter.castToType(var5[36].call(attributes, (Object)"sourceValue"), $get$$class$groovy$lang$Closure());
         pb = var5[37].callConstructor($get$$class$org$codehaus$groovy$binding$ClosureSourceBinding(), (Object)queryValue);
         String trigger = (String)ScriptBytecodeAdapter.castToType(var5[38].call(attributes, (Object)"sourceEvent"), $get$$class$java$lang$String());
         sb = var5[39].callConstructor($get$$class$org$codehaus$groovy$binding$EventTriggerBinding(), source, trigger);
         fb = (FullBinding)ScriptBytecodeAdapter.castToType(var5[40].call(sb, pb, tb), $get$$class$org$codehaus$groovy$binding$FullBinding());
      } else {
         if (!DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(spa) && !DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(sea) && DefaultTypeTransformation.booleanUnbox(sva) ? Boolean.TRUE : Boolean.FALSE) ? Boolean.TRUE : Boolean.FALSE)) {
            if (!DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(sea) && !DefaultTypeTransformation.booleanUnbox(sva) ? Boolean.FALSE : Boolean.TRUE) && !DefaultTypeTransformation.booleanUnbox(spa) ? Boolean.FALSE : Boolean.TRUE)) {
               Object newAttributes = ScriptBytecodeAdapter.createMap(new Object[0]);
               var5[51].call(newAttributes, (Object)attributes);
               var5[52].call(bindContext, tb, newAttributes);
               var5[53].call(attributes);
               return (Object)ScriptBytecodeAdapter.castToType(var5[54].callConstructor($get$$class$org$codehaus$groovy$binding$ClosureTriggerBinding(), (Object)this.syntheticBindings), $get$$class$java$lang$Object());
            }

            throw (Throwable)var5[55].callConstructor($get$$class$java$lang$RuntimeException(), (Object)"Both sourceEvent: and sourceValue: cannot be specified along with sourceProperty: or a value argument");
         }

         var10000 = var5[41].call(attributes, (Object)"sourceProperty");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = value;
         }

         String property = (String)ScriptBytecodeAdapter.castToType(var10000, $get$$class$java$lang$String());
         pb = var5[42].callConstructor($get$$class$org$codehaus$groovy$binding$PropertyBinding(), source, property);
         TriggerBinding trigger = (TriggerBinding)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$org$codehaus$groovy$binding$TriggerBinding());
         Object trigger;
         if (DefaultTypeTransformation.booleanUnbox(sea)) {
            String triggerName = (String)ScriptBytecodeAdapter.castToType(var5[43].call(attributes, (Object)"sourceEvent"), $get$$class$java$lang$String());
            trigger = var5[44].callConstructor($get$$class$org$codehaus$groovy$binding$EventTriggerBinding(), source, triggerName);
         } else {
            trigger = (TriggerBinding)ScriptBytecodeAdapter.castToType(var5[45].callCurrent(this, (Object)pb), $get$$class$org$codehaus$groovy$binding$TriggerBinding());
         }

         SourceBinding sb = (SourceBinding)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$org$codehaus$groovy$binding$SourceBinding());
         if (DefaultTypeTransformation.booleanUnbox(sva)) {
            Closure queryValue = (Closure)ScriptBytecodeAdapter.castToType(var5[46].call(attributes, (Object)"sourceValue"), $get$$class$groovy$lang$Closure());
            sb = var5[47].callConstructor($get$$class$org$codehaus$groovy$binding$ClosureSourceBinding(), (Object)queryValue);
         } else {
            sb = pb;
         }

         if (DefaultTypeTransformation.booleanUnbox(var5[48].call(attributes, (Object)"mutual"))) {
            fb = var5[49].callConstructor($get$$class$org$codehaus$groovy$binding$MutualPropertyBinding(), trigger, sb, tb, ScriptBytecodeAdapter.getMethodPointer(this, "getTriggerBinding"));
         } else {
            fb = (FullBinding)ScriptBytecodeAdapter.castToType(var5[50].call(trigger, sb, tb), $get$$class$org$codehaus$groovy$binding$FullBinding());
         }
      }

      if (DefaultTypeTransformation.booleanUnbox(var5[56].call(attributes, (Object)"value"))) {
         var5[57].call(bindContext, fb, ScriptBytecodeAdapter.createMap(new Object[]{"value", var5[58].call(attributes, (Object)"value")}));
      }

      Object o = var5[59].call(attributes, (Object)"bind");
      if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(o, (Object)null) && !DefaultTypeTransformation.booleanUnbox(var5[60].call(attributes, (Object)"group")) ? Boolean.TRUE : Boolean.FALSE) && !DefaultTypeTransformation.booleanUnbox(o instanceof Boolean && DefaultTypeTransformation.booleanUnbox(var5[61].call((Boolean)ScriptBytecodeAdapter.castToType(o, $get$$class$java$lang$Boolean()))) ? Boolean.TRUE : Boolean.FALSE) ? Boolean.FALSE : Boolean.TRUE)) {
         var5[62].call(fb);
      }

      if (DefaultTypeTransformation.booleanUnbox(var5[63].callGetProperty(attributes) instanceof AggregateBinding && fb instanceof BindingUpdatable ? Boolean.TRUE : Boolean.FALSE)) {
         var5[64].call(var5[65].call(attributes, (Object)"group"), fb);
      }

      var5[66].call(builder, (Object)ScriptBytecodeAdapter.getMethodPointer(fb, "unbind"));
      return (Object)ScriptBytecodeAdapter.castToType(fb, $get$$class$java$lang$Object());
   }

   public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
      CallSite[] var4 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$util$AbstractFactory(), this, "onNodeCompleted", new Object[]{builder, parent, node});
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(node instanceof FullBinding && DefaultTypeTransformation.booleanUnbox(var4[67].callGetProperty(node)) ? Boolean.TRUE : Boolean.FALSE) && DefaultTypeTransformation.booleanUnbox(var4[68].callGetProperty(node)) ? Boolean.TRUE : Boolean.FALSE)) {
         try {
            var4[69].call(node);
         } catch (Exception var17) {
         } finally {
            ;
         }

         try {
            var4[70].call(node);
         } catch (Exception var15) {
         } finally {
            ;
         }
      }

   }

   public boolean isLeaf() {
      CallSite[] var1 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
   }

   public boolean isHandlesNodeChildren() {
      CallSite[] var1 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.TRUE, $get$$class$java$lang$Boolean()));
   }

   public boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
      CallSite[] var4 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(node instanceof FullBinding && ScriptBytecodeAdapter.compareEqual(var4[71].callGetProperty(node), (Object)null) ? Boolean.TRUE : Boolean.FALSE)) {
         ScriptBytecodeAdapter.setProperty(childContent, $get$$class$groovy$swing$factory$BindFactory(), node, "converter");
         return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
      } else if (node instanceof ClosureTriggerBinding) {
         ScriptBytecodeAdapter.setProperty(childContent, $get$$class$groovy$swing$factory$BindFactory(), node, "closure");
         return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
      } else {
         if (node instanceof TriggerBinding) {
            Object var10000 = var4[72].call(var4[73].call(var4[74].callGroovyObjectGetProperty(builder), (Object)CONTEXT_DATA_KEY), node);
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = ScriptBytecodeAdapter.createMap(new Object[0]);
            }

            Object bindAttrs = var10000;
            if (!DefaultTypeTransformation.booleanUnbox(var4[75].call(bindAttrs, (Object)"converter"))) {
               var4[76].call(bindAttrs, "converter", childContent);
               return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
            }
         }

         throw (Throwable)var4[77].callConstructor($get$$class$java$lang$RuntimeException(), (Object)"Binding nodes do not accept child content when a converter is already specified");
      }
   }

   public TriggerBinding getTriggerBinding(PropertyBinding psb) {
      CallSite[] var2 = $getCallSiteArray();
      String property = (String)ScriptBytecodeAdapter.castToType(var2[78].callGetProperty(psb), $get$$class$java$lang$String());

      for(Class currentClass = (Class)ScriptBytecodeAdapter.castToType(var2[79].call(var2[80].callGetProperty(psb)), $get$$class$java$lang$Class()); ScriptBytecodeAdapter.compareNotEqual(currentClass, (Object)null); currentClass = (Class)ScriptBytecodeAdapter.castToType(var2[83].call(currentClass), $get$$class$java$lang$Class())) {
         Object trigger = (TriggerBinding)ScriptBytecodeAdapter.castToType(var2[81].call(this.syntheticBindings, (Object)ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.asType(new GStringImpl(new Object[]{var2[82].callGetProperty(currentClass), property}, new String[]{"", "#", ""}), $get$$class$java$lang$String()), $get$$class$java$lang$String())), $get$$class$org$codehaus$groovy$binding$TriggerBinding());
         if (ScriptBytecodeAdapter.compareNotEqual(trigger, (Object)null)) {
            return (TriggerBinding)ScriptBytecodeAdapter.castToType(trigger, $get$$class$org$codehaus$groovy$binding$TriggerBinding());
         }
      }

      return (TriggerBinding)ScriptBytecodeAdapter.castToType(psb, $get$$class$org$codehaus$groovy$binding$TriggerBinding());
   }

   public Object bindingAttributeDelegate(FactoryBuilderSupport builder, Object node, Object attributes) {
      CallSite[] var4 = $getCallSiteArray();
      Iterator iter = (Iterator)ScriptBytecodeAdapter.castToType(var4[84].call(var4[85].call(attributes)), $get$$class$java$util$Iterator());
      Object var10000 = var4[86].call(var4[87].callGroovyObjectGetProperty(builder), (Object)CONTEXT_DATA_KEY);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = ScriptBytecodeAdapter.createMap(new Object[0]);
      }

      Map bindContext = (Map)ScriptBytecodeAdapter.castToType(var10000, $get$$class$java$util$Map());

      while(true) {
         FullBinding fb;
         while(true) {
            if (!DefaultTypeTransformation.booleanUnbox(var4[88].call(iter))) {
               return null;
            }

            Entry entry = (Entry)ScriptBytecodeAdapter.castToType(var4[89].call(iter), $get$$class$java$util$Map$Entry());
            String property = (String)ScriptBytecodeAdapter.castToType(var4[90].call(var4[91].callGetProperty(entry)), $get$$class$java$lang$String());
            Object value = var4[92].callGetProperty(entry);
            var10000 = var4[93].call(bindContext, (Object)value);
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = ScriptBytecodeAdapter.createMap(new Object[0]);
            }

            Object bindAttrs = var10000;
            var10000 = var4[94].call(builder, (Object)var4[95].callGetProperty($get$$class$groovy$swing$SwingBuilder()));
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = var4[96].callGetProperty($get$$class$groovy$swing$SwingBuilder());
            }

            Object idAttr = var10000;
            Object id = var4[97].call(bindAttrs, idAttr);
            if (DefaultTypeTransformation.booleanUnbox(var4[98].call(bindAttrs, (Object)"value"))) {
               ScriptBytecodeAdapter.setProperty(var4[99].call(bindAttrs, (Object)"value"), $get$$class$groovy$swing$factory$BindFactory(), node, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{property}, new String[]{"", ""}), $get$$class$java$lang$String()));
            }

            fb = (FullBinding)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$org$codehaus$groovy$binding$FullBinding());
            Object psb;
            if (value instanceof MutualPropertyBinding) {
               fb = (FullBinding)ScriptBytecodeAdapter.castToType(value, $get$$class$org$codehaus$groovy$binding$FullBinding());
               psb = var4[100].callConstructor($get$$class$org$codehaus$groovy$binding$PropertyBinding(), node, property);
               if (ScriptBytecodeAdapter.compareEqual(var4[101].callGetProperty(fb), (Object)null)) {
                  ScriptBytecodeAdapter.setProperty(psb, $get$$class$groovy$swing$factory$BindFactory(), fb, "sourceBinding");
                  var4[102].callCurrent(this, fb, builder, bindAttrs, id);
               } else if (ScriptBytecodeAdapter.compareEqual(var4[103].callGetProperty(fb), (Object)null)) {
                  ScriptBytecodeAdapter.setProperty(psb, $get$$class$groovy$swing$factory$BindFactory(), fb, "targetBinding");
               }
               break;
            }

            if (value instanceof FullBinding) {
               fb = (FullBinding)ScriptBytecodeAdapter.castToType(value, $get$$class$org$codehaus$groovy$binding$FullBinding());
               ScriptBytecodeAdapter.setProperty(var4[104].callConstructor($get$$class$org$codehaus$groovy$binding$PropertyBinding(), node, property), $get$$class$groovy$swing$factory$BindFactory(), fb, "targetBinding");
               break;
            }

            if (value instanceof TargetBinding) {
               psb = var4[105].callConstructor($get$$class$org$codehaus$groovy$binding$PropertyBinding(), node, property);
               fb = (FullBinding)ScriptBytecodeAdapter.castToType(var4[106].call(var4[107].callCurrent(this, (Object)psb), psb, value), $get$$class$org$codehaus$groovy$binding$FullBinding());
               var4[108].callCurrent(this, fb, builder, bindAttrs, id);
               break;
            }

            if (value instanceof ClosureTriggerBinding) {
               psb = var4[109].callConstructor($get$$class$org$codehaus$groovy$binding$PropertyBinding(), node, property);
               fb = (FullBinding)ScriptBytecodeAdapter.castToType(var4[110].call(value, value, psb), $get$$class$org$codehaus$groovy$binding$FullBinding());
               var4[111].callCurrent(this, fb, builder, bindAttrs, id);
               break;
            }
         }

         try {
            var4[112].call(fb);
         } catch (Exception var26) {
         } finally {
            ;
         }

         try {
            var4[113].call(fb);
         } catch (Exception var24) {
         } finally {
            ;
         }

         var4[114].call(iter);
      }
   }

   private Object finishContextualBinding(FullBinding fb, FactoryBuilderSupport builder, Object bindAttrs, Object id) {
      FullBinding fb = new Reference(fb);
      CallSite[] var6 = $getCallSiteArray();
      Object bindValue = var6[115].call(bindAttrs, (Object)"bind");
      var6[116].call(bindAttrs, (Object)(new GeneratedClosure(this, this, fb) {
         private Reference<T> fb;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$binding$FullBinding;
         // $FF: synthetic field
         private static Class $class$groovy$swing$factory$BindFactory$_finishContextualBinding_closure1;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.fb = (Reference)fb;
         }

         public Object doCall(Object k, Object v) {
            Object kx = new Reference(k);
            Object vx = new Reference(v);
            CallSite[] var5 = $getCallSiteArray();
            Object var10000 = vx.get();
            ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$swing$factory$BindFactory$_finishContextualBinding_closure1(), this.fb.get(), (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{kx.get()}, new String[]{"", ""}), $get$$class$java$lang$String()));
            return var10000;
         }

         public Object call(Object k, Object v) {
            Object kx = new Reference(k);
            Object vx = new Reference(v);
            CallSite[] var5 = $getCallSiteArray();
            return var5[0].callCurrent(this, kx.get(), vx.get());
         }

         public FullBinding getFb() {
            CallSite[] var1 = $getCallSiteArray();
            return (FullBinding)ScriptBytecodeAdapter.castToType(this.fb.get(), $get$$class$org$codehaus$groovy$binding$FullBinding());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$swing$factory$BindFactory$_finishContextualBinding_closure1()) {
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
            return new CallSiteArray($get$$class$groovy$swing$factory$BindFactory$_finishContextualBinding_closure1(), var0);
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
         private static Class $get$$class$java$lang$String() {
            Class var10000 = $class$java$lang$String;
            if (var10000 == null) {
               var10000 = $class$java$lang$String = class$("java.lang.String");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$binding$FullBinding() {
            Class var10000 = $class$org$codehaus$groovy$binding$FullBinding;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$binding$FullBinding = class$("org.codehaus.groovy.binding.FullBinding");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$swing$factory$BindFactory$_finishContextualBinding_closure1() {
            Class var10000 = $class$groovy$swing$factory$BindFactory$_finishContextualBinding_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$factory$BindFactory$_finishContextualBinding_closure1 = class$("groovy.swing.factory.BindFactory$_finishContextualBinding_closure1");
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
      if (DefaultTypeTransformation.booleanUnbox(!ScriptBytecodeAdapter.compareEqual(bindValue, (Object)null) && !DefaultTypeTransformation.booleanUnbox(bindValue instanceof Boolean && DefaultTypeTransformation.booleanUnbox(var6[117].call((Boolean)ScriptBytecodeAdapter.castToType(bindValue, $get$$class$java$lang$Boolean()))) ? Boolean.TRUE : Boolean.FALSE) ? Boolean.FALSE : Boolean.TRUE)) {
         var6[118].call(fb.get());
      }

      var6[119].call(builder, (Object)ScriptBytecodeAdapter.getMethodPointer(fb.get(), "unbind"));
      return DefaultTypeTransformation.booleanUnbox(id) ? var6[120].call(builder, id, fb.get()) : null;
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$BindFactory()) {
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
      Class var10000 = $get$$class$groovy$swing$factory$BindFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$BindFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$BindFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public final Map<String, TriggerBinding> getSyntheticBindings() {
      return this.syntheticBindings;
   }

   // $FF: synthetic method
   public Object this$3$finishContextualBinding(FullBinding var1, FactoryBuilderSupport var2, Object var3, Object var4) {
      return this.finishContextualBinding(var1, var2, var3, var4);
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
      var0[0] = "<$constructor$>";
      var0[1] = "putAll";
      var0[2] = "syntheticProperties";
      var0[3] = "putAll";
      var0[4] = "syntheticProperties";
      var0[5] = "putAll";
      var0[6] = "syntheticProperties";
      var0[7] = "putAll";
      var0[8] = "syntheticProperties";
      var0[9] = "putAll";
      var0[10] = "syntheticProperties";
      var0[11] = "putAll";
      var0[12] = "syntheticProperties";
      var0[13] = "putAll";
      var0[14] = "syntheticProperties";
      var0[15] = "putAll";
      var0[16] = "syntheticProperties";
      var0[17] = "putAll";
      var0[18] = "syntheticProperties";
      var0[19] = "remove";
      var0[20] = "remove";
      var0[21] = "get";
      var0[22] = "context";
      var0[23] = "isEmpty";
      var0[24] = "put";
      var0[25] = "context";
      var0[26] = "remove";
      var0[27] = "<$constructor$>";
      var0[28] = "remove";
      var0[29] = "<$constructor$>";
      var0[30] = "putAll";
      var0[31] = "put";
      var0[32] = "clear";
      var0[33] = "containsKey";
      var0[34] = "containsKey";
      var0[35] = "containsKey";
      var0[36] = "remove";
      var0[37] = "<$constructor$>";
      var0[38] = "remove";
      var0[39] = "<$constructor$>";
      var0[40] = "createBinding";
      var0[41] = "remove";
      var0[42] = "<$constructor$>";
      var0[43] = "remove";
      var0[44] = "<$constructor$>";
      var0[45] = "getTriggerBinding";
      var0[46] = "remove";
      var0[47] = "<$constructor$>";
      var0[48] = "remove";
      var0[49] = "<$constructor$>";
      var0[50] = "createBinding";
      var0[51] = "putAll";
      var0[52] = "put";
      var0[53] = "clear";
      var0[54] = "<$constructor$>";
      var0[55] = "<$constructor$>";
      var0[56] = "containsKey";
      var0[57] = "put";
      var0[58] = "remove";
      var0[59] = "remove";
      var0[60] = "containsKey";
      var0[61] = "booleanValue";
      var0[62] = "bind";
      var0[63] = "group";
      var0[64] = "addBinding";
      var0[65] = "remove";
      var0[66] = "addDisposalClosure";
      var0[67] = "sourceBinding";
      var0[68] = "targetBinding";
      var0[69] = "update";
      var0[70] = "rebind";
      var0[71] = "converter";
      var0[72] = "getAt";
      var0[73] = "get";
      var0[74] = "context";
      var0[75] = "containsKey";
      var0[76] = "putAt";
      var0[77] = "<$constructor$>";
      var0[78] = "propertyName";
      var0[79] = "getClass";
      var0[80] = "bean";
      var0[81] = "get";
      var0[82] = "name";
      var0[83] = "getSuperclass";
      var0[84] = "iterator";
      var0[85] = "entrySet";
      var0[86] = "get";
      var0[87] = "context";
      var0[88] = "hasNext";
      var0[89] = "next";
      var0[90] = "toString";
      var0[91] = "key";
      var0[92] = "value";
      var0[93] = "get";
      var0[94] = "getAt";
      var0[95] = "DELEGATE_PROPERTY_OBJECT_ID";
      var0[96] = "DEFAULT_DELEGATE_PROPERTY_OBJECT_ID";
      var0[97] = "remove";
      var0[98] = "containsKey";
      var0[99] = "remove";
      var0[100] = "<$constructor$>";
      var0[101] = "sourceBinding";
      var0[102] = "finishContextualBinding";
      var0[103] = "targetBinding";
      var0[104] = "<$constructor$>";
      var0[105] = "<$constructor$>";
      var0[106] = "createBinding";
      var0[107] = "getTriggerBinding";
      var0[108] = "finishContextualBinding";
      var0[109] = "<$constructor$>";
      var0[110] = "createBinding";
      var0[111] = "finishContextualBinding";
      var0[112] = "update";
      var0[113] = "rebind";
      var0[114] = "remove";
      var0[115] = "remove";
      var0[116] = "each";
      var0[117] = "booleanValue";
      var0[118] = "bind";
      var0[119] = "addDisposalClosure";
      var0[120] = "setVariable";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[121];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$BindFactory(), var0);
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
   private static Class $get$$class$groovy$swing$binding$JComponentProperties() {
      Class var10000 = $class$groovy$swing$binding$JComponentProperties;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JComponentProperties = class$("groovy.swing.binding.JComponentProperties");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$RuntimeException() {
      Class var10000 = $class$java$lang$RuntimeException;
      if (var10000 == null) {
         var10000 = $class$java$lang$RuntimeException = class$("java.lang.RuntimeException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$JSpinnerProperties() {
      Class var10000 = $class$groovy$swing$binding$JSpinnerProperties;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JSpinnerProperties = class$("groovy.swing.binding.JSpinnerProperties");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$util$AbstractFactory() {
      Class var10000 = $class$groovy$util$AbstractFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$util$AbstractFactory = class$("groovy.util.AbstractFactory");
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
   private static Class $get$$class$java$util$Map() {
      Class var10000 = $class$java$util$Map;
      if (var10000 == null) {
         var10000 = $class$java$util$Map = class$("java.util.Map");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$JComboBoxProperties() {
      Class var10000 = $class$groovy$swing$binding$JComboBoxProperties;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JComboBoxProperties = class$("groovy.swing.binding.JComboBoxProperties");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$binding$PropertyBinding() {
      Class var10000 = $class$org$codehaus$groovy$binding$PropertyBinding;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$binding$PropertyBinding = class$("org.codehaus.groovy.binding.PropertyBinding");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$JTableProperties() {
      Class var10000 = $class$groovy$swing$binding$JTableProperties;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JTableProperties = class$("groovy.swing.binding.JTableProperties");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$AbstractButtonProperties() {
      Class var10000 = $class$groovy$swing$binding$AbstractButtonProperties;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$AbstractButtonProperties = class$("groovy.swing.binding.AbstractButtonProperties");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$BindFactory() {
      Class var10000 = $class$groovy$swing$factory$BindFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$BindFactory = class$("groovy.swing.factory.BindFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$JTextComponentProperties() {
      Class var10000 = $class$groovy$swing$binding$JTextComponentProperties;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JTextComponentProperties = class$("groovy.swing.binding.JTextComponentProperties");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$binding$ClosureSourceBinding() {
      Class var10000 = $class$org$codehaus$groovy$binding$ClosureSourceBinding;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$binding$ClosureSourceBinding = class$("org.codehaus.groovy.binding.ClosureSourceBinding");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$JListProperties() {
      Class var10000 = $class$groovy$swing$binding$JListProperties;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JListProperties = class$("groovy.swing.binding.JListProperties");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$binding$ClosureTriggerBinding() {
      Class var10000 = $class$org$codehaus$groovy$binding$ClosureTriggerBinding;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$binding$ClosureTriggerBinding = class$("org.codehaus.groovy.binding.ClosureTriggerBinding");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$binding$SourceBinding() {
      Class var10000 = $class$org$codehaus$groovy$binding$SourceBinding;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$binding$SourceBinding = class$("org.codehaus.groovy.binding.SourceBinding");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$binding$EventTriggerBinding() {
      Class var10000 = $class$org$codehaus$groovy$binding$EventTriggerBinding;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$binding$EventTriggerBinding = class$("org.codehaus.groovy.binding.EventTriggerBinding");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Class() {
      Class var10000 = $class$java$lang$Class;
      if (var10000 == null) {
         var10000 = $class$java$lang$Class = class$("java.lang.Class");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$binding$FullBinding() {
      Class var10000 = $class$org$codehaus$groovy$binding$FullBinding;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$binding$FullBinding = class$("org.codehaus.groovy.binding.FullBinding");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$Closure() {
      Class var10000 = $class$groovy$lang$Closure;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Closure = class$("groovy.lang.Closure");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$Iterator() {
      Class var10000 = $class$java$util$Iterator;
      if (var10000 == null) {
         var10000 = $class$java$util$Iterator = class$("java.util.Iterator");
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
   private static Class $get$$class$groovy$swing$binding$JSliderProperties() {
      Class var10000 = $class$groovy$swing$binding$JSliderProperties;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JSliderProperties = class$("groovy.swing.binding.JSliderProperties");
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
   private static Class $get$$class$java$util$Map$Entry() {
      Class var10000 = $class$java$util$Map$Entry;
      if (var10000 == null) {
         var10000 = $class$java$util$Map$Entry = class$("java.util.Map$Entry");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$SwingBuilder() {
      Class var10000 = $class$groovy$swing$SwingBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$SwingBuilder = class$("groovy.swing.SwingBuilder");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$binding$TargetBinding() {
      Class var10000 = $class$org$codehaus$groovy$binding$TargetBinding;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$binding$TargetBinding = class$("org.codehaus.groovy.binding.TargetBinding");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$HashMap() {
      Class var10000 = $class$java$util$HashMap;
      if (var10000 == null) {
         var10000 = $class$java$util$HashMap = class$("java.util.HashMap");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$binding$MutualPropertyBinding() {
      Class var10000 = $class$org$codehaus$groovy$binding$MutualPropertyBinding;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$binding$MutualPropertyBinding = class$("org.codehaus.groovy.binding.MutualPropertyBinding");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$binding$TriggerBinding() {
      Class var10000 = $class$org$codehaus$groovy$binding$TriggerBinding;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$binding$TriggerBinding = class$("org.codehaus.groovy.binding.TriggerBinding");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$JScrollBarProperties() {
      Class var10000 = $class$groovy$swing$binding$JScrollBarProperties;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JScrollBarProperties = class$("groovy.swing.binding.JScrollBarProperties");
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
