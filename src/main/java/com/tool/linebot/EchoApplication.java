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
import java.util.HashMap;
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

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("福岡", "400010");
		map.put("八幡", "400020");
		map.put("飯塚", "400030");
		map.put("久留米", "400040");
		map.put("佐賀", "410010");
		map.put("伊万里", "410020");
		map.put("長崎", "420010");
		map.put("佐世保", "420020");
		map.put("厳原", "420030");
		map.put("福江", "420040");
		map.put("熊本", "430010");
		map.put("阿蘇", "430020");
		map.put("牛深", "430030");
		map.put("人吉", "430040");
		map.put("大分", "440010");
		map.put("中津", "440020");
		map.put("日田", "440030");
		map.put("佐伯", "440040");
		map.put("宮崎", "450010");
		map.put("延岡", "450020");
		map.put("都城", "450030");
		map.put("高千穂", "450040");
		map.put("鹿児島", "460010");
		map.put("種子島", "460030");
		map.put("名瀬", "460040");
		map.put("那覇", "471010");
		map.put("名護", "471020");
		map.put("久米島", "471030");
		map.put("南大東", "472000");
		map.put("宮古島", "473000");
		map.put("石垣島", "474010");
		map.put("与那国島", "474020");

		String message = "失敗";
		if (map.containsKey(event.getMessage().getText())){
			try {
				URL url = new URL("http://weather.livedoor.com/forecast/webservice/json/v1?city=" + map.get(event.getMessage().getText()));
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