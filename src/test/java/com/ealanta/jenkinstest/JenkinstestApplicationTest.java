package com.ealanta.jenkinstest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient.Request;
import com.github.dockerjava.transport.DockerHttpClient.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JenkinstestApplicationTest {

  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

  DockerHttpClient dockerHttpClient = new ApacheDockerHttpClient.Builder()
      .dockerHost(config.getDockerHost())
      .sslConfig(config.getSSLConfig())
      .maxConnections(100)
      .connectionTimeout(Duration.ofSeconds(30))
      .responseTimeout(Duration.ofSeconds(45))
      .build();

  DockerClient dockerClient = DockerClientImpl.getInstance(config, dockerHttpClient);

  @Test
  @Order(0)
  void testJavaVersion() {
    String version = System.getProperty("java.version");
    System.out.println(version);
    System.out.println(Runtime.version().feature());
    assertEquals(17, Runtime.version().feature());
  }

  @Test
  @Order(1)
  void testPing() throws Exception {
    Request request = Request.builder()
        .method(Request.Method.GET)
        .path("/_ping")
        .build();

    try (Response response = dockerHttpClient.execute(request)) {
      assertThat(response.getStatusCode(), equalTo(200));
      assertThat(new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8),
          equalTo("OK"));
    }
  }

  @Test
  @Order(2)
  void testGetVersion() throws Exception {
    Request request = Request.builder()
        .method(Request.Method.GET)
        .path("/version")
        .build();

    try (Response response = dockerHttpClient.execute(request)) {
      assertThat(response.getStatusCode(), equalTo(200));

      String json = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
      String pretty = gson.toJson(JsonParser.parseString(json));
      System.out.println(pretty);
    }
  }

  @Test
  @Order(3)
  void testDocker() {
    List<Container> containers = dockerClient.listContainersCmd()
        .exec();
    System.out.printf("CONTAINERS [%d]%n", containers.size());
    containers.forEach(System.out::println);
  }

}
