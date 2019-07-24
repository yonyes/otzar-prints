package com.yes.main;


import com.yes.services.RemoteSiteService;

public class OtzarPrints {
    public static void main(String[] args) {
        RemoteSiteService remoteSiteService =
                new RemoteSiteService();
        if (args.length>=2) {
            remoteSiteService.grabTheThreadToFile(Integer.parseInt(args[0]),
                    args[1]);
        }
    }
}
