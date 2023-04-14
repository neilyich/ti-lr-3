package neilyich

interface Game : Iterable<List<Int>> {
    fun situation(strategies: List<Int>): Situation
    fun players(): List<Player>
    fun situations(): Iterable<Situation> = this.map { situation(it) }
}