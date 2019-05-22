package soot.JastAddJ;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class Attributes {
   protected BytecodeParser p;
   protected boolean isSynthetic;

   protected Attributes(BytecodeParser parser) {
      this.p = parser;
      this.isSynthetic = false;
   }

   protected void processAttribute(String attribute_name, int attribute_length) {
      if (attribute_name.equals("Synthetic")) {
         this.isSynthetic = true;
      } else {
         this.p.skip(attribute_length);
      }

   }

   protected void attributes() {
      int attributes_count = this.p.u2();

      for(int j = 0; j < attributes_count; ++j) {
         int attribute_name_index = this.p.u2();
         int attribute_length = this.p.u4();
         String attribute_name = this.p.getCONSTANT_Utf8_Info(attribute_name_index).string();
         this.processAttribute(attribute_name, attribute_length);
      }

   }

   public boolean isSynthetic() {
      return this.isSynthetic;
   }

   protected ElementValue readElementValue() {
      char c = (char)this.p.u1();
      switch(c) {
      case '@':
         return new ElementAnnotationValue(this.readAnnotation());
      case 'A':
      case 'E':
      case 'G':
      case 'H':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
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
      case 'f':
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
         throw new Error("AnnotationDefault tag " + c + " not supported");
      case 'B':
      case 'C':
      case 'D':
      case 'F':
      case 'I':
      case 'J':
      case 'S':
      case 'Z':
      case 's':
         int const_value_index = this.p.u2();
         Expr e = this.p.getCONSTANT_Info(const_value_index).expr();
         return new ElementConstantValue(e);
      case '[':
         int index = this.p.u2();
         List list = new List();

         for(int i = 0; i < index; ++i) {
            list.add(this.readElementValue());
         }

         return new ElementArrayValue(list);
      case 'c':
         int class_info_index = this.p.u2();
         String descriptor = this.p.getCONSTANT_Utf8_Info(class_info_index).string();
         Expr e = (new TypeDescriptor(this.p, descriptor)).type();
         return new ElementConstantValue(e);
      case 'e':
         int type_name_index = this.p.u2();
         String type_name = this.p.getCONSTANT_Utf8_Info(type_name_index).string();
         Access typeAccess = this.p.fromClassName(type_name.substring(1, type_name.length() - 1));
         int const_name_index = this.p.u2();
         String const_name = this.p.getCONSTANT_Utf8_Info(const_name_index).string();
         return new ElementConstantValue(typeAccess.qualifiesAccess(new VarAccess(const_name)));
      }
   }

   protected Annotation readAnnotation() {
      Access typeAccess = (new FieldDescriptor(this.p, "")).type();
      int num_element_value_pairs = this.p.u2();
      List list = new List();

      for(int i = 0; i < num_element_value_pairs; ++i) {
         int element_name_index = this.p.u2();
         String element_name = this.p.getCONSTANT_Utf8_Info(element_name_index).string();
         ElementValue element_value = this.readElementValue();
         list.add(new ElementValuePair(element_name, element_value));
      }

      return new Annotation("Annotation", typeAccess, list);
   }

   public static class TypeAttributes extends Attributes {
      TypeDecl typeDecl;
      TypeDecl outerTypeDecl;
      Program classPath;
      private boolean isInnerClass;

      public TypeAttributes(BytecodeParser p, TypeDecl typeDecl, TypeDecl outerTypeDecl, Program classPath) {
         super(p);
         this.typeDecl = typeDecl;
         this.outerTypeDecl = outerTypeDecl;
         this.classPath = classPath;
         this.attributes();
      }

      public boolean isInnerClass() {
         return this.isInnerClass;
      }

      protected void processAttribute(String attribute_name, int attribute_length) {
         if (attribute_name.equals("InnerClasses")) {
            this.innerClasses();
         } else {
            int num_annotations;
            if (attribute_name.equals("Signature")) {
               num_annotations = this.p.u2();
               String s = this.p.getCONSTANT_Utf8_Info(num_annotations).string();
               Signatures.ClassSignature classSignature = new Signatures.ClassSignature(s);
               this.typeDecl = this.typeDecl.makeGeneric(classSignature);
            } else {
               int j;
               Annotation a;
               if (attribute_name.equals("RuntimeVisibleAnnotations")) {
                  num_annotations = this.p.u2();

                  for(j = 0; j < num_annotations; ++j) {
                     a = this.readAnnotation();
                     this.typeDecl.getModifiers().addModifier(a);
                  }
               } else if (attribute_name.equals("RuntimeInvisibleAnnotations")) {
                  num_annotations = this.p.u2();

                  for(j = 0; j < num_annotations; ++j) {
                     a = this.readAnnotation();
                     this.typeDecl.getModifiers().addModifier(a);
                  }
               } else {
                  super.processAttribute(attribute_name, attribute_length);
               }
            }
         }

      }

      protected void innerClasses() {
         int number_of_classes = this.p.u2();

         for(int i = 0; i < number_of_classes; ++i) {
            int inner_class_info_index = this.p.u2();
            int outer_class_info_index = this.p.u2();
            int inner_name_index = this.p.u2();
            int inner_class_access_flags = this.p.u2();
            if (inner_class_info_index > 0) {
               CONSTANT_Class_Info inner_class_info = this.p.getCONSTANT_Class_Info(inner_class_info_index);
               String inner_class_name = inner_class_info.name();
               String inner_name = inner_class_name.substring(inner_class_name.lastIndexOf("$") + 1);
               String outer_class_name;
               CONSTANT_Class_Info m;
               if (outer_class_info_index > 0) {
                  m = this.p.getCONSTANT_Class_Info(outer_class_info_index);
                  if (inner_class_info == null || m == null) {
                     System.out.println("Null");
                  }

                  outer_class_name = m.name();
               } else {
                  outer_class_name = inner_class_name.substring(0, inner_class_name.lastIndexOf("$"));
               }

               if (inner_class_info.name().equals(this.p.classInfo.name())) {
                  this.typeDecl.setID(inner_name);
                  this.typeDecl.setModifiers(BytecodeParser.modifiers(inner_class_access_flags & 1055));
                  if (this.p.outerClassName != null && this.p.outerClassName.equals(outer_class_name)) {
                     m = null;
                     if (this.typeDecl instanceof ClassDecl) {
                        MemberTypeDecl m = new MemberClassDecl((ClassDecl)this.typeDecl);
                        this.outerTypeDecl.addBodyDecl(m);
                     } else if (this.typeDecl instanceof InterfaceDecl) {
                        MemberTypeDecl m = new MemberInterfaceDecl((InterfaceDecl)this.typeDecl);
                        this.outerTypeDecl.addBodyDecl(m);
                     }
                  } else {
                     this.isInnerClass = true;
                  }
               }

               if (outer_class_name.equals(this.p.classInfo.name())) {
                  try {
                     InputStream is = null;

                     try {
                        is = this.classPath.getInputStream(inner_class_name);
                        if (is != null) {
                           BytecodeParser p = new BytecodeParser(is, this.p.name);
                           p.parse(this.typeDecl, outer_class_name, this.classPath, (inner_class_access_flags & 8) == 0);
                        } else {
                           this.p.println("Error: ClassFile " + inner_class_name + " not found");
                        }
                     } catch (Error var18) {
                        if (!var18.getMessage().startsWith("Could not find nested type")) {
                           throw var18;
                        }
                     } finally {
                        if (is != null) {
                           is.close();
                           m = null;
                        }

                     }
                  } catch (FileNotFoundException var20) {
                     System.out.println("Error: " + inner_class_name + " not found");
                  } catch (Exception var21) {
                     throw new RuntimeException(var21);
                  }
               }
            }
         }

      }
   }

   public static class MethodAttributes extends Attributes {
      protected List exceptionList;
      protected ElementValue elementValue;
      public Signatures.MethodSignature methodSignature;
      public ArrayList annotations;
      public ArrayList[] parameterAnnotations;

      public MethodAttributes(BytecodeParser p) {
         super(p);
         this.attributes();
      }

      protected void processAttribute(String attribute_name, int attribute_length) {
         if (attribute_name.equals("Exceptions")) {
            this.parseExceptions();
         } else if (attribute_name.equals("AnnotationDefault")) {
            this.annotationDefault();
         } else {
            int num_parameters;
            if (attribute_name.equals("Signature")) {
               num_parameters = this.p.u2();
               String s = this.p.getCONSTANT_Utf8_Info(num_parameters).string();
               this.methodSignature = new Signatures.MethodSignature(s);
            } else {
               int i;
               if (attribute_name.equals("RuntimeVisibleAnnotations")) {
                  num_parameters = this.p.u2();
                  if (this.annotations == null) {
                     this.annotations = new ArrayList();
                  }

                  for(i = 0; i < num_parameters; ++i) {
                     this.annotations.add(this.readAnnotation());
                  }
               } else if (attribute_name.equals("RuntimeInvisibleAnnotations")) {
                  num_parameters = this.p.u2();
                  if (this.annotations == null) {
                     this.annotations = new ArrayList();
                  }

                  for(i = 0; i < num_parameters; ++i) {
                     this.annotations.add(this.readAnnotation());
                  }
               } else {
                  int num_annotations;
                  int j;
                  if (attribute_name.equals("RuntimeVisibleParameterAnnotations")) {
                     num_parameters = this.p.u1();
                     if (this.parameterAnnotations == null) {
                        this.parameterAnnotations = new ArrayList[num_parameters];
                     }

                     for(i = 0; i < num_parameters; ++i) {
                        if (this.parameterAnnotations[i] == null) {
                           this.parameterAnnotations[i] = new ArrayList();
                        }

                        num_annotations = this.p.u2();

                        for(j = 0; j < num_annotations; ++j) {
                           this.parameterAnnotations[i].add(this.readAnnotation());
                        }
                     }
                  } else if (attribute_name.equals("RuntimeInvisibleParameterAnnotations")) {
                     num_parameters = this.p.u1();
                     if (this.parameterAnnotations == null) {
                        this.parameterAnnotations = new ArrayList[num_parameters];
                     }

                     for(i = 0; i < num_parameters; ++i) {
                        if (this.parameterAnnotations[i] == null) {
                           this.parameterAnnotations[i] = new ArrayList();
                        }

                        num_annotations = this.p.u2();

                        for(j = 0; j < num_annotations; ++j) {
                           this.parameterAnnotations[i].add(this.readAnnotation());
                        }
                     }
                  } else {
                     super.processAttribute(attribute_name, attribute_length);
                  }
               }
            }
         }

      }

      private void parseExceptions() {
         int number_of_exceptions = this.p.u2();
         this.exceptionList = new List();

         for(int i = 0; i < number_of_exceptions; ++i) {
            CONSTANT_Class_Info exception = this.p.getCONSTANT_Class_Info(this.p.u2());
            this.exceptionList.add(exception.access());
         }

      }

      public List exceptionList() {
         return this.exceptionList != null ? this.exceptionList : new List();
      }

      public ElementValue elementValue() {
         return this.elementValue;
      }

      private void annotationDefault() {
         this.elementValue = this.readElementValue();
      }
   }

   public static class FieldAttributes extends Attributes {
      protected CONSTANT_Info constantValue;
      public ArrayList annotations;
      public Signatures.FieldSignature fieldSignature;

      public FieldAttributes(BytecodeParser p) {
         super(p);
         this.attributes();
      }

      protected void processAttribute(String attribute_name, int attribute_length) {
         int num_annotations;
         if (attribute_name.equals("ConstantValue") && attribute_length == 2) {
            num_annotations = this.p.u2();
            this.constantValue = this.p.getCONSTANT_Info(num_annotations);
         } else {
            int j;
            if (attribute_name.equals("RuntimeVisibleAnnotations")) {
               num_annotations = this.p.u2();
               if (this.annotations == null) {
                  this.annotations = new ArrayList();
               }

               for(j = 0; j < num_annotations; ++j) {
                  this.annotations.add(this.readAnnotation());
               }
            } else if (attribute_name.equals("RuntimeInvisibleAnnotations")) {
               num_annotations = this.p.u2();
               if (this.annotations == null) {
                  this.annotations = new ArrayList();
               }

               for(j = 0; j < num_annotations; ++j) {
                  this.annotations.add(this.readAnnotation());
               }
            } else if (attribute_name.equals("Signature")) {
               num_annotations = this.p.u2();
               String s = this.p.getCONSTANT_Utf8_Info(num_annotations).string();
               this.fieldSignature = new Signatures.FieldSignature(s);
            } else {
               super.processAttribute(attribute_name, attribute_length);
            }
         }

      }

      public CONSTANT_Info constantValue() {
         return this.constantValue;
      }
   }
}
