package ru.mpoperechny.eventreminder.utilites

import ru.mpoperechny.eventreminder.database.EventEntity

object EventImportExport {
    const val FILE_NAME = "eventreminder_events.json"

    fun toJson(events: List<EventEntity>): String {
        val builder = StringBuilder()
        builder.append("{\"events\":[")

        events.forEachIndexed { index, event ->
            if (index > 0) builder.append(',')
            builder.append('{')
            builder.append("\"id\":${event.id},")
            builder.append("\"date\":${event.date},")
            builder.append("\"type\":${event.type},")
            builder.append("\"person\":")
            builder.append(escapeJsonString(event.person ?: ""))
            builder.append(',')
            builder.append("\"description\":")
            builder.append(escapeJsonString(event.description ?: ""))
            builder.append('}')
        }

        builder.append("]}")
        return builder.toString()
    }

    fun fromJson(json: String): List<EventEntity> {
        val source = json.trim()
        if (source.isEmpty()) return emptyList()

        val eventsStart = source.indexOf("\"events\"")
        if (eventsStart == -1) return emptyList()

        val arrayStart = source.indexOf('[', eventsStart)
        val arrayEnd = source.lastIndexOf(']')
        if (arrayStart == -1 || arrayEnd <= arrayStart) return emptyList()

        val items = source.substring(arrayStart + 1, arrayEnd)
        if (items.trim().isEmpty()) return emptyList()

        val result = mutableListOf<EventEntity>()
        var cursor = 0

        while (cursor < items.length) {
            val objectStart = items.indexOf('{', cursor)
            if (objectStart == -1) break

            val objectEnd = findMatchingClosingBrace(items, objectStart)
            if (objectEnd == -1) break

            val objectBody = items.substring(objectStart + 1, objectEnd)
            val values = parseKeyValues(objectBody)
            val event = EventEntity(
                date = values["date"]?.toLongOrNull() ?: 0L,
                type = values["type"]?.toIntOrNull() ?: 0,
                person = values["person"].takeIf { !it.isNullOrBlank() },
                description = values["description"].takeIf { !it.isNullOrBlank() }
            )
            event.id = values["id"]?.toIntOrNull() ?: 0
            result.add(event)

            cursor = objectEnd + 1
        }

        return result
    }

    private fun parseKeyValues(source: String): Map<String, String> {
        val result = linkedMapOf<String, String>()
        var index = 0

        while (index < source.length) {
            while (index < source.length && source[index].isWhitespace()) index++
            if (index >= source.length) break
            if (source[index] != '"') {
                index++
                continue
            }

            val key = readJsonString(source, index)
            index = key.second
            while (index < source.length && source[index].isWhitespace()) index++
            if (index < source.length && source[index] == ':') index++
            while (index < source.length && source[index].isWhitespace()) index++

            val value = if (index < source.length && source[index] == '"') {
                val parsed = readJsonString(source, index)
                index = parsed.second
                parsed.first
            } else {
                val start = index
                while (index < source.length && source[index] != ',' && source[index] != '}') index++
                source.substring(start, index).trim()
            }

            result[key.first] = value

            while (index < source.length && source[index].isWhitespace()) index++
            if (index < source.length && source[index] == ',') index++
        }

        return result
    }

    private fun readJsonString(source: String, startIndex: Int): Pair<String, Int> {
        val builder = StringBuilder()
        var index = startIndex + 1

        while (index < source.length) {
            val current = source[index]
            if (current == '\\') {
                if (index + 1 < source.length) {
                    val next = source[index + 1]
                    when (next) {
                        '"' -> builder.append('"')
                        '\\' -> builder.append('\\')
                        '/' -> builder.append('/')
                        'b' -> builder.append('\b')
                        'f' -> builder.append('\u000C')
                        'n' -> builder.append('\n')
                        'r' -> builder.append('\r')
                        't' -> builder.append('\t')
                        'u' -> {
                            if (index + 5 < source.length) {
                                val code = source.substring(index + 2, index + 6)
                                try {
                                    builder.append(code.toInt(16).toChar())
                                } catch (_: NumberFormatException) {
                                    builder.append('?')
                                }
                                index += 4
                            }
                        }
                        else -> builder.append(next)
                    }
                    index += 2
                    continue
                }
            } else if (current == '"') {
                return builder.toString() to index + 1
            }

            builder.append(current)
            index++
        }

        return builder.toString() to source.length
    }

    private fun findMatchingClosingBrace(source: String, startIndex: Int): Int {
        var depth = 0
        var index = startIndex
        var escaped = false

        while (index < source.length) {
            val current = source[index]
            when {
                escaped -> escaped = false
                current == '\\' -> escaped = true
                current == '{' -> depth++
                current == '}' -> {
                    depth--
                    if (depth == 0) return index
                }
            }
            index++
        }

        return -1
    }

    private fun escapeJsonString(value: String): String {
        val builder = StringBuilder()
        builder.append('"')
        value.forEach { char ->
            when (char) {
                '\\' -> builder.append("\\\\")
                '"' -> builder.append("\\\"")
                '\n' -> builder.append("\\n")
                '\r' -> builder.append("\\r")
                '\t' -> builder.append("\\t")
                else -> builder.append(char)
            }
        }
        builder.append('"')
        return builder.toString()
    }
}
