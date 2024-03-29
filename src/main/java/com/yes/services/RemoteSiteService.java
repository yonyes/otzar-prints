package com.yes.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;


public class RemoteSiteService {
    private static final String OTZAR_URL_PREFIX = "https://forum" +
            ".otzar" +
            ".org/viewtopic" +
            ".php?f=190&t=";
    private static final String OTZAR_URL_POSTFIX = "&view=print";
    private static final int COMMENTS_PER_PAGE = 40;

    public void grabTheThreadToFile(int thread,
                                    String fileName) {
        String firstPage = getAllPages(thread);
        saveToHtmlFile(fileName, firstPage);
    }


    private void saveToHtmlFile(String fileName, String html) {
        try {
            BufferedWriter bw = new BufferedWriter
                    (new OutputStreamWriter(new FileOutputStream(fileName+".html"), StandardCharsets.UTF_8));
            bw.write(html);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getAllPages(int thread) {
        StringBuilder allPages = new StringBuilder();
        System.out.println("start working on the first page");
        Document firstPage = getSinglePage(thread, 0);
        int pagesAmount = Integer.parseInt(firstPage.selectFirst("div" +
                ".page-number").getAllElements().get(2).text());
        allPages.append(firstPage.outerHtml());

        for (int page = 1; page < pagesAmount; page++) {
            System.out.printf("Now working on page: %s%n",
                    (page + 1));
            Document tempPage = getSinglePage(thread, page);
            tempPage.select("div#page-header").remove();
            allPages.append(tempPage);
        }

        return allPages.toString();
    }

    private Document getSinglePage(int thread, int pageNumber) {
        String threadAddress =
                MessageFormat.format("{0}{1}&start={2}{3}",
                        OTZAR_URL_PREFIX,
                        String.valueOf(thread),
                        String.valueOf(pageNumber * COMMENTS_PER_PAGE),
                        OTZAR_URL_POSTFIX);
        try {
            Document doc = Jsoup.connect(threadAddress).get();
            doc.charset(Charset.forName("UTF-8"));
            return doc;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
