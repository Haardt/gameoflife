package com.conpinion.gameoflife

import org.amshove.kluent.`should be equal to`
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal object SpekTest : Spek({
    Feature("Testing spek") {
        Scenario("first test") {
            When("condition") {

            }
            Then("Test") {
                1 `should be equal to` 2
            }


        }
    }
})