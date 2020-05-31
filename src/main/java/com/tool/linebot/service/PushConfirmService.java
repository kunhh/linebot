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

package com.tool.linebot.service;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.tool.linebot.config.LineProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PushConfirmService {

    private final LineProperties lineProperties;
    private final LineMessagingClient lineMessagingClient;

    PushConfirmService(LineProperties lineProperties, LineMessagingClient lineMessagingClient) {
        this.lineProperties = lineProperties;
        this.lineMessagingClient = lineMessagingClient;
    }

    public void pushBurnablesAlarm() throws URISyntaxException {
        try {
            BotApiResponse response = lineMessagingClient
                                            .pushMessage(
                                                    new PushMessage(
                                                            lineProperties.getId(),
                                                            new TemplateMessage(
                                                                    "Tomorrow is the garbage day for burnables！",
                                                                    new ConfirmTemplate("Did you take out the garbage?",
                                                                            new MessageAction("yes", "yes"),
                                                                            new MessageAction("no", "no")
                                                                    )
                                                            )
                                                    )
                                            )
                                            .get();
            System.out.println(response);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}