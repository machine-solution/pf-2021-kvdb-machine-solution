import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import java.io.File
import kotlin.concurrent.thread
import kotlin.system.exitProcess

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

//fun changeDatabase(name: String): String {
//    if (name in databaseNames) {
//        if (!databaseIsLoad[name]!!) {
//            databaseMap[name] = KeyValueDataBase(name)
//            databaseIsLoad[name] = true
//        }
//        database = databaseMap[name]!!
//        basename = name
//        return "The database was changed successfully"
//    }
//    else return "This database not exists"
//}

suspend fun autoSave() {
    while (true) {
        database?.saveData()
        delay(10000) // Автосохранение каждые 10 секунд в асинхранном режиме
        saveDatabases()
        delay(10000) // Автосохранение каждые 10 секунд в асинхранном режиме
    }
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

