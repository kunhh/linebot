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
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebhookService {

    private final PushConfirmService lineMessagingService;
    private int i = 0;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    WebhookService(PushConfirmService lineMessagingService) {
        this.lineMessagingService = lineMessagingService;
    }

    @Scheduled(cron="${garbage.reminder.cron.burnables}", zone = "Asia/Tokyo")
    public void executeBurnablesAlarm() {
        try {
            lineMessagingService.pushBurnablesAlarm();
        } catch (URISyntaxException e) {
        	System.out.println(e);
        }
        System.out.println(sdf.format(new Date()));
    }
}