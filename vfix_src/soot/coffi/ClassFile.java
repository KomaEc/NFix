package soot.coffi;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Timers;
import soot.options.Options;

public class ClassFile {
   private static final Logger logger = LoggerFactory.getLogger(ClassFile.class);
   static final long MAGIC = 3405691582L;
   static final short ACC_PUBLIC = 1;
   static final short ACC_PRIVATE = 2;
   static final short ACC_PROTECTED = 4;
   static final short ACC_STATIC = 8;
   static final short ACC_FINAL = 16;
   static final short ACC_SUPER = 32;
   static final short ACC_VOLATILE = 64;
   static final short ACC_TRANSIENT = 128;
   static final short ACC_INTERFACE = 512;
   static final short ACC_ABSTRACT = 1024;
   static final short ACC_STRICT = 2048;
   static final short ACC_ANNOTATION = 8192;
   static final short ACC_ENUM = 16384;
   static final short ACC_UNKNOWN = 28672;
   static final String DESC_BYTE = "B";
   static final String DESC_CHAR = "C";
   static final String DESC_DOUBLE = "D";
   static final String DESC_FLOAT = "F";
   static final String DESC_INT = "I";
   static final String DESC_LONG = "J";
   static final String DESC_OBJECT = "L";
   static final String DESC_SHORT = "S";
   static final String DESC_BOOLEAN = "Z";
   static final String DESC_VOID = "V";
   static final String DESC_ARRAY = "[";
   boolean debug;
   String fn;
   long magic;
   int minor_version;
   int major_version;
   public int constant_pool_count;
   public cp_info[] constant_pool;
   public int access_flags;
   public int this_class;
   public int super_class;
   public int interfaces_count;
   public int[] interfaces;
   public int fields_count;
   public field_info[] fields;
   public int methods_count;
   public method_info[] methods;
   public int attributes_count;
   public attribute_info[] attributes;
   public BootstrapMethods_attribute bootstrap_methods_attribute;

   public ClassFile(String nfn) {
      this.fn = nfn;
   }

   public String toString() {
      return this.constant_pool[this.this_class].toString(this.constant_pool);
   }

   public boolean loadClassFile(InputStream is) {
      InputStream f = null;
      InputStream classFileStream = is;
      if (Options.v().time()) {
         Timers.v().readTimer.start();
      }

      try {
         DataInputStream classDataStream = new DataInputStream(classFileStream);
         byte[] data = new byte[classDataStream.available()];
         classDataStream.readFully(data);
         f = new ByteArrayInputStream(data);
      } catch (IOException var8) {
         logger.debug((String)var8.getMessage(), (Throwable)var8);
      }

      if (Options.v().time()) {
         Timers.v().readTimer.end();
      }

      DataInputStream d = new DataInputStream(f);
      boolean b = this.readClass(d);

      try {
         classFileStream.close();
         d.close();
         if (f != null) {
            f.close();
         }
      } catch (IOException var9) {
         logger.debug("IOException with " + this.fn + ": " + var9.getMessage());
         return false;
      }

      return b;
   }

   boolean saveClassFile() {
      FileOutputStream f;
      try {
         f = new FileOutputStream(this.fn);
      } catch (FileNotFoundException var8) {
         if (this.fn.indexOf(".class") >= 0) {
            logger.debug("Can't find " + this.fn);
            return false;
         }

         this.fn = this.fn + ".class";

         try {
            f = new FileOutputStream(this.fn);
         } catch (FileNotFoundException var7) {
            logger.debug("Can't find " + this.fn);
            return false;
         }
      }

      DataOutputStream d = new DataOutputStream(f);
      boolean b = this.writeClass(d);

      try {
         d.close();
         f.close();
         return b;
      } catch (IOException var6) {
         logger.debug("IOException with " + this.fn + ": " + var6.getMessage());
         return false;
      }
   }

   static String access_string(int af, String separator) {
      boolean hasone = false;
      String s = "";
      if ((af & 1) != 0) {
         s = "public";
         hasone = true;
      }

      if ((af & 2) != 0) {
         if (hasone) {
            s = s + separator;
         } else {
            hasone = true;
         }

         s = s + "private";
      }

      if ((af & 4) != 0) {
         if (hasone) {
            s = s + separator;
         } else {
            hasone = true;
         }

         s = s + "protected";
      }

      if ((af & 8) != 0) {
         if (hasone) {
            s = s + separator;
         } else {
            hasone = true;
         }

         s = s + "static";
      }

      if ((af & 16) != 0) {
         if (hasone) {
            s = s + separator;
         } else {
            hasone = true;
         }

         s = s + "final";
      }

      if ((af & 32) != 0) {
         if (hasone) {
            s = s + separator;
         } else {
            hasone = true;
         }

         s = s + "super";
      }

      if ((af & 64) != 0) {
         if (hasone) {
            s = s + separator;
         } else {
            hasone = true;
         }

         s = s + "volatile";
      }

      if ((af & 128) != 0) {
         if (hasone) {
            s = s + separator;
         } else {
            hasone = true;
         }

         s = s + "transient";
      }

      if ((af & 512) != 0) {
         if (hasone) {
            s = s + separator;
         } else {
            hasone = true;
         }

         s = s + "interface";
      }

      if ((af & 1024) != 0) {
         if (hasone) {
            s = s + separator;
         } else {
            hasone = true;
         }

         s = s + "abstract";
      }

      if ((af & 2048) != 0) {
         if (hasone) {
            s = s + separator;
         } else {
            hasone = true;
         }

         s = s + "strict";
      }

      if ((af & 8192) != 0) {
         if (hasone) {
            s = s + separator;
         } else {
            hasone = true;
         }

         s = s + "annotation";
      }

      if ((af & 16384) != 0) {
         if (hasone) {
            s = s + separator;
         } else {
            hasone = true;
         }

         s = s + "enum";
      }

      if ((af & 28672) != 0) {
         if (hasone) {
            s = s + separator;
         } else {
            hasone = true;
         }

         s = s + "unknown";
      }

      return s;
   }

