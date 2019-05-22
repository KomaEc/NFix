package soot.toolkits.exceptions;

import com.google.common.cache.CacheBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.AnySubType;
import soot.FastHierarchy;
import soot.G;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.options.Options;

public final class ThrowableSet {
   private static final boolean INSTRUMENTING = false;
   private final Set<RefLikeType> exceptionsIncluded;
   private final Set<AnySubType> exceptionsExcluded;
   private Map<Object, ThrowableSet> memoizedAdds;

   private ThrowableSet getMemoizedAdds(Object key) {
      return this.memoizedAdds == null ? null : (ThrowableSet)this.memoizedAdds.get(key);
   }

   private void addToMemoizedAdds(Object key, ThrowableSet value) {
      if (this.memoizedAdds == null) {
         this.memoizedAdds = new HashMap();
      }

      this.memoizedAdds.put(key, value);
   }

   private ThrowableSet(Set<RefLikeType> include, Set<AnySubType> exclude) {
      this.exceptionsIncluded = getImmutable(include);
      this.exceptionsExcluded = getImmutable(exclude);
   }

   private static <T> Set<T> getImmutable(Set<T> in) {
      if (null != in && !in.isEmpty()) {
         return 1 == in.size() ? Collections.singleton(in.iterator().next()) : Collections.unmodifiableSet(in);
      } else {
         return Collections.emptySet();
      }
   }

   public ThrowableSet add(RefType e) throws ThrowableSet.AlreadyHasExclusionsException {
      if (this.exceptionsIncluded.contains(e)) {
         return this;
      } else {
         ThrowableSet result = this.getMemoizedAdds(e);
         if (result != null) {
            return result;
         } else {
            FastHierarchy hierarchy = Scene.v().getOrMakeFastHierarchy();
            Iterator var4 = this.exceptionsExcluded.iterator();

            RefType incumbentBase;
            do {
               if (!var4.hasNext()) {
                  if (!e.getSootClass().isPhantom()) {
                     var4 = this.exceptionsIncluded.iterator();

                     while(var4.hasNext()) {
                        RefLikeType incumbent = (RefLikeType)var4.next();
                        if (incumbent instanceof AnySubType) {
                           incumbentBase = ((AnySubType)incumbent).getBase();
                           if (hierarchy.canStoreType(e, incumbentBase)) {
                              this.addToMemoizedAdds(e, this);
                              return this;
                           }
                        } else if (!(incumbent instanceof RefType)) {
                           throw new IllegalStateException("ThrowableSet.add(RefType): Set element " + incumbent.toString() + " is neither a RefType nor an AnySubType.");
                        }
                     }
                  }

                  Set<RefLikeType> resultSet = new HashSet(this.exceptionsIncluded);
                  resultSet.add(e);
                  result = ThrowableSet.Manager.v().registerSetIfNew(resultSet, this.exceptionsExcluded);
                  this.addToMemoizedAdds(e, result);
                  return result;
               }

               AnySubType excludedType = (AnySubType)var4.next();
               incumbentBase = excludedType.getBase();
            } while((!e.getSootClass().isPhantom() || !incumbentBase.equals(e)) && (e.getSootClass().isPhantom() || !hierarchy.canStoreType(e, incumbentBase)));

            throw new ThrowableSet.AlreadyHasExclusionsException("ThrowableSet.add(RefType): adding" + e.toString() + " to the set [ " + this.toString() + "] where " + incumbentBase.toString() + " is excluded.");
         }
      }
   }

