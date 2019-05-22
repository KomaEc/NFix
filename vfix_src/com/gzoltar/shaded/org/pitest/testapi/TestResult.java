package com.gzoltar.shaded.org.pitest.testapi;

public final class TestResult {
   private final Description description;
   private final Throwable throwable;
   private final TestUnitState state;

   public TestResult(Description description, Throwable t) {
      this(description, t, TestUnitState.FINISHED);
   }

   public TestResult(Description description, Throwable t, TestUnitState state) {
      this.description = description;
      this.throwable = t;
      this.state = state;
   }

   public Throwable getThrowable() {
      return this.throwable;
   }

   public TestUnitState getState() {
      return this.state;
   }

   public Description getDescription() {
      return this.description;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.description == null ? 0 : this.description.hashCode());
      result = 31 * result + (this.state == null ? 0 : this.state.hashCode());
      result = 31 * result + (this.throwable == null ? 0 : this.throwable.hashCode());
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
         TestResult other = (TestResult)obj;
         if (this.description == null) {
            if (other.description != null) {
               return false;
            }
         } else if (!this.description.equals(other.description)) {
            return false;
         }

         if (this.state == null) {
            if (other.state != null) {
               return false;
            }
         } else if (!this.state.equals(other.state)) {
            return false;
         }

         if (this.throwable == null) {
            if (other.throwable != null) {
               return false;
            }
         } else if (!this.throwable.equals(other.throwable)) {
            return false;
         }

         return true;
      }
   }

   public String toString() {
      return "TestResult [description=" + this.description + ", state=" + this.state + ", throwable=" + this.throwable + "]";
   }
}
