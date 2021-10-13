import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlin.system.exitProcess

enum class Arg {
    NULL,
    KEY_VALUE,
    KEY,
    FILE_DELIMITER,
    BASENAME,
}

enum class Command {
    NULL,
    ADD,
    DELETE,
    GET,
    REPLACE,
    FILE_ADD,
    FILE_REPLACE,
    FILE_DELETE,
    CHANGE_DATABASE,
    CREATE_DATABASE,
    DELETE_DATABASE,
    CONFIRM,
}

@Composable
fun simpleButton(colorful: Boolean, name: String, onClick : () -> Unit) {
    val color = if (colorful)
        ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.Black)
    else
        ButtonDefaults.buttonColors(backgroundColor = Color.Blue, contentColor = Color.White)
    Button(colors = color, onClick = onClick) {Text(name)}
}

fun exit() {
    database?.saveData()
    saveDatabases()
    exitProcess(0)
}

fun gui() = application {
    loadDatabases()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Key-Value database",
        state = rememberWindowState(width = 1000.dp, height = 700.dp)
    ) {
        val key = mutableStateOf("")
        val value = mutableStateOf("")
        val file = mutableStateOf("")
        val delimiter = mutableStateOf(" -> ")
        val newBasename = mutableStateOf("")

        val args = mutableStateOf(Arg.NULL)
        val cmd = mutableStateOf(Command.NULL)
        val correctInput = mutableStateOf(true)

        val log = mutableStateOf("")
/* нереализованная фича выпадающего списка
        val expanded = mutableStateOf(false)
        val selectedText = mutableStateOf("")

        val textFieldSize = mutableStateOf(Size.Zero)

        val icon = if (expanded.value)
            Icons.Filled.ArrowDropDown
        else
            Icons.Filled.ArrowDropDown
 */
        MaterialTheme {
            // список кнопок на экране
            Column(
                Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)
            ) {
                simpleButton(cmd.value == Command.ADD, "Add") {
                    args.value = Arg.KEY_VALUE
                    cmd.value = Command.ADD
                }
                simpleButton(cmd.value == Command.GET, "Get") {
                    args.value = Arg.KEY
                    cmd.value = Command.GET
                }
                simpleButton(cmd.value == Command.DELETE, "Delete") {
                    args.value = Arg.KEY
                    cmd.value = Command.DELETE
                }
                simpleButton(cmd.value == Command.REPLACE, "Replace") {
                    args.value = Arg.KEY_VALUE
                    cmd.value = Command.REPLACE
                }
                simpleButton(cmd.value == Command.FILE_ADD, "File Add") {
                    args.value = Arg.FILE_DELIMITER
                    cmd.value = Command.FILE_ADD
                }
                simpleButton(cmd.value == Command.FILE_DELETE, "File Delete") {
                    args.value = Arg.FILE_DELIMITER
                    cmd.value = Command.FILE_DELETE
                }
                simpleButton(cmd.value == Command.FILE_REPLACE, "File Replace") {
                    args.value = Arg.FILE_DELIMITER
                    cmd.value = Command.FILE_REPLACE
                }
                simpleButton(cmd.value == Command.CHANGE_DATABASE, "Change Database") {
                    args.value = Arg.BASENAME
                    cmd.value = Command.CHANGE_DATABASE
                }
                simpleButton(cmd.value == Command.CREATE_DATABASE, "Create Database") {
                    args.value = Arg.BASENAME
                    cmd.value = Command.CREATE_DATABASE
                }
                simpleButton(cmd.value == Command.DELETE_DATABASE, "Delete Database") {
                    args.value = Arg.BASENAME
                    cmd.value = Command.DELETE_DATABASE
                }
                simpleButton(false, "Exit") {
                    exit()
                }
            }
            // список аргументов команды или ответ на запрос к базе данных
            when (args.value) {
                Arg.KEY_VALUE -> {
                    Column(
                        Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)
                    ) {
                        TextField(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            value = key.value,
                            onValueChange = {
                                key.value = it
                            },
                            label = { Text("Enter a key") }
                        )
                        TextField(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            value = value.value,
                            onValueChange = {
                                value.value = it
                            },
                            label = { Text("Enter a value") }
                        )
                        correctInput.value = isCorrectString(key.value) && isCorrectString(value.value)
                    }
                }
                Arg.KEY -> {
                    Column(
                        Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)
                    ) {
                        TextField(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            value = key.value,
                            onValueChange = {
                                key.value = it
                            },
                            label = { Text("Enter a key") }
                        )
                    }
                    correctInput.value = isCorrectString(key.value)
                }
                Arg.FILE_DELIMITER -> {
                    Column(
                        Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)
                    ) {
                        TextField(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            value = file.value,
                            onValueChange = {
                                file.value = it
                            },
                            label = { Text("Enter a file path") }
                        )
                        TextField(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            value = delimiter.value,
                            onValueChange = {
                                delimiter.value = it
                            },
                            label = { Text("Enter a delimiter") }
                        )
                    }
                    correctInput.value = isCorrectPath(file.value) && isCorrectString(delimiter.value)
                }
                Arg.BASENAME -> {
                    Column(
                        Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)
                    ) {
                        TextField(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            value = newBasename.value,
                            onValueChange = {
                                newBasename.value = it
                            },
                            label = { Text("Enter a basename") }
                        )
                    }
                    correctInput.value = isCorrectString(newBasename.value)