   public ThrowableSet add(AnySubType e) throws ThrowableSet.AlreadyHasExclusionsException {
      ThrowableSet result = this.getMemoizedAdds(e);
      if (result != null) {
         return result;
      } else {
         FastHierarchy hierarchy = Scene.v().getOrMakeFastHierarchy();
         RefType newBase = e.getBase();
         Iterator var5 = this.exceptionsExcluded.iterator();

         RefType exclusionBase;
         boolean isExcluded;
         do {
            if (!var5.hasNext()) {
               if (this.exceptionsIncluded.contains(e)) {
                  return this;
               }

               int changes = 0;
               boolean addNewException = true;
               Set<RefLikeType> resultSet = new HashSet();
               Iterator var14 = this.exceptionsIncluded.iterator();

               while(var14.hasNext()) {
                  RefLikeType incumbent = (RefLikeType)var14.next();
                  if (incumbent instanceof RefType) {
                     if (hierarchy.canStoreType(incumbent, newBase)) {
                        ++changes;
                     } else {
                        resultSet.add(incumbent);
                     }
                  } else {
                     if (!(incumbent instanceof AnySubType)) {
                        throw new IllegalStateException("ThrowableSet.add(AnySubType): Set element " + incumbent.toString() + " is neither a RefType nor an AnySubType.");
                     }

                     RefType incumbentBase = ((AnySubType)incumbent).getBase();
                     if (newBase.getSootClass().isPhantom()) {
                        if (!incumbentBase.equals(newBase)) {
                           resultSet.add(incumbent);
                        }
                     } else if (hierarchy.canStoreType(newBase, incumbentBase)) {
                        addNewException = false;
                        resultSet.add(incumbent);
                     } else if (hierarchy.canStoreType(incumbentBase, newBase)) {
                        ++changes;
                     } else {
                        resultSet.add(incumbent);
                     }
                  }
               }

               if (addNewException) {
                  resultSet.add(e);
                  ++changes;
               }

               if (changes > 0) {
                  result = ThrowableSet.Manager.v().registerSetIfNew(resultSet, this.exceptionsExcluded);
               } else {
                  result = this;
               }

               this.addToMemoizedAdds(e, result);
               return result;
            }

            AnySubType excludedType = (AnySubType)var5.next();
            exclusionBase = excludedType.getBase();
            isExcluded = exclusionBase.getSootClass().isPhantom() && exclusionBase.equals(newBase);
            isExcluded |= !exclusionBase.getSootClass().isPhantom() && (hierarchy.canStoreType(newBase, exclusionBase) || hierarchy.canStoreType(exclusionBase, newBase));
         } while(!isExcluded);

         throw new ThrowableSet.AlreadyHasExclusionsException("ThrowableSet.add(" + e.toString() + ") to the set [ " + this.toString() + "] where " + exclusionBase.toString() + " is excluded.");
      }
   }

   public ThrowableSet add(ThrowableSet s) throws ThrowableSet.AlreadyHasExclusionsException {
      if (this.exceptionsExcluded.size() <= 0 && s.exceptionsExcluded.size() <= 0) {
         ThrowableSet result = this.getMemoizedAdds(s);
         if (result == null) {
            result = this.add(s.exceptionsIncluded);
            this.addToMemoizedAdds(s, result);
         }

         return result;
      } else {
         throw new ThrowableSet.AlreadyHasExclusionsException("ThrowableSet.Add(ThrowableSet): attempt to add to [" + this.toString() + "] after removals recorded.");
      }
   }

   private ThrowableSet add(Set<RefLikeType> addedExceptions) {
      Set<RefLikeType> resultSet = new HashSet(this.exceptionsIncluded);
      int changes = 0;
      FastHierarchy hierarchy = Scene.v().getOrMakeFastHierarchy();
      Iterator result = addedExceptions.iterator();

      while(true) {
         RefLikeType newType;
         do {
            if (!result.hasNext()) {
               result = null;
               ThrowableSet result;
               if (changes > 0) {
                  result = ThrowableSet.Manager.v().registerSetIfNew(resultSet, this.exceptionsExcluded);
               } else {
                  result = this;
               }

               return result;
            }

            newType = (RefLikeType)result.next();
         } while(resultSet.contains(newType));

         boolean addNewType = true;
         if (newType instanceof RefType) {
            Iterator var13 = resultSet.iterator();

            while(var13.hasNext()) {
               RefLikeType incumbentType = (RefLikeType)var13.next();
               if (incumbentType instanceof RefType) {
                  if (newType == incumbentType) {
                     throw new IllegalStateException("ThrowableSet.add(Set): resultSet.contains() failed to screen duplicate RefType " + newType);
                  }
               } else {
                  if (!(incumbentType instanceof AnySubType)) {
                     throw new IllegalStateException("ThrowableSet.add(Set): incumbent Set element " + incumbentType + " is neither a RefType nor an AnySubType.");
                  }

                  RefType incumbentBase = ((AnySubType)incumbentType).getBase();
                  if (hierarchy.canStoreType(newType, incumbentBase)) {
                     addNewType = false;
                  }
               }
            }
         } else {
            if (!(newType instanceof AnySubType)) {
               throw new IllegalArgumentException("ThrowableSet.add(Set): new Set element " + newType + " is neither a RefType nor an AnySubType.");
            }

            RefType newBase = ((AnySubType)newType).getBase();
            Iterator j = resultSet.iterator();

            while(j.hasNext()) {
               RefLikeType incumbentType = (RefLikeType)j.next();
               RefType incumbentBase;
               if (incumbentType instanceof RefType) {
                  incumbentBase = (RefType)incumbentType;
                  if (hierarchy.canStoreType(incumbentBase, newBase)) {
                     j.remove();
                     ++changes;
                  }
               } else {
                  if (!(incumbentType instanceof AnySubType)) {
                     throw new IllegalStateException("ThrowableSet.add(Set): old Set element " + incumbentType + " is neither a RefType nor an AnySubType.");
                  }

                  incumbentBase = ((AnySubType)incumbentType).getBase();
                  if (newBase == incumbentBase) {
                     throw new IllegalStateException("ThrowableSet.add(Set): resultSet.contains() failed to screen duplicate AnySubType " + newBase);
                  }

                  if (hierarchy.canStoreType(incumbentBase, newBase)) {
                     j.remove();
                     ++changes;
                  } else if (hierarchy.canStoreType(newBase, incumbentBase)) {
                     addNewType = false;
                  }
               }
            }
         }

         if (addNewType) {
            ++changes;
            resultSet.add(newType);
         }
      }
   }

