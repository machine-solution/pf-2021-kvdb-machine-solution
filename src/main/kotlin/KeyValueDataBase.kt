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
    constructor(string: String, file: File = File("incorrect_global"), delimiter: String = " -> ") {
        assign(string, delimiter, file)
    }


    // Разбить строку данных на ключ и значение и сохранить
    // delimiter - разделитель между ключём и значением может быть пользовательский
    // incorrect - файл, в который сохраняются некорректные запросы
    private fun assign(string: String, delimiter: String = " -> ", incorrect: File = File("incorrect_global")): Boolean {
        val pattern = delimiter.toRegex()
        val result = pattern.find(string)
        val separator: Int
        if (result?.range != null)
            separator = result.range.first
        else {
            key = ""
            value = ""
            incorrect.appendText("$string\n") // Нужно скидывать нечитаемую строку в мусорный файл
            return false
        }
        key = string.substring(0,separator)
        value = string.substring(separator + delimiter.length)
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

class KeyValueDataBase// При создании загружает данные из bootFile
    (direct : String) {
    private var map: MutableMap<String,String> = mutableMapOf()
    private var directory: String = direct + "_base"

    init {
        val elements = readData(directory + bootFile)
        for (element in elements)
            addElement(element)
    }

    // промежуточное сохранение
    fun saveData() {
        val elements = mutableListOf<KeyValueElement>()
        for (element in map)
            elements.add(KeyValueElement(element.key, element.value))
        writeData(directory + bootFile, elements)
    }

    // прочитать данные из указанного файла
    // с пользовательским разделителем
    fun readData(filename : String, delimiter: String = " -> ") :List<KeyValueElement> {
        val data = File(filename).readLines()
        val elements = MutableList(0){KeyValueElement()}
        for (string in data) {
            val element = KeyValueElement(string, File(directory + incorrectInput), delimiter)
            if (element != KeyValueElement("","")) // проверяем корректность ввода элемента
                elements.add(element)
        }
        return elements
    }

    // прочитать только ключи из указанного файла
    private fun readDataKeys(filename : String) :List<String> {
        val data = File(filename).readLines()
        val elements = MutableList(0){""}
        for (string in data) {
            elements.add(string)
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
    fun fileAdd(filename: String, delimiter: String = " -> "): String {
        val data = readData(filename, delimiter) // некорректные запросы автоматически попали в incorrect_input.txt
        for (element in data) {
            // запросы, пытающиеся сделать замену существующих элементов попадают в unconfirmed_add_query.txt
            if (addElement(element) == "This key already in the database")
                File(directory + unconfirmedAddQueries).appendText("$element\n")
        }
        return "Success" // добавить логи в дальнейшем
    }

    // выполняет replaceElement (т.е. addElement(replace = true)) для всех запросов в файле
    fun fileReplace(filename: String, delimiter: String = " -> "): String {
        val data = readData(filename, delimiter) // некорректные запросы автоматически попали в incorrect_input.txt
        for (element in data) {
            addElement(element, true)
        }
        return "Success" // добавить логи в дальнейшем
    }

    // выполняет deleteElement для всех запросов в файле
    fun fileDelete(filename: String): String {
        val data = readDataKeys(filename) // некорректные запросы автоматически попали в incorrect_input.txt
        for (element in data) {
            deleteElement(element)
        }
        return "Success" // добавить логи в дальнейшем
    }

    fun confirmAllAddQueries() {
        fileReplace(directory + unconfirmedAddQueries)
        File(directory + unconfirmedAddQueries).writeText("")
    }

}