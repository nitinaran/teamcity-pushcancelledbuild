package com.teamcity.pushcancelledbuild;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.serverSide.BuildServerAdapter;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SQueuedBuild;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildEventListener extends BuildServerAdapter {

    public static final Logger Log = Logger.getInstance(BuildEventListener.class.getName());
    private final SBuildServer mySBuildServer;
    private final Pattern rawTriggeredBy;

    public BuildEventListener(SBuildServer mySBuildServer) {
        this.mySBuildServer = mySBuildServer;
        this.rawTriggeredBy = Pattern.compile(".*type='(?<type>reAddedOnStop)'.*");
    }

    @Override
    public void buildTypeAddedToQueue(SQueuedBuild queuedBuild) {
        Log.debug("Added to queue build id: " + queuedBuild.getBuildPromotion().getId());
        Log.debug("Raw Triggered By: " + queuedBuild.getTriggeredBy().getRawTriggeredBy());
        Matcher matcher = this.rawTriggeredBy.matcher(queuedBuild.getTriggeredBy().getRawTriggeredBy());
        if (matcher.find()) {
            Log.info("MOVE_TO_TOP:: Moving the build to the top of the queue");
            mySBuildServer.getQueue().moveTop(Long.toString(queuedBuild.getBuildPromotion().getId()));
        }
    }
}
