package com.gzoltar.shaded.org.pitest.coverage;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.Location;

public final class BlockLocation {
   private final Location location;
   private final int block;

   public BlockLocation(Location location, int block) {
      this.location = location;
      this.block = block;
   }

   public static BlockLocation blockLocation(Location location, int block) {
      return new BlockLocation(location, block);
   }

   public boolean isFor(ClassName clazz) {
      return this.location.getClassName().equals(clazz);
   }

   public int getBlock() {
      return this.block;
   }

   public Location getLocation() {
      return this.location;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.block;
      result = 31 * result + (this.location == null ? 0 : this.location.hashCode());
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
         BlockLocation other = (BlockLocation)obj;
         if (this.block != other.block) {
            return false;
         } else {
            if (this.location == null) {
               if (other.location != null) {
                  return false;
               }
            } else if (!this.location.equals(other.location)) {
               return false;
            }

            return true;
         }
      }
   }

   public String toString() {
      return "BlockLocation [location=" + this.location + ", block=" + this.block + "]";
   }
}
