package pro.khodoian.gotit.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import pro.khodoian.gotit.SQLAsyncTasks.DeletePostByIdAsyncTask;
import pro.khodoian.gotit.SQLAsyncTasks.GetPostsAsyncTask;
import pro.khodoian.gotit.R;
import pro.khodoian.gotit.models.Post;
import pro.khodoian.gotit.presenter.PostListAdapter;
import pro.khodoian.gotit.services.PostService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DeletePostByIdAsyncTask.Callback, GetPostsAsyncTask.Callback {

    public static final String TAG = MainActivity.class.getCanonicalName();

    FloatingActionButton fab;
    RelativeLayout contentLayout;
    PostListAdapter postsAdapter;
    ListView postsListView;

    PostService postService;
    private ServiceConnection postServiceConnection = new ServiceConnection() {
        // Called when the connection with the service is established
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Because we have bound to an explicit
            // service that is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            PostService.LocalBinder binder = (PostService.LocalBinder) service;
            postService = binder.getService();
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            postService = null;
        }
    };

    public static Intent makeIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contentLayout = (RelativeLayout) findViewById(R.id.contentMain);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(CheckinActivity.makeIntent(MainActivity.this),
                            CheckinActivity.REQUEST_CODE);
                    fab.hide();
                }
            });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.title_activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        postsListView = (ListView) findViewById(R.id.postListView);
        postsAdapter = new PostListAdapter(this, new ArrayList<Post>());
        postsListView.setAdapter(postsAdapter);

        // connect to PostService
        bindService(PostService.makeIntent(this), postServiceConnection, BIND_AUTO_CREATE);

        // TODO: set full name and username to nav bar
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!fab.isShown())
            fab.show();
        Long limit = null;
        new GetPostsAsyncTask(this, this).execute(limit);
    }

    @Override
    protected void onDestroy() {
        if (postServiceConnection != null)
            unbindService(postServiceConnection);
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
        // Menu is not inflated as it is replaced with the drawer
        // getMenuInflater().inflate(R.menu.main, menu);
        return false;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        long postId = -1;


        // Check in
        if (requestCode == CheckinActivity.REQUEST_CODE) {

            if (resultCode == CheckinActivity.SUCCESSFUL) {
                // successfully added post to local SQLite
                if (fab != null)
                    //fab.hide();
                if (contentLayout != null)
                    if (data != null && data.getLongExtra(CheckinActivity.KEY_POST_ID, -1) != -1) {
                        Snackbar.make(contentLayout, R.string.post_added, Snackbar.LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // undo
                                new DeletePostByIdAsyncTask(MainActivity.this, MainActivity.this)
                                        .execute(data.getLongExtra(CheckinActivity.KEY_POST_ID, -1));
                            }
                        }).setCallback(new Snackbar.Callback() {
                            @Override
                            public void onShown(Snackbar snackbar) {
                                super.onShown(snackbar);
                                if (fab.isShown())
                                    fab.hide();
                            }

                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                if (!fab.isShown())
                                    fab.show();
                                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                    // UNDO not pressed, so post to server
                                    // send post to server
                                    // call login activity if
                                    if (postService != null)
                                        postService.addPost(
                                                data.getLongExtra(CheckinActivity.KEY_POST_ID, -1),
                                                new PostService.AddPostListener() {
                                                    @Override
                                                    public void onSuccess() {
                                                        Toast.makeText(MainActivity.this, "Post added: " + data.getLongExtra(CheckinActivity.KEY_POST_ID, -1), Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onUnauthorized() {
                                                        startActivity(LoginActivity.makeIntent(MainActivity.this));
                                                    }

                                                    @Override
                                                    public void onFailure() {
                                                        Toast.makeText(MainActivity.this, "Error sending post. Sorry", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                }
                            }
                        }).show();
                    } else {
                        if (data == null)
                            Log.e("Snackbar not shown", "data == null");
                        else if (data.getLongExtra(CheckinActivity.KEY_POST_ID, -1) != -1)
                            Log.e("Snackbar not shown", "data.getLongExtra(CheckinActivity.KEY_POST_ID, -1) != -1");
                    }
            } else {
                // unable to add post
                // Toast.makeText(this, R.string.cant_add_post, Toast.LENGTH_LONG).show();
            }
        }
    }

    // Undo adding post - success
    @Override
    public void onPostDeletedByIdSuccess() {
        // Item deleted: update list of posts (without server request)
        Long limit = null;
        new GetPostsAsyncTask(MainActivity.this, MainActivity.this).execute(limit);
    }

    // Undo adding post - failed
    @Override
    public void onPostDeletedByIdFailed() {
        Toast.makeText(MainActivity.this,
                R.string.cant_undo, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGotPostsSuccess(ArrayList<Post> posts) {
        Log.v(MainActivity.class.getName(), "onGotPostsSuccess() started");
        postsListView.setAdapter(null);
        postsAdapter = new PostListAdapter(MainActivity.this, posts);
        postsListView.setAdapter(postsAdapter);
    }

    @Override
    public void onGotPostFailed() {
        Log.v(MainActivity.class.getName(), "onGotPostsFailed() started");
        Snackbar.make(findViewById(R.id.contentMain),
                R.string.cant_update_posts_list, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long limit = null;
                new GetPostsAsyncTask(MainActivity.this, MainActivity.this).execute(limit);
            }
        }).setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                fab.show();
            }

            @Override
            public void onShown(Snackbar snackbar) {
                super.onShown(snackbar);
                fab.hide();
            }
        }).show();
    }


}
