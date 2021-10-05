import kotlinx.coroutines.*
import java.io.File
import kotlin.system.exitProcess

val database = KeyValueDataBase("default_base") // глобальная переменная базы данных

suspend fun autoSave() {
    while (true) {
        database.saveData()
        delay(10000) // Автосохранение каждые 10 секунд в асинхранном режиме
    }
}

suspend fun userInterface() {
    while (true) {
        delay(500) // прерываем на полсекунды для входа в сохранение
        println("Enter a command or write \"i\" for more information")
        when (userMeanCmd(readLine())) {
            "a" -> add()
            "d" -> delete()
            "g" -> get()
            "r" -> replace()
            "i" -> printAvailableCommands()
            "e" -> {
                database.saveData()
                exitProcess(0)
            }
            "fa" -> addFromFile()
            "fr" -> replaceFromFile()
        }
    }
}

fun main(): Unit = runBlocking {
    println("Hello, dear user!")
//    createWindow("Hello world!")
    launch {
        autoSave()
    }

    launch {
        userInterface()
    }
}

