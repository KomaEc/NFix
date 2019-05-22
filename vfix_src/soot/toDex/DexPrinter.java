package soot.toDex;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.BuilderOffsetInstruction;
import org.jf.dexlib2.builder.Label;
import org.jf.dexlib2.builder.MethodImplementationBuilder;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.immutable.ImmutableAnnotation;
import org.jf.dexlib2.immutable.ImmutableAnnotationElement;
import org.jf.dexlib2.immutable.ImmutableClassDef;
import org.jf.dexlib2.immutable.ImmutableExceptionHandler;
import org.jf.dexlib2.immutable.ImmutableField;
import org.jf.dexlib2.immutable.ImmutableMethod;
import org.jf.dexlib2.immutable.ImmutableMethodParameter;
import org.jf.dexlib2.immutable.reference.ImmutableFieldReference;
import org.jf.dexlib2.immutable.reference.ImmutableMethodReference;
import org.jf.dexlib2.immutable.reference.ImmutableStringReference;
import org.jf.dexlib2.immutable.reference.ImmutableTypeReference;
import org.jf.dexlib2.immutable.value.ImmutableAnnotationEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableArrayEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableBooleanEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableByteEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableCharEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableDoubleEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableEnumEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableFieldEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableFloatEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableIntEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableLongEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableMethodEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableNullEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableShortEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableStringEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableTypeEncodedValue;
import org.jf.dexlib2.writer.builder.BuilderEncodedValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.CompilationDeathException;
import soot.IntType;
import soot.Local;
import soot.PackManager;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.SourceLocator;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.dexpler.DexInnerClassParser;
import soot.dexpler.DexType;
import soot.dexpler.Util;
import soot.jimple.ClassConstant;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.MonitorStmt;
import soot.jimple.NopStmt;
import soot.jimple.Stmt;
import soot.jimple.toolkits.scalar.EmptySwitchEliminator;
import soot.options.Options;
import soot.tagkit.AbstractHost;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationBooleanElem;
import soot.tagkit.AnnotationClassElem;
import soot.tagkit.AnnotationDefaultTag;
import soot.tagkit.AnnotationDoubleElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationEnumElem;
import soot.tagkit.AnnotationFloatElem;
import soot.tagkit.AnnotationIntElem;
import soot.tagkit.AnnotationLongElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.ConstantValueTag;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.EnclosingMethodTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.InnerClassAttribute;
import soot.tagkit.InnerClassTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LineNumberTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.SignatureTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;
import soot.toDex.instructions.AbstractPayload;
import soot.toDex.instructions.Insn;
import soot.toDex.instructions.Insn10t;
import soot.toDex.instructions.Insn30t;
import soot.toDex.instructions.InsnWithOffset;
import soot.util.Chain;

public class DexPrinter {
   private static final Logger LOGGER = LoggerFactory.getLogger(DexPrinter.class);
   public static final Pattern SIGNATURE_FILE_PATTERN = Pattern.compile("META-INF/[^/]+(\\.SF|\\.DSA|\\.RSA|\\.EC)$");
   protected MultiDexBuilder dexBuilder = this.createDexBuilder();
   protected File originalApk;

   protected MultiDexBuilder createDexBuilder() {
      int api = Scene.v().getAndroidAPIVersion();
      return new MultiDexBuilder(Opcodes.forApi(api));
   }

   private static boolean isSignatureFile(String fileName) {
      return SIGNATURE_FILE_PATTERN.matcher(fileName).matches();
   }

   private static int getVisibility(int visibility) {
      if (visibility == 0) {
         return 1;
      } else if (visibility == 1) {
         return 2;
      } else if (visibility == 2) {
         return 0;
      } else {
         throw new RuntimeException("Unknown annotation visibility: '" + visibility + "'");
      }
   }

   protected static FieldReference toFieldReference(SootField f) {
      FieldReference fieldRef = new ImmutableFieldReference(SootToDexUtils.getDexClassName(f.getDeclaringClass().getName()), f.getName(), SootToDexUtils.getDexTypeDescriptor(f.getType()));
      return fieldRef;
   }

   protected static FieldReference toFieldReference(SootFieldRef ref) {
      FieldReference fieldRef = new ImmutableFieldReference(SootToDexUtils.getDexClassName(ref.declaringClass().getName()), ref.name(), SootToDexUtils.getDexTypeDescriptor(ref.type()));
      return fieldRef;
   }

   protected static MethodReference toMethodReference(SootMethodRef m) {
      List<String> parameters = new ArrayList();
      Iterator var2 = m.parameterTypes().iterator();

      while(var2.hasNext()) {
         Type t = (Type)var2.next();
         parameters.add(SootToDexUtils.getDexTypeDescriptor(t));
      }

      MethodReference methodRef = new ImmutableMethodReference(SootToDexUtils.getDexClassName(m.declaringClass().getName()), m.name(), parameters, SootToDexUtils.getDexTypeDescriptor(m.returnType()));
      return methodRef;
   }

   protected static TypeReference toTypeReference(Type t) {
      ImmutableTypeReference tRef = new ImmutableTypeReference(SootToDexUtils.getDexTypeDescriptor(t));
      return tRef;
   }

