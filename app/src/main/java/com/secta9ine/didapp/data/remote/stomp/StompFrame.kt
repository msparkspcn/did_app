package com.secta9ine.didapp.data.remote.stomp


enum class StompCommand {
    // Client commands
    CONNECT, STOMP, SEND, SUBSCRIBE, UNSUBSCRIBE, DISCONNECT,
    // Server commands
    CONNECTED, MESSAGE, RECEIPT, ERROR
}

/**
 * STOMP 프레임 모델.
 * STOMP 1.2 스펙에 따라 프레임을 인코딩/디코딩한다.
 */
data class StompFrame(
    val command: StompCommand,
    val headers: Map<String, String> = emptyMap(),
    val body: String? = null
) {
    /**
     * STOMP 와이어 포맷으로 직렬화.
     * 형식: COMMAND\nheader1:value1\nheader2:value2\n\nbody\u0000
     */
    fun encode(): String {
        val sb = StringBuilder()
        sb.append(command.name).append('\n')
        headers.forEach { (key, value) ->
            sb.append(key).append(':').append(value).append('\n')
        }
        sb.append('\n')
        if (body != null) {
            sb.append(body)
        }
        sb.append(NULL_CHAR)
        return sb.toString()
    }

    companion object {
        const val NULL_CHAR = '\u0000'

        /**
         * STOMP 와이어 포맷 문자열을 StompFrame으로 파싱.
         * heart-beat 프레임(줄바꿈만 있는)은 null 반환.
         */
        fun decode(raw: String): StompFrame? {
            val cleaned = raw.trimEnd(NULL_CHAR)
            if (cleaned.isBlank()) return null // heart-beat frame

            val nlIndex = cleaned.indexOf('\n')
            if (nlIndex < 0) return null

            val commandStr = cleaned.substring(0, nlIndex).trim()
            val command = runCatching { StompCommand.valueOf(commandStr) }.getOrNull()
                ?: return null

            val rest = cleaned.substring(nlIndex + 1)
            val headerBodySeparator = rest.indexOf("\n\n")
            val headers = mutableMapOf<String, String>()
            val body: String?

            if (headerBodySeparator >= 0) {
                val headerBlock = rest.substring(0, headerBodySeparator)
                body = rest.substring(headerBodySeparator + 2).ifEmpty { null }
                parseHeaders(headerBlock, headers)
            } else {
                // 헤더만 있고 body 없는 경우
                parseHeaders(rest.trimEnd(), headers)
                body = null
            }

            return StompFrame(command, headers, body)
        }

        private fun parseHeaders(block: String, out: MutableMap<String, String>) {
            block.lines().forEach { line ->
                val colonIndex = line.indexOf(':')
                if (colonIndex > 0) {
                    val key = line.substring(0, colonIndex)
                    val value = line.substring(colonIndex + 1)
                    // STOMP 스펙: 첫 번째 등장한 헤더가 우선
                    out.putIfAbsent(key, value)
                }
            }
        }
    }
}
