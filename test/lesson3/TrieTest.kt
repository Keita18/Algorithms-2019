package lesson3

import org.junit.jupiter.api.Tag
import java.security.SecureRandom
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TrieTest {

    @Test
    @Tag("Example")
    fun generalTest() {
        val trie = Trie()
        assertEquals(0, trie.size)
        assertFalse("Some" in trie)
        trie.add("abcdefg")
        assertTrue("abcdefg" in trie)
        assertFalse("abcdef" in trie)
        assertFalse("a" in trie)
        assertFalse("g" in trie)

        trie.add("zyx")
        trie.add("zwv")
        trie.add("zyt")
        trie.add("abcde")
        assertEquals(5, trie.size)
        assertTrue("abcdefg" in trie)
        assertFalse("abcdef" in trie)
        assertTrue("abcde" in trie)
        assertTrue("zyx" in trie)
        assertTrue("zyt" in trie)
        assertTrue("zwv" in trie)
        assertFalse("zy" in trie)
        assertFalse("zv" in trie)

        trie.remove("zwv")
        trie.remove("zy")
        assertEquals(4, trie.size)
        assertTrue("zyt" in trie)
        assertFalse("zwv" in trie)

        trie.clear()
        assertEquals(0, trie.size)
        assertFalse("zyx" in trie)
    }

    @Test
    @Tag("Hard")
    fun rudeIteratorTest() {
        val trie = Trie()
        assertEquals(setOf<String>(), trie)
        trie.add("abcdefg")
        trie.add("zyx")
        trie.add("zwv")
        trie.add("zyt")
        trie.add("abcde")

        assertEquals(setOf("abcdefg", "zyx", "zwv", "zyt", "abcde"), trie)
    }


    private class RandomIterator {
        private val charLower = "abcdefghijklmnopqrstuvwxyz"
        private val charUpper = charLower.toUpperCase()
        private val number = "0123456789"
        private val dataForRandomString =
            charLower + charUpper + number
        private val random: SecureRandom = SecureRandom()

        fun generateRandomString(length: Int): String {
            require(length >= 1)
            val sb = StringBuilder(length)
            for (i in 0 until length) { // 0-62 (exclusive), random returns 0-61
                val rndCharAt: Int = random.nextInt(dataForRandomString.length)
                val rndChar = dataForRandomString[rndCharAt]
                sb.append(rndChar)
            }
            return sb.toString()
        }
    }

    @Test
    @Tag("Hard")
    fun doTrieIteratorTest() {
        val randomIterator = RandomIterator()

        val trie = Trie()
        val mutableSet = mutableSetOf<String>()
        for (i in 0..4) {
            val randomLength = (4..8).random()
            val generateString = randomIterator.generateRandomString(randomLength)
            mutableSet.add(generateString)
            trie.add(generateString)
        }
        val trieIterator = trie.iterator()

        while (trieIterator.hasNext()) {
            while (trieIterator.hasNext()) {
                assertTrue(mutableSet.contains(trieIterator.next()))
            }

        }
        val iterator1 = trie.iterator()
        val iterator2 = trie.iterator()

        // hasNext call should not affect iterator position
        while (iterator1.hasNext()) {
            assertEquals(
                iterator2.next(), iterator1.next(),
                "Call of iterator.hasNext() changes its state while iterating $trie"
            )
        }
    }

    @Test
    @Tag("Hard")
    fun doIteratorRemoveTest() {
        testIteratorRemove { Trie() }
    }


    private fun testIteratorRemove(create: () -> Trie) {
        val random = Random()
        val randomIterator = RandomIterator()

        for (iteration in 1..100) {
            val list = mutableListOf<String>()
            for (i in 1..20) {
                val randomLength = (4..8).random()
                val generateString = randomIterator.generateRandomString(randomLength)
                list.add(generateString)
            }
            val treeSet = TreeSet<String>()
            val binarySet = create()
            for (element in list) {
                treeSet += element
                binarySet += element
            }
            val toRemove = list[random.nextInt(list.size)]
            treeSet.remove(toRemove)
            println("Removing $toRemove from $list")
            val iterator = binarySet.iterator()
            var counter = binarySet.size
            while (iterator.hasNext()) {
                val element = iterator.next()
                counter--
                print("$element ")
                if (element == toRemove) {
                    iterator.remove()
                }
            }
            assertEquals(
                0, counter,
                "Iterator.remove() of $toRemove from $list changed iterator position: " +
                        "we've traversed a total of ${binarySet.size - counter} elements instead of ${binarySet.size}"
            )
            println()
            assertEquals(treeSet.size, binarySet.size, "Size is incorrect after removal of $toRemove from $list")
            for (element in list) {
                val inn = element != toRemove
                assertEquals(
                    inn, element in binarySet,
                    "$element should be ${if (inn) "in" else "not in"} tree"
                )
            }
        }
    }
}
