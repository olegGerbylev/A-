import kotlin.math.abs

class FindPath (field: Array<IntArray>,
                private val startRow: Int,
                private val startCol: Int,
                private val endRow: Int,
                private val endCol: Int ){
//    private val initPath = Path(startRow,startCol, distanceToGoal(startRow,startCol))
    private val field: Array<IntArray>
    private val rows = field.size
    private val cols = field[0].size
    val pathSortedQueue: MinPQ<Path> = MinPQ()
    init {
        for (i in field.indices){
            for (j in field[0].indices) {
                if(field[i][j] == 1){
                    field[i][j] = -1
                }
                if (field[i][j] == 0 || field[i][j] == 3){
                    field[i][j] = Int.MAX_VALUE
                }
            }
        }
        field[startRow][startCol] = 0
//        field[endRow][endCol] = 0
        this.field = field
        pathSortedQueue.enqueue(Path(startRow,startCol,distanceToGoal(startRow,startCol)))
    }

    var isSolvable = false

    var totalStep: Int? = null
    var solvablePath: Path? = null


    fun step():Path{
        var path = pathSortedQueue.dequeue() ?: throw NullPointerException()
        //        val stackPath = Stack<Path>()
        val cell1 = takeNeighbor(path,path.y-1,path.x)
        if (cell1 != null){
            pathSortedQueue.enqueue(cell1)
        }
        val cell2 = takeNeighbor(path,path.y+1,path.x)
        if (cell2 != null){
            pathSortedQueue.enqueue(cell2)
        }

        val cell3 = takeNeighbor(path,path.y,path.x-1)
        if (cell3 != null){
            pathSortedQueue.enqueue(cell3)
        }

        val cell4 = takeNeighbor(path,path.y,path.x+1)
        if (cell4 != null){
            pathSortedQueue.enqueue(cell4)
        }

        if (distanceToGoal(path.y,path.x) == 0){
            isSolvable = true
            totalStep = path.step
            solvablePath = path
        }

        return path
    }

    private fun distanceToGoal(row:Int,col:Int):Int{
        if (abs(endRow-row)+ abs(endCol - col) == 1){
            return abs(endRow-row)+ abs(endCol - col)
        }
        return abs(endRow-row)+ abs(endCol - col)

    }

    private fun takeNeighbor(previousPath: Path,row: Int, col: Int): Path?{
        var path: Path? = null
        if ((row >= 0 && col >= 0) && (row < field.size && col < field[0].size)){
            if (field[row][col]>=0 && field[row][col] > previousPath.step+1){
                path = Path(row,col,distanceToGoal(row,col), previousPath.step+1,previousPath)
                field[row][col] = previousPath.step+1
            }
        }
        return path
    }

}