package com.yes.main;


import com.yes.services.remoteSiteService;

public class main {
    public static void main(String[] args) {
        remoteSiteService remoteSiteService =
                new remoteSiteService();
        if (args.length>=2) {
            remoteSiteService.grabTheClusterToFile(Integer.parseInt(args[0]),
                    args[1]);
        }
    }
}
