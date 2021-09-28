import kotlin.test.*

fun toTestIO() {
    bootFile = "testing/data_base_test.txt"
    incorrectInput = "testing/incorrect_input_test.txt"
    unconfirmedAddQuery = "testing/unconfirmed_add_query_test.txt"
}

fun toRealizeIO() {
    incorrectInput = "incorrect_input.txt"
    unconfirmedAddQuery = "unconfirmed_add_query.txt"
    bootFile = "data_base.txt"
}

internal class Test1 {
    @BeforeTest
    fun beforeTest() {
        toTestIO()
    }

    @AfterTest
    fun afterTest() {
        toRealizeIO()
    }

    @Test
    fun test1() {
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
