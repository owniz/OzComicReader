package es.jmoral.ozreader.activities.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Created by owniz on 13/04/17.
 */

interface MainView {
    void setFragment(@NonNull Fragment fragment, String tag);
    void showShareDialog();
    void openMail();
    void showFileChooserDialog();
    void setNavViewColor();
    //void openHelpDev();
}
