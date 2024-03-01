package com.github.alexwith.humap.dirtytracking;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DirtyTrackerImpl implements DirtyTracker {
    private final Set<DirtyEntry> entries = new LinkedHashSet<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public Set<DirtyEntry> getDirty() {
        final Lock lock = this.lock.readLock();
        lock.lock();
        try {
            return new HashSet<>(this.entries);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void add(DirtyType type, Object[] path) {
        final Lock lock = this.lock.writeLock();
        lock.lock();
        try {
            this.entries.add(new DirtyEntryImpl(type, path));
        } finally {
            lock.unlock();
        }
    }
}
