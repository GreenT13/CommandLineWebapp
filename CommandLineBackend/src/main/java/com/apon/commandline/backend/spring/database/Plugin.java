/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apon.commandline.backend.spring.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Plugin {

	private @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
	private String pluginIdentifier;
	private Byte[] jarContent;

	public Plugin() {}

	public Plugin(String pluginIdentifier, Byte[] jarContent) {
		this.pluginIdentifier = pluginIdentifier;
		this.jarContent = jarContent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPluginIdentifier() {
		return pluginIdentifier;
	}

	public void setPluginIdentifier(String pluginIdentifier) {
		this.pluginIdentifier = pluginIdentifier;
	}

	public Byte[] getJarContent() {
		return jarContent;
	}

	public void setJarContent(Byte[] jarContent) {
		this.jarContent = jarContent;
	}
}