   private void printZip() throws IOException {
      ZipOutputStream outputZip = this.getZipOutputStream();
      Throwable var2 = null;

      try {
         LOGGER.info("Do not forget to sign the .apk file with jarsigner and to align it with zipalign");
         if (this.originalApk != null) {
            ZipFile original = new ZipFile(this.originalApk);
            Throwable var4 = null;

            try {
               this.copyAllButClassesDexAndSigFiles(original, outputZip);
            } catch (Throwable var51) {
               var4 = var51;
               throw var51;
            } finally {
               if (original != null) {
                  if (var4 != null) {
                     try {
                        original.close();
                     } catch (Throwable var50) {
                        var4.addSuppressed(var50);
                     }
                  } else {
                     original.close();
                  }
               }

            }
         }

         Path tempPath = Files.createTempDirectory(Long.toString(System.nanoTime()));
         List<File> files = this.dexBuilder.writeTo(tempPath.toString());
         Iterator var5 = files.iterator();

         while(var5.hasNext()) {
            File file = (File)var5.next();
            InputStream is = Files.newInputStream(file.toPath());
            Throwable var8 = null;

            try {
               outputZip.putNextEntry(new ZipEntry(file.getName()));
               byte[] buffer = new byte[16384];
               boolean var10 = false;

               int read;
               while((read = is.read(buffer)) > 0) {
                  outputZip.write(buffer, 0, read);
               }

               outputZip.closeEntry();
            } catch (Throwable var53) {
               var8 = var53;
               throw var53;
            } finally {
               if (is != null) {
                  if (var8 != null) {
                     try {
                        is.close();
                     } catch (Throwable var49) {
                        var8.addSuppressed(var49);
                     }
                  } else {
                     is.close();
                  }
               }

            }
         }

         if (Options.v().output_jar()) {
            this.addManifest(outputZip, files);
         }

         Files.walkFileTree(tempPath, new SimpleFileVisitor<Path>() {
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
               Files.delete(file);
               return FileVisitResult.CONTINUE;
            }

            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
               Files.delete(dir);
               return FileVisitResult.CONTINUE;
            }
         });
      } catch (Throwable var55) {
         var2 = var55;
         throw var55;
      } finally {
         if (outputZip != null) {
            if (var2 != null) {
               try {
                  outputZip.close();
               } catch (Throwable var48) {
                  var2.addSuppressed(var48);
               }
            } else {
               outputZip.close();
            }
         }

      }
   }

   private ZipOutputStream getZipOutputStream() throws IOException {
      if (Options.v().output_jar()) {
         LOGGER.info((String)"Writing JAR to \"{}\"", (Object)Options.v().output_dir());
         return PackManager.v().getJarFile();
      } else {
         String name = this.originalApk == null ? "out.apk" : this.originalApk.getName();
         if (this.originalApk == null) {
            LOGGER.warn((String)"Setting output file name to \"{}\" as original APK has not been found.", (Object)name);
         }

         Path outputFile = Paths.get(SourceLocator.v().getOutputDir(), name);
         if (Files.exists(outputFile, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
            if (!Options.v().force_overwrite()) {
               throw new CompilationDeathException("Output file \"" + outputFile + "\" exists. Not overwriting.");
            }

            try {
               Files.delete(outputFile);
            } catch (IOException var4) {
               throw new IllegalStateException("Removing \"" + outputFile + "\" failed. Not writing out anything.", var4);
            }
         }

         LOGGER.info((String)"Writing APK to \"{}\".", (Object)outputFile);
         return new ZipOutputStream(Files.newOutputStream(outputFile, StandardOpenOption.CREATE_NEW));
      }
   }

   private void copyAllButClassesDexAndSigFiles(ZipFile source, ZipOutputStream destination) throws IOException {
      Enumeration sourceEntries = source.entries();

      while(true) {
         ZipEntry sourceEntry;
         String sourceEntryName;
         do {
            do {
               if (!sourceEntries.hasMoreElements()) {
                  return;
               }

               sourceEntry = (ZipEntry)sourceEntries.nextElement();
               sourceEntryName = sourceEntry.getName();
            } while(sourceEntryName.endsWith(".dex"));
         } while(isSignatureFile(sourceEntryName));

         ZipEntry destinationEntry = new ZipEntry(sourceEntryName);
         destinationEntry.setMethod(sourceEntry.getMethod());
         destinationEntry.setSize(sourceEntry.getSize());
         destinationEntry.setCrc(sourceEntry.getCrc());
         destination.putNextEntry(destinationEntry);
         InputStream zipEntryInput = source.getInputStream(sourceEntry);
         byte[] buffer = new byte[2048];

         for(int bytesRead = zipEntryInput.read(buffer); bytesRead > 0; bytesRead = zipEntryInput.read(buffer)) {
            destination.write(buffer, 0, bytesRead);
         }

         zipEntryInput.close();
      }
   }

   private void addManifest(ZipOutputStream destination, Collection<File> dexFiles) throws IOException {
      Manifest manifest = new Manifest();
      manifest.getMainAttributes().put(Name.MANIFEST_VERSION, "1.0");
      manifest.getMainAttributes().put(new Name("Created-By"), "Soot Dex Printer");
      if (dexFiles != null && !dexFiles.isEmpty()) {
         manifest.getMainAttributes().put(new Name("Dex-Location"), dexFiles.stream().map(File::getName).collect(Collectors.joining(" ")));
      }

      ZipEntry manifestEntry = new ZipEntry("META-INF/MANIFEST.MF");
      destination.putNextEntry(manifestEntry);
      manifest.write(new BufferedOutputStream(destination));
      destination.closeEntry();
   }

   private EncodedValue buildEncodedValueForAnnotation(AnnotationElem elem) {
      AnnotationStringElem e;
      String fSig;
      String classString;
      String fieldName;
      AnnotationIntElem e;
      switch(elem.getKind()) {
      case '@':
         AnnotationAnnotationElem e = (AnnotationAnnotationElem)elem;
         Set<String> alreadyWritten = new HashSet();
         List<AnnotationElement> elements = null;
         if (!e.getValue().getElems().isEmpty()) {
            elements = new ArrayList();
            Iterator var25 = e.getValue().getElems().iterator();

            while(var25.hasNext()) {
               AnnotationElem ae = (AnnotationElem)var25.next();
               if (!alreadyWritten.add(ae.getName())) {
                  throw new RuntimeException("Duplicate annotation attribute: " + ae.getName());
               }

               AnnotationElement element = new ImmutableAnnotationElement(ae.getName(), this.buildEncodedValueForAnnotation(ae));
               elements.add(element);
            }
         }

         return new ImmutableAnnotationEncodedValue(SootToDexUtils.getDexClassName(e.getValue().getType()), elements);
      case 'A':
      case 'E':
      case 'G':
      case 'H':
      case 'K':
      case 'L':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      case '\\':
      case ']':
      case '^':
      case '_':
      case '`':
      case 'a':
      case 'b':
      case 'd':
      case 'g':
      case 'h':
      case 'i':
      case 'j':
      case 'k':
      case 'l':
      case 'm':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      case 'r':
      default:
         throw new RuntimeException("Unknown Elem Attr Kind: " + elem.getKind());
      case 'B':
         e = (AnnotationIntElem)elem;
         return new ImmutableByteEncodedValue((byte)e.getValue());
      case 'C':
         e = (AnnotationIntElem)elem;
         return new ImmutableCharEncodedValue((char)e.getValue());
      case 'D':
         AnnotationDoubleElem e = (AnnotationDoubleElem)elem;
         return new ImmutableDoubleEncodedValue(e.getValue());
      case 'F':
         AnnotationFloatElem e = (AnnotationFloatElem)elem;
         return new ImmutableFloatEncodedValue(e.getValue());
      case 'I':
         e = (AnnotationIntElem)elem;
         return new ImmutableIntEncodedValue(e.getValue());
      case 'J':
         AnnotationLongElem e = (AnnotationLongElem)elem;
         return new ImmutableLongEncodedValue(e.getValue());
      case 'M':
         e = (AnnotationStringElem)elem;
         String[] sp = e.getValue().split(" ");
         String classString = SootToDexUtils.getDexClassName(sp[0].split(":")[0]);
         if (classString.isEmpty()) {
            throw new RuntimeException("Empty class name in annotation");
         }

         classString = sp[1];
         String[] sp2 = sp[2].split("\\(");
         fieldName = sp2[0];
         String parameters = sp2[1].replaceAll("\\)", "");
         List<String> paramTypeList = parameters.isEmpty() ? null : Arrays.asList(parameters.split(","));
         return new ImmutableMethodEncodedValue(new ImmutableMethodReference(classString, fieldName, paramTypeList, classString));
      case 'N':
         return ImmutableNullEncodedValue.INSTANCE;
      case 'S':
         e = (AnnotationIntElem)elem;
         return new ImmutableShortEncodedValue((short)e.getValue());
      case 'Z':
         if (elem instanceof AnnotationIntElem) {
            e = (AnnotationIntElem)elem;
            if (e.getValue() == 0) {
               return ImmutableBooleanEncodedValue.FALSE_VALUE;
            } else {
               if (e.getValue() == 1) {
                  return ImmutableBooleanEncodedValue.TRUE_VALUE;
               }

               throw new RuntimeException("error: boolean value from int with value != 0 or 1.");
            }
         } else {
            if (elem instanceof AnnotationBooleanElem) {
               AnnotationBooleanElem e = (AnnotationBooleanElem)elem;
               if (e.getValue()) {
                  return ImmutableBooleanEncodedValue.TRUE_VALUE;
               }

               return ImmutableBooleanEncodedValue.FALSE_VALUE;
            }

            throw new RuntimeException("Annotation type incompatible with target type boolean");
         }
      case '[':
         AnnotationArrayElem e = (AnnotationArrayElem)elem;
         List<EncodedValue> values = new ArrayList();

         for(int i = 0; i < e.getNumValues(); ++i) {
            EncodedValue val = this.buildEncodedValueForAnnotation(e.getValueAt(i));
            values.add(val);
         }

         return new ImmutableArrayEncodedValue(values);
      case 'c':
         AnnotationClassElem e = (AnnotationClassElem)elem;
         return new ImmutableTypeEncodedValue(e.getDesc());
      case 'e':
         AnnotationEnumElem e = (AnnotationEnumElem)elem;
         fSig = SootToDexUtils.getDexClassName(e.getTypeName());
         return new ImmutableEnumEncodedValue(new ImmutableFieldReference(fSig, e.getConstantName(), fSig));
      case 'f':
         e = (AnnotationStringElem)elem;
         fSig = e.getValue();
         String[] sp = fSig.split(" ");
         classString = SootToDexUtils.getDexClassName(sp[0].split(":")[0]);
         if (classString.isEmpty()) {
            throw new RuntimeException("Empty class name in annotation");
         } else {
            String typeString = sp[1];
            if (typeString.isEmpty()) {
               throw new RuntimeException("Empty type string in annotation");
            }

            fieldName = sp[2];
            return new ImmutableFieldEncodedValue(new ImmutableFieldReference(classString, fieldName, typeString));
         }
      case 's':
         e = (AnnotationStringElem)elem;
         return new ImmutableStringEncodedValue(e.getValue());
      }
   }

   private EncodedValue makeConstantItem(SootField sf, Tag t) {
      if (!(t instanceof ConstantValueTag)) {
         throw new RuntimeException("error: t not ConstantValueTag.");
      } else if (t instanceof IntegerConstantValueTag) {
         Type sft = sf.getType();
         IntegerConstantValueTag i = (IntegerConstantValueTag)t;
         if (sft instanceof BooleanType) {
            int v = i.getIntValue();
            if (v == 0) {
               return ImmutableBooleanEncodedValue.FALSE_VALUE;
            } else if (v == 1) {
               return ImmutableBooleanEncodedValue.TRUE_VALUE;
            } else {
               throw new RuntimeException("error: boolean value from int with value != 0 or 1.");
            }
         } else if (sft instanceof CharType) {
            return new ImmutableCharEncodedValue((char)i.getIntValue());
         } else if (sft instanceof ByteType) {
            return new ImmutableByteEncodedValue((byte)i.getIntValue());
         } else if (sft instanceof IntType) {
            return new ImmutableIntEncodedValue(i.getIntValue());
         } else if (sft instanceof ShortType) {
            return new ImmutableShortEncodedValue((short)i.getIntValue());
         } else {
            throw new RuntimeException("error: unexpected constant tag type: " + t + " for field " + sf);
         }
      } else if (t instanceof LongConstantValueTag) {
         LongConstantValueTag l = (LongConstantValueTag)t;
         return new ImmutableLongEncodedValue(l.getLongValue());
      } else if (t instanceof DoubleConstantValueTag) {
         DoubleConstantValueTag d = (DoubleConstantValueTag)t;
         return new ImmutableDoubleEncodedValue(d.getDoubleValue());
      } else if (t instanceof FloatConstantValueTag) {
         FloatConstantValueTag f = (FloatConstantValueTag)t;
         return new ImmutableFloatEncodedValue(f.getFloatValue());
      } else if (t instanceof StringConstantValueTag) {
         StringConstantValueTag s = (StringConstantValueTag)t;
         return sf.getType().equals(RefType.v("java.lang.String")) ? new ImmutableStringEncodedValue(s.getStringValue()) : null;
      } else {
         throw new RuntimeException("Unexpected constant type");
      }
   }

   private void addAsClassDefItem(SootClass c) {
      String sourceFile = null;
      if (c.hasTag("SourceFileTag")) {
         SourceFileTag sft = (SourceFileTag)c.getTag("SourceFileTag");
         sourceFile = sft.getSourceFile();
      }

      String classType = SootToDexUtils.getDexTypeDescriptor(c.getType());
      int accessFlags = c.getModifiers();
      String superClass = c.hasSuperclass() ? SootToDexUtils.getDexTypeDescriptor(c.getSuperclass().getType()) : null;
      List<String> interfaces = null;
      if (!c.getInterfaces().isEmpty()) {
         interfaces = new ArrayList();
         Iterator var7 = c.getInterfaces().iterator();

         while(var7.hasNext()) {
            SootClass ifc = (SootClass)var7.next();
            interfaces.add(SootToDexUtils.getDexTypeDescriptor(ifc.getType()));
         }
      }

      List<Field> fields = null;
      if (!c.getFields().isEmpty()) {
         fields = new ArrayList();
         Iterator var15 = c.getFields().iterator();

         label47:
         while(true) {
            SootField f;
            do {
               if (!var15.hasNext()) {
                  break label47;
               }

               f = (SootField)var15.next();
            } while(f.isPhantom());

            EncodedValue staticInit = null;
            Iterator var11 = f.getTags().iterator();

            while(var11.hasNext()) {
               Tag t = (Tag)var11.next();
               if (t instanceof ConstantValueTag) {
                  if (staticInit != null) {
                     LOGGER.warn((String)"More than one constant tag for field \"{}\": \"{}\"", (Object)f, (Object)t);
                  } else {
                     staticInit = this.makeConstantItem(f, t);
                  }
               }
            }

            if (staticInit == null) {
               staticInit = BuilderEncodedValues.defaultValueForType(SootToDexUtils.getDexTypeDescriptor(f.getType()));
            }

            Set<Annotation> fieldAnnotations = this.buildFieldAnnotations(f);
            ImmutableField field = new ImmutableField(classType, f.getName(), SootToDexUtils.getDexTypeDescriptor(f.getType()), f.getModifiers(), (EncodedValue)staticInit, fieldAnnotations);
            fields.add(field);
         }
      }

      Collection<Method> methods = this.toMethods(c);
      ClassDef classDef = new ImmutableClassDef(classType, accessFlags, superClass, interfaces, sourceFile, this.buildClassAnnotations(c), fields, methods);
      this.dexBuilder.internClass(classDef);
   }

   private Set<Annotation> buildClassAnnotations(SootClass c) {
      Set<String> skipList = new HashSet();
      Set<Annotation> annotations = this.buildCommonAnnotations(c, skipList);
      if (c.hasTag("EnclosingMethodTag")) {
         EnclosingMethodTag eMethTag = (EnclosingMethodTag)c.getTag("EnclosingMethodTag");
         Annotation enclosingMethodItem = this.buildEnclosingMethodTag(eMethTag, skipList);
         if (enclosingMethodItem != null) {
            annotations.add(enclosingMethodItem);
         }
      } else if (c.hasOuterClass() && skipList.add("Ldalvik/annotation/EnclosingClass;")) {
         ImmutableAnnotationElement enclosingElement = new ImmutableAnnotationElement("value", new ImmutableTypeEncodedValue(SootToDexUtils.getDexClassName(c.getOuterClass().getName())));
         annotations.add(new ImmutableAnnotation(2, "Ldalvik/annotation/EnclosingClass;", Collections.singleton(enclosingElement)));
      }

      InnerClassAttribute icTag;
      List memberClassesItem;
      if (c.hasOuterClass()) {
         icTag = (InnerClassAttribute)c.getOuterClass().getTag("InnerClassAttribute");
         if (icTag != null) {
            memberClassesItem = this.buildInnerClassAttribute(c, icTag, skipList);
            if (memberClassesItem != null) {
               annotations.addAll(memberClassesItem);
            }
         }
      }

      icTag = (InnerClassAttribute)c.getTag("InnerClassAttribute");
      if (icTag != null) {
         memberClassesItem = this.buildMemberClassesAttribute(c, icTag, skipList);
         if (memberClassesItem != null) {
            annotations.addAll(memberClassesItem);
         }
      }

      Iterator var15 = c.getTags().iterator();

      while(var15.hasNext()) {
         Tag t = (Tag)var15.next();
         if (t.getName().equals("VisibilityAnnotationTag")) {
            List<ImmutableAnnotation> visibilityItems = this.buildVisibilityAnnotationTag((VisibilityAnnotationTag)t, skipList);
            annotations.addAll(visibilityItems);
         }
      }

      List<AnnotationElem> defaults = new ArrayList();
      Iterator var17 = c.getMethods().iterator();

      while(var17.hasNext()) {
         SootMethod method = (SootMethod)var17.next();
         AnnotationDefaultTag tag = (AnnotationDefaultTag)method.getTag("AnnotationDefaultTag");
         if (tag != null) {
            tag.getDefaultVal().setName(method.getName());
            defaults.add(tag.getDefaultVal());
         }
      }

      if (defaults.size() > 0) {
         VisibilityAnnotationTag defaultAnnotationTag = new VisibilityAnnotationTag(1);
         AnnotationTag a = new AnnotationTag("Ldalvik/annotation/AnnotationDefault;");
         defaultAnnotationTag.addAnnotation(a);
         AnnotationTag at = new AnnotationTag(SootToDexUtils.getDexClassName(c.getName()));
         AnnotationAnnotationElem ae = new AnnotationAnnotationElem(at, '@', "value");
         a.addElem(ae);
         Iterator var10 = defaults.iterator();

         while(var10.hasNext()) {
            AnnotationElem aelem = (AnnotationElem)var10.next();
            at.addElem(aelem);
         }

         List<ImmutableAnnotation> visibilityItems = this.buildVisibilityAnnotationTag(defaultAnnotationTag, skipList);
         annotations.addAll(visibilityItems);
      }

      return annotations;
   }

   private Set<Annotation> buildFieldAnnotations(SootField f) {
      Set<String> skipList = new HashSet();
      Set<Annotation> annotations = this.buildCommonAnnotations(f, skipList);
      Iterator var4 = f.getTags().iterator();

      while(var4.hasNext()) {
         Tag t = (Tag)var4.next();
         if (t.getName().equals("VisibilityAnnotationTag")) {
            List<ImmutableAnnotation> visibilityItems = this.buildVisibilityAnnotationTag((VisibilityAnnotationTag)t, skipList);
            annotations.addAll(visibilityItems);
         }
      }

      return annotations;
   }

   private Set<Annotation> buildMethodAnnotations(SootMethod m) {
      Set<String> skipList = new HashSet();
      Set<Annotation> annotations = this.buildCommonAnnotations(m, skipList);
      Iterator var4 = m.getTags().iterator();

      while(var4.hasNext()) {
         Tag t = (Tag)var4.next();
         if (t.getName().equals("VisibilityAnnotationTag")) {
            List<ImmutableAnnotation> visibilityItems = this.buildVisibilityAnnotationTag((VisibilityAnnotationTag)t, skipList);
            annotations.addAll(visibilityItems);
         }
      }

      List<SootClass> exceptionList = m.getExceptionsUnsafe();
      if (exceptionList != null && !exceptionList.isEmpty()) {
         List<ImmutableEncodedValue> valueList = new ArrayList(exceptionList.size());
         Iterator var12 = exceptionList.iterator();

         while(var12.hasNext()) {
            SootClass exceptionClass = (SootClass)var12.next();
            valueList.add(new ImmutableTypeEncodedValue(DexType.toDalvikICAT(exceptionClass.getName()).replace(".", "/")));
         }

         ImmutableArrayEncodedValue valueValue = new ImmutableArrayEncodedValue(valueList);
         ImmutableAnnotationElement valueElement = new ImmutableAnnotationElement("value", valueValue);
         Set<ImmutableAnnotationElement> elements = Collections.singleton(valueElement);
         ImmutableAnnotation ann = new ImmutableAnnotation(2, "Ldalvik/annotation/Throws;", elements);
         annotations.add(ann);
      }

      return annotations;
   }

   private Set<Annotation> buildMethodParameterAnnotations(SootMethod m, int paramIdx) {
      Set<String> skipList = null;
      Set<Annotation> annotations = null;
      Iterator var5 = m.getTags().iterator();

      while(var5.hasNext()) {
         Tag t = (Tag)var5.next();
         if (t.getName().equals("VisibilityParameterAnnotationTag")) {
            VisibilityParameterAnnotationTag vat = (VisibilityParameterAnnotationTag)t;
            if (skipList == null) {
               skipList = new HashSet();
               annotations = new HashSet();
            }

            List<ImmutableAnnotation> visibilityItems = this.buildVisibilityParameterAnnotationTag(vat, skipList, paramIdx);
            annotations.addAll(visibilityItems);
         }
      }

      return annotations;
   }

   private Set<Annotation> buildCommonAnnotations(AbstractHost host, Set<String> skipList) {
      Set<Annotation> annotations = new HashSet();
      if (host.hasTag("DeprecatedTag") && !skipList.contains("Ljava/lang/Deprecated;")) {
         ImmutableAnnotation ann = new ImmutableAnnotation(1, "Ljava/lang/Deprecated;", Collections.emptySet());
         annotations.add(ann);
         skipList.add("Ljava/lang/Deprecated;");
      }

      if (host.hasTag("SignatureTag") && !skipList.contains("Ldalvik/annotation/Signature;")) {
         SignatureTag tag = (SignatureTag)host.getTag("SignatureTag");
         List<String> splitSignature = SootToDexUtils.splitSignature(tag.getSignature());
         Set<ImmutableAnnotationElement> elements = null;
         if (splitSignature != null && splitSignature.size() > 0) {
            List<ImmutableEncodedValue> valueList = new ArrayList();
            Iterator var8 = splitSignature.iterator();

            while(var8.hasNext()) {
               String s = (String)var8.next();
               ImmutableStringEncodedValue val = new ImmutableStringEncodedValue(s);
               valueList.add(val);
            }

            ImmutableArrayEncodedValue valueValue = new ImmutableArrayEncodedValue(valueList);
            ImmutableAnnotationElement valueElement = new ImmutableAnnotationElement("value", valueValue);
            elements = Collections.singleton(valueElement);
         } else {
            LOGGER.info("Signature annotation without value detected");
         }

         ImmutableAnnotation ann = new ImmutableAnnotation(2, "Ldalvik/annotation/Signature;", elements);
         annotations.add(ann);
         skipList.add("Ldalvik/annotation/Signature;");
      }

      return annotations;
   }

   private List<ImmutableAnnotation> buildVisibilityAnnotationTag(VisibilityAnnotationTag t, Set<String> skipList) {
      if (t.getAnnotations() == null) {
         return Collections.emptyList();
      } else {
         List<ImmutableAnnotation> annotations = new ArrayList();
         Iterator var4 = t.getAnnotations().iterator();

         while(true) {
            AnnotationTag at;
            String type;
            do {
               if (!var4.hasNext()) {
                  return annotations;
               }

               at = (AnnotationTag)var4.next();
               type = at.getType();
            } while(!skipList.add(type));

            Set<String> alreadyWritten = new HashSet();
            List<AnnotationElement> elements = null;
            if (!at.getElems().isEmpty()) {
               elements = new ArrayList();
               Iterator var9 = at.getElems().iterator();

               while(var9.hasNext()) {
                  AnnotationElem ae = (AnnotationElem)var9.next();
                  if (ae.getName() == null || ae.getName().isEmpty()) {
                     throw new RuntimeException("Null or empty annotation name encountered");
                  }

                  if (!alreadyWritten.add(ae.getName())) {
                     throw new RuntimeException("Duplicate annotation attribute: " + ae.getName());
                  }

                  EncodedValue value = this.buildEncodedValueForAnnotation(ae);
                  ImmutableAnnotationElement element = new ImmutableAnnotationElement(ae.getName(), value);
                  elements.add(element);
               }
            }

            String typeName = SootToDexUtils.getDexClassName(at.getType());
            ImmutableAnnotation ann = new ImmutableAnnotation(getVisibility(t.getVisibility()), typeName, elements);
            annotations.add(ann);
         }
      }
   }

   private List<ImmutableAnnotation> buildVisibilityParameterAnnotationTag(VisibilityParameterAnnotationTag t, Set<String> skipList, int paramIdx) {
      if (t.getVisibilityAnnotations() == null) {
         return Collections.emptyList();
      } else {
         int paramTagIdx = 0;
         List<ImmutableAnnotation> annotations = new ArrayList();

         label59:
         for(Iterator var6 = t.getVisibilityAnnotations().iterator(); var6.hasNext(); ++paramTagIdx) {
            VisibilityAnnotationTag vat = (VisibilityAnnotationTag)var6.next();
            if (paramTagIdx == paramIdx && vat != null && vat.getAnnotations() != null) {
               Iterator var8 = vat.getAnnotations().iterator();

               while(true) {
                  AnnotationTag at;
                  String type;
                  do {
                     if (!var8.hasNext()) {
                        continue label59;
                     }

                     at = (AnnotationTag)var8.next();
                     type = at.getType();
                  } while(!skipList.add(type));

                  Set<String> alreadyWritten = new HashSet();
                  List<AnnotationElement> elements = null;
                  if (!at.getElems().isEmpty()) {
                     elements = new ArrayList();
                     Iterator var13 = at.getElems().iterator();

                     while(var13.hasNext()) {
                        AnnotationElem ae = (AnnotationElem)var13.next();
                        if (ae.getName() == null || ae.getName().isEmpty()) {
                           throw new RuntimeException("Null or empty annotation name encountered");
                        }

                        if (!alreadyWritten.add(ae.getName())) {
                           throw new RuntimeException("Duplicate annotation attribute: " + ae.getName());
                        }

                        EncodedValue value = this.buildEncodedValueForAnnotation(ae);
                        ImmutableAnnotationElement element = new ImmutableAnnotationElement(ae.getName(), value);
                        elements.add(element);
                     }
                  }

                  ImmutableAnnotation ann = new ImmutableAnnotation(getVisibility(vat.getVisibility()), SootToDexUtils.getDexClassName(at.getType()), elements);
                  annotations.add(ann);
               }
            }
         }

         return annotations;
      }
   }

   private Annotation buildEnclosingMethodTag(EnclosingMethodTag t, Set<String> skipList) {
      if (!skipList.add("Ldalvik/annotation/EnclosingMethod;")) {
         return null;
      } else if (t.getEnclosingMethod() == null) {
         return null;
      } else {
         String[] split1 = t.getEnclosingMethodSig().split("\\)");
         String parametersS = split1[0].replaceAll("\\(", "");
         String returnTypeS = split1[1];
         List<String> typeList = new ArrayList();
         if (!parametersS.equals("")) {
            Iterator var7 = Util.splitParameters(parametersS).iterator();

            while(var7.hasNext()) {
               String p = (String)var7.next();
               if (!p.isEmpty()) {
                  typeList.add(p);
               }
            }
         }

         ImmutableMethodReference mRef = new ImmutableMethodReference(SootToDexUtils.getDexClassName(t.getEnclosingClass()), t.getEnclosingMethod(), typeList, returnTypeS);
         ImmutableMethodEncodedValue methodRef = new ImmutableMethodEncodedValue(mRef);
         AnnotationElement methodElement = new ImmutableAnnotationElement("value", methodRef);
         return new ImmutableAnnotation(2, "Ldalvik/annotation/EnclosingMethod;", Collections.singleton(methodElement));
      }
   }

   private List<Annotation> buildInnerClassAttribute(SootClass parentClass, InnerClassAttribute t, Set<String> skipList) {
      if (t.getSpecs() == null) {
         return null;
      } else {
         List<Annotation> anns = null;
         Iterator var5 = t.getSpecs().iterator();

         while(true) {
            InnerClassTag icTag;
            String outerClass;
            do {
               do {
                  String innerClass;
                  do {
                     do {
                        if (!var5.hasNext()) {
                           return anns;
                        }

                        Tag t2 = (Tag)var5.next();
                        icTag = (InnerClassTag)t2;
                        outerClass = DexInnerClassParser.getOuterClassNameFromTag(icTag);
                        innerClass = icTag.getInnerClass().replaceAll("/", ".");
                     } while(!parentClass.hasOuterClass());
                  } while(!innerClass.equals(parentClass.getName()));

                  if (parentClass.getName().equals(outerClass) && icTag.getOuterClass() == null) {
                     outerClass = null;
                  }
               } while(parentClass.getName().equals(outerClass));
            } while(!skipList.add("Ldalvik/annotation/InnerClass;"));

            List<AnnotationElement> elements = new ArrayList();
            ImmutableAnnotationElement flagsElement = new ImmutableAnnotationElement("accessFlags", new ImmutableIntEncodedValue(icTag.getAccessFlags()));
            elements.add(flagsElement);
            Object nameValue;
            if (icTag.getShortName() != null && !icTag.getShortName().isEmpty()) {
               nameValue = new ImmutableStringEncodedValue(icTag.getShortName());
            } else {
               nameValue = ImmutableNullEncodedValue.INSTANCE;
            }

            ImmutableAnnotationElement nameElement = new ImmutableAnnotationElement("name", (ImmutableEncodedValue)nameValue);
            elements.add(nameElement);
            if (anns == null) {
               anns = new ArrayList();
            }

            anns.add(new ImmutableAnnotation(2, "Ldalvik/annotation/InnerClass;", elements));
         }
      }
   }

   private List<Annotation> buildMemberClassesAttribute(SootClass parentClass, InnerClassAttribute t, Set<String> skipList) {
      List<Annotation> anns = null;
      Set<String> memberClasses = null;
      Iterator var6 = t.getSpecs().iterator();

      while(var6.hasNext()) {
         Tag t2 = (Tag)var6.next();
         InnerClassTag icTag = (InnerClassTag)t2;
         String outerClass = DexInnerClassParser.getOuterClassNameFromTag(icTag);
         if (icTag.getOuterClass() != null && parentClass.getName().equals(outerClass)) {
            if (memberClasses == null) {
               memberClasses = new HashSet();
            }

            memberClasses.add(SootToDexUtils.getDexClassName(icTag.getInnerClass()));
         }
      }

      if (memberClasses != null && !memberClasses.isEmpty() && skipList.add("Ldalvik/annotation/MemberClasses;")) {
         List<EncodedValue> classes = new ArrayList();
         Iterator var11 = memberClasses.iterator();

         while(var11.hasNext()) {
            String memberClass = (String)var11.next();
            ImmutableTypeEncodedValue classValue = new ImmutableTypeEncodedValue(memberClass);
            classes.add(classValue);
         }

         ImmutableArrayEncodedValue classesValue = new ImmutableArrayEncodedValue(classes);
         ImmutableAnnotationElement element = new ImmutableAnnotationElement("value", classesValue);
         ImmutableAnnotation memberAnnotation = new ImmutableAnnotation(2, "Ldalvik/annotation/MemberClasses;", Collections.singletonList(element));
         if (anns == null) {
            anns = new ArrayList();
         }

         anns.add(memberAnnotation);
      }

      return anns;
   }

   private Collection<Method> toMethods(SootClass clazz) {
      if (clazz.getMethods().isEmpty()) {
         return null;
      } else {
         String classType = SootToDexUtils.getDexTypeDescriptor(clazz.getType());
         List<Method> methods = new ArrayList();
         Iterator var4 = clazz.getMethods().iterator();

         while(true) {
            SootMethod sm;
            do {
               if (!var4.hasNext()) {
                  return methods;
               }

               sm = (SootMethod)var4.next();
            } while(sm.isPhantom());

            MethodImplementation impl = this.toMethodImplementation(sm);
            List<String> parameterNames = null;
            if (sm.hasTag("ParamNamesTag")) {
               parameterNames = ((ParamNamesTag)sm.getTag("ParamNamesTag")).getNames();
            }

            int paramIdx = 0;
            List<MethodParameter> parameters = null;
            if (sm.getParameterCount() > 0) {
               parameters = new ArrayList();

               for(Iterator var10 = sm.getParameterTypes().iterator(); var10.hasNext(); ++paramIdx) {
                  Type tp = (Type)var10.next();
                  String paramType = SootToDexUtils.getDexTypeDescriptor(tp);
                  parameters.add(new ImmutableMethodParameter(paramType, this.buildMethodParameterAnnotations(sm, paramIdx), sm.isConcrete() && parameterNames != null ? (String)parameterNames.get(paramIdx) : null));
               }
            }

            String returnType = SootToDexUtils.getDexTypeDescriptor(sm.getReturnType());
            int accessFlags = SootToDexUtils.getDexAccessFlags(sm);
            ImmutableMethod meth = new ImmutableMethod(classType, sm.getName(), parameters, returnType, accessFlags, this.buildMethodAnnotations(sm), impl);
            methods.add(meth);
         }
      }
   }

   private MethodImplementation toMethodImplementation(SootMethod m) {
      if (!m.isAbstract() && !m.isNative()) {
         Body activeBody = m.retrieveActiveBody();
         if ((m.getName().contains("<") || m.getName().equals(">")) && !m.getName().equals("<init>") && !m.getName().equals("<clinit>")) {
            throw new RuntimeException("Invalid method name: " + m.getName());
         } else {
            EmptySwitchEliminator.v().transform(activeBody);
            SynchronizedMethodTransformer.v().transform(activeBody);
            FastDexTrapTightener.v().transform(activeBody);
            DexArrayInitDetector initDetector = new DexArrayInitDetector();
            initDetector.constructArrayInitializations(activeBody);
            initDetector.fixTraps(activeBody);
            TrapSplitter.v().transform(activeBody);
            int inWords = SootToDexUtils.getDexWords(m.getParameterTypes());
            if (!m.isStatic()) {
               ++inWords;
            }

            Collection<Unit> units = activeBody.getUnits();
            StmtVisitor stmtV = new StmtVisitor(m, initDetector);
            Chain<Trap> traps = activeBody.getTraps();
            Set<Unit> trapReferences = new HashSet(traps.size() * 3);
            Iterator var9 = activeBody.getTraps().iterator();

            while(var9.hasNext()) {
               Trap t = (Trap)var9.next();
               trapReferences.add(t.getBeginUnit());
               trapReferences.add(t.getEndUnit());
               trapReferences.add(t.getHandlerUnit());
            }

            this.toInstructions(units, stmtV, trapReferences);
            int registerCount = stmtV.getRegisterCount();
            if (inWords > registerCount) {
               registerCount = inWords;
            }

            MethodImplementationBuilder builder = new MethodImplementationBuilder(registerCount);
            LabelAssigner labelAssinger = new LabelAssigner(builder);
            List<BuilderInstruction> instructions = stmtV.getRealInsns(labelAssinger);
            this.fixLongJumps(instructions, labelAssinger, stmtV);
            Map<Local, Integer> seenRegisters = new HashMap();
            Map<Instruction, LocalRegisterAssignmentInformation> instructionRegisterMap = stmtV.getInstructionRegisterMap();
            Iterator var15;
            if (Options.v().write_local_annotations()) {
               var15 = stmtV.getParameterInstructionsList().iterator();

               while(var15.hasNext()) {
                  LocalRegisterAssignmentInformation assignment = (LocalRegisterAssignmentInformation)var15.next();
                  if (!assignment.getLocal().getName().equals("this")) {
                     this.addRegisterAssignmentDebugInfo(assignment, seenRegisters, builder);
                  }
               }
            }

            var15 = instructions.iterator();

            while(var15.hasNext()) {
               BuilderInstruction ins = (BuilderInstruction)var15.next();
               Stmt origStmt = stmtV.getStmtForInstruction(ins);
               if (stmtV.getInstructionPayloadMap().containsKey(ins)) {
                  builder.addLabel(labelAssinger.getLabelName((AbstractPayload)stmtV.getInstructionPayloadMap().get(ins)));
               }

               if (origStmt != null) {
                  if (trapReferences.contains(origStmt)) {
                     labelAssinger.getOrCreateLabel(origStmt);
                  }

                  String labelName = labelAssinger.getLabelName(origStmt);
                  if (labelName != null && !builder.getLabel(labelName).isPlaced()) {
                     builder.addLabel(labelName);
                  }

                  if (stmtV.getStmtForInstruction(ins) != null) {
                     List<Tag> tags = origStmt.getTags();
                     Iterator var20 = tags.iterator();

                     while(var20.hasNext()) {
                        Tag t = (Tag)var20.next();
                        if (t instanceof LineNumberTag) {
                           LineNumberTag lnt = (LineNumberTag)t;
                           builder.addLineNumber(lnt.getLineNumber());
                        } else if (t instanceof SourceFileTag) {
                           SourceFileTag sft = (SourceFileTag)t;
                           builder.addSetSourceFile(new ImmutableStringReference(sft.getSourceFile()));
                        }
                     }
                  }
               }

               builder.addInstruction(ins);
               LocalRegisterAssignmentInformation registerAssignmentTag = (LocalRegisterAssignmentInformation)instructionRegisterMap.get(ins);
               if (registerAssignmentTag != null) {
                  this.addRegisterAssignmentDebugInfo(registerAssignmentTag, seenRegisters, builder);
               }
            }

            var15 = seenRegisters.values().iterator();

            while(var15.hasNext()) {
               int registersLeft = (Integer)var15.next();
               builder.addEndLocal(registersLeft);
            }

            this.toTries(activeBody.getTraps(), stmtV, builder, labelAssinger);
            var15 = labelAssinger.getAllLabels().iterator();

            Label lbl;
            do {
               if (!var15.hasNext()) {
                  return builder.getMethodImplementation();
               }

               lbl = (Label)var15.next();
            } while(lbl.isPlaced());

            throw new RuntimeException("Label not placed: " + lbl);
         }
      } else {
         return null;
      }
   }

   private void fixLongJumps(List<BuilderInstruction> instructions, LabelAssigner labelAssigner, StmtVisitor stmtV) {
      Map<Instruction, Integer> instructionsToIndex = new HashMap();
      List<Integer> instructionsToOffsets = new ArrayList();
      Map<Label, Integer> labelsToOffsets = new HashMap();
      HashMap labelsToIndex = new HashMap();

      boolean hasChanged;
      do {
         hasChanged = false;
         instructionsToOffsets.clear();
         int j = 0;
         int idx = 0;

         for(Iterator var11 = instructions.iterator(); var11.hasNext(); ++idx) {
            BuilderInstruction bi = (BuilderInstruction)var11.next();
            instructionsToIndex.put(bi, idx);
            instructionsToOffsets.add(j);
            Stmt origStmt = stmtV.getStmtForInstruction(bi);
            if (origStmt != null) {
               Label lbl = labelAssigner.getLabelUnsafe(origStmt);
               if (lbl != null) {
                  labelsToOffsets.put(lbl, j);
                  labelsToIndex.put(lbl, idx);
               }
            }

            j += bi.getFormat().size / 2;
         }

         for(j = 0; j < instructions.size(); ++j) {
            BuilderInstruction bj = (BuilderInstruction)instructions.get(j);
            if (bj instanceof BuilderOffsetInstruction) {
               BuilderOffsetInstruction boj = (BuilderOffsetInstruction)bj;
               Insn jumpInsn = stmtV.getInsnForInstruction(boj);
               if (jumpInsn instanceof InsnWithOffset) {
                  InsnWithOffset offsetInsn = (InsnWithOffset)jumpInsn;
                  Integer targetOffset = (Integer)labelsToOffsets.get(boj.getTarget());
                  if (targetOffset != null) {
                     int distance = (Integer)instructionsToOffsets.get(j) - targetOffset;
                     if (Math.abs(distance) > offsetInsn.getMaxJumpOffset()) {
                        this.insertIntermediateJump((Integer)labelsToIndex.get(boj.getTarget()), j, stmtV, instructions, labelAssigner);
                        hasChanged = true;
                        break;
                     }
                  }
               }
            }
         }
      } while(hasChanged);

   }

   private void insertIntermediateJump(int targetInsPos, int jumpInsPos, StmtVisitor stmtV, List<BuilderInstruction> instructions, LabelAssigner labelAssigner) {
      BuilderInstruction originalJumpInstruction = (BuilderInstruction)instructions.get(jumpInsPos);
      Insn originalJumpInsn = stmtV.getInsnForInstruction(originalJumpInstruction);
      if (originalJumpInsn != null) {
         if (!(originalJumpInsn instanceof InsnWithOffset)) {
            throw new RuntimeException("Unexpected jump instruction target");
         } else {
            InsnWithOffset offsetInsn = (InsnWithOffset)originalJumpInsn;
            if (originalJumpInsn instanceof Insn10t && originalJumpInsn.getOpcode() == Opcode.GOTO) {
               Insn30t newJump = new Insn30t(Opcode.GOTO_32);
               newJump.setTarget(((Insn10t)originalJumpInsn).getTarget());
               BuilderInstruction newJumpInstruction = newJump.getRealInsn(labelAssigner);
               instructions.remove(jumpInsPos);
               instructions.add(jumpInsPos, newJumpInstruction);
               stmtV.fakeNewInsn(stmtV.getStmtForInstruction(originalJumpInstruction), newJump, newJumpInstruction);
            } else {
               int distance = Math.max(targetInsPos, jumpInsPos) - Math.min(targetInsPos, jumpInsPos);
               if (distance != 0) {
                  int newJumpIdx = Math.min(targetInsPos, jumpInsPos) + distance / 2;
                  int sign = (int)Math.signum((float)(targetInsPos - jumpInsPos));

                  do {
                     Stmt newStmt = stmtV.getStmtForInstruction((Instruction)instructions.get(newJumpIdx));
                     Stmt prevStmt = newJumpIdx > 0 ? stmtV.getStmtForInstruction((Instruction)instructions.get(newJumpIdx - 1)) : null;
                     if (newStmt != null && newStmt != prevStmt) {
                        NopStmt nop = Jimple.v().newNopStmt();
                        Insn30t newJump = new Insn30t(Opcode.GOTO_32);
                        newJump.setTarget(stmtV.getStmtForInstruction((Instruction)instructions.get(targetInsPos)));
                        BuilderInstruction newJumpInstruction = newJump.getRealInsn(labelAssigner);
                        instructions.add(newJumpIdx, newJumpInstruction);
                        stmtV.fakeNewInsn(nop, newJump, newJumpInstruction);
                        if (newJumpIdx <= jumpInsPos) {
                           ++jumpInsPos;
                        }

                        if (newJumpIdx <= targetInsPos) {
                           ++targetInsPos;
                        }

                        offsetInsn.setTarget(nop);
                        BuilderInstruction replacementJumpInstruction = offsetInsn.getRealInsn(labelAssigner);

                        assert instructions.get(jumpInsPos) == originalJumpInstruction;

                        instructions.remove(jumpInsPos);
                        instructions.add(jumpInsPos, replacementJumpInstruction);
                        stmtV.fakeNewInsn(stmtV.getStmtForInstruction(originalJumpInstruction), originalJumpInsn, replacementJumpInstruction);
                        Stmt afterNewJump = stmtV.getStmtForInstruction((Instruction)instructions.get(newJumpIdx + 1));
                        Insn10t jumpAround = new Insn10t(Opcode.GOTO);
                        jumpAround.setTarget(afterNewJump);
                        BuilderInstruction jumpAroundInstruction = jumpAround.getRealInsn(labelAssigner);
                        instructions.add(newJumpIdx, jumpAroundInstruction);
                        stmtV.fakeNewInsn(Jimple.v().newNopStmt(), jumpAround, jumpAroundInstruction);
                        return;
                     }

                     newJumpIdx -= sign;
                  } while(newJumpIdx >= 0 && newJumpIdx < instructions.size());

                  throw new RuntimeException("No position for inserting intermediate jump instruction found");
               }
            }
         }
      }
   }

   private void addRegisterAssignmentDebugInfo(LocalRegisterAssignmentInformation registerAssignment, Map<Local, Integer> seenRegisters, MethodImplementationBuilder builder) {
      Local local = registerAssignment.getLocal();
      String dexLocalType = SootToDexUtils.getDexTypeDescriptor(local.getType());
      StringReference localName = new ImmutableStringReference(local.getName());
      Register reg = registerAssignment.getRegister();
      int register = reg.getNumber();
      Integer beforeRegister = (Integer)seenRegisters.get(local);
      if (beforeRegister != null) {
         if (beforeRegister == register) {
            return;
         }

         builder.addEndLocal(beforeRegister);
      }

      builder.addStartLocal(register, localName, new ImmutableTypeReference(dexLocalType), new ImmutableStringReference(""));
      seenRegisters.put(local, register);
   }

   private void toInstructions(Collection<Unit> units, StmtVisitor stmtV, Set<Unit> trapReferences) {
      Set<ClassConstant> monitorConsts = new HashSet();
      Iterator var5 = units.iterator();

      while(var5.hasNext()) {
         Unit u = (Unit)var5.next();
         if (u instanceof MonitorStmt) {
            MonitorStmt monitorStmt = (MonitorStmt)u;
            if (monitorStmt.getOp() instanceof ClassConstant) {
               monitorConsts.add((ClassConstant)monitorStmt.getOp());
            }
         }
      }

      boolean monitorAllocsMade = false;
      Iterator var9 = units.iterator();

      while(var9.hasNext()) {
         Unit u = (Unit)var9.next();
         if (!monitorAllocsMade && !monitorConsts.isEmpty() && !(u instanceof IdentityStmt)) {
            stmtV.preAllocateMonitorConsts(monitorConsts);
            monitorAllocsMade = true;
         }

         stmtV.beginNewStmt((Stmt)u);
         u.apply(stmtV);
      }

      stmtV.finalizeInstructions(trapReferences);
   }

   private void toTries(Collection<Trap> traps, StmtVisitor stmtV, MethodImplementationBuilder builder, LabelAssigner labelAssigner) {
      Map<DexPrinter.CodeRange, List<ExceptionHandler>> codeRangesToTryItem = new LinkedHashMap();

      Iterator var6;
      DexPrinter.CodeRange range;
      ArrayList newHandlers;
      for(var6 = traps.iterator(); var6.hasNext(); codeRangesToTryItem.put(range, newHandlers)) {
         Trap t = (Trap)var6.next();
         Stmt beginStmt = (Stmt)t.getBeginUnit();
         Stmt endStmt = (Stmt)t.getEndUnit();
         int startCodeAddress = labelAssigner.getLabel(beginStmt).getCodeAddress();
         int endCodeAddress = labelAssigner.getLabel(endStmt).getCodeAddress();
         range = new DexPrinter.CodeRange(startCodeAddress, endCodeAddress);
         String exceptionType = SootToDexUtils.getDexTypeDescriptor(t.getException().getType());
         int codeAddress = labelAssigner.getLabel((Stmt)t.getHandlerUnit()).getCodeAddress();
         ImmutableExceptionHandler exceptionHandler = new ImmutableExceptionHandler(exceptionType, codeAddress);
         newHandlers = new ArrayList();
         Iterator var17 = codeRangesToTryItem.keySet().iterator();

         while(var17.hasNext()) {
            DexPrinter.CodeRange r = (DexPrinter.CodeRange)var17.next();
            List oldHandlers;
            if (r.containsRange(range)) {
               range.startAddress = r.startAddress;
               range.endAddress = r.endAddress;
               oldHandlers = (List)codeRangesToTryItem.get(r);
               if (oldHandlers != null) {
                  newHandlers.addAll(oldHandlers);
               }
               break;
            }

            if (range.containsRange(r)) {
               range.startAddress = r.startAddress;
               range.endAddress = r.endAddress;
               oldHandlers = (List)codeRangesToTryItem.get(range);
               if (oldHandlers != null) {
                  newHandlers.addAll(oldHandlers);
               }

               codeRangesToTryItem.remove(r);
               break;
            }
         }

         if (!newHandlers.contains(exceptionHandler)) {
            newHandlers.add(exceptionHandler);
         }
      }

      var6 = codeRangesToTryItem.keySet().iterator();

      DexPrinter.CodeRange range;
      while(var6.hasNext()) {
         range = (DexPrinter.CodeRange)var6.next();
         Iterator var21 = codeRangesToTryItem.keySet().iterator();

         while(var21.hasNext()) {
            DexPrinter.CodeRange r2 = (DexPrinter.CodeRange)var21.next();
            if (range != r2 && range.overlaps(r2)) {
               LOGGER.warn("Trap region overlaps detected");
            }
         }
      }

      var6 = codeRangesToTryItem.keySet().iterator();

      while(var6.hasNext()) {
         range = (DexPrinter.CodeRange)var6.next();
         boolean allCaughtForRange = false;
         Iterator var24 = ((List)codeRangesToTryItem.get(range)).iterator();

         while(var24.hasNext()) {
            ExceptionHandler handler = (ExceptionHandler)var24.next();
            if (!allCaughtForRange) {
               if ("Ljava/lang/Throwable;".equals(handler.getExceptionType())) {
                  allCaughtForRange = true;
               }

               builder.addCatch((TypeReference)(new ImmutableTypeReference(handler.getExceptionType())), labelAssigner.getLabelAtAddress(range.startAddress), labelAssigner.getLabelAtAddress(range.endAddress), labelAssigner.getLabelAtAddress(handler.getHandlerCodeAddress()));
            }
         }
      }

   }

   public void add(SootClass c) {
      if (!c.isPhantom()) {
         this.addAsClassDefItem(c);
         Map<String, File> dexClassIndex = SourceLocator.v().dexClassIndex();
         if (dexClassIndex != null) {
            File sourceForClass = (File)dexClassIndex.get(c.getName());
            if (sourceForClass != null && !sourceForClass.getName().endsWith(".dex")) {
               if (this.originalApk != null && !this.originalApk.equals(sourceForClass)) {
                  throw new CompilationDeathException("multiple APKs as source of an application are not supported");
               } else {
                  this.originalApk = sourceForClass;
               }
            }
         }
      }
   }

   public void print() {
      try {
         if (!Options.v().output_jar() && (this.originalApk == null || Options.v().output_format() == 11)) {
            String outputDir = SourceLocator.v().getOutputDir();
            LOGGER.info((String)"Writing dex files to \"{}\" folder.", (Object)outputDir);
            this.dexBuilder.writeTo(outputDir);
         } else {
            this.printZip();
         }

      } catch (IOException var2) {
         throw new CompilationDeathException("I/O exception while printing dex", var2);
      }
   }

   private static class CodeRange {
      int startAddress;
      int endAddress;

      public CodeRange(int startAddress, int endAddress) {
         this.startAddress = startAddress;
         this.endAddress = endAddress;
      }

      public boolean containsRange(DexPrinter.CodeRange r) {
         return r.startAddress >= this.startAddress && r.endAddress <= this.endAddress;
      }

      public boolean overlaps(DexPrinter.CodeRange r) {
         return r.startAddress >= this.startAddress && r.startAddress < this.endAddress || r.startAddress <= this.startAddress && r.endAddress > this.startAddress;
      }

      public String toString() {
         return this.startAddress + "-" + this.endAddress;
      }

      public boolean equals(Object other) {
         if (other == this) {
            return true;
         } else if (other != null && other instanceof DexPrinter.CodeRange) {
            DexPrinter.CodeRange cr = (DexPrinter.CodeRange)other;
            return this.startAddress == cr.startAddress && this.endAddress == cr.endAddress;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return 17 * this.startAddress + 13 * this.endAddress;
      }
   }
}
