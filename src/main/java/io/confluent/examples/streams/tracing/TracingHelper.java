package io.confluent.examples.streams.tracing;

import brave.Tracing;
import brave.sampler.Sampler;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.kafka11.KafkaSender;

public class TracingHelper {
  public static Tracing build(String serviceName) {
    final Sender sender = KafkaSender.create("localhost:29092").toBuilder().build();
    final Reporter<Span> reporter = AsyncReporter.create(sender);
    return Tracing.newBuilder().localServiceName(serviceName)
        .spanReporter(reporter)
        .sampler(Sampler.ALWAYS_SAMPLE)
        .build();
  }
}
