package com.gzoltar.shaded.org.pitest.reloc.xstream;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterRegistry;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.DataHolder;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverterWrapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.BigDecimalConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.BigIntegerConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.BooleanConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.ByteConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.CharConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.DateConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.DoubleConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.FloatConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.IntConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.LongConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.NullConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.ShortConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.StringBufferConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.StringConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.URIConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.URLConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections.ArrayConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections.BitSetConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections.CharArrayConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections.CollectionConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections.MapConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections.PropertiesConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections.SingletonCollectionConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections.SingletonMapConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections.TreeMapConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections.TreeSetConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.ColorConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.DynamicProxyConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.EncodedByteArrayConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.FileConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.FontConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.GregorianCalendarConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.JavaClassConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.JavaFieldConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.JavaMethodConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.LocaleConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.LookAndFeelConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.SqlDateConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.SqlTimeConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.SqlTimestampConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.TextAttributeConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ExternalizableConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ReflectionConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ReflectionProvider;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.SerializableConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.ClassLoaderReference;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.DefaultConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.JVM;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.MapBackedDataHolder;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.ReferenceByIdMarshallingStrategy;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.ReferenceByXPathMarshallingStrategy;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.TreeMarshallingStrategy;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.CompositeClassLoader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.CustomObjectInputStream;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.CustomObjectOutputStream;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.SelfStreamingInstanceChecker;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamDriver;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StatefulWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml.XppDriver;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.AnnotationConfiguration;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.ArrayMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.AttributeAliasingMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.AttributeMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.CachingMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.ClassAliasingMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.DefaultImplementationsMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.DefaultMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.DynamicProxyMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.FieldAliasingMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.ImmutableTypesMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.ImplicitCollectionMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.LocalConversionMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.MapperWrapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.OuterClassMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.PackageAliasingMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.SecurityMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.SystemAttributeAliasingMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.XStream11XmlFriendlyMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.security.AnyTypePermission;
import com.gzoltar.shaded.org.pitest.reloc.xstream.security.ExplicitTypePermission;
import com.gzoltar.shaded.org.pitest.reloc.xstream.security.NoPermission;
import com.gzoltar.shaded.org.pitest.reloc.xstream.security.RegExpTypePermission;
import com.gzoltar.shaded.org.pitest.reloc.xstream.security.TypeHierarchyPermission;
import com.gzoltar.shaded.org.pitest.reloc.xstream.security.TypePermission;
import com.gzoltar.shaded.org.pitest.reloc.xstream.security.WildcardTypePermission;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotActiveException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class XStream {
   private ReflectionProvider reflectionProvider;
   private HierarchicalStreamDriver hierarchicalStreamDriver;
   private ClassLoaderReference classLoaderReference;
   private MarshallingStrategy marshallingStrategy;
   private ConverterLookup converterLookup;
   private ConverterRegistry converterRegistry;
   private Mapper mapper;
   private PackageAliasingMapper packageAliasingMapper;
   private ClassAliasingMapper classAliasingMapper;
   private FieldAliasingMapper fieldAliasingMapper;
   private AttributeAliasingMapper attributeAliasingMapper;
   private SystemAttributeAliasingMapper systemAttributeAliasingMapper;
   private AttributeMapper attributeMapper;
   private DefaultImplementationsMapper defaultImplementationsMapper;
   private ImmutableTypesMapper immutableTypesMapper;
   private ImplicitCollectionMapper implicitCollectionMapper;
   private LocalConversionMapper localConversionMapper;
   private SecurityMapper securityMapper;
   private AnnotationConfiguration annotationConfiguration;
   public static final int NO_REFERENCES = 1001;
   public static final int ID_REFERENCES = 1002;
   public static final int XPATH_RELATIVE_REFERENCES = 1003;
   public static final int XPATH_ABSOLUTE_REFERENCES = 1004;
   public static final int SINGLE_NODE_XPATH_RELATIVE_REFERENCES = 1005;
   public static final int SINGLE_NODE_XPATH_ABSOLUTE_REFERENCES = 1006;
   public static final int PRIORITY_VERY_HIGH = 10000;
   public static final int PRIORITY_NORMAL = 0;
   public static final int PRIORITY_LOW = -10;
   public static final int PRIORITY_VERY_LOW = -20;
   private static final String ANNOTATION_MAPPER_TYPE = "com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.AnnotationMapper";
   private static final Pattern IGNORE_ALL = Pattern.compile(".*");

   public XStream() {
      this((ReflectionProvider)null, (Mapper)((Mapper)null), (HierarchicalStreamDriver)(new XppDriver()));
   }

   public XStream(ReflectionProvider reflectionProvider) {
      this(reflectionProvider, (Mapper)((Mapper)null), (HierarchicalStreamDriver)(new XppDriver()));
   }

   public XStream(HierarchicalStreamDriver hierarchicalStreamDriver) {
      this((ReflectionProvider)null, (Mapper)((Mapper)null), (HierarchicalStreamDriver)hierarchicalStreamDriver);
   }

   public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver hierarchicalStreamDriver) {
      this(reflectionProvider, (Mapper)null, hierarchicalStreamDriver);
   }

   /** @deprecated */
   public XStream(ReflectionProvider reflectionProvider, Mapper mapper, HierarchicalStreamDriver driver) {
      this(reflectionProvider, driver, (ClassLoader)(new CompositeClassLoader()), mapper);
   }

   public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoaderReference classLoaderReference) {
      this(reflectionProvider, driver, (ClassLoaderReference)classLoaderReference, (Mapper)null);
   }

   /** @deprecated */
   public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoader classLoader) {
      this(reflectionProvider, driver, (ClassLoader)classLoader, (Mapper)null);
   }

   /** @deprecated */
   public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoader classLoader, Mapper mapper) {
      this(reflectionProvider, driver, new ClassLoaderReference(classLoader), mapper, new DefaultConverterLookup());
   }

   public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoaderReference classLoaderReference, Mapper mapper) {
      this(reflectionProvider, driver, classLoaderReference, mapper, new DefaultConverterLookup());
   }

   private XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoaderReference classLoader, Mapper mapper, final DefaultConverterLookup defaultConverterLookup) {
      this(reflectionProvider, driver, classLoader, mapper, new ConverterLookup() {
         public Converter lookupConverterForType(Class type) {
            return defaultConverterLookup.lookupConverterForType(type);
         }
      }, new ConverterRegistry() {
         public void registerConverter(Converter converter, int priority) {
            defaultConverterLookup.registerConverter(converter, priority);
         }
      });
   }

   /** @deprecated */
   public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoader classLoader, Mapper mapper, ConverterLookup converterLookup, ConverterRegistry converterRegistry) {
      this(reflectionProvider, driver, new ClassLoaderReference(classLoader), mapper, converterLookup, converterRegistry);
   }

   public XStream(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoaderReference classLoaderReference, Mapper mapper, ConverterLookup converterLookup, ConverterRegistry converterRegistry) {
      if (reflectionProvider == null) {
         reflectionProvider = JVM.newReflectionProvider();
      }

      this.reflectionProvider = reflectionProvider;
      this.hierarchicalStreamDriver = driver;
      this.classLoaderReference = classLoaderReference;
      this.converterLookup = converterLookup;
      this.converterRegistry = converterRegistry;
      this.mapper = mapper == null ? this.buildMapper() : mapper;
      this.setupMappers();
      this.setupSecurity();
      this.setupAliases();
      this.setupDefaultImplementations();
      this.setupConverters();
      this.setupImmutableTypes();
      this.setMode(1003);
   }

   private Mapper buildMapper() {
      Mapper mapper = new DefaultMapper(this.classLoaderReference);
      if (this.useXStream11XmlFriendlyMapper()) {
         mapper = new XStream11XmlFriendlyMapper((Mapper)mapper);
      }

      Mapper mapper = new DynamicProxyMapper((Mapper)mapper);
      Mapper mapper = new PackageAliasingMapper(mapper);
      Mapper mapper = new ClassAliasingMapper(mapper);
      Mapper mapper = new FieldAliasingMapper(mapper);
      Mapper mapper = new AttributeAliasingMapper(mapper);
      Mapper mapper = new SystemAttributeAliasingMapper(mapper);
      Mapper mapper = new ImplicitCollectionMapper(mapper);
      Mapper mapper = new OuterClassMapper(mapper);
      Mapper mapper = new ArrayMapper(mapper);
      Mapper mapper = new DefaultImplementationsMapper(mapper);
      mapper = new AttributeMapper(mapper, this.converterLookup, this.reflectionProvider);
      if (JVM.is15()) {
         mapper = this.buildMapperDynamically("com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.EnumMapper", new Class[]{Mapper.class}, new Object[]{mapper});
      }

      Mapper mapper = new LocalConversionMapper((Mapper)mapper);
      mapper = new ImmutableTypesMapper(mapper);
      if (JVM.is18()) {
         mapper = this.buildMapperDynamically("com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.LambdaMapper", new Class[]{Mapper.class}, new Object[]{mapper});
      }

      mapper = new SecurityMapper((Mapper)mapper);
      if (JVM.is15()) {
         mapper = this.buildMapperDynamically("com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.AnnotationMapper", new Class[]{Mapper.class, ConverterRegistry.class, ConverterLookup.class, ClassLoaderReference.class, ReflectionProvider.class}, new Object[]{mapper, this.converterRegistry, this.converterLookup, this.classLoaderReference, this.reflectionProvider});
      }

      Mapper mapper = this.wrapMapper((MapperWrapper)mapper);
      Mapper mapper = new CachingMapper(mapper);
      return mapper;
   }

   private Mapper buildMapperDynamically(String className, Class[] constructorParamTypes, Object[] constructorParamValues) {
      try {
         Class type = Class.forName(className, false, this.classLoaderReference.getReference());
         Constructor constructor = type.getConstructor(constructorParamTypes);
         return (Mapper)constructor.newInstance(constructorParamValues);
      } catch (Exception var6) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("Could not instantiate mapper : " + className, var6);
      } catch (LinkageError var7) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("Could not instantiate mapper : " + className, var7);
      }
   }

   protected MapperWrapper wrapMapper(MapperWrapper next) {
      return next;
   }

   /** @deprecated */
   protected boolean useXStream11XmlFriendlyMapper() {
      return false;
   }

   private void setupMappers() {
      this.packageAliasingMapper = (PackageAliasingMapper)this.mapper.lookupMapperOfType(PackageAliasingMapper.class);
      this.classAliasingMapper = (ClassAliasingMapper)this.mapper.lookupMapperOfType(ClassAliasingMapper.class);
      this.fieldAliasingMapper = (FieldAliasingMapper)this.mapper.lookupMapperOfType(FieldAliasingMapper.class);
      this.attributeMapper = (AttributeMapper)this.mapper.lookupMapperOfType(AttributeMapper.class);
      this.attributeAliasingMapper = (AttributeAliasingMapper)this.mapper.lookupMapperOfType(AttributeAliasingMapper.class);
      this.systemAttributeAliasingMapper = (SystemAttributeAliasingMapper)this.mapper.lookupMapperOfType(SystemAttributeAliasingMapper.class);
      this.implicitCollectionMapper = (ImplicitCollectionMapper)this.mapper.lookupMapperOfType(ImplicitCollectionMapper.class);
      this.defaultImplementationsMapper = (DefaultImplementationsMapper)this.mapper.lookupMapperOfType(DefaultImplementationsMapper.class);
      this.immutableTypesMapper = (ImmutableTypesMapper)this.mapper.lookupMapperOfType(ImmutableTypesMapper.class);
      this.localConversionMapper = (LocalConversionMapper)this.mapper.lookupMapperOfType(LocalConversionMapper.class);
      this.securityMapper = (SecurityMapper)this.mapper.lookupMapperOfType(SecurityMapper.class);
      this.annotationConfiguration = (AnnotationConfiguration)this.mapper.lookupMapperOfType(AnnotationConfiguration.class);
   }

   protected void setupSecurity() {
      if (this.securityMapper != null) {
         this.addPermission(AnyTypePermission.ANY);
      }
   }

   protected void setupAliases() {
      if (this.classAliasingMapper != null) {
         this.alias("null", Mapper.Null.class);
         this.alias("int", Integer.class);
         this.alias("float", Float.class);
         this.alias("double", Double.class);
         this.alias("long", Long.class);
         this.alias("short", Short.class);
         this.alias("char", Character.class);
         this.alias("byte", Byte.class);
         this.alias("boolean", Boolean.class);
         this.alias("number", Number.class);
         this.alias("object", Object.class);
         this.alias("big-int", BigInteger.class);
         this.alias("big-decimal", BigDecimal.class);
         this.alias("string-buffer", StringBuffer.class);
         this.alias("string", String.class);
         this.alias("java-class", Class.class);
         this.alias("method", Method.class);
         this.alias("constructor", Constructor.class);
         this.alias("field", Field.class);
         this.alias("date", Date.class);
         this.alias("uri", URI.class);
         this.alias("url", URL.class);
         this.alias("bit-set", BitSet.class);
         this.alias("map", Map.class);
         this.alias("entry", Entry.class);
         this.alias("properties", Properties.class);
         this.alias("list", List.class);
         this.alias("set", Set.class);
         this.alias("sorted-set", SortedSet.class);
         this.alias("linked-list", LinkedList.class);
         this.alias("vector", Vector.class);
         this.alias("tree-map", TreeMap.class);
         this.alias("tree-set", TreeSet.class);
         this.alias("hashtable", Hashtable.class);
         this.alias("empty-list", Collections.EMPTY_LIST.getClass());
         this.alias("empty-map", Collections.EMPTY_MAP.getClass());
         this.alias("empty-set", Collections.EMPTY_SET.getClass());
         this.alias("singleton-list", Collections.singletonList(this).getClass());
         this.alias("singleton-map", Collections.singletonMap(this, (Object)null).getClass());
         this.alias("singleton-set", Collections.singleton(this).getClass());
         if (JVM.isAWTAvailable()) {
            this.alias("awt-color", JVM.loadClassForName("java.awt.Color", false));
            this.alias("awt-font", JVM.loadClassForName("java.awt.Font", false));
            this.alias("awt-text-attribute", JVM.loadClassForName("java.awt.font.TextAttribute"));
         }

         if (JVM.isSQLAvailable()) {
            this.alias("sql-timestamp", JVM.loadClassForName("java.sql.Timestamp"));
            this.alias("sql-time", JVM.loadClassForName("java.sql.Time"));
            this.alias("sql-date", JVM.loadClassForName("java.sql.Date"));
         }

         this.alias("file", File.class);
         this.alias("locale", Locale.class);
         this.alias("gregorian-calendar", Calendar.class);
         if (JVM.is14()) {
            this.aliasDynamically("auth-subject", "javax.security.auth.Subject");
            this.alias("linked-hash-map", JVM.loadClassForName("java.util.LinkedHashMap"));
            this.alias("linked-hash-set", JVM.loadClassForName("java.util.LinkedHashSet"));
            this.alias("trace", JVM.loadClassForName("java.lang.StackTraceElement"));
            this.alias("currency", JVM.loadClassForName("java.util.Currency"));
            this.aliasType("charset", JVM.loadClassForName("java.nio.charset.Charset"));
         }

         if (JVM.is15()) {
            this.aliasDynamically("duration", "javax.xml.datatype.Duration");
            this.alias("concurrent-hash-map", JVM.loadClassForName("java.util.concurrent.ConcurrentHashMap"));
            this.alias("enum-set", JVM.loadClassForName("java.util.EnumSet"));
            this.alias("enum-map", JVM.loadClassForName("java.util.EnumMap"));
            this.alias("string-builder", JVM.loadClassForName("java.lang.StringBuilder"));
            this.alias("uuid", JVM.loadClassForName("java.util.UUID"));
         }

         if (JVM.loadClassForName("java.lang.invoke.SerializedLambda") != null) {
            this.aliasDynamically("serialized-lambda", "java.lang.invoke.SerializedLambda");
         }

      }
   }

   private void aliasDynamically(String alias, String className) {
      Class type = JVM.loadClassForName(className);
      if (type != null) {
         this.alias(alias, type);
      }

   }

   protected void setupDefaultImplementations() {
      if (this.defaultImplementationsMapper != null) {
         this.addDefaultImplementation(HashMap.class, Map.class);
         this.addDefaultImplementation(ArrayList.class, List.class);
         this.addDefaultImplementation(HashSet.class, Set.class);
         this.addDefaultImplementation(TreeSet.class, SortedSet.class);
         this.addDefaultImplementation(GregorianCalendar.class, Calendar.class);
      }
   }

   protected void setupConverters() {
      this.registerConverter((Converter)(new ReflectionConverter(this.mapper, this.reflectionProvider)), -20);
      this.registerConverter((Converter)(new SerializableConverter(this.mapper, this.reflectionProvider, this.classLoaderReference)), -10);
      this.registerConverter((Converter)(new ExternalizableConverter(this.mapper, this.classLoaderReference)), -10);
      this.registerConverter((Converter)(new NullConverter()), 10000);
      this.registerConverter((SingleValueConverter)(new IntConverter()), 0);
      this.registerConverter((SingleValueConverter)(new FloatConverter()), 0);
      this.registerConverter((SingleValueConverter)(new DoubleConverter()), 0);
      this.registerConverter((SingleValueConverter)(new LongConverter()), 0);
      this.registerConverter((SingleValueConverter)(new ShortConverter()), 0);
      this.registerConverter((Converter)(new CharConverter()), 0);
      this.registerConverter((SingleValueConverter)(new BooleanConverter()), 0);
      this.registerConverter((SingleValueConverter)(new ByteConverter()), 0);
      this.registerConverter((SingleValueConverter)(new StringConverter()), 0);
      this.registerConverter((SingleValueConverter)(new StringBufferConverter()), 0);
      this.registerConverter((SingleValueConverter)(new DateConverter()), 0);
      this.registerConverter((Converter)(new BitSetConverter()), 0);
      this.registerConverter((SingleValueConverter)(new URIConverter()), 0);
      this.registerConverter((SingleValueConverter)(new URLConverter()), 0);
      this.registerConverter((SingleValueConverter)(new BigIntegerConverter()), 0);
      this.registerConverter((SingleValueConverter)(new BigDecimalConverter()), 0);
      this.registerConverter((Converter)(new ArrayConverter(this.mapper)), 0);
      this.registerConverter((Converter)(new CharArrayConverter()), 0);
      this.registerConverter((Converter)(new CollectionConverter(this.mapper)), 0);
      this.registerConverter((Converter)(new MapConverter(this.mapper)), 0);
      this.registerConverter((Converter)(new TreeMapConverter(this.mapper)), 0);
      this.registerConverter((Converter)(new TreeSetConverter(this.mapper)), 0);
      this.registerConverter((Converter)(new SingletonCollectionConverter(this.mapper)), 0);
      this.registerConverter((Converter)(new SingletonMapConverter(this.mapper)), 0);
      this.registerConverter((Converter)(new PropertiesConverter()), 0);
      this.registerConverter((Converter)(new EncodedByteArrayConverter()), 0);
      this.registerConverter((SingleValueConverter)(new FileConverter()), 0);
      if (JVM.isSQLAvailable()) {
         this.registerConverter((SingleValueConverter)(new SqlTimestampConverter()), 0);
         this.registerConverter((SingleValueConverter)(new SqlTimeConverter()), 0);
         this.registerConverter((SingleValueConverter)(new SqlDateConverter()), 0);
      }

      this.registerConverter((Converter)(new DynamicProxyConverter(this.mapper, this.classLoaderReference)), 0);
      this.registerConverter((SingleValueConverter)(new JavaClassConverter(this.classLoaderReference)), 0);
      this.registerConverter((Converter)(new JavaMethodConverter(this.classLoaderReference)), 0);
      this.registerConverter((Converter)(new JavaFieldConverter(this.classLoaderReference)), 0);
      if (JVM.isAWTAvailable()) {
         this.registerConverter((Converter)(new FontConverter(this.mapper)), 0);
         this.registerConverter((Converter)(new ColorConverter()), 0);
         this.registerConverter((SingleValueConverter)(new TextAttributeConverter()), 0);
      }

      if (JVM.isSwingAvailable()) {
         this.registerConverter((Converter)(new LookAndFeelConverter(this.mapper, this.reflectionProvider)), 0);
      }

      this.registerConverter((SingleValueConverter)(new LocaleConverter()), 0);
      this.registerConverter((Converter)(new GregorianCalendarConverter()), 0);
      if (JVM.is14()) {
         this.registerConverterDynamically("com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.SubjectConverter", 0, new Class[]{Mapper.class}, new Object[]{this.mapper});
         this.registerConverterDynamically("com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.ThrowableConverter", 0, new Class[]{ConverterLookup.class}, new Object[]{this.converterLookup});
         this.registerConverterDynamically("com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.StackTraceElementConverter", 0, (Class[])null, (Object[])null);
         this.registerConverterDynamically("com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.CurrencyConverter", 0, (Class[])null, (Object[])null);
         this.registerConverterDynamically("com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.RegexPatternConverter", 0, (Class[])null, (Object[])null);
         this.registerConverterDynamically("com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.CharsetConverter", 0, (Class[])null, (Object[])null);
      }

      if (JVM.is15()) {
         if (JVM.loadClassForName("javax.xml.datatype.Duration") != null) {
            this.registerConverterDynamically("com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended.DurationConverter", 0, (Class[])null, (Object[])null);
         }

         this.registerConverterDynamically("com.gzoltar.shaded.org.pitest.reloc.xstream.converters.enums.EnumConverter", 0, (Class[])null, (Object[])null);
         this.registerConverterDynamically("com.gzoltar.shaded.org.pitest.reloc.xstream.converters.enums.EnumSetConverter", 0, new Class[]{Mapper.class}, new Object[]{this.mapper});
         this.registerConverterDynamically("com.gzoltar.shaded.org.pitest.reloc.xstream.converters.enums.EnumMapConverter", 0, new Class[]{Mapper.class}, new Object[]{this.mapper});
         this.registerConverterDynamically("com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.StringBuilderConverter", 0, (Class[])null, (Object[])null);
         this.registerConverterDynamically("com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.UUIDConverter", 0, (Class[])null, (Object[])null);
      }

      if (JVM.is18()) {
         this.registerConverterDynamically("com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.LambdaConverter", 0, new Class[]{Mapper.class, ReflectionProvider.class, ClassLoaderReference.class}, new Object[]{this.mapper, this.reflectionProvider, this.classLoaderReference});
      }

      this.registerConverter((Converter)(new SelfStreamingInstanceChecker(this.converterLookup, this)), 0);
   }

   private void registerConverterDynamically(String className, int priority, Class[] constructorParamTypes, Object[] constructorParamValues) {
      try {
         Class type = Class.forName(className, false, this.classLoaderReference.getReference());
         Constructor constructor = type.getConstructor(constructorParamTypes);
         Object instance = constructor.newInstance(constructorParamValues);
         if (instance instanceof Converter) {
            this.registerConverter((Converter)instance, priority);
         } else if (instance instanceof SingleValueConverter) {
            this.registerConverter((SingleValueConverter)instance, priority);
         }

      } catch (Exception var8) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("Could not instantiate converter : " + className, var8);
      } catch (LinkageError var9) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("Could not instantiate converter : " + className, var9);
      }
   }

   protected void setupImmutableTypes() {
      if (this.immutableTypesMapper != null) {
         this.addImmutableType(Boolean.TYPE);
         this.addImmutableType(Boolean.class);
         this.addImmutableType(Byte.TYPE);
         this.addImmutableType(Byte.class);
         this.addImmutableType(Character.TYPE);
         this.addImmutableType(Character.class);
         this.addImmutableType(Double.TYPE);
         this.addImmutableType(Double.class);
         this.addImmutableType(Float.TYPE);
         this.addImmutableType(Float.class);
         this.addImmutableType(Integer.TYPE);
         this.addImmutableType(Integer.class);
         this.addImmutableType(Long.TYPE);
         this.addImmutableType(Long.class);
         this.addImmutableType(Short.TYPE);
         this.addImmutableType(Short.class);
         this.addImmutableType(Mapper.Null.class);
         this.addImmutableType(BigDecimal.class);
         this.addImmutableType(BigInteger.class);
         this.addImmutableType(String.class);
         this.addImmutableType(URI.class);
         this.addImmutableType(URL.class);
         this.addImmutableType(File.class);
         this.addImmutableType(Class.class);
         this.addImmutableType(Collections.EMPTY_LIST.getClass());
         this.addImmutableType(Collections.EMPTY_SET.getClass());
         this.addImmutableType(Collections.EMPTY_MAP.getClass());
         if (JVM.isAWTAvailable()) {
            this.addImmutableTypeDynamically("java.awt.font.TextAttribute");
         }

         if (JVM.is14()) {
            this.addImmutableTypeDynamically("java.nio.charset.Charset");
            this.addImmutableTypeDynamically("java.util.Currency");
         }

      }
   }

   private void addImmutableTypeDynamically(String className) {
      Class type = JVM.loadClassForName(className);
      if (type != null) {
         this.addImmutableType(type);
      }

   }

   public void setMarshallingStrategy(MarshallingStrategy marshallingStrategy) {
      this.marshallingStrategy = marshallingStrategy;
   }

   public String toXML(Object obj) {
      Writer writer = new StringWriter();
      this.toXML(obj, (Writer)writer);
      return writer.toString();
   }

   public void toXML(Object obj, Writer out) {
      HierarchicalStreamWriter writer = this.hierarchicalStreamDriver.createWriter(out);

      try {
         this.marshal(obj, writer);
      } finally {
         writer.flush();
      }

   }

   public void toXML(Object obj, OutputStream out) {
      HierarchicalStreamWriter writer = this.hierarchicalStreamDriver.createWriter(out);

      try {
         this.marshal(obj, writer);
      } finally {
         writer.flush();
      }

   }

   public void marshal(Object obj, HierarchicalStreamWriter writer) {
      this.marshal(obj, writer, (DataHolder)null);
   }

   public void marshal(Object obj, HierarchicalStreamWriter writer, DataHolder dataHolder) {
      this.marshallingStrategy.marshal(writer, obj, this.converterLookup, this.mapper, dataHolder);
   }

   public Object fromXML(String xml) {
      return this.fromXML((Reader)(new StringReader(xml)));
   }

   public Object fromXML(Reader reader) {
      return this.unmarshal(this.hierarchicalStreamDriver.createReader(reader), (Object)null);
   }

   public Object fromXML(InputStream input) {
      return this.unmarshal(this.hierarchicalStreamDriver.createReader(input), (Object)null);
   }

   public Object fromXML(URL url) {
      return this.fromXML((URL)url, (Object)null);
   }

   public Object fromXML(File file) {
      return this.fromXML((File)file, (Object)null);
   }

   public Object fromXML(String xml, Object root) {
      return this.fromXML((Reader)(new StringReader(xml)), root);
   }

   public Object fromXML(Reader xml, Object root) {
      return this.unmarshal(this.hierarchicalStreamDriver.createReader(xml), root);
   }

   public Object fromXML(URL url, Object root) {
      return this.unmarshal(this.hierarchicalStreamDriver.createReader(url), root);
   }

   public Object fromXML(File file, Object root) {
      HierarchicalStreamReader reader = this.hierarchicalStreamDriver.createReader(file);

      Object var4;
      try {
         var4 = this.unmarshal(reader, root);
      } finally {
         reader.close();
      }

      return var4;
   }

   public Object fromXML(InputStream input, Object root) {
      return this.unmarshal(this.hierarchicalStreamDriver.createReader(input), root);
   }

   public Object unmarshal(HierarchicalStreamReader reader) {
      return this.unmarshal(reader, (Object)null, (DataHolder)null);
   }

   public Object unmarshal(HierarchicalStreamReader reader, Object root) {
      return this.unmarshal(reader, root, (DataHolder)null);
   }

   public Object unmarshal(HierarchicalStreamReader reader, Object root, DataHolder dataHolder) {
      try {
         return this.marshallingStrategy.unmarshal(root, reader, dataHolder, this.converterLookup, this.mapper);
      } catch (ConversionException var7) {
         Package pkg = this.getClass().getPackage();
         String version = pkg != null ? pkg.getImplementationVersion() : null;
         var7.add("version", version != null ? version : "not available");
         throw var7;
      }
   }

   public void alias(String name, Class type) {
      if (this.classAliasingMapper == null) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("No " + ClassAliasingMapper.class.getName() + " available");
      } else {
         this.classAliasingMapper.addClassAlias(name, type);
      }
   }

   public void aliasType(String name, Class type) {
      if (this.classAliasingMapper == null) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("No " + ClassAliasingMapper.class.getName() + " available");
      } else {
         this.classAliasingMapper.addTypeAlias(name, type);
      }
   }

   public void alias(String name, Class type, Class defaultImplementation) {
      this.alias(name, type);
      this.addDefaultImplementation(defaultImplementation, type);
   }

   public void aliasPackage(String name, String pkgName) {
      if (this.packageAliasingMapper == null) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("No " + PackageAliasingMapper.class.getName() + " available");
      } else {
         this.packageAliasingMapper.addPackageAlias(name, pkgName);
      }
   }

   public void aliasField(String alias, Class definedIn, String fieldName) {
      if (this.fieldAliasingMapper == null) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("No " + FieldAliasingMapper.class.getName() + " available");
      } else {
         this.fieldAliasingMapper.addFieldAlias(alias, definedIn, fieldName);
      }
   }

   public void aliasAttribute(String alias, String attributeName) {
      if (this.attributeAliasingMapper == null) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("No " + AttributeAliasingMapper.class.getName() + " available");
      } else {
         this.attributeAliasingMapper.addAliasFor(attributeName, alias);
      }
   }

   public void aliasSystemAttribute(String alias, String systemAttributeName) {
      if (this.systemAttributeAliasingMapper == null) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("No " + SystemAttributeAliasingMapper.class.getName() + " available");
      } else {
         this.systemAttributeAliasingMapper.addAliasFor(systemAttributeName, alias);
      }
   }

   public void aliasAttribute(Class definedIn, String attributeName, String alias) {
      this.aliasField(alias, definedIn, attributeName);
      this.useAttributeFor(definedIn, attributeName);
   }

   public void useAttributeFor(String fieldName, Class type) {
      if (this.attributeMapper == null) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("No " + AttributeMapper.class.getName() + " available");
      } else {
         this.attributeMapper.addAttributeFor(fieldName, type);
      }
   }

   public void useAttributeFor(Class definedIn, String fieldName) {
      if (this.attributeMapper == null) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("No " + AttributeMapper.class.getName() + " available");
      } else {
         this.attributeMapper.addAttributeFor(definedIn, fieldName);
      }
   }

   public void useAttributeFor(Class type) {
      if (this.attributeMapper == null) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("No " + AttributeMapper.class.getName() + " available");
      } else {
         this.attributeMapper.addAttributeFor(type);
      }
   }

   public void addDefaultImplementation(Class defaultImplementation, Class ofType) {
      if (this.defaultImplementationsMapper == null) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("No " + DefaultImplementationsMapper.class.getName() + " available");
      } else {
         this.defaultImplementationsMapper.addDefaultImplementation(defaultImplementation, ofType);
      }
   }

   public void addImmutableType(Class type) {
      if (this.immutableTypesMapper == null) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("No " + ImmutableTypesMapper.class.getName() + " available");
      } else {
         this.immutableTypesMapper.addImmutableType(type);
      }
   }

   public void registerConverter(Converter converter) {
      this.registerConverter((Converter)converter, 0);
   }

   public void registerConverter(Converter converter, int priority) {
      if (this.converterRegistry != null) {
         this.converterRegistry.registerConverter(converter, priority);
      }

   }

   public void registerConverter(SingleValueConverter converter) {
      this.registerConverter((SingleValueConverter)converter, 0);
   }

   public void registerConverter(SingleValueConverter converter, int priority) {
      if (this.converterRegistry != null) {
         this.converterRegistry.registerConverter(new SingleValueConverterWrapper(converter), priority);
      }

   }

   public void registerLocalConverter(Class definedIn, String fieldName, Converter converter) {
      if (this.localConversionMapper == null) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("No " + LocalConversionMapper.class.getName() + " available");
      } else {
         this.localConversionMapper.registerLocalConverter(definedIn, fieldName, converter);
      }
   }

   public void registerLocalConverter(Class definedIn, String fieldName, SingleValueConverter converter) {
      this.registerLocalConverter(definedIn, fieldName, (Converter)(new SingleValueConverterWrapper(converter)));
   }

   public Mapper getMapper() {
      return this.mapper;
   }

   public ReflectionProvider getReflectionProvider() {
      return this.reflectionProvider;
   }

   public ConverterLookup getConverterLookup() {
      return this.converterLookup;
   }

   public void setMode(int mode) {
      switch(mode) {
      case 1001:
         this.setMarshallingStrategy(new TreeMarshallingStrategy());
         break;
      case 1002:
         this.setMarshallingStrategy(new ReferenceByIdMarshallingStrategy());
         break;
      case 1003:
         this.setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(ReferenceByXPathMarshallingStrategy.RELATIVE));
         break;
      case 1004:
         this.setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(ReferenceByXPathMarshallingStrategy.ABSOLUTE));
         break;
      case 1005:
         this.setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(ReferenceByXPathMarshallingStrategy.RELATIVE | ReferenceByXPathMarshallingStrategy.SINGLE_NODE));
         break;
      case 1006:
         this.setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(ReferenceByXPathMarshallingStrategy.ABSOLUTE | ReferenceByXPathMarshallingStrategy.SINGLE_NODE));
         break;
      default:
         throw new IllegalArgumentException("Unknown mode : " + mode);
      }

   }

   public void addImplicitCollection(Class ownerType, String fieldName) {
      this.addImplicitCollection(ownerType, fieldName, (String)null, (Class)null);
   }

   public void addImplicitCollection(Class ownerType, String fieldName, Class itemType) {
      this.addImplicitCollection(ownerType, fieldName, (String)null, itemType);
   }

   public void addImplicitCollection(Class ownerType, String fieldName, String itemFieldName, Class itemType) {
      this.addImplicitMap(ownerType, fieldName, itemFieldName, itemType, (String)null);
   }

   public void addImplicitArray(Class ownerType, String fieldName) {
      this.addImplicitCollection(ownerType, fieldName);
   }

   public void addImplicitArray(Class ownerType, String fieldName, Class itemType) {
      this.addImplicitCollection(ownerType, fieldName, itemType);
   }

   public void addImplicitArray(Class ownerType, String fieldName, String itemName) {
      this.addImplicitCollection(ownerType, fieldName, itemName, (Class)null);
   }

   public void addImplicitMap(Class ownerType, String fieldName, Class itemType, String keyFieldName) {
      this.addImplicitMap(ownerType, fieldName, (String)null, itemType, keyFieldName);
   }

   public void addImplicitMap(Class ownerType, String fieldName, String itemName, Class itemType, String keyFieldName) {
      if (this.implicitCollectionMapper == null) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("No " + ImplicitCollectionMapper.class.getName() + " available");
      } else {
         this.implicitCollectionMapper.add(ownerType, fieldName, itemName, itemType, keyFieldName);
      }
   }

   public DataHolder newDataHolder() {
      return new MapBackedDataHolder();
   }

   public ObjectOutputStream createObjectOutputStream(Writer writer) throws IOException {
      return this.createObjectOutputStream(this.hierarchicalStreamDriver.createWriter(writer), "object-stream");
   }

   public ObjectOutputStream createObjectOutputStream(HierarchicalStreamWriter writer) throws IOException {
      return this.createObjectOutputStream(writer, "object-stream");
   }

   public ObjectOutputStream createObjectOutputStream(Writer writer, String rootNodeName) throws IOException {
      return this.createObjectOutputStream(this.hierarchicalStreamDriver.createWriter(writer), rootNodeName);
   }

   public ObjectOutputStream createObjectOutputStream(OutputStream out) throws IOException {
      return this.createObjectOutputStream(this.hierarchicalStreamDriver.createWriter(out), "object-stream");
   }

   public ObjectOutputStream createObjectOutputStream(OutputStream out, String rootNodeName) throws IOException {
      return this.createObjectOutputStream(this.hierarchicalStreamDriver.createWriter(out), rootNodeName);
   }

   public ObjectOutputStream createObjectOutputStream(HierarchicalStreamWriter writer, String rootNodeName) throws IOException {
      final StatefulWriter statefulWriter = new StatefulWriter(writer);
      statefulWriter.startNode(rootNodeName, (Class)null);
      return new CustomObjectOutputStream(new CustomObjectOutputStream.StreamCallback() {
         public void writeToStream(Object object) {
            XStream.this.marshal(object, statefulWriter);
         }

         public void writeFieldsToStream(Map fields) throws NotActiveException {
            throw new NotActiveException("not in call to writeObject");
         }

         public void defaultWriteObject() throws NotActiveException {
            throw new NotActiveException("not in call to writeObject");
         }

         public void flush() {
            statefulWriter.flush();
         }

         public void close() {
            if (statefulWriter.state() != StatefulWriter.STATE_CLOSED) {
               statefulWriter.endNode();
               statefulWriter.close();
            }

         }
      });
   }

   public ObjectInputStream createObjectInputStream(Reader xmlReader) throws IOException {
      return this.createObjectInputStream(this.hierarchicalStreamDriver.createReader(xmlReader));
   }

   public ObjectInputStream createObjectInputStream(InputStream in) throws IOException {
      return this.createObjectInputStream(this.hierarchicalStreamDriver.createReader(in));
   }

   public ObjectInputStream createObjectInputStream(final HierarchicalStreamReader reader) throws IOException {
      return new CustomObjectInputStream(new CustomObjectInputStream.StreamCallback() {
         public Object readFromStream() throws EOFException {
            if (!reader.hasMoreChildren()) {
               throw new EOFException();
            } else {
               reader.moveDown();
               Object result = XStream.this.unmarshal(reader);
               reader.moveUp();
               return result;
            }
         }

         public Map readFieldsFromStream() throws IOException {
            throw new NotActiveException("not in call to readObject");
         }

         public void defaultReadObject() throws NotActiveException {
            throw new NotActiveException("not in call to readObject");
         }

         public void registerValidation(ObjectInputValidation validation, int priority) throws NotActiveException {
            throw new NotActiveException("stream inactive");
         }

         public void close() {
            reader.close();
         }
      }, this.classLoaderReference);
   }

   public void setClassLoader(ClassLoader classLoader) {
      this.classLoaderReference.setReference(classLoader);
   }

   public ClassLoader getClassLoader() {
      return this.classLoaderReference.getReference();
   }

   public ClassLoaderReference getClassLoaderReference() {
      return this.classLoaderReference;
   }

   public void omitField(Class definedIn, String fieldName) {
      if (this.fieldAliasingMapper == null) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("No " + FieldAliasingMapper.class.getName() + " available");
      } else {
         this.fieldAliasingMapper.omitField(definedIn, fieldName);
      }
   }

   public void ignoreUnknownElements() {
      this.ignoreUnknownElements(IGNORE_ALL);
   }

   public void ignoreUnknownElements(String pattern) {
      this.ignoreUnknownElements(Pattern.compile(pattern));
   }

   public void ignoreUnknownElements(Pattern pattern) {
      if (this.fieldAliasingMapper == null) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("No " + FieldAliasingMapper.class.getName() + " available");
      } else {
         this.fieldAliasingMapper.addFieldsToIgnore(pattern);
      }
   }

   public void processAnnotations(Class[] types) {
      if (this.annotationConfiguration == null) {
         throw new com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException("No com.thoughtworks.xstream.mapper.AnnotationMapper available");
      } else {
         this.annotationConfiguration.processAnnotations(types);
      }
   }

   public void processAnnotations(Class type) {
      this.processAnnotations(new Class[]{type});
   }

   public void autodetectAnnotations(boolean mode) {
      if (this.annotationConfiguration != null) {
         this.annotationConfiguration.autodetectAnnotations(mode);
      }

   }

   public void addPermission(TypePermission permission) {
      if (this.securityMapper != null) {
         this.securityMapper.addPermission(permission);
      }

   }

   public void allowTypes(String[] names) {
      this.addPermission(new ExplicitTypePermission(names));
   }

   public void allowTypes(Class[] types) {
      this.addPermission(new ExplicitTypePermission(types));
   }

   public void allowTypeHierarchy(Class type) {
      this.addPermission(new TypeHierarchyPermission(type));
   }

   public void allowTypesByRegExp(String[] regexps) {
      this.addPermission(new RegExpTypePermission(regexps));
   }

   public void allowTypesByRegExp(Pattern[] regexps) {
      this.addPermission(new RegExpTypePermission(regexps));
   }

   public void allowTypesByWildcard(String[] patterns) {
      this.addPermission(new WildcardTypePermission(patterns));
   }

   public void denyPermission(TypePermission permission) {
      this.addPermission(new NoPermission(permission));
   }

   public void denyTypes(String[] names) {
      this.denyPermission(new ExplicitTypePermission(names));
   }

   public void denyTypes(Class[] types) {
      this.denyPermission(new ExplicitTypePermission(types));
   }

   public void denyTypeHierarchy(Class type) {
      this.denyPermission(new TypeHierarchyPermission(type));
   }

   public void denyTypesByRegExp(String[] regexps) {
      this.denyPermission(new RegExpTypePermission(regexps));
   }

   public void denyTypesByRegExp(Pattern[] regexps) {
      this.denyPermission(new RegExpTypePermission(regexps));
   }

   public void denyTypesByWildcard(String[] patterns) {
      this.denyPermission(new WildcardTypePermission(patterns));
   }

   /** @deprecated */
   public static class InitializationException extends XStreamException {
      /** @deprecated */
      public InitializationException(String message, Throwable cause) {
         super(message, cause);
      }

      /** @deprecated */
      public InitializationException(String message) {
         super(message);
      }
   }
}
