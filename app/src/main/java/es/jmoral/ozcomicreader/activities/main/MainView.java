package es.jmoral.ozcomicreader.activities.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Created by owniz on 13/04/17.
 */

interface MainView {
    void setFragment(@NonNull Fragment fragment, String tag);
    void showShareDialog();
    void openMail();
    void showFileChooserDialog();
    void setNavViewColor();
    void openDonate();
}
