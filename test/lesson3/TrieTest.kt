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


    private inner class RandomIterator {
        private val CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz"
        private val CHAR_UPPER = CHAR_LOWER.toUpperCase()
        private val NUMBER = "0123456789"
        private val DATA_FOR_RANDOM_STRING =
            CHAR_LOWER + CHAR_UPPER + NUMBER
        private val random: SecureRandom = SecureRandom()

        fun generateRandomString(length: Int): String {
            require(length >= 1)
            val sb = StringBuilder(length)
            for (i in 0 until length) { // 0-62 (exclusive), random returns 0-61
                val rndCharAt: Int = random.nextInt(DATA_FOR_RANDOM_STRING.length)
                val rndChar = DATA_FOR_RANDOM_STRING[rndCharAt]
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
        for (i in 0..64) {
            val randomLength = (4..8).random()
            val generateString = randomIterator.generateRandomString(randomLength)
            mutableSet.add(generateString)
            trie.add(generateString)
        }
        val mutableSetIterator = mutableSet.iterator()
        val trieIterator = trie.iterator()

        while (trieIterator.hasNext()) {
            val a = mutableSetIterator.next()
            val b = toString(trieIterator.next())
            println("mutableA: $a and triB: $b")

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

    fun toString(str: String): String {
        val sb = StringBuilder()
        for (i in 0 until str.length - 1)
            sb.append(str[i])
        return sb.toString()
    }
}