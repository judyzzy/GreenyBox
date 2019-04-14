package com.example.greenybox;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    public ArrayList<Item> mData;
    static MainActivity mActivity;
    private static final String TAG = "MainActivity";
    private int sortID = 0;
    public int modify = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        mContext = MainActivity.this;
        GridView grid_photo = (GridView) findViewById(R.id.grid_photo);

        mData = new ArrayList<Item>();
        restore();

        final BaseAdapter mAdapter = new MyAdapter<Item>(mData, R.layout.item_grid) {
            @Override
            public void bindView(ViewHolder holder, Item obj) {

                holder.setText(R.id.txt_icon, obj.getName());
            }
        };

        grid_photo.setAdapter(mAdapter);


        //add new click listener to grid photo
        grid_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Set img maybe jump some where
                // setImageResource(R.id.img_icon, obj.getiId());
                // TODO: THIS IS WHERE MODIFY SHOULD HAPPEND
                //Toast.makeText(mContext, "Clicked" + position + ":", Toast.LENGTH_SHORT).show();
                //mData.remove(position);
                //Intent intent = getIntent();
                //finish();
                //startActivity(intent);
                modify = position;
                System.out.println("inside main: set position to " + String.valueOf(modify));
                goToModify(view);
            }
        });
    }

    /**
     * Dashboard instance.
     * @return
     */
    public static MainActivity getInstance(){
        return mActivity;
    }

    /**
     * Called when the user taps the Setting button
     *
     * @param view
     * @author Judy
     */
    public void goToSetting(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Setting.class);
        startActivity(intent);
    }

    /**
     * Called when the user taps the Add button
     *
     * @param view
     */
    public void goToAdd(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, AddItem.class);
        startActivity(intent);
    }

    /**
     * Called when the user taps the Modify button
     *
     * @param view
     * @author SoJung
     */
    public void goToModify(View view) {
        if (modify == -1){
            System.out.println("item not selected");
        }
        System.out.println("item position selected in main: " + String.valueOf(modify));

        Intent intent = new Intent(this, Modify.class);
        intent.putExtra("position",modify);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    public void save(){
        try {
            File file = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            Log.d(TAG, "save: "+file.getAbsolutePath());
            FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath()+"/items");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(mData);

            objectOutputStream.close();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restore(){
        try {
            File file = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath()+"/items");

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            mData = (ArrayList<Item>) objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * TODO: this is for now only for names, further implement in item class or add more sort.
     * api 24 min
     */
    @TargetApi(24)
    public void sort(View view) {
        Button sortButton = (Button)findViewById(R.id.sortBtn);
        //mData.sort((o1,o2) -> o1.getName().compareTo(o2.getName()));
        Collections.sort(mData, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                if (sortID == 0) {
                    sortButton.setText("Sort: Name");
                    return o1.getName().compareTo(o2.getName());
                } else if (sortID == 1) {
                    sortButton.setText("Sort: Buy Date");
                    return o1.getBuyDate().isAfter(o2.getBuyDate()) ? 1 : -1;
                } else if (sortID == 2) {
                    sortButton.setText("Sort: Exp Date");
                    return o1.getExpDate().isAfter(o2.getExpDate()) ? 1 : -1;
                } else {
                    sortButton.setText("Sort: Freshness");
                    return o1.Freshness() >= o2.Freshness() ? 1 : -1;
                }
            }
        });
        sortID += 1;
        if (sortID == 4) sortID = 0;
        GridView grid_photo = (GridView) findViewById(R.id.grid_photo);
        final BaseAdapter mAdapter = new MyAdapter<Item>(mData, R.layout.item_grid) {
            @Override
            public void bindView(ViewHolder holder, Item obj) {

                holder.setText(R.id.txt_icon, obj.getName());
            }
        };

        grid_photo.setAdapter(mAdapter);
    }

    /**
     *
     *
     */
    public void sortBuyDate(View view) {
        Collections.sort(mData, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getBuyDate().isAfter(o2.getBuyDate()) ? 1 : -1;
            }
        });
        GridView grid_photo = (GridView) findViewById(R.id.grid_photo);
        final BaseAdapter mAdapter = new MyAdapter<Item>(mData, R.layout.item_grid) {
            @Override
            public void bindView(ViewHolder holder, Item obj) {
                holder.setText(R.id.txt_icon, obj.getName());
            }
        };

        grid_photo.setAdapter(mAdapter);
    }

    /**
     *
     *
     */
    public void sortExpDate(View view) {
        Collections.sort(mData, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                if (o1.getExpDate().isAfter(o2.getExpDate())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        GridView grid_photo = (GridView) findViewById(R.id.grid_photo);
        final BaseAdapter mAdapter = new MyAdapter<Item>(mData, R.layout.item_grid) {
            @Override
            public void bindView(ViewHolder holder, Item obj) {
                holder.setText(R.id.txt_icon, obj.getName());
            }
        };

        grid_photo.setAdapter(mAdapter);
    }

    /**
     *
     *
     */
    public void sortFreshness(View view) {
        Collections.sort(mData, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                if (o1.Freshness() >= o2.Freshness()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        GridView grid_photo = (GridView) findViewById(R.id.grid_photo);
        final BaseAdapter mAdapter = new MyAdapter<Item>(mData, R.layout.item_grid) {
            @Override
            public void bindView(ViewHolder holder, Item obj) {
                holder.setText(R.id.txt_icon, obj.getName());
            }
        };

        grid_photo.setAdapter(mAdapter);
    }
}