   private ThrowableSet remove(Set<RefLikeType> removedExceptions) {
      if (removedExceptions.isEmpty()) {
         return this;
      } else {
         int changes = 0;
         Set<RefLikeType> resultSet = new HashSet(this.exceptionsIncluded);
         Iterator result = removedExceptions.iterator();

         while(result.hasNext()) {
            RefLikeType tp = (RefLikeType)result.next();
            if (tp instanceof RefType && resultSet.remove(tp)) {
               ++changes;
            }
         }

         result = null;
         ThrowableSet result;
         if (changes > 0) {
            result = ThrowableSet.Manager.v().registerSetIfNew(resultSet, this.exceptionsExcluded);
         } else {
            result = this;
         }

         return result;
      }
   }

   public ThrowableSet remove(ThrowableSet s) {
      if (this.exceptionsExcluded.size() <= 0 && s.exceptionsExcluded.size() <= 0) {
         return this.remove(s.exceptionsIncluded);
      } else {
         throw new ThrowableSet.AlreadyHasExclusionsException("ThrowableSet.Add(ThrowableSet): attempt to add to [" + this.toString() + "] after removals recorded.");
      }
   }

   public boolean catchableAs(RefType catcher) {
      FastHierarchy h = Scene.v().getOrMakeFastHierarchy();
      Iterator var3;
      if (this.exceptionsExcluded.size() > 0) {
         var3 = this.exceptionsExcluded.iterator();

         while(var3.hasNext()) {
            AnySubType exclusion = (AnySubType)var3.next();
            if (catcher.getSootClass().isPhantom()) {
               if (exclusion.getBase().equals(catcher)) {
                  return false;
               }
            } else if (h.canStoreType(catcher, exclusion.getBase())) {
               return false;
            }
         }
      }

      if (this.exceptionsIncluded.contains(catcher)) {
         return true;
      } else {
         var3 = this.exceptionsIncluded.iterator();

         while(true) {
            while(var3.hasNext()) {
               RefLikeType thrownType = (RefLikeType)var3.next();
               if (!(thrownType instanceof RefType)) {
                  RefType thrownBase = ((AnySubType)thrownType).getBase();
                  if (catcher.getSootClass().isPhantom()) {
                     if (thrownBase.equals(catcher) || thrownBase.getClassName().equals("java.lang.Throwable")) {
                        return true;
                     }
                  } else if (h.canStoreType(thrownBase, catcher) || h.canStoreType(catcher, thrownBase)) {
                     return true;
                  }
               } else {
                  if (thrownType == catcher) {
                     throw new IllegalStateException("ThrowableSet.catchableAs(RefType): exceptions.contains() failed to match contained RefType " + catcher);
                  }

                  if (!catcher.getSootClass().isPhantom() && h.canStoreType(thrownType, catcher)) {
                     return true;
                  }
               }
            }

            return false;
         }
      }
   }