/* Нереализованная фича выпадающего списка
                        TextField(
                            value = selectedText.value,
                            onValueChange = {selectedText.value = it },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .onGloballyPositioned { coordinates ->
                                    //This value is used to assign to the DropDown the same width
                                    textFieldSize.value = coordinates.size.toSize()
                                },
                            label = {Text("Enter a basename")},
                            trailingIcon = {
                                Icon(icon,"contentDescription",
                                    Modifier.clickable { expanded.value = !expanded.value })
                            }
                        )
                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current){textFieldSize.value.width.toDp()})
                        ) {
                            databaseNames.forEach { label ->
                                DropdownMenuItem(onClick = {
                                    selectedText.value = label
                                }) {
                                    Text(text = label)
                                }
                            }
                        }
 */
                }
                else ->
                    Column(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            log.value,
                            modifier = Modifier.fillMaxWidth(1f),
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                            color = Color(red = 0x00, green = 0x00, blue = 0x00, alpha = 0xFF)
                        )
                        if (cmd.value == Command.CONFIRM) {
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.Green,
                                    contentColor = Color.Black
                                ),
                                onClick = {
                                    log.value = confirmAddQueries(true)
                                    cmd.value = Command.NULL
                                }
                            ) {
                                Text("YES")
                            }
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.Red,
                                    contentColor = Color.Black
                                ),
                                onClick = {
                                    log.value = confirmAddQueries(false)
                                    cmd.value = Command.NULL
                                }
                            ) {
                                Text("NO")
                            }
                        }
                    }
            }
            // большая зелёная кнопка выполнения команды
            if (correctInput.value && args.value != Arg.NULL)
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Green,
                            contentColor = Color.Black
                        ),
                        onClick = {
                            log.value = when (cmd.value) {
                                Command.ADD -> add(key.value, value.value)
                                Command.GET -> get(key.value)
                                Command.DELETE -> delete(key.value)
                                Command.REPLACE -> replace(key.value, value.value)
                                Command.FILE_ADD -> addFromFile(file.value, delimiter.value)
                                Command.FILE_REPLACE -> replaceFromFile(file.value, delimiter.value)
                                Command.FILE_DELETE -> deleteFromFile(file.value)
                                Command.CHANGE_DATABASE -> changeDatabase(newBasename.value)
                                Command.CREATE_DATABASE -> createDatabase(newBasename.value)
                                Command.DELETE_DATABASE -> deleteDatabase(newBasename.value)
                                Command.CONFIRM -> "nothing" // nothing
                                Command.NULL -> ""
                            }
                            key.value = ""
                            value.value = ""
                            file.value = ""
                            delimiter.value = " -> "
                            newBasename.value = ""
                            args.value = Arg.NULL
                            if ((log.value.length >= 13 && log.value.substring(0,13) == "Query contain") ||
                                    log.value == "This key is already in the database.\nReplace it?")
                                cmd.value = Command.CONFIRM
                            else
                                cmd.value = Command.NULL
                            correctInput.value = true
                            confirmAddQueries(false)
                        }
                    ) {
                        Text("DO!")
                    }
                }
            // показывает имя текущей базы данных
            Text(
                text = "< $basename >",
                modifier = Modifier.fillMaxWidth(1f),
                fontSize = 22.sp,
                textAlign = TextAlign.Right,
                color = Color(red = 0x00, green = 0xF0, blue = 0x90, alpha = 0xFF)
            )
        }
    }
}