   public boolean readClass(DataInputStream d) {
      try {
         this.magic = (long)d.readInt() & 4294967295L;
         if (this.magic != 3405691582L) {
            logger.debug("Wrong magic number in " + this.fn + ": " + this.magic);
            return false;
         } else {
            this.minor_version = d.readUnsignedShort();
            this.major_version = d.readUnsignedShort();
            this.constant_pool_count = d.readUnsignedShort();
            if (!this.readConstantPool(d)) {
               return false;
            } else {
               this.access_flags = d.readUnsignedShort();
               this.this_class = d.readUnsignedShort();
               this.super_class = d.readUnsignedShort();
               this.interfaces_count = d.readUnsignedShort();
               if (this.interfaces_count > 0) {
                  this.interfaces = new int[this.interfaces_count];

                  for(int j = 0; j < this.interfaces_count; ++j) {
                     this.interfaces[j] = d.readUnsignedShort();
                  }
               }

               if (Options.v().time()) {
                  Timers.v().fieldTimer.start();
               }

               this.fields_count = d.readUnsignedShort();
               this.readFields(d);
               if (Options.v().time()) {
                  Timers.v().fieldTimer.end();
               }

               if (Options.v().time()) {
                  Timers.v().methodTimer.start();
               }

               this.methods_count = d.readUnsignedShort();
               this.readMethods(d);
               if (Options.v().time()) {
                  Timers.v().methodTimer.end();
               }

               if (Options.v().time()) {
                  Timers.v().attributeTimer.start();
               }

               this.attributes_count = d.readUnsignedShort();
               if (this.attributes_count > 0) {
                  this.attributes = new attribute_info[this.attributes_count];
                  this.readAttributes(d, this.attributes_count, this.attributes);
               }

               if (Options.v().time()) {
                  Timers.v().attributeTimer.end();
               }

               return true;
            }
         }
      } catch (IOException var3) {
         throw new RuntimeException("IOException with " + this.fn + ": " + var3.getMessage(), var3);
      }
   }

   protected boolean readConstantPool(DataInputStream d) throws IOException {
      this.constant_pool = new cp_info[this.constant_pool_count];
      boolean skipone = false;

      for(int i = 1; i < this.constant_pool_count; ++i) {
         if (skipone) {
            skipone = false;
         } else {
            byte tag = (byte)d.readUnsignedByte();
            Object cp;
            switch(tag) {
            case 1:
               CONSTANT_Utf8_info cputf8 = new CONSTANT_Utf8_info(d);
               cp = CONSTANT_Utf8_collector.v().add(cputf8);
               if (this.debug) {
                  logger.debug("Constant pool[" + i + "]: Utf8 = \"" + cputf8.convert() + "\"");
               }
               break;
            case 2:
            case 13:
            case 14:
            case 16:
            case 17:
            default:
               logger.debug("Unknown tag in constant pool: " + tag + " at entry " + i);
               return false;
            case 3:
               cp = new CONSTANT_Integer_info();
               ((CONSTANT_Integer_info)cp).bytes = (long)d.readInt();
               if (this.debug) {
                  logger.debug("Constant pool[" + i + "]: Integer = " + ((CONSTANT_Integer_info)cp).bytes);
               }
               break;
            case 4:
               cp = new CONSTANT_Float_info();
               ((CONSTANT_Float_info)cp).bytes = (long)d.readInt();
               if (this.debug) {
                  logger.debug("Constant pool[" + i + "]: Float = " + ((CONSTANT_Float_info)cp).convert());
               }
               break;
            case 5:
               cp = new CONSTANT_Long_info();
               ((CONSTANT_Long_info)cp).high = (long)d.readInt() & 4294967295L;
               ((CONSTANT_Long_info)cp).low = (long)d.readInt() & 4294967295L;
               if (this.debug) {
                  String temp = ((cp_info)cp).toString(this.constant_pool);
                  logger.debug("Constant pool[" + i + "]: Long = " + temp);
               }

               skipone = true;
               break;
            case 6:
               cp = new CONSTANT_Double_info();
               ((CONSTANT_Double_info)cp).high = (long)d.readInt() & 4294967295L;
               ((CONSTANT_Double_info)cp).low = (long)d.readInt() & 4294967295L;
               if (this.debug) {
                  logger.debug("Constant pool[" + i + "]: Double = " + ((CONSTANT_Double_info)cp).convert());
               }

               skipone = true;
               break;
            case 7:
               cp = new CONSTANT_Class_info();
               ((CONSTANT_Class_info)cp).name_index = d.readUnsignedShort();
               if (this.debug) {
                  logger.debug("Constant pool[" + i + "]: Class");
               }
               break;
            case 8:
               cp = new CONSTANT_String_info();
               ((CONSTANT_String_info)cp).string_index = d.readUnsignedShort();
               if (this.debug) {
                  logger.debug("Constant pool[" + i + "]: String");
               }
               break;
            case 9:
               cp = new CONSTANT_Fieldref_info();
               ((CONSTANT_Fieldref_info)cp).class_index = d.readUnsignedShort();
               ((CONSTANT_Fieldref_info)cp).name_and_type_index = d.readUnsignedShort();
               if (this.debug) {
                  logger.debug("Constant pool[" + i + "]: Fieldref");
               }
               break;
            case 10:
               cp = new CONSTANT_Methodref_info();
               ((CONSTANT_Methodref_info)cp).class_index = d.readUnsignedShort();
               ((CONSTANT_Methodref_info)cp).name_and_type_index = d.readUnsignedShort();
               if (this.debug) {
                  logger.debug("Constant pool[" + i + "]: Methodref");
               }
               break;
            case 11:
               cp = new CONSTANT_InterfaceMethodref_info();
               ((CONSTANT_InterfaceMethodref_info)cp).class_index = d.readUnsignedShort();
               ((CONSTANT_InterfaceMethodref_info)cp).name_and_type_index = d.readUnsignedShort();
               if (this.debug) {
                  logger.debug("Constant pool[" + i + "]: MethodHandle");
               }
               break;
            case 12:
               cp = new CONSTANT_NameAndType_info();
               ((CONSTANT_NameAndType_info)cp).name_index = d.readUnsignedShort();
               ((CONSTANT_NameAndType_info)cp).descriptor_index = d.readUnsignedShort();
               if (this.debug) {
                  logger.debug("Constant pool[" + i + "]: Name and Type");
               }
               break;
            case 15:
               cp = new CONSTANT_MethodHandle_info();
               ((CONSTANT_MethodHandle_info)cp).kind = d.readByte();
               ((CONSTANT_MethodHandle_info)cp).target_index = d.readUnsignedShort();
               break;
            case 18:
               cp = new CONSTANT_InvokeDynamic_info();
               ((CONSTANT_InvokeDynamic_info)cp).bootstrap_method_index = d.readUnsignedShort();
               ((CONSTANT_InvokeDynamic_info)cp).name_and_type_index = d.readUnsignedShort();
            }

            ((cp_info)cp).tag = tag;
            this.constant_pool[i] = (cp_info)cp;
         }
      }

      return true;
   }

