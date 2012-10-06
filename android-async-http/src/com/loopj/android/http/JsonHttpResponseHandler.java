/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.loopj.android.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;

/**
 * Used to intercept and handle the responses from requests made using
 * {@link AsyncHttpClient}, with automatic parsing into a {@link JSONObject}
 * or {@link JSONArray}.
 * <p>
 * This class is designed to be passed to get, post, put and delete requests
 * with the {@link #onSuccess(JSONObject)} or {@link #onSuccess(JSONArray)}
 * methods anonymously overridden.
 * <p>
 * Additionally, you can override the other event methods from the
 * parent class.
 * @param <T>
 */
public class JsonHttpResponseHandler<T> extends AsyncHttpResponseHandler<T> {
    private final Class<T> resultType; 
    
    public JsonHttpResponseHandler(Class<T> resultType){
    	this.resultType = resultType;
    }
    //
    // Callbacks to be overridden, typically anonymously
    //

    /**
     * Fired when a request returns successfully and contains a json array
     * at the base of the response string. Override to handle in your
     * own code.
     * @param response the parsed json array found in the server response (if any)
     */
    
    @Override
    public void onSuccess(T response){
    	
    }

    //
    // Pre-processing of messages (executes in background threadpool thread)
    //

    @Override
    protected void sendSuccessMessage(String responseBody) {
        try {
            T jsonResponse = parseResponse(responseBody);
            sendMessage(obtainMessage(SUCCESS_MESSAGE, jsonResponse));
        } catch(JSONException e) {
            sendFailureMessage(e, responseBody);
        }
    }


    //
    // Pre-processing of messages (in original calling thread, typically the UI thread)
    //

    protected T parseResponse(String responseBody) throws JSONException {
        T result = null;
        
        Gson gson = new Gson();
        //trim the string to prevent start with blank, and test if the string is valid JSON, because the parser don't do this :(. If Json is not valid this will return null
		responseBody = responseBody.trim();
		if(responseBody.startsWith("{") || responseBody.startsWith("[")) {
			result = gson.fromJson(responseBody, resultType);
		}
		return result;
    }
}
