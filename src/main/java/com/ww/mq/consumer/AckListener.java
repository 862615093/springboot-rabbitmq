package com.ww.mq.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//@Component
//@RabbitListener(queues = {"work-queue"})
public class AckListener {
    /**
     * 监听队列 direct-queue 中的消息，有消息会自动取出并回调该方法
     * @param message 原生消息的详细信息，包括消息头+消息体
     * @param o  从消息体中解码出的javabean
     * @param channel 当前传输的数据通道
     */
//    @RabbitHandler
    public void listen(Message message, Object o, Channel channel){
        // 消息投递的标签号，在同一个channel内按顺序递增
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("deliveryTag:" + deliveryTag + ">>>>>>" + o);
        try {
            // 手动决定是否确认消息
            if (deliveryTag % 2 == 0){
                // 确认消息
                // 参数1：消息标签号  参数2：是否批量确认。一般为false，处理1个确认1个；否则会一次性确认掉当前消息标签号之前的全部消息，可能造成丢失
                channel.basicAck(deliveryTag, false);
                System.out.println("确认了消息：" + deliveryTag);
            }else {
                // 拒绝消息
                // 参数1：消息标签号  参数2：是否批量拒绝，一般为false  参数3：拒绝后是否将消息重新入队
                channel.basicNack(deliveryTag, false, false);
                System.out.println("拒绝了消息：" + deliveryTag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
