package com.uzflsoft.vphapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.uzflsoft.vphapp.MainActivity.*;



public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView name;
        TextView status;
        TextView points;
        ImageView clean;
        ImageView save;
        int position;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.main_listview_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataModel dataModel = getItem(position);
        final ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.main_listview_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status);
            viewHolder.points = (TextView) convertView.findViewById(R.id.points);
            viewHolder.clean = (ImageView) convertView.findViewById(R.id.cleanButton);
            viewHolder.save = (ImageView) convertView.findViewById(R.id.saveButton);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;

        dataModel.setPosition(position);

        viewHolder.name.setText(dataModel.getName());

        tables[position].math();
        dataModel.setPoints(round(tables[position].getValue(), 2));

        double x = dataModel.getPoints();
        dataModel.setStatus(x == 0 ? "" : Math(x, position));

        viewHolder.status.setText(dataModel.getStatus());


        if(dataModel.getPoints() == 0) {
            viewHolder.points.setText("");
            dataModel.setVisibility(0);
        }
        else {
            viewHolder.points.setText("(" + dataModel.getPoints() + ")");
            dataModel.setVisibility(1);
        }

        if(dataModel.getVisibility() == 1) {
            viewHolder.clean.setVisibility(View.VISIBLE);
            viewHolder.save.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.clean.setVisibility(View.INVISIBLE);
            viewHolder.save.setVisibility(View.INVISIBLE);
        }

        viewHolder.save.setOnClickListener(this);
        viewHolder.save.setTag(dataModel);
        viewHolder.clean.setOnClickListener(this);
        viewHolder.clean.setTag(dataModel);

        notifyDataSetChanged();

        return convertView;
    }

    public void onClick(final View v) {

        if(v.getId() == R.id.cleanButton) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Подтверждение")
                   .setMessage("Вы уверены что хотите очистить таблицу?")
                   .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                       }
                   })
                   .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           DataModel dataModel = (DataModel) v.getTag();
                           dataModel.setStatus("");
                           dataModel.setPoints(0);
                           dataModel.setVisibility(0);
                           int pos = dataModel.getPosition();
                           for (int i = 0; i < tables[pos].getBoxes().size(); i++)
                               tables[pos].setBox(i, false);
                           tables[pos].setValue(0);

                           SaveBoxes(tables[pos].getName(), listToString(tables[pos].getBoxes()));
                           notifyDataSetChanged();
                           dialog.dismiss();
                       }
                   });
            AlertDialog dialog = builder.create();
            dialog.show();


        }
        else {
            final EditText input = new EditText(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Сохранение")
                    .setView(input)
                    .setMessage("Введите имя:")
                    .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DataModel dataModel = (DataModel) v.getTag();
                            String name = input.getText().toString();
                            if(!name.equals("")) {
                                double points = dataModel.getPoints();
                                String date = new SimpleDateFormat("yy/MM/dd HH:mm").format(new Date());
                                int pos = dataModel.getPosition();


                                Database db = new Database(mContext);
                                SQLiteDatabase database = db.getWritableDatabase();
                                ContentValues cv = new ContentValues();
                                cv.put(Database.column[0], date);
                                cv.put(Database.column[1], name);
                                cv.put(Database.column[2], points);
                                cv.put(Database.column[3], tables[pos].getName());
                                cv.put(Database.column[4], dataModel.getStatus());
                                database.insert(Database.TABLE_NAME, null, cv);
                            }

                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }



    }

    public String Math(double x, int pos) {
        String result = "";
        if(pos >= 0 && pos <=2) {
            String ss[] = tables[pos].getDegree().split(",");
            double u1, u2, s1, s2, t1, t2, kt1, kt2, k;
            u1 = Integer.parseInt(ss[0].split(" ")[0]);
            u2 = Integer.parseInt(ss[0].split(" ")[1]);
            s1 = Integer.parseInt(ss[1].split(" ")[0]);
            s2 = Integer.parseInt(ss[1].split(" ")[1]);
            t1 = Integer.parseInt(ss[2].split(" ")[0]);
            t2 = Integer.parseInt(ss[2].split(" ")[1]);
            kt1 = Integer.parseInt(ss[3].split(" ")[0]);
            kt2 = Integer.parseInt(ss[3].split(" ")[1]);
            k = Integer.parseInt(ss[4]);

            if (x >= u1 && x <= u2) {
                result = "Удовлетв.";
            } else if (x >= s1 && x <= s2) {
                result = "Средн. тяж.";
            } else if (x >= t1 && x <= t2) {
                result = "Тяжелое";
            } else if (x >= kt1 && x <= kt2) {
                result = "Крайне тяж.";
            } else if (x > k) {
                result = "Критич.";
            }
        }
        else if(pos >= 3 && pos <= 5) {
            String ss[] = tables[pos].getDegree().split(",");
            double u1, u2, s1, s2, t1, t2, k;
            u1 = Double.valueOf(ss[0].split(" ")[0]);
            u2 = Double.valueOf(ss[0].split(" ")[1]);
            s1 = Double.valueOf(ss[1].split(" ")[0]);
            s2 = Double.valueOf(ss[1].split(" ")[1]);
            t1 = Double.valueOf(ss[2].split(" ")[0]);
            t2 = Double.valueOf(ss[2].split(" ")[1]);
            k = Double.valueOf(ss[3]);

            if (x >= u1 && x <= u2) {
                result = "Легкие";
            } else if (x >= s1 && x <= s2) {
                result = "Средн. тяж.";
            } else if (x >= t1 && x <= t2) {
                result = "Тяжелые";
            } else if (x > k) {
                result = "Крайне тяж.";
            }
        }
        else if(pos == 8) {
            String ss[] = tables[pos].getDegree().split(",");
            double u1, u2, s1, s2, k;
            u1 = Integer.parseInt(ss[0].split(" ")[0]);
            u2 = Integer.parseInt(ss[0].split(" ")[1]);
            s1 = Integer.parseInt(ss[1].split(" ")[0]);
            s2 = Integer.parseInt(ss[1].split(" ")[1]);
            k = Integer.parseInt(ss[2]);

            if (x >= u1 && x <= u2) {
                result = "Мин. риск";
            } else if (x >= s1 && x <= s2) {
                result = "Вероятн. риск.";
            } else if (x > k) {
                result = "Опер. против.";
            }
        }

        return result;

    }


    public static double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    public void SaveBoxes(String key, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String listToString(List<Boolean> list) {
        String s = "";
        for(boolean b: list)
            s += b + " ";
        return s;
    }

}
