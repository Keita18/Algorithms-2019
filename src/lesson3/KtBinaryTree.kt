package lesson3

import java.util.*
import kotlin.NoSuchElementException
import kotlin.math.max

// Attention: comparable supported but comparator is not
open class KtBinaryTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    override var size = 0

    private class Node<T>(var value: T) {

        var left: Node<T>? = null

        var right: Node<T>? = null

        var parent: Node<T>? = null
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
                newNode.parent = closest
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
                newNode.parent = closest
            }
        }
        size++
        return true
    }

    override fun checkInvariant(): Boolean =
        root?.let { checkInvariant(it) } ?: true

    override fun height(): Int = height(root)

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    private fun height(node: Node<T>?): Int {
        if (node == null) return 0
        return 1 + max(height(node.left), height(node.right))
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     *
     * Time Complexity O(h) - h height of tree
     */
    override fun remove(element: T): Boolean {
        val toDelete: Node<T>? = find(element) ?: return false

        if (toDelete!!.left == null || toDelete.right == null)
            splice(toDelete)
        else {
            var minRight = toDelete.right
            while (minRight!!.left != null)
                minRight = minRight.left
            toDelete.value = minRight.value
            splice(minRight)
        }
        return true
    }

    private fun splice(toDelete: Node<T>) {
        val parent: Node<T>?
        var s: Node<T>? = null
        when {
            toDelete.left != null -> s = toDelete.left!!
            toDelete.right != null -> s = toDelete.right!!
        }
        if (toDelete == root) {
            root = s
            parent = null
        } else {
            parent = toDelete.parent
            when (toDelete) {
                parent?.left -> parent.left = s
                parent?.right -> parent.right = s
            }
        }
        if (s != null)
            s.parent = parent
        size--
    }


    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? =
        root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    inner class BinaryTreeIterator internal constructor() : MutableIterator<T> {

        private var stack: Stack<Node<T>>

        init {
            var node = root
            stack = Stack()
            while (node != null) {
                stack.push(node)
                node = node.left
            }
        }

        /**
         * Проверка наличия следующего элемента
         * Средняя
         *
         * Time Complexity O(1)
         */
        override fun hasNext(): Boolean {
            return stack.isNotEmpty()
        }

        /**
         * Поиск следующего элемента
         * Средняя
         */

        override fun next(): T {
            var node = stack.pop()
            result = node.value
            if (node.right != null) {
                node = node.right
                while (node != null) {
                    stack.push(node)
                    node = node.left
                }
            }
            return result!!
        }

        var result: T? = null


        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            this@KtBinaryTree.remove(result)
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Найти множество всех элементов в диапазоне [fromElement, toElement)
     * Очень сложная
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        return SubsetTree(fromElement, toElement, this)
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        return SubsetTree(null, toElement, this)
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        return SubsetTree(fromElement, null, this)
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }
}

class SubsetTree<T : Comparable<T>>
    (
    private val fromElement: T?,                 //up border
    private val toElement: T?,                  //bottom border
    private val delegate: KtBinaryTree<T>
) : KtBinaryTree<T>() {

    override var size: Int
        get() {
            var size = 0
            for (next in delegate) {
                if (checkBounds(next)) size++
            }
            return size
        }
        set(value) {}

    private fun checkBounds(value: T): Boolean {
        return if (fromElement != null && toElement != null)
            toElement > value && fromElement <= value
        else if (fromElement == null)
            toElement!! > value
        else
            fromElement <= value
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

        if (checkBounds(element) && contains(element))
            delegate.remove(element)
        else
            throw IllegalArgumentException()
        return true
    }

    override fun iterator(): MutableIterator<T> {
        return SubSetIterator()
    }

    inner class SubSetIterator : MutableIterator<T> {

        internal var iterator: MutableIterator<T> = delegate.iterator()
        private var current: T? = null
        internal var next = findNext()

        private fun findNext(): T? {
            if (fromElement != null) {
                next = fromElement
            }
            while (iterator.hasNext()) {
                val nextElement = iterator.next()
                if (checkBounds(nextElement)) {
                    next = nextElement
                    return nextElement
                }
            }
            return null
        }

        override fun hasNext(): Boolean {
            return next != null
        }

        override fun next(): T {
            if (next == null) throw NoSuchElementException()
            current = next
            next = findNext()
            return current!!
        }

        override fun remove() {
            iterator.remove()
        }
    }
}

fun main() {
    val binaryTree = KtBinaryTree<Int>()

    for (i in 0..25 step 3)
        binaryTree.add(i)
    println("binaryTree -> $binaryTree")

    val subset = binaryTree.subSet(9, 21)
    var first = subset.first()
    println("subset -> $subset , firt -> $first")

    val subsubset = subset.subSet(10, 16)
    first = subsubset.first()
    println("subsubset -> $subsubset,  firt -> $first")
}