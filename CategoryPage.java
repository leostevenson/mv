package com.vuclip.mv;

import java.util.StringTokenizer;

import com.vuclip.mv.DefaultPage.DemoJavaScriptInterface;
import com.vuclip.mv.DefaultPage.MyWebChromeClient;
import android.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class CategoryPage extends Activity{
	private static final String LOG_TAG = "ChannelPage";
	 private WebView mWebView;
	 //Button  btnBack;
	 private Handler mHandler = new Handler();
	 
	 private Intent parentIntent;
	 static public CategoryPage cp = null;
	 public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       cp = this;
       
       parentIntent = this.getIntent().getParcelableExtra("defaultIntent");
       setContentView(com.vuclip.mv.R.layout.defaultpage);
       //setContentView(cn.getview.com.R.layout.channelpage);
       
      /* btnBack = (Button) findViewById(cn.getview.com.R.id.btn_back);
       btnBack.setOnClickListener(
               new OnClickListener() {
                   public void onClick(View v) {
                   		onBack();
                   }
               });*/
       
       mWebView = (WebView) findViewById(com.vuclip.mv.R.id.webview);
       mWebView.setFocusableInTouchMode(true);
       WebSettings webSettings = mWebView.getSettings();
       webSettings.setSavePassword(false);
       webSettings.setSaveFormData(false);
       webSettings.setJavaScriptEnabled(true);
       webSettings.setSupportZoom(false);
       //webSettings.setLightTouchEnabled(false);
      
    
       webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
       webSettings.setPluginsEnabled(false);
       webSettings.setSupportMultipleWindows(false);
 

       MyWebViewClient wv = new MyWebViewClient(); 
       //wv.shouldOverrideUrlLoading(mWebView, "http://wap.lanpingguo.com"); 
      // webView.getSettings().setLoadsImagesAutomatically(true); 
       mWebView.setWebViewClient(wv); 
      
       
       mWebView.setWebChromeClient(new MyWebChromeClient());

       //mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");
       //mWebView.setFocusable(false);
       String url;
   	   url = String.format(commonSetting.getInstance().pageCategory,commonSetting.getInstance().strDomain,commonSetting.getInstance().myId);       
       mWebView.loadUrl(url);
   }
	 
	 public boolean onCreateOptionsMenu(Menu menu) {
		 //if(!mWebView.canGoBack())
		//	 return false;
		 MenuInflater inflater = getMenuInflater();
	     inflater.inflate(com.vuclip.mv.R.menu.menu_category, menu);
		 return true;
	 }
	 
	 public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case com.vuclip.mv.R.id.menu_back:
	        	onBack();
	        	return true;
//	        case cn.getview.com.R.id.menu_setting:
//	        	GetView.OnMsg(commonSetting.MSG_SETTING, null);
//	        	return true;
	        case com.vuclip.mv.R.id.menu_channel:
	        	VuclipMV.OnMsg(commonSetting.MSG_musicChannel, null);
	        	return true;
	        case com.vuclip.mv.R.id.menu_library:
	        	VuclipMV.OnMsg(commonSetting.MSG_DOWNLOADING, null);
	        	return true;
	        case com.vuclip.mv.R.id.menu_index:
	        	VuclipMV.OnMsg(commonSetting.MSG_INDEX, null);
	        	return true;
	        }
	        return false;
	    }
	
	 public boolean onBack() {
	        if(mWebView.canGoBack())
	        	mWebView.goBack();
	        return true;
	    }
	 
	final class DemoJavaScriptInterface {

       DemoJavaScriptInterface() {
       }
       public void clickOnAndroid() {
           mHandler.post(new Runnable() {
               public void run() {
                   mWebView.loadUrl("javascript:wave()");
               }
           });

       }
   }
	
	 final class MyWebViewClient extends WebViewClient {
		 public void onPageFinished(WebView view, String url)
		 {
			 super.onPageFinished(view,url);
			// Log.e("onPageFinished", url); 
		 }
		 
		 public void onPageStarted (WebView view, String url, Bitmap favicon)
		 {
			 super.onPageStarted(view,url,favicon);
			// Log.e("onPageStarted", url); 
		 }
		 
		 public void onReceivedError (WebView view, int errorCode, String description, String failingUrl)
		 {
			 super.onReceivedError(view,errorCode,description,failingUrl);
			// Log.e("onReceivedError", description); 
			 view.loadUrl("file:///android_asset/neterror.html");
		 }
	 }
	
	 final class MyWebChromeClient extends WebChromeClient {
	        @Override
	        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
	        	view.refreshDrawableState();
	          //  Log.d(LOG_TAG, message);
	            StringTokenizer st = new StringTokenizer(message,"-");
	            String[] items = new String[st.countTokens()];
	            int i = 0;
	            String title;
	            while(st.hasMoreTokens())
	            {
	            	items[i++] = st.nextToken();
	            }
	            if( i > 0)
	            {
	            	if(items[0].equals("TITLE"))
	            	{
	            		title = items[1];
	            	}
	            	else if(items[0].equals("ID"))
	            	{
	            		structs data = new structs();
	            		data._complete = false;
	            		data._movieID = Integer.parseInt(items[1]);
	            		data._type = Integer.parseInt(items[3]);
	            		if( i > 4)
	            		{
	            			//streaming
	            			data.strMovieUrl = items[5];
	            			VuclipMV.OnMsg(commonSetting.MSG_STREAMING,data);
	            			//Intent videoIntent = this.getIntent()
	            			//parentIntent.putExtra("path", data.strMovieUrl);
	                		//startActivityForResult(parentIntent, 0);
	            		}
	            		else
	            		{
	            			//download
	            			//GetView.OnMsg(commonSetting.MSG_DOWNLOAD,data);
	            			//download
	            			//ChannelPage mChannelPage = new ChannelPage();
	            			DataManager mdbhelper = new DataManager(cp);
	    		       	    mdbhelper.open();
	    		       	   // Cursor inLib = mdbhelper.fetchAllNotes();
	    		       	    Cursor movieIDCursor= mdbhelper.checkDuplicateDownload(data._movieID);
	    		       	   // Log.v("Oct 21, DB match test before downloading", "Duplicate found:" + movieIDCursor.getCount());
	    		       	    if(movieIDCursor.getCount()==0){
	    		       	    	VuclipMV.OnMsg(commonSetting.MSG_DOWNLOAD,data);}
	    		       	    mdbhelper.close();
	            		}	
	            	}
	            	else
	            	{
	        
	            	}
	            }
	            result.confirm();
	            return true;
	        }
	    }
}
