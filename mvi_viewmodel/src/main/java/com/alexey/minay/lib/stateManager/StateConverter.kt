package com.alexey.minay.lib.stateManager

class StateConverter {

    private val mHeader = "HTTP/1.1 200 OK\\r\\nContent-Type: text/html\\r\\n\\r\\n"

    fun convert(states: Map<Int, List<StateStorageEntity>>): String {
        return "$mHeader${createHtml(states)}"
    }

    private fun createHtml(states: Map<Int, List<StateStorageEntity>>): String {
        return """
            <!DOCTYPE html>
            <html>
             <head>
              <meta charset="utf-8">
              <title>Глобальные стили</title>
              </head>
              <body>

              <h1>States</h1>
              <script>function view(n) {
                  style = document.getElementById(n).style;
                  style.display = (style.display == 'block') ? 'none' : 'block';
              }
              </script>
             ${
            states.values.map { entities ->
                entities.mapIndexed { index, entity ->
                    """
                    <a href="#hidden$index" onclick="view('hidden$index'); return false" style="font-size: 120%; font-family: monospace; color: #cd66cc; padding: 0px 48px;">$index. ${entity.stateOrNull?.result?.prettyResult()}</a>
                     <div id="hidden$index" style="display: none; padding: 12px 48px;">
                         <p>${entity.stateOrNull?.state.toString().pretty()}</p>
                     </div>
                     <div/>
                 """.trimIndent()
                }
            }.flatten()
                .joinToString(separator = "") { "$it\n" }
        }

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

    private fun Any.prettyResult() = toString().substringAfter("$")
        .substringBefore("@")

}