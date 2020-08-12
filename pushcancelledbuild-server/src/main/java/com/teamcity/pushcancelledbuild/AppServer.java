package com.teamcity.pushcancelledbuild;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.serverSide.BuildServerListener;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.util.EventDispatcher;

public class AppServer {
    private static final Logger Log = Logger.getInstance(AppServer.class.getName());
    private final EventDispatcher<BuildServerListener> myEventDispatcher;

    public AppServer(EventDispatcher<BuildServerListener> myEventDispatcher,
                     SBuildServer mySBuildServer) {
        this.myEventDispatcher = myEventDispatcher;
        BuildEventListener buildEventListener = new BuildEventListener(mySBuildServer);
        AddListener(buildEventListener);
        Log.info(AppServer.class.getName() + " initialized");
    }

    private void AddListener(BuildEventListener buildEventListener) {
        Log.info("Registering the event listener");
        myEventDispatcher.addListener(buildEventListener);
    }
}
