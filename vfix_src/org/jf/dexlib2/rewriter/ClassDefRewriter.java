package org.jf.dexlib2.rewriter;

import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;

public class ClassDefRewriter implements Rewriter<ClassDef> {
   @Nonnull
   protected final Rewriters rewriters;

   public ClassDefRewriter(@Nonnull Rewriters rewriters) {
      this.rewriters = rewriters;
   }

   @Nonnull
   public ClassDef rewrite(@Nonnull ClassDef classDef) {
      return new ClassDefRewriter.RewrittenClassDef(classDef);
   }

   protected class RewrittenClassDef extends BaseTypeReference implements ClassDef {
      @Nonnull
      protected ClassDef classDef;

      public RewrittenClassDef(@Nonnull ClassDef classdef) {
         this.classDef = classdef;
      }

      @Nonnull
      public String getType() {
         return (String)ClassDefRewriter.this.rewriters.getTypeRewriter().rewrite(this.classDef.getType());
      }

      public int getAccessFlags() {
         return this.classDef.getAccessFlags();
      }

      @Nullable
      public String getSuperclass() {
         return (String)RewriterUtils.rewriteNullable(ClassDefRewriter.this.rewriters.getTypeRewriter(), this.classDef.getSuperclass());
      }

      @Nonnull
      public List<String> getInterfaces() {
         return RewriterUtils.rewriteList(ClassDefRewriter.this.rewriters.getTypeRewriter(), this.classDef.getInterfaces());
      }

      @Nullable
      public String getSourceFile() {
         return this.classDef.getSourceFile();
      }

      @Nonnull
      public Set<? extends Annotation> getAnnotations() {
         return RewriterUtils.rewriteSet(ClassDefRewriter.this.rewriters.getAnnotationRewriter(), this.classDef.getAnnotations());
      }

      @Nonnull
      public Iterable<? extends Field> getStaticFields() {
         return RewriterUtils.rewriteIterable(ClassDefRewriter.this.rewriters.getFieldRewriter(), this.classDef.getStaticFields());
      }

      @Nonnull
      public Iterable<? extends Field> getInstanceFields() {
         return RewriterUtils.rewriteIterable(ClassDefRewriter.this.rewriters.getFieldRewriter(), this.classDef.getInstanceFields());
      }

      @Nonnull
      public Iterable<? extends Field> getFields() {
         return new Iterable<Field>() {
            @Nonnull
            public Iterator<Field> iterator() {
               return Iterators.concat(RewrittenClassDef.this.getStaticFields().iterator(), RewrittenClassDef.this.getInstanceFields().iterator());
            }
         };
      }

      @Nonnull
      public Iterable<? extends Method> getDirectMethods() {
         return RewriterUtils.rewriteIterable(ClassDefRewriter.this.rewriters.getMethodRewriter(), this.classDef.getDirectMethods());
      }

      @Nonnull
      public Iterable<? extends Method> getVirtualMethods() {
         return RewriterUtils.rewriteIterable(ClassDefRewriter.this.rewriters.getMethodRewriter(), this.classDef.getVirtualMethods());
      }

      @Nonnull
      public Iterable<? extends Method> getMethods() {
         return new Iterable<Method>() {
            @Nonnull
            public Iterator<Method> iterator() {
               return Iterators.concat(RewrittenClassDef.this.getDirectMethods().iterator(), RewrittenClassDef.this.getVirtualMethods().iterator());
            }
         };
      }
   }
}
