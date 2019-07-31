package com.uzflsoft.vphapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.LinkedList;
import java.util.List;


public class SaveActivity extends AppCompatActivity {

    ListView listview;
    List<String> data;
    ArrayAdapter<String> adapter;
    Database db;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        context = this;

        db = new Database(this);

        if(!db.isTableEmpty()) {
            data = getData();
            listview = (ListView) findViewById(R.id.listviewSave);
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Подтверждение")
                            .setMessage("Удалить запись?")
                            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.deleteAllData();
                                    data.remove(position);
                                    SQLiteDatabase database = db.getWritableDatabase();
                                    ContentValues cv = new ContentValues();
                                    for (String row : data) {
                                        String s[] = row.split("  ");
                                        cv.put(Database.column[0], s[0]);
                                        cv.put(Database.column[1], s[1]);
                                        cv.put(Database.column[2], s[4]);
                                        cv.put(Database.column[3], s[2]);
                                        cv.put(Database.column[3], s[3]);
                                        database.insert(Database.TABLE_NAME, null, cv);
                                    }
                                    adapter.notifyDataSetChanged();
                                    dialog.dismiss();

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });
        }

    }

    public List<String> getData() {
        List<String> list = new LinkedList<>();


        Cursor curs =  db.getData();
        curs.moveToFirst();

        while(!curs.isAfterLast()) {
            String date, name, points, type, status;
            date = curs.getString(curs.getColumnIndex(Database.column[0]));
            name = curs.getString(curs.getColumnIndex(Database.column[1]));
            points = curs.getString(curs.getColumnIndex(Database.column[2]));
            type = curs.getString(curs.getColumnIndex(Database.column[3]));
            status = curs.getString(curs.getColumnIndex(Database.column[4]));

            list.add(date + "  " + name + "  " + type + "  " + status + "  " + points);

            curs.moveToNext();
        }

        curs.close();


        return list;
    }



}
