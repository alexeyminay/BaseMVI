package com.alexey.minay.lib.stateManager

class StateConverter {

    private val mHeader = "HTTP/1.1 200 OK\\r\\nContent-Type: text/html\\r\\n\\r\\n"

    fun convert(states: Map<Int, Any>): String {
        return "$mHeader${createHtml(states)}"
    }

    private fun createHtml(states: Map<Int, Any>): String {
        val state = "${states.values.map { "$it\n" }}"
        return """
            <!DOCTYPE html>
            <html>
            <body>

            <h1>My First Heading</h1>
            <p>$state</p>

            </body>
            </html>
        """.trimIndent()
    }

}