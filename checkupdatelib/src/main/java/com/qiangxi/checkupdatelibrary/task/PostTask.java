package com.qiangxi.checkupdatelibrary.task;
/*
 * Copyright © qiangxi(任强强)
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

import com.qiangxi.checkupdatelibrary.request.IRequest;
import com.qiangxi.checkupdatelibrary.request.PostRequest;

import java.util.Map;

/**
 * PostTask
 */

public class PostTask extends BaseTask<PostTask> {
    public PostTask(String url) {
        super(url);
    }

    public PostTask(String url, Map<String, String> params) {
        super(url, params);
    }

    @Override
    public void execute() {
        IRequest request = new PostRequest(mUrl, mParams, mCallback);
        request.request();
    }
}
