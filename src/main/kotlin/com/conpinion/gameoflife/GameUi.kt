package com.conpinion.gameoflife

import Cell
import GameOfLife
import arrow.syntax.function.partially2
import arrow.syntax.function.pipe
import org.hexworks.zircon.api.*
import org.hexworks.zircon.api.builder.graphics.CharacterTileStringBuilder
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.graphics.CharacterTileString
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.internal.graphics.DefaultCharacterTileString


class GameUi(val viewWidth: Int = 100, val viewHeight: Int = 70) {

    private lateinit var tileGrid: TileGrid

    fun createUi() {
        tileGrid = SwingApplications.startTileGrid(
                AppConfigs.newConfig()
                        .withSize(Sizes.create(viewWidth, viewHeight))
                        .withDefaultTileset(CP437TilesetResources.rexPaint16x16())
                        .build())
    }

    fun updateCells(x: Int, y: Int, alive: Boolean) {
        if (x > 0 && x < viewWidth && y > 0 && y < viewHeight) {
            val foregroundColor = if (alive) ANSITileColor.WHITE else ANSITileColor.BLACK
            val character = if (alive) 'X' else 'O'
            tileGrid.setTileAt(
                    Positions.create(x, y),
                    Tiles.newBuilder()
                            .withBackgroundColor(ANSITileColor.BLACK)
                            .withForegroundColor(foregroundColor)
                            .withCharacter(character)
                            .build())
        }
    }

    fun clearGrid() {
        tileGrid.clear()
    }
}

fun main() {
    val game = GameUi()
    game.createUi()

    val gameOfLife = GameOfLife()

    val rPentomino = listOf(
            Cell(40, 30), Cell(41, 30),
            Cell(39, 31), Cell(40, 31),
            Cell(40, 32)
    )

    with(gameOfLife) {
        (0..1500).fold(rPentomino) { world, i ->
            Thread.sleep(10)
            println("Iteration: $i / listSize: ${world.size}")
            world pipe ::step pipe ::printCells.partially2(game::updateCells)
        }
    }
    println("Ende")
}