import java.awt.*
import java.awt.event.*
import java.lang.IllegalArgumentException
import java.util.*
import javax.swing.*
import javax.swing.Timer
import kotlin.system.exitProcess

class DrawField(private val rows: Int, private val cols: Int) : JPanel() {

    private val cellSize = 15
    private var field = Array(rows) { IntArray(cols) { 0 } }
    private var gameRunning = false
    val timer = Timer(1, null)
    var state = 1
    private var startRow: Int? = null
    private var startCol: Int? = null
    private var endRow: Int? = null
    private var endCol: Int? = null

    var isSolved = false
    var counterStep = 0
    var error: Int = Int.MAX_VALUE
    init {
        preferredSize = Dimension(cols * cellSize, rows * cellSize)

        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                if (!gameRunning){
                    toggleCellState(e)
                }
            }
        })

        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                if (!gameRunning){
                    if (state == 1){
                        toggleCellState(e)
                    }
                }
            }
        })
    }

    private fun toggleCellState(e: MouseEvent) {
        val row = e.y / cellSize
        val col = e.x / cellSize

        if (row in 0 until rows && col in 0 until cols) {
            if(state == 1){
                setWall(e,row,col)
            }else if (state == 2){
                setStart(e,row,col)
            }else{
                setEnd(e,row,col)
            }
            repaint()
        }
    }
    
    private fun setWall(e: MouseEvent, row: Int, col:Int){
        if (startRow == row && startCol == col){
            return
        }
        if(endRow == row && endCol == col){
            return
        }
        if (SwingUtilities.isLeftMouseButton(e)) {
            field[row][col] = 1
        } else if (SwingUtilities.isRightMouseButton(e)) {
            field[row][col] = 0
        }
    }
    
    private fun setStart(e:MouseEvent,row: Int,col: Int){
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (startRow != null && startCol != null) {
                    field[startRow!!][startCol!!]=0
                }
                startRow = row
                startCol = col
                field[row][col] = 2
        }
    }
    
    private fun setEnd(e:MouseEvent,row: Int,col: Int){
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (endRow != null && endCol != null) {
                field[endRow!!][endCol!!]=0
            }
            endRow = row
            endCol = col
            field[row][col] = 3
        }
    }
    
    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val x = j * cellSize
                val y = i * cellSize
                if (field[i][j] == 0) {
                    g.color = Color.BLACK
                    g.fillRect(x, y, cellSize, cellSize)
                } else if (field[i][j] == 1){
                    g.color = Color.WHITE
                    g.fillRect(x, y, cellSize, cellSize)
                } else if (field[i][j] == 2) {
                    g.color = Color.GREEN
                    g.fillRect(x, y, cellSize, cellSize)
                } else if(field[i][j] == 3){
                    g.color = Color.RED
                    g.fillRect(x, y, cellSize, cellSize)
                } else if (field[i][j] == 4){
                    g.color = Color.BLUE
                    g.fillRect(x,y,cellSize,cellSize)
                }
                g.color = Color.GRAY
                g.drawRect(x, y, cellSize, cellSize)
            }
        }
    }

    fun start(){
        if (startRow == null || startCol == null || endRow == null || endCol == null){
            throw IllegalArgumentException("you must declare the beginning and the end")
        }
        val copyField = copyIntArrayArray(field)
        val findPath = FindPath(copyIntArrayArray(field),startRow!!,startCol!!,endRow!!,endCol!!)
        timer.addActionListener {
            field = copyIntArrayArray(copyField)
            var path:Path? = findPath.step()
            counterStep = path!!.step
            error = path.distance
//            field[endRow!!][endCol!!] = 3
            for (paths in findPath.pathSortedQueue){
                field[paths.y][paths.x] = 4
            }
            while (path != null){
                field[path.y][path.x] = 2
                path = path.previousPath
            }
//            field[endRow!!][endCol!!] = 3
            repaint()
            if (findPath.isSolvable){
                isSolved = true
                timer.stop()
            }
        }

        timer.start()
    }

    private fun copyIntArrayArray(original: Array<IntArray>): Array<IntArray> {
        val copy = Array(rows){IntArray(cols)}
        for (i in original.indices){
            for (j in original[0].indices){
                copy[i][j] = original[i][j]
            }
        }
        return copy
    }
//    fun findPath(){
//
//    }

    fun reset(){
        timer.stop()
        field = Array(rows) { IntArray(cols){0} }
        gameRunning = false
        repaint()
    }
}

fun main() {
    val rows = 65
    val cols = 100

    val frame = JFrame("Game Board")
    var gameBoard = DrawField(rows, cols)
    val startButton = JButton("Start")
    val resetButton = JButton("Reset")
    val setStart = JButton("Set Start")
    val setGoal = JButton("Set Goal")
    val setWall = JButton("Set Wall")
    resetButton.addActionListener { gameBoard.reset() }
    startButton.addActionListener { gameBoard.start() }
    setWall.addActionListener { gameBoard.state = 1 }
    setStart.addActionListener { gameBoard.state = 2 }
    setGoal.addActionListener { gameBoard.state = 3 }

//    val counterStep = JTextField("Step: " + gameBoard.counterStep)
//    val error = JTextField("Error: "+ gameBoard.error)
//    val isSolved = JTextField("isSolved: "+ gameBoard.isSolved)
//    val infoPanel = JPanel()
//    infoPanel.layout = FlowLayout(FlowLayout.CENTER)
//    infoPanel.add(counterStep)
//    infoPanel.add(error)
//    infoPanel.add(isSolved)

    val buttonPanel = JPanel()
    buttonPanel.layout = FlowLayout(FlowLayout.CENTER)
    buttonPanel.add(startButton)
    buttonPanel.add(resetButton)
    buttonPanel.add(setWall)
    buttonPanel.add(setStart)
    buttonPanel.add(setGoal)
//    buttonPanel.add(counterStep, BorderLayout.CENTER)


    val scrollPane = JScrollPane(gameBoard)

    frame.layout = BorderLayout()

    frame.add(buttonPanel, BorderLayout.NORTH)
    frame.add(scrollPane, BorderLayout.CENTER)
//    frame.add(isSolved,BorderLayout.NORTH)

    frame.pack()
    frame.isResizable = true
    frame.maximumSize = Dimension(1280, 720)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.addWindowListener(object : WindowAdapter() {
        override fun windowClosing(e: WindowEvent) {
            exitProcess(0)
        }
    })
    frame.isVisible = true

}
