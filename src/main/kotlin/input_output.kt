// import java.io.File

// Имена файлов ввода - вывода
const val incorrectInput = "/incorrect_input.txt"
const val unconfirmedAddQueries = "/unconfirmed_add_queries.txt"
const val bootFile = "/data_base.txt"

//
//fun add() {
//    println("Enter a key and value in format \"key -> value\"")
//    val userInput = readLine()
//    val element = KeyValueElement("","")
//    if (userInput != null && element.assign(userInput)) {
//        var log = database.addElement(element)
//        if (log == "This key already in the database") {
//            println("This key already in the database. Do you want to replace value for this key? (y == yes, n == no)")
//            val userAns = readLine()
//            if (userAns == "y")
//                log = database.addElement(element, true)
//            else
//                log = "Command is canceled"
//        }
//        println(log)
//    } else {
//        println("incorrect input")
//    }
//}
//
//fun addFromFile() {
//    val path = getCorrectPath("resource file")
//    val log = database.fileAdd(path)
//    println(log)
//}
//
//fun delete() {
//    println("Enter a key")
//    val userInput = readLine()
//    if (userInput != null) {
//        val log = database.deleteElement(userInput)
//        println(log)
//    }
//}
//
//fun get() {
//    println("Enter a key")
//    val userInput = readLine()
//    if (userInput != null) {
//        val log = database.getElement(userInput)
//        if (log != null)
//            println(log)
//        else
//            println("This key not in a database")
//    }
//}
//
//fun replace() {
//    println("Enter a key and value in format \"key -> value\"")
//    val userInput = readLine()
//    val element = KeyValueElement("","")
//    if (userInput != null && element.assign(userInput)) {
//        val log = database.addElement(element, true)
//        println(log)
//    } else {
//        println("incorrect input")
//    }
//}
//
//fun replaceFromFile() {
//    val path = getCorrectPath("resource file")
//    val log = database.fileReplace(path)
//    println(log)
//}
//
//// список всех команд
//fun printAvailableCommands() {
//    println("a -- add element <key -> value>")
//    println("d -- delete element <key>")
//    println("g -- get element <key>")
//    println("r -- replace element <key -> value>")
//    println("e -- exit program")
//    println("fa -- add elements of file -> <file>")
//    println("fr -- replace elements of file <file>")
//}

// Понимаем какую команду имел ввиду пользователь
//fun userMeanCmd(cmd: String?): String {
//    if (cmd == null || ((cmd[0] != 'a') && (cmd[0] != 'd') && (cmd[0] != 'g') && (cmd[0] != 'r') &&
//                (cmd[0] != 'e')  && (cmd[0] != 'i')  && (cmd[0] != 'f')))
//        return " "
//    else if (cmd[0] == 'f') {
//        if (cmd.length <= 1 || ((cmd[1] != 'a') && (cmd[1] != 'd') && (cmd[1] != 'g') && (cmd[1] != 'r')))
//            return " "
//        else
//            return cmd.substring(0,2)
//    } else {
//        return cmd[0].toString()
//    }
//    return " " // если я забыл какой-то случай
//}
//
//// Интерактивно получает от пользователя корректный путь на файл
//fun getCorrectPath(fileAlias: String): String? {
//    println("Enter the path of $fileAlias")
//    var path = readLine()
//    while (path == null || !File(path).isFile) {
//        if (path == null || path.isEmpty())
//            return null
//        println("Unable to convert file to text. Please, enter the path of another file")
//        path = readLine()
//    }
//    return path.toString()
//}
