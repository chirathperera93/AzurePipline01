package com.ayubo.life.ayubolife.design_patterns;

/**
 * Created by appdev on 12/30/2017.
 */

public class Singleton_Common_Functions {

    private static Singleton_Common_Functions singleton = new Singleton_Common_Functions( );

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private Singleton_Common_Functions() { }

    /* Static 'instance' method */
    public static Singleton_Common_Functions getInstance( ) {
        return singleton;
    }

    /* Other methods protected by singleton-ness */
    public void demoMethod( ) {
        System.out.println("demoMethod for singleton");
    }

}
