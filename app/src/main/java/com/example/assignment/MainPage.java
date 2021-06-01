package com.example.assignment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;

public class MainPage extends AppCompatActivity {
    ImageView imageView;
    Button browse,camera,showAll;
    EditText sName;
    Uri filepath;
    Bitmap bitmap;
    StorageReference reference = FirebaseStorage.getInstance().getReference();
    DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        imageView=findViewById(R.id.image);
        browse=findViewById(R.id.browse);
        camera=findViewById(R.id.camera);
        sName=findViewById(R.id.name);
        showAll=findViewById(R.id.show_all);
        showAll.setOnClickListener(v -> startActivity(new Intent(MainPage.this,ShowActivity.class)));
       
        browse.setOnClickListener(v -> Dexter.withActivity(MainPage.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        Intent intent=new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        launchSomeActivity.launch(Intent.createChooser(intent,"Please select Image"));
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check());

         camera.setOnClickListener(v ->
         {
        if (filepath != null){
            upLoadToFirebase(filepath);
        }else{
            Toast.makeText(MainPage.this, "Please Select Image", Toast.LENGTH_SHORT).show();
        }
    });

        findViewById(R.id.home).setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        });
    }
    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if(result.getResultCode() == Activity.RESULT_OK )
                {
                    Intent data=result.getData();
                    assert data != null;
                    filepath=data.getData();
                    try
                    {
                        InputStream inputStream=getContentResolver().openInputStream(filepath);
                        bitmap=BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(bitmap);
                    }catch (Exception ignored)
                    {

                    }
                }
            });

    private void upLoadToFirebase(Uri uri)
    {
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();


        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(filepath)
                .addOnSuccessListener(taskSnapshot -> {


                    String TempImageName = sName.getText().toString().trim();
                    if(TempImageName.equals(" "))
                    {
                        TempImageName = String.valueOf(System.currentTimeMillis());
                    }
                    Model model = new Model(uri.toString(),TempImageName);
                    String modelId = root.push().getKey();
                    assert modelId != null;
                    root.child(modelId).setValue(model);
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();
                })
                .addOnProgressListener(taskSnapshot -> {
                    double percent=(100.00 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    dialog.setMessage("Uploaded :"+(int)percent+" %");
                });
    }
    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }
}