   private void readAllBytes(byte[] dest, DataInputStream d) throws IOException {
      int total_len = dest.length;

      int curr_read;
      for(int read_len = 0; read_len < total_len; read_len += curr_read) {
         int to_read = total_len - read_len;
         curr_read = d.read(dest, read_len, to_read);
      }

   }

   protected boolean readAttributes(DataInputStream d, int attributes_count, attribute_info[] ai) throws IOException {
      attribute_info a = null;

      for(int i = 0; i < attributes_count; ++i) {
         int j = d.readUnsignedShort();
         long len = (long)d.readInt() & 4294967295L;
         String s = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)this.constant_pool[j])).convert();
         if (s.compareTo("SourceFile") == 0) {
            SourceFile_attribute sa = new SourceFile_attribute();
            sa.sourcefile_index = d.readUnsignedShort();
            a = sa;
         } else if (s.compareTo("ConstantValue") == 0) {
            ConstantValue_attribute ca = new ConstantValue_attribute();
            ca.constantvalue_index = d.readUnsignedShort();
            a = ca;
         } else {
            int count;
            if (s.compareTo("Code") == 0) {
               Code_attribute ca = new Code_attribute();
               ca.max_stack = d.readUnsignedShort();
               ca.max_locals = d.readUnsignedShort();
               ca.code_length = (long)d.readInt() & 4294967295L;
               ca.code = new byte[(int)ca.code_length];
               this.readAllBytes(ca.code, d);
               ca.exception_table_length = d.readUnsignedShort();
               ca.exception_table = new exception_table_entry[ca.exception_table_length];

               for(count = 0; count < ca.exception_table_length; ++count) {
                  exception_table_entry e = new exception_table_entry();
                  e.start_pc = d.readUnsignedShort();
                  e.end_pc = d.readUnsignedShort();
                  e.handler_pc = d.readUnsignedShort();
                  e.catch_type = d.readUnsignedShort();
                  ca.exception_table[count] = e;
               }

               ca.attributes_count = d.readUnsignedShort();
               ca.attributes = new attribute_info[ca.attributes_count];
               this.readAttributes(d, ca.attributes_count, ca.attributes);
               a = ca;
            } else if (s.compareTo("Exceptions") == 0) {
               Exception_attribute ea = new Exception_attribute();
               ea.number_of_exceptions = d.readUnsignedShort();
               if (ea.number_of_exceptions > 0) {
                  ea.exception_index_table = new int[ea.number_of_exceptions];

                  for(count = 0; count < ea.number_of_exceptions; ++count) {
                     ea.exception_index_table[count] = d.readUnsignedShort();
                  }
               }

               a = ea;
            } else if (s.compareTo("LineNumberTable") == 0) {
               LineNumberTable_attribute la = new LineNumberTable_attribute();
               la.line_number_table_length = d.readUnsignedShort();
               la.line_number_table = new line_number_table_entry[la.line_number_table_length];

               for(count = 0; count < la.line_number_table_length; ++count) {
                  line_number_table_entry e = new line_number_table_entry();
                  e.start_pc = d.readUnsignedShort();
                  e.line_number = d.readUnsignedShort();
                  la.line_number_table[count] = e;
               }

               a = la;
            } else if (s.compareTo("LocalVariableTable") == 0) {
               LocalVariableTable_attribute la = new LocalVariableTable_attribute();
               la.local_variable_table_length = d.readUnsignedShort();
               la.local_variable_table = new local_variable_table_entry[la.local_variable_table_length];

               for(count = 0; count < la.local_variable_table_length; ++count) {
                  local_variable_table_entry e = new local_variable_table_entry();
                  e.start_pc = d.readUnsignedShort();
                  e.length = d.readUnsignedShort();
                  e.name_index = d.readUnsignedShort();
                  e.descriptor_index = d.readUnsignedShort();
                  e.index = d.readUnsignedShort();
                  la.local_variable_table[count] = e;
               }

               a = la;
            } else if (s.compareTo("LocalVariableTypeTable") == 0) {
               LocalVariableTypeTable_attribute la = new LocalVariableTypeTable_attribute();
               la.local_variable_type_table_length = d.readUnsignedShort();
               la.local_variable_type_table = new local_variable_type_table_entry[la.local_variable_type_table_length];

               for(count = 0; count < la.local_variable_type_table_length; ++count) {
                  local_variable_type_table_entry e = new local_variable_type_table_entry();
                  e.start_pc = d.readUnsignedShort();
                  e.length = d.readUnsignedShort();
                  e.name_index = d.readUnsignedShort();
                  e.signature_index = d.readUnsignedShort();
                  e.index = d.readUnsignedShort();
                  la.local_variable_type_table[count] = e;
               }

               a = la;
            } else if (s.compareTo("Synthetic") == 0) {
               Synthetic_attribute ia = new Synthetic_attribute();
               a = ia;
            } else if (s.compareTo("Signature") == 0) {
               Signature_attribute ia = new Signature_attribute();
               ia.signature_index = d.readUnsignedShort();
               a = ia;
            } else if (s.compareTo("Deprecated") == 0) {
               Deprecated_attribute da = new Deprecated_attribute();
               a = da;
            } else if (s.compareTo("EnclosingMethod") == 0) {
               EnclosingMethod_attribute ea = new EnclosingMethod_attribute();
               ea.class_index = d.readUnsignedShort();
               ea.method_index = d.readUnsignedShort();
               a = ea;
            } else if (s.compareTo("InnerClasses") == 0) {
               InnerClasses_attribute ia = new InnerClasses_attribute();
               ia.inner_classes_length = d.readUnsignedShort();
               ia.inner_classes = new inner_class_entry[ia.inner_classes_length];

               for(count = 0; count < ia.inner_classes_length; ++count) {
                  inner_class_entry e = new inner_class_entry();
                  e.inner_class_index = d.readUnsignedShort();
                  e.outer_class_index = d.readUnsignedShort();
                  e.name_index = d.readUnsignedShort();
                  e.access_flags = d.readUnsignedShort();
                  ia.inner_classes[count] = e;
               }

               a = ia;
            } else {
               annotation annot;
               if (s.compareTo("RuntimeVisibleAnnotations") == 0) {
                  RuntimeVisibleAnnotations_attribute ra = new RuntimeVisibleAnnotations_attribute();
                  ra.number_of_annotations = d.readUnsignedShort();
                  ra.annotations = new annotation[ra.number_of_annotations];

                  for(count = 0; count < ra.number_of_annotations; ++count) {
                     annot = new annotation();
                     annot.type_index = d.readUnsignedShort();
                     annot.num_element_value_pairs = d.readUnsignedShort();
                     annot.element_value_pairs = this.readElementValues(annot.num_element_value_pairs, d, true, 0);
                     ra.annotations[count] = annot;
                  }

                  a = ra;
               } else if (s.compareTo("RuntimeInvisibleAnnotations") == 0) {
                  RuntimeInvisibleAnnotations_attribute ra = new RuntimeInvisibleAnnotations_attribute();
                  ra.number_of_annotations = d.readUnsignedShort();
                  ra.annotations = new annotation[ra.number_of_annotations];

                  for(count = 0; count < ra.number_of_annotations; ++count) {
                     annot = new annotation();
                     annot.type_index = d.readUnsignedShort();
                     annot.num_element_value_pairs = d.readUnsignedShort();
                     annot.element_value_pairs = this.readElementValues(annot.num_element_value_pairs, d, true, 0);
                     ra.annotations[count] = annot;
                  }

                  a = ra;
               } else {
                  parameter_annotation pAnnot;
                  int k;
                  annotation annot;
                  if (s.compareTo("RuntimeVisibleParameterAnnotations") == 0) {
                     RuntimeVisibleParameterAnnotations_attribute ra = new RuntimeVisibleParameterAnnotations_attribute();
                     ra.num_parameters = d.readUnsignedByte();
                     ra.parameter_annotations = new parameter_annotation[ra.num_parameters];
                     count = 0;

                     while(true) {
                        if (count >= ra.num_parameters) {
                           a = ra;
                           break;
                        }

                        pAnnot = new parameter_annotation();
                        pAnnot.num_annotations = d.readUnsignedShort();
                        pAnnot.annotations = new annotation[pAnnot.num_annotations];

                        for(k = 0; k < pAnnot.num_annotations; ++k) {
                           annot = new annotation();
                           annot.type_index = d.readUnsignedShort();
                           annot.num_element_value_pairs = d.readUnsignedShort();
                           annot.element_value_pairs = this.readElementValues(annot.num_element_value_pairs, d, true, 0);
                           pAnnot.annotations[k] = annot;
                        }

                        ra.parameter_annotations[count] = pAnnot;
                        ++count;
                     }
                  } else if (s.compareTo("RuntimeInvisibleParameterAnnotations") == 0) {
                     RuntimeInvisibleParameterAnnotations_attribute ra = new RuntimeInvisibleParameterAnnotations_attribute();
                     ra.num_parameters = d.readUnsignedByte();
                     ra.parameter_annotations = new parameter_annotation[ra.num_parameters];
                     count = 0;

                     while(true) {
                        if (count >= ra.num_parameters) {
                           a = ra;
                           break;
                        }

                        pAnnot = new parameter_annotation();
                        pAnnot.num_annotations = d.readUnsignedShort();
                        pAnnot.annotations = new annotation[pAnnot.num_annotations];

                        for(k = 0; k < pAnnot.num_annotations; ++k) {
                           annot = new annotation();
                           annot.type_index = d.readUnsignedShort();
                           annot.num_element_value_pairs = d.readUnsignedShort();
                           annot.element_value_pairs = this.readElementValues(annot.num_element_value_pairs, d, true, 0);
                           pAnnot.annotations[k] = annot;
                        }

                        ra.parameter_annotations[count] = pAnnot;
                        ++count;
                     }
                  } else if (s.compareTo("AnnotationDefault") == 0) {
                     AnnotationDefault_attribute da = new AnnotationDefault_attribute();
                     element_value[] result = this.readElementValues(1, d, false, 0);
                     da.default_value = result[0];
                     a = da;
                  } else if (!s.equals("BootstrapMethods")) {
                     Generic_attribute ga = new Generic_attribute();
                     if (len > 0L) {
                        ga.info = new byte[(int)len];
                        this.readAllBytes(ga.info, d);
                     }

                     a = ga;
                  } else {
                     BootstrapMethods_attribute bsma = new BootstrapMethods_attribute();
                     count = d.readUnsignedShort();
                     bsma.method_handles = new short[count];
                     bsma.arg_indices = new short[count][];
                     int num = 0;

                     while(true) {
                        if (num >= count) {
                           assert this.bootstrap_methods_attribute == null : "More than one bootstrap methods attribute!";

                           a = this.bootstrap_methods_attribute = bsma;
                           break;
                        }

                        short index = (short)d.readUnsignedShort();
                        bsma.method_handles[num] = index;
                        int argCount = d.readUnsignedShort();
                        bsma.arg_indices[num] = new short[argCount];

                        for(int numArg = 0; numArg < argCount; ++numArg) {
                           short indexArg = (short)d.readUnsignedShort();
                           bsma.arg_indices[num][numArg] = indexArg;
                        }

                        ++num;
                     }
                  }
               }
            }
         }

         ((attribute_info)a).attribute_name = j;
         ((attribute_info)a).attribute_length = len;
         ai[i] = (attribute_info)a;
      }

      return true;
   }

   private element_value[] readElementValues(int count, DataInputStream d, boolean needName, int name_index) throws IOException {
      element_value[] list = new element_value[count];

      for(int x = 0; x < count; ++x) {
         if (needName) {
            name_index = d.readUnsignedShort();
         }

         int tag = d.readUnsignedByte();
         char kind = (char)tag;
         if (kind != 'B' && kind != 'C' && kind != 'D' && kind != 'F' && kind != 'I' && kind != 'J' && kind != 'S' && kind != 'Z' && kind != 's') {
            if (kind == 'e') {
               enum_constant_element_value elem = new enum_constant_element_value();
               elem.name_index = name_index;
               elem.tag = kind;
               elem.type_name_index = d.readUnsignedShort();
               elem.constant_name_index = d.readUnsignedShort();
               list[x] = elem;
            } else if (kind == 'c') {
               class_element_value elem = new class_element_value();
               elem.name_index = name_index;
               elem.tag = kind;
               elem.class_info_index = d.readUnsignedShort();
               list[x] = elem;
            } else if (kind == '[') {
               array_element_value elem = new array_element_value();
               elem.name_index = name_index;
               elem.tag = kind;
               elem.num_values = d.readUnsignedShort();
               elem.values = this.readElementValues(elem.num_values, d, false, name_index);
               list[x] = elem;
            } else {
               if (kind != '@') {
                  throw new RuntimeException("Unknown element value pair kind: " + kind);
               }

               annotation_element_value elem = new annotation_element_value();
               elem.name_index = name_index;
               elem.tag = kind;
               annotation annot = new annotation();
               annot.type_index = d.readUnsignedShort();
               annot.num_element_value_pairs = d.readUnsignedShort();
               annot.element_value_pairs = this.readElementValues(annot.num_element_value_pairs, d, true, 0);
               elem.annotation_value = annot;
               list[x] = elem;
            }
         } else {
            constant_element_value elem = new constant_element_value();
            elem.name_index = name_index;
            elem.tag = kind;
            elem.constant_value_index = d.readUnsignedShort();
            list[x] = elem;
         }
      }

      return list;
   }

   protected boolean readFields(DataInputStream d) throws IOException {
      this.fields = new field_info[this.fields_count];

      for(int i = 0; i < this.fields_count; ++i) {
         field_info fi = new field_info();
         fi.access_flags = d.readUnsignedShort();
         fi.name_index = d.readUnsignedShort();
         fi.descriptor_index = d.readUnsignedShort();
         fi.attributes_count = d.readUnsignedShort();
         if (fi.attributes_count > 0) {
            fi.attributes = new attribute_info[fi.attributes_count];
            this.readAttributes(d, fi.attributes_count, fi.attributes);
         }

         this.fields[i] = fi;
      }

      return true;
   }

   protected boolean readMethods(DataInputStream d) throws IOException {
      this.methods = new method_info[this.methods_count];

      for(int i = 0; i < this.methods_count; ++i) {
         method_info mi = new method_info();
         mi.access_flags = d.readUnsignedShort();
         mi.name_index = d.readUnsignedShort();
         mi.descriptor_index = d.readUnsignedShort();
         mi.attributes_count = d.readUnsignedShort();
         if (mi.attributes_count > 0) {
            mi.attributes = new attribute_info[mi.attributes_count];
            this.readAttributes(d, mi.attributes_count, mi.attributes);

            for(int j = 0; j < mi.attributes_count; ++j) {
               if (mi.attributes[j] instanceof Code_attribute) {
                  mi.code_attr = (Code_attribute)mi.attributes[j];
                  break;
               }
            }
         }

         this.methods[i] = mi;
      }

      return true;
   }

   protected boolean writeConstantPool(DataOutputStream dd) throws IOException {
      boolean skipone = false;

      for(int i = 1; i < this.constant_pool_count; ++i) {
         if (skipone) {
            skipone = false;
         } else {
            cp_info cp = this.constant_pool[i];
            dd.writeByte(cp.tag);
            switch(cp.tag) {
            case 1:
               ((CONSTANT_Utf8_info)cp).writeBytes(dd);
               break;
            case 2:
            default:
               logger.debug("Unknown tag in constant pool: " + cp.tag);
               return false;
            case 3:
               dd.writeInt((int)((CONSTANT_Integer_info)cp).bytes);
               break;
            case 4:
               dd.writeInt((int)((CONSTANT_Float_info)cp).bytes);
               break;
            case 5:
               dd.writeInt((int)((CONSTANT_Long_info)cp).high);
               dd.writeInt((int)((CONSTANT_Long_info)cp).low);
               skipone = true;
               break;
            case 6:
               dd.writeInt((int)((CONSTANT_Double_info)cp).high);
               dd.writeInt((int)((CONSTANT_Double_info)cp).low);
               skipone = true;
               break;
            case 7:
               dd.writeShort(((CONSTANT_Class_info)cp).name_index);
               break;
            case 8:
               dd.writeShort(((CONSTANT_String_info)cp).string_index);
               break;
            case 9:
               dd.writeShort(((CONSTANT_Fieldref_info)cp).class_index);
               dd.writeShort(((CONSTANT_Fieldref_info)cp).name_and_type_index);
               break;
            case 10:
               dd.writeShort(((CONSTANT_Methodref_info)cp).class_index);
               dd.writeShort(((CONSTANT_Methodref_info)cp).name_and_type_index);
               break;
            case 11:
               dd.writeShort(((CONSTANT_InterfaceMethodref_info)cp).class_index);
               dd.writeShort(((CONSTANT_InterfaceMethodref_info)cp).name_and_type_index);
               break;
            case 12:
               dd.writeShort(((CONSTANT_NameAndType_info)cp).name_index);
               dd.writeShort(((CONSTANT_NameAndType_info)cp).descriptor_index);
            }
         }
      }

      return true;
   }

   protected boolean writeAttributes(DataOutputStream dd, int attributes_count, attribute_info[] ai) throws IOException {
      attribute_info a = null;

      for(int i = 0; i < attributes_count; ++i) {
         a = ai[i];
         dd.writeShort(a.attribute_name);
         dd.writeInt((int)a.attribute_length);
         if (a instanceof SourceFile_attribute) {
            SourceFile_attribute sa = (SourceFile_attribute)a;
            dd.writeShort(sa.sourcefile_index);
         } else if (a instanceof ConstantValue_attribute) {
            ConstantValue_attribute ca = (ConstantValue_attribute)a;
            dd.writeShort(ca.constantvalue_index);
         } else {
            int k;
            if (!(a instanceof Code_attribute)) {
               if (a instanceof Exception_attribute) {
                  Exception_attribute ea = (Exception_attribute)a;
                  dd.writeShort(ea.number_of_exceptions);
                  if (ea.number_of_exceptions > 0) {
                     for(k = 0; k < ea.number_of_exceptions; ++k) {
                        dd.writeShort(ea.exception_index_table[k]);
                     }
                  }
               } else if (a instanceof LineNumberTable_attribute) {
                  LineNumberTable_attribute la = (LineNumberTable_attribute)a;
                  dd.writeShort(la.line_number_table_length);

                  for(k = 0; k < la.line_number_table_length; ++k) {
                     line_number_table_entry e = la.line_number_table[k];
                     dd.writeShort(e.start_pc);
                     dd.writeShort(e.line_number);
                  }
               } else if (a instanceof LocalVariableTable_attribute) {
                  LocalVariableTable_attribute la = (LocalVariableTable_attribute)a;
                  dd.writeShort(la.local_variable_table_length);

                  for(k = 0; k < la.local_variable_table_length; ++k) {
                     local_variable_table_entry e = la.local_variable_table[k];
                     dd.writeShort(e.start_pc);
                     dd.writeShort(e.length);
                     dd.writeShort(e.name_index);
                     dd.writeShort(e.descriptor_index);
                     dd.writeShort(e.index);
                  }
               } else {
                  logger.debug("Generic/Unknown Attribute in output");
                  Generic_attribute ga = (Generic_attribute)a;
                  if (ga.attribute_length > 0L) {
                     dd.write(ga.info, 0, (int)ga.attribute_length);
                  }
               }
            } else {
               Code_attribute ca = (Code_attribute)a;
               dd.writeShort(ca.max_stack);
               dd.writeShort(ca.max_locals);
               dd.writeInt((int)ca.code_length);
               dd.write(ca.code, 0, (int)ca.code_length);
               dd.writeShort(ca.exception_table_length);

               for(k = 0; k < ca.exception_table_length; ++k) {
                  exception_table_entry e = ca.exception_table[k];
                  dd.writeShort(e.start_pc);
                  dd.writeShort(e.end_pc);
                  dd.writeShort(e.handler_pc);
                  dd.writeShort(e.catch_type);
               }

               dd.writeShort(ca.attributes_count);
               if (ca.attributes_count > 0) {
                  this.writeAttributes(dd, ca.attributes_count, ca.attributes);
               }
            }
         }
      }

      return true;
   }

   protected boolean writeFields(DataOutputStream dd) throws IOException {
      for(int i = 0; i < this.fields_count; ++i) {
         field_info fi = this.fields[i];
         dd.writeShort(fi.access_flags);
         dd.writeShort(fi.name_index);
         dd.writeShort(fi.descriptor_index);
         dd.writeShort(fi.attributes_count);
         if (fi.attributes_count > 0) {
            this.writeAttributes(dd, fi.attributes_count, fi.attributes);
         }
      }

      return true;
   }

   protected boolean writeMethods(DataOutputStream dd) throws IOException {
      for(int i = 0; i < this.methods_count; ++i) {
         method_info mi = this.methods[i];
         dd.writeShort(mi.access_flags);
         dd.writeShort(mi.name_index);
         dd.writeShort(mi.descriptor_index);
         dd.writeShort(mi.attributes_count);
         if (mi.attributes_count > 0) {
            this.writeAttributes(dd, mi.attributes_count, mi.attributes);
         }
      }

      return true;
   }

   boolean writeClass(DataOutputStream dd) {
      try {
         dd.writeInt((int)this.magic);
         dd.writeShort(this.minor_version);
         dd.writeShort(this.major_version);
         dd.writeShort(this.constant_pool_count);
         if (!this.writeConstantPool(dd)) {
            return false;
         } else {
            dd.writeShort(this.access_flags);
            dd.writeShort(this.this_class);
            dd.writeShort(this.super_class);
            dd.writeShort(this.interfaces_count);
            if (this.interfaces_count > 0) {
               for(int j = 0; j < this.interfaces_count; ++j) {
                  dd.writeShort(this.interfaces[j]);
               }
            }

            dd.writeShort(this.fields_count);
            this.writeFields(dd);
            dd.writeShort(this.methods_count);
            this.writeMethods(dd);
            dd.writeShort(this.attributes_count);
            if (this.attributes_count > 0) {
               this.writeAttributes(dd, this.attributes_count, this.attributes);
            }

            return true;
         }
      } catch (IOException var3) {
         logger.debug("IOException with " + this.fn + ": " + var3.getMessage());
         return false;
      }
   }

   public Instruction parseMethod(method_info m) {
      Instruction head = null;
      Instruction tail = null;
      ByteCode bc = new ByteCode();
      Code_attribute ca = m.locate_code_attribute();
      if (ca == null) {
         return null;
      } else {
         int j;
         Instruction inst;
         for(j = 0; (long)j < ca.code_length; tail = inst) {
            inst = bc.disassemble_bytecode(ca.code, j);
            inst.originalIndex = j;
            if (inst instanceof Instruction_Unknown) {
               logger.debug("Unknown instruction in \"" + m.toName(this.constant_pool) + "\" at offset " + j);
               logger.debug(" bytecode = " + (inst.code & 255));
            }

            j = inst.nextOffset(j);
            if (head == null) {
               head = inst;
            } else {
               tail.next = inst;
               inst.prev = tail;
            }
         }

         bc.build(head);

         for(j = 0; j < ca.exception_table_length; ++j) {
            exception_table_entry e = ca.exception_table[j];
            e.start_inst = bc.locateInst(e.start_pc);
            if ((long)e.end_pc == ca.code_length) {
               e.end_inst = null;
            } else {
               e.end_inst = bc.locateInst(e.end_pc);
            }

            e.handler_inst = bc.locateInst(e.handler_pc);
            if (e.handler_inst != null) {
               e.handler_inst.labelled = true;
            }
         }

         m.instructions = head;
         attribute_info[] var9 = ca.attributes;
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            attribute_info element = var9[var11];
            if (element instanceof LineNumberTable_attribute) {
               LineNumberTable_attribute lntattr = (LineNumberTable_attribute)element;
               line_number_table_entry[] var14 = lntattr.line_number_table;
               int var15 = var14.length;

               for(int var16 = 0; var16 < var15; ++var16) {
                  line_number_table_entry element0 = var14[var16];
                  element0.start_inst = bc.locateInst(element0.start_pc);
               }
            }
         }

         return head;
      }
   }

   public void parse() {
      for(int i = 0; i < this.methods_count; ++i) {
         method_info mi = this.methods[i];
         mi.instructions = this.parseMethod(mi);
      }

   }

   int relabel(Instruction i) {
      int index;
      for(index = 0; i != null; i = i.next) {
         i.label = index;
         index = i.nextOffset(index);
      }

      return index;
   }

   byte[] unparseMethod(method_info m) {
      m.cfg.reconstructInstructions();
      int codesize = this.relabel(m.instructions);
      byte[] bc = new byte[codesize];
      Instruction i = m.instructions;

      for(codesize = 0; i != null; i = i.next) {
         codesize = i.compile(bc, codesize);
      }

      if (codesize != bc.length) {
         logger.warn("code size doesn't match array length!");
      }

      return bc;
   }

   void unparse() {
      for(int i = 0; i < this.methods_count; ++i) {
         method_info mi = this.methods[i];
         Code_attribute ca = mi.locate_code_attribute();
         if (ca != null) {
            byte[] bc = this.unparseMethod(mi);
            if (bc == null) {
               logger.debug("Recompile of " + mi.toName(this.constant_pool) + " failed!");
            } else {
               ca.code_length = (long)bc.length;
               ca.code = bc;

               for(int j = 0; j < ca.exception_table_length; ++j) {
                  exception_table_entry e = ca.exception_table[j];
                  e.start_pc = e.start_inst.label;
                  if (e.end_inst != null) {
                     e.end_pc = e.end_inst.label;
                  } else {
                     e.end_pc = (int)ca.code_length;
                  }

                  e.handler_pc = e.handler_inst.label;
               }
            }
         }
      }

   }

   static String parseMethodDesc_return(String s) {
      int j = s.lastIndexOf(41);
      return j >= 0 ? parseDesc(s.substring(j + 1), ",") : parseDesc(s, ",");
   }

   static String parseMethodDesc_params(String s) {
      int i = s.indexOf(40);
      if (i >= 0) {
         int j = s.indexOf(41, i + 1);
         if (j >= 0) {
            return parseDesc(s.substring(i + 1, j), ",");
         }
      }

      return "<parse error>";
   }

   static String parseDesc(String desc, String sep) {
      String params = "";
      int arraylevel = 0;
      boolean didone = false;
      int len = desc.length();

      for(int i = 0; i < len; ++i) {
         char c = desc.charAt(i);
         String param;
         if (c == "B".charAt(0)) {
            param = "byte";
         } else if (c == "C".charAt(0)) {
            param = "char";
         } else if (c == "D".charAt(0)) {
            param = "double";
         } else if (c == "F".charAt(0)) {
            param = "float";
         } else if (c == "I".charAt(0)) {
            param = "int";
         } else if (c == "J".charAt(0)) {
            param = "long";
         } else if (c == "S".charAt(0)) {
            param = "short";
         } else if (c == "Z".charAt(0)) {
            param = "boolean";
         } else if (c == "V".charAt(0)) {
            param = "void";
         } else {
            if (c == "[".charAt(0)) {
               ++arraylevel;
               continue;
            }

            if (c == "L".charAt(0)) {
               int j = desc.indexOf(59, i + 1);
               if (j < 0) {
                  logger.warn("Parse error -- can't find a ; in " + desc.substring(i + 1));
                  param = "<error>";
               } else {
                  if (j - i > 10 && desc.substring(i + 1, i + 11).compareTo("java/lang/") == 0) {
                     i += 10;
                  }

                  param = desc.substring(i + 1, j);
                  param = param.replace('/', '.');
                  i = j;
               }
            } else {
               param = "???";
            }
         }

         if (didone) {
            params = params + sep;
         }

         for(params = params + param; arraylevel > 0; --arraylevel) {
            params = params + "[]";
         }

         didone = true;
      }

      return params;
   }

   method_info findMethod(String s) {
      for(int i = 0; i < this.methods_count; ++i) {
         method_info m = this.methods[i];
         if (s.equals(m.toName(this.constant_pool))) {
            return m;
         }
      }

      return null;
   }

   void listMethods() {
      for(int i = 0; i < this.methods_count; ++i) {
         logger.debug("" + this.methods[i].prototype(this.constant_pool));
      }

   }

   void listConstantPool() {
      for(int i = 1; i < this.constant_pool_count; ++i) {
         cp_info c = this.constant_pool[i];
         logger.debug("[" + i + "] " + c.typeName() + "=" + c.toString(this.constant_pool));
         if (this.constant_pool[i].tag == 5 || this.constant_pool[i].tag == 6) {
            ++i;
         }
      }

   }

   void listFields() {
      for(int i = 0; i < this.fields_count; ++i) {
         field_info fi = this.fields[i];
         logger.debug("" + fi.prototype(this.constant_pool));

         for(int j = 0; j < fi.attributes_count; ++j) {
            CONSTANT_Utf8_info cm = (CONSTANT_Utf8_info)((CONSTANT_Utf8_info)this.constant_pool[fi.attributes[j].attribute_name]);
            if (cm.convert().compareTo("ConstantValue") == 0) {
               ConstantValue_attribute cva = (ConstantValue_attribute)((ConstantValue_attribute)fi.attributes[j]);
               logger.debug(" = " + this.constant_pool[cva.constantvalue_index].toString(this.constant_pool));
               break;
            }
         }

         logger.debug(";");
      }

   }

   void moveMethod(String m, int pos) {
      logger.debug("Moving " + m + " to position " + pos + " of " + this.methods_count);

      for(int i = 0; i < this.methods_count; ++i) {
         if (m.compareTo(this.methods[i].toName(this.constant_pool)) == 0) {
            method_info mthd = this.methods[i];
            int j;
            if (i > pos) {
               for(j = i; j > pos && j > 0; --j) {
                  this.methods[j] = this.methods[j - 1];
               }

               this.methods[pos] = mthd;
            } else if (i < pos) {
               for(j = i; j < pos && j < this.methods_count - 1; ++j) {
                  this.methods[j] = this.methods[j + 1];
               }

               this.methods[pos] = mthd;
            }

            return;
         }
      }

   }

   boolean descendsFrom(ClassFile cf) {
      return this.descendsFrom(cf.toString());
   }

   boolean descendsFrom(String cname) {
      cp_info cf = this.constant_pool[this.super_class];
      if (cf.toString(this.constant_pool).compareTo(cname) == 0) {
         return true;
      } else {
         for(int i = 0; i < this.interfaces_count; ++i) {
            cf = this.constant_pool[this.interfaces[i]];
            if (cf.toString(this.constant_pool).compareTo(cname) == 0) {
               return true;
            }
         }

         return false;
      }
   }

   boolean isSterile() {
      return (this.access_flags & 1) == 0 || (this.access_flags & 16) != 0;
   }

   boolean sameClass(String cfn) {
      String s = cfn;
      int i = cfn.lastIndexOf(".class");
      if (i > 0) {
         s = cfn.substring(0, i);
      }

      return s.compareTo(this.toString()) == 0;
   }

   String fieldName(int i) {
      return this.fields[i].toName(this.constant_pool);
   }
}
