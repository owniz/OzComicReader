package es.jmoral.ozcomicreader.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by owniz on 14/04/17.
 */

public class Comic implements Parcelable {
    private Long _id;
    private String coverPath;
    private String filePath;
    private long addedTimeStamp;
    private String title;
    private int numPages;
    private int currentPage;

    public Comic() {
        this.coverPath = "noCoverPath";
        this.filePath = "noFilePath";
        this.addedTimeStamp = 0;
        this.title = "noTitle";
        this.numPages = 0;
        this.currentPage = 0;
    }

    public Comic(String coverPath, String filePath, long AddedTimeStamp, String title, int numPages, int currentPage) {
        this.coverPath = coverPath;
        this.filePath = filePath;
        this.addedTimeStamp = AddedTimeStamp;
        this.title = title;
        this.numPages = numPages;
        this.currentPage = currentPage;
    }

    protected Comic(Parcel in) {
        _id = in.readLong();
        coverPath = in.readString();
        filePath = in.readString();
        addedTimeStamp = in.readLong();
        title = in.readString();
        numPages = in.readInt();
        currentPage = in.readInt();
    }

    public static final Creator<Comic> CREATOR = new Creator<Comic>() {
        @Override
        public Comic createFromParcel(Parcel in) {
            return new Comic(in);
        }

        @Override
        public Comic[] newArray(int size) {
            return new Comic[size];
        }
    };

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getAddedTimeStamp() {
        return addedTimeStamp;
    }

    public void setAddedTimeStamp(long addedTimeStamp) {
        this.addedTimeStamp = addedTimeStamp;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(_id);
        parcel.writeString(coverPath);
        parcel.writeString(filePath);
        parcel.writeLong(addedTimeStamp);
        parcel.writeString(title);
        parcel.writeInt(numPages);
        parcel.writeInt(currentPage);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Comic && this._id.equals(((Comic) obj).get_id());
    }
}
