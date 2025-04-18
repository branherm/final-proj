package edu.guilford;

/**
 * An interface defining the basic operations for a hash table map that stores account passwords.
 */
public interface hashTableMap {
    /**
     * Retrieves the password associated with the specified account.
     * @param Account the account name to look up
     * @return the password associated with the account, or null if not found
     */
    Object get_Acc(Object Account);
    
    /**
     * Adds or updates an account-password pair in the hash table.
     * @param Account the account name to add/update
     * @param passwd the password to associate with the account
     * @return the hash value where the account was stored
     */
    int add_Acc(Object Account, Object passwd);
    
    /**
     * Removes an account from the hash table.
     * @param Account the account name to remove
     * @return the password that was associated with the account, or null if not found
     */
    Object remove_Acc(Object Account);
}