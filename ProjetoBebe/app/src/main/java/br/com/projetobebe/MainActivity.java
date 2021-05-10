package br.com.projetobebe;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import br.com.projetobebe.fragment.HomeFragment;
import br.com.projetobebe.fragment.RegisterEventFragment;
import br.com.projetobebe.fragment.SummaryFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.onFragmentBtnSelected {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        drawerLayout = findViewById( R.id.drawer );
        navigationView = findViewById( R.id.navigationView );
        navigationView.setNavigationItemSelectedListener( this );

        actionBarDrawerToggle = new ActionBarDrawerToggle( this,drawerLayout,toolbar, R.string.open,R.string.close);
        drawerLayout.addDrawerListener( actionBarDrawerToggle );
        actionBarDrawerToggle.setDrawerIndicatorEnabled( true );
        actionBarDrawerToggle.syncState();

        initializeFragment( new HomeFragment("all"));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer( GravityCompat.START );

        if(menuItem.getItemId() == R.id.home)
            initializeFragment( new HomeFragment("all"));

        if(menuItem.getItemId() == R.id.sleep)
            initializeFragment( new HomeFragment("baby slept"));

        if(menuItem.getItemId() == R.id.wake_up)
            initializeFragment( new HomeFragment("baby woke up"));

        if(menuItem.getItemId() == R.id.change)
            initializeFragment( new HomeFragment("changed the baby"));

        if(menuItem.getItemId() == R.id.suck)
            initializeFragment( new HomeFragment("baby suckled"));

        if(menuItem.getItemId() == R.id.summary_days)
            initializeFragment( new SummaryFragment());

        return true;
    }

    @Override
    public void onButtonSelected() {
        initializeFragment(new RegisterEventFragment() );
    }

    public void initializeFragment(Fragment fragment){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.container_fragment, fragment);
        fragmentTransaction.commit();
    }
}
