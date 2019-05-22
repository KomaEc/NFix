package com.gzoltar.shaded.org.jacoco.core.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ExecutionDataStore implements IExecutionDataVisitor {
   private final Map<Long, ExecutionData> entries = new HashMap();
   private final Set<String> names = new HashSet();

   public void put(ExecutionData data) throws IllegalStateException {
      Long id = data.getId();
      ExecutionData entry = (ExecutionData)this.entries.get(id);
      if (entry == null) {
         this.entries.put(id, data);
         this.names.add(data.getName());
      } else {
         entry.merge(data);
      }

   }

   public void subtract(ExecutionData data) throws IllegalStateException {
      Long id = data.getId();
      ExecutionData entry = (ExecutionData)this.entries.get(id);
      if (entry != null) {
         entry.merge(data, false);
      }

   }

   public void subtract(ExecutionDataStore store) {
      Iterator i$ = store.getContents().iterator();

      while(i$.hasNext()) {
         ExecutionData data = (ExecutionData)i$.next();
         this.subtract(data);
      }

   }

   public ExecutionData get(long id) {
      return (ExecutionData)this.entries.get(id);
   }

   public boolean contains(String name) {
      return this.names.contains(name);
   }

   public ExecutionData get(Long id, String name, int probecount) {
      ExecutionData entry = (ExecutionData)this.entries.get(id);
      if (entry == null) {
         entry = new ExecutionData(id, name, probecount);
         this.entries.put(id, entry);
         this.names.add(name);
      } else {
         entry.assertCompatibility(id, name, probecount);
      }

      return entry;
   }

   public void reset() {
      Iterator i$ = this.entries.values().iterator();

      while(i$.hasNext()) {
         ExecutionData executionData = (ExecutionData)i$.next();
         executionData.reset();
      }

   }

   public Collection<ExecutionData> getContents() {
      return new ArrayList(this.entries.values());
   }

   public void accept(IExecutionDataVisitor visitor) {
      Iterator i$ = this.getContents().iterator();

      while(i$.hasNext()) {
         ExecutionData data = (ExecutionData)i$.next();
         visitor.visitClassExecution(data);
      }

   }

   public void visitClassExecution(ExecutionData data) {
      this.put(data);
   }
}
