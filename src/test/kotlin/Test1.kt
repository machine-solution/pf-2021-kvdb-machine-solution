import java.io.File
import kotlin.test.*

internal class Test1 {

    @Test
    fun queriesTest() {
        val kvdb = KeyValueDataBase("testing_base_1")
        val data = kvdb.readData("testing_base_1_base$bootFile")
        assertEquals(KeyValueElement("1", "1"), data[0])
        assertEquals(KeyValueElement("2", "2 -> 2"), data[1])
        assertEquals("This key is already in the database.\nReplace it?", kvdb.addElement(KeyValueElement("1", "1 -> 1")))
        assertEquals("1", kvdb.getElement("1"))
        kvdb.deleteElement("1")
        assertEquals(null, kvdb.getElement("1"))
        kvdb.addElement(KeyValueElement("1", "1 -> 1"))
        assertEquals("1 -> 1", kvdb.getElement("1"))
        kvdb.addElement(KeyValueElement("1", "1"), true)
        assertEquals("1", kvdb.getElement("1"))
    }
}

internal class Test2 {

    @Test
    fun fileQueriesTest() {
        File("testing_base_2_base$incorrectInput").writeText("")
        File("testing_base_2_base$unconfirmedAddQueries").writeText("")
        val kvdb = KeyValueDataBase("testing_base_2")
        kvdb.fileAdd("testing_base_2_base/add_queries.txt")
        val incorrect = File("testing_base_2_base$incorrectInput").readLines()
        var unconfirmed = File("testing_base_2_base$unconfirmedAddQueries").readLines()
        assertEquals(4, incorrect.size)
        assertEquals(4, unconfirmed.size)
        kvdb.confirmAllAddQueries()
        unconfirmed = File("testing_base_2_base$unconfirmedAddQueries").readLines()
        assertEquals(0, unconfirmed.size)
        assertEquals("c", kvdb.getElement("a"))
    }
}

internal class Test3 {

    @Test
    fun fileQueriesTest() {
        File("testing_base_3_base$incorrectInput").writeText("")
        File("testing_base_3_base$unconfirmedAddQueries").writeText("")
        val kvdb = KeyValueDataBase("testing_base_3")
        kvdb.fileAdd("testing_base_3_base/add_queries.txt", ", ")
        val incorrect = File("testing_base_3_base$incorrectInput").readLines()
        val unconfirmed = File("testing_base_3_base$unconfirmedAddQueries").readLines()
        assertEquals(4, incorrect.size)
        assertEquals(0, unconfirmed.size)
        assertEquals("1", kvdb.getElement("1"))
        assertEquals("2", kvdb.getElement("2"))
        assertEquals("3", kvdb.getElement("3 "))
        assertEquals("5", kvdb.getElement("5 ,"))
    }
}

internal class Test4 {

    @Test
    fun fileQueriesTest() {
        // обнулить всё
        File("testing_base_4.1_base$incorrectInput").writeText("")
        File("testing_base_4.1_base$unconfirmedAddQueries").writeText("")
        File("testing_base_4.2_base$incorrectInput").writeText("")
        File("testing_base_4.2_base$unconfirmedAddQueries").writeText("")
        // тестируем автоматические функции
        databaseNames.add("testing_base_4.1")
        databaseNames.add("testing_base_4.2")
        databaseIsLoad["testing_base_4.1"] = false
        databaseIsLoad["testing_base_4.2"] = false

        changeDatabase("testing_base_4.1")
        addFromFile(
        "testing_base_4.1_base\\add_queries.txt",
        ", ")
        changeDatabase("testing_base_4.2")
        addFromFile(
        "testing_base_4.1_base\\add_queries.txt",
        " -> ")
        val incorrect2 = File("testing_base_4.2_base$incorrectInput").readLines()
        val unconfirmed2 = File("testing_base_4.2_base$unconfirmedAddQueries").readLines()
        assertEquals(6, incorrect2.size)
        assertEquals(0, unconfirmed2.size)
        assertEquals("7", get("7"))
        assertEquals("8 -> 8", get("8"))
    }
}
