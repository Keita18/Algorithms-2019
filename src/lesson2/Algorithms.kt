@file:Suppress("UNUSED_PARAMETER")

package lesson2

import java.io.File
import kotlin.math.sqrt


/**
 * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
 * Простая
 *
 * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
 * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
 *
 * 201
 * 196
 * 190
 * 198
 * 187
 * 194
 * 193
 * 185
 *
 * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
 * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
 * Вернуть пару из двух моментов.
 * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
 * Например, для приведённого выше файла результат должен быть Pair(3, 4)
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun optimizeBuyAndSell(inputName: String): Pair<Int, Int> {
    TODO()
}

/**
 * Задача Иосифа Флафия.
 * Простая
 *
 * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
 *
 * 1 2 3
 * 8   4
 * 7 6 5
 *
 * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
 * Человек, на котором остановился счёт, выбывает.
 *
 * 1 2 3
 * 8   4
 * 7 6 х
 *
 * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
 * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
 *
 * 1 х 3
 * 8   4
 * 7 6 Х
 *
 * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
 *
 * 1 Х 3
 * х   4
 * 7 6 Х
 *
 * 1 Х 3
 * Х   4
 * х 6 Х
 *
 * х Х 3
 * Х   4
 * Х 6 Х
 *
 * Х Х 3
 * Х   х
 * Х 6 Х
 *
 * Х Х 3
 * Х   Х
 * Х х Х
 *
 * Общий комментарий: решение из Википедии для этой задачи принимается,
 * но приветствуется попытка решить её самостоятельно.
 */
fun josephTask(menNumber: Int, choiceInterval: Int): Int {
    TODO()
}


/**
 * Наибольшая общая подстрока.
 * Средняя
 *
 * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
 * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
 * Если общих подстрок нет, вернуть пустую строку.
 * При сравнении подстрок, регистр символов *имеет* значение.
 * Если имеется несколько самых длинных общих подстрок одной длины,
 * вернуть ту из них, которая встречается раньше в строке first.
 */
//Time Complexity: Θ(n)
//Memory Complexity: Θ(n)
fun longestCommonSubstring(first: String, second: String): String {
    val str = StringBuilder()
    var maxSubString = ""
    for (j in first.indices) {
        for (i in j until first.length) {
            str.append(first[i])
            if (!second.contains(str)) {
                str.deleteCharAt(str.length - 1)
                break
            }
        }
        if (str.length > maxSubString.length) {
            maxSubString = str.toString()
        }
        str.delete(0, str.length)
    }
    return maxSubString
}

/**
 * Число простых чисел в интервале
 * Простая
 *
 * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
 * Если limit <= 1, вернуть результат 0.
 *
 * Справка: простым считается число, которое делится нацело только на 1 и на себя.
 * Единица простым числом не считается.
 */
fun calcPrimesNumber(limit: Int): Int {
    fun isPrime(n: Int): Boolean {
        if (n < 2) return false
        if (n == 2) return true
        if (n % 2 == 0) return false
        for (m in 3..sqrt(n.toDouble()).toInt() step 2) {
            if (n % m == 0) return false
        }
        return true
    }

    var primeNum = 0
    for (i in 1..limit)
        if (isPrime(i))
            primeNum++
    return primeNum
}

/**
 * Балда
 * Сложная
 *
 * В файле с именем inputName задана матрица из букв в следующем формате
 * (отдельные буквы в ряду разделены пробелами):
 *
 * И Т Ы Н
 * К Р А Н
 * А К В А
 *
 * В аргументе words содержится множество слов для поиска, например,
 * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
 *
 * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
 * и вернуть множество найденных слов. В данном случае:
 * ТРАВА, КРАН, АКВА, НАРТЫ
 *
 * И т Ы Н     И т ы Н
 * К р а Н     К р а н
 * А К в а     А К В А
 *
 * Все слова и буквы -- русские или английские, прописные.
 * В файле буквы разделены пробелами, строки -- переносами строк.
 * Остальные символы ни в файле, ни в словах не допускаются.
 */
//Time Complexity: O(n*m), n - number letters , m - number words
fun baldaSearcher(inputName: String, words: Set<String>): Set<String> {

    val result = mutableListOf<String>()
    var i = 0

    while (i < words.size) {
        val str = words.toList()[i]
        val r = initialisation(inputName, str.toUpperCase())
        if (r != "")
            result.add(r)
        i++
    }
    return result.toSet()
}
//Time Complexity: O(n), n - number letters
// Memory Complexity: Θ(n)
fun initialisation(inputName: String, words: String): String {

    val map = File(inputName).readLines()
        .map { "| ${it.toUpperCase()} |".split(" ").toTypedArray() }.toMutableList()

    val protection = mutableListOf<String>()
    for (i in map[0].indices)
        protection.add("|")

    map.add(0, protection.toTypedArray())
    map.add(protection.toTypedArray())

    val array = map.toTypedArray()

    var result = false

    for (i in array.indices) {
        for (j in array[i].indices)
            if (array[i][j] == words[0].toString()) {
                result = searchPath(array, j, i, words)
                if (result)
                    break
            }
        if (result)
            break
    }
    return if (result) words else ""
}
//depth first search
fun searchPath(maze: Array<Array<String>>, x: Int, y: Int, words: String): Boolean {

    val str = StringBuilder()
    str.append(words)
    var mot = ""
    if (mot == words) {
        return true
    }

    if (maze[y][x] == str.first().toString()) {
        mot += str.first()
        maze[y][x] = "2"
        str.deleteCharAt(0)

        // System.out.println(Arrays.deepToString(maze))
        var dx = -1
        var dy = 0
        if (searchPath(maze, x + dx, y + dy, str.toString())) {
            return true
        }

        dx = 1
        dy = 0
        if (searchPath(maze, x + dx, y + dy, str.toString())) {
            return true
        }

        dx = 0
        dy = -1
        if (searchPath(maze, x + dx, y + dy, str.toString())) {
            return true
        }

        dx = 0
        dy = 1
        if (searchPath(maze, x + dx, y + dy, str.toString())) {
            return true
        }
    }
    return false
}