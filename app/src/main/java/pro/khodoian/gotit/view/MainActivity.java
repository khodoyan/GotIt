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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pro.khodoian.gotit.preferences.AuthenticationDetailsManager;
import pro.khodoian.gotit.sqlasynctasks.DeleteUnsentPostByIdAsyncTask;
import pro.khodoian.gotit.sqlasynctasks.GetPostsAsyncTask;
import pro.khodoian.gotit.R;
import pro.khodoian.gotit.models.Post;
import pro.khodoian.gotit.presenter.PostListAdapter;
import pro.khodoian.gotit.services.PostService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DeleteUnsentPostByIdAsyncTask.Callback, GetPostsAsyncTask.Callback {

    public static final String TAG = MainActivity.class.getCanonicalName();

    FloatingActionButton fab;
    RelativeLayout contentLayout;
    PostListAdapter postsAdapter;
    ListView postsListView;
    TextView usernameTextView;
    TextView nameTextView;

    PostService postService;
    boolean postServiceConnected = false;
    boolean addingPost = false;

    private ServiceConnection postServiceConnection = new ServiceConnection() {
        // Called when the connection with the service is established
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Because we have bound to an explicit
            // service that is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            PostService.LocalBinder binder = (PostService.LocalBinder) service;
            postService = binder.getService();
            updateFeed();
            postServiceConnected = true;
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


        // TODO: understand, why usernameTextView and nameTextView are null after assignment
        usernameTextView = (TextView)
                findViewById(R.id.nav_view).findViewById(R.id.nav_username_text_view);
        nameTextView = (TextView)
                findViewById(R.id.nav_view).findViewById(R.id.nav_name_text_view);

        postsListView = (ListView) findViewById(R.id.postListView);
        postsAdapter = new PostListAdapter(this, new ArrayList<Post>());
        postsListView.setAdapter(postsAdapter);

        // connect to PostService
        bindService(PostService.makeIntent(this), postServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!fab.isShown())
            fab.show();
        Long limit = null;
        new GetPostsAsyncTask(this, this).execute(limit);

        // set full name and username to nav bar
        AuthenticationDetailsManager authManager = new AuthenticationDetailsManager(this);

        if (usernameTextView != null && authManager != null && authManager.getUsername() != null) {
            usernameTextView.setText(authManager.getUsername());
        }
        if (nameTextView != null && authManager != null ) {
            String firstname = authManager.getFirstname();
            String lastname = authManager.getLastname();
            String fullName = "";
            if (firstname != null)
                fullName += firstname;
            if (lastname != null) {
                if (!fullName.equals(""))
                    fullName += " ";
                fullName += lastname;
            }
            nameTextView.setText(fullName);
        }

        if (!authManager.isPatient())
            fab.hide();

        if (postServiceConnected && postService != null)
            updateFeed();

        // check privileges and show/hide floating action button
        checkPatientPrivilegesAndShowOrHideFAB();

        // TODO: hide checkin item in drawer menu
    }

    @Override
    protected void onDestroy() {
        if (postServiceConnection != null) {
            unbindService(postServiceConnection);
            postServiceConnected = false;
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // logout pressed
        if (id == R.id.action_logout) {
            AuthenticationDetailsManager authManager = new AuthenticationDetailsManager(this);
            authManager.clearAll();
            startActivity(SplashActivity.makeIntent(this));
            finish();
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
        if (!fab.isShown())
            fab.show();
        long postId = -1;

        // Check in
        if (requestCode == CheckinActivity.REQUEST_CODE) {

            if (resultCode == CheckinActivity.SUCCESSFUL) {
                addingPost = true;
                // successfully added post to local SQLite
                if (data != null && data.getLongExtra(CheckinActivity.KEY_POST_ID, -1) != -1) {
                    final long id = data.getLongExtra(CheckinActivity.KEY_POST_ID, -1);
                    if (postService != null) {
                        postService.addPost(id, new PostService.AddPostListener() {
                            @Override
                            public void onSuccess(final Post post) {
                                Snackbar.make(contentLayout, R.string.post_added, Snackbar.LENGTH_LONG)
                                        .setAction(R.string.undo, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                // undo
                                                new DeleteUnsentPostByIdAsyncTask(MainActivity.this, MainActivity.this)
                                                        .execute(data.getLongExtra(CheckinActivity.KEY_POST_ID, -1));
                                                postService.deletePost(post.getServerId());
                                                addingPost = false;
                                                updateFeed();
                                            }
                                        }).setCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onDismissed(Snackbar snackbar, int event) {
                                        if (event != DISMISS_EVENT_ACTION) {
                                            addingPost = false;
                                            updateFeed();
                                        }
                                        super.onDismissed(snackbar, event);
                                    }
                                }).show();
                            }

                            @Override
                            public void onUnauthorized() {
                                addingPost = false;
                            }

                            @Override
                            public void onFailure() {
                                addingPost = false;
                            }
                        });
                    }

                }
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

    private void updateFeed() {
        // update feed
        if (postService != null && !addingPost) {
            try {
                postService.updatePosts(new PostService.UpdateFeedListener() {
                    @Override
                    public void onSuccess() {
                        Long limitAsyncTask = null;
                        new GetPostsAsyncTask(
                                MainActivity.this,
                                new GetPostsAsyncTask.Callback() {
                                    @Override
                                    public void onGotPostsSuccess(ArrayList<Post> posts) {
                                        postsAdapter.setData(posts);
                                    }

                                    @Override
                                    public void onGotPostFailed() {
                                        Toast.makeText(MainActivity.this,
                                                "Can't update feed. Please try again",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                        ).execute(limitAsyncTask);
                    }

                    @Override
                    public void onUnauthorized() {
                        startActivity(LoginActivity.makeIntent(MainActivity.this));
                    }

                    @Override
                    public void onFailure() {
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }

        }
    }

    public void checkPatientPrivilegesAndShowOrHideFAB() {
        AuthenticationDetailsManager authManager = new AuthenticationDetailsManager(this);
        if (!authManager.isPatient())
            fab.hide();
        else
            fab.show();
    }
}
