package lesson3

import java.util.*


class Trie : AbstractMutableSet<String>(), MutableSet<String> {
    override var size: Int = 0
        private set

    private class Node {
        val children: MutableMap<Char, Node> = linkedMapOf()
    }

    private var root = Node()

    override fun clear() {
        root.children.clear()
        size = 0
    }

    private fun String.withZero() = this + 0.toChar()

    private fun findNode(element: String): Node? {
        var current = root
        for (char in element) {
            current = current.children[char] ?: return null
        }
        return current
    }

    override fun contains(element: String): Boolean =
        findNode(element.withZero()) != null

    override fun add(element: String): Boolean {
        var current = root
        var modified = false
        for (char in element.withZero()) {
            val child = current.children[char]
            if (child != null) {
                current = child
            } else {
                modified = true
                val newChild = Node()
                current.children[char] = newChild
                current = newChild
            }
        }
        if (modified) {
            size++
        }
        return modified
    }

    override fun remove(element: String): Boolean {
        val current = findNode(element) ?: return false
        if (current.children.remove(0.toChar()) != null) {
            size--
            return true
        }
        return false
    }

    /**
     * Итератор для префиксного дерева
     * Сложная
     * Time Complexity O(n * l) n - nodeNumber in the tree , l word length
     * Memory Complexity (n) n - nodeNumber
     */
    override fun iterator(): MutableIterator<String> {
        return TrieIterator(root, "")
    }

    /* Pre-order iterator */
    private inner class TrieIterator(node: Node?, prefix: String?) :
        MutableIterator<String> {
        private var next: String? = null
        private val deque: Deque<MutableIterator<MutableMap.MutableEntry<Char, Node>>> =
            ArrayDeque()
        private val strB = StringBuilder()


        init {
            strB.append(prefix)
            deque.push(node!!.children.entries.iterator())
            findNext()
        }


        private fun findNext() {
            next = null
            var iterator = deque.peek()
            var i = 0
            var j = 0
            while (iterator != null) {
                i++
                while (iterator!!.hasNext()) {
                    j++

                    val itNext = iterator.next()
                    val key = itNext.key
                    strB.append(key)
                    val node = itNext.value
                    iterator = node.children.entries.iterator()
                    deque.push(iterator)
                }
                deque.pop()
                val length = strB.length
                if (length > 0) {
                    strB.deleteCharAt(strB.length - 1)
                }
                iterator = deque.peek()
            }
        }

        override fun hasNext(): Boolean {
            return next != null
        }

        override fun next(): String {
            val ret = next
            findNext()
            return ret!!
        }

        /**
         * Removes from the underlying collection the last element returned by this iterator.
         */
        override fun remove() {
            this@Trie.remove(next)
        }
    }

}