package com.gzoltar.shaded.org.jacoco.core.internal.instr;

import com.gzoltar.shaded.org.jacoco.core.internal.data.CRC64;
import com.gzoltar.shaded.org.jacoco.core.internal.flow.ClassProbesAdapter;
import com.gzoltar.shaded.org.jacoco.core.runtime.IExecutionDataAccessorGenerator;
import com.gzoltar.shaded.org.objectweb.asm.ClassReader;

public final class ProbeArrayStrategyFactory {
   private ProbeArrayStrategyFactory() {
   }

   public static IProbeArrayStrategy createFor(ClassReader reader, IExecutionDataAccessorGenerator accessorGenerator) {
      String className = reader.getClassName();
      int version = getVersion(reader);
      long classId = CRC64.checksum(reader.b);
      boolean withFrames = version >= 50;
      if (isInterface(reader)) {
         ProbeCounter counter = getProbeCounter(reader);
         if (counter.getCount() == 0) {
            return new NoneProbeArrayStrategy();
         } else {
            return (IProbeArrayStrategy)(version >= 52 && counter.hasMethods() ? new InterfaceFieldProbeArrayStrategy(className, classId, counter.getCount(), accessorGenerator) : new LocalProbeArrayStrategy(className, classId, counter.getCount(), accessorGenerator));
         }
      } else {
         return new ClassFieldProbeArrayStrategy(className, classId, withFrames, accessorGenerator);
      }
   }

   private static boolean isInterface(ClassReader reader) {
      return (reader.getAccess() & 512) != 0;
   }

   private static int getVersion(ClassReader reader) {
      return reader.readShort(6);
   }

   private static ProbeCounter getProbeCounter(ClassReader reader) {
      ProbeCounter counter = new ProbeCounter();
      reader.accept(new ClassProbesAdapter(counter, false), 0);
      return counter;
   }
}
