package edu.guilford;

/**
 * A class that implements a hash table for storing account passwords.
 * Supports linear probing for collision resolution and automatic rehashing.
 */
public class HashtablePassword implements hashTableMap {
    private final int useProbe;
    private Entry[] entries;
    private final float loadFactor;
    private int size, used;
    private final Entry NIL = new Entry(null, null);

    /**
     * Represents an entry in the hash table (key-value pair).
     */
    private static class Entry {
        Object key, value;
        Entry(Object k, Object v) {
            key = k;
            value = v;
        }
    }

    /**
     * Constructs a new hash table with the specified parameters.
     * @param capacity the initial capacity of the hash table
     * @param loadFactor the load factor at which rehashing should occur
     * @param useProbe the probing strategy to use (0 for linear probing)
     */
    public HashtablePassword(int capacity, float loadFactor, int useProbe) {
        entries = new Entry[capacity];
        this.loadFactor = loadFactor;
        this.useProbe = useProbe;
    }

    /**
     * Computes the hash value for a given key.
     * @param key the key to hash
     * @return the computed hash value
     */
    public int hash(Object key) {
        return (key.hashCode() & 0x7FFFFFFF) % entries.length;
    }

    /**
     * Computes the next probe location using linear probing.
     * @param h the initial hash value
     * @param i the probe number
     * @return the next probe location
     */
    private int nextProbe(int h, int i) {
        return (h + i) % entries.length;
    }

    /**
     * Rehashes the table when the load factor is exceeded.
     * Doubles the table size and reinserts all entries.
     */
    private void rehash() {
        Entry[] oldEntries = entries;
        entries = new Entry[2 * entries.length + 1];
        for (Entry entry : oldEntries) {
            if (entry == NIL || entry == null) continue;
            int h = hash(entry.key);
            for (int x = 0; x < entries.length; x++) {
                int j = nextProbe(h, x);
                if (entries[j] == null) {
                    entries[j] = entry;
                    break;
                }
            }
            used = size;
        }
    }

    @Override
    public int add_Acc(Object Account, Object passwd) {
        if (used > (loadFactor * entries.length)) rehash();
        int h = hash(Account);
        for (int i = 0; i < entries.length; i++) {
            int j = (h + i) % entries.length;
            Entry entry = entries[j];
            if (entry == null) {
                entries[j] = new Entry(Account, passwd);
                ++size;
                ++used;
                return h;
            }
            if (entry == NIL) continue;
            if (entry.key.equals(Account)) {
                Object oldValue = entry.value;
                entries[j].value = passwd;
                return (int) oldValue;
            }
        }
        return h;
    }

    @Override
    public Object get_Acc(Object Account) {
        int h = hash(Account);
        for (int i = 0; i < entries.length; i++) {
            int j = nextProbe(h, i);
            Entry entry = entries[j];
            if (entry == null) break;
            if (entry == NIL) continue;
            if (entry.key.equals(Account)) return entry.value;
        }
        return null;
    }

    @Override
    public Object remove_Acc(Object Account) {
        int h = hash(Account);
        for (int i = 0; i < entries.length; i++) {
            int j = nextProbe(h, i);
            Entry entry = entries[j];
            if (entry == NIL) continue;
            if (entry.key.equals(Account)) {
                Object Value = entry.value;
                entries[j] = NIL;
                size--;
                return Value;
            }
        }
        return null;
    }
}