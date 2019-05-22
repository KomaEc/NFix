package org.junit.runner;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.io.ObjectInputStream.GetField;
import java.io.ObjectOutputStream.PutField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class Result implements Serializable {
   private static final long serialVersionUID = 1L;
   private static final ObjectStreamField[] serialPersistentFields = ObjectStreamClass.lookup(Result.SerializedForm.class).getFields();
   private final AtomicInteger count;
   private final AtomicInteger ignoreCount;
   private final CopyOnWriteArrayList<Failure> failures;
   private final AtomicLong runTime;
   private final AtomicLong startTime;
   private Result.SerializedForm serializedForm;

   public Result() {
      this.count = new AtomicInteger();
      this.ignoreCount = new AtomicInteger();
      this.failures = new CopyOnWriteArrayList();
      this.runTime = new AtomicLong();
      this.startTime = new AtomicLong();
   }

   private Result(Result.SerializedForm serializedForm) {
      this.count = serializedForm.fCount;
      this.ignoreCount = serializedForm.fIgnoreCount;
      this.failures = new CopyOnWriteArrayList(serializedForm.fFailures);
      this.runTime = new AtomicLong(serializedForm.fRunTime);
      this.startTime = new AtomicLong(serializedForm.fStartTime);
   }

   public int getRunCount() {
      return this.count.get();
   }

   public int getFailureCount() {
      return this.failures.size();
   }

   public long getRunTime() {
      return this.runTime.get();
   }

   public List<Failure> getFailures() {
      return this.failures;
   }

   public int getIgnoreCount() {
      return this.ignoreCount.get();
   }

   public boolean wasSuccessful() {
      return this.getFailureCount() == 0;
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      Result.SerializedForm serializedForm = new Result.SerializedForm(this);
      serializedForm.serialize(s);
   }

   private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
      this.serializedForm = Result.SerializedForm.deserialize(s);
   }

   private Object readResolve() {
      return new Result(this.serializedForm);
   }

   public RunListener createListener() {
      return new Result.Listener();
   }

   private static class SerializedForm implements Serializable {
      private static final long serialVersionUID = 1L;
      private final AtomicInteger fCount;
      private final AtomicInteger fIgnoreCount;
      private final List<Failure> fFailures;
      private final long fRunTime;
      private final long fStartTime;

      public SerializedForm(Result result) {
         this.fCount = result.count;
         this.fIgnoreCount = result.ignoreCount;
         this.fFailures = Collections.synchronizedList(new ArrayList(result.failures));
         this.fRunTime = result.runTime.longValue();
         this.fStartTime = result.startTime.longValue();
      }

      private SerializedForm(GetField fields) throws IOException {
         this.fCount = (AtomicInteger)fields.get("fCount", (Object)null);
         this.fIgnoreCount = (AtomicInteger)fields.get("fIgnoreCount", (Object)null);
         this.fFailures = (List)fields.get("fFailures", (Object)null);
         this.fRunTime = fields.get("fRunTime", 0L);
         this.fStartTime = fields.get("fStartTime", 0L);
      }

      public void serialize(ObjectOutputStream s) throws IOException {
         PutField fields = s.putFields();
         fields.put("fCount", this.fCount);
         fields.put("fIgnoreCount", this.fIgnoreCount);
         fields.put("fFailures", this.fFailures);
         fields.put("fRunTime", this.fRunTime);
         fields.put("fStartTime", this.fStartTime);
         s.writeFields();
      }

      public static Result.SerializedForm deserialize(ObjectInputStream s) throws ClassNotFoundException, IOException {
         GetField fields = s.readFields();
         return new Result.SerializedForm(fields);
      }
   }

   @RunListener.ThreadSafe
   private class Listener extends RunListener {
      private Listener() {
      }

      public void testRunStarted(Description description) throws Exception {
         Result.this.startTime.set(System.currentTimeMillis());
      }

      public void testRunFinished(Result result) throws Exception {
         long endTime = System.currentTimeMillis();
         Result.this.runTime.addAndGet(endTime - Result.this.startTime.get());
      }

      public void testFinished(Description description) throws Exception {
         Result.this.count.getAndIncrement();
      }

      public void testFailure(Failure failure) throws Exception {
         Result.this.failures.add(failure);
      }

      public void testIgnored(Description description) throws Exception {
         Result.this.ignoreCount.getAndIncrement();
      }

      public void testAssumptionFailure(Failure failure) {
      }

      // $FF: synthetic method
      Listener(Object x1) {
         this();
      }
   }
}
