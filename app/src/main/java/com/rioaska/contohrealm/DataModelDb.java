package com.rioaska.contohrealm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rio on 12/02/16.
 */
public class DataModelDb extends RealmObject {

    private String nama;
    private String no_hp;

    @PrimaryKey
    private String id;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
