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

package com.tool.linebot.controller;

import java.net.URISyntaxException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tool.linebot.service.PushConfirmService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class WebhookController {

    private final PushConfirmService lineMessagingService;

    WebhookController(PushConfirmService lineMessagingService) {
        this.lineMessagingService = lineMessagingService;
    }

    @GetMapping()
    public String defaultPage() {
        return "";
    }

    @GetMapping("api/alarm/burnables")
    public void pushBurnablesAlarm() {
        try {
            lineMessagingService.pushBurnablesAlarm();
        } catch (URISyntaxException e) {
            System.out.println(e);
        }
    }
}