package pro.khodoian.gotit.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import pro.khodoian.gotit.R;
import pro.khodoian.gotit.client.UserService;
import pro.khodoian.gotit.models.User;
import pro.khodoian.gotit.presenter.PeopleListAdapter;
import pro.khodoian.gotit.services.FollowersService;
import pro.khodoian.gotit.sql.SqlOperations;
import pro.khodoian.gotit.sql.UserContract;
import pro.khodoian.gotit.sql.UserSqlOperations;

public class PeopleActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = PeopleActivity.class.getName();
    public static Intent makeIntent(Context context) {
        return new Intent(context, PeopleActivity.class);
    }

    private ListView listView;
    private PeopleListAdapter listAdapter;

    private FollowersService followersService;
    private ServiceConnection followerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            FollowersService.LocalBinder binder = (FollowersService.LocalBinder) iBinder;
            followersService = binder.getService();
            if (followersService != null) {
                updateListView();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            followersService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // TODO: for debug purposes. Delete when finished
        final ArrayList<User> sampleUsersList = User.getSampleUsersList(PeopleActivity.this);

        ArrayList<User> userList = new ArrayList<>();
        Cursor cursor = new UserSqlOperations(this).queryAll(null, UserContract.Columns.USERNAME, SqlOperations.SortOrder.ASC);
        while(cursor.moveToNext()) {
            Log.v(TAG, "Next cursor position");
            User user = User.makeUser(cursor);
            userList.add(user);
            if (user != null)
                Log.v(TAG, String.format("User created: %s", user.toString()));
        }

        listView = (ListView) findViewById(R.id.people_list_view);
        listAdapter = new PeopleListAdapter(this, userList);
        listAdapter.notifyDataSetChanged();
        if (listView != null)
            listView.setAdapter(listAdapter);

        // set FAB
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_people);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: implement adding new user
                Toast.makeText(PeopleActivity.this,
                        "Sorry, this was my next thing to do. It is actually done on server side",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // connect to FollowersService
        bindService(FollowersService.makeIntent(this), followerServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if (followerServiceConnection != null) {
            unbindService(followerServiceConnection);
        }
        super.onDestroy();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.people, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // pass item to perform action to proxy
        NavigationMenuProxy menuProxy = new NavigationMenuProxy(this);
        menuProxy.onNavigationItemSelected(item);

        // hide drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        // consume event
        return true;
    }

    public void updateListView() {
        if (followersService != null) {
            followersService.updateFollowers(new UserService.GetFollowersListener() {
                @Override
                public void onSuccess(ArrayList<User> users) {
                    Log.v(TAG, "onSuccess() is called");
                    ArrayList<User> userList = new ArrayList<>();
                    Cursor cursor = new UserSqlOperations(PeopleActivity.this)
                            .queryAll(
                                    null,
                                    UserContract.Columns.USERNAME,
                                    SqlOperations.SortOrder.ASC
                            );
                    while(cursor.moveToNext()) {
                        Log.v(TAG, "Next cursor position");
                        User user = User.makeUser(cursor);
                        userList.add(user);
                        if (user != null)
                            Log.v(TAG, String.format("User created: %s", user.toString()));
                    }

                    PeopleListAdapter peopleAdapter = (PeopleListAdapter) listView.getAdapter();
                    peopleAdapter.clearList();
                    peopleAdapter.setUsers(userList);
                }

                @Override
                public void onUnauthorized() {
                    Log.v(TAG, "onUnauthorized() is called");
                }

                @Override
                public void onFailure() {
                    Log.v(TAG, "onFailure() is called");
                }
            });
        }
    }


}