   public ThrowableSet.Pair whichCatchableAs(RefType catcher) {
      FastHierarchy h = Scene.v().getOrMakeFastHierarchy();
      Set<RefLikeType> caughtIncluded = null;
      Set<AnySubType> caughtExcluded = null;
      Set<RefLikeType> uncaughtIncluded = null;
      Set<AnySubType> uncaughtExcluded = null;
      Iterator var7 = this.exceptionsExcluded.iterator();

      RefType base;
      while(var7.hasNext()) {
         AnySubType exclusion = (AnySubType)var7.next();
         base = exclusion.getBase();
         if (catcher.getSootClass().isPhantom() && base.equals(catcher)) {
            return new ThrowableSet.Pair(ThrowableSet.Manager.v().EMPTY, this);
         }

         if (h.canStoreType(catcher, base)) {
            return new ThrowableSet.Pair(ThrowableSet.Manager.v().EMPTY, this);
         }

         if (h.canStoreType(base, catcher)) {
            caughtExcluded = this.addExceptionToSet(exclusion, caughtExcluded);
         } else {
            uncaughtExcluded = this.addExceptionToSet(exclusion, uncaughtExcluded);
         }
      }

      var7 = this.exceptionsIncluded.iterator();

      while(var7.hasNext()) {
         RefLikeType inclusion = (RefLikeType)var7.next();
         if (inclusion instanceof RefType) {
            if (catcher.getSootClass().isPhantom()) {
               if (inclusion.equals(catcher)) {
                  caughtIncluded = this.addExceptionToSet(inclusion, caughtIncluded);
               } else {
                  uncaughtIncluded = this.addExceptionToSet(inclusion, uncaughtIncluded);
               }
            } else if (h.canStoreType(inclusion, catcher)) {
               caughtIncluded = this.addExceptionToSet(inclusion, caughtIncluded);
            } else {
               uncaughtIncluded = this.addExceptionToSet(inclusion, uncaughtIncluded);
            }
         } else {
            base = ((AnySubType)inclusion).getBase();
            if (catcher.getSootClass().isPhantom()) {
               if (base.equals(catcher)) {
                  caughtIncluded = this.addExceptionToSet(inclusion, caughtIncluded);
               } else {
                  if (base.getClassName().equals("java.lang.Throwable")) {
                     caughtIncluded = this.addExceptionToSet(catcher, caughtIncluded);
                  }

                  uncaughtIncluded = this.addExceptionToSet(inclusion, uncaughtIncluded);
               }
            } else if (h.canStoreType(base, catcher)) {
               caughtIncluded = this.addExceptionToSet(inclusion, caughtIncluded);
            } else if (h.canStoreType(catcher, base)) {
               uncaughtIncluded = this.addExceptionToSet(inclusion, uncaughtIncluded);
               uncaughtExcluded = this.addExceptionToSet(AnySubType.v(catcher), uncaughtExcluded);
               caughtIncluded = this.addExceptionToSet(AnySubType.v(catcher), caughtIncluded);
            } else {
               uncaughtIncluded = this.addExceptionToSet(inclusion, uncaughtIncluded);
            }
         }
      }

      ThrowableSet caughtSet = ThrowableSet.Manager.v().registerSetIfNew(caughtIncluded, caughtExcluded);
      ThrowableSet uncaughtSet = ThrowableSet.Manager.v().registerSetIfNew(uncaughtIncluded, uncaughtExcluded);
      return new ThrowableSet.Pair(caughtSet, uncaughtSet);
   }

   private <T> Set<T> addExceptionToSet(T e, Set<T> set) {
      if (set == null) {
         set = new HashSet();
      }

      ((Set)set).add(e);
      return (Set)set;
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer(this.toBriefString());
      buffer.append(":\n  ");
      Iterator var2 = this.exceptionsIncluded.iterator();

      RefLikeType ee;
      while(var2.hasNext()) {
         ee = (RefLikeType)var2.next();
         buffer.append('+');
         buffer.append(ee == null ? "null" : ee.toString());
      }

      var2 = this.exceptionsExcluded.iterator();

      while(var2.hasNext()) {
         ee = (RefLikeType)var2.next();
         buffer.append('-');
         buffer.append(ee.toString());
      }

      return buffer.toString();
   }

