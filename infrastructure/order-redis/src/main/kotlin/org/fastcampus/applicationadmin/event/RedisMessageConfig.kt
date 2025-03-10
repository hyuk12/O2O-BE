package org.fastcampus.applicationadmin.event

import org.fastcampus.order.event.NotificationReceiver
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class RedisMessageConfig(
    @Qualifier("OrderNotificationReceiver")
    private val orderNotificationReceiver: NotificationReceiver,
    @Qualifier("OrderCancellationReceiver")
    private val orderCancellationReceiver: NotificationReceiver,
) {
    @Bean
    fun redisContainer(
        connectionFactory: RedisConnectionFactory,
        @Qualifier("OrderNotificationReceiverAdapter") orderNotificationListenerAdapter: MessageListenerAdapter,
        @Qualifier("OrderCancellationReceiverAdapter") orderCancellationReceiverAdapter: MessageListenerAdapter,
    ): RedisMessageListenerContainer {
        return RedisMessageListenerContainer()
            .apply {
                setConnectionFactory(connectionFactory)
                addMessageListener(orderNotificationListenerAdapter, PatternTopic("ORDER_NOTIFICATION"))
                addMessageListener(orderCancellationReceiverAdapter, PatternTopic("ORDER_CANCELLATION"))
                setTaskExecutor(
                    ThreadPoolTaskExecutor().apply {
                        val cpuCoreCount = Runtime.getRuntime().availableProcessors()
                        corePoolSize = cpuCoreCount * 2
                        maxPoolSize = cpuCoreCount * 4
                        queueCapacity = 100
                        keepAliveSeconds = 60
                        setThreadNamePrefix("MsgListener-")
                        initialize()
                    },
                )
            }
    }

    @Bean("OrderNotificationReceiverAdapter")
    fun orderNotificationReceiverAdapter(): MessageListenerAdapter {
        return MessageListenerAdapter(orderNotificationReceiver)
    }

    @Bean("OrderCancellationReceiverAdapter")
    fun orderCancellationReceiverAdapter(): MessageListenerAdapter {
        return MessageListenerAdapter(orderCancellationReceiver)
    }

    @Bean
    fun stringRedisTemplate(connectionFactory: RedisConnectionFactory): StringRedisTemplate {
        return StringRedisTemplate(connectionFactory)
    }
}
