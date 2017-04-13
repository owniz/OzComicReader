package es.jmoral.simplecomicreader.activities.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Created by owniz on 13/04/17.
 */

interface MainView {
    void setFragment(@NonNull Fragment fragment);
    void showShareDialog();
    void openMail();
}
