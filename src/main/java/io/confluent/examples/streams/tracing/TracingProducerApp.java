package io.confluent.examples.streams.tracing;

import brave.Tracing;
import brave.kafka.clients.KafkaTracing;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TracingProducerApp {
  public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
    final Tracing tracing = TracingHelper.build("producer");
    final Map<String, Object> config = new HashMap<>();
    config.put("bootstrap.servers", "localhost:29092");
    config.put("key.serializer", StringSerializer.class);
    config.put("value.serializer", StringSerializer.class);
    final Producer<String, String> producer = new KafkaProducer<>(config);
    final KafkaTracing kafkaTracing = KafkaTracing.newBuilder(tracing).remoteServiceName("kafka").build();
    final Producer<String, String> tracingProducer = kafkaTracing.producer(producer);

    final ProducerRecord<String, String> producerRecord =
        new ProducerRecord<>("streams-plaintext-input-1", "", "hello world");
    RecordMetadata a = tracingProducer.send(producerRecord).get();
    System.out.println(a);
    System.in.read();
  }
}
