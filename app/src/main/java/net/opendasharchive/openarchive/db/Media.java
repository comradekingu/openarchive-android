package net.opendasharchive.openarchive.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.orm.SugarRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.opendasharchive.openarchive.R;
import net.opendasharchive.openarchive.util.Utility;

/**
 * Created by micahjlucas on 1/11/15.
 */
public class Media extends SugarRecord {

    public String originalFilePath;
    public String mimeType;
    public Date createDate;
    public Date updateDate;


    public Date uploadDate;
    public String serverUrl;

    public String title;
    public String description;
    public String author;
    public String location;
    public String tags;

    public String licenseUrl;
    public byte[] mediaHash;

    public int status;

    public long projectId;

    public long collectionId;

    public long contentLength;
    public long progress;

    public boolean flag = false;

    public int priority = 0;

    //public final static int STATUS_ERROR = 9;
    //public final static int STATUS_DELETE_LOCAL = 6;
    public final static int STATUS_DELETE_REMOTE = 7;
    //public final static int STATUS_ARCHIVED = 5;
    public final static int STATUS_UPLOADED = 5;
    public final static int STATUS_UPLOADING = 4;
    public final static int STATUS_PUBLISHED = 3;
    public final static int STATUS_QUEUED = 2;
    public final static int STATUS_LOCAL = 1;
    public final static int STATUS_NEW = 0;

    private final static String[] WHERE_NOT_DELETED = {STATUS_UPLOADED+""};

    public static enum MEDIA_TYPE {
        AUDIO, IMAGE, VIDEO, FILE;
    }

    //left public ONLY for Sugar ORM
    public Media() {};

    /* getters and setters */
    public String getOriginalFilePath() {
        return this.originalFilePath;
    }
    public void setOriginalFilePath(String originalFilePath) {
        this.originalFilePath = originalFilePath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Date getCreateDate() {
        return this.createDate;
    }
    public String getFormattedCreateDate() {
        if (this.createDate != null)
            return SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(this.createDate);
        else
            return "";
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getServerUrl() {
        return this.serverUrl;
    }
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTags() {
        return tags;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public byte[] getMediaHash ()
    {
        return mediaHash;
    }

    public void setMediaHash (byte[] mediaHash)
    {
        this.mediaHash = mediaHash;
    }

    public void setTags(String tags) {
        // repace spaces and commas with semicolons
        tags = tags.replace(' ', ';');
        tags = tags.replace(',', ';');
        this.tags = tags;
    }

    public static List<Media> getAllMediaAsList() {
        return Media.find(Media.class,"status <= ?",WHERE_NOT_DELETED,null,"ID DESC",null);
       // return Media.listAll(Media.class,);
    }

    public static List<Media> getMediaByStatus(long status) {
        String[] values = {status+""};
        return Media.find(Media.class,"status = ?",values,null,"STATUS DESC",null);
    }

    public static List<Media> getMediaByStatus(long[] statuses) {
        String[] values = new String[statuses.length];
        int idx = 0;
        for (long status: statuses)
            values[idx++] = status + "";

        StringBuffer sbWhere = new StringBuffer();
        for (int i = 0; i < values.length; i++) {
            sbWhere.append("status = ?");
            if (i + 1 < values.length)
                sbWhere.append(" OR ");
        }

        return Media.find(Media.class,sbWhere.toString(),values,null,"STATUS, PRIORITY DESC",null);
    }

    public static List<Media> getMediaByProjectAndCollection(long projectId, long collectionId) {
        String[] values = {projectId+"",collectionId+""};
        return Media.find(Media.class,"PROJECT_ID = ? AND COLLECTION_ID = ?",values,null,"STATUS, ID DESC",null);
    }

    public static List<Media> getMediaByProject(long projectId) {
        String[] values = {projectId+""};
        return Media.find(Media.class,"PROJECT_ID = ?",values,null,"STATUS, ID DESC",null);
    }

    public static List<Media> getMediaByProjectAndUploadDate(long projectId, long uploadDate) {
        String[] values = {projectId+"",uploadDate+""};
        return Media.find(Media.class,"PROJECT_ID = ? AND UPLOAD_DATE = ?",values,null,"STATUS, ID DESC",null);
    }

    public static List<Media> getMediaByProjectAndStatus(long projectId, String statusMatch, long status) {
        String[] values = {projectId+"",status+""};
        return Media.find(Media.class,"PROJECT_ID = ? AND STATUS " + statusMatch + " ?",values,null,"STATUS, ID DESC",null);
    }


    public static Media getMediaById(long mediaId) {
        return Media.findById(Media.class, mediaId);
    }

    public static boolean deleteMediaById(long mediaId) {
        Media media = Media.findById(Media.class, mediaId);
        return media.delete();
    }

    public void setProjectId (long projectId)
    {
        this.projectId = projectId;
    }

    public long getProjectId ()
    {
        return this.projectId;
    }

    public void setContentLength (long contentLength)
    {
        this.contentLength = contentLength;
    }

    public long getContentLength ()
    {
        return this.contentLength;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }


    public boolean isFlagged() {
        return flag;
    }

    public void setFlagged(boolean flag) {
        this.flag = flag;
    }


    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}