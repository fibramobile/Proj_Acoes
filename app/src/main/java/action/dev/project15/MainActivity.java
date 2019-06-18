package action.dev.project15;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import action.dev.project15.fragment.busca.BuscaFragment;
import action.dev.project15.fragment.lucro.LucroFragment;
import action.dev.project15.fragment.radar.RadarListaFragment;
import action.dev.project15.model.Finance;
import action.dev.project15.model.Storage;
import action.dev.project15.view.HomeFragment;
import action.dev.project15.view.RegistryFragment;
import action.dev.project15.view.SeachFragment;
import action.dev.project15.view.TradeFragment;
import action.dev.project15.view.UpdateFragment;
import action.dev.project15.view.VaryingFragment;

/**
 * Activity Principal
 * <p>
 * Finalizado: OK
 * Revisado: OK
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public RegistryFragment registryFragment;
    public UpdateFragment updateFragment;
    public HomeFragment homeFragment;
    public TradeFragment tradeFragment;
    public VaryingFragment varyingFragment;

    // Atualizado 08/04/2019
    public SeachFragment seachFragment;

    public Storage storage;
    public Toolbar toolbar;
    public Finance finance;

    private boolean enableBack;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = new Storage();
        storage.load(this);

        finance = new Finance(this);

        registryFragment = new RegistryFragment();
        registryFragment.activity = this;

        updateFragment = new UpdateFragment();
        updateFragment.activity = this;

        varyingFragment = new VaryingFragment();
        varyingFragment.activity = this;

        homeFragment = new HomeFragment();
        homeFragment.activity = this;

        tradeFragment = new TradeFragment();
        tradeFragment.activity = this;

        // Atualizado 08/04/2019
        seachFragment = new SeachFragment();
        seachFragment.activity = this;

        setContentView(R.layout.activity_main);

        // Define a toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Define o menu Drawer
        drawer = findViewById(R.id.root);

        // Combina a Toolbar para acionar o DrawerLayout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.menu_open,
                R.string.menu_close);

        // Adiciona o evento
        drawer.addDrawerListener(toggle);

        // Sincroniza o status
        toggle.syncState();

        NavigationView navigation = findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(this);

        homeFragment.preload();
        registryFragment.preload();
        updateFragment.preload();
        tradeFragment.preload();
        varyingFragment.preload();

        // Atualizado 08/04/2019
        seachFragment.preload();

        setFragment(homeFragment);
        updateQuote();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }


    @Override /* Toolbar */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_registry:
                registryFragment.setRegistryMode();
                setFragment(registryFragment);
                return true;

            case R.id.menu_update_quote:
                updateQuote();
                return true;

            default:
                return false;
        }
    }

    @Override   /* Navegação Drawer */
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Fecha a navegação
        drawer.closeDrawers();

        // Função
        switch (item.getItemId()) {

            // Inicio
            case R.id.menu_home:
                setFragment(homeFragment);
                return true;

            // Lucro
            case R.id.menu_lucro:
                setFragment(LucroFragment.newInstance());
                return true;

            // Relatorio
            case R.id.menu_radar:
                setFragment(RadarListaFragment.newInstance());
                return true;

            // Relatorio
            case R.id.menu_busca:
                setFragment(BuscaFragment.newInstance());
                return true;

            // Busca
            case R.id.menu_search:
                setFragment(seachFragment);
                return true;


            case R.id.menu_backup:
                Intent intent = new Intent(this, BackupActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_update:
                if (storage.isNotEmpty()) {
                    updateFragment.setRegistryMode();
                    setFragment(updateFragment);
                } else {
                    showMessage(getString(R.string.update_val_10));
                }
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onResume() {
        storage.load(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        storage.save(this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (!enableBack) super.onBackPressed();
        else {
            if (registryFragment.isVisible() && registryFragment.actionEntity != null) {
                setFragment(tradeFragment);
                tradeFragment.update(registryFragment.actionEntity);
            } else if (updateFragment.isVisible() && updateFragment.actionEntity != null) {
                setFragment(varyingFragment);
                varyingFragment.update(updateFragment.actionEntity);
            } else {
                setFragment(homeFragment);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    /**
     * Atualiza a cotação
     */
    public void updateQuote() {
        finance.getQuote(() -> homeFragment.update(), storage);
        showMessage("Atualizado");
    }

    /**
     * Ativa a funcão de voltar o fragmento
     */
    public void enableBackFragment() {
        enableBack = true;
    }

    /**
     * Desativa a função de voltar o fragmento
     */
    public void disableBackFragment() {
        enableBack = false;
    }

    /**
     * Define o fragmento atual
     *
     * @param fragment Fragmento
     */
    public void setFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.container, fragment)
                .commit();
    }

    /**
     * Exibe a mesagem
     *
     * @param message Mesagem
     */
    public void showMessage(String message) {
        Snackbar bar;
        (bar = Snackbar.make(this.findViewById(R.id.root), message, Snackbar.LENGTH_LONG))
                .setAction("Fechar", v -> bar.dismiss())
                .show();
    }
}