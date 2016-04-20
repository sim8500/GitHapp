package com.dev.sim8500.githapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.sim8500.githapp.app_logic.AuthRequestsManager;
import com.dev.sim8500.githapp.app_logic.FileLineBinder;
import com.dev.sim8500.githapp.app_logic.FileLinePresenter;
import com.dev.sim8500.githapp.app_logic.FilePatchParser;
import com.dev.sim8500.githapp.app_logic.GitHappCurrents;
import com.dev.sim8500.githapp.app_logic.RecyclerBaseAdapter;
import com.dev.sim8500.githapp.models.FileLineModel;
import com.dev.sim8500.githapp.models.FileModel;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import okio.BufferedSink;
import okio.Okio;
import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sbernad on 03.04.16.
 */
public class FileContentFragment extends ContentFragment {

    @Bind(R.id.header_filename)
    protected TextView filenameTxtView;

    protected RecyclerBaseAdapter<FileLineModel, FileLinePresenter> adapterInstance = new RecyclerBaseAdapter<>(new FileLineBinder());

    protected OkHttpClient httpClient = new OkHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layoutResId = R.layout.fragment_file_content;

        View inflatedView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, inflatedView);

        recyclerView.setAdapter(adapterInstance);

        return inflatedView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GitHappApp.getInstance().inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        FileModel fileModel = appCurrents.getCurrent("FileModel");

        filenameTxtView.setText(fileModel.filename);

        if(fileModel != null) {
            progressBar.setVisibility(View.VISIBLE);

            downloadFile(fileModel).subscribeOn(Schedulers.io())
                                   .observeOn(AndroidSchedulers.mainThread())
                                   .unsubscribeOn(Schedulers.io())
                                   .subscribe(new Subscriber<List<FileLineModel>>() {
                                       @Override
                                       public void onCompleted() {

                                       }

                                       @Override
                                       public void onError(Throwable e) {
                                           Toast.makeText(FileContentFragment.this.getContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                                       }

                                       @Override
                                       public void onNext(List<FileLineModel> value) {
                                           progressBar.setVisibility(View.GONE);
                                           displayFileContent(value);
                                       }
                                   });
        }
    }

    protected List<FileLineModel> loadFile(File file) {

        List<FileLineModel> resultList = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        }
        catch (FileNotFoundException ex) {
            Log.e("FileContentFrag", ex.getMessage());
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        FileModel fileModel = appCurrents.getCurrent("FileModel");
        @FileModel.FileStatus int fileStatus = fileModel.getStatus();

        if(fileStatus == FileModel.FILE_STATUS_MODIFIED) {
            resultList = loadPatchedFile(reader, fileModel.patch);
        }
        else {
            resultList = loadUniformFile(reader, getDefaultPatchStatus(fileStatus));
        }

        try {
            reader.close();
            inputStream.close();
        }
        catch(IOException ex) {
            Log.e("FileContentFrag", ex.getMessage());
        }

        return resultList;
    }

    protected List<FileLineModel> loadPatchedFile(BufferedReader reader, String patchString) {
        List<FileLineModel> resultList = new ArrayList<>();
        List<FileLineModel> patchLines = new FilePatchParser(patchString).parsePatchString();

        boolean done = false;
        int lineCounter = 1;
        int patchLineIndex = 0;
        String line = null;

        while (!done) {

            if(patchLineIndex >= patchLines.size() || lineCounter < patchLines.get(patchLineIndex).lineNumber) {
                line = readBufferLine(reader);
                done = (line == null);

                if (line != null) {
                    FileLineModel flm = new FileLineModel();
                    flm.lineNumber = lineCounter;
                    flm.lineContent = line;
                    flm.lineStatus = FileLineModel.PATCH_STATUS_NONE;

                    resultList.add(flm);
                    ++lineCounter;
                }
            }
            else {
                FileLineModel flm = patchLines.get(patchLineIndex);
                resultList.add(flm);
                ++patchLineIndex;

                if(flm.lineStatus == FileLineModel.PATCH_STATUS_ADDED) {
                    line = readBufferLine(reader);
                    done = (line == null);
                    ++lineCounter;
                }
            }
        }

        return resultList;
    }

    protected List<FileLineModel> loadUniformFile(BufferedReader reader, @FileLineModel.PatchStatus int lineStatus) {
        List<FileLineModel> resultList = new ArrayList<>();

        boolean done = false;
        int lineCounter = 1;

        while (!done) {
            final String line = readBufferLine(reader);
            done = (line == null);

            if (line != null) {
                FileLineModel flm = new FileLineModel();
                flm.lineNumber = lineCounter;
                flm.lineContent = line;
                flm.lineStatus = lineStatus;

                resultList.add(flm);
            }
            ++lineCounter;
        }
        return resultList;
    }

    protected String readBufferLine(BufferedReader reader) {
        String resLine = null;
        try {
            resLine = reader.readLine();
        }
        catch(IOException ex) {
            Log.e("FileContentFrag", ex.getMessage());
        }
        return resLine;
    }

    protected @FileLineModel.PatchStatus int getDefaultPatchStatus(@FileModel.FileStatus int fileStatus) {

        switch(fileStatus) {
            case FileModel.FILE_STATUS_ADDED:
                return FileLineModel.PATCH_STATUS_ADDED;

            case FileModel.FILE_STATUS_REMOVED:
                return FileLineModel.PATCH_STATUS_DELETED;

            default:
                return FileLineModel.PATCH_STATUS_NONE;
        }
    }

    protected Observable<List<FileLineModel>> downloadFile(final FileModel fileModel)
    {
        return Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> sub) {
                int lastSep = TextUtils.lastIndexOf(fileModel.filename, '/');
                String pureName = fileModel.filename.substring(lastSep + 1);
                File file = new File(FileContentFragment.this.getContext().getExternalCacheDir() + File.separator + pureName);

                Request request = new Request.Builder().url(fileModel.raw_url).build();

                Response response = null;
                try {
                    response = httpClient.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        throw new IOException();
                    }
                } catch (IOException io) {
                    throw OnErrorThrowable.from(OnErrorThrowable.addValueAsLastCause(io, fileModel.filename));
                }

                try (BufferedSink sink = Okio.buffer(Okio.sink(file))) {
                    sink.writeAll(response.body().source());
                    sink.close();


                    sub.onNext(file);
                } catch (IOException io) {
                    Log.e("FileContentFrag", io.getMessage());
                    throw OnErrorThrowable.from(OnErrorThrowable.addValueAsLastCause(io, fileModel.filename));
                }
            }
        }).map(new Func1<File, List<FileLineModel>>() {
            @Override
            public List<FileLineModel> call(File file) {
                return loadFile(file);
            }
        });
    }

    @UiThread
    protected void displayFileContent(List<FileLineModel> fileContent) {

        if(fileContent != null) {
            adapterInstance.clearItems();
            adapterInstance.initAdapter(this.getContext(), fileContent);
            adapterInstance.notifyDataSetChanged();
        }
        else {
            throw new InvalidParameterException("FileContent cannot be null.");
        }
    }

}
