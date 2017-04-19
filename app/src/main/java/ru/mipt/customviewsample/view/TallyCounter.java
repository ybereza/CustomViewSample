
package ru.mipt.customviewsample.view;

/**
 * An interface for a view implementing a tally counter.
 */
public interface TallyCounter {

    /**
     * Reset the counter.
     */
    void reset();

    /**
     * Increment the counter.
     */
    void increment();

    /**
     * @return The current count of the counter.
     */
    int getCount();

    /**
     * Set the counter value.
     */
    void setCount(int count);

}





