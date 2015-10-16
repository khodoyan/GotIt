package pro.khodoian.gotit.models;

import android.content.ContentValues;

/**
 * Interface used to simplify stored object transformation to csv
 *
 * @author eduardkhodoyan
 */
public interface ToContentValues {
    public ContentValues toContentValues();
}
