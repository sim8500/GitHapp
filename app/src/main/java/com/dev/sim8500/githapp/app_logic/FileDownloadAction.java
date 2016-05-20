package com.dev.sim8500.githapp.app_logic;

import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import okio.BufferedSink;
import okio.Okio;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.OnErrorThrowable;

/**
 * Created by sbernad on 18.05.16.
 */
public class FileDownloadAction implements Observable.OnSubscribe<File> {

    protected File resultFile;
    protected String inputUrl;
    protected OkHttpClient httpClient;

    public FileDownloadAction(OkHttpClient client, File file, String url) {
        resultFile = file;
        inputUrl = url;
        httpClient = client;
    }

    @Override
    public void call(Subscriber<? super File> sub) {

        if (!resultFile.exists()) {
            Request request = new Request.Builder().url(inputUrl).addHeader("Accept", "application/vnd.github.v3.raw").build();

            Response response = null;
            try {
                response = httpClient.newCall(request).execute();
                if (!response.isSuccessful()) {
                        throw new IOException();
                }
            } catch (IOException io) {
                throw OnErrorThrowable.from(OnErrorThrowable.addValueAsLastCause(io, inputUrl));
            }

            try (BufferedSink sink = Okio.buffer(Okio.sink(resultFile))) {
                sink.writeAll(response.body().source());
                sink.close();

                sub.onNext(resultFile);
            } catch (IOException io) {
                Log.e("FileContentFrag", io.getMessage());
                throw OnErrorThrowable.from(OnErrorThrowable.addValueAsLastCause(io, inputUrl));
            }
        }
        else {
            sub.onNext(resultFile);
        }
    }

}
