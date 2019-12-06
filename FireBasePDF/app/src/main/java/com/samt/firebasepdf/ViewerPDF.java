package com.samt.firebasepdf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ViewerPDF extends AppCompatActivity {

    ListView myPDFList;
    List<UploadPDF> uploadPDFs;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer_pdf);

        myPDFList=(ListView) findViewById(R.id.listView);

        viewPdfList();
        //for open files
        myPDFList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                UploadPDF uploadPDF = uploadPDFs.get(i);
                Intent intent=new Intent();
                intent.setData(Uri.parse(uploadPDF.getUrl()));
                startActivity(intent);

            }
        });
    }

    private void viewPdfList() {

        databaseReference= FirebaseDatabase.getInstance().getReference("Upload");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Access the data from database and preform operation and used for loop for get files in firebase
                for(DataSnapshot postSnapshot :dataSnapshot.getChildren())
                {
                    UploadPDF uploadPDF= postSnapshot.getValue(UploadPDF.class);
                    uploadPDFs.add(uploadPDF);
                }

                String[] uploads=new String[uploadPDFs.size()];
                for(int i=0;i<uploads.length;i++)
                {
                    uploads[i]=uploadPDFs.get(i).getName();
                }
                ArrayAdapter<String> adapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,uploads)
                {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView mytext=(TextView) view.findViewById(R.id.text);
                        mytext.setTextColor(Color.BLACK);

                        return view;
                    }
                };
                myPDFList.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

