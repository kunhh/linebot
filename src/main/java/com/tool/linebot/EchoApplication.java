/*
 * Copyright 2018 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.tool.linebot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.script.Bindings;
import javax.script.ScriptEngineManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
	private final Logger log = LoggerFactory.getLogger(EchoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EchoApplication.class, args);
	}

	@EventMapping
	public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
		String message = "失敗";
		if ("福岡の天気".equals(event)){
			try {
				URL url = new URL("http://weather.livedoor.com/forecast/webservice/json/v1?city=130010");
				String json;
				// Get通信してStringに（evalするために丸括弧で囲む）
				try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));) {
					json = br.lines().collect(Collectors.joining("", "(", ")"));
				}// ↑ "({"forecasts":[{"dateLabel":"x",...},{"dateLabel":"y",...},...],...})" こんなString

				Bindings jsObj = (Bindings) new ScriptEngineManager().getEngineByName("js").eval(json);

				jsObj = (Bindings) jsObj.get("forecasts");
				// get(key)で取得できるが、戻りはObject型。必要に応じてキャスト

				StringJoiner sj = new StringJoiner("\t");

				// js配列は values() で
				jsObj.values().stream()
				.map(o -> (Bindings) o)
				.map(o -> o.get("dateLabel") + "\t" + o.get("telop"))
				.forEach(i -> sj.add(String.valueOf(i)));
				message = sj.toString();

			} catch (Exception e) {
				e.printStackTrace();
			}// 例外処理はまるっと雑にしちゃっています。ごめんなさい。
		} else {
		log.info("event: " + event);
		message = event.getMessage().getText();
		}
		return new TextMessage(message);
	}

	@EventMapping
	public void handleDefaultMessageEvent(Event event) {
		System.out.println("event: " + event);
	}
}