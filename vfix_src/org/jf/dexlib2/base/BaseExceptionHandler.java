package org.jf.dexlib2.base;

import com.google.common.base.Objects;
import com.google.common.primitives.Ints;
import java.util.Comparator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.reference.TypeReference;

public abstract class BaseExceptionHandler implements ExceptionHandler {
   public static final Comparator<ExceptionHandler> BY_EXCEPTION = new Comparator<ExceptionHandler>() {
      public int compare(ExceptionHandler o1, ExceptionHandler o2) {
         String exceptionType1 = o1.getExceptionType();
         if (exceptionType1 == null) {
            return o2.getExceptionType() != null ? 1 : 0;
         } else {
            String exceptionType2 = o2.getExceptionType();
            return exceptionType2 == null ? -1 : exceptionType1.compareTo(o2.getExceptionType());
         }
      }
   };

   @Nullable
   public TypeReference getExceptionTypeReference() {
      final String exceptionType = this.getExceptionType();
      return exceptionType == null ? null : new BaseTypeReference() {
         @Nonnull
         public String getType() {
            return exceptionType;
         }
      };
   }

   public int hashCode() {
      String exceptionType = this.getExceptionType();
      int hashCode = exceptionType == null ? 0 : exceptionType.hashCode();
      return hashCode * 31 + this.getHandlerCodeAddress();
   }

   public boolean equals(@Nullable Object o) {
      if (!(o instanceof ExceptionHandler)) {
         return false;
      } else {
         ExceptionHandler other = (ExceptionHandler)o;
         return Objects.equal(this.getExceptionType(), other.getExceptionType()) && this.getHandlerCodeAddress() == other.getHandlerCodeAddress();
      }
   }

   public int compareTo(@Nonnull ExceptionHandler o) {
      String exceptionType = this.getExceptionType();
      if (exceptionType == null) {
         if (o.getExceptionType() != null) {
            return 1;
         }
      } else {
         String otherExceptionType = o.getExceptionType();
         if (otherExceptionType == null) {
            return -1;
         }

         int res = exceptionType.compareTo(o.getExceptionType());
         if (res != 0) {
            return res;
         }
      }

      return Ints.compare(this.getHandlerCodeAddress(), o.getHandlerCodeAddress());
   }
}
