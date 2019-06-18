package action.dev.project15;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.events.OpenFileCallback;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import action.dev.project15.model.Storage;

/**
 * Controlador backup
 *
 * Finalizado: OK
 * Revisado: OK
 */
public class BackupActivity extends AppCompatActivity {

    private final int REQUEST_CODE_SIGN_IN = 11;

    private DriveResourceClient mDriveResourceClient;
    private Storage storage;

    private final static byte[] BUFFER = new byte[1024];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        storage = new Storage();
        storage.load(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.view_backup);
        checkLogin();
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }

    /**
     * Verifica se está logado
     */
    public void checkLogin() {

        if (mDriveResourceClient == null) {

            signIn();
        }
    }

    /**
     * Botão salvar
     **/
    public void upload(View view) {

        if (mDriveResourceClient == null) {

            signIn();

        } else {

            send(storage.save());
        }
    }

    /**
     * Botão Download
     **/
    public void download(View view) {

        if (storage.isNotEmpty()) {

            Snackbar bar = Snackbar.make(findViewById(R.id.bcontainer),
                    R.string.backup_msg_3, Snackbar.LENGTH_LONG);
            bar.getView().setBackgroundColor(0xFFFF1818);
            bar.setActionTextColor(0xFFFFFFFF);
            bar.setAction("Sim", v -> findFile());
            bar.show();

        } else {

            findFile();
        }
    }

    /**
     * Recebe o conteudo do arquivo
     *
     * @param json String
     */
    public void onReciveJSON(String json) {

        if (json == null) {

            Snackbar bar = Snackbar.make(findViewById(R.id.bcontainer),
                    R.string.backup_msg_4, Snackbar.LENGTH_LONG);
            bar.getView().setBackgroundColor(0xFFFF1818);
            bar.setActionTextColor(0xFFFFFFFF);
            bar.setAction("Fechar", v -> bar.dismiss());
            bar.show();

        } else {

            storage.load(json);
            storage.save(this);

            Snackbar bar = Snackbar.make(findViewById(R.id.bcontainer),
                    R.string.backup_msg_5, Snackbar.LENGTH_LONG);
            bar.setAction("Fechar", v -> bar.dismiss());
            bar.show();
        }
    }

    /**
     * Eventos de volta
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CODE_SIGN_IN:

                if (resultCode != RESULT_OK) {

                    Snackbar bar = Snackbar.make(findViewById(R.id.bcontainer),
                            R.string.backup_msg_6, Snackbar.LENGTH_LONG);
                    bar.setAction("Fechar", v -> bar.dismiss());
                    bar.show();
                    return;
                }

                Task<GoogleSignInAccount> getAccountTask
                        = GoogleSignIn.getSignedInAccountFromIntent(data);

                if (getAccountTask.isSuccessful()) {

                    initializeDriveClient(getAccountTask.getResult());

                } else {

                    Snackbar bar = Snackbar.make(findViewById(R.id.bcontainer),
                            R.string.backup_msg_7, Snackbar.LENGTH_LONG);
                    bar.getView().setBackgroundColor(0xFFFF1818);
                    bar.setActionTextColor(0xFFFFFFFF);
                    bar.setAction("Fechar", v -> bar.dismiss());
                    bar.show();
                }
                break;
        }
    }

    /**
     * Faz o login do cliente no drive
     */
    protected void signIn() {

        // Remoção, Requer o login toda vez que logar
        //Set<Scope> requiredScopes = new HashSet<>(2);
        //requiredScopes.add(Drive.SCOPE_FILE);
        //requiredScopes.add(Drive.SCOPE_APPFOLDER);
        //GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        //if (signInAccount != null && signInAccount.getGrantedScopes().containsAll(requiredScopes)) {
        //    initializeDriveClient(signInAccount);
        // } else {

            GoogleSignInOptions signInOptions =
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestScopes(Drive.SCOPE_FILE)
                            .requestScopes(Drive.SCOPE_APPFOLDER)
                            .build();

            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
            startActivityForResult(googleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
        //}
    }

    /**
     * Inicializa o acesso ao drive
     */
    private void initializeDriveClient(GoogleSignInAccount signInAccount) {

        mDriveResourceClient = Drive.getDriveResourceClient(this, signInAccount);
    }

    /**
     * Busca o arquivo
     **/
    public void findFile() {

        Query query = new Query.Builder()
                .addFilter(Filters.contains(SearchableField.TITLE, Constants.nameBackup))
                .build();

        Task<MetadataBuffer> queryTask = mDriveResourceClient.query(query);
        queryTask.addOnSuccessListener(this, metadataBuffer -> {

            if (metadataBuffer.getCount() >= 1) {

                recive(metadataBuffer.get(metadataBuffer.getCount() - 1));

            } else {

                Snackbar bar = Snackbar.make(findViewById(R.id.bcontainer),
                        R.string.backup_msg_8, Snackbar.LENGTH_LONG);
                bar.getView().setBackgroundColor(0xFFFF1818);
                bar.setActionTextColor(0xFFFFFFFF);
                bar.setAction("Fechar", v -> bar.dismiss());
                bar.show();
            }

        }).addOnFailureListener(this, e -> {

            Snackbar bar = Snackbar.make(findViewById(R.id.bcontainer),
                    R.string.backup_msg_9, Snackbar.LENGTH_LONG);
            bar.getView().setBackgroundColor(0xFFFF1818);
            bar.setActionTextColor(0xFFFFFFFF);
            bar.setAction("Fechar", v -> bar.dismiss());
            bar.show();
        });
    }


    /**
     * Recebe o conteudo do arquivo e envia para onReciveJSON
     *
     * @param metadata Metadata
     */
    public void recive(final Metadata metadata) {

        OpenFileCallback openFileCallback = new OpenFileCallback() {
            @Override
            public void onProgress(long l, long l1) {
            }

            @Override
            public void onContents(@NonNull DriveContents driveContents) {
                try {

                    InputStream inputStream = driveContents.getInputStream();
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                    int read;
                    while ((read = inputStream.read(BUFFER, 0, BUFFER.length)) != -1) {
                        buffer.write(BUFFER, 0, read);
                    }
                    inputStream.close();

                    byte[] data = buffer.toByteArray();

                    onReciveJSON(new String(data, "UTF-8"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Exception e) {

                Snackbar bar = Snackbar.make(findViewById(R.id.bcontainer),
                        R.string.backup_msg_9, Snackbar.LENGTH_LONG);
                bar.getView().setBackgroundColor(0xFFFF1818);
                bar.setActionTextColor(0xFFFFFFFF);
                bar.setAction("Fechar", v -> bar.dismiss());
                bar.show();
            }
        };

        mDriveResourceClient.openFile(metadata.getDriveId().asDriveFile(), DriveFile.MODE_READ_ONLY, openFileCallback);
    }

    /**
     * Envia o json para o arquivo de backup
     *
     * @param json String
     */
    public void send(final String json) {

        final Task<DriveFolder> appFolderTask = mDriveResourceClient.getAppFolder();
        final Task<DriveContents> createContentsTask = mDriveResourceClient.createContents();

        Tasks.whenAll(appFolderTask, createContentsTask).continueWithTask(task -> {

            DriveFolder parent = appFolderTask.getResult();

            DriveContents contents = createContentsTask.getResult();
            OutputStream outputStream = null;
            if (contents != null) {
                outputStream = contents.getOutputStream();
            }
            if (outputStream != null) {
                outputStream.write(json.getBytes("UTF-8"));
            }

            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle(Constants.nameBackup)
                    .setMimeType("application/json")
                    .build();

            return mDriveResourceClient.createFile(Objects.requireNonNull(parent), changeSet, contents);

        }).addOnSuccessListener(this, driveFile -> {

            Snackbar bar = Snackbar.make(findViewById(R.id.bcontainer),
                    R.string.backup_msg_10, Snackbar.LENGTH_LONG);
            bar.setAction("Fechar", v -> bar.dismiss());
            bar.show();

        }).addOnFailureListener(this, e -> {

            Snackbar bar = Snackbar.make(findViewById(R.id.bcontainer),
                    R.string.backup_msg_11, Snackbar.LENGTH_LONG);
            bar.getView().setBackgroundColor(0xFFFF1818);
            bar.setActionTextColor(0xFFFFFFFF);
            bar.setAction("Fechar", v -> bar.dismiss());
            bar.show();
        });
    }
}
