package org.gtre.itg.hermes;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseUtility {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}