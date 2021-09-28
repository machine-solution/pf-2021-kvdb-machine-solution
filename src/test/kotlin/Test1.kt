import java.io.File
import kotlin.test.*

fun toTestIO(num: Int = 1) {
    bootFile = "testing/data_base_test$num.txt"
    incorrectInput = "testing/incorrect_input_test$num.txt"
    unconfirmedAddQuery = "testing/unconfirmed_add_query_test$num.txt"
}

fun toRealizeIO() {
    incorrectInput = "incorrect_input.txt"
    unconfirmedAddQuery = "unconfirmed_add_query.txt"
    bootFile = "data_base.txt"
}

internal class Test1 {
    @BeforeTest
    fun beforeTest() {
        toTestIO(1)
    }

    @AfterTest
    fun afterTest() {
        toRealizeIO()
    }

    @Test
    fun queriesTest() {
        val kvdb = KeyValueDataBase()
        val data = kvdb.readData(bootFile)
        assertEquals(KeyValueElement("1", "1"), data[0])
        assertEquals(KeyValueElement("2", "2 -> 2"), data[1])
        assertEquals("This key already in the database", kvdb.addElement(KeyValueElement("1", "1 -> 1")))
        assertEquals("1", kvdb.getElement("1"))
        kvdb.deleteElement("1")
        assertEquals(null, kvdb.getElement("1"))
        kvdb.addElement(KeyValueElement("1", "1 -> 1"))
        assertEquals("1 -> 1", kvdb.getElement("1"))
        kvdb.addElement(KeyValueElement("1", "1"), true)
        assertEquals("1", kvdb.getElement("1"))
        kvdb.saveData()
    }
}

internal class Test2 {
    @BeforeTest
    fun beforeTest() {
        toTestIO(2)
    }

    @AfterTest
    fun afterTest() {
        toRealizeIO()
    }

    @Test
    fun fileQueriesTest() {
        File(incorrectInput).writeText("")
        File(unconfirmedAddQuery).writeText("")
        val kvdb = KeyValueDataBase()
        kvdb.fileAdd("testing/add_queries2.txt")
        val incorrect = File(incorrectInput).readLines()
        var unconfirmed = File(unconfirmedAddQuery).readLines()
        assertEquals(4, incorrect.size)
        assertEquals(4, unconfirmed.size)
        kvdb.confirmAllAddQueries()
        unconfirmed = File(unconfirmedAddQuery).readLines()
        assertEquals(0, unconfirmed.size)
        assertEquals("c", kvdb.getElement("a"))
    }
}
