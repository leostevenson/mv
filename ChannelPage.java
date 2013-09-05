package com.vuclip.mv;

import java.util.StringTokenizer;



import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import com.vuclip.mv.DefaultPage.DemoJavaScriptInterface;
import com.vuclip.mv.DefaultPage.MyWebChromeClient;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.AbsListView.OnScrollListener;

public class ChannelPage extends Activity  implements AbsListView.OnScrollListener{
	 private static final String LOG_TAG = "SearchPage";
	 static private WebView mWebView;
	 private Handler mHandler = new Handler();

	// Button mStartSearch;
	 private Intent parentIntent;
	static public ChannelPage sp = null;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        sp = this;
	        DataManager mdbhelper=new DataManager(this);
	        parentIntent = this.getIntent().getParcelableExtra("SearchPage");
	        setContentView(com.vuclip.mv.R.layout.defaultpage);
	        //setContentView(R.layout.searchpage);
	        mWebView = (WebView) findViewById(R.id.webview);
	        
	        WebViewClient wv = new MyWebViewClient(); 
	        mWebView.setWebViewClient(wv); 
	        mWebView.setFocusableInTouchMode(true);
	        WebSettings webSettings = mWebView.getSettings();
	        webSettings.setSavePassword(false);
	        webSettings.setSaveFormData(false);
	        webSettings.setJavaScriptEnabled(true);
	        webSettings.setSupportZoom(false);
//	        webSettings.setCacheMode(1);
	       // webSettings.setLightTouchEnabled(false);
	     /*   mStartSearch = (Button) findViewById(R.id.btn_start_search);
	        mStartSearch.setOnClickListener(
	                new OnClickListener() {
	                    public void onClick(View v) {
	                    		onSearchRequested();
	                    }
	                });*/
	        mWebView.setWebChromeClient(new MyWebChromeClient());
	
	        String url;
//	  Sept 22      
	        url = String.format(commonSetting.getInstance().pageChannel,commonSetting.getInstance().strDomain,commonSetting.getInstance().myId);
	        mWebView.loadUrl(url);

	    }
	 
	 public boolean onCreateOptionsMenu(Menu menu) {
		 MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.menu_channels, menu);
		 return true;
	 }
	 
	 public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
//	        case R.id.menu_setting:
//	        	GetView.OnMsg(commonSetting.MSG_SETTING, null);
//	        	return true;
//	        case cn.getview.com.R.id.menu_downloading:
//	        	GetView.OnMsg(commonSetting.MSG_DOWNLOADING, null);
//	        	return true;
	        case com.vuclip.mv.R.id.menu_back:
	        	onBack();
	        	return true;
	        case com.vuclip.mv.R.id.menu_library:
	        	VuclipMV.OnMsg(commonSetting.MSG_DOWNLOADING, null);
	        	return true;
	        case com.vuclip.mv.R.id.menu_category:
	        	VuclipMV.OnMsg(commonSetting.MSG_CATEGORY, null);
	        	return true;
	        case com.vuclip.mv.R.id.menu_channel:
	        	VuclipMV.OnMsg(commonSetting.MSG_musicChannel, null);
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
		
		 final class MyWebChromeClient extends WebChromeClient {
		        @Override
		        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
		        	view.refreshDrawableState();
		           // Log.d(LOG_TAG, message);
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
		            			Log.v("channel Page url",data.strMovieUrl);
		
		            			VuclipMV.OnMsg(commonSetting.MSG_STREAMING,data);
		            		 
		            			//Intent videoIntent = this.getIntent()
		            			//parentIntent.putExtra("path", data.strMovieUrl);
		                		//startActivityForResult(parentIntent, 0);
		            		}
		            		else
		            		{
		            			//download
		            			//ChannelPage mChannelPage = new ChannelPage();
		            			DataManager mdbhelper = new DataManager(sp);
		    		       	    mdbhelper.open();
		    		       	//    Cursor inLib = mdbhelper.fetchAllNotes();
		    		       	    Cursor movieIDCursor= mdbhelper.checkDuplicateDownload(data._movieID);
		    		       /**	 for(int k=0;k<inLib.getCount();k++){
		    		       		 inLib.moveToNext();
		    		       		// Log.v("Oct 21, in Lib URL", inLib.getString(5));
		    		       		 String currentURL = inLib.getString(5);
		    		       		 currentURL.substring(currentURL.indexOf("?c=")+3, currentURL.indexOf("u=")-1);
		    		       	 }
		    		       	   // Log.v("Oct 21, DB match test before downloading", "Duplicate found:" + movieIDCursor.getCount());*/
		    		       	    if(movieIDCursor.getCount()==0){
		    		       	    	VuclipMV.OnMsg(commonSetting.MSG_DOWNLOAD,data);}
		    		       	    mdbhelper.close();
		            		}	
		            	}
		            	if(items[0].equals("DELETE"))
		            	{
		            		String url2;
		        	        url2 = String.format(commonSetting.getInstance().pageChannel,1);
		        	        mWebView.loadUrl(url2);
		            	}
		            	else
		            	{
		        
		            	}
		            }
		            result.confirm();
		            return true;
		        }
		    }
		 
		 public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
		            int totalItemCount) {
		    }
			
			public void onScrollStateChanged(AbsListView view, int scrollState) {
		        switch (scrollState) {
		        case OnScrollListener.SCROLL_STATE_IDLE:
		            break;
		        case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
		            break;
		        case OnScrollListener.SCROLL_STATE_FLING:
		            break;
		        }
		    }
	
			protected void onSearchCalled()
			{
				Intent i = new Intent(this,SearchHold.class);
				this.startActivity(i);
			}
			
		    public boolean onSearchRequested() {
		        Bundle appDataBundle = null;
		        appDataBundle = new Bundle();
		        appDataBundle.putString("demo_key", (String)this.getResources().getText(R.string.app_name));
		        startSearch("", false, appDataBundle, false); 
			 	//Intent i = new Intent(this,SearchHold.class);
			 	//this.startActivity(i);
		        return true;
		    }
		 
		 static public  void onSearch(final Intent newIntent) 
			{
				final String queryString = newIntent.getStringExtra(SearchManager.QUERY);
				final Bundle appData = newIntent.getBundleExtra(SearchManager.APP_DATA);
				String testStr = null;
		        if (appData == null) {
		        	testStr = appData.getString("demo_key");
		        }
		      
		        if(queryString!=null)
		        {
		        	String s = String.format("%d,%s",8, queryString);
	            	LogManage.getInstance().AddToLog(s);  
		        	
		        	//Log.d(LOG_TAG, "search" + queryString);
		        	
		        	String url;
		 	        url = String.format(commonSetting.getInstance().pageChannel,commonSetting.getInstance().strDomain,queryString.toString());
		 	        
		        	mWebView.loadUrl(url);
		        }
			}

}
