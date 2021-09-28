import java.io.File


// Пара ключ-значение
class KeyValueElement {
    // поля
    var key: String = ""
    var value: String = ""

    // конструктор по умолчанию
    constructor() {
        key = ""
        value = ""
    }
    // конструктор, создающий элемент по ключу и значению
    constructor(key: String, value: String) {
        this.key = key
        this.value = value
    }
    // конструктор, создающий элемент по строке
    constructor(string: String) {
        assign(string)
    }


    // Разбить строку данных на ключ и значение и сохранить
    fun assign(string: String): Boolean {
        val pattern = " -> ".toRegex()
        val result = pattern.find(string)
        val separator: Int
        if (result?.range != null)
            separator = result.range.first
        else {
            key = ""
            value = ""
            File(incorrectInput).appendText("$string\n") // Нужно скидывать нечитаемую строку в мусорный файл
            return false
        }
        key = string.substring(0,separator)
        value = string.substring(separator + 4)
        return true
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
    private var map: MutableMap<String,String> = mutableMapOf()

    // При создании загружает данные из bootFile
    constructor() {
        val elements = readData(bootFile)
        for (element in elements)
            addElement(element)
    }

    // промежуточное сохранение
    fun saveData() {
        val elements = mutableListOf<KeyValueElement>()
        for (element in map)
            elements.add(KeyValueElement(element.key, element.value))
        writeData(bootFile, elements)
    }

    // прочитать данные из указанного файла
    fun readData(filename : String) :List<KeyValueElement> {
        val data = File(filename).readLines()
        val elements = MutableList(0){KeyValueElement()}
        for (string in data) {
            val element = KeyValueElement(string)
            if (element != KeyValueElement("","")) // проверяем корректность ввода элемента
                elements.add(element)
        }
        return elements
    }

    // записать данные в указанный файл
    private fun writeData(filename: String, data: List<KeyValueElement>) {
        val file = File(filename)
        if (!file.exists())
            return
        if (data.isEmpty())
            return
        file.writeText("${data.first()}\n")
        for (it in 1 until data.size)
            file.appendText("${data[it]}\n")
    }

    // добавить элемент
    fun addElement(element: KeyValueElement, replace: Boolean = false): String {
        if (map.containsKey(element.key)) { // такой элемент уже существует
            if (replace) {
                map[element.key] = element.value // всё равно заменить
                return "Element was replaced successfully"
            }
            return "This key already in the database"
        } else {
            map[element.key] = element.value
            return "Element was added successfully"
        }
    }

    // удалить элемент
    fun deleteElement(key: String): String{
        return if (map.containsKey(key)) {
            map.remove(key)
            "Element was deleted successfully"
        } else {
            "This key already not in database"
        }
    }

    // Получить элемент, если он есть
    // и вернуть null в обратном случае
    fun getElement(key: String): String? {
        return if (map.containsKey(key)) {
            map[key].toString()
        } else {
            null
        }
    }

    // выполняет addElement для всех запросов в файле
    fun fileAdd(filename: String?) {
        var path = filename
        if (path == null)
            path = getCorrectPath("resource file")
        val data = readData(path) // некорректные запросы автоматически попали в incorrect_input.txt
        for (element in data) {
            // запросы, пытающиеся сделать замену существующих элементов попадают в unconfirmed_add_query.txt
            if (addElement(element) == "This key already in the database")
                File(unconfirmedAddQuery).appendText("$element\n")
        }
    }

    // выполняет replaceElement (т.е. addElement(replace = true)) для всех запросов в файле
    fun fileReplace(filename: String?) {
        var path = filename
        if (path == null)
            path = getCorrectPath("resource file")
        val data = readData(path) // некорректные запросы автоматически попали в incorrect_input.txt
        for (element in data) {
            addElement(element, true)
        }
    }

    fun confirmAllAddQueries() {
        fileReplace(unconfirmedAddQuery)
        File(unconfirmedAddQuery).writeText("")
    }

}

val database = KeyValueDataBase() // глобальная переменная базы данных

fun main() {
    while (true) {
        println("Enter a command (a == add, d == delete, g == get, r == replace, e == exit application)")
        when (userMeanCmd(readLine())) {
            "a" -> add()
            "d" -> delete()
            "g" -> get()
            "r" -> replace()
            "e" -> {
                database.saveData()
                return
            }
        }
    }
}
