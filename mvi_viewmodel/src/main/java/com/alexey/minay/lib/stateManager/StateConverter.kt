package com.alexey.minay.lib.stateManager

class StateConverter {

    private val mHeader = "HTTP/1.1 200 OK\\r\\nContent-Type: text/html\\r\\n\\r\\n"

    fun convert(states: Map<Int, List<Any>>): String {
        return "$mHeader${createHtml(states)}"
    }

    private fun createHtml(states: Map<Int, List<Any>>): String {
        val state =
            "${states.values.map { "${it.map { "<br>${it.toString().pretty()}<br>" }}\n\n" }}"
        return """
            <!DOCTYPE html>
            <html>
            <body>

            <h1>States</h1>
            <p>$state</p>

            </body>
            </html>
        """.trimIndent()
    }

    private fun String.pretty(dif: Int = 4): String {
        var spaceCount = 0
        val getSpaces: () -> String = { "&nbsp;".repeat(spaceCount) }
        val prettyString = StringBuilder("<b>")
        forEachIndexed { index, char ->
            when (char) {
                ',' -> prettyString.append(",<br>${getSpaces()}<b>")
                '=' -> prettyString.append("</b>: ")
                ' ' -> Unit
                '(' -> {
                    spaceCount += dif
                    prettyString.append("</b>(<br>${getSpaces()}<b>")
                }
                ')' -> {
                    spaceCount -= dif
                    prettyString.append("<br>${getSpaces()})")
                }
                '[' -> {
                    spaceCount += dif
                    if (this.length > index + 1 && this[index + 1] == ']') {
                        prettyString.append("[")
                    } else {
                        prettyString.append("[<br>${getSpaces()}<b>")
                    }
                }
                ']' -> {
                    spaceCount -= dif
                    if (index - 1 >= 0 && this[index - 1] == '[') {
                        prettyString.append(" ]")
                    } else {
                        prettyString.append("</b></br>${getSpaces()}]")
                    }
                }
                else -> {
                    prettyString.append(char)
                }
            }
        }
        return prettyString.toString()
    }

}