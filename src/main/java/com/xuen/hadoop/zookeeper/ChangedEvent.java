package com.xuen.hadoop.zookeeper;

/**
 * @author zheng.xu
 * @since 2017-07-10
 */
public class ChangedEvent {

    public static enum Type{
        CHILD_ADDED,
        CHILD_UPDATED,
        CHILD_REMOVED;
    }

    private String path;
    private Type type;

    public ChangedEvent(String path, Type type) {
        this.path = path;
        this.type = type;
    }

    public String getPath() {
        return path;
    }



    public Type getType() {
        return type;
    }


}
