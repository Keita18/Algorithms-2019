package lesson3

import java.util.*

class GenericSortedSet<T : Comparable<T>>(
    private val fromElement: T?,                 //up border
    private val toElement: T?,                  //bottom border
    private val delegate: KtBinaryTree<T>
) : AbstractMutableSet<T>(), SortedSet<T> {
    private var ourDelegate = KtBinaryTree<T>()

    init {
        val iterator = delegate.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (checkBounds(next))
                ourDelegate.add(next)
        }
    }

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

    override fun last(): T {
        return ourDelegate.last()
    }

    override fun first(): T {
        return ourDelegate.first()
    }

    override operator fun contains(element: T): Boolean {
        if (!checkBounds(element))
            return false
        return delegate.contains(element)
    }

    override fun add(element: T): Boolean {
        if (checkBounds(element)) {
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

    override fun iterator(): MutableIterator<T> {
        return ourDelegate.iterator()
    }

    private fun checkBounds(value: T): Boolean {
        return if (fromElement != null && toElement != null)
            toElement > value && fromElement <= value
        else if (fromElement == null)
            toElement!! > value
        else
            fromElement <= value
    }

    override val size: Int
        get() = delegate.count { checkBounds(it) }

}