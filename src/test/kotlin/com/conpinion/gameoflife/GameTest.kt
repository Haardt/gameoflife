package com.conpinion.gameoflife

import Cell
import GameOfLife
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain same`
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class GameTest : Spek({

    Feature("Game of Life") {

        Scenario("A cell with neighbors") {
            val game = GameOfLife()

            var result = false

            When("#hasNeighbors called with direct neighbor") {
                result = game.areNeighbors(Cell(5, 5), Cell(4, 5))
            }
            Then("it should return true") {
                result `should be equal to` true
            }


            When("#hasNeighbors called without neighbors") {
                result = game.areNeighbors(Cell(5, 5), Cell(7, 7))
            }
            Then("it should return true") {
                result `should be equal to` false
            }

            var numberOfNeighbors = 0

            When("#neighborPositions called with three neighbors") {
                numberOfNeighbors = game.numberOfNeighbors(listOf(
                        Cell(5, 5),
                        Cell(5, 6),
                        Cell(5, 7)
                ), Cell(4, 6))
            }
            Then("it should return 3") {
                numberOfNeighbors `should be equal to` 3
            }

            When("#neighborPositions called with 8 neighbors") {
                numberOfNeighbors = game.numberOfNeighbors(listOf(
                        Cell(5, 5), Cell(6, 5), Cell(7, 5),
                        Cell(5, 6), Cell(6, 6), Cell(7, 6),
                        Cell(5, 7), Cell(6, 7), Cell(7, 7)
                ), Cell(6, 6))
            }
            Then("it should return 8") {
                numberOfNeighbors `should be equal to` 8
            }
        }
        Scenario("Calculate neighbors positions") {
            val game = GameOfLife()

            var positions = emptyList<Cell>()

            When("#neighborPositions called") {
                positions = game.neighborPositions(6, 6)
            }

            Then("it should return a list of cells") {
                positions `should contain same` listOf(
                        Cell(5, 5), Cell(6, 5), Cell(7, 5),
                        Cell(5, 6), Cell(6, 6), Cell(7, 6),
                        Cell(5, 7), Cell(6, 7), Cell(7, 7)
                )
            }
        }

        Scenario("Calculate new cells") {

            /**
             * Blinker:
             *     X
             *     X
             *     X
             *     after step
             *     O
             *    XXX
             *     O
             */

            val game = GameOfLife()

            var stepResult = emptyList<Cell>()
            When("#newCells called with exact three neighbors") {
                stepResult = game.createCells(listOf(
                        Cell(5, 5),
                        Cell(5, 6),
                        Cell(5, 7)
                ))
            }
            Then("it should generate two new cells") {
                println("Create Result: $stepResult")
                stepResult `should contain same` listOf(
                        Cell(4, 6),
                        Cell(6, 6)
                )
            }
        }

        Scenario("Filter dead cells") {

            /**
             * Blinker:
             *     X
             *     X
             *     X
             *     after step
             *     O
             *    XXX
             *     O
             */

            val game = GameOfLife()

            var stepResult = emptyList<Cell>()
            When("#filterDeadCells called with exact three cells in line") {
                stepResult = game.filterDeadCells(listOf(
                        Cell(5, 5),
                        Cell(5, 6),
                        Cell(5, 7)
                ))
            }
            Then("it should left one cell") {
                stepResult `should contain same` listOf(
                        Cell(5, 6)
                )
            }
        }

        Scenario("Calculate the next generation") {
            val game = GameOfLife()
            var stepResult = emptyList<Cell>()
            When("#step") {
                stepResult = game.step(listOf(
                        Cell(3, 0),
                        Cell(3, 1),
                        Cell(3, 2)
                ))

            }
            Then("it should generate the new world") {
                stepResult `should contain same` listOf(
                        Cell(2, 1),
                        Cell(4, 1),
                        Cell(3, 1)
                )
            }
        }

        Scenario("The printer should 'fill' the closure") {
            val game = GameOfLife()

            val screen = Array(5) { Array(5) { 'O' } }

            When("#filterDeadCells called with exact three cells in line") {

                game.printCells(listOf(
                        Cell(3, 0),
                        Cell(3, 1),
                        Cell(3, 2)
                )) { x, y, alive ->
                    screen[x][y] = if (alive) 'X' else 'O'
                }
            }

            Then("it render line 0") {
                screen[0] `should contain same` Array(5) { 'O' }
            }

            Then("it render line 1") {
                println("Size: ${screen[0].size}")
                screen.forEach { line -> println(line.joinToString("") { it.toString() }) }
                screen[1] `should contain same` Array(5) { 'O' }

            }
            Then("it render line 2") {
                screen[2] `should contain same` charArrayOf('O', 'X', 'X', 'X', 'O').toTypedArray()

            }
        }
    }
})
