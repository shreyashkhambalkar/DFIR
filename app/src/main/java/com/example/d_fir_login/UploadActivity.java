package com.example.d_fir_login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class UploadActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 2;
    private static final String ALGO = "AES";
    private Cipher cipher;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private Uri uri;
    private TextView filename;
    private Calendar calendar;
    private SecretKey secretKey;
    private String name, sha256Hash, id, policeName, caseFileName;
    private EditText txtEnterCaseId;
    private byte[] encryptedData;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        Button browse = findViewById(R.id.browse);
        Button upload = findViewById(R.id.upload);
        filename = findViewById(R.id.filename);
        filename.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = extras.getString("EmployeeId");
        policeName = extras.getString("OfficerName");

        storageReference = firebaseStorage.getReference();
        databaseReference = firebaseDatabase.getReference();

        calendar = Calendar.getInstance();

        txtEnterCaseId = findViewById(R.id.txtEnterCaseId);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    showFileChooser();
                else
                    ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 45);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri != null)
                    UploadFile();
                else
                    Toast.makeText(UploadActivity.this, "Select a file", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 45 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            showFileChooser();
        else
            Toast.makeText(UploadActivity.this, "Please provide permission", Toast.LENGTH_LONG).show();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            String path = null;

            if (uri != null) {
                path = uri.getPath();
            }
            if (path != null) {
                name = path.substring(path.lastIndexOf("/") + 1);
            }
            name = name.substring((name.indexOf(":")) + 1);
            filename.setText(name);

            try {
                HashGenerator();
                encryptFile();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }

        } else
            Toast.makeText(UploadActivity.this, "Please select a file!", Toast.LENGTH_LONG).show();

    }

    private void HashGenerator() throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("sha-256");
        InputStream inputStream = getContentResolver().openInputStream(uri);


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

    private void UploadFile() {
        SimpleDateFormat dateFormat;

        dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        String date = dateFormat.format(calendar.getTime());

        String name1 = name.substring(0, name.indexOf("."));
        String name2 = name.substring(name.indexOf("."), name.length());
        caseFileName = name1 + "_(" + policeName + " - " + id + ")" + "_(" + date + ")" + name2;
        String caseFileName2 = name1 + "_(" + policeName + " - " + id + ")" + "_(" + date + ")";

        storageReference.child("Cases").child(txtEnterCaseId.getText().toString()).child(caseFileName).putBytes(encryptedData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UploadActivity.this, "Upload Successful", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, "Uploading Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        //name = name.substring(0, name.indexOf("."));
        //caseFileName = caseFileName.substring(0, caseFileName.indexOf("."));

        String str = secretKey.toString();
        String encoded = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);

        databaseReference.child("Hash Values").child(txtEnterCaseId.getText().toString()).child(caseFileName2).setValue(sha256Hash);
        databaseReference.child("Keys").child(txtEnterCaseId.getText().toString()).child(caseFileName2).setValue(encoded);
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

        InputStream iStream = getContentResolver().openInputStream(uri);
        byte[] inputData = getBytes(iStream);

        encryptedData = cipher.doFinal(inputData);
        /*FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(encryptedData);
        fos.close();*/

        //output_uri = Uri.fromFile(outputFile);
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;

        while ((len = inputStream.read(buffer)) != -1)
            byteArrayOutputStream.write(buffer, 0, len);

        return byteArrayOutputStream.toByteArray();
    }
}