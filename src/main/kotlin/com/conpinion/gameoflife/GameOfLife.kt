import arrow.syntax.function.partially1
import arrow.syntax.function.pipe

data class Cell(val x: Int, val y: Int)

class GameOfLife {

    fun step(cells: List<Cell>) = (createCells(cells) + filterDeadCells(cells))

    fun printCells(cells: List<Cell>, renderer: (Int, Int, Boolean) -> Unit): List<Cell> {

        val isCellAlive = ::isCellAlive.partially1(cells)

        val left = cells.minBy { it.x } ?: return cells
        val top = cells.minBy { it.y } ?: return cells
        val right = cells.maxBy { it.x } ?: return cells
        val bottom = cells.maxBy { it.y } ?: return cells
        (top.y..bottom.y).forEach { posY ->
            (left.x..right.x).forEach { posX ->
                renderer(posX, posY, isCellAlive(posX, posY))
            }
        }
        return cells
    }

    private fun isCellAlive(cells: List<Cell>, x: Int, y: Int) =
            cells.any { (cellX, cellY) -> x == cellX && y == cellY }

    internal fun createCells(cells: List<Cell>) = ::numberOfNeighbors.partially1(cells).let { numOfNeighborsFn ->
        cells.map { (x, y) ->
            neighborPositions(x, y)
                    .filter { pos -> numOfNeighborsFn(pos) == 3 }
                    .map { (x, y) -> Cell(x, y) }
        }.flatten()
                .distinct()
                .filter { !isCellAlive(cells, it.x, it.y) }
    }

    internal fun filterDeadCells(cells: List<Cell>) = ::numberOfNeighbors.partially1(cells).let { numOfNeighborsFn ->
        cells.filter { cell -> numOfNeighborsFn(cell).let { it == 2 || it == 3 } }
    }

    internal fun neighborPositions(nx: Int, ny: Int) = ((ny - 1)..(ny + 1)).map { y ->
        ((nx - 1)..(nx + 1))
                .map { x -> Cell(x, y) }
    }.flatten()

    internal fun numberOfNeighbors(cells: List<Cell>, searchCell: Cell) =
            cells.filter(::areNeighbors.partially1(searchCell)).count()

    internal fun areNeighbors(searchCell: Cell, currentCell: Cell): Boolean =
            if (searchCell == currentCell) {
                false
            } else {
                with(searchCell) {
                    (currentCell.x in (x - 1).rangeTo(x + 1) && (currentCell.y in (y - 1).rangeTo(y + 1)))
                }
            }
}


