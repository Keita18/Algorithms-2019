@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import java.io.IOException
import java.io.PrintWriter

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 *
 * Time Complexity: O(nlogn)
 * Memory Complexity: Θ(n), n - the number of lines
 */
fun sortTimes(inputName: String, outputName: String) {

    val text = File(inputName).readLines()
        // linear time & memory
        .map {
            require(Regex("""^\d\d:\d\d:\d\d (AM|PM)""").matches(it)) { it }
            val time = it.split(" ")[0]
            val types = it.split(" ")[1]
            val result = timeStrToSeconds(time, types)
            result to it
        }
        //O(nlogn) time & Θ(1) memory
        .sortedBy { it.first }
        // linear time
        .joinToString("\n") { it.second }
    File(outputName).writeText(text)

}

//Time Complexity: O(1)
// Memory Complexity: Θ(1)
fun timeStrToSeconds(str: String, types: String): Int {
    val parts = str.split(":").toTypedArray() // size = 3
    if (types == "AM") { // 12am = 00ч
        val first = parts[0].toInt()
        if (first == 12)
            parts[0] = "0"
    }

    var result = 0
    //O(3) {hours , minutes, seconds} -> O(1)
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
        if (types == "PM") { //1pm = 13ч
            if (parts.first().toInt() != 12)
                result += 12 * 3600
        }
    }
    return result
}

/**
 * Сортировка адресов
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 *
 * Time Complexity: O(n * P) - n number of line in inputName , P number of person per address
 * or O(nlogn) if P << n
 * Memory Complexity: O(n)
 */
fun sortAddresses(inputName: String, outputName: String) {

    val text = File(inputName).readLines()
        // linear time & memory
        .map {
            require(Regex("""(\S+ \S+) - (\S+) (\d+)""").matches(it)) { it }
            it.split(" - ")[1] to it.split(" - ")[0]
        }
        // O(nlogn) time & O(1) memory
        .sortedWith(compareBy({ it.first.split(" ")[0] }, { it.first.split(" ")[1].toInt() }, { it.second }))
        //linear time and probably linear memory
        .groupBy { it.first }
        //linear time * probably P - number of person in list
        .mapValues { (_, value) -> value.joinToString(", ") { it.second } }
        // linear time & memory
        .map { it.key + " - " + it.value }
        // linear
        .joinToString("\n")
    File(outputName).writeText(text)
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 *
 *
 * Time Complexity: O(n) n - number of line
 * Memory complexity: O(1) {one table with constant size}
 */
fun sortTemperatures(inputName: String, outputName: String) {
    val temperatures = arrayOfNulls<Int>(7731)
    try {
        File(inputName).readLines().forEach {
            require(it.toDouble() <= 500 && it.toDouble() >= -273)
            val number = (it.toDouble() * 10).toInt()

            if (temperatures[number + 2730] == null)
                temperatures[number + 2730] = 1
            else temperatures[number + 2730] = temperatures[number + 2730]!! + 1
        }
    } catch (e: IOException) {
        println("Incorrect format")
    }

    try {
        PrintWriter(outputName).use { pw ->
            for (i in 0..7730) // linear times n
                if (temperatures[i] != null)
                    for (j in 1..temperatures[i]!!) // linear times m
                        pw.println(((i - 2730)) / 10.0)
        }
    } catch (e: IOException) {
        println("Error")
    }
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 *
 *
 * Time Complexity: Θ(n)
 * Memory Complexity: Θ(n)
 */
fun sortSequence(inputName: String, outputName: String) {

    val numbers = File(inputName).readLines()

    val count = numbers.groupBy { it.toInt() }.map { it.key to it.value.size }.toMap() // linear

    val maxValue = count.values.max() ?: 0  // linear
    val minValue = count.filter { it.value == maxValue }.keys.min() ?: 0  // linear

    val list = numbers.filter { it.toInt() != minValue }.toMutableList() // linear
    repeat(maxValue) {
        list.add(minValue.toString())
    }
    val text = list.joinToString("\n") // linear
    File(outputName).writeText(text)
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 *
 *
 * Time Complexity: Θ(n), n - length of the second array
 * Memory Complexity: Θ(1)
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    var secondIndex = first.size
    var firstIndex = 0

    for (it in second.indices) {
        if (
            secondIndex >= second.size ||
            firstIndex < first.size && first[firstIndex] <= second[secondIndex]!!
        ) {
            second[it] = first[firstIndex++]
        } else {
            second[it] = second[secondIndex++]
        }
    }
}
