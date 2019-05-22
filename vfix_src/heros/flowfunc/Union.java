package heros.flowfunc;

import com.google.common.collect.Sets;
import heros.FlowFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Union<D> implements FlowFunction<D> {
   private final FlowFunction<D>[] funcs;

   private Union(FlowFunction<D>... funcs) {
      this.funcs = funcs;
   }

   public Set<D> computeTargets(D source) {
      Set<D> res = Sets.newHashSet();
      FlowFunction[] var3 = this.funcs;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         FlowFunction<D> func = var3[var5];
         res.addAll(func.computeTargets(source));
      }

      return res;
   }

   public static <D> FlowFunction<D> union(FlowFunction<D>... funcs) {
      List<FlowFunction<D>> list = new ArrayList();
      FlowFunction[] var2 = funcs;
      int var3 = funcs.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         FlowFunction<D> f = var2[var4];
         if (f != Identity.v()) {
            list.add(f);
         }
      }

      if (list.size() == 1) {
         return (FlowFunction)list.get(0);
      } else if (list.isEmpty()) {
         return Identity.v();
      } else {
         return new Union((FlowFunction[])list.toArray(new FlowFunction[list.size()]));
      }
   }
}
