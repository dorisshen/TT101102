package com.test.tt101102;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {

    MyDataHandler dataHandler;
    //TextView tv;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataHandler = new MyDataHandler();
        //tv = (TextView) findViewById(R.id.textView);
        lv = (ListView) findViewById(R.id.listView);
        //網路連線需要另外獨立一個Thread
        new Thread(){
            @Override
            public void run() {
                super.run();
                URL url = null;
                InputStream inputStream;
                try {
                    url = new URL("http://udn.com/rssfeed/news/1");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    inputStream = conn.getInputStream();
                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                    }
                    String str = result.toString("UTF-8");
                    Log.d("NET", str);

                    SAXParserFactory spf = SAXParserFactory.newInstance();
                    SAXParser sp = spf.newSAXParser();
                    XMLReader xr = sp.getXMLReader();
                    xr.setContentHandler(dataHandler);
                    xr.parse(new InputSource(new StringReader(str)));
                    //UI上資料的變動要到回到UI上的Thread上異動;將UDN之RSS取得的資料(網路取得資源)，呈現在ListView上
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //tv.setText("Finish");
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    MainActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    dataHandler.xmlData
                            );
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent it = new Intent(MainActivity.this, DetailActivity.class);
                                    it.putExtra("link", dataHandler.XMLLink.get(position));
                                    startActivity(it);
                                }
                            });
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }
}
