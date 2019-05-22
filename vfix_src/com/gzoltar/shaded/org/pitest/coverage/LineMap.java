package com.gzoltar.shaded.org.pitest.coverage;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import java.util.Map;
import java.util.Set;

public interface LineMap {
   Map<BlockLocation, Set<Integer>> mapLines(ClassName var1);
}
