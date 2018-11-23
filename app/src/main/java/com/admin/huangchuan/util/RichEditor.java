package com.admin.huangchuan.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.admin.huangchuan.View.MyWebView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Copyright (C) 2015 Wasabeef
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class RichEditor extends MyWebView {

    public interface OnTextChangeListener {

        void onTextChange(String text);
    }

    public interface AfterInitialLoadListener {
        void afterInitialLoad(boolean isReady);
    }

    private static final String SETUP_HTML = "file:///android_res/raw/editor.html";
    private static final String CALLBACK_SCHEME = "re-callback://";
    private static final String CLICK_IMG_CALLBACK = "re-click_img_callback://";
    private boolean isReady = false;
    private String mContents;
    private OnTextChangeListener mListener;
    private AfterInitialLoadListener mloadListener;
    private  int o;

    public RichEditor(Context context) {
        this(context, null);
    }

    public RichEditor(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }




    @SuppressLint("SetJavaScriptEnabled")
    public RichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        getSettings().setJavaScriptEnabled(true);
        setWebChromeClient(new WebChromeClient());


            setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    Log.d("reg", url);
                    isReady = url.equalsIgnoreCase(SETUP_HTML);
                    if (mloadListener != null) {
                        mloadListener.afterInitialLoad(isReady);
                    }
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.d("reg", url);
                    if (TextUtils.indexOf(url, CALLBACK_SCHEME) == 0) {
                        callback(url);
                        return true;
                    }
                    if (TextUtils.indexOf(url, CLICK_IMG_CALLBACK) > 0) {
                        if (o==2){
                            clickImgCallBack(url);
                        }
                        return true;
                    }

                    return super.shouldOverrideUrlLoading(view, url);
                }
            });


        loadUrl(SETUP_HTML);
    }

    public void setOnTextChangeListener(OnTextChangeListener listener) {
        mListener = listener;
    }

    public void setOnInitialLoadListener(AfterInitialLoadListener listener) {
        mloadListener = listener;
    }


    public void callback(String text) {
        try {
            String decode = URLDecoder.decode(text, "UTF-8");
            mContents = decode.replaceFirst(CALLBACK_SCHEME, "");
            if (mListener != null) {
                mListener.onTextChange(mContents);
            }
        } catch (UnsupportedEncodingException e) {
            // No handling
        }
    }

    public void clickImgCallBack(String text) {
        try {
            String decode = URLDecoder.decode(text, "UTF-8");
            final String img_id = decode.replace(CLICK_IMG_CALLBACK, "").replace("file:///android_res/raw/", "");
            new AlertDialog.Builder(getContext()).setTitle("是否删除此图片？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    delImg(img_id);

                }
            }) //为对话框设置一个”取消“按钮
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }

                    })


                    .create().show();
        } catch (UnsupportedEncodingException e) {
            Log.e("VACK", e.toString());
        }
    }

    public void  Type(int o){
        this.o =o;

    }



    public void setEditorFontSize(int px) {
        exec("javascript:RE.setFontSize('" + px + "px');");
    }

    public void setHtml(String contents) {
        if (contents == null) {
            contents = "";
        }
        try {
            exec("javascript:RE.setHtml('" + URLEncoder.encode(contents, "UTF-8") + "');");
        } catch (UnsupportedEncodingException e) {
            // No handling
        }
        mContents = contents;
    }

    public String getHtml() {
        return mContents;
    }

    public void setEditorBackgroundColor(int color) {
        String hex = convertHexColorString(color);
        exec("javascript:RE.setBackgroundColor('" + hex + "');");
    }

    public void setEditorWidth(int px) {
        exec("javascript:RE.setWidth('" + px + "px');");
    }

    public void setEditorHeight(int px) {
        exec("javascript:RE.setHeight('" + px + "px');");
    }

    public void setPlaceholder(String placeholder) {
        exec("javascript:RE.setPlaceholder('" + placeholder + "');");
    }

    public void loadCSS(String cssFile) {
        String jsCSSImport =
                "(function() {" +
                        "    var head  = document.getElementsByTagName(\"head\")[0];" +
                        "    var link  = document.createElement(\"link\");" +
                        "    link.rel  = \"stylesheet\";" +
                        "    link.type = \"text/css\";" +
                        "    link.href = \"" + cssFile + "\";" +
                        "    link.media = \"all\";" +
                        "    head.appendChild(link);" +
                        "}) ();";
        exec("javascript:" + jsCSSImport + "");
    }

    //上一步
    public void undo() {
        exec("javascript:RE.undo();");
    }

    //下一步
    public void redo() {
        exec("javascript:RE.redo();");
    }

    //加粗
    public void setBold() {
        exec("javascript:RE.setBold();");
    }

    //倾斜
    public void setItalic() {
        exec("javascript:RE.setItalic();");
    }

    //右下小字
    public void setSubscript() {
        exec("javascript:RE.setSubscript();");
    }

    //右上小字
    public void setSuperscript() {
        exec("javascript:RE.setSuperscript();");
    }

    //中划线
    public void setStrikeThrough() {
        exec("javascript:RE.setStrikeThrough();");
    }

    //下划线
    public void setUnderline() {
        exec("javascript:RE.setUnderline();");
    }

    //字体颜色
    public void setTextColor(int color) {
        exec("javascript:RE.prepareInsert();");

        String hex = convertHexColorString(color);
        exec("javascript:RE.setTextColor('" + hex + "');");
    }

    //字体背景颜色
    public void setTextBackgroundColor(int color) {
        exec("javascript:RE.prepareInsert();");
        String hex = convertHexColorString(color);
        exec("javascript:RE.setTextBackgroundColor('" + hex + "');");
    }

    public void setBullets() {
        exec("javascript:RE.setBullets();");
    }

    public void setNumbers() {
        exec("javascript:RE.setNumbers();");
    }

    public void insertTodo() {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.setTodo('" + Utils.getCurrentTime() + "');");
    }

    //字体大小h1-h6
    public void setHeading(int heading) {
        exec("javascript:RE.setHeading('" + heading + "');");
    }

    //左缩进
    public void setIndent() {
        exec("javascript:RE.setIndent();");
    }

    //右缩进
    public void setOutdent() {
        exec("javascript:RE.setOutdent();");
    }

    //左对齐
    public void setAlignLeft() {
        exec("javascript:RE.setJustifyLeft();");
    }

    //居中对齐
    public void setAlignCenter() {
        exec("javascript:RE.setJustifyCenter();");
    }

    //右对齐
    public void setAlignRight() {
        exec("javascript:RE.setJustifyRight();");
    }

    //加入引用，不知道有什么用
    public void setBlockquote() {
        exec("javascript:RE.setBlockquote();");
    }

    //插入图片，大概要添加一张就在
    public void insertImage(String url, String alt) {
        String img_id = System.currentTimeMillis() + "";
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertImage('" + url + "', '" + alt + "','" + img_id + "');");
    }

    public void delImg(String img_id) {
        exec("javascript:RE.delImage(" + img_id + ");");
        exec("javascript:RE.reload();");
    }






    //插入链接
    public void insertLink(String href, String title) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertLink('" + href + "', '" + title + "');");

    }

    public void setEditorFontColor(int color) {
        String hex = convertHexColorString(color);
        exec("javascript:RE.setBaseTextColor('" + hex + "');");
    }

    public void focusEditor() {
        requestFocus();
        exec("javascript:RE.focus();");
    }

    public void clearFocusEditor() {
        exec("javascript:RE.blurFocus();");
    }

    private String convertHexColorString(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    private void exec(String trigger) {
        if (isReady) {
            load(trigger);
        } else {
            new waitLoad(trigger).execute();
        }
    }

    private void load(String trigger) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(trigger, null);
        } else {
            loadUrl(trigger);
        }
    }

    private class waitLoad extends AsyncTask<Void, Void, Void> {

        private String mTrigger;

        public waitLoad(String trigger) {
            super();
            mTrigger = trigger;
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (!RichEditor.this.isReady) {
                sleep(100);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            load(mTrigger);
        }

        private synchronized void sleep(long ms) {
            try {
                wait(ms);
            } catch (InterruptedException ignore) {
            }
        }
    }


}