package sun.pitest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public final class CodeCoverageStore {
   private static final int CLASS_HIT_INDEX = 0;
   public static final String CLASS_NAME = CodeCoverageStore.class.getName().replace('.', '/');
   public static final String PROBE_METHOD_NAME = "visitProbes";
   private static InvokeReceiver invokeQueue;
   private static int classId = 0;
   private static final Map<Integer, boolean[]> CLASS_HITS = new ConcurrentHashMap();

   public static void init(InvokeReceiver invokeQueue) {
      CodeCoverageStore.invokeQueue = invokeQueue;
   }

   private CodeCoverageStore() {
   }

   public static void visitSingleProbe(int classId, int probe) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;
      bs[probe + 1] = true;
   }

   public static void visitProbes(int classId, int offset, boolean[] probes) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;

      for(int i = 0; i != probes.length; ++i) {
         if (probes[i]) {
            bs[i + offset + 1] = true;
         }
      }

   }

   public static void visitProbes(int classId, int offset, boolean p0) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;
      if (p0) {
         bs[offset + 1] = true;
      }

   }

   public static void visitProbes(int classId, int offset, boolean p0, boolean p1) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;
      if (p0) {
         bs[offset + 1] = true;
      }

      if (p1) {
         bs[offset + 2] = true;
      }

   }

   public static void visitProbes(int classId, int offset, boolean p0, boolean p1, boolean p2) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;
      if (p0) {
         bs[offset + 1] = true;
      }

      if (p1) {
         bs[offset + 2] = true;
      }

      if (p2) {
         bs[offset + 3] = true;
      }

   }

   public static void visitProbes(int classId, int offset, boolean p0, boolean p1, boolean p2, boolean p3) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;
      if (p0) {
         bs[offset + 1] = true;
      }

      if (p1) {
         bs[offset + 2] = true;
      }

      if (p2) {
         bs[offset + 3] = true;
      }

      if (p3) {
         bs[offset + 4] = true;
      }

   }

   public static void visitProbes(int classId, int offset, boolean p0, boolean p1, boolean p2, boolean p3, boolean p4) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;
      if (p0) {
         bs[offset + 1] = true;
      }

      if (p1) {
         bs[offset + 2] = true;
      }

      if (p2) {
         bs[offset + 3] = true;
      }

      if (p3) {
         bs[offset + 4] = true;
      }

      if (p4) {
         bs[offset + 5] = true;
      }

   }

   public static void visitProbes(int classId, int offset, boolean p0, boolean p1, boolean p2, boolean p3, boolean p4, boolean p5) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;
      if (p0) {
         bs[offset + 1] = true;
      }

      if (p1) {
         bs[offset + 2] = true;
      }

      if (p2) {
         bs[offset + 3] = true;
      }

      if (p3) {
         bs[offset + 4] = true;
      }

      if (p4) {
         bs[offset + 5] = true;
      }

      if (p5) {
         bs[offset + 6] = true;
      }

   }

   public static void visitProbes(int classId, int offset, boolean p0, boolean p1, boolean p2, boolean p3, boolean p4, boolean p5, boolean p6) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;
      if (p0) {
         bs[offset + 1] = true;
      }

      if (p1) {
         bs[offset + 2] = true;
      }

      if (p2) {
         bs[offset + 3] = true;
      }

      if (p3) {
         bs[offset + 4] = true;
      }

      if (p4) {
         bs[offset + 5] = true;
      }

      if (p5) {
         bs[offset + 6] = true;
      }

      if (p6) {
         bs[offset + 7] = true;
      }

   }

   public static void visitProbes(int classId, int offset, boolean p0, boolean p1, boolean p2, boolean p3, boolean p4, boolean p5, boolean p6, boolean p7) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;
      if (p0) {
         bs[offset + 1] = true;
      }

      if (p1) {
         bs[offset + 2] = true;
      }

      if (p2) {
         bs[offset + 3] = true;
      }

      if (p3) {
         bs[offset + 4] = true;
      }

      if (p4) {
         bs[offset + 5] = true;
      }

      if (p5) {
         bs[offset + 6] = true;
      }

      if (p6) {
         bs[offset + 7] = true;
      }

      if (p7) {
         bs[offset + 8] = true;
      }

   }

   public static void visitProbes(int classId, int offset, boolean p0, boolean p1, boolean p2, boolean p3, boolean p4, boolean p5, boolean p6, boolean p7, boolean p8) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;
      if (p0) {
         bs[offset + 1] = true;
      }

      if (p1) {
         bs[offset + 2] = true;
      }

      if (p2) {
         bs[offset + 3] = true;
      }

      if (p3) {
         bs[offset + 4] = true;
      }

      if (p4) {
         bs[offset + 5] = true;
      }

      if (p5) {
         bs[offset + 6] = true;
      }

      if (p6) {
         bs[offset + 7] = true;
      }

      if (p7) {
         bs[offset + 8] = true;
      }

      if (p8) {
         bs[offset + 9] = true;
      }

   }

   public static void visitProbes(int classId, int offset, boolean p0, boolean p1, boolean p2, boolean p3, boolean p4, boolean p5, boolean p6, boolean p7, boolean p8, boolean p9) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;
      if (p0) {
         bs[offset + 1] = true;
      }

      if (p1) {
         bs[offset + 2] = true;
      }

      if (p2) {
         bs[offset + 3] = true;
      }

      if (p3) {
         bs[offset + 4] = true;
      }

      if (p4) {
         bs[offset + 5] = true;
      }

      if (p5) {
         bs[offset + 6] = true;
      }

      if (p6) {
         bs[offset + 7] = true;
      }

      if (p7) {
         bs[offset + 8] = true;
      }

      if (p8) {
         bs[offset + 9] = true;
      }

      if (p9) {
         bs[offset + 10] = true;
      }

   }

   public static void visitProbes(int classId, int offset, boolean p0, boolean p1, boolean p2, boolean p3, boolean p4, boolean p5, boolean p6, boolean p7, boolean p8, boolean p9, boolean p10) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;
      if (p0) {
         bs[offset + 1] = true;
      }

      if (p1) {
         bs[offset + 2] = true;
      }

      if (p2) {
         bs[offset + 3] = true;
      }

      if (p3) {
         bs[offset + 4] = true;
      }

      if (p4) {
         bs[offset + 5] = true;
      }

      if (p5) {
         bs[offset + 6] = true;
      }

      if (p6) {
         bs[offset + 7] = true;
      }

      if (p7) {
         bs[offset + 8] = true;
      }

      if (p8) {
         bs[offset + 9] = true;
      }

      if (p9) {
         bs[offset + 10] = true;
      }

      if (p10) {
         bs[offset + 11] = true;
      }

   }

   public static void visitProbes(int classId, int offset, boolean p0, boolean p1, boolean p2, boolean p3, boolean p4, boolean p5, boolean p6, boolean p7, boolean p8, boolean p9, boolean p10, boolean p11) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;
      if (p0) {
         bs[offset + 1] = true;
      }

      if (p1) {
         bs[offset + 2] = true;
      }

      if (p2) {
         bs[offset + 3] = true;
      }

      if (p3) {
         bs[offset + 4] = true;
      }

      if (p4) {
         bs[offset + 5] = true;
      }

      if (p5) {
         bs[offset + 6] = true;
      }

      if (p6) {
         bs[offset + 7] = true;
      }

      if (p7) {
         bs[offset + 8] = true;
      }

      if (p8) {
         bs[offset + 9] = true;
      }

      if (p9) {
         bs[offset + 10] = true;
      }

      if (p10) {
         bs[offset + 11] = true;
      }

      if (p11) {
         bs[offset + 12] = true;
      }

   }

   public static void visitProbes(int classId, int offset, boolean p0, boolean p1, boolean p2, boolean p3, boolean p4, boolean p5, boolean p6, boolean p7, boolean p8, boolean p9, boolean p10, boolean p11, boolean p12) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;
      if (p0) {
         bs[offset + 1] = true;
      }

      if (p1) {
         bs[offset + 2] = true;
      }

      if (p2) {
         bs[offset + 3] = true;
      }

      if (p3) {
         bs[offset + 4] = true;
      }

      if (p4) {
         bs[offset + 5] = true;
      }

      if (p5) {
         bs[offset + 6] = true;
      }

      if (p6) {
         bs[offset + 7] = true;
      }

      if (p7) {
         bs[offset + 8] = true;
      }

      if (p8) {
         bs[offset + 9] = true;
      }

      if (p9) {
         bs[offset + 10] = true;
      }

      if (p10) {
         bs[offset + 11] = true;
      }

      if (p11) {
         bs[offset + 12] = true;
      }

      if (p12) {
         bs[offset + 13] = true;
      }

   }

   public static void visitProbes(int classId, int offset, boolean p0, boolean p1, boolean p2, boolean p3, boolean p4, boolean p5, boolean p6, boolean p7, boolean p8, boolean p9, boolean p10, boolean p11, boolean p12, boolean p13) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;
      if (p0) {
         bs[offset + 1] = true;
      }

      if (p1) {
         bs[offset + 2] = true;
      }

      if (p2) {
         bs[offset + 3] = true;
      }

      if (p3) {
         bs[offset + 4] = true;
      }

      if (p4) {
         bs[offset + 5] = true;
      }

      if (p5) {
         bs[offset + 6] = true;
      }

      if (p6) {
         bs[offset + 7] = true;
      }

      if (p7) {
         bs[offset + 8] = true;
      }

      if (p8) {
         bs[offset + 9] = true;
      }

      if (p9) {
         bs[offset + 10] = true;
      }

      if (p10) {
         bs[offset + 11] = true;
      }

      if (p11) {
         bs[offset + 12] = true;
      }

      if (p12) {
         bs[offset + 13] = true;
      }

      if (p13) {
         bs[offset + 14] = true;
      }

   }

   public static void visitProbes(int classId, int offset, boolean p0, boolean p1, boolean p2, boolean p3, boolean p4, boolean p5, boolean p6, boolean p7, boolean p8, boolean p9, boolean p10, boolean p11, boolean p12, boolean p13, boolean p14) {
      boolean[] bs = (boolean[])CLASS_HITS.get(classId);
      bs[0] = true;
      if (p0) {
         bs[offset + 1] = true;
      }

      if (p1) {
         bs[offset + 2] = true;
      }

      if (p2) {
         bs[offset + 3] = true;
      }

      if (p3) {
         bs[offset + 4] = true;
      }

      if (p4) {
         bs[offset + 5] = true;
      }

      if (p5) {
         bs[offset + 6] = true;
      }

      if (p6) {
         bs[offset + 7] = true;
      }

      if (p7) {
         bs[offset + 8] = true;
      }

      if (p8) {
         bs[offset + 9] = true;
      }

      if (p9) {
         bs[offset + 10] = true;
      }

      if (p10) {
         bs[offset + 11] = true;
      }

      if (p11) {
         bs[offset + 12] = true;
      }

      if (p12) {
         bs[offset + 13] = true;
      }

      if (p13) {
         bs[offset + 14] = true;
      }

      if (p14) {
         bs[offset + 15] = true;
      }

   }

   public static synchronized void reset() {
      Iterator i$ = CLASS_HITS.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<Integer, boolean[]> each = (Entry)i$.next();
         CLASS_HITS.put(each.getKey(), new boolean[((boolean[])each.getValue()).length]);
      }

   }

   public static synchronized Collection<Long> getHits() {
      Collection<Long> blockHits = new ArrayList();
      Iterator i$ = CLASS_HITS.entrySet().iterator();

      while(true) {
         Entry each;
         boolean[] bs;
         do {
            if (!i$.hasNext()) {
               return blockHits;
            }

            each = (Entry)i$.next();
            bs = (boolean[])each.getValue();
         } while(!bs[0]);

         int classId = (Integer)each.getKey();

         for(int probeId = 1; probeId != bs.length; ++probeId) {
            if (bs[probeId]) {
               blockHits.add(encode(classId, probeId - 1));
            }
         }
      }
   }

   public static int registerClass(String className) {
      int id = nextId();
      invokeQueue.registerClass(id, className);
      return id;
   }

   public static void registerMethod(int clazz, String methodName, String methodDesc, int firstProbe, int lastProbe) {
      invokeQueue.registerProbes(clazz, methodName, methodDesc, firstProbe, lastProbe);
   }

   private static synchronized int nextId() {
      return classId++;
   }

   public static int decodeClassId(long value) {
      return (int)(value >> 32);
   }

   public static int decodeLineId(long value) {
      return (int)(value & -1L);
   }

   public static long encode(int classId, int line) {
      return (long)classId << 32 | (long)line;
   }

   public static void registerClassProbes(int classId, int probeCount) {
      CLASS_HITS.put(classId, new boolean[probeCount + 1]);
   }

   public static void resetAllStaticState() {
      CLASS_HITS.clear();
   }
}
