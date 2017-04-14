package es.jmoral.simplecomicreader.models;

/**
 * Created by owniz on 14/04/17.
 */

public class Comic {
    private Long _id;
    private String coverPath;
    private String filePath;
    private long AddedTimeStamp;
    private String title;
    private int numPages;
    private int currentPage;

    public Comic() {
        this.coverPath = "noCoverPath";
        this.filePath = "noFilePath";
        this.AddedTimeStamp = 0;
        this.title = "noTitle";
        this.numPages = 0;
        this.currentPage = 0;
    }

    public Comic(String coverPath, String filePath, long AddedTimeStamp, String title, int numPages, int currentPage) {
        this.coverPath = coverPath;
        this.filePath = filePath;
        this.AddedTimeStamp = AddedTimeStamp;
        this.title = title;
        this.numPages = numPages;
        this.currentPage = currentPage;
    }

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
        return AddedTimeStamp;
    }

    public void setAddedTimeStamp(long addedTimeStamp) {
        AddedTimeStamp = addedTimeStamp;
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
}
