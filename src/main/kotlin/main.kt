import java.io.File

// Пара ключ-значение
class KeyValueElement {
    // поля
    private var key: String = ""
    private var value: String = ""

    // конструктор по умолчанию
    constructor() {
        key = ""
        value = ""
    }
    // конструктор, создающий элемент по строке
    constructor(key: String, value: String) {
        this.key = key
        this.value = value
    }
    // конструктор, создающий элемент по строке
    constructor(string: String) {
        assign(string)
    }


    // Разбить строку данных на ключ и значение и сохранить
    private fun assign(string: String) {
        val pattern = " -> ".toRegex()
        val result = pattern.find(string)
        var separator = 0
        if (result?.range != null)
            separator = result.range.first
        else {
            key = ""
            value = ""
            return // Нужно скидывать нечитаему строку в мусорный файл
        }
        key = string.substring(0,separator)
        value = string.substring(separator + 4)
    }

    // toString() для записи в файл
    override fun toString(): String {
        return "$key -> $value"
    }
    // поэлементное сравнение
    override fun equals(other: Any?): Boolean = (other is KeyValueElement)
            && this.key == other.key
            && this.value == other.value
}

class KeyValueDataBase {
    var map: MutableMap<String,String> = mutableMapOf()

    // прочитать данные из указанного файла
    fun readData(filename : String) :List<KeyValueElement> {
        val data = File(filename).readLines()
        val elements = MutableList(0){KeyValueElement()}
        for (string in data)
            elements.add(KeyValueElement(string))
        return elements
    }

    fun writeData(filename: String, data: List<KeyValueElement>) {
        val file = File(filename)
        if (!file.exists())
            return
        if (data.isEmpty())
            return
        file.writeText("${data.first()}\n")
        for (it in 1 until data.size)
            file.appendText("${data[it]}\n")
    }
}

fun main() {
}
