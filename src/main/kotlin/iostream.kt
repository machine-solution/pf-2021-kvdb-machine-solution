

fun add() {
    println("Enter a key and value in format \"key -> value\"")
    val userInput = readLine()
    val element = KeyValueElement("","")
    if (userInput != null && element.assign(userInput)) {
        var log = database.addElement(element)
        if (log == "This key already in the database") {
            println("This key already in the database. Do you want to replace value for this key? (y == yes, n == no)")
            val userAns = readLine()
            if (userAns == "y")
                log = database.addElement(element, true)
        }
        println(log)
    } else {
        println("incorrect input.")
    }
}

fun delete() {
    println("Enter a key")
    val userInput = readLine()
    if (userInput != null) {
        val log = database.deleteElement(userInput)
        println(log)
    }
}

fun get() {
    println("Enter a key")
    val userInput = readLine()
    if (userInput != null) {
        val log = database.getElement(userInput)
        if (log != null)
            println(log)
        else
            println("This key not in a database.")
    }
}

fun replace() {
    println("Enter a key and value in format \"key -> value\"")
    val userInput = readLine()
    val element = KeyValueElement("","")
    if (userInput != null && element.assign(userInput)) {
        var log = database.addElement(element, true)
        println(log)
    } else {
        println("incorrect input.")
    }
}

fun userMeanCmd(cmd: String?): String {
    return if (cmd == null || ((cmd[0] != 'a') && (cmd[0] != 'd') && (cmd[0] != 'g') && (cmd[0] != 'r') && (cmd[0] != 'e')))
        " "
    else
        cmd[0].toString()
}