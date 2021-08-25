package pro.network.jvrmobiles.web;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import pro.network.jvrmobiles.R;


public class WebFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main_webview, container, false);

        WebView webView = rootView.findViewById(R.id.webview);
        final ProgressBar webProgress = rootView.findViewById(R.id.webProgress);
        webProgress.setVisibility(View.VISIBLE);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new MyWebViewClient() {

            public void onPageFinished(WebView view, String url) {
                webProgress.setVisibility(View.GONE);
            }
        });
        webView.loadUrl("https://www.google.com/maps/place/JVR+CELL+SERVICES/@11.0715731,79.5559677,17z/data=!3m1!4b1!4m5!3m4!1s0x3a5529139cc16a05:0x36902f002d702d3d!8m2!3d11.0715731!4d79.5581564");
      return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
         //   finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
