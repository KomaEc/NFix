package pxb.android.arsc;

import java.util.ArrayList;
import java.util.List;

public class Type {
   public List<Config> configs = new ArrayList();
   public int id;
   public String name;
   public ResSpec[] specs;
   int wPosition;

   public void addConfig(Config config) {
      if (config.entryCount != this.specs.length) {
         throw new RuntimeException();
      } else {
         this.configs.add(config);
      }
   }

   public ResSpec getSpec(int resId) {
      ResSpec res = this.specs[resId];
      if (res == null) {
         res = new ResSpec(resId);
         this.specs[resId] = res;
      }

      return res;
   }
}
