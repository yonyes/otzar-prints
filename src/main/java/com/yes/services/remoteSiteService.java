package com.yes.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;


public class remoteSiteService {
    final String OTZAR_URL_PREFIX = "https://forum.otzar" +
            ".org/viewtopic" +
            ".php?f=190&t=";
    final String OTZAR_URL_POSTFIX = "&view=print";

    public void grabTheClusterToFile(int cluster,
                                     String fileName) {
        String firstPage = getAllPages(cluster);
        saveToHtmlFile(fileName, firstPage);
    }


    private void saveToHtmlFile(String fileName, String html) {
        System.out.println(html);
        BufferedWriter bw =
                null;
        try {
            bw = new BufferedWriter
                    (new OutputStreamWriter(new FileOutputStream(fileName+".html"), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bw.write(html);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getAllPages(int cluster) {
        StringBuilder allPages = new StringBuilder();
        Document firstPage = getOnePage(cluster, 0);
        int pagesAmount = Integer.valueOf(firstPage.selectFirst("div" +
                ".page-number").getAllElements().get(2).text());
        allPages.append(firstPage.outerHtml());
        for (int page = 1; page < pagesAmount; page++) {
            Document tempPage = getOnePage(cluster, page * 40);
            tempPage.select("div#page-header").remove();
            allPages.append(tempPage);
        }

        return allPages.toString();
    }

    private Document getOnePage(int cluster, int startMessage) {
        String clusterAddress =
                MessageFormat.format("{0}{1}&start={2}{3}", OTZAR_URL_PREFIX, String.valueOf(cluster), String.valueOf(startMessage), OTZAR_URL_POSTFIX);
        try {
            Document doc = Jsoup.connect(clusterAddress).get();
            doc.charset(Charset.forName("UTF-8"));
            return doc;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
