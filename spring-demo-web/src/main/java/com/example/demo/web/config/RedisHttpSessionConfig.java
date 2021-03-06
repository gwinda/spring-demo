package com.example.demo.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.session.web.http.SessionRepositoryFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Spring Session Configuration. In {@link SessionRepositoryFilter}, it will replace {@link
 * HttpServletRequest} with {@link SessionRepositoryFilter.SessionRepositoryRequestWrapper}. This
 * wrapper will return a {@link
 * SessionRepositoryFilter.SessionRepositoryRequestWrapper.HttpSessionWrapper} to client. And then
 * will persist session data in redis.
 */
@EnableRedisHttpSession
@Configuration
@ConfigurationProperties("demo.redis")
public class RedisHttpSessionConfig {
  private String host;
  private int port;

  @Bean
  public LettuceConnectionFactory lettuceConnectionFactory() {
    return new LettuceConnectionFactory(host, port);
  }

  /**
   * This resolver can help to use HTTP headers to convey current session information instead of
   * cookies.
   *
   * @return bean httpSessionIdResolver
   */
  // @Bean
  public HttpSessionIdResolver httpSessionIdResolver() {
    return HeaderHttpSessionIdResolver.xAuthToken();
  }

  @Bean
  public HttpSessionListener httpSessionListener() {
    return new RedisHttpSessionListener();
  }

  private static class RedisHttpSessionListener implements HttpSessionListener {
    private static final Logger logger = LoggerFactory.getLogger(RedisHttpSessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
      logger.info("{} has created", se.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
      logger.info("{} has destroyed", se.getSession());
    }
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }
}
