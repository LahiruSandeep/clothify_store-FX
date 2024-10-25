package edu.icet.service;

public class ServiceFactory {

   //    Singleton design pattern
    private static ServiceFactory instance;
    private ServiceFactory(){}

    public static ServiceFactory getInstance() {
        return instance==null?instance=new ServiceFactory():instance;
    }
}
