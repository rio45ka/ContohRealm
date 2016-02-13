package com.rioaska.contohrealm.adapter;

import android.content.Context;

import com.rioaska.contohrealm.DataModelDb;

import io.realm.RealmResults;

/**
 * Created by rio on 13/02/16.
 */
public class RealmDataAdapter extends RealmModelAdapter<DataModelDb> {
    public RealmDataAdapter(Context context, RealmResults<DataModelDb> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }
}
