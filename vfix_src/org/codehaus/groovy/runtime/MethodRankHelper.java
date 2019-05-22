package org.codehaus.groovy.runtime;

import groovy.lang.MetaMethod;
import groovy.lang.MetaProperty;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.ClassInfo;

public class MethodRankHelper {
   public static final int DL_SUBSTITUTION = 10;
   public static final int DL_DELETE = 10;
   public static final int DL_TRANSPOSITION = 5;
   public static final int DL_CASE = 5;
   public static final int MAX_RECOMENDATIONS = 5;
   public static final int MAX_METHOD_SCORE = 50;
   public static final int MAX_CONSTRUCTOR_SCORE = 20;
   public static final int MAX_FIELD_SCORE = 30;

   public static String getMethodSuggestionString(String methodName, Class type, Object[] arguments) {
      ClassInfo ci = ClassInfo.getClassInfo(type);
      List<MetaMethod> methods = new ArrayList(ci.getMetaClass().getMethods());
      methods.addAll(ci.getMetaClass().getMetaMethods());
      List<MetaMethod> sugg = rankMethods(methodName, arguments, methods);
      StringBuffer sb = new StringBuffer();
      if (!sugg.isEmpty()) {
         sb.append("\nPossible solutions: ");

         for(int i = 0; i < sugg.size(); ++i) {
            if (i != 0) {
               sb.append(", ");
            }

            sb.append(((MetaMethod)sugg.get(i)).getName()).append("(");
            sb.append(listParameterNames(((MetaMethod)sugg.get(i)).getParameterTypes()));
            sb.append(")");
         }
      }

      Class[] argumentClasses = getArgumentClasses(arguments);
      List<MethodRankHelper.Pair<Class, Class>> conflictClasses = getConflictClasses(sugg, argumentClasses);
      if (!conflictClasses.isEmpty()) {
         sb.append("\nThe following classes appear as argument class and as parameter class, ");
         sb.append("but are defined by different class loader:\n");
         boolean first = true;
         Iterator i$ = conflictClasses.iterator();

         while(i$.hasNext()) {
            MethodRankHelper.Pair<Class, Class> pair = (MethodRankHelper.Pair)i$.next();
            if (!first) {
               sb.append(", ");
            } else {
               first = false;
            }

            sb.append(((Class)pair.u).getName()).append(" (defined by '");
            sb.append(((Class)pair.u).getClassLoader());
            sb.append("' and '");
            sb.append(((Class)pair.v).getClassLoader());
            sb.append("')");
         }

         sb.append("\nIf one of the method suggestions matches the method you wanted to call, ");
         sb.append("\nthen check your class loader setup.");
      }

      return sb.toString();
   }

