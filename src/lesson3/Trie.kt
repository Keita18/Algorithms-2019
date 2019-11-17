package lesson3

import java.util.*


class Trie : AbstractMutableSet<String>(), MutableSet<String> {
    override var size: Int = 0
        private set

    private class Node {
        val children: MutableMap<Char, Node> = linkedMapOf()
        var isWord = false
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

    override fun contains(element: String): Boolean {
        val node = findNode(element.withZero())
        return node != null && node.isWord
    }

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
        if (modified && !current.isWord) {
            current.isWord = true
            size++
        }
        return modified && current.isWord
    }

    override fun remove(element: String): Boolean {
        val current = findNode(element) ?: return false
        if (current.children.remove(0.toChar()) != null) {
            size--
            current.isWord = false
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
        private val stringBuilder = StringBuilder()
        private fun findNext() {
            next = null
            var iterator = deque.peek()
            while (iterator != null) {
                while (iterator!!.hasNext()) {
                    val e = iterator.next()
                    val key = e.key
                    stringBuilder.append(key)
                    val node = e.value
                    iterator = node.children.entries.iterator()
                    deque.push(iterator)
                    if (node.isWord) {
                        next = stringBuilder.toString()
                        return
                    }
                }
                deque.pop()
                val len = stringBuilder.length
                if (len > 0) {
                    stringBuilder.deleteCharAt(stringBuilder.length - 1)
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

        init {
            stringBuilder.append(prefix)
            deque.push(node!!.children.entries.iterator())
            if (node.isWord) {
                next = prefix
            } else {
                findNext()
            }
        }

        /**
         * Removes from the underlying collection the last element returned by this iterator.
         */
        override fun remove() {
            this@Trie.remove(next)
        }
    }

}


fun main() {
    val trie = Trie()
    trie.add("keita")
    trie.add("alpha")
    trie.add("sidiki")
    trie.add("q")
    trie.remove("sidiki")

    println(trie.contains("sidiki"))
}