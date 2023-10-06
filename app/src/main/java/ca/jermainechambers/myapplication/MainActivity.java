package ca.jermainechambers.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNoteBtn = findViewById(R.id.add_note_btn);
        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);

        addNoteBtn.setOnClickListener((v) -> startActivity(new Intent(MainActivity.this, NoteDetailsActivity.class)));
        menuBtn.setOnClickListener((v) -> showMenu());

        // Initialize the RecyclerView and adapter
        setupRecyclerView();
    }

    void showMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.getMenu().add("Contact");
        popupMenu.getMenu().add("Share");
        popupMenu.getMenu().add("About Us");
        popupMenu.getMenu().add("Phone");
        popupMenu.getMenu().add("Email");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle().equals("Logout")) {
                    // Handle logout
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    return true;
                } else if (menuItem.getTitle().equals("Contact")) {
                    // Handle contact option
                    startActivity(new Intent(MainActivity.this, ContactActivity.class));
                    finish();
                    return true;
                } else if (menuItem.getTitle().equals("Share")) {
                    // Handle share option
                    // Implement the logic to share your app
                    return true;
                } else if (menuItem.getTitle().equals("About Us")) {
                    // Handle about us option
                    startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                    finish();
                    return true;
                } else if (menuItem.getTitle().equals("Phone")) {
                    // Handle phone option
                    // Implement the logic to launch the phone dialer
                    return true;
                } else if (menuItem.getTitle().equals("Email")) {
                    // Handle email option
                    // Implement the logic to open the email client
                    return true;
                }
                return false;
            }
        });
    }

    void setupRecyclerView() {
        // Query Firestore for notes
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();

        // Initialize the RecyclerView and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options, this);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening to Firestore updates
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening to Firestore updates when the activity is stopped
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Add any necessary logic when the activity resumes
    }
}
