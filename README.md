Information
===========

My idea for an elasticsearch and kibana application was to simply 
extract the current video information from youtube's trending section. 
Information extracted is video Title, uploader, date, views, description, and 
category. Using kibana I can visualize and seek out trends among these trending videos.

This code is a parser for the current information of youtube's
 trending video section of their dashboard. Running this code 
will output a file containing info about each video's title, 
author, date uploaded in epoch, and view count. This is all 
formated to json.

The javascript code from Handler.js uploadings the trendingvideos.json to the
 elasticsearch index, 'trending'. It will update existing documents.

Note that these two programs are set to run once everyday at about 6 AM PST.

Tool used to automate: Linux Crontabs. Schedule uploading with handler.js and retrieving new data from youtube with App.java

Tools used: AWS Elastic Search, Kibana, JSOUP, GSON.
