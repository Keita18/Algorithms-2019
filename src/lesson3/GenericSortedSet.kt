package lesson3

import java.util.*

class GenericSortedSet<T : Comparable<T>>(
    private val fromElement: T?,                 //up border
    private val toElement: T?,                  //bottom border
    private val delegate: KtBinaryTree<T>
) : AbstractMutableSet<T>(), SortedSet<T> {

    override fun comparator(): Comparator<in T>? = delegate.comparator()

    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        return GenericSortedSet(fromElement, toElement, delegate)
    }

    override fun headSet(toElement: T): SortedSet<T> {
        return GenericSortedSet(null, toElement, delegate)
    }

    override fun tailSet(fromElement: T): SortedSet<T> {
        return GenericSortedSet(fromElement, null, delegate)
    }

    /**
     * Time Complexity O(n) in worst cage
     * Memory Complexity O(1)
     */
    override fun first(): T? {
        val iterator = delegate.iterator()
        var currentFirst: T? = null
        while (iterator.hasNext() && currentFirst == null) {
            val nextElement = iterator.next()
            if (checkInRange(nextElement) && this.contains(nextElement)) {
                currentFirst = nextElement
            }
        }
        return currentFirst
    }

    /**
     * Time Complexity in worst case O(n-1) -> O(n) - n number of element in KtBinary
     * if toElement == KtBinary.last
     * Memory Complexity O(1)
     */
    override fun last(): T? {
        val iterator = delegate.iterator()
        var currentLast: T? = null
        while (iterator.hasNext()) {
            val nextElement = iterator.next()
            if (checkInRange(nextElement) && this.contains(nextElement)) {
                currentLast = nextElement
            }
        }
        return currentLast
    }

    /**
     * contains, add, remove
     * Time Complexity O(h) - h height of tree
     * Memory Complexity O(1)
     */
    override operator fun contains(element: T): Boolean {
        if (!checkInRange(element))
            return false
        return delegate.contains(element)
    }

    override fun add(element: T): Boolean {
        if (checkInRange(element)) {
            delegate.add(element)
            return true
        } else throw IllegalArgumentException()
    }

    override fun remove(element: T): Boolean {
        if (contains(element))
            delegate.remove(element)
        else
            throw IllegalArgumentException()
        return true
    }

    override fun iterator(): MutableIterator<T> = object : MutableIterator<T> {
        private val delegate = this@GenericSortedSet.delegate.iterator()

        private var next: T? = null

        init {
            while (delegate.hasNext()) {
                val next = delegate.next()
                if (checkInRange(next)) {
                    this.next = next
                    break
                }
            }
        }

        /**
         * Time Complexity O(1)
         * Memory Complexity O(1)
         */
        override fun hasNext(): Boolean {
            return next != null
        }

        /**
         * Time Complexity O(1)
         * Memory Complexity O(1)
         */
        override fun next(): T {
            val result = next ?: throw NoSuchElementException()
            next = if (delegate.hasNext()) delegate.next() else null
            return result
        }

        /**
         * like in KtBinary tree
         */
        override fun remove() {
            delegate.remove()
        }

    }

    private fun checkInRange(value: T): Boolean {
        return if (fromElement != null && toElement != null)
            toElement > value && fromElement <= value
        else if (fromElement == null)
            toElement!! > value
        else
            fromElement <= value
    }

    /**
     * Time Complexity O(n) - n element in binary
     * Memory Complexity O(1)
     */
    override val size: Int
        get() = delegate.count { checkInRange(it) }

}