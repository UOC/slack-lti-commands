package edu.uoc.elc.slack.lti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class SlackLtiCommandApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlackLtiCommandApplication.class, args);
	}
}
