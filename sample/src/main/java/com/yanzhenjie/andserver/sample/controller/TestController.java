/*
 * Copyright 2018 Zhenjie Yan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.andserver.sample.controller;

import com.yanzhenjie.andserver.annotation.*;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;
import com.yanzhenjie.andserver.http.cookie.Cookie;
import com.yanzhenjie.andserver.http.multipart.MultipartFile;
import com.yanzhenjie.andserver.http.session.Session;
import com.yanzhenjie.andserver.sample.component.LoginInterceptor;
import com.yanzhenjie.andserver.sample.model.UserInfo;
import com.yanzhenjie.andserver.sample.util.FileUtils;
import com.yanzhenjie.andserver.sample.util.Logger;
import com.yanzhenjie.andserver.util.MediaType;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

/**
 * Created by Zhenjie Yan on 2018/6/9.
 */
@RestController
@RequestMapping(path = "/user")
class TestController {

    @GetMapping(path = "/get/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String info(@PathVariable(name = "userId") String userId) {
        return userId;
    }

    @PutMapping(path = "/get/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String modify(@PathVariable("userId") String userId, @RequestParam(name = "sex") String sex) {
        return String.format("The userId is %1$s, and the sex is %2$s.", userId, sex);
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String login(HttpRequest request, HttpResponse response, @RequestParam(name = "account") String account,
                 @RequestParam(name = "password") String password) {
        
        /*
        Session session = request.getValidSession();
        session.setAttribute(LoginInterceptor.LOGIN_ATTRIBUTE, true);

        Cookie cookie = new Cookie("account", account + "=" + password);
        response.addCookie(cookie);
        */
        
        String[] ta = account.split(" ", 0);

        int tl = ta.length;
        int i=0, ti;
        String t, MR="";

        Stack<String> S = new Stack<String>();

        while(i<tl) {
            t=ta[i];

            if (t.equals("+")) {

                S.push( Integer.toString(Integer.parseInt(S.pop()) + Integer.parseInt( S.pop() ) ));

            }
            else if (t.equals("t:")) {

                MR = S.pop();

            }
            else
                S.push(t);

            i++;
        }

        return " MR "+ MR +" S.size " + S.size() + " tl " + ta.length + " " + account +" Login successful.";
        // return "Login successful.";
    }

    @Addition(stringType = "login", booleanType = true)
    @GetMapping(path = "/userInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    UserInfo userInfo(@CookieValue("account") String account) {
        Logger.i("Account: " + account);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("123");
        userInfo.setUserName("AndServer");
        return userInfo;
    }

    @PostMapping(path = "/upload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String upload(@RequestParam(name = "header") MultipartFile file) throws IOException {
        File localFile = FileUtils.createRandomFile(file);
        file.transferTo(localFile);
        return localFile.getAbsolutePath();
    }

    @GetMapping(path = "/consume", consumes = {"application/json", "!application/xml"})
    String consume() {
        return "Consume is successful";
    }

    @GetMapping(path = "/produce", produces = {"application/json; charset=utf-8"})
    String produce() {
        return "Produce is successful";
    }

    @GetMapping(path = "/include", params = {"name=123"})
    String include(@RequestParam(name = "name") String name) {
        return name;
    }

    @GetMapping(path = "/exclude", params = "name!=123")
    String exclude() {
        return "Exclude is successful.";
    }

    @GetMapping(path = {"/mustKey", "/getName"}, params = "name")
    String getMustKey(@RequestParam(name = "name") String name) {
        return name;
    }

    @PostMapping(path = {"/mustKey", "/postName"}, params = "name")
    String postMustKey(@RequestParam(name = "name") String name) {
        return name;
    }

    @GetMapping(path = "/noName", params = "!name")
    String noName() {
        return "NoName is successful.";
    }

    @PostMapping(path = "/formPart")
    UserInfo forPart(@FormPart(name = "user") UserInfo userInfo) {
        return userInfo;
    }

    @PostMapping(path = "/jsonBody")
    UserInfo jsonBody(@RequestBody UserInfo userInfo) {
        return userInfo;
    }

    @PostMapping(path = "/listBody")
    List<UserInfo> jsonBody(@RequestBody List<UserInfo> infoList) {
        return infoList;
    }
}
