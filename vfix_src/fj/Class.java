package fj;

import fj.data.List;
import fj.data.Option;
import fj.data.Tree;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class Class<T> {
   private final java.lang.Class<T> c;

   private Class(java.lang.Class<T> c) {
      this.c = c;
   }

   public List<Class<? super T>> inheritance() {
      return List.unfold(new F<java.lang.Class<? super T>, Option<P2<java.lang.Class<? super T>, java.lang.Class<? super T>>>>() {
         public Option<P2<java.lang.Class<? super T>, java.lang.Class<? super T>>> f(final java.lang.Class<? super T> c) {
            if (c == null) {
               return Option.none();
            } else {
               P2<java.lang.Class<? super T>, java.lang.Class<? super T>> p = new P2<java.lang.Class<? super T>, java.lang.Class<? super T>>() {
                  public java.lang.Class<? super T> _1() {
                     return c;
                  }

                  public java.lang.Class<? super T> _2() {
                     return c.getSuperclass();
                  }
               };
               return Option.some(p);
            }
         }
      }, this.c).map(new F<java.lang.Class<? super T>, Class<? super T>>() {
         public Class<? super T> f(java.lang.Class<? super T> c) {
            return Class.clas(c);
         }
      });
   }

   public Tree<Type> classParameters() {
      return typeParameterTree(this.c);
   }

   public Tree<Type> superclassParameters() {
      return typeParameterTree(this.c.getGenericSuperclass());
   }

   public List<Tree<Type>> interfaceParameters() {
      List<Tree<Type>> ts = List.nil();
      java.lang.Class[] var2 = this.c.getInterfaces();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Type t = var2[var4];
         ts = ts.snoc(typeParameterTree(t));
      }

      return ts;
   }

   public static Tree<Type> typeParameterTree(Type t) {
      List<Tree<Type>> typeArgs = List.nil();
      Tree types;
      if (t instanceof ParameterizedType) {
         ParameterizedType pt = (ParameterizedType)t;
         Type[] var4 = pt.getActualTypeArguments();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Type arg = var4[var6];
            typeArgs = typeArgs.snoc(typeParameterTree(arg));
         }

         types = Tree.node(pt.getRawType(), (List)typeArgs);
      } else {
         types = Tree.node(t, (List)List.nil());
      }

      return types;
   }

   public java.lang.Class<T> clas() {
      return this.c;
   }

   public static <T> Class<T> clas(java.lang.Class<T> c) {
      return new Class(c);
   }
}
