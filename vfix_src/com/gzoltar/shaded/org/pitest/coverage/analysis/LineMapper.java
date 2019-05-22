package com.gzoltar.shaded.org.pitest.coverage.analysis;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.classpath.CodeSource;
import com.gzoltar.shaded.org.pitest.coverage.BlockLocation;
import com.gzoltar.shaded.org.pitest.coverage.LineMap;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.Location;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MethodName;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassReader;
import com.gzoltar.shaded.org.pitest.reloc.asm.tree.ClassNode;
import com.gzoltar.shaded.org.pitest.reloc.asm.tree.MethodNode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LineMapper implements LineMap {
   private final CodeSource source;

   public LineMapper(CodeSource source) {
      this.source = source;
   }

   public Map<BlockLocation, Set<Integer>> mapLines(ClassName clazz) {
      ControlFlowAnalyser cfa = new ControlFlowAnalyser();
      Map<BlockLocation, Set<Integer>> map = new HashMap();
      Option<byte[]> maybeBytes = this.source.fetchClassBytes(clazz);
      Iterator i$ = maybeBytes.iterator();

      while(i$.hasNext()) {
         byte[] bytes = (byte[])i$.next();
         ClassReader cr = new ClassReader(bytes);
         ClassNode classNode = new ClassNode();
         cr.accept(classNode, 8);
         Iterator i$ = classNode.methods.iterator();

         while(i$.hasNext()) {
            Object m = i$.next();
            MethodNode mn = (MethodNode)m;
            Location l = Location.location(clazz, MethodName.fromString(mn.name), mn.desc);
            List<Block> blocks = cfa.analyze(mn);

            for(int i = 0; i != blocks.size(); ++i) {
               BlockLocation bl = new BlockLocation(l, i);
               map.put(bl, ((Block)blocks.get(i)).getLines());
            }
         }
      }

      return map;
   }
}
