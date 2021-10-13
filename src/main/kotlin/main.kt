import kotlinx.coroutines.*
import java.io.File

val databaseMap = mutableMapOf<String, KeyValueDataBase>()
val databaseNames = mutableSetOf<String>()
val databaseIsLoad = mutableMapOf<String, Boolean>()
var databaseId = ""

var database : KeyValueDataBase? = null // глобальная переменная базы данных
var basename = "null"

fun loadDatabases() {
    val names = File("basenames.txt").readLines()
    for (name in names) {
        databaseNames.add(name)
        databaseIsLoad[name] = false
    }
    databaseMap[names[0]] = KeyValueDataBase(names[0])
    databaseIsLoad[names[0]] = true
    databaseId = names[0]

    database = databaseMap[names[0]]!!
    basename = names[0]
}

fun saveDatabases() {
    val file = File("basenames.txt")
    file.writeText("")
    for (name in databaseNames) {
        file.appendText(name + "\n")
    }
}

suspend fun autoSave() {
    while (true) {
        database?.saveData()
        delay(10000) // Автосохранение каждые 10 секунд в асинхранном режиме
        saveDatabases()
        delay(10000) // Автосохранение каждые 10 секунд в асинхранном режиме
    }
}

// проверка корректности key, value, basename, delimiter
fun isCorrectString(string: String): Boolean {
    return string.isNotEmpty()
}

// проверка корректности path
fun isCorrectPath(path: String): Boolean {
    return File(path).isFile
}

fun add(key: String, value: String): String {
    val element = KeyValueElement(key, value)
    return database?.addElement(element) ?: "database isn't chosen"
}

fun delete(key: String): String {
    return database?.deleteElement(key) ?: "database isn't chosen"
}

fun get(key: String): String {
    return if (database == null)
        "database isn't chosen"
    else
        database?.getElement(key) ?: "This key not in a database"
}

fun replace(key: String, value: String): String {
    val element = KeyValueElement(key, value)
    return database?.addElement(element, true) ?: "database isn't chosen"
}

fun addFromFile(path: String, delimiter: String): String {
    return database?.fileAdd(path, delimiter) ?: "database isn't chosen"
}

fun replaceFromFile(path: String, delimiter: String): String {
    return database?.fileReplace(path, delimiter) ?: "database isn't chosen"
}

fun deleteFromFile(path: String): String {
    return database?.fileDelete(path) ?: "database isn't chosen"
}

fun confirmAddQueries(confirm: Boolean = true): String {
    return database?.confirmAllAddQueries(confirm) ?: "database isn't chosen"
}

fun changeDatabase(name: String): String {
    database?.saveData()
    return if (name in databaseNames) {
        if (!databaseIsLoad[name]!!) {
            databaseMap[name] = KeyValueDataBase(name)
            databaseIsLoad[name] = true
        }
        database = databaseMap[name]!!
        basename = name
        "The database was changed successfully"
    }
    else "This database doesn't exist"
}

fun createDatabase(name: String): String {
    return if (name in databaseNames)
        "This database already exists"
    else {
        File(name + "_base/").mkdir()
        File(name + "_base/data_base.txt").createNewFile()
        File(name + "_base/incorrect_input.txt").createNewFile()
        File(name + "_base/unconfirmed_add_queries.txt").createNewFile()
        databaseMap[name] = KeyValueDataBase(name)
        databaseIsLoad[name] = true
        databaseNames.add(name)

        database = databaseMap[name]!!
        basename = name
        "The database was created successfully"
    }
}

fun deleteDatabase(name: String): String {
    return if (name in databaseNames && name != basename) {
//        File(name + "_base/data_base.txt").delete()
//        File(name + "_base/incorrect_input.txt").delete()
//        File(name + "_base/unconfirmed_add_queries.txt").delete()
        File(name + "_base/").deleteRecursively()
        databaseMap.remove(name)
        databaseIsLoad.remove(name)
        databaseNames.remove(name)


        "The database was deleted successfully"
    }
    else return if (name == basename) "This database is open now"
    else "This database doesn't exist"

}

//suspend fun userInterface() {
//    while (true) {
//        delay(500) // прерываем на полсекунды для входа в сохранение
//        println("Enter a command or write \"i\" for more information")
//        when (userMeanCmd(readLine())) {
//            "a" -> add()
//            "d" -> delete()
//            "g" -> get()
//            "r" -> replace()
//            "i" -> printAvailableCommands()
//            "e" -> {
//                database.saveData()
//                exitProcess(0)
//            }
//            "fa" -> addFromFile()
//            "fr" -> replaceFromFile()
//        }
//    }
//}

//suspend fun ui(): Unit = runBlocking {
//    while (true) {
//        delay(500)
//        gui()
//    }
//}

fun main(): Unit = runBlocking {

    gui()

    // Пока не работает, так как функция gui не прерывается
    launch {
        autoSave()
    }
}

