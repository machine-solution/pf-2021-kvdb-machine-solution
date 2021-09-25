import kotlin.test.*

internal class Test1 {

    @Test
    fun test1() {
        val kvdb = KeyValueDataBase()
        val data = kvdb.readData("testdata1.txt")
        assertEquals(KeyValueElement("1", "1"), data[0])
        assertEquals(KeyValueElement("2", "2 -> 2"), data[1])
    }
}
