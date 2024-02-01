class Path(
    val y: Int,
    val x: Int,
    val distance: Int,
    val step: Int = 0,
    val previousPath: Path? = null
): Comparable<Path>{
    override fun compareTo(other: Path): Int {
        val sum = errorDistance()-other.errorDistance()
        if (sum == 0 && step !=other.step){
            return step-other.step
        }
        return sum
    }
    fun errorDistance():Int{
        return distance
    }

    override fun toString(): String {

        return "(" + y + "," + x + ") -> d: " + distance + "s: " + step
    }
}