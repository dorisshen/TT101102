package com.test.tt101102;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by teacher on 2016/10/11.
 */

public class MyDataHandler extends DefaultHandler {
    boolean isTitle = false;
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("title"))
        {
            isTitle = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equals("title"))
        {
            isTitle = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (isTitle == true)
        {
            String fetchStr = new String(ch).substring(start, start + length);
            Log.d("TITLE", fetchStr);
        }
    }
}