   private static List<MethodRankHelper.Pair<Class, Class>> getConflictClasses(List<MetaMethod> sugg, Class[] argumentClasses) {
      List<MethodRankHelper.Pair<Class, Class>> ret = new LinkedList();
      Set<Class> recordedClasses = new HashSet();
      Iterator i$ = sugg.iterator();

      while(i$.hasNext()) {
         MetaMethod method = (MetaMethod)i$.next();
         Class[] para = method.getNativeParameterTypes();
         Class[] arr$ = para;
         int len$ = para.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class aPara = arr$[i$];
            if (!recordedClasses.contains(aPara)) {
               Class[] arr$ = argumentClasses;
               int len$ = argumentClasses.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  Class argumentClass = arr$[i$];
                  if (argumentClass != null && argumentClass != aPara && argumentClass.getName().equals(aPara.getName())) {
                     ret.add(new MethodRankHelper.Pair(argumentClass, aPara));
                  }
               }

               recordedClasses.add(aPara);
            }
         }
      }

      return ret;
   }

   private static Class[] getArgumentClasses(Object[] arguments) {
      Class[] argumentClasses = new Class[arguments.length];

      for(int i = 0; i < argumentClasses.length; ++i) {
         Object arg = arguments[i];
         if (arg != null) {
            argumentClasses[i] = arg.getClass();
         }
      }

      return argumentClasses;
   }

   public static String getConstructorSuggestionString(Class type, Object[] arguments) {
      Constructor[] sugg = rankConstructors(arguments, type.getConstructors());
      if (sugg.length > 0) {
         StringBuffer sb = new StringBuffer();
         sb.append("\nPossible solutions: ");

         for(int i = 0; i < sugg.length; ++i) {
            if (i != 0) {
               sb.append(", ");
            }

            sb.append(type.getName()).append("(");
            sb.append(listParameterNames(sugg[i].getParameterTypes()));
            sb.append(")");
         }

         return sb.toString();
      } else {
         return "";
      }
   }

   public static String getPropertySuggestionString(String fieldName, Class type) {
      ClassInfo ci = ClassInfo.getClassInfo(type);
      List<MetaProperty> fi = ci.getMetaClass().getProperties();
      List<MethodRankHelper.RankableField> rf = new ArrayList(fi.size());
      StringBuffer sb = new StringBuffer();
      sb.append("\nPossible solutions: ");
      Iterator i$ = fi.iterator();

      while(i$.hasNext()) {
         MetaProperty mp = (MetaProperty)i$.next();
         rf.add(new MethodRankHelper.RankableField(fieldName, mp));
      }

      Collections.sort(rf);
      int i = 0;

      for(Iterator i$ = rf.iterator(); i$.hasNext(); ++i) {
         MethodRankHelper.RankableField f = (MethodRankHelper.RankableField)i$.next();
         if (i > 5 || f.score > 30) {
            break;
         }

         if (i > 0) {
            sb.append(", ");
         }

         sb.append(f.f.getName());
      }

      return i > 0 ? sb.toString() : "";
   }

   private static String listParameterNames(Class[] cachedClasses) {
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < cachedClasses.length; ++i) {
         if (i != 0) {
            sb.append(", ");
         }

         sb.append(cachedClasses[i].getName());
      }

      return sb.toString();
   }

   private static String listParameterNames(CachedClass[] cachedClasses) {
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < cachedClasses.length; ++i) {
         if (i != 0) {
            sb.append(", ");
         }

         sb.append(cachedClasses[i].getName());
      }

      return sb.toString();
   }

   private static List<MetaMethod> rankMethods(String name, Object[] original, List<MetaMethod> methods) {
      List<MethodRankHelper.RankableMethod> rm = new ArrayList(methods.size());
      if (original == null) {
         original = new Object[0];
      }

      Class[] ta = new Class[original.length];
      Class nullC = MethodRankHelper.NullObject.class;

      for(int i = 0; i < original.length; ++i) {
         ta[i] = original[i] == null ? nullC : original[i].getClass();
      }

      Iterator i$ = methods.iterator();

      while(i$.hasNext()) {
         MetaMethod m = (MetaMethod)i$.next();
         rm.add(new MethodRankHelper.RankableMethod(name, ta, m));
      }

      Collections.sort(rm);
      List<MetaMethod> l = new ArrayList(rm.size());
      Iterator i$ = rm.iterator();

      while(i$.hasNext()) {
         MethodRankHelper.RankableMethod m = (MethodRankHelper.RankableMethod)i$.next();
         if (l.size() > 5 || m.score > 50) {
            break;
         }

         l.add(m.m);
      }

      return l;
   }

   private static Constructor[] rankConstructors(Object[] original, Constructor[] candidates) {
      MethodRankHelper.RankableConstructor[] rc = new MethodRankHelper.RankableConstructor[candidates.length];
      Class[] ta = new Class[original.length];
      Class nullC = MethodRankHelper.NullObject.class;

      int i;
      for(i = 0; i < original.length; ++i) {
         ta[i] = original[i] == null ? nullC : original[i].getClass();
      }

      for(i = 0; i < candidates.length; ++i) {
         rc[i] = new MethodRankHelper.RankableConstructor(ta, candidates[i]);
      }

      Arrays.sort(rc);
      List<Constructor> l = new ArrayList();

      for(int index = 0; l.size() < 5 && index < rc.length && rc[index].score < 20; ++index) {
         l.add(rc[index].c);
      }

      return (Constructor[])l.toArray(new Constructor[l.size()]);
   }

   protected static Class boxVar(Class c) {
      if (Boolean.TYPE.equals(c)) {
         return Boolean.class;
      } else if (Character.TYPE.equals(c)) {
         return Character.class;
      } else if (Byte.TYPE.equals(c)) {
         return Byte.class;
      } else if (Double.TYPE.equals(c)) {
         return Double.class;
      } else if (Float.TYPE.equals(c)) {
         return Float.class;
      } else if (Integer.TYPE.equals(c)) {
         return Integer.class;
      } else if (Long.TYPE.equals(c)) {
         return Long.class;
      } else {
         return Short.TYPE.equals(c) ? Short.class : c;
      }
   }

   public static int delDistance(CharSequence s, CharSequence t) {
      if (s != null && t != null) {
         int n = s.length();
         int m = t.length();
         if (n == 0) {
            return m;
         } else if (m == 0) {
            return n;
         } else {
            int[][] vals = new int[3][n + 1];

            int i;
            for(i = 0; i <= n; ++i) {
               vals[1][i] = i * 10;
            }

            for(int j = 1; j <= m; ++j) {
               char t_j = t.charAt(j - 1);
               vals[0][0] = j * 10;

               for(i = 1; i <= n; ++i) {
                  char s_i = s.charAt(i - 1);
                  int cost;
                  if (Character.isLowerCase(s_i) ^ Character.isLowerCase(t_j)) {
                     cost = caselessCompare(s_i, t_j) ? 5 : 10;
                  } else {
                     cost = s_i == t_j ? 0 : 10;
                  }

                  vals[0][i] = Math.min(Math.min(vals[0][i - 1] + 10, vals[1][i] + 10), vals[1][i - 1] + cost);
                  if (i > 1 && j > 1) {
                     cost = Character.isLowerCase(s_i) ^ Character.isLowerCase(t.charAt(j - 2)) ? 5 : 0;
                     cost = Character.isLowerCase(s.charAt(i - 2)) ^ Character.isLowerCase(t_j) ? cost + 5 : cost;
                     if (caselessCompare(s_i, t.charAt(j - 2)) && caselessCompare(s.charAt(i - 2), t_j)) {
                        vals[0][i] = Math.min(vals[0][i], vals[2][i - 2] + 5 + cost);
                     }
                  }
               }

               int[] _d = vals[2];
               vals[2] = vals[1];
               vals[1] = vals[0];
               vals[0] = _d;
            }

            return vals[1][n];
         }
      } else {
         throw new IllegalArgumentException("Strings must not be null");
      }
   }

   private static boolean caselessCompare(char a, char b) {
      return Character.toLowerCase(a) == Character.toLowerCase(b);
   }

   public static int damerauLevenshteinDistance(Object[] s, Object[] t) {
      if (s != null && t != null) {
         int n = s.length;
         int m = t.length;
         if (n == 0) {
            return m;
         } else if (m == 0) {
            return n;
         } else {
            int[][] vals = new int[3][n + 1];

            int i;
            for(i = 0; i <= n; ++i) {
               vals[1][i] = i * 10;
            }

            for(int j = 1; j <= m; ++j) {
               Object t_j = t[j - 1];
               vals[0][0] = j * 10;

               for(i = 1; i <= n; ++i) {
                  int cost = s[i - 1].equals(t_j) ? 0 : 10;
                  vals[0][i] = Math.min(Math.min(vals[0][i - 1] + 10, vals[1][i] + 10), vals[1][i - 1] + cost);
                  if (i > 1 && j > 1 && s[i - 1].equals(t[j - 2]) && s[i - 2].equals(t_j)) {
                     vals[0][i] = Math.min(vals[0][i], vals[2][i - 2] + 5);
                  }
               }

               int[] _d = vals[2];
               vals[2] = vals[1];
               vals[1] = vals[0];
               vals[0] = _d;
            }

            return vals[1][n];
         }
      } else {
         throw new IllegalArgumentException("Arrays must not be null");
      }
   }

   private static class NullObject {
   }

   private static final class RankableField implements Comparable {
      final MetaProperty f;
      final Integer score;

      public RankableField(String name, MetaProperty mp) {
         this.f = mp;
         this.score = MethodRankHelper.delDistance(name, mp.getName());
      }

      public int compareTo(Object o) {
         MethodRankHelper.RankableField co = (MethodRankHelper.RankableField)o;
         return this.score.compareTo(co.score);
      }
   }

   private static final class RankableConstructor implements Comparable {
      final Constructor c;
      final Integer score;

      public RankableConstructor(Class[] argumentTypes, Constructor c) {
         this.c = c;
         Class[] cArgs = new Class[c.getParameterTypes().length];

         for(int i = 0; i < cArgs.length; ++i) {
            cArgs[i] = MethodRankHelper.boxVar(c.getParameterTypes()[i]);
         }

         this.score = MethodRankHelper.damerauLevenshteinDistance(argumentTypes, cArgs);
      }

      public int compareTo(Object o) {
         MethodRankHelper.RankableConstructor co = (MethodRankHelper.RankableConstructor)o;
         return this.score.compareTo(co.score);
      }
   }

   private static final class RankableMethod implements Comparable {
      final MetaMethod m;
      final Integer score;

      public RankableMethod(String name, Class[] argumentTypes, MetaMethod m2) {
         this.m = m2;
         int nameDist = MethodRankHelper.delDistance(name, m2.getName());
         Class[] mArgs = new Class[m2.getParameterTypes().length];

         int argDist;
         for(argDist = 0; argDist < mArgs.length; ++argDist) {
            mArgs[argDist] = MethodRankHelper.boxVar(m2.getParameterTypes()[argDist].getTheClass());
         }

         argDist = MethodRankHelper.damerauLevenshteinDistance(argumentTypes, mArgs);
         this.score = nameDist + argDist;
      }

      public int compareTo(Object o) {
         MethodRankHelper.RankableMethod mo = (MethodRankHelper.RankableMethod)o;
         return this.score.compareTo(mo.score);
      }
   }

   private static final class Pair<U, V> {
      private U u;
      private V v;

      public Pair(U u, V v) {
         this.u = u;
         this.v = v;
      }
   }
}
