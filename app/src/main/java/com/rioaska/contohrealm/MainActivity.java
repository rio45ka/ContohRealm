package com.rioaska.contohrealm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rioaska.contohrealm.adapter.ListDataAdapter;
import com.rioaska.contohrealm.adapter.RealmDataAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.fab)
    FloatingActionButton mFloatingActionButton;
    @Bind(R.id.rv_main)
    RecyclerView mRecyclerView;

    private Realm mRealm = null;
    private ListDataAdapter mAdapter;
    private List<DataModelDb> mDataList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private RealmResults<DataModelDb> dataModelDbs;
    private RealmDataAdapter realmDataAdapter;

    private String dialogNama;
    private String dialogHape;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRealm = Realm.getDefaultInstance();

        setupToolbar();
        setupRecyclerView();

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tampilkanDialogTambah();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataModelDbs = mRealm.where(DataModelDb.class).findAll();
        realmDataAdapter = new RealmDataAdapter(MainActivity.this, dataModelDbs, true);

        mAdapter.setRealmAdapter(realmDataAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ListDataAdapter(MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.ubahData(new ListDataAdapter.UbahDataInterface() {
            @Override
            public void ubahData(View view, int position) {
                dapatkanData(view, dataModelDbs.get(position));
            }
        });

        mAdapter.hapusData(new ListDataAdapter.HapusDataInterface() {
            @Override
            public void hapusData(View view, int position) {
                menghapusData(position);

                mAdapter.notifyDataSetChanged();

                Toast.makeText(MainActivity.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void dapatkanData(View view, DataModelDb dataModelDb) {
        String id = dataModelDb.getId();
        dialogNama = dataModelDb.getNama();
        dialogHape = dataModelDb.getNo_hp();

        tampilkanDialogUbah(id, dialogNama, dialogHape);
    }

    private void tambahData() {
        mRealm.beginTransaction();

        DataModelDb dataModel = mRealm.createObject(DataModelDb.class);
        dataModel.setId(dapatkanId());
        dataModel.setNama(dialogNama);
        dataModel.setNo_hp(dialogHape);

        mRealm.commitTransaction();
    }

    private void ubahData(String id, String nama, String nope) {
        mRealm.beginTransaction();

        DataModelDb dataModel = mRealm.where(DataModelDb.class).equalTo("id", id).findFirst();
        dataModel.setNama(nama);
        dataModel.setNo_hp(nope);

        mRealm.commitTransaction();
    }

    private void menghapusData(int position) {
        mRealm.beginTransaction();

        dataModelDbs.remove(position);

        mRealm.commitTransaction();
    }

    private String dapatkanId() {
        String dateTime = "";
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTime = df.format(c.getTime());

        return dateTime;
    }

    private void tampilkanDialogTambah() {
        String titleBtn = "Simpan";

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

        View itemView	= inflater.inflate(R.layout.dialog_isi_data, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setView(itemView);

        final EditText nameInput = (EditText) itemView.findViewById(R.id.et_nama);
        final EditText hapeInput = (EditText) itemView.findViewById(R.id.et_noHape);

        builder.setCancelable(false).setPositiveButton(titleBtn, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (nameInput.length() == 0 || hapeInput.length() == 0) {
                    Toast.makeText(MainActivity.this, "Data belum lengkap", Toast.LENGTH_SHORT).show();
                }else {
                    dialogNama = nameInput.getText().toString();
                    dialogHape = hapeInput.getText().toString();

                    tambahData();

                    mAdapter.notifyDataSetChanged();

                    Toast.makeText(MainActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void tampilkanDialogUbah(final String id, String nama, String nope) {
        String titleBtn = "Ubah";

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

        View itemView	= inflater.inflate(R.layout.dialog_isi_data, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setView(itemView);

        final EditText nameInput = (EditText) itemView.findViewById(R.id.et_nama);
        final EditText hapeInput = (EditText) itemView.findViewById(R.id.et_noHape);

        nameInput.setText(nama);
        hapeInput.setText(nope);

        builder.setCancelable(false).setPositiveButton(titleBtn, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (nameInput.length() == 0 || hapeInput.length() == 0) {
                    Toast.makeText(MainActivity.this, "Data belum lengkap", Toast.LENGTH_SHORT).show();
                }else {
                    dialogNama = nameInput.getText().toString();
                    dialogHape = hapeInput.getText().toString();

                    ubahData(id, dialogNama, dialogHape);

                    mAdapter.notifyDataSetChanged();

                    Toast.makeText(MainActivity.this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
