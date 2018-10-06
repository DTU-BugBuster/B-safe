package com.womensafety.shajt3ch;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.codemybrainsout.ratingdialog.RatingDialog;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Boolean isActive = false;

    private Button btStartService;
    private TextView tvText;
    MediaPlayer mediaPlayer;


    private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mediaPlayer = MediaPlayer.create(this, R.raw.girlshout);

//         final ImageButton     panicButton = findViewById(R.id.panicButton);
//
//        panicButton.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                playsound();
//
//                return true;
//            }
//        });
//
//        panicButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String phoneNo = "8851735067";
//                String message = "https://www.google.com/maps/@28.7298838,76.7325634,11z";
//                smsSendMessage(panicButton);
//
//            }
//        });



//        btStartService = (Button) findViewById(R.id.btStartService);
//        tvText = (TextView) findViewById(R.id.tvText);
        enableAutoStart();


        if (checkServiceRunning()) {
            btStartService.setText(getString(R.string.stop_service));
            tvText.setVisibility(View.VISIBLE);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_inst);


/*
        //wi;; delete
        SQLiteDatabase db = openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM details", null);

        while (c.moveToFirst())
        {
            Log.d("Number", c.getString(1));
        }*/






    }


    private void playsound(){

        mediaPlayer.start();
    }

    public void smsSendMessage(View view) {

        String smsNumber = String.format("smsto: %s",
                "8860134724");


//        String smsNumber = String.format("smsto: %s",
//                );



        String sms = "I am in trouble . Please help me out! My current location is :- \n\n https://www.google.com/maps/@28.7298838,76.7325634,11z";
        // Create the intent.
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        // Set the data for the intent as the phone number.
        smsIntent.setData(Uri.parse(smsNumber));
        // Add the message (sms) with the key ("sms_body").
        smsIntent.putExtra("sms_body", sms);
        // If package resolves (target app installed), send intent.
        if (smsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(smsIntent);
        } else {
//           Log.d(TAG, "Can't resolve app for ACTION_SENDTO Intent");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_power:
                android.support.v7.app.ActionBar bar = getSupportActionBar();
                //item.setTitle("changed");

                Log.d("Title:", item.getTitle().toString());

                if (item.getTitle().toString().contains("START")) {
                    Log.d("Title2:", "If er vitore");

                    SQLiteDatabase db2 = this.openOrCreateDatabase("NumberDB", MODE_PRIVATE, null);



                    Log.d("Number is:", Register.getNumber(db2));

                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#78C257")));
                    item.setTitle("STOP SERVICE");

                    return true;
                }

                else
                {
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E81123")));
                    item.setTitle("START SERVICE");
                    return true;
                }

/*
*/
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }    }

    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean checkServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                    Integer.MAX_VALUE)) {
                if (getString(R.string.my_service_name).equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void enableAutoStart() {
        for (Intent intent : Constants.AUTO_START_INTENTS) {
            if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                new MaterialDialog.Builder(this).title(R.string.enable_autostart)
                        .content(R.string.ask_permission)
                        .theme(Theme.LIGHT)
                        .positiveText(getString(R.string.allow))
                        .onPositive((dialog, which) -> {
                            try {
                                for (Intent intent1 : Constants.AUTO_START_INTENTS)
                                    if (getPackageManager().resolveActivity(intent1, PackageManager.MATCH_DEFAULT_ONLY)
                                            != null) {
                                        startActivity(intent1);
                                        break;
                                    }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        })
                        .show();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.newbutton, menu);

        // Locate MenuItem with ShareActionProvider
       // MenuItem item = menu.findItem(R.id.menu_share);

        // Fetch and store ShareActionProvider
        //mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        //setIntent("Testing Share Feature");
        // Return true to display menu

        return true;
    }

    // Call to update the share intent
    private void setIntent(String s) {
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_TEXT,s);
        mShareActionProvider.setShareIntent(intent);

    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/
   /* @Override
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
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());

        // Handle navigation view item clicks here.
        /*int id = item.getItemId();
        if (id == R.id.nav_camera) {

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } *//*else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }
    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
           case R.id.nav_inst: {
               LayoutInflater inflater = getLayoutInflater();
               View alertLayout = inflater.inflate(R.layout.popup_layout, null);
               AlertDialog alertDialog = new AlertDialog.Builder(
                       MainActivity.this).create();

               // Setting Dialog Title
               alertDialog.setTitle("Instructions");
               // Setting Dialog Message
               alertDialog.setView(alertLayout);

               // Setting Icon to Dialog
               alertDialog.setIcon(R.drawable.instruct_icon);



               // Showing Alert Message
               alertDialog.show();
           }
           break;

            case R.id.nav_verify:
                fragment = new Verify();
                break;
            case R.id.nav_register:
                fragment = new Register();
                break;
            case R.id.nav_display:
                fragment = new Display();
                break;
            case R.id.nav_nearby:
                Intent i = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(i);
                break;
            case R.id.nav_issafe:
                Intent in = new Intent(MainActivity.this, IsSafe.class);
                startActivity(in);
                 break;


            case R.id.nav_safety:
                Intent intent = new Intent(MainActivity.this,Safety.class);
                startActivity(intent);
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }





}
