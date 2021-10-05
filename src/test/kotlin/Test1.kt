import java.io.File
import kotlin.test.*

internal class Test1 {

    @Test
    fun queriesTest() {
        val kvdb = KeyValueDataBase("testing_base_1")
        val data = kvdb.readData("testing_base_1$bootFile")
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
    }
}

internal class Test2 {

    @Test
    fun fileQueriesTest() {
        File("testing_base_2$incorrectInput").writeText("")
        File("testing_base_2$unconfirmedAddQueries").writeText("")
        val kvdb = KeyValueDataBase("testing_base_2")
        kvdb.fileAdd("testing_base_2/add_queries.txt")
        val incorrect = File("testing_base_2$incorrectInput").readLines()
        var unconfirmed = File("testing_base_2$unconfirmedAddQueries").readLines()
        assertEquals(4, incorrect.size)
        assertEquals(4, unconfirmed.size)
        kvdb.confirmAllAddQueries()
        unconfirmed = File("testing_base_2$unconfirmedAddQueries").readLines()
        assertEquals(0, unconfirmed.size)
        assertEquals("c", kvdb.getElement("a"))
    }
}