   public String toBriefString() {
      return super.toString();
   }

   private static <T extends RefLikeType> Iterator<T> sortedThrowableIterator(Collection<T> coll) {
      if (coll.size() <= 1) {
         return coll.iterator();
      } else {
         T[] array = (RefLikeType[])((RefLikeType[])coll.toArray(new RefLikeType[coll.size()]));
         Arrays.sort(array, new ThrowableSet.ThrowableComparator());
         return Arrays.asList(array).iterator();
      }
   }

   public String toAbbreviatedString() {
      return this.toAbbreviatedString(this.exceptionsIncluded, '+') + this.toAbbreviatedString(this.exceptionsExcluded, '-');
   }

   private String toAbbreviatedString(Set<? extends RefLikeType> s, char connector) {
      String JAVA_LANG = "java.lang.";
      String EXCEPTION = "Exception";
      Collection<RefLikeType> vmErrorThrowables = ThrowableSet.Manager.v().VM_ERRORS.exceptionsIncluded;
      boolean containsAllVmErrors = s.containsAll(vmErrorThrowables);
      StringBuffer buf = new StringBuffer();
      if (containsAllVmErrors) {
         buf.append(connector);
         buf.append("vmErrors");
      }

      Iterator it = sortedThrowableIterator(s);

      while(true) {
         RefLikeType reflikeType;
         RefType baseType;
         while(true) {
            if (!it.hasNext()) {
               return buf.toString();
            }

            reflikeType = (RefLikeType)it.next();
            baseType = null;
            if (reflikeType instanceof RefType) {
               baseType = (RefType)reflikeType;
               if (containsAllVmErrors && vmErrorThrowables.contains(baseType)) {
                  continue;
               }

               buf.append(connector);
               break;
            }

            if (!(reflikeType instanceof AnySubType)) {
               throw new RuntimeException("Unsupported type " + reflikeType.getClass().getName());
            }

            buf.append(connector);
            buf.append('(');
            baseType = ((AnySubType)reflikeType).getBase();
            break;
         }

         String typeName = baseType.toString();
         int start = 0;
         int end = typeName.length();
         if (typeName.startsWith("java.lang.")) {
            start += "java.lang.".length();
         }

         if (typeName.endsWith("Exception")) {
            end -= "Exception".length();
         }

         buf.append(typeName, start, end);
         if (reflikeType instanceof AnySubType) {
            buf.append(')');
         }
      }
   }

   Collection<RefLikeType> typesIncluded() {
      return this.exceptionsIncluded;
   }

   Collection<AnySubType> typesExcluded() {
      return this.exceptionsExcluded;
   }

