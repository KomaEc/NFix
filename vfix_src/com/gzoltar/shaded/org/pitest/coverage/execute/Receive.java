package com.gzoltar.shaded.org.pitest.coverage.execute;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.coverage.BlockLocation;
import com.gzoltar.shaded.org.pitest.coverage.CoverageResult;
import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.Location;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MethodName;
import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.util.ReceiveStrategy;
import com.gzoltar.shaded.org.pitest.util.SafeDataInputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import sun.pitest.CodeCoverageStore;

final class Receive implements ReceiveStrategy {
   private final Map<Integer, ClassName> classIdToName = new ConcurrentHashMap();
   private final Map<Long, BlockLocation> probeToBlock = new ConcurrentHashMap();
   private final SideEffect1<CoverageResult> handler;

   Receive(SideEffect1<CoverageResult> handler) {
      this.handler = handler;
   }

   public void apply(byte control, SafeDataInputStream is) {
      switch(control) {
      case 4:
         this.handleProbes(is);
         break;
      case 16:
         this.handleTestEnd(is);
         break;
      case 32:
         int id = is.readInt();
         String name = is.readString();
         this.classIdToName.put(id, ClassName.fromString(name));
      case 64:
      }

   }

   private void handleProbes(SafeDataInputStream is) {
      int classId = is.readInt();
      String methodName = is.readString();
      String methodSig = is.readString();
      int first = is.readInt();
      int last = is.readInt();
      Location loc = Location.location((ClassName)this.classIdToName.get(classId), MethodName.fromString(methodName), methodSig);

      for(int i = first; i != last + 1; ++i) {
         this.probeToBlock.put(CodeCoverageStore.encode(classId, i), new BlockLocation(loc, i - first));
      }

   }

   private void handleTestEnd(SafeDataInputStream is) {
      Description d = (Description)is.read(Description.class);
      int numberOfResults = is.readInt();
      Set<BlockLocation> hits = new HashSet(numberOfResults);

      for(int i = 0; i != numberOfResults; ++i) {
         this.readProbeHit(is, hits);
      }

      this.handler.apply(this.createCoverageResult(is, d, hits));
   }

   private void readProbeHit(SafeDataInputStream is, Set<BlockLocation> hits) {
      long encoded = is.readLong();
      BlockLocation location = this.probeToBlock(encoded);
      hits.add(location);
   }

   private BlockLocation probeToBlock(long encoded) {
      return (BlockLocation)this.probeToBlock.get(encoded);
   }

   private CoverageResult createCoverageResult(SafeDataInputStream is, Description d, Collection<BlockLocation> visitedBlocks) {
      boolean isGreen = is.readBoolean();
      int executionTime = is.readInt();
      CoverageResult cr = new CoverageResult(d, executionTime, isGreen, visitedBlocks);
      return cr;
   }
}
