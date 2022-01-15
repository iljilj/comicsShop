package ru.sfedu.comicsShop.utils;

import java.util.Objects;

public class HistoryContent {
    private long id;
    private String className;
    private String createdDate;
    private String actor;
    private String methodName;
    private Object object;
    private Status status;

    public HistoryContent() {
    }

//    public HistoryContent(String className, Date createdDate, String actor) {
//        this.className = className;
//        this.createdDate = createdDate;
//        this.actor = actor;
//    }

    public HistoryContent(long id, String className, String createdDate, String actor, String methodName, Object object, Status status) {
        this.id = id;
        this.className = className;
        this.createdDate = createdDate;
        this.actor = actor;
        this.methodName = methodName;
        this.object = object;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryContent that = (HistoryContent) o;
        return id == that.id && className.equals(that.className) && createdDate.equals(that.createdDate) && actor.equals(that.actor) && methodName.equals(that.methodName) && object.equals(that.object) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, className, createdDate, actor, methodName, object, status);
    }

    @Override
    public String toString() {
        return "HistoryContent{" +
                "id=" + id +
                ", className='" + className + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", actor='" + actor + '\'' +
                ", methodName='" + methodName + '\'' +
                ", object=" + object +
                ", status=" + status +
                '}';
    }
}
