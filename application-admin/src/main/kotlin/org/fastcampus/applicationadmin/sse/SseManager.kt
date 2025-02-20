package org.fastcampus.applicationadmin.sse

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet

@Component
class SseManager {
    fun manage(key: String, emitter: SseEmitter) {
        emitter.onError {
            logger.error("SSE onError id: [{}], emitter: [{}]", key, emitter)
        }
        emitter.onTimeout {
            logger.debug("SSE onTimeout id: [{}], emitter: [{}]", key, emitter)
        }
        emitter.onCompletion {
            logger.debug("SSE onCompletion id: [{}], emitter: [{}]", key, emitter)
            removeEmitter(key, emitter)
        }
        emitters.computeIfAbsent(key) { CopyOnWriteArraySet<SseEmitter>() }.add(emitter)

        // 연결 시작시 연결 지속을 위해 더미데이터 응답 1회
        push(
            key = key,
            comment = "connected",
            reconnectTime = 500L,
        )
        logger.debug("관리중 세션: {}", emitters)
    }

    fun push(
        key: String,
        eventType: String? = null,
        data: String? = null,
        comment: String? = null,
        lastEventId: String? = null,
        reconnectTime: Long? = null,
    ) {
        try {
            emitters[key]?.forEach {
                it.send(
                    SseEmitter
                        .event()
                        .apply {
                            eventType?.let { name(eventType) }
                            data?.let { data(data) }
                            comment?.let { comment(comment) }
                            lastEventId?.let { id(lastEventId) }
                            reconnectTime?.let { reconnectTime(reconnectTime) }
                        },
                )
            } ?: logger.debug("연결된 점주 [{}] 미존재", key)
        } catch (ex: Exception) {
            logger.error("SSE push error", ex)
            emitters.remove(key)
        }
    }

    private fun removeEmitter(key: String, emitter: SseEmitter) {
        val emitterSet = emitters[key]
        if (emitterSet?.remove(emitter) == true) {
            logger.debug("*** [{}] emitter 제거됨", emitter)
            // 리스트가 비어있다면
            if (emitterSet.isEmpty()) {
                logger.debug("*** [{}] 점주의 연결목록 모두 삭제", key)
                emitters.remove(key)
                logger.debug("*** emitters map: {}", emitters)
            }
        }
    }

    private companion object {
        private val logger = LoggerFactory.getLogger(SseManager::class.java)
        private val emitters = ConcurrentHashMap<String, CopyOnWriteArraySet<SseEmitter>>()
    }
}
