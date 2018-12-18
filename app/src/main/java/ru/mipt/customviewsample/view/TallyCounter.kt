package ru.mipt.customviewsample.view

/**
 * An interface for a view implementing a tally counter.
 */
interface TallyCounter {

    /**
     * Reset the counter.
     */
    fun reset()

    /**
     * Increment the counter.
     */
    fun increment();

    /**
     * @return The current count of the counter.
     */
    fun getCount() : Int

    /**
     * Set the counter value.
     */
    fun setCount(count : Int)
}





