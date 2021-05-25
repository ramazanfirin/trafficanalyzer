package com.masterteknoloji.trafficanalyzer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Trafficanalyzer.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

	public String videoFilesPath;

	public String getVideoFilesPath() {
		return videoFilesPath;
	}

	public void setVideoFilesPath(String videoFilesPath) {
		this.videoFilesPath = videoFilesPath;
	}
	
	
}
