package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

interface CycleDetectingLock<ID> {
   ListMultimap<Long, ID> lockOrDetectPotentialLocksCycle();

   void unlock();

   public static class CycleDetectingLockFactory<ID> {
      private Map<Long, CycleDetectingLock.CycleDetectingLockFactory<ID>.ReentrantCycleDetectingLock> lockThreadIsWaitingOn = Maps.newHashMap();
      private final Multimap<Long, CycleDetectingLock.CycleDetectingLockFactory<ID>.ReentrantCycleDetectingLock> locksOwnedByThread = LinkedHashMultimap.create();

      CycleDetectingLock<ID> create(ID newLockId) {
         return new CycleDetectingLock.CycleDetectingLockFactory.ReentrantCycleDetectingLock(newLockId, new ReentrantLock());
      }

      class ReentrantCycleDetectingLock implements CycleDetectingLock<ID> {
         private final Lock lockImplementation;
         private final ID userLockId;
         private Long lockOwnerThreadId = null;
         private int lockReentranceCount = 0;

         ReentrantCycleDetectingLock(ID userLockId, Lock lockImplementation) {
            this.userLockId = Preconditions.checkNotNull(userLockId, "userLockId");
            this.lockImplementation = (Lock)Preconditions.checkNotNull(lockImplementation, "lockImplementation");
         }

         public ListMultimap<Long, ID> lockOrDetectPotentialLocksCycle() {
            long currentThreadId = Thread.currentThread().getId();
            synchronized(CycleDetectingLockFactory.this) {
               this.checkState();
               ListMultimap<Long, ID> locksInCycle = this.detectPotentialLocksCycle();
               if (!locksInCycle.isEmpty()) {
                  return locksInCycle;
               }

               CycleDetectingLockFactory.this.lockThreadIsWaitingOn.put(currentThreadId, this);
            }

            this.lockImplementation.lock();
            synchronized(CycleDetectingLockFactory.this) {
               CycleDetectingLockFactory.this.lockThreadIsWaitingOn.remove(currentThreadId);
               this.checkState();
               this.lockOwnerThreadId = currentThreadId;
               ++this.lockReentranceCount;
               CycleDetectingLockFactory.this.locksOwnedByThread.put(currentThreadId, this);
            }

            return ImmutableListMultimap.of();
         }

         public void unlock() {
            long currentThreadId = Thread.currentThread().getId();
            synchronized(CycleDetectingLockFactory.this) {
               this.checkState();
               Preconditions.checkState(this.lockOwnerThreadId != null, "Thread is trying to unlock a lock that is not locked");
               Preconditions.checkState(this.lockOwnerThreadId == currentThreadId, "Thread is trying to unlock a lock owned by another thread");
               this.lockImplementation.unlock();
               --this.lockReentranceCount;
               if (this.lockReentranceCount == 0) {
                  this.lockOwnerThreadId = null;
                  Preconditions.checkState(CycleDetectingLockFactory.this.locksOwnedByThread.remove(currentThreadId, this), "Internal error: Can not find this lock in locks owned by a current thread");
                  if (CycleDetectingLockFactory.this.locksOwnedByThread.get(currentThreadId).isEmpty()) {
                     CycleDetectingLockFactory.this.locksOwnedByThread.removeAll(currentThreadId);
                  }
               }

            }
         }

         void checkState() throws IllegalStateException {
            long currentThreadId = Thread.currentThread().getId();
            Preconditions.checkState(!CycleDetectingLockFactory.this.lockThreadIsWaitingOn.containsKey(currentThreadId), "Internal error: Thread should not be in a waiting thread on a lock now");
            if (this.lockOwnerThreadId != null) {
               Preconditions.checkState(this.lockReentranceCount >= 0, "Internal error: Lock ownership and reentrance count internal states do not match");
               Preconditions.checkState(CycleDetectingLockFactory.this.locksOwnedByThread.get(this.lockOwnerThreadId).contains(this), "Internal error: Set of locks owned by a current thread and lock ownership status do not match");
            } else {
               Preconditions.checkState(this.lockReentranceCount == 0, "Internal error: Reentrance count of a non locked lock is expect to be zero");
               Preconditions.checkState(!CycleDetectingLockFactory.this.locksOwnedByThread.values().contains(this), "Internal error: Non locked lock should not be owned by any thread");
            }

         }

         private ListMultimap<Long, ID> detectPotentialLocksCycle() {
            long currentThreadId = Thread.currentThread().getId();
            if (this.lockOwnerThreadId != null && this.lockOwnerThreadId != currentThreadId) {
               ListMultimap<Long, ID> potentialLocksCycle = Multimaps.newListMultimap(new LinkedHashMap(), new Supplier<List<ID>>() {
                  public List<ID> get() {
                     return Lists.newArrayList();
                  }
               });

               Long threadOwnerThreadWaits;
               for(CycleDetectingLock.CycleDetectingLockFactory.ReentrantCycleDetectingLock lockOwnerWaitingOn = this; lockOwnerWaitingOn != null && lockOwnerWaitingOn.lockOwnerThreadId != null; lockOwnerWaitingOn = (CycleDetectingLock.CycleDetectingLockFactory.ReentrantCycleDetectingLock)CycleDetectingLockFactory.this.lockThreadIsWaitingOn.get(threadOwnerThreadWaits)) {
                  threadOwnerThreadWaits = lockOwnerWaitingOn.lockOwnerThreadId;
                  potentialLocksCycle.putAll(threadOwnerThreadWaits, this.getAllLockIdsAfter(threadOwnerThreadWaits, lockOwnerWaitingOn));
                  if (threadOwnerThreadWaits == currentThreadId) {
                     return potentialLocksCycle;
                  }
               }

               return ImmutableListMultimap.of();
            } else {
               return ImmutableListMultimap.of();
            }
         }

         private List<ID> getAllLockIdsAfter(long threadId, CycleDetectingLock.CycleDetectingLockFactory<ID>.ReentrantCycleDetectingLock lock) {
            List<ID> ids = Lists.newArrayList();
            boolean found = false;
            Collection<CycleDetectingLock.CycleDetectingLockFactory<ID>.ReentrantCycleDetectingLock> ownedLocks = CycleDetectingLockFactory.this.locksOwnedByThread.get(threadId);
            Preconditions.checkNotNull(ownedLocks, "Internal error: No locks were found taken by a thread");
            Iterator i$ = ownedLocks.iterator();

            while(i$.hasNext()) {
               CycleDetectingLock.CycleDetectingLockFactory<ID>.ReentrantCycleDetectingLock ownedLock = (CycleDetectingLock.CycleDetectingLockFactory.ReentrantCycleDetectingLock)i$.next();
               if (ownedLock == lock) {
                  found = true;
               }

               if (found) {
                  ids.add(ownedLock.userLockId);
               }
            }

            Preconditions.checkState(found, "Internal error: We can not find locks that created a cycle that we detected");
            return ids;
         }

         public String toString() {
            Long localLockOwnerThreadId = this.lockOwnerThreadId;
            return localLockOwnerThreadId != null ? String.format("CycleDetectingLock[%s][locked by %s]", this.userLockId, localLockOwnerThreadId) : String.format("CycleDetectingLock[%s][unlocked]", this.userLockId);
         }
      }
   }
}
