package com.example.d_fir_login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class ScanActivity extends AppCompatActivity {

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    private  static final int CAMERA_REQUEST_CODE = 200;
    private  static final int STORAGE_REQUEST_CODE = 400;
    private  static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private  static final int IMAGE_PICK_CAMERA_CODE = 1001;
    private static final String ALGO = "AES";
    private Cipher cipher;
    private SecretKey secretKey;
    private byte[] encryptedData;
    private Calendar calendar;
    Button save;
    String cameraPermission[];
    String storagePermission[];
    EditText mResultEt;
    ImageView mPreviewIv;
    Uri image_uri;
    String mText, officerName, officerId, caseId, sha256Hash, text, name, nameWithoutExtension;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;

    private Uri input_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = firebaseDatabase.getReference();

        mResultEt = findViewById(R.id.resultEt);
        mPreviewIv = findViewById(R.id.imageIv);

        calendar =  Calendar.getInstance();

        save = findViewById(R.id.msave);
        mResultEt = findViewById(R.id.resultEt);
        mPreviewIv = findViewById(R.id.imageIv);
        cameraPermission = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        /*Intent intent = getIntent();
        str3 = intent.getStringExtra("caseid1");*/
        Intent intent = getIntent();
        Bundle extras3 = intent.getExtras();
        officerName = extras3.getString("officerName");
        officerId = extras3.getString("officerId");
        caseId = extras3.getString("caseId");

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                mText = mResultEt.getText().toString().trim();

                if (mText.isEmpty()){
                    Toast.makeText(ScanActivity.this,"Please scan a image first", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) {
                            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission, WRITE_EXTERNAL_STORAGE_CODE);
                        } else {
                            saveToTxtFile(mText);
                        }
                    }else {
                        saveToTxtFile(mText);
                    }
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveToTxtFile(String mText) {
        SimpleDateFormat dateFormat;

        dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        String date = dateFormat.format(calendar.getTime());
        try {
            name = caseId + "_(" + officerName + " - " + officerId + ")" + "_(" + date + ").txt";
            nameWithoutExtension = caseId + "_(" + officerName + " - " + officerId + ")" + "_(" + date + ")";

            /*File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/My_Files/");
            if(!dir.exists()){
                dir.mkdir();
            }
            File file = new File(dir, name);

            FileWriter fw = new FileWriter(file.getAbsoluteFile());

            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(mText);
            bw.close();

            input_uri = Uri.fromFile(file);*/

            HashGenerator();
            encryptFile();

            UploadFile();

            //Toast.makeText(this,name+ "is saved to\n" + dir, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void HashGenerator() throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("sha-256");
        InputStream inputStream = getContentResolver().openInputStream(input_uri);


        byte[] databytes = new byte[1024];
        int nread;
        if (inputStream != null) {
            while ((nread = inputStream.read(databytes)) != -1)
                md.update(databytes, 0, nread);
        }

        byte[] messagedigest = md.digest();
        StringBuilder hexString = new StringBuilder();

        for (byte b : messagedigest) {
            String h = Integer.toHexString(0xFF & b);
            String StrComplete = PrependValue(h);
            hexString.append(StrComplete);
        }
        sha256Hash = hexString.toString();
    }

    private String PrependValue(String iStr) {
        StringBuilder sReturnedStr = new StringBuilder(iStr);
        while (sReturnedStr.length() < 2)
            sReturnedStr.insert(0, "0");
        return sReturnedStr.toString();
    }

    private void encryptFile() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGO);
        keyGenerator.init(256);

        secretKey = keyGenerator.generateKey();

        byte[] iv = new byte[256/8];
        SecureRandom secureRandom = new SecureRandom();

        secureRandom.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        //outputFile = new File(Environment.getExternalStorageState(), caseFileName);

        /*InputStream iStream = getContentResolver().openInputStream(input_uri);
        byte[] inputData = getBytes(iStream);*/

        byte[] inputData = text.getBytes();

        encryptedData = cipher.doFinal(inputData);
        /*FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(encryptedData);
        fos.close();*/

        //output_uri = Uri.fromFile(outputFile);
    }

    private void UploadFile() {

        storageReference.child("Cases").child(caseId).child(name).putBytes(encryptedData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ScanActivity.this, "Upload Successful", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ScanActivity.this, "Uploading Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        //name = name.substring(0, name.indexOf("."));
        //caseFileName = caseFileName.substring(0, caseFileName.indexOf("."));

        String str = secretKey.toString();
        String encoded = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);

        databaseReference.child("Hash Values").child(caseId).child(nameWithoutExtension).setValue(sha256Hash);
        databaseReference.child("Keys").child(caseId).child(nameWithoutExtension).setValue(encoded);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addImage) {
            showImageImportDialog();
        }
        if (id == R.id.setting) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showImageImportDialog() {
        String[] items = {"Camera", "Gallery"};
        AlertDialog.Builder dailog = new AlertDialog.Builder(this);
        dailog.setTitle("Select Image");
        dailog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickCamera();
                    }

                }
                if (which == 1) {
                    if (!checkStoragePermission()) {
                        requstStoragePermission();
                    } else {
                        pickGallery();
                    }
                }
            }

            private void pickCamera() {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "NewPic");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Image To text");
                image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                startActivityForResult(cameraintent, IMAGE_PICK_CAMERA_CODE);
            }


        });
        dailog.create().show();
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void requstStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        pickGallery();
                    } else {
                        Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        pickGallery();
                    } else {
                        Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                    }
                }
            case WRITE_EXTERNAL_STORAGE_CODE:{
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    saveToTxtFile(mText);
                }else {
                    Toast.makeText(this,"Storage Permission is required to store data", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();


                mPreviewIv.setImageURI(resultUri);

                BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> item = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < item.size(); i++) {
                        TextBlock myItem = item.valueAt(i);
                        sb.append(myItem.getValue());
                        sb.append("\n");
                    }
                    mResultEt.setText(sb.toString());
                    text = sb.toString();

                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }

    }

}