package groovy.grape;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URI;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.ivy.Ivy;
import org.apache.ivy.core.cache.ResolutionCacheManager;
import org.apache.ivy.core.event.IvyListener;
import org.apache.ivy.core.module.descriptor.DefaultDependencyDescriptor;
import org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.ArtifactDownloadReport;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.ResolveOptions;
import org.apache.ivy.core.settings.IvySettings;
import org.apache.ivy.plugins.resolver.ChainResolver;
import org.apache.ivy.plugins.resolver.IBiblioResolver;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class GrapeIvy implements GrapeEngine, GroovyObject {
   private static final int DEFAULT_DEPTH = DefaultTypeTransformation.intUnbox(3);
   private final Object exclusiveGrabArgs;
   private boolean enableGrapes;
   private Ivy ivyInstance;
   private Set<String> resolvedDependencies;
   private Set<String> downloadedArtifacts;
   private Map<ClassLoader, Set<IvyGrabRecord>> loadedDeps;
   private Set<IvyGrabRecord> grabRecordsForCurrDependencies;
   private IvySettings settings;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)3;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)-2;
   // $FF: synthetic field
   private static final Integer $const$3 = (Integer)10;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524202418L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202418 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$org$apache$ivy$core$event$IvyListener;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$reflection$ReflectionUtils;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovySystem;
   // $FF: synthetic field
   private static Class $class$java$lang$RuntimeException;
   // $FF: synthetic field
   private static Class array$$class$java$util$Map;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$apache$ivy$Ivy;
   // $FF: synthetic field
   private static Class $class$java$util$Map;
   // $FF: synthetic field
   private static Class $class$java$io$File;
   // $FF: synthetic field
   private static Class $class$org$apache$ivy$core$report$ResolveReport;
   // $FF: synthetic field
   private static Class $class$org$apache$ivy$core$module$id$ModuleRevisionId;
   // $FF: synthetic field
   private static Class $class$java$util$Set;
   // $FF: synthetic field
   private static Class $class$java$util$regex$Pattern;
   // $FF: synthetic field
   private static Class $class$groovy$grape$GrapeIvy;
   // $FF: synthetic field
   private static Class $class$org$apache$ivy$core$resolve$ResolveOptions;
   // $FF: synthetic field
   private static Class $class$org$apache$ivy$plugins$resolver$ChainResolver;
   // $FF: synthetic field
   private static Class array$$class$java$net$URI;
   // $FF: synthetic field
   private static Class $class$org$apache$ivy$core$module$descriptor$DefaultDependencyArtifactDescriptor;
   // $FF: synthetic field
   private static Class $class$org$apache$ivy$util$DefaultMessageLogger;
   // $FF: synthetic field
   private static Class $class$org$apache$ivy$util$Message;
   // $FF: synthetic field
   private static Class $class$groovy$grape$IvyGrabRecord;
   // $FF: synthetic field
   private static Class $class$java$util$LinkedHashSet;
   // $FF: synthetic field
   private static Class array$$class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$apache$ivy$core$module$descriptor$DefaultModuleDescriptor;
   // $FF: synthetic field
   private static Class $class$org$apache$ivy$core$module$descriptor$Configuration;
   // $FF: synthetic field
   private static Class $class$java$lang$System;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$org$apache$ivy$core$cache$ResolutionCacheManager;
   // $FF: synthetic field
   private static Class $class$org$apache$ivy$core$module$descriptor$DefaultDependencyDescriptor;
   // $FF: synthetic field
   private static Class $class$org$apache$ivy$plugins$resolver$IBiblioResolver;
   // $FF: synthetic field
   private static Class $class$java$util$WeakHashMap;
   // $FF: synthetic field
   private static Class $class$org$apache$ivy$core$settings$IvySettings;

   public GrapeIvy() {
      CallSite[] var1 = $getCallSiteArray();
      this.exclusiveGrabArgs = var1[0].call(ScriptBytecodeAdapter.createList(new Object[]{ScriptBytecodeAdapter.createList(new Object[]{"group", "groupId", "organisation", "organization", "org"}), ScriptBytecodeAdapter.createList(new Object[]{"module", "artifactId", "artifact"}), ScriptBytecodeAdapter.createList(new Object[]{"version", "revision", "rev"}), ScriptBytecodeAdapter.createList(new Object[]{"conf", "scope", "configuration"})}), ScriptBytecodeAdapter.createMap(new Object[0]), new GrapeIvy._closure1(this, this));
      this.loadedDeps = (Map)ScriptBytecodeAdapter.castToType(var1[1].callConstructor($get$$class$java$util$WeakHashMap()), $get$$class$java$util$Map());
      this.grabRecordsForCurrDependencies = (Set)ScriptBytecodeAdapter.castToType(var1[2].callConstructor($get$$class$java$util$LinkedHashSet()), $get$$class$java$util$Set());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      if (!DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.enableGrapes))) {
         ScriptBytecodeAdapter.setProperty(var1[3].callConstructor($get$$class$org$apache$ivy$util$DefaultMessageLogger(), (Object)ScriptBytecodeAdapter.createPojoWrapper((Integer)ScriptBytecodeAdapter.asType(var1[4].call($get$$class$java$lang$System(), "ivy.message.logger.level", "-1"), $get$$class$java$lang$Integer()), Integer.TYPE)), $get$$class$groovy$grape$GrapeIvy(), $get$$class$org$apache$ivy$util$Message(), "defaultLogger");
         this.settings = (IvySettings)ScriptBytecodeAdapter.castToType(var1[5].callConstructor($get$$class$org$apache$ivy$core$settings$IvySettings()), $get$$class$org$apache$ivy$core$settings$IvySettings());
         Object grapeConfig = var1[6].callCurrent(this);
         if (!DefaultTypeTransformation.booleanUnbox(var1[7].call(grapeConfig))) {
            grapeConfig = var1[8].call($get$$class$groovy$grape$GrapeIvy(), (Object)"defaultGrapeConfig.xml");
         }

         try {
            var1[9].call(this.settings, (Object)grapeConfig);
         } catch (ParseException var6) {
            var1[10].call(var1[11].callGetProperty($get$$class$java$lang$System()), var1[12].call(new GStringImpl(new Object[]{var1[13].callGetProperty(grapeConfig)}, new String[]{"Local Ivy config file '", "' appears corrupt - ignoring it and using default config instead\nError was: "}), (Object)var1[14].callGetProperty(var6)));
            grapeConfig = var1[15].call($get$$class$groovy$grape$GrapeIvy(), (Object)"defaultGrapeConfig.xml");
            var1[16].call(this.settings, (Object)grapeConfig);
         } finally {
            ;
         }

         ScriptBytecodeAdapter.setProperty(var1[17].callCurrent(this), $get$$class$groovy$grape$GrapeIvy(), this.settings, "defaultCache");
         var1[18].call(this.settings, "ivy.default.configuration.m2compatible", "true");
         this.ivyInstance = (Ivy)ScriptBytecodeAdapter.castToType((Ivy)ScriptBytecodeAdapter.castToType(var1[19].call($get$$class$org$apache$ivy$Ivy(), (Object)this.settings), $get$$class$org$apache$ivy$Ivy()), $get$$class$org$apache$ivy$Ivy());
         this.resolvedDependencies = (Set)ScriptBytecodeAdapter.castToType((Set)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$Set()), $get$$class$java$util$Set());
         this.downloadedArtifacts = (Set)ScriptBytecodeAdapter.castToType((Set)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$Set()), $get$$class$java$util$Set());
         this.enableGrapes = DefaultTypeTransformation.booleanUnbox(Boolean.TRUE);
      }
   }

   public File getGroovyRoot() {
      CallSite[] var1 = $getCallSiteArray();
      String root = (String)ScriptBytecodeAdapter.castToType(var1[20].call($get$$class$java$lang$System(), (Object)"groovy.root"), $get$$class$java$lang$String());
      Object groovyRoot = null;
      if (ScriptBytecodeAdapter.compareEqual(root, (Object)null)) {
         groovyRoot = var1[21].callConstructor($get$$class$java$io$File(), var1[22].call($get$$class$java$lang$System(), (Object)"user.home"), ".groovy");
      } else {
         groovyRoot = var1[23].callConstructor($get$$class$java$io$File(), (Object)root);
      }

      try {
         groovyRoot = var1[24].callGetProperty(groovyRoot);
      } catch (IOException var7) {
      } finally {
         ;
      }

      return (File)ScriptBytecodeAdapter.castToType(groovyRoot, $get$$class$java$io$File());
   }

   public File getLocalGrapeConfig() {
      CallSite[] var1 = $getCallSiteArray();
      String grapeConfig = (String)ScriptBytecodeAdapter.castToType(var1[25].call($get$$class$java$lang$System(), (Object)"grape.config"), $get$$class$java$lang$String());
      return DefaultTypeTransformation.booleanUnbox(grapeConfig) ? (File)ScriptBytecodeAdapter.castToType(var1[26].callConstructor($get$$class$java$io$File(), (Object)grapeConfig), $get$$class$java$io$File()) : (File)ScriptBytecodeAdapter.castToType(var1[27].callConstructor($get$$class$java$io$File(), var1[28].callCurrent(this), "grapeConfig.xml"), $get$$class$java$io$File());
   }

   public File getGrapeDir() {
      CallSite[] var1 = $getCallSiteArray();
      String root = (String)ScriptBytecodeAdapter.castToType(var1[29].call($get$$class$java$lang$System(), (Object)"grape.root"), $get$$class$java$lang$String());
      if (ScriptBytecodeAdapter.compareEqual(root, (Object)null)) {
         return (File)ScriptBytecodeAdapter.castToType(var1[30].callCurrent(this), $get$$class$java$io$File());
      } else {
         Object grapeRoot = var1[31].callConstructor($get$$class$java$io$File(), (Object)root);

         try {
            grapeRoot = (File)ScriptBytecodeAdapter.castToType(var1[32].callGetProperty(grapeRoot), $get$$class$java$io$File());
         } catch (IOException var7) {
         } finally {
            ;
         }

         return (File)ScriptBytecodeAdapter.castToType(grapeRoot, $get$$class$java$io$File());
      }
   }

   public File getGrapeCacheDir() {
      CallSite[] var1 = $getCallSiteArray();
      File cache = var1[33].callConstructor($get$$class$java$io$File(), var1[34].callCurrent(this), "grapes");
      if (!DefaultTypeTransformation.booleanUnbox(var1[35].call(cache))) {
         var1[36].call(cache);
      } else if (!DefaultTypeTransformation.booleanUnbox(var1[37].call(cache))) {
         throw (Throwable)var1[38].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{cache}, new String[]{"The grape cache dir ", " is not a directory"})));
      }

      return (File)ScriptBytecodeAdapter.castToType(cache, $get$$class$java$io$File());
   }

   public Object chooseClassLoader(Map args) {
      CallSite[] var2 = $getCallSiteArray();
      Object loader = var2[39].callGetProperty(args);
      if (!DefaultTypeTransformation.booleanUnbox(var2[40].callCurrent(this, (Object)loader))) {
         CallSite var10000 = var2[41];
         Object var10001 = var2[42].callGetPropertySafe(var2[43].callGetProperty(args));
         if (!DefaultTypeTransformation.booleanUnbox(var10001)) {
            CallSite var4 = var2[44];
            Class var10002 = $get$$class$org$codehaus$groovy$reflection$ReflectionUtils();
            Object var10003 = var2[45].callGetProperty(args);
            if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
               var10003 = $const$1;
            }

            var10001 = var4.call(var10002, (Object)var10003);
         }

         for(loader = var10000.callGetPropertySafe(var10001); DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(loader) && !DefaultTypeTransformation.booleanUnbox(var2[46].callCurrent(this, (Object)loader)) ? Boolean.TRUE : Boolean.FALSE); loader = var2[47].callGetProperty(loader)) {
         }

         if (!DefaultTypeTransformation.booleanUnbox(var2[48].callCurrent(this, (Object)loader))) {
            throw (Throwable)var2[49].callConstructor($get$$class$java$lang$RuntimeException(), (Object)"No suitable ClassLoader found for grab");
         }
      }

      return loader;
   }

   private boolean isValidTargetClassLoader(Object loader) {
      CallSite[] var2 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(var2[50].callCurrent(this, (Object)var2[51].callGetPropertySafe(loader)), $get$$class$java$lang$Boolean()));
   }

   private boolean isValidTargetClassLoaderClass(Class loaderClass) {
      CallSite[] var2 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.compareNotEqual(loaderClass, (Object)null) && DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(!ScriptBytecodeAdapter.compareEqual(var2[52].callGetProperty(loaderClass), "groovy.lang.GroovyClassLoader") && !ScriptBytecodeAdapter.compareEqual(var2[53].callGetProperty(loaderClass), "org.codehaus.groovy.tools.RootLoader") ? Boolean.FALSE : Boolean.TRUE) && !DefaultTypeTransformation.booleanUnbox(var2[54].callCurrent(this, (Object)var2[55].callGetProperty(loaderClass))) ? Boolean.FALSE : Boolean.TRUE) ? Boolean.TRUE : Boolean.FALSE, $get$$class$java$lang$Boolean()));
   }

   public IvyGrabRecord createGrabRecord(Map deps) {
      CallSite[] var2 = $getCallSiteArray();
      Object var10000 = var2[56].callGetProperty(deps);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = var2[57].callGetProperty(deps);
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var2[58].callGetProperty(deps);
         }
      }

      String module = (String)ScriptBytecodeAdapter.castToType(var10000, $get$$class$java$lang$String());
      if (!DefaultTypeTransformation.booleanUnbox(module)) {
         throw (Throwable)var2[59].callConstructor($get$$class$java$lang$RuntimeException(), (Object)"grab requires at least a module: or artifactId: or artifact: argument");
      } else {
         var10000 = var2[60].callGetProperty(deps);
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var2[61].callGetProperty(deps);
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = var2[62].callGetProperty(deps);
               if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
                  var10000 = var2[63].callGetProperty(deps);
                  if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
                     var10000 = var2[64].callGetProperty(deps);
                     if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
                        var10000 = "";
                     }
                  }
               }
            }
         }

         String groupId = (String)ScriptBytecodeAdapter.castToType(var10000, $get$$class$java$lang$String());
         var10000 = var2[65].callGetProperty(deps);
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var2[66].callGetProperty(deps);
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = var2[67].callGetProperty(deps);
               if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
                  var10000 = "*";
               }
            }
         }

         String version = (String)ScriptBytecodeAdapter.castToType(var10000, $get$$class$java$lang$String());
         if (ScriptBytecodeAdapter.compareEqual("*", version)) {
            version = "latest.default";
         }

         ModuleRevisionId mrid = (ModuleRevisionId)ScriptBytecodeAdapter.castToType(var2[68].call($get$$class$org$apache$ivy$core$module$id$ModuleRevisionId(), groupId, module, version), $get$$class$org$apache$ivy$core$module$id$ModuleRevisionId());
         Boolean force = (Boolean)ScriptBytecodeAdapter.castToType(DefaultTypeTransformation.booleanUnbox(var2[69].call(deps, (Object)"force")) ? var2[70].callGetProperty(deps) : Boolean.TRUE, $get$$class$java$lang$Boolean());
         Boolean changing = (Boolean)ScriptBytecodeAdapter.castToType(DefaultTypeTransformation.booleanUnbox(var2[71].call(deps, (Object)"changing")) ? var2[72].callGetProperty(deps) : Boolean.FALSE, $get$$class$java$lang$Boolean());
         Boolean transitive = (Boolean)ScriptBytecodeAdapter.castToType(DefaultTypeTransformation.booleanUnbox(var2[73].call(deps, (Object)"transitive")) ? var2[74].callGetProperty(deps) : Boolean.TRUE, $get$$class$java$lang$Boolean());
         var10000 = var2[75].callGetProperty(deps);
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var2[76].callGetProperty(deps);
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = var2[77].callGetProperty(deps);
               if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
                  var10000 = ScriptBytecodeAdapter.createList(new Object[]{"default"});
               }
            }
         }

         Object conf = var10000;
         if (conf instanceof String) {
            if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var2[78].call(conf, (Object)"[")) && DefaultTypeTransformation.booleanUnbox(var2[79].call(conf, (Object)"]")) ? Boolean.TRUE : Boolean.FALSE)) {
               conf = var2[80].call(conf, (Object)ScriptBytecodeAdapter.createRange($const$1, $const$2, true));
            }

            conf = var2[81].call(var2[82].call(conf, (Object)","));
         }

         var10000 = var2[83].callGetProperty(deps);
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = null;
         }

         Object classifier = var10000;
         return (IvyGrabRecord)ScriptBytecodeAdapter.castToType(var2[84].callConstructor($get$$class$groovy$grape$IvyGrabRecord(), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"mrid", mrid, "conf", conf, "changing", changing, "transitive", transitive, "force", force, "classifier", classifier})), $get$$class$groovy$grape$IvyGrabRecord());
      }
   }

   public Object grab(String endorsedModule) {
      CallSite[] var2 = $getCallSiteArray();
      return var2[85].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"group", "groovy.endorsed", "module", endorsedModule, "version", var2[86].callGetProperty($get$$class$groovy$lang$GroovySystem())}));
   }

   public Object grab(Map args) {
      CallSite[] var2 = $getCallSiteArray();
      Object var10000 = var2[87].callGetProperty(args);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = var2[88].call(DefaultTypeTransformation.box(DEFAULT_DEPTH), (Object)$const$1);
      }

      ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$grape$GrapeIvy(), args, "calleeDepth");
      return var2[89].callCurrent(this, args, args);
   }

   public Object grab(Map args, Map... dependencies) {
      CallSite[] var3 = $getCallSiteArray();
      Object loader = null;
      var3[90].call(this.grabRecordsForCurrDependencies);

      Object uri;
      try {
         CallSite var10000 = var3[91];
         Object[] var10002 = new Object[]{"classLoader", var3[92].call(args, (Object)"classLoader"), "refObject", var3[93].call(args, (Object)"refObject"), "calleeDepth", null};
         Object var10005 = var3[94].callGetProperty(args);
         if (!DefaultTypeTransformation.booleanUnbox(var10005)) {
            var10005 = DefaultTypeTransformation.box(DEFAULT_DEPTH);
         }

         var10002[5] = var10005;
         loader = var10000.callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(var10002));
         if (DefaultTypeTransformation.booleanUnbox(loader)) {
            uri = null;
            Object var12 = var3[95].call(var3[96].callCurrent(this, loader, args, dependencies));

            while(((Iterator)var12).hasNext()) {
               uri = ((Iterator)var12).next();
               var3[97].call(loader, var3[98].call(uri));
            }

            return null;
         }

         uri = null;
      } catch (Exception var10) {
         Set grabRecordsForCurrLoader = (Set)ScriptBytecodeAdapter.castToType(var3[99].callCurrent(this, (Object)loader), $get$$class$java$util$Set());
         var3[100].call(grabRecordsForCurrLoader, (Object)this.grabRecordsForCurrDependencies);
         var3[101].call(this.grabRecordsForCurrDependencies);
         if (DefaultTypeTransformation.booleanUnbox(var3[102].callGetProperty(args))) {
            Exception var7 = var10;
            return var7;
         }

         throw (Throwable)var10;
      } finally {
         ;
      }

      return uri;
   }

   public ResolveReport getDependencies(Map args, IvyGrabRecord... grabRecords) {
      CallSite[] var3 = $getCallSiteArray();
      ResolutionCacheManager cacheManager = (ResolutionCacheManager)ScriptBytecodeAdapter.castToType(var3[103].callGetProperty(this.ivyInstance), $get$$class$org$apache$ivy$core$cache$ResolutionCacheManager());
      Object md = var3[104].callConstructor($get$$class$org$apache$ivy$core$module$descriptor$DefaultModuleDescriptor(), var3[105].call($get$$class$org$apache$ivy$core$module$id$ModuleRevisionId(), "caller", "all-caller", "working"), "integration", (Object)null, Boolean.TRUE);
      var3[106].call(md, var3[107].callConstructor($get$$class$org$apache$ivy$core$module$descriptor$Configuration(), (Object)"default"));
      var3[108].call(md, var3[109].call($get$$class$java$lang$System()));
      var3[110].callCurrent(this, args, md);
      IvyGrabRecord grabRecord = null;

      Reference dd;
      for(Object var7 = var3[111].call(grabRecords); ((Iterator)var7).hasNext(); var3[127].call(md, dd.get())) {
         grabRecord = ((Iterator)var7).next();
         dd = new Reference(var3[112].callConstructor($get$$class$org$apache$ivy$core$module$descriptor$DefaultDependencyDescriptor(), (Object[])ArrayUtil.createArray(md, var3[113].callGroovyObjectGetProperty(grabRecord), var3[114].callGroovyObjectGetProperty(grabRecord), var3[115].callGroovyObjectGetProperty(grabRecord), var3[116].callGroovyObjectGetProperty(grabRecord))));
         Object var10000 = var3[117].callGroovyObjectGetProperty(grabRecord);
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = ScriptBytecodeAdapter.createList(new Object[]{"*"});
         }

         Object conf = var10000;
         var3[118].call(conf, (Object)(new GeneratedClosure(this, this, dd) {
            private Reference<T> dd;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$org$apache$ivy$core$module$descriptor$DefaultDependencyDescriptor;
            // $FF: synthetic field
            private static Class $class$groovy$grape$GrapeIvy$_getDependencies_closure2;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.dd = (Reference)dd;
            }

            public Object doCall(Object it) {
               Object itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               return var3[0].call(this.dd.get(), "default", itx.get());
            }

            public DefaultDependencyDescriptor getDd() {
               CallSite[] var1 = $getCallSiteArray();
               return (DefaultDependencyDescriptor)ScriptBytecodeAdapter.castToType(this.dd.get(), $get$$class$org$apache$ivy$core$module$descriptor$DefaultDependencyDescriptor());
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$grape$GrapeIvy$_getDependencies_closure2()) {
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
               var0[0] = "addDependencyConfiguration";
               var0[1] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[2];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$grape$GrapeIvy$_getDependencies_closure2(), var0);
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
            private static Class $get$$class$org$apache$ivy$core$module$descriptor$DefaultDependencyDescriptor() {
               Class var10000 = $class$org$apache$ivy$core$module$descriptor$DefaultDependencyDescriptor;
               if (var10000 == null) {
                  var10000 = $class$org$apache$ivy$core$module$descriptor$DefaultDependencyDescriptor = class$("org.apache.ivy.core.module.descriptor.DefaultDependencyDescriptor");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$groovy$grape$GrapeIvy$_getDependencies_closure2() {
               Class var10000 = $class$groovy$grape$GrapeIvy$_getDependencies_closure2;
               if (var10000 == null) {
                  var10000 = $class$groovy$grape$GrapeIvy$_getDependencies_closure2 = class$("groovy.grape.GrapeIvy$_getDependencies_closure2");
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
         if (DefaultTypeTransformation.booleanUnbox(var3[119].callGroovyObjectGetProperty(grabRecord))) {
            CallSite var14 = var3[120];
            Class var10001 = $get$$class$org$apache$ivy$core$module$descriptor$DefaultDependencyArtifactDescriptor();
            Object var10002 = dd.get();
            Object var10003 = var3[121].callGetProperty(var3[122].callGroovyObjectGetProperty(grabRecord));
            Object var10005 = var3[123].callGroovyObjectGetProperty(grabRecord);
            if (!DefaultTypeTransformation.booleanUnbox(var10005)) {
               var10005 = "jar";
            }

            Object dad = new Reference(var14.callConstructor(var10001, (Object[])ArrayUtil.createArray(var10002, var10003, "jar", var10005, (Object)null, ScriptBytecodeAdapter.createMap(new Object[]{"classifier", var3[124].callGroovyObjectGetProperty(grabRecord)}))));
            var3[125].call(conf, (Object)(new GeneratedClosure(this, this, dad) {
               private Reference<T> dad;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$grape$GrapeIvy$_getDependencies_closure3;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.dad = (Reference)dad;
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].call(this.dad.get(), itx.get());
               }

               public Object getDad() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.dad.get();
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$grape$GrapeIvy$_getDependencies_closure3()) {
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
                  var0[0] = "addConfiguration";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$grape$GrapeIvy$_getDependencies_closure3(), var0);
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
               private static Class $get$$class$groovy$grape$GrapeIvy$_getDependencies_closure3() {
                  Class var10000 = $class$groovy$grape$GrapeIvy$_getDependencies_closure3;
                  if (var10000 == null) {
                     var10000 = $class$groovy$grape$GrapeIvy$_getDependencies_closure3 = class$("groovy.grape.GrapeIvy$_getDependencies_closure3");
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
            var3[126].call(dd.get(), "default", dad.get());
         }
      }

      ResolveOptions resolveOptions = (ResolveOptions)ScriptBytecodeAdapter.castToType(var3[128].call(var3[129].call(var3[130].call(var3[131].callConstructor($get$$class$org$apache$ivy$core$resolve$ResolveOptions()), (Object)ScriptBytecodeAdapter.createPojoWrapper((String[])ScriptBytecodeAdapter.asType(ScriptBytecodeAdapter.createList(new Object[]{"default"}), $get$array$$class$java$lang$String()), $get$array$$class$java$lang$String())), (Object)Boolean.FALSE), DefaultTypeTransformation.booleanUnbox(var3[132].call(args, (Object)"validate")) ? var3[133].callGetProperty(args) : Boolean.FALSE), $get$$class$org$apache$ivy$core$resolve$ResolveOptions());
      ScriptBytecodeAdapter.setProperty(DefaultTypeTransformation.booleanUnbox(var3[134].callGetProperty(args)) ? "downloadGrapes" : "cachedGrapes", $get$$class$groovy$grape$GrapeIvy(), var3[135].callGetProperty(this.ivyInstance), "defaultResolver");
      Boolean reportDownloads = (Boolean)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.compareEqual(var3[136].call($get$$class$java$lang$System(), "groovy.grape.report.downloads", "false"), "true") ? Boolean.TRUE : Boolean.FALSE, $get$$class$java$lang$Boolean());
      if (DefaultTypeTransformation.booleanUnbox(reportDownloads)) {
         var3[137].call(var3[138].callGetProperty(this.ivyInstance), (Object)ScriptBytecodeAdapter.createPojoWrapper((IvyListener)ScriptBytecodeAdapter.asType(ScriptBytecodeAdapter.createMap(new Object[]{"progress", new GeneratedClosure(this, this) {
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$org$apache$ivy$core$event$resolve$StartResolveEvent;
            // $FF: synthetic field
            private static Class $class$groovy$grape$GrapeIvy$_getDependencies_closure4;
            // $FF: synthetic field
            private static Class $class$org$apache$ivy$core$event$download$PrepareDownloadEvent;

            public {
               CallSite[] var3 = $getCallSiteArray();
            }

            public Object doCall(Object ivyEvent) {
               Object ivyEventx = new Reference(ivyEvent);
               CallSite[] var3 = $getCallSiteArray();
               Object var4 = ivyEventx.get();
               if (ScriptBytecodeAdapter.isCase(var4, $get$$class$org$apache$ivy$core$event$resolve$StartResolveEvent())) {
                  return var3[0].call(var3[1].callGetProperty(var3[2].callGetProperty(ivyEventx.get())), (Object)(new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$groovy$grape$GrapeIvy$_getDependencies_closure4_closure12;
                     // $FF: synthetic field
                     private static Class $class$java$lang$System;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object it) {
                        Object itx = new Reference(it);
                        CallSite[] var3 = $getCallSiteArray();
                        Object name = new Reference(var3[0].call(itx.get()));
                        if (!DefaultTypeTransformation.booleanUnbox(var3[1].call(var3[2].callGroovyObjectGetProperty(this), name.get()))) {
                           var3[3].call(var3[4].callGroovyObjectGetProperty(this), name.get());
                           return var3[5].call(var3[6].callGetProperty($get$$class$java$lang$System()), var3[7].call("Resolving ", (Object)name.get()));
                        } else {
                           return null;
                        }
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$grape$GrapeIvy$_getDependencies_closure4_closure12()) {
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
                        var0[0] = "toString";
                        var0[1] = "contains";
                        var0[2] = "resolvedDependencies";
                        var0[3] = "leftShift";
                        var0[4] = "resolvedDependencies";
                        var0[5] = "println";
                        var0[6] = "err";
                        var0[7] = "plus";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[8];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$grape$GrapeIvy$_getDependencies_closure4_closure12(), var0);
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
                     private static Class $get$$class$groovy$grape$GrapeIvy$_getDependencies_closure4_closure12() {
                        Class var10000 = $class$groovy$grape$GrapeIvy$_getDependencies_closure4_closure12;
                        if (var10000 == null) {
                           var10000 = $class$groovy$grape$GrapeIvy$_getDependencies_closure4_closure12 = class$("groovy.grape.GrapeIvy$_getDependencies_closure4_closure12");
                        }

                        return var10000;
                     }

                     // $FF: synthetic method
                     private static Class $get$$class$java$lang$System() {
                        Class var10000 = $class$java$lang$System;
                        if (var10000 == null) {
                           var10000 = $class$java$lang$System = class$("java.lang.System");
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
               } else {
                  return ScriptBytecodeAdapter.isCase(var4, $get$$class$org$apache$ivy$core$event$download$PrepareDownloadEvent()) ? var3[3].call(var3[4].callGetProperty(ivyEventx.get()), (Object)(new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$System;
                     // $FF: synthetic field
                     private static Class $class$groovy$grape$GrapeIvy$_getDependencies_closure4_closure13;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object it) {
                        Object itx = new Reference(it);
                        CallSite[] var3 = $getCallSiteArray();
                        Object name = new Reference(var3[0].call(itx.get()));
                        if (!DefaultTypeTransformation.booleanUnbox(var3[1].call(var3[2].callGroovyObjectGetProperty(this), name.get()))) {
                           var3[3].call(var3[4].callGroovyObjectGetProperty(this), name.get());
                           return var3[5].call(var3[6].callGetProperty($get$$class$java$lang$System()), var3[7].call("Preparing to download artifact ", (Object)name.get()));
                        } else {
                           return null;
                        }
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$grape$GrapeIvy$_getDependencies_closure4_closure13()) {
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
                        var0[0] = "toString";
                        var0[1] = "contains";
                        var0[2] = "downloadedArtifacts";
                        var0[3] = "leftShift";
                        var0[4] = "downloadedArtifacts";
                        var0[5] = "println";
                        var0[6] = "err";
                        var0[7] = "plus";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[8];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$grape$GrapeIvy$_getDependencies_closure4_closure13(), var0);
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
                     private static Class $get$$class$java$lang$System() {
                        Class var10000 = $class$java$lang$System;
                        if (var10000 == null) {
                           var10000 = $class$java$lang$System = class$("java.lang.System");
                        }

                        return var10000;
                     }

                     // $FF: synthetic method
                     private static Class $get$$class$groovy$grape$GrapeIvy$_getDependencies_closure4_closure13() {
                        Class var10000 = $class$groovy$grape$GrapeIvy$_getDependencies_closure4_closure13;
                        if (var10000 == null) {
                           var10000 = $class$groovy$grape$GrapeIvy$_getDependencies_closure4_closure13 = class$("groovy.grape.GrapeIvy$_getDependencies_closure4_closure13");
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
                  })) : null;
               }
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$grape$GrapeIvy$_getDependencies_closure4()) {
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
               var0[0] = "each";
               var0[1] = "dependencies";
               var0[2] = "moduleDescriptor";
               var0[3] = "each";
               var0[4] = "artifacts";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[5];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$grape$GrapeIvy$_getDependencies_closure4(), var0);
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
            private static Class $get$$class$org$apache$ivy$core$event$resolve$StartResolveEvent() {
               Class var10000 = $class$org$apache$ivy$core$event$resolve$StartResolveEvent;
               if (var10000 == null) {
                  var10000 = $class$org$apache$ivy$core$event$resolve$StartResolveEvent = class$("org.apache.ivy.core.event.resolve.StartResolveEvent");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$groovy$grape$GrapeIvy$_getDependencies_closure4() {
               Class var10000 = $class$groovy$grape$GrapeIvy$_getDependencies_closure4;
               if (var10000 == null) {
                  var10000 = $class$groovy$grape$GrapeIvy$_getDependencies_closure4 = class$("groovy.grape.GrapeIvy$_getDependencies_closure4");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$org$apache$ivy$core$event$download$PrepareDownloadEvent() {
               Class var10000 = $class$org$apache$ivy$core$event$download$PrepareDownloadEvent;
               if (var10000 == null) {
                  var10000 = $class$org$apache$ivy$core$event$download$PrepareDownloadEvent = class$("org.apache.ivy.core.event.download.PrepareDownloadEvent");
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
         }}), $get$$class$org$apache$ivy$core$event$IvyListener()), $get$$class$org$apache$ivy$core$event$IvyListener()));
      }

      ResolveReport report = (ResolveReport)ScriptBytecodeAdapter.castToType(var3[139].call(this.ivyInstance, md, resolveOptions), $get$$class$org$apache$ivy$core$report$ResolveReport());
      if (DefaultTypeTransformation.booleanUnbox(var3[140].call(report))) {
         throw (Throwable)var3[141].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{var3[142].callGetProperty(report)}, new String[]{"Error grabbing Grapes -- ", ""})));
      } else {
         if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var3[143].callGetProperty(report)) && DefaultTypeTransformation.booleanUnbox(reportDownloads) ? Boolean.TRUE : Boolean.FALSE)) {
            var3[144].call(var3[145].callGetProperty($get$$class$java$lang$System()), (Object)(new GStringImpl(new Object[]{var3[146].call(var3[147].callGetProperty(report), (Object)$const$3), var3[148].callGetProperty(report), var3[149].call(ScriptBytecodeAdapter.invokeMethod0SpreadSafe($get$$class$groovy$grape$GrapeIvy(), var3[150].callGetProperty(report), "toString"), (Object)"\n  ")}, new String[]{"Downloaded ", " Kbytes in ", "ms:\n  ", ""})));
         }

         md = var3[151].callGetProperty(report);
         if (!DefaultTypeTransformation.booleanUnbox(var3[152].callGetProperty(args))) {
            var3[153].call(var3[154].call(cacheManager, (Object)var3[155].callGetProperty(md)));
            var3[156].call(var3[157].call(cacheManager, (Object)var3[158].callGetProperty(md)));
         }

         return (ResolveReport)ScriptBytecodeAdapter.castToType(report, $get$$class$org$apache$ivy$core$report$ResolveReport());
      }
   }

   private Object addExcludesIfNeeded(Map args, DefaultModuleDescriptor md) {
      DefaultModuleDescriptor md = new Reference(md);
      CallSite[] var4 = $getCallSiteArray();
      return !DefaultTypeTransformation.booleanUnbox(var4[159].call(args, (Object)"excludes")) ? null : var4[160].call(var4[161].callGetProperty(args), (Object)(new GeneratedClosure(this, this, md) {
         private Reference<T> md;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$apache$ivy$core$module$descriptor$DefaultModuleDescriptor;
         // $FF: synthetic field
         private static Class $class$org$apache$ivy$plugins$matcher$PatternMatcher;
         // $FF: synthetic field
         private static Class $class$groovy$grape$GrapeIvy$_addExcludesIfNeeded_closure5;
         // $FF: synthetic field
         private static Class $class$org$apache$ivy$core$module$id$ArtifactId;
         // $FF: synthetic field
         private static Class $class$org$apache$ivy$core$module$descriptor$DefaultExcludeRule;
         // $FF: synthetic field
         private static Class $class$org$apache$ivy$core$module$id$ModuleId;
         // $FF: synthetic field
         private static Class $class$org$apache$ivy$plugins$matcher$ExactPatternMatcher;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.md = (Reference)md;
         }

         public Object doCall(Object map) {
            Object mapx = new Reference(map);
            CallSite[] var3 = $getCallSiteArray();
            Object excludeRule = var3[0].callConstructor($get$$class$org$apache$ivy$core$module$descriptor$DefaultExcludeRule(), var3[1].callConstructor($get$$class$org$apache$ivy$core$module$id$ArtifactId(), var3[2].callConstructor($get$$class$org$apache$ivy$core$module$id$ModuleId(), var3[3].callGetProperty(mapx.get()), var3[4].callGetProperty(mapx.get())), var3[5].callGetProperty($get$$class$org$apache$ivy$plugins$matcher$PatternMatcher()), var3[6].callGetProperty($get$$class$org$apache$ivy$plugins$matcher$PatternMatcher()), var3[7].callGetProperty($get$$class$org$apache$ivy$plugins$matcher$PatternMatcher())), var3[8].callGetProperty($get$$class$org$apache$ivy$plugins$matcher$ExactPatternMatcher()), (Object)null);
            var3[9].call(excludeRule, (Object)"default");
            return var3[10].call(this.md.get(), excludeRule);
         }

         public DefaultModuleDescriptor getMd() {
            CallSite[] var1 = $getCallSiteArray();
            return (DefaultModuleDescriptor)ScriptBytecodeAdapter.castToType(this.md.get(), $get$$class$org$apache$ivy$core$module$descriptor$DefaultModuleDescriptor());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$grape$GrapeIvy$_addExcludesIfNeeded_closure5()) {
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
            var0[0] = "<$constructor$>";
            var0[1] = "<$constructor$>";
            var0[2] = "<$constructor$>";
            var0[3] = "group";
            var0[4] = "module";
            var0[5] = "ANY_EXPRESSION";
            var0[6] = "ANY_EXPRESSION";
            var0[7] = "ANY_EXPRESSION";
            var0[8] = "INSTANCE";
            var0[9] = "addConfiguration";
            var0[10] = "addExcludeRule";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[11];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$grape$GrapeIvy$_addExcludesIfNeeded_closure5(), var0);
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
         private static Class $get$$class$org$apache$ivy$core$module$descriptor$DefaultModuleDescriptor() {
            Class var10000 = $class$org$apache$ivy$core$module$descriptor$DefaultModuleDescriptor;
            if (var10000 == null) {
               var10000 = $class$org$apache$ivy$core$module$descriptor$DefaultModuleDescriptor = class$("org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$apache$ivy$plugins$matcher$PatternMatcher() {
            Class var10000 = $class$org$apache$ivy$plugins$matcher$PatternMatcher;
            if (var10000 == null) {
               var10000 = $class$org$apache$ivy$plugins$matcher$PatternMatcher = class$("org.apache.ivy.plugins.matcher.PatternMatcher");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$grape$GrapeIvy$_addExcludesIfNeeded_closure5() {
            Class var10000 = $class$groovy$grape$GrapeIvy$_addExcludesIfNeeded_closure5;
            if (var10000 == null) {
               var10000 = $class$groovy$grape$GrapeIvy$_addExcludesIfNeeded_closure5 = class$("groovy.grape.GrapeIvy$_addExcludesIfNeeded_closure5");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$apache$ivy$core$module$id$ArtifactId() {
            Class var10000 = $class$org$apache$ivy$core$module$id$ArtifactId;
            if (var10000 == null) {
               var10000 = $class$org$apache$ivy$core$module$id$ArtifactId = class$("org.apache.ivy.core.module.id.ArtifactId");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$apache$ivy$core$module$descriptor$DefaultExcludeRule() {
            Class var10000 = $class$org$apache$ivy$core$module$descriptor$DefaultExcludeRule;
            if (var10000 == null) {
               var10000 = $class$org$apache$ivy$core$module$descriptor$DefaultExcludeRule = class$("org.apache.ivy.core.module.descriptor.DefaultExcludeRule");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$apache$ivy$core$module$id$ModuleId() {
            Class var10000 = $class$org$apache$ivy$core$module$id$ModuleId;
            if (var10000 == null) {
               var10000 = $class$org$apache$ivy$core$module$id$ModuleId = class$("org.apache.ivy.core.module.id.ModuleId");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$apache$ivy$plugins$matcher$ExactPatternMatcher() {
            Class var10000 = $class$org$apache$ivy$plugins$matcher$ExactPatternMatcher;
            if (var10000 == null) {
               var10000 = $class$org$apache$ivy$plugins$matcher$ExactPatternMatcher = class$("org.apache.ivy.plugins.matcher.ExactPatternMatcher");
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

   public Map<String, Map<String, List<String>>> enumerateGrapes() {
      CallSite[] var1 = $getCallSiteArray();
      Map bunches = new Reference(ScriptBytecodeAdapter.createMap(new Object[0]));
      Pattern ivyFilePattern = new Reference((Pattern)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.bitwiseNegate("ivy-(.*)\\.xml"), $get$$class$java$util$regex$Pattern()));
      var1[162].call(var1[163].callGroovyObjectGetProperty(this), (Object)(new GeneratedClosure(this, this, bunches, ivyFilePattern) {
         private Reference<T> bunches;
         private Reference<T> ivyFilePattern;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$util$regex$Pattern;
         // $FF: synthetic field
         private static Class $class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6;
         // $FF: synthetic field
         private static Class $class$java$util$Map;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.bunches = (Reference)bunches;
            this.ivyFilePattern = (Reference)ivyFilePattern;
         }

         public Object doCall(File groupDir) {
            File groupDirx = new Reference(groupDir);
            CallSite[] var3 = $getCallSiteArray();
            Map grapes = new Reference(ScriptBytecodeAdapter.createMap(new Object[0]));
            CallSite var10000 = var3[0];
            Object var10001 = this.bunches.get();
            Object var10002 = var3[1].callGetProperty(groupDirx.get());
            Object var5 = grapes.get();
            var10000.call(var10001, var10002, var5);
            var10000 = var3[2];
            var10001 = groupDirx.get();
            Object var10005 = this.getThisObject();
            Reference ivyFilePattern = this.ivyFilePattern;
            return var10000.call(var10001, (Object)(new GeneratedClosure(this, var10005, grapes, ivyFilePattern) {
               private Reference<T> grapes;
               private Reference<T> ivyFilePattern;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$util$regex$Pattern;
               // $FF: synthetic field
               private static Class $class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6_closure14;
               // $FF: synthetic field
               private static Class $class$java$util$Map;

               public {
                  Reference ivyFilePatternx = new Reference(ivyFilePattern);
                  CallSite[] var6 = $getCallSiteArray();
                  this.grapes = (Reference)grapes;
                  this.ivyFilePattern = (Reference)((Reference)ivyFilePatternx.get());
               }

               public Object doCall(File moduleDir) {
                  File moduleDirx = new Reference(moduleDir);
                  CallSite[] var3 = $getCallSiteArray();
                  Object versions = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
                  CallSite var10000 = var3[0];
                  Object var10001 = moduleDirx.get();
                  Object var10002 = this.ivyFilePattern.get();
                  Object var10006 = this.getThisObject();
                  Reference ivyFilePattern = this.ivyFilePattern;
                  var10000.call(var10001, var10002, new GeneratedClosure(this, var10006, versions, ivyFilePattern) {
                     private Reference<T> versions;
                     private Reference<T> ivyFilePattern;
                     // $FF: synthetic field
                     private static final Integer $const$0 = (Integer)1;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$util$regex$Pattern;
                     // $FF: synthetic field
                     private static Class $class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6_closure14_closure15;

                     public {
                        Reference ivyFilePatternx = new Reference(ivyFilePattern);
                        CallSite[] var6 = $getCallSiteArray();
                        this.versions = (Reference)versions;
                        this.ivyFilePattern = (Reference)((Reference)ivyFilePatternx.get());
                     }

                     public Object doCall(File ivyFile) {
                        File ivyFilex = new Reference(ivyFile);
                        CallSite[] var3 = $getCallSiteArray();
                        Object m = var3[0].call(this.ivyFilePattern.get(), var3[1].callGetProperty(ivyFilex.get()));
                        if (DefaultTypeTransformation.booleanUnbox(var3[2].call(m))) {
                           Object var10000 = var3[3].call(this.versions.get(), var3[4].call(m, (Object)$const$0));
                           this.versions.set(var10000);
                           return var10000;
                        } else {
                           return null;
                        }
                     }

                     public Object call(File ivyFile) {
                        File ivyFilex = new Reference(ivyFile);
                        CallSite[] var3 = $getCallSiteArray();
                        return var3[5].callCurrent(this, (Object)ivyFilex.get());
                     }

                     public Object getVersions() {
                        CallSite[] var1 = $getCallSiteArray();
                        return this.versions.get();
                     }

                     public Pattern getIvyFilePattern() {
                        CallSite[] var1 = $getCallSiteArray();
                        return (Pattern)ScriptBytecodeAdapter.castToType(this.ivyFilePattern.get(), $get$$class$java$util$regex$Pattern());
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6_closure14_closure15()) {
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
                        var0[0] = "matcher";
                        var0[1] = "name";
                        var0[2] = "matches";
                        var0[3] = "plus";
                        var0[4] = "group";
                        var0[5] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[6];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6_closure14_closure15(), var0);
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
                     private static Class $get$$class$java$util$regex$Pattern() {
                        Class var10000 = $class$java$util$regex$Pattern;
                        if (var10000 == null) {
                           var10000 = $class$java$util$regex$Pattern = class$("java.util.regex.Pattern");
                        }

                        return var10000;
                     }

                     // $FF: synthetic method
                     private static Class $get$$class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6_closure14_closure15() {
                        Class var10000 = $class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6_closure14_closure15;
                        if (var10000 == null) {
                           var10000 = $class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6_closure14_closure15 = class$("groovy.grape.GrapeIvy$_enumerateGrapes_closure6_closure14_closure15");
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
                  });
                  var10000 = var3[1];
                  var10001 = this.grapes.get();
                  var10002 = var3[2].callGetProperty(moduleDirx.get());
                  Object var6 = versions.get();
                  var10000.call(var10001, var10002, var6);
                  return var6;
               }

               public Object call(File moduleDir) {
                  File moduleDirx = new Reference(moduleDir);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[3].callCurrent(this, (Object)moduleDirx.get());
               }

               public Map<String, List<String>> getGrapes() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (Map)ScriptBytecodeAdapter.castToType(this.grapes.get(), $get$$class$java$util$Map());
               }

               public Pattern getIvyFilePattern() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (Pattern)ScriptBytecodeAdapter.castToType(this.ivyFilePattern.get(), $get$$class$java$util$regex$Pattern());
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6_closure14()) {
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
                  var0[0] = "eachFileMatch";
                  var0[1] = "putAt";
                  var0[2] = "name";
                  var0[3] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[4];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6_closure14(), var0);
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
               private static Class $get$$class$java$util$regex$Pattern() {
                  Class var10000 = $class$java$util$regex$Pattern;
                  if (var10000 == null) {
                     var10000 = $class$java$util$regex$Pattern = class$("java.util.regex.Pattern");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6_closure14() {
                  Class var10000 = $class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6_closure14;
                  if (var10000 == null) {
                     var10000 = $class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6_closure14 = class$("groovy.grape.GrapeIvy$_enumerateGrapes_closure6_closure14");
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
               static Class class$(String var0) {
                  try {
                     return Class.forName(var0);
                  } catch (ClassNotFoundException var2) {
                     throw new NoClassDefFoundError(var2.getMessage());
                  }
               }
            }));
         }

         public Object call(File groupDir) {
            File groupDirx = new Reference(groupDir);
            CallSite[] var3 = $getCallSiteArray();
            return var3[3].callCurrent(this, (Object)groupDirx.get());
         }

         public Map<String, Map<String, List<String>>> getBunches() {
            CallSite[] var1 = $getCallSiteArray();
            return (Map)ScriptBytecodeAdapter.castToType(this.bunches.get(), $get$$class$java$util$Map());
         }

         public Pattern getIvyFilePattern() {
            CallSite[] var1 = $getCallSiteArray();
            return (Pattern)ScriptBytecodeAdapter.castToType(this.ivyFilePattern.get(), $get$$class$java$util$regex$Pattern());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6()) {
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
            var0[0] = "putAt";
            var0[1] = "name";
            var0[2] = "eachDir";
            var0[3] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[4];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6(), var0);
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
         private static Class $get$$class$java$util$regex$Pattern() {
            Class var10000 = $class$java$util$regex$Pattern;
            if (var10000 == null) {
               var10000 = $class$java$util$regex$Pattern = class$("java.util.regex.Pattern");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6() {
            Class var10000 = $class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6;
            if (var10000 == null) {
               var10000 = $class$groovy$grape$GrapeIvy$_enumerateGrapes_closure6 = class$("groovy.grape.GrapeIvy$_enumerateGrapes_closure6");
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
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      }));
      return (Map)ScriptBytecodeAdapter.castToType(bunches.get(), $get$$class$java$util$Map());
   }

   public URI[] resolve(Map args, Map... dependencies) {
      CallSite[] var3 = $getCallSiteArray();
      return (URI[])ScriptBytecodeAdapter.castToType(var3[164].callCurrent(this, args, (Object)null, dependencies), $get$array$$class$java$net$URI());
   }

   public URI[] resolve(Map args, List depsInfo, Map... dependencies) {
      CallSite[] var4 = $getCallSiteArray();
      CallSite var10000 = var4[165];
      Object[] var10002 = new Object[]{"classLoader", var4[166].call(args, (Object)"classLoader"), "refObject", var4[167].call(args, (Object)"refObject"), "calleeDepth", null};
      Object var10005 = var4[168].callGetProperty(args);
      if (!DefaultTypeTransformation.booleanUnbox(var10005)) {
         var10005 = DefaultTypeTransformation.box(DEFAULT_DEPTH);
      }

      var10002[5] = var10005;
      Object loader = var10000.callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(var10002));
      return !DefaultTypeTransformation.booleanUnbox(loader) ? (URI[])ScriptBytecodeAdapter.castToType((Object)null, $get$array$$class$java$net$URI()) : (URI[])ScriptBytecodeAdapter.castToType(var4[169].callCurrent(this, loader, args, depsInfo, dependencies), $get$array$$class$java$net$URI());
   }

   public URI[] resolve(ClassLoader loader, Map args, Map... dependencies) {
      CallSite[] var4 = $getCallSiteArray();
      return (URI[])ScriptBytecodeAdapter.castToType(var4[170].callCurrent(this, loader, args, (Object)null, dependencies), $get$array$$class$java$net$URI());
   }

   public URI[] resolve(ClassLoader loader, Map args, List depsInfo, Map... dependencies) {
      List depsInfo = new Reference(depsInfo);
      CallSite[] var6 = $getCallSiteArray();
      Set keys = new Reference((Set)ScriptBytecodeAdapter.castToType(var6[171].call(args), $get$$class$java$util$Set()));
      var6[172].call(keys.get(), (Object)(new GeneratedClosure(this, this, keys) {
         private Reference<T> keys;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$grape$GrapeIvy$_resolve_closure7;
         // $FF: synthetic field
         private static Class $class$java$util$Set;
         // $FF: synthetic field
         private static Class $class$java$lang$RuntimeException;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.keys = (Reference)keys;
         }

         public Object doCall(Object a) {
            Object ax = new Reference(a);
            CallSite[] var3 = $getCallSiteArray();
            Set badArgs = new Reference((Set)ScriptBytecodeAdapter.castToType(var3[0].call(var3[1].callGroovyObjectGetProperty(this), ax.get()), $get$$class$java$util$Set()));
            if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(badArgs.get()) && !DefaultTypeTransformation.booleanUnbox(var3[2].call(badArgs.get(), this.keys.get())) ? Boolean.TRUE : Boolean.FALSE)) {
               throw (Throwable)var3[3].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{var3[4].call(var3[5].call(this.keys.get(), badArgs.get()), ax.get())}, new String[]{"Mutually exclusive arguments passed into grab: ", ""})));
            } else {
               return null;
            }
         }

         public Set getKeys() {
            CallSite[] var1 = $getCallSiteArray();
            return (Set)ScriptBytecodeAdapter.castToType(this.keys.get(), $get$$class$java$util$Set());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$grape$GrapeIvy$_resolve_closure7()) {
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
            var0[0] = "getAt";
            var0[1] = "exclusiveGrabArgs";
            var0[2] = "disjoint";
            var0[3] = "<$constructor$>";
            var0[4] = "plus";
            var0[5] = "intersect";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[6];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$grape$GrapeIvy$_resolve_closure7(), var0);
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
         private static Class $get$$class$groovy$grape$GrapeIvy$_resolve_closure7() {
            Class var10000 = $class$groovy$grape$GrapeIvy$_resolve_closure7;
            if (var10000 == null) {
               var10000 = $class$groovy$grape$GrapeIvy$_resolve_closure7 = class$("groovy.grape.GrapeIvy$_resolve_closure7");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$util$Set() {
            Class var10000 = $class$java$util$Set;
            if (var10000 == null) {
               var10000 = $class$java$util$Set = class$("java.util.Set");
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
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      }));
      if (!DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.enableGrapes))) {
         return (URI[])ScriptBytecodeAdapter.castToType((Object)null, $get$array$$class$java$net$URI());
      } else {
         Boolean populateDepsInfo = (Boolean)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.compareNotEqual(depsInfo.get(), (Object)null) ? Boolean.TRUE : Boolean.FALSE, $get$$class$java$lang$Boolean());
         Set localDeps = new Reference((Set)ScriptBytecodeAdapter.castToType(var6[173].callCurrent(this, (Object)loader), $get$$class$java$util$Set()));
         var6[174].call(dependencies, (Object)(new GeneratedClosure(this, this, localDeps) {
            private Reference<T> localDeps;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$grape$GrapeIvy$_resolve_closure8;
            // $FF: synthetic field
            private static Class $class$java$util$Set;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$groovy$grape$IvyGrabRecord;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.localDeps = (Reference)localDeps;
            }

            public Object doCall(Object it) {
               Object itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               IvyGrabRecord igr = (IvyGrabRecord)ScriptBytecodeAdapter.castToType(var3[0].callCurrent(this, (Object)itx.get()), $get$$class$groovy$grape$IvyGrabRecord());
               var3[1].call(var3[2].callGroovyObjectGetProperty(this), (Object)igr);
               return var3[3].call(this.localDeps.get(), (Object)igr);
            }

            public Set<IvyGrabRecord> getLocalDeps() {
               CallSite[] var1 = $getCallSiteArray();
               return (Set)ScriptBytecodeAdapter.castToType(this.localDeps.get(), $get$$class$java$util$Set());
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$grape$GrapeIvy$_resolve_closure8()) {
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
               var0[0] = "createGrabRecord";
               var0[1] = "add";
               var0[2] = "grabRecordsForCurrDependencies";
               var0[3] = "add";
               var0[4] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[5];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$grape$GrapeIvy$_resolve_closure8(), var0);
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
            private static Class $get$$class$groovy$grape$GrapeIvy$_resolve_closure8() {
               Class var10000 = $class$groovy$grape$GrapeIvy$_resolve_closure8;
               if (var10000 == null) {
                  var10000 = $class$groovy$grape$GrapeIvy$_resolve_closure8 = class$("groovy.grape.GrapeIvy$_resolve_closure8");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$java$util$Set() {
               Class var10000 = $class$java$util$Set;
               if (var10000 == null) {
                  var10000 = $class$java$util$Set = class$("java.util.Set");
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
            private static Class $get$$class$groovy$grape$IvyGrabRecord() {
               Class var10000 = $class$groovy$grape$IvyGrabRecord;
               if (var10000 == null) {
                  var10000 = $class$groovy$grape$IvyGrabRecord = class$("groovy.grape.IvyGrabRecord");
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
         CallSite var10000 = var6[175];
         Object[] var10002 = new Object[]{args};
         Object[] var10003 = new Object[]{var6[176].call(var6[177].call(localDeps.get()))};
         int[] var10 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(1, Integer.TYPE))};
         ResolveReport report = (ResolveReport)ScriptBytecodeAdapter.castToType(var10000.callCurrent(this, (Object[])ScriptBytecodeAdapter.despreadList(var10002, var10003, var10)), $get$$class$org$apache$ivy$core$report$ResolveReport());
         List results = ScriptBytecodeAdapter.createList(new Object[0]);
         ArtifactDownloadReport deps = null;
         Object var14 = var6[178].call(var6[179].callGetProperty(report));

         while(((Iterator)var14).hasNext()) {
            deps = ((Iterator)var14).next();
            if (DefaultTypeTransformation.booleanUnbox(var6[180].callGetProperty(deps))) {
               results = (List)ScriptBytecodeAdapter.castToType(var6[181].call(results, (Object)var6[182].call(var6[183].callGetProperty(deps))), $get$$class$java$util$List());
            }
         }

         if (DefaultTypeTransformation.booleanUnbox(populateDepsInfo)) {
            deps = var6[184].callGetProperty(report);
            var6[185].call(deps, (Object)(new GeneratedClosure(this, this, depsInfo) {
               private Reference<T> depsInfo;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$grape$GrapeIvy$_resolve_closure9;
               // $FF: synthetic field
               private static Class $class$java$util$List;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.depsInfo = (Reference)depsInfo;
               }

               public Object doCall(Object depNode) {
                  Object depNodex = new Reference(depNode);
                  CallSite[] var3 = $getCallSiteArray();
                  Object id = var3[0].callGetProperty(depNodex.get());
                  return var3[1].call(this.depsInfo.get(), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"group", var3[2].callGetProperty(id), "module", var3[3].callGetProperty(id), "revision", var3[4].callGetProperty(id)}));
               }

               public List getDepsInfo() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (List)ScriptBytecodeAdapter.castToType(this.depsInfo.get(), $get$$class$java$util$List());
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$grape$GrapeIvy$_resolve_closure9()) {
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
                  var0[0] = "id";
                  var0[1] = "leftShift";
                  var0[2] = "organisation";
                  var0[3] = "name";
                  var0[4] = "revision";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[5];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$grape$GrapeIvy$_resolve_closure9(), var0);
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
               private static Class $get$$class$groovy$grape$GrapeIvy$_resolve_closure9() {
                  Class var10000 = $class$groovy$grape$GrapeIvy$_resolve_closure9;
                  if (var10000 == null) {
                     var10000 = $class$groovy$grape$GrapeIvy$_resolve_closure9 = class$("groovy.grape.GrapeIvy$_resolve_closure9");
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
               static Class class$(String var0) {
                  try {
                     return Class.forName(var0);
                  } catch (ClassNotFoundException var2) {
                     throw new NoClassDefFoundError(var2.getMessage());
                  }
               }
            }));
         }

         return (URI[])ScriptBytecodeAdapter.castToType((URI[])ScriptBytecodeAdapter.asType(results, $get$array$$class$java$net$URI()), $get$array$$class$java$net$URI());
      }
   }

   private Set<IvyGrabRecord> getLoadedDepsForLoader(ClassLoader loader) {
      CallSite[] var2 = $getCallSiteArray();
      Set localDeps = (Set)ScriptBytecodeAdapter.castToType(var2[186].call(this.loadedDeps, (Object)loader), $get$$class$java$util$Set());
      if (ScriptBytecodeAdapter.compareEqual(localDeps, (Object)null)) {
         localDeps = var2[187].callConstructor($get$$class$java$util$LinkedHashSet());
         var2[188].call(this.loadedDeps, loader, localDeps);
      }

      return (Set)ScriptBytecodeAdapter.castToType(localDeps, $get$$class$java$util$Set());
   }

   public Map[] listDependencies(ClassLoader classLoader) {
      CallSite[] var2 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(var2[189].call(this.loadedDeps, (Object)classLoader))) {
         List results = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
         var2[190].call(var2[191].call(this.loadedDeps, (Object)classLoader), (Object)(new GeneratedClosure(this, this, results) {
            private Reference<T> results;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$util$List;
            // $FF: synthetic field
            private static Class $class$groovy$grape$GrapeIvy$_listDependencies_closure10;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.results = (Reference)results;
            }

            public Object doCall(IvyGrabRecord grabbed) {
               IvyGrabRecord grabbedx = new Reference(grabbed);
               CallSite[] var3 = $getCallSiteArray();
               Object dep = new Reference(ScriptBytecodeAdapter.createMap(new Object[]{"group", var3[0].callGetProperty(var3[1].callGroovyObjectGetProperty(grabbedx.get())), "module", var3[2].callGetProperty(var3[3].callGroovyObjectGetProperty(grabbedx.get())), "version", var3[4].callGetProperty(var3[5].callGroovyObjectGetProperty(grabbedx.get()))}));
               if (ScriptBytecodeAdapter.compareNotEqual(var3[6].callGroovyObjectGetProperty(grabbedx.get()), ScriptBytecodeAdapter.createList(new Object[]{"default"}))) {
                  ScriptBytecodeAdapter.setProperty(var3[7].callGroovyObjectGetProperty(grabbedx.get()), $get$$class$groovy$grape$GrapeIvy$_listDependencies_closure10(), dep.get(), "conf");
               }

               if (DefaultTypeTransformation.booleanUnbox(var3[8].callGroovyObjectGetProperty(grabbedx.get()))) {
                  ScriptBytecodeAdapter.setProperty(var3[9].callGroovyObjectGetProperty(grabbedx.get()), $get$$class$groovy$grape$GrapeIvy$_listDependencies_closure10(), dep.get(), "changing");
               }

               if (!DefaultTypeTransformation.booleanUnbox(var3[10].callGroovyObjectGetProperty(grabbedx.get()))) {
                  ScriptBytecodeAdapter.setProperty(var3[11].callGroovyObjectGetProperty(grabbedx.get()), $get$$class$groovy$grape$GrapeIvy$_listDependencies_closure10(), dep.get(), "transitive");
               }

               if (!DefaultTypeTransformation.booleanUnbox(var3[12].callGroovyObjectGetProperty(grabbedx.get()))) {
                  ScriptBytecodeAdapter.setProperty(var3[13].callGroovyObjectGetProperty(grabbedx.get()), $get$$class$groovy$grape$GrapeIvy$_listDependencies_closure10(), dep.get(), "force");
               }

               if (DefaultTypeTransformation.booleanUnbox(var3[14].callGroovyObjectGetProperty(grabbedx.get()))) {
                  ScriptBytecodeAdapter.setProperty(var3[15].callGroovyObjectGetProperty(grabbedx.get()), $get$$class$groovy$grape$GrapeIvy$_listDependencies_closure10(), dep.get(), "classifier");
               }

               if (DefaultTypeTransformation.booleanUnbox(var3[16].callGroovyObjectGetProperty(grabbedx.get()))) {
                  ScriptBytecodeAdapter.setProperty(var3[17].callGroovyObjectGetProperty(grabbedx.get()), $get$$class$groovy$grape$GrapeIvy$_listDependencies_closure10(), dep.get(), "ext");
               }

               return var3[18].call(this.results.get(), dep.get());
            }

            public Object call(IvyGrabRecord grabbed) {
               IvyGrabRecord grabbedx = new Reference(grabbed);
               CallSite[] var3 = $getCallSiteArray();
               return var3[19].callCurrent(this, (Object)grabbedx.get());
            }

            public List<Map> getResults() {
               CallSite[] var1 = $getCallSiteArray();
               return (List)ScriptBytecodeAdapter.castToType(this.results.get(), $get$$class$java$util$List());
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$grape$GrapeIvy$_listDependencies_closure10()) {
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
               var0[0] = "organisation";
               var0[1] = "mrid";
               var0[2] = "name";
               var0[3] = "mrid";
               var0[4] = "revision";
               var0[5] = "mrid";
               var0[6] = "conf";
               var0[7] = "conf";
               var0[8] = "changing";
               var0[9] = "changing";
               var0[10] = "transitive";
               var0[11] = "transitive";
               var0[12] = "force";
               var0[13] = "force";
               var0[14] = "classifier";
               var0[15] = "classifier";
               var0[16] = "ext";
               var0[17] = "ext";
               var0[18] = "leftShift";
               var0[19] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[20];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$grape$GrapeIvy$_listDependencies_closure10(), var0);
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
            private static Class $get$$class$java$util$List() {
               Class var10000 = $class$java$util$List;
               if (var10000 == null) {
                  var10000 = $class$java$util$List = class$("java.util.List");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$groovy$grape$GrapeIvy$_listDependencies_closure10() {
               Class var10000 = $class$groovy$grape$GrapeIvy$_listDependencies_closure10;
               if (var10000 == null) {
                  var10000 = $class$groovy$grape$GrapeIvy$_listDependencies_closure10 = class$("groovy.grape.GrapeIvy$_listDependencies_closure10");
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
         return (Map[])ScriptBytecodeAdapter.castToType(results.get(), $get$array$$class$java$util$Map());
      } else {
         return (Map[])ScriptBytecodeAdapter.castToType((Object)null, $get$array$$class$java$util$Map());
      }
   }

   public void addResolver(Map<String, Object> args) {
      CallSite[] var2 = $getCallSiteArray();
      ChainResolver chainResolver = (ChainResolver)ScriptBytecodeAdapter.castToType(var2[192].call(this.settings, (Object)"downloadGrapes"), $get$$class$org$apache$ivy$plugins$resolver$ChainResolver());
      CallSite var10000 = var2[193];
      Class var10001 = $get$$class$org$apache$ivy$plugins$resolver$IBiblioResolver();
      Object[] var10002 = new Object[]{"name", var2[194].callGetProperty(args), "root", var2[195].callGetProperty(args), "m2compatible", null, null, null};
      Object var10005 = var2[196].callGetProperty(args);
      if (!DefaultTypeTransformation.booleanUnbox(var10005)) {
         var10005 = Boolean.TRUE;
      }

      var10002[5] = var10005;
      var10002[6] = "settings";
      var10002[7] = this.settings;
      IBiblioResolver resolver = var10000.callConstructor(var10001, (Object)ScriptBytecodeAdapter.createMap(var10002));
      var2[197].call(chainResolver, (Object)resolver);
      this.ivyInstance = (Ivy)ScriptBytecodeAdapter.castToType((Ivy)ScriptBytecodeAdapter.castToType(var2[198].call($get$$class$org$apache$ivy$Ivy(), (Object)this.settings), $get$$class$org$apache$ivy$Ivy()), $get$$class$org$apache$ivy$Ivy());
      this.resolvedDependencies = (Set)ScriptBytecodeAdapter.castToType((Set)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$Set()), $get$$class$java$util$Set());
      this.downloadedArtifacts = (Set)ScriptBytecodeAdapter.castToType((Set)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$Set()), $get$$class$java$util$Set());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$grape$GrapeIvy()) {
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
      Class var10000 = $get$$class$groovy$grape$GrapeIvy();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$grape$GrapeIvy(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$grape$GrapeIvy(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public static final int getDEFAULT_DEPTH() {
      return DEFAULT_DEPTH;
   }

   public boolean getEnableGrapes() {
      return this.enableGrapes;
   }

   public boolean isEnableGrapes() {
      return this.enableGrapes;
   }

   public void setEnableGrapes(boolean var1) {
      this.enableGrapes = var1;
   }

   public Ivy getIvyInstance() {
      return this.ivyInstance;
   }

   public void setIvyInstance(Ivy var1) {
      this.ivyInstance = var1;
   }

   public Set<String> getResolvedDependencies() {
      return this.resolvedDependencies;
   }

   public void setResolvedDependencies(Set<String> var1) {
      this.resolvedDependencies = var1;
   }

   public Set<String> getDownloadedArtifacts() {
      return this.downloadedArtifacts;
   }

   public void setDownloadedArtifacts(Set<String> var1) {
      this.downloadedArtifacts = var1;
   }

   public Map<ClassLoader, Set<IvyGrabRecord>> getLoadedDeps() {
      return this.loadedDeps;
   }

   public void setLoadedDeps(Map<ClassLoader, Set<IvyGrabRecord>> var1) {
      this.loadedDeps = var1;
   }

   public Set<IvyGrabRecord> getGrabRecordsForCurrDependencies() {
      return this.grabRecordsForCurrDependencies;
   }

   public void setGrabRecordsForCurrDependencies(Set<IvyGrabRecord> var1) {
      this.grabRecordsForCurrDependencies = var1;
   }

   public IvySettings getSettings() {
      return this.settings;
   }

   public void setSettings(IvySettings var1) {
      this.settings = var1;
   }

   // $FF: synthetic method
   public boolean this$2$isValidTargetClassLoader(Object var1) {
      return this.isValidTargetClassLoader(var1);
   }

   // $FF: synthetic method
   public boolean this$2$isValidTargetClassLoaderClass(Class var1) {
      return this.isValidTargetClassLoaderClass(var1);
   }

   // $FF: synthetic method
   public Object this$2$addExcludesIfNeeded(Map var1, DefaultModuleDescriptor var2) {
      return this.addExcludesIfNeeded(var1, var2);
   }

   // $FF: synthetic method
   public Set this$2$getLoadedDepsForLoader(ClassLoader var1) {
      return this.getLoadedDepsForLoader(var1);
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
      var0[0] = "inject";
      var0[1] = "<$constructor$>";
      var0[2] = "<$constructor$>";
      var0[3] = "<$constructor$>";
      var0[4] = "getProperty";
      var0[5] = "<$constructor$>";
      var0[6] = "getLocalGrapeConfig";
      var0[7] = "exists";
      var0[8] = "getResource";
      var0[9] = "load";
      var0[10] = "println";
      var0[11] = "err";
      var0[12] = "plus";
      var0[13] = "canonicalPath";
      var0[14] = "message";
      var0[15] = "getResource";
      var0[16] = "load";
      var0[17] = "getGrapeCacheDir";
      var0[18] = "setVariable";
      var0[19] = "newInstance";
      var0[20] = "getProperty";
      var0[21] = "<$constructor$>";
      var0[22] = "getProperty";
      var0[23] = "<$constructor$>";
      var0[24] = "canonicalFile";
      var0[25] = "getProperty";
      var0[26] = "<$constructor$>";
      var0[27] = "<$constructor$>";
      var0[28] = "getGrapeDir";
      var0[29] = "getProperty";
      var0[30] = "getGroovyRoot";
      var0[31] = "<$constructor$>";
      var0[32] = "canonicalFile";
      var0[33] = "<$constructor$>";
      var0[34] = "getGrapeDir";
      var0[35] = "exists";
      var0[36] = "mkdirs";
      var0[37] = "isDirectory";
      var0[38] = "<$constructor$>";
      var0[39] = "classLoader";
      var0[40] = "isValidTargetClassLoader";
      var0[41] = "classLoader";
      var0[42] = "class";
      var0[43] = "refObject";
      var0[44] = "getCallingClass";
      var0[45] = "calleeDepth";
      var0[46] = "isValidTargetClassLoader";
      var0[47] = "parent";
      var0[48] = "isValidTargetClassLoader";
      var0[49] = "<$constructor$>";
      var0[50] = "isValidTargetClassLoaderClass";
      var0[51] = "class";
      var0[52] = "name";
      var0[53] = "name";
      var0[54] = "isValidTargetClassLoaderClass";
      var0[55] = "superclass";
      var0[56] = "module";
      var0[57] = "artifactId";
      var0[58] = "artifact";
      var0[59] = "<$constructor$>";
      var0[60] = "group";
      var0[61] = "groupId";
      var0[62] = "organisation";
      var0[63] = "organization";
      var0[64] = "org";
      var0[65] = "version";
      var0[66] = "revision";
      var0[67] = "rev";
      var0[68] = "newInstance";
      var0[69] = "containsKey";
      var0[70] = "force";
      var0[71] = "containsKey";
      var0[72] = "changing";
      var0[73] = "containsKey";
      var0[74] = "transitive";
      var0[75] = "conf";
      var0[76] = "scope";
      var0[77] = "configuration";
      var0[78] = "startsWith";
      var0[79] = "endsWith";
      var0[80] = "getAt";
      var0[81] = "toList";
      var0[82] = "split";
      var0[83] = "classifier";
      var0[84] = "<$constructor$>";
      var0[85] = "grab";
      var0[86] = "version";
      var0[87] = "calleeDepth";
      var0[88] = "plus";
      var0[89] = "grab";
      var0[90] = "clear";
      var0[91] = "chooseClassLoader";
      var0[92] = "remove";
      var0[93] = "remove";
      var0[94] = "calleeDepth";
      var0[95] = "iterator";
      var0[96] = "resolve";
      var0[97] = "addURL";
      var0[98] = "toURL";
      var0[99] = "getLoadedDepsForLoader";
      var0[100] = "removeAll";
      var0[101] = "clear";
      var0[102] = "noExceptions";
      var0[103] = "resolutionCacheManager";
      var0[104] = "<$constructor$>";
      var0[105] = "newInstance";
      var0[106] = "addConfiguration";
      var0[107] = "<$constructor$>";
      var0[108] = "setLastModified";
      var0[109] = "currentTimeMillis";
      var0[110] = "addExcludesIfNeeded";
      var0[111] = "iterator";
      var0[112] = "<$constructor$>";
      var0[113] = "mrid";
      var0[114] = "force";
      var0[115] = "changing";
      var0[116] = "transitive";
      var0[117] = "conf";
      var0[118] = "each";
      var0[119] = "classifier";
      var0[120] = "<$constructor$>";
      var0[121] = "name";
      var0[122] = "mrid";
      var0[123] = "ext";
      var0[124] = "classifier";
      var0[125] = "each";
      var0[126] = "addDependencyArtifact";
      var0[127] = "addDependency";
      var0[128] = "setValidate";
      var0[129] = "setOutputReport";
      var0[130] = "setConfs";
      var0[131] = "<$constructor$>";
      var0[132] = "containsKey";
      var0[133] = "validate";
      var0[134] = "autoDownload";
      var0[135] = "settings";
      var0[136] = "getProperty";
      var0[137] = "addIvyListener";
      var0[138] = "eventManager";
      var0[139] = "resolve";
      var0[140] = "hasError";
      var0[141] = "<$constructor$>";
      var0[142] = "allProblemMessages";
      var0[143] = "downloadSize";
      var0[144] = "println";
      var0[145] = "err";
      var0[146] = "rightShift";
      var0[147] = "downloadSize";
      var0[148] = "downloadTime";
      var0[149] = "join";
      var0[150] = "allArtifactsReports";
      var0[151] = "moduleDescriptor";
      var0[152] = "preserveFiles";
      var0[153] = "delete";
      var0[154] = "getResolvedIvyFileInCache";
      var0[155] = "moduleRevisionId";
      var0[156] = "delete";
      var0[157] = "getResolvedIvyPropertiesInCache";
      var0[158] = "moduleRevisionId";
      var0[159] = "containsKey";
      var0[160] = "each";
      var0[161] = "excludes";
      var0[162] = "eachDir";
      var0[163] = "grapeCacheDir";
      var0[164] = "resolve";
      var0[165] = "chooseClassLoader";
      var0[166] = "remove";
      var0[167] = "remove";
      var0[168] = "calleeDepth";
      var0[169] = "resolve";
      var0[170] = "resolve";
      var0[171] = "keySet";
      var0[172] = "each";
      var0[173] = "getLoadedDepsForLoader";
      var0[174] = "each";
      var0[175] = "getDependencies";
      var0[176] = "reverse";
      var0[177] = "asList";
      var0[178] = "iterator";
      var0[179] = "allArtifactsReports";
      var0[180] = "localFile";
      var0[181] = "plus";
      var0[182] = "toURI";
      var0[183] = "localFile";
      var0[184] = "dependencies";
      var0[185] = "each";
      var0[186] = "get";
      var0[187] = "<$constructor$>";
      var0[188] = "put";
      var0[189] = "containsKey";
      var0[190] = "each";
      var0[191] = "getAt";
      var0[192] = "getResolver";
      var0[193] = "<$constructor$>";
      var0[194] = "name";
      var0[195] = "root";
      var0[196] = "m2Compatible";
      var0[197] = "add";
      var0[198] = "newInstance";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[199];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$grape$GrapeIvy(), var0);
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
   private static Class $get$$class$org$apache$ivy$core$event$IvyListener() {
      Class var10000 = $class$org$apache$ivy$core$event$IvyListener;
      if (var10000 == null) {
         var10000 = $class$org$apache$ivy$core$event$IvyListener = class$("org.apache.ivy.core.event.IvyListener");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$reflection$ReflectionUtils() {
      Class var10000 = $class$org$codehaus$groovy$reflection$ReflectionUtils;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$reflection$ReflectionUtils = class$("org.codehaus.groovy.reflection.ReflectionUtils");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$GroovySystem() {
      Class var10000 = $class$groovy$lang$GroovySystem;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovySystem = class$("groovy.lang.GroovySystem");
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
   private static Class $get$array$$class$java$util$Map() {
      Class var10000 = array$$class$java$util$Map;
      if (var10000 == null) {
         var10000 = array$$class$java$util$Map = class$("[Ljava.util.Map;");
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
   private static Class $get$$class$org$apache$ivy$Ivy() {
      Class var10000 = $class$org$apache$ivy$Ivy;
      if (var10000 == null) {
         var10000 = $class$org$apache$ivy$Ivy = class$("org.apache.ivy.Ivy");
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
   private static Class $get$$class$java$io$File() {
      Class var10000 = $class$java$io$File;
      if (var10000 == null) {
         var10000 = $class$java$io$File = class$("java.io.File");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$ivy$core$report$ResolveReport() {
      Class var10000 = $class$org$apache$ivy$core$report$ResolveReport;
      if (var10000 == null) {
         var10000 = $class$org$apache$ivy$core$report$ResolveReport = class$("org.apache.ivy.core.report.ResolveReport");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$ivy$core$module$id$ModuleRevisionId() {
      Class var10000 = $class$org$apache$ivy$core$module$id$ModuleRevisionId;
      if (var10000 == null) {
         var10000 = $class$org$apache$ivy$core$module$id$ModuleRevisionId = class$("org.apache.ivy.core.module.id.ModuleRevisionId");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$Set() {
      Class var10000 = $class$java$util$Set;
      if (var10000 == null) {
         var10000 = $class$java$util$Set = class$("java.util.Set");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$regex$Pattern() {
      Class var10000 = $class$java$util$regex$Pattern;
      if (var10000 == null) {
         var10000 = $class$java$util$regex$Pattern = class$("java.util.regex.Pattern");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$grape$GrapeIvy() {
      Class var10000 = $class$groovy$grape$GrapeIvy;
      if (var10000 == null) {
         var10000 = $class$groovy$grape$GrapeIvy = class$("groovy.grape.GrapeIvy");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$ivy$core$resolve$ResolveOptions() {
      Class var10000 = $class$org$apache$ivy$core$resolve$ResolveOptions;
      if (var10000 == null) {
         var10000 = $class$org$apache$ivy$core$resolve$ResolveOptions = class$("org.apache.ivy.core.resolve.ResolveOptions");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$ivy$plugins$resolver$ChainResolver() {
      Class var10000 = $class$org$apache$ivy$plugins$resolver$ChainResolver;
      if (var10000 == null) {
         var10000 = $class$org$apache$ivy$plugins$resolver$ChainResolver = class$("org.apache.ivy.plugins.resolver.ChainResolver");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$array$$class$java$net$URI() {
      Class var10000 = array$$class$java$net$URI;
      if (var10000 == null) {
         var10000 = array$$class$java$net$URI = class$("[Ljava.net.URI;");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$ivy$core$module$descriptor$DefaultDependencyArtifactDescriptor() {
      Class var10000 = $class$org$apache$ivy$core$module$descriptor$DefaultDependencyArtifactDescriptor;
      if (var10000 == null) {
         var10000 = $class$org$apache$ivy$core$module$descriptor$DefaultDependencyArtifactDescriptor = class$("org.apache.ivy.core.module.descriptor.DefaultDependencyArtifactDescriptor");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$ivy$util$DefaultMessageLogger() {
      Class var10000 = $class$org$apache$ivy$util$DefaultMessageLogger;
      if (var10000 == null) {
         var10000 = $class$org$apache$ivy$util$DefaultMessageLogger = class$("org.apache.ivy.util.DefaultMessageLogger");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$ivy$util$Message() {
      Class var10000 = $class$org$apache$ivy$util$Message;
      if (var10000 == null) {
         var10000 = $class$org$apache$ivy$util$Message = class$("org.apache.ivy.util.Message");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$grape$IvyGrabRecord() {
      Class var10000 = $class$groovy$grape$IvyGrabRecord;
      if (var10000 == null) {
         var10000 = $class$groovy$grape$IvyGrabRecord = class$("groovy.grape.IvyGrabRecord");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$LinkedHashSet() {
      Class var10000 = $class$java$util$LinkedHashSet;
      if (var10000 == null) {
         var10000 = $class$java$util$LinkedHashSet = class$("java.util.LinkedHashSet");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$array$$class$java$lang$String() {
      Class var10000 = array$$class$java$lang$String;
      if (var10000 == null) {
         var10000 = array$$class$java$lang$String = class$("[Ljava.lang.String;");
      }

      return var10000;
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
   private static Class $get$$class$org$apache$ivy$core$module$descriptor$DefaultModuleDescriptor() {
      Class var10000 = $class$org$apache$ivy$core$module$descriptor$DefaultModuleDescriptor;
      if (var10000 == null) {
         var10000 = $class$org$apache$ivy$core$module$descriptor$DefaultModuleDescriptor = class$("org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$ivy$core$module$descriptor$Configuration() {
      Class var10000 = $class$org$apache$ivy$core$module$descriptor$Configuration;
      if (var10000 == null) {
         var10000 = $class$org$apache$ivy$core$module$descriptor$Configuration = class$("org.apache.ivy.core.module.descriptor.Configuration");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$System() {
      Class var10000 = $class$java$lang$System;
      if (var10000 == null) {
         var10000 = $class$java$lang$System = class$("java.lang.System");
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
   private static Class $get$$class$org$apache$ivy$core$cache$ResolutionCacheManager() {
      Class var10000 = $class$org$apache$ivy$core$cache$ResolutionCacheManager;
      if (var10000 == null) {
         var10000 = $class$org$apache$ivy$core$cache$ResolutionCacheManager = class$("org.apache.ivy.core.cache.ResolutionCacheManager");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$ivy$core$module$descriptor$DefaultDependencyDescriptor() {
      Class var10000 = $class$org$apache$ivy$core$module$descriptor$DefaultDependencyDescriptor;
      if (var10000 == null) {
         var10000 = $class$org$apache$ivy$core$module$descriptor$DefaultDependencyDescriptor = class$("org.apache.ivy.core.module.descriptor.DefaultDependencyDescriptor");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$ivy$plugins$resolver$IBiblioResolver() {
      Class var10000 = $class$org$apache$ivy$plugins$resolver$IBiblioResolver;
      if (var10000 == null) {
         var10000 = $class$org$apache$ivy$plugins$resolver$IBiblioResolver = class$("org.apache.ivy.plugins.resolver.IBiblioResolver");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$WeakHashMap() {
      Class var10000 = $class$java$util$WeakHashMap;
      if (var10000 == null) {
         var10000 = $class$java$util$WeakHashMap = class$("java.util.WeakHashMap");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$apache$ivy$core$settings$IvySettings() {
      Class var10000 = $class$org$apache$ivy$core$settings$IvySettings;
      if (var10000 == null) {
         var10000 = $class$org$apache$ivy$core$settings$IvySettings = class$("org.apache.ivy.core.settings.IvySettings");
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

   class _closure1 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$groovy$grape$GrapeIvy$_closure1;

      public _closure1(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object m, Object g) {
         Object mx = new Reference(m);
         Object gx = new Reference(g);
         CallSite[] var5 = $getCallSiteArray();
         var5[0].call(gx.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), gx, mx) {
            private Reference<T> g;
            private Reference<T> m;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$util$Set;
            // $FF: synthetic field
            private static Class $class$groovy$grape$GrapeIvy$_closure1_closure11;

            public {
               CallSite[] var5 = $getCallSiteArray();
               this.g = (Reference)g;
               this.m = (Reference)m;
            }

            public Object doCall(Object a) {
               Object ax = new Reference(a);
               CallSite[] var3 = $getCallSiteArray();
               CallSite var10000 = var3[0];
               Object var10001 = this.m.get();
               Object var10002 = ax.get();
               Set var4 = (Set)ScriptBytecodeAdapter.asType(var3[1].call(this.g.get(), ax.get()), $get$$class$java$util$Set());
               var10000.call(var10001, var10002, var4);
               return var4;
            }

            public Object getG() {
               CallSite[] var1 = $getCallSiteArray();
               return this.g.get();
            }

            public Object getM() {
               CallSite[] var1 = $getCallSiteArray();
               return this.m.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$grape$GrapeIvy$_closure1_closure11()) {
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
               var0[0] = "putAt";
               var0[1] = "minus";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[2];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$grape$GrapeIvy$_closure1_closure11(), var0);
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
            private static Class $get$$class$java$util$Set() {
               Class var10000 = $class$java$util$Set;
               if (var10000 == null) {
                  var10000 = $class$java$util$Set = class$("java.util.Set");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$groovy$grape$GrapeIvy$_closure1_closure11() {
               Class var10000 = $class$groovy$grape$GrapeIvy$_closure1_closure11;
               if (var10000 == null) {
                  var10000 = $class$groovy$grape$GrapeIvy$_closure1_closure11 = class$("groovy.grape.GrapeIvy$_closure1_closure11");
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
         return mx.get();
      }

      public Object call(Object m, Object g) {
         Object mx = new Reference(m);
         Object gx = new Reference(g);
         CallSite[] var5 = $getCallSiteArray();
         return var5[1].callCurrent(this, mx.get(), gx.get());
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$grape$GrapeIvy$_closure1()) {
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
         var0[0] = "each";
         var0[1] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[2];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$grape$GrapeIvy$_closure1(), var0);
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
      private static Class $get$$class$groovy$grape$GrapeIvy$_closure1() {
         Class var10000 = $class$groovy$grape$GrapeIvy$_closure1;
         if (var10000 == null) {
            var10000 = $class$groovy$grape$GrapeIvy$_closure1 = class$("groovy.grape.GrapeIvy$_closure1");
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
}
