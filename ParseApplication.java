package com.example.topapps;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class  ParseApplication {
    private static final String TAG = "parseApplication";
    private ArrayList<FeedEntry> applications;

    public ParseApplication() {
        this.applications = new ArrayList<>();
    }

    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }
    public boolean parse(String xmlData){
        boolean status = true;
        FeedEntry currentRecord = null;
        boolean inEntry = false;
        String textValue = "";
        boolean gotImage = false;
        String mullifier = null;

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch(eventType) {

                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse: starting tag for " + tagName);
                        if ("entry".equalsIgnoreCase(tagName)) {
                            Log.d(TAG, "parse: found ENTRY");
                            inEntry = true;
                            currentRecord = new FeedEntry();
                        }
                        /*else if("image".equalsIgnoreCase(tagName) && inEntry){
                            String imageResolution = xpp.getAttributeName((int)mullifier, "height");
                            if (imageResolution != null){
                                gotImage = "170".equalsIgnoreCase(imageResolution);
                            }
                        }*/
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending tag for" + tagName);
                        if (inEntry) {

                            if ("entry".equalsIgnoreCase(tagName)) {
                                applications.add(currentRecord);
                                Log.d(TAG, "parse: inside entry end");
                                inEntry = false;
                            } else if ("name".equalsIgnoreCase(tagName)) {
                                Log.d(TAG, "parse: name entry"+ textValue);
                                currentRecord.setName(textValue);
                            } else if ("artist".equalsIgnoreCase(tagName)) {
                                Log.d(TAG, "parse: inside artist");
                                currentRecord.setArtist(textValue);
                            } else if ("releaseDate".equalsIgnoreCase(tagName)) {
                                Log.d(TAG, "parse: inside release Date");
                                currentRecord.setReleaseDate(textValue);
                            } else if ("summary".equalsIgnoreCase(tagName)) {
                                Log.d(TAG, "parse: inside summary");
                                currentRecord.setSummary(textValue);
                            }
                            else if("image".equalsIgnoreCase(tagName)){
                                /*if(gotImage){
                                currentRecord.setName(textValue);}*/
                                Log.d(TAG, "parse: inside summary");
                                currentRecord.setImageURL(textValue);
                            }

                        }
                        break;
                    default:
                        //nothing to do
                }


                eventType = xpp.next();

            }
            for(FeedEntry app: applications){
                Log.d(TAG, "parse: ******* "+ app.toString());
            }

        }
        catch (Exception e){
            status = false;
            Log.e(TAG, "parse: " + e.getMessage());
            e.printStackTrace();
        }
            return status;
    }
}

