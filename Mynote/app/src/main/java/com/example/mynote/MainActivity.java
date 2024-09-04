package com.example.mynote;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private  static final String PREFS_NAME="NotePrefs";
    private  static final String KEY_NOTE_COUNT ="NoteCount";

    private LinearLayout notescontainer;
    private List<Note> notelist;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        notescontainer= findViewById(R.id.notes_container);
        Button savebutton= findViewById(R.id.note_id);

        notelist= new ArrayList<>();

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();

            }
    });

        loadNotesFromPreferences();
        displayNotes();


    }

    private void displayNotes() {

        for(Note note:notelist)
        {
            createNoteView(note);
        }
    }

    private void loadNotesFromPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        int notecount = sharedPreferences.getInt(KEY_NOTE_COUNT,0);

        for(int i=0;i<notecount;i++)
        {
            String title= sharedPreferences.getString("note_title_"+i,"");
            String content= sharedPreferences.getString("note_content_"+i,"");

            Note note =new Note();
            note.setTitle(title);
            note.setContent(content);

            notelist.add(note);
        }

    }

    private void saveNote() {
        EditText title_edittext= findViewById(R.id.title_note);
        EditText content_edittext= findViewById(R.id.content_note);

        String title = title_edittext.getText().toString();
        String content= content_edittext.getText().toString();

        if(!title.isEmpty() && !content.isEmpty())
        {
            Note note= new Note();
            note.setTitle(title);
            note.setContent(content);

            notelist.add(note);
            saveNotesToReference();
            createNoteView(note);
            clearInputFields();
            
        }

    }

    private void clearInputFields() {
        EditText titleEdittext= findViewById(R.id.title_note);
        EditText contentEdittext= findViewById(R.id.content_note);

        titleEdittext.getText().clear();
        contentEdittext.getText().clear();

    }

    private void createNoteView(final Note note) {
        View noteview= getLayoutInflater().inflate(R.layout.note_item,null);
        TextView titletextview= noteview.findViewById(R.id.title_textview);
        TextView contenttextview= noteview.findViewById(R.id.content_textview);

        titletextview.setText(note.getTitle());
        contenttextview.setText(note.getContent());

        noteview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog(note);
                return true;
            }
        });

        notescontainer.addView(noteview);


    }

    private void showDeleteDialog(final Note note) {

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Delete this note");
        builder.setMessage("Are you sure You want to delete this Note?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteNoteAndRefresh(note);

            }
        });

        builder.setNegativeButton("cancel",null);
        builder.show();

    }

    private void deleteNoteAndRefresh(Note note) {
        notelist.remove(note);
        saveNotesToReference();
        refreshNoteView();
    }

    private void refreshNoteView() {
        notescontainer.removeAllViews();
        displayNotes();

    }

    private void saveNotesToReference() {
        SharedPreferences sharedPreferences=getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putInt(KEY_NOTE_COUNT,notelist.size());
        for(int i=0;i<notelist.size();i++)
        {
            Note note=notelist.get(i);
            editor.putString("note_title_"+i, note.getTitle());
            editor.putString("note_content_"+i,note.getContent());
        }

    editor.apply();
    }
}
