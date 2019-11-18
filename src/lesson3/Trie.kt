package lesson3

import java.util.*
import kotlin.NoSuchElementException


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

    override fun contains(element: String): Boolean {
        val node = findNode(element.withZero())
        return node != null
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
     */
    override fun iterator(): MutableIterator<String> {
        return TrieIterator(root, "")
    }

    private inner class TrieIterator(node: Node?, prefix: String?) :
        MutableIterator<String> {
        private var next: String? = null
        val prefix = ArrayDeque(listOf(prefix))
        val nodes = ArrayDeque(listOf(node))

        /**
         * Time Complexity O(1)
         * Memory Complexity O(1)
         */
        override fun hasNext(): Boolean {
            return nodes.isNotEmpty()
        }

        /**
         * Time Complexity O(n)
         * Memory Complexity O(n)
         */
        override fun next(): String {
            while (nodes.isNotEmpty()) {
                val iterator = nodes.removeLast().children
                next = prefix.removeLast()
                for ((char, node) in iterator)
                    if (char != 0.toChar()) {
                        prefix.add(next + char)
                        nodes.add(node)
                    } else return next!!
            }
            throw NoSuchElementException()
        }

        /**
         * Removes from the underlying collection the last element returned by this iterator.
         *
         * Time Complexity O(l) - l length of string
         * Memory Complexity O(1)
         */
        override fun remove() {
            this@Trie.remove(next)
        }
    }

}