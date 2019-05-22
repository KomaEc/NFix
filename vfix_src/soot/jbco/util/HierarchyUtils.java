package soot.jbco.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import soot.Hierarchy;
import soot.Scene;
import soot.SootClass;

public final class HierarchyUtils {
   private HierarchyUtils() {
      throw new IllegalAccessError();
   }

   public static List<SootClass> getAllInterfacesOf(SootClass sc) {
      Hierarchy hierarchy = Scene.v().getActiveHierarchy();
      Stream<SootClass> superClassInterfaces = sc.isInterface() ? Stream.empty() : hierarchy.getSuperclassesOf(sc).stream().map(HierarchyUtils::getAllInterfacesOf).flatMap(Collection::stream);
      Stream<SootClass> directInterfaces = Stream.concat(sc.getInterfaces().stream(), sc.getInterfaces().stream().map(HierarchyUtils::getAllInterfacesOf).flatMap(Collection::stream));
      return (List)Stream.concat(superClassInterfaces, directInterfaces).collect(Collectors.toList());
   }
}
