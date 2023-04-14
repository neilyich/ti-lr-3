package neilyich

class Player(
    val id: Int,
    private val strategiesCount: Int
) {
    fun strategies(): Iterable<Int> {
        return 0 until strategiesCount
    }
}