   Map<Object, ThrowableSet> getMemoizedAdds() {
      return this.memoizedAdds == null ? Collections.emptyMap() : Collections.unmodifiableMap(this.memoizedAdds);
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.exceptionsIncluded.hashCode();
      result = 31 * result + this.exceptionsExcluded.hashCode();
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         ThrowableSet other = (ThrowableSet)obj;
         return this.exceptionsIncluded.equals(other.exceptionsIncluded) && this.exceptionsExcluded.equals(other.exceptionsExcluded);
      }
   }

   // $FF: synthetic method
   ThrowableSet(Set x0, Set x1, Object x2) {
      this(x0, x1);
   }

   private static class ThrowableComparator<T extends RefLikeType> implements Comparator<T> {
      private ThrowableComparator() {
      }

      private static RefType baseType(RefLikeType o) {
         return o instanceof AnySubType ? ((AnySubType)o).getBase() : (RefType)o;
      }

      public int compare(T o1, T o2) {
         RefType t1 = baseType(o1);
         RefType t2 = baseType(o2);
         if (t1.equals(t2)) {
            if (o1 instanceof AnySubType) {
               return o2 instanceof AnySubType ? 0 : -1;
            } else {
               return o2 instanceof AnySubType ? 1 : 0;
            }
         } else {
            return t1.toString().compareTo(t2.toString());
         }
      }

      // $FF: synthetic method
      ThrowableComparator(Object x0) {
         this();
      }
   }

   public static class Pair {
      private ThrowableSet caught;
      private ThrowableSet uncaught;

      protected Pair(ThrowableSet caught, ThrowableSet uncaught) {
         this.caught = caught;
         this.uncaught = uncaught;
      }

      public ThrowableSet getCaught() {
         return this.caught;
      }

      public ThrowableSet getUncaught() {
         return this.uncaught;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof ThrowableSet.Pair)) {
            return false;
         } else {
            ThrowableSet.Pair tsp = (ThrowableSet.Pair)o;
            return this.caught.equals(tsp.caught) && this.uncaught.equals(tsp.uncaught);
         }
      }

      public int hashCode() {
         int result = 31;
         int result = 37 * result + this.caught.hashCode();
         result = 37 * result + this.uncaught.hashCode();
         return result;
      }
   }

   public static class AlreadyHasExclusionsException extends IllegalStateException {
      public AlreadyHasExclusionsException(String s) {
         super(s);
      }
   }

   public static class Manager {
      private final Map<ThrowableSet, ThrowableSet> registry = CacheBuilder.newBuilder().weakValues().build().asMap();
      public final ThrowableSet EMPTY = this.registerSetIfNew((Set)null, (Set)null);
      final ThrowableSet ALL_THROWABLES;
      final ThrowableSet VM_ERRORS;
      final ThrowableSet RESOLVE_CLASS_ERRORS;
      final ThrowableSet RESOLVE_FIELD_ERRORS;
      final ThrowableSet RESOLVE_METHOD_ERRORS;
      final ThrowableSet INITIALIZATION_ERRORS;
      public final RefType RUNTIME_EXCEPTION = Scene.v().getRefType("java.lang.RuntimeException");
      public final RefType ARITHMETIC_EXCEPTION = Scene.v().getRefType("java.lang.ArithmeticException");
      public final RefType ARRAY_STORE_EXCEPTION = Scene.v().getRefType("java.lang.ArrayStoreException");
      public final RefType CLASS_CAST_EXCEPTION = Scene.v().getRefType("java.lang.ClassCastException");
      public final RefType ILLEGAL_MONITOR_STATE_EXCEPTION = Scene.v().getRefType("java.lang.IllegalMonitorStateException");
      public final RefType INDEX_OUT_OF_BOUNDS_EXCEPTION = Scene.v().getRefType("java.lang.IndexOutOfBoundsException");
      public final RefType ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION = Scene.v().getRefType("java.lang.ArrayIndexOutOfBoundsException");
      public final RefType NEGATIVE_ARRAY_SIZE_EXCEPTION = Scene.v().getRefType("java.lang.NegativeArraySizeException");
      public final RefType NULL_POINTER_EXCEPTION = Scene.v().getRefType("java.lang.NullPointerException");
      public final RefType INSTANTIATION_ERROR = Scene.v().getRefType("java.lang.InstantiationError");
      private int addsOfRefType = 0;
      private int addsOfAnySubType = 0;
      private int addsOfSet = 0;
      private int addsInclusionFromMap = 0;
      private int addsInclusionFromMemo = 0;
      private int addsInclusionFromSearch = 0;
      private int addsInclusionInterrupted = 0;
      private int addsExclusionWithSearch = 0;
      private int addsExclusionWithoutSearch = 0;
      private int removesOfAnySubType = 0;
      private final int removesFromMap = 0;
      private final int removesFromMemo = 0;
      private int removesFromSearch = 0;
      private int registrationCalls = 0;
      private int catchableAsQueries = 0;
      private int catchableAsFromMap = 0;
      private int catchableAsFromSearch = 0;

      public Manager(Singletons.Global g) {
         Set<RefLikeType> allThrowablesSet = new HashSet();
         allThrowablesSet.add(AnySubType.v(Scene.v().getRefType("java.lang.Throwable")));
         this.ALL_THROWABLES = this.registerSetIfNew(allThrowablesSet, (Set)null);
         Set<RefLikeType> vmErrorSet = new HashSet();
         vmErrorSet.add(Scene.v().getRefType("java.lang.InternalError"));
         vmErrorSet.add(Scene.v().getRefType("java.lang.OutOfMemoryError"));
         vmErrorSet.add(Scene.v().getRefType("java.lang.StackOverflowError"));
         vmErrorSet.add(Scene.v().getRefType("java.lang.UnknownError"));
         vmErrorSet.add(Scene.v().getRefType("java.lang.ThreadDeath"));
         this.VM_ERRORS = this.registerSetIfNew(vmErrorSet, (Set)null);
         Set<RefLikeType> resolveClassErrorSet = new HashSet();
         resolveClassErrorSet.add(Scene.v().getRefType("java.lang.ClassCircularityError"));
         if (!Options.v().j2me()) {
            resolveClassErrorSet.add(AnySubType.v(Scene.v().getRefType("java.lang.ClassFormatError")));
         }

         resolveClassErrorSet.add(Scene.v().getRefType("java.lang.IllegalAccessError"));
         resolveClassErrorSet.add(Scene.v().getRefType("java.lang.IncompatibleClassChangeError"));
         resolveClassErrorSet.add(Scene.v().getRefType("java.lang.LinkageError"));
         resolveClassErrorSet.add(Scene.v().getRefType("java.lang.NoClassDefFoundError"));
         resolveClassErrorSet.add(Scene.v().getRefType("java.lang.VerifyError"));
         this.RESOLVE_CLASS_ERRORS = this.registerSetIfNew(resolveClassErrorSet, (Set)null);
         Set<RefLikeType> resolveFieldErrorSet = new HashSet(resolveClassErrorSet);
         resolveFieldErrorSet.add(Scene.v().getRefType("java.lang.NoSuchFieldError"));
         this.RESOLVE_FIELD_ERRORS = this.registerSetIfNew(resolveFieldErrorSet, (Set)null);
         Set<RefLikeType> resolveMethodErrorSet = new HashSet(resolveClassErrorSet);
         resolveMethodErrorSet.add(Scene.v().getRefType("java.lang.AbstractMethodError"));
         resolveMethodErrorSet.add(Scene.v().getRefType("java.lang.NoSuchMethodError"));
         resolveMethodErrorSet.add(Scene.v().getRefType("java.lang.UnsatisfiedLinkError"));
         this.RESOLVE_METHOD_ERRORS = this.registerSetIfNew(resolveMethodErrorSet, (Set)null);
         Set<RefLikeType> initializationErrorSet = new HashSet();
         initializationErrorSet.add(AnySubType.v(Scene.v().getRefType("java.lang.Error")));
         this.INITIALIZATION_ERRORS = this.registerSetIfNew(initializationErrorSet, (Set)null);
      }

      public static ThrowableSet.Manager v() {
         return G.v().soot_toolkits_exceptions_ThrowableSet_Manager();
      }

      private ThrowableSet registerSetIfNew(Set<RefLikeType> include, Set<AnySubType> exclude) {
         ThrowableSet result = new ThrowableSet(include, exclude);
         ThrowableSet ref = (ThrowableSet)this.registry.get(result);
         if (null != ref) {
            return ref;
         } else {
            this.registry.put(result, result);
            return result;
         }
      }

      public String reportInstrumentation() {
         int setCount = this.registry.size();
         StringBuffer buf = (new StringBuffer("registeredSets: ")).append(setCount).append("\naddsOfRefType: ").append(this.addsOfRefType).append("\naddsOfAnySubType: ").append(this.addsOfAnySubType).append("\naddsOfSet: ").append(this.addsOfSet).append("\naddsInclusionFromMap: ").append(this.addsInclusionFromMap).append("\naddsInclusionFromMemo: ").append(this.addsInclusionFromMemo).append("\naddsInclusionFromSearch: ").append(this.addsInclusionFromSearch).append("\naddsInclusionInterrupted: ").append(this.addsInclusionInterrupted).append("\naddsExclusionWithoutSearch: ").append(this.addsExclusionWithoutSearch).append("\naddsExclusionWithSearch: ").append(this.addsExclusionWithSearch).append("\nremovesOfAnySubType: ").append(this.removesOfAnySubType).append("\nremovesFromMap: ").append(0).append("\nremovesFromMemo: ").append(0).append("\nremovesFromSearch: ").append(this.removesFromSearch).append("\nregistrationCalls: ").append(this.registrationCalls).append("\ncatchableAsQueries: ").append(this.catchableAsQueries).append("\ncatchableAsFromMap: ").append(this.catchableAsFromMap).append("\ncatchableAsFromSearch: ").append(this.catchableAsFromSearch).append('\n');
         return buf.toString();
      }

      Set<ThrowableSet> getThrowableSets() {
         return this.registry.keySet();
      }
   }
}
