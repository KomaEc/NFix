package org.objectweb.asm.xml;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

public final class SAXClassAdapter extends ClassVisitor {
   SAXAdapter sa;
   private final boolean singleDocument;
   private static final int ACCESS_CLASS = 262144;
   private static final int ACCESS_FIELD = 524288;
   private static final int ACCESS_INNER = 1048576;

   public SAXClassAdapter(ContentHandler h, boolean singleDocument) {
      super(327680);
      this.sa = new SAXAdapter(h);
      this.singleDocument = singleDocument;
      if (!singleDocument) {
         this.sa.addDocumentStart();
      }

   }

   public void visitSource(String source, String debug) {
      AttributesImpl att = new AttributesImpl();
      if (source != null) {
         att.addAttribute("", "file", "file", "", encode(source));
      }

      if (debug != null) {
         att.addAttribute("", "debug", "debug", "", encode(debug));
      }

      this.sa.addElement("source", att);
   }

   public void visitOuterClass(String owner, String name, String desc) {
      AttributesImpl att = new AttributesImpl();
      att.addAttribute("", "owner", "owner", "", owner);
      if (name != null) {
         att.addAttribute("", "name", "name", "", name);
      }

      if (desc != null) {
         att.addAttribute("", "desc", "desc", "", desc);
      }

      this.sa.addElement("outerclass", att);
   }

   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
      return new SAXAnnotationAdapter(this.sa, "annotation", visible ? 1 : -1, (String)null, desc);
   }

   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
      return new SAXAnnotationAdapter(this.sa, "typeAnnotation", visible ? 1 : -1, (String)null, desc, typeRef, typePath);
   }

   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
      StringBuilder sb = new StringBuilder();
      appendAccess(access | 262144, sb);
      AttributesImpl att = new AttributesImpl();
      att.addAttribute("", "access", "access", "", sb.toString());
      if (name != null) {
         att.addAttribute("", "name", "name", "", name);
      }

      if (signature != null) {
         att.addAttribute("", "signature", "signature", "", encode(signature));
      }

      if (superName != null) {
         att.addAttribute("", "parent", "parent", "", superName);
      }

      att.addAttribute("", "major", "major", "", Integer.toString(version & '\uffff'));
      att.addAttribute("", "minor", "minor", "", Integer.toString(version >>> 16));
      this.sa.addStart("class", att);
      this.sa.addStart("interfaces", new AttributesImpl());
      if (interfaces != null && interfaces.length > 0) {
         for(int i = 0; i < interfaces.length; ++i) {
            AttributesImpl att2 = new AttributesImpl();
            att2.addAttribute("", "name", "name", "", interfaces[i]);
            this.sa.addElement("interface", att2);
         }
      }

      this.sa.addEnd("interfaces");
   }

   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
      StringBuilder sb = new StringBuilder();
      appendAccess(access | 524288, sb);
      AttributesImpl att = new AttributesImpl();
      att.addAttribute("", "access", "access", "", sb.toString());
      att.addAttribute("", "name", "name", "", name);
      att.addAttribute("", "desc", "desc", "", desc);
      if (signature != null) {
         att.addAttribute("", "signature", "signature", "", encode(signature));
      }

      if (value != null) {
         att.addAttribute("", "value", "value", "", encode(value.toString()));
      }

      return new SAXFieldAdapter(this.sa, att);
   }

   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      StringBuilder sb = new StringBuilder();
      appendAccess(access, sb);
      AttributesImpl att = new AttributesImpl();
      att.addAttribute("", "access", "access", "", sb.toString());
      att.addAttribute("", "name", "name", "", name);
      att.addAttribute("", "desc", "desc", "", desc);
      if (signature != null) {
         att.addAttribute("", "signature", "signature", "", signature);
      }

      this.sa.addStart("method", att);
      this.sa.addStart("exceptions", new AttributesImpl());
      if (exceptions != null && exceptions.length > 0) {
         for(int i = 0; i < exceptions.length; ++i) {
            AttributesImpl att2 = new AttributesImpl();
            att2.addAttribute("", "name", "name", "", exceptions[i]);
            this.sa.addElement("exception", att2);
         }
      }

      this.sa.addEnd("exceptions");
      return new SAXCodeAdapter(this.sa, access);
   }

   public final void visitInnerClass(String name, String outerName, String innerName, int access) {
      StringBuilder sb = new StringBuilder();
      appendAccess(access | 1048576, sb);
      AttributesImpl att = new AttributesImpl();
      att.addAttribute("", "access", "access", "", sb.toString());
      if (name != null) {
         att.addAttribute("", "name", "name", "", name);
      }

      if (outerName != null) {
         att.addAttribute("", "outerName", "outerName", "", outerName);
      }

      if (innerName != null) {
         att.addAttribute("", "innerName", "innerName", "", innerName);
      }

      this.sa.addElement("innerclass", att);
   }

   public final void visitEnd() {
      this.sa.addEnd("class");
      if (!this.singleDocument) {
         this.sa.addDocumentEnd();
      }

   }

   static final String encode(String s) {
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < s.length(); ++i) {
         char c = s.charAt(i);
         if (c == '\\') {
            sb.append("\\\\");
         } else if (c >= ' ' && c <= 127) {
            sb.append(c);
         } else {
            sb.append("\\u");
            if (c < 16) {
               sb.append("000");
            } else if (c < 256) {
               sb.append("00");
            } else if (c < 4096) {
               sb.append('0');
            }

            sb.append(Integer.toString(c, 16));
         }
      }

      return sb.toString();
   }

   static void appendAccess(int access, StringBuilder sb) {
      if ((access & 1) != 0) {
         sb.append("public ");
      }

      if ((access & 2) != 0) {
         sb.append("private ");
      }

      if ((access & 4) != 0) {
         sb.append("protected ");
      }

      if ((access & 16) != 0) {
         sb.append("final ");
      }

      if ((access & 8) != 0) {
         sb.append("static ");
      }

      if ((access & 32) != 0) {
         if ((access & 262144) == 0) {
            sb.append("synchronized ");
         } else {
            sb.append("super ");
         }
      }

      if ((access & 64) != 0) {
         if ((access & 524288) == 0) {
            sb.append("bridge ");
         } else {
            sb.append("volatile ");
         }
      }

      if ((access & 128) != 0) {
         if ((access & 524288) == 0) {
            sb.append("varargs ");
         } else {
            sb.append("transient ");
         }
      }

      if ((access & 256) != 0) {
         sb.append("native ");
      }

      if ((access & 2048) != 0) {
         sb.append("strict ");
      }

      if ((access & 512) != 0) {
         sb.append("interface ");
      }

      if ((access & 1024) != 0) {
         sb.append("abstract ");
      }

      if ((access & 4096) != 0) {
         sb.append("synthetic ");
      }

      if ((access & 8192) != 0) {
         sb.append("annotation ");
      }

      if ((access & 16384) != 0) {
         sb.append("enum ");
      }

      if ((access & 131072) != 0) {
         sb.append("deprecated ");
      }

      if ((access & '耀') != 0) {
         sb.append("mandated ");
      }

   }